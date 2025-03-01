/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.config;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.beans.impl.flexi.FlexiBean;

import com.opengamma.engine.view.ViewDefinition;
import com.opengamma.id.UniqueId;
import com.opengamma.master.config.ConfigDocument;
import com.opengamma.web.json.ViewDefinitionJSONBuilder;

/**
 * RESTful resource for a version of a config.
 */
@Path("/configs/{configId}/versions/{versionId}")
@Produces(MediaType.TEXT_HTML)
public class WebConfigVersionResource extends AbstractWebConfigResource {

  /**
   * Creates the resource.
   *
   * @param parent
   *          the parent resource, not null
   */
  public WebConfigVersionResource(final AbstractWebConfigResource parent) {
    super(parent);
  }

  // -------------------------------------------------------------------------
  @GET
  public String getHTML() {
    final FlexiBean out = createRootData();
    final ConfigDocument doc = data().getVersioned();
    out.put(CONFIG_XML, StringEscapeUtils.escapeJavaScript(createBeanXML(doc.getConfig().getValue())));
    return getFreemarker().build(HTML_DIR + "configversion.ftl", out);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getJSON(@Context final Request request) {
    final EntityTag etag = new EntityTag(data().getVersioned().getUniqueId().toString());
    final ResponseBuilder builder = request.evaluatePreconditions(etag);
    if (builder != null) {
      return builder.build();
    }
    final FlexiBean out = createRootData();
    final ConfigDocument doc = data().getVersioned();
    final String jsonConfig = toJSON(doc.getConfig().getValue());
    if (jsonConfig != null) {
      out.put("configJSON", jsonConfig);
    } else {
      out.put(CONFIG_XML, StringEscapeUtils.escapeJavaScript(createBeanXML(doc.getConfig().getValue())));
    }
    out.put("type", data().getTypeMap().inverse().get(doc.getType()));
    final String json = getFreemarker().build(JSON_DIR + "config.ftl", out);
    return Response.ok(json).tag(etag).build();
  }

  private static String toJSON(final Object config) {
    if (config.getClass().isAssignableFrom(ViewDefinition.class)) {
      return ViewDefinitionJSONBuilder.INSTANCE.toJSON((ViewDefinition) config);
    }
    return null;
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
    final ConfigDocument latestDoc = data().getConfig();
    final ConfigDocument versionedConfig = data().getVersioned();
    out.put("latestConfigDoc", latestDoc);
    out.put("latestConfig", latestDoc.getConfig().getValue());
    out.put("configDoc", versionedConfig);
    out.put("config", versionedConfig.getConfig().getValue());
    out.put("configDescription", getConfigTypesProvider().getDescription(versionedConfig.getConfig().getType()));
    out.put("configXml", createXML(versionedConfig.getConfig().getValue()));
    out.put("deleted", !latestDoc.isLatest());
    return out;
  }

  // -------------------------------------------------------------------------
  /**
   * Builds a URI for this resource.
   *
   * @param data
   *          the data, not null
   * @return the URI, not null
   */
  public static URI uri(final WebConfigData data) {
    return uri(data, null);
  }

  /**
   * Builds a URI for this resource.
   *
   * @param data
   *          the data, not null
   * @param overrideVersionId
   *          the override version id, null uses information from data
   * @return the URI, not null
   */
  public static URI uri(final WebConfigData data, final UniqueId overrideVersionId) {
    final String configId = data.getBestConfigUriId(null);
    final String versionId = StringUtils.defaultString(overrideVersionId != null ? overrideVersionId.getVersion() : data.getUriVersionId());
    return data.getUriInfo().getBaseUriBuilder().path(WebConfigVersionResource.class).build(configId, versionId);
  }

}
