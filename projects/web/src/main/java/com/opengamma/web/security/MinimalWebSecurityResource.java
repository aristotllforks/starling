/**
 * Copyright (C) 2015 - present by McLeod Moores Software Limited
 * Modified from APLv2 code Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
package com.opengamma.web.security;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.beans.Bean;
import org.joda.beans.impl.flexi.FlexiBean;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesInfoSearchRequest;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesInfoSearchResult;
import com.opengamma.master.security.ManageableSecurity;
import com.opengamma.master.security.SecurityDocument;
import com.opengamma.master.security.SecurityLoaderRequest;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.util.JodaBeanSerialization;
import com.opengamma.web.FreemarkerCustomRenderer;

/**
 * RESTful resource for a security.
 */
@Path("/securities/{securityId}")
public class MinimalWebSecurityResource extends AbstractMinimalWebSecurityResource {

  /**
   * Creates the resource.
   *
   * @param parent
   *          the parent resource, not null
   */
  public MinimalWebSecurityResource(final AbstractMinimalWebSecurityResource parent) {
    super(parent);
  }

  // -------------------------------------------------------------------------
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String getHTML() {
    final FlexiBean out = createRootData();
    return getFreemarker().build(HTML_DIR + "security.ftl", out);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getJSON() {
    final FlexiBean out = createRootData();
    return getFreemarker().build(JSON_DIR + getFreemarkerTemplateName(), out);
  }

  private String getFreemarkerTemplateName() {
    final SecurityDocument doc = data().getSecurity();
    final ManageableSecurity security = doc.getSecurity();
    String result = "default-security.ftl";
    if (security instanceof FinancialSecurity) {
      final FinancialSecurity financialSec = (FinancialSecurity) security;
      final String templateName = financialSec.accept(getTemplateProvider());
      if (templateName != null) {
        result = templateName;
      }
    } else {
      return security.getSecurityType().replace("_", "-").toLowerCase().concat(".ftl");
    }
    return result;
  }

  // -------------------------------------------------------------------------
  @PUT
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response putHTML(@FormParam("type") final String type, @FormParam(SECURITY_XML) final String securityXml) {

    final SecurityDocument doc = data().getSecurity();
    if (!doc.isLatest()) {
      return Response.status(Status.FORBIDDEN).entity(getHTML()).build();
    }
    URI responseURI = null;
    final String trimmedType = StringUtils.defaultString(StringUtils.trimToNull(type), "");
    switch (trimmedType) {
      case "xml":
        final String trimmedSecurityXml = StringUtils.trimToNull(securityXml);
        try {
          responseURI = updateSecurity(trimmedSecurityXml);
        } catch (final Exception ex) {
          final FlexiBean out = createRootData();
          out.put("err_securityXml", true);
          out.put("err_securityXmlMsg", ex.getMessage());
          out.put(SECURITY_XML, StringEscapeUtils.escapeJavaScript(trimmedSecurityXml));
          final String html = getFreemarker().build(HTML_DIR + "security-update.ftl", out);
          return Response.ok(html).build();
        }
        break;
      case "id":
        responseURI = updateSecurity(doc);
        break;
      default:
        throw new IllegalArgumentException("Can only update security by XML or ID");
    }
    return Response.seeOther(responseURI).build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response putJSON(@FormParam("type") final String type, @FormParam(SECURITY_XML) final String securityXml) {
    final SecurityDocument doc = data().getSecurity();
    if (!doc.isLatest()) { // TODO: idempotent
      return Response.status(Status.FORBIDDEN).entity(getHTML()).build();
    }

    final String trimmedType = StringUtils.defaultString(StringUtils.trimToNull(type), "");
    switch (trimmedType) {
      case "xml":
        final String trimmedSecurityXml = StringUtils.trimToNull(securityXml);
        updateSecurity(trimmedSecurityXml);
        break;
      case "": // update security by ID if type is missing
      case "id":
        updateSecurity(doc);
        break;
      default:
        throw new IllegalArgumentException("Can only update security by XML or ID");
    }
    return Response.ok().build();
  }

  private URI updateSecurity(final String securityXml) {
    final Bean securityBean = JodaBeanSerialization.deserializer().xmlReader().read(securityXml);
    final SecurityMaster securityMaster = data().getSecurityMaster();
    final ManageableSecurity manageableSecurity = (ManageableSecurity) securityBean;

    final SecurityDocument updatedSecDoc = securityMaster.update(new SecurityDocument(manageableSecurity));
    final MinimalWebSecuritiesUris webSecuritiesUris = new MinimalWebSecuritiesUris(data());
    return webSecuritiesUris.security(updatedSecDoc.getSecurity());
  }

  private URI updateSecurity(final SecurityDocument doc) {
    final ExternalIdBundle identifierBundle = doc.getSecurity().getExternalIdBundle();
    final SecurityLoaderRequest request = SecurityLoaderRequest.create(identifierBundle);
    request.setForceUpdate(true);
    data().getSecurityLoader().loadSecurities(request); // ignore errors
    return MinimalWebSecurityResource.uri(data());
  }

  // -------------------------------------------------------------------------
  @DELETE
  @Produces(MediaType.TEXT_HTML)
  public Response deleteHTML() {
    final SecurityDocument doc = data().getSecurity();
    if (!doc.isLatest()) {
      return Response.status(Status.FORBIDDEN).entity(getHTML()).build();
    }
    data().getSecurityMaster().remove(doc.getUniqueId());
    final URI uri = MinimalWebSecurityResource.uri(data());
    return Response.seeOther(uri).build();
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteJSON() {
    final SecurityDocument doc = data().getSecurity();
    if (doc.isLatest()) { // idempotent DELETE
      data().getSecurityMaster().remove(doc.getUniqueId());
    }
    return Response.ok().build();
  }

  // -------------------------------------------------------------------------
  /**
   * Creates the output root data.
   *
   * @return the output root data, not null
   */
  @Override
  protected FlexiBean createRootData() {
    final FlexiBean out = super.createRootData();
    final SecurityDocument securityDoc = data().getSecurity();
    final ManageableSecurity security = securityDoc.getSecurity();

    // REVIEW jonathan 2012-01-12 -- we are throwing away any adjuster that may be required, e.g. to apply
    // normalisation to the time-series. This reproduces the previous behaviour but probably indicates that the
    // time-series information is in the wrong place.

    // Get the last price HTS for the security
    ObjectId tsObjectId = null;
    if (data().getHistoricalTimeSeriesMaster() != null) {
      final HistoricalTimeSeriesInfoSearchRequest searchRequest = new HistoricalTimeSeriesInfoSearchRequest(security.getExternalIdBundle());
      final HistoricalTimeSeriesInfoSearchResult searchResult = data().getHistoricalTimeSeriesMaster().search(searchRequest);
      if (searchResult.getFirstInfo() != null) {
        tsObjectId = searchResult.getFirstInfo().getUniqueId().getObjectId();
      }
    }
    out.put("securityAttributes", security.getAttributes());
    out.put("securityDoc", securityDoc);
    out.put("security", security);
    out.put("timeSeriesId", tsObjectId);
    out.put("deleted", !securityDoc.isLatest());
    addSecuritySpecificMetaData(security, out);
    out.put("customRenderer", FreemarkerCustomRenderer.INSTANCE);
    out.put(SECURITY_XML, StringEscapeUtils.escapeJavaScript(createBeanXML(security)));
    return out;
  }

  // -------------------------------------------------------------------------
  @Path("versions")
  public MinimalWebSecurityVersionsResource findVersions() {
    return new MinimalWebSecurityVersionsResource(this);
  }

  // -------------------------------------------------------------------------
  /**
   * Builds a URI for this resource.
   *
   * @param data
   *          the data, not null
   * @return the URI, not null
   */
  public static URI uri(final WebSecuritiesData data) {
    return uri(data, null);
  }

  /**
   * Builds a URI for this resource.
   *
   * @param data
   *          the data, not null
   * @param overrideSecurityId
   *          the override security id, null uses information from data
   * @return the URI, not null
   */
  public static URI uri(final WebSecuritiesData data, final UniqueId overrideSecurityId) {
    final String securityId = data.getBestSecurityUriId(overrideSecurityId);
    return data.getUriInfo().getBaseUriBuilder().path(MinimalWebSecurityResource.class).build(securityId);
  }

}
