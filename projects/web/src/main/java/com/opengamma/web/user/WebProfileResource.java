/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.user;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.shiro.authc.credential.PasswordService;
import org.joda.beans.impl.flexi.FlexiBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.master.user.ManageableUser;
import com.opengamma.master.user.UserForm;
import com.opengamma.master.user.UserFormError;
import com.opengamma.master.user.UserFormException;
import com.opengamma.master.user.UserMaster;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.auth.AuthUtils;
import com.opengamma.web.AbstractSingletonWebResource;
import com.opengamma.web.WebHomeUris;

/**
 * RESTful resource for the profile page.
 */
@Path("/profile")
public class WebProfileResource extends AbstractSingletonWebResource {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(WebProfileResource.class);
  /**
   * The ftl file.
   */
  private static final String PROFILE_GREEN = "users/html/profile.ftl";

  /**
   * The user master.
   */
  private final UserMaster _userMaster;
  /**
   * The password service.
   */
  private final PasswordService _pwService;

  /**
   * Creates the resource.
   *
   * @param userMaster  the user master, not null
   * @param pwService  the password service, not null
   */
  public WebProfileResource(final UserMaster userMaster, final PasswordService pwService) {
    _userMaster = ArgumentChecker.notNull(userMaster, "userMaster");
    _pwService = ArgumentChecker.notNull(pwService, "pwService");
  }

  //-------------------------------------------------------------------------
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String getGreen(
      @Context final HttpServletRequest request,
      @Context final ServletContext servletContext,
      @Context final UriInfo uriInfo) {

    final FlexiBean out = createStandardRootData(uriInfo);
    return getFreemarker(servletContext).build(PROFILE_GREEN, out);
  }

  //-------------------------------------------------------------------------
  @POST
  @Produces(MediaType.TEXT_HTML)
  public Response updateProfile(
      @Context final ServletContext servletContext,
      @Context final UriInfo uriInfo,
      @FormParam("email") final String email,
      @FormParam("displayname") final String displayName,
      @FormParam("locale") final String locale,
      @FormParam("timezone") final String zone,
      @FormParam("datestyle") final String dateStyle,
      @FormParam("timestyle") final String timeStyle) {
    try {
      final String userName = AuthUtils.getUserName();
      final ManageableUser user = _userMaster.getByName(userName);
      final UserForm form = new UserForm(user, email, displayName, locale, zone, dateStyle, timeStyle);
      form.update(_userMaster, _pwService);
      return Response.seeOther(new WebHomeUris(uriInfo).home()).build();

    } catch (final UserFormException ex) {
      ex.logUnexpected(LOGGER);
      final FlexiBean out = createRootData(uriInfo);
      out.put("email", email);
      out.put("displayname", displayName);
      out.put("locale", locale);
      out.put("timezone", zone);
      out.put("datestyle", dateStyle);
      out.put("timestyle", timeStyle);
      out.put("err", ex.getErrors().size() > 0);
      for (final UserFormError error : ex.getErrors()) {
        out.put("err_" + error.toLowerCamel(), true);
      }
      return Response.ok(getFreemarker(servletContext).build(PROFILE_GREEN, out)).build();
    }
  }

  @POST
  @Path("password")
  @Produces(MediaType.TEXT_HTML)
  public Response changePassword(
      @Context final ServletContext servletContext,
      @Context final UriInfo uriInfo,
      @FormParam("password") final String password) {
    try {
      final String userName = AuthUtils.getUserName();
      final ManageableUser user = _userMaster.getByName(userName);
      final UserForm form = new UserForm(user, password);
      form.update(_userMaster, _pwService);
      return Response.seeOther(new WebHomeUris(uriInfo).home()).build();

    } catch (final UserFormException ex) {
      ex.logUnexpected(LOGGER);
      final FlexiBean out = createStandardRootData(uriInfo);
      out.put("err", ex.getErrors().size() > 0);
      for (final UserFormError error : ex.getErrors()) {
        out.put("err_" + error.toLowerCamel(), true);
      }
      return Response.ok(getFreemarker(servletContext).build(PROFILE_GREEN, out)).build();
    }
  }

  private FlexiBean createStandardRootData(final UriInfo uriInfo) {
    final String userName = AuthUtils.getUserName();
    final ManageableUser user = _userMaster.getByName(userName);
    final FlexiBean out = createRootData(uriInfo);
    out.put("username", user.getUserName());
    out.put("email", user.getEmailAddress());
    out.put("displayname", user.getProfile().getDisplayName());
    out.put("locale", user.getProfile().getLocale().toString());
    out.put("timezone", user.getProfile().getZone().toString());
    out.put("datestyle", user.getProfile().getDateStyle().name());
    out.put("timestyle", user.getProfile().getTimeStyle().name());
    return out;
  }

  //-------------------------------------------------------------------------
  /**
   * Builds a URI for this page.
   *
   * @param uriInfo  the uriInfo, not null
   * @return the URI, not null
   */
  public static URI uri(final UriInfo uriInfo) {
    return uriInfo.getBaseUriBuilder().path(WebProfileResource.class).build();
  }

}
