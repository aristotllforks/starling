/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.user.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.opengamma.core.user.UserAccount;
import com.opengamma.core.user.UserSource;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.rest.AbstractDataResource;

/**
 * RESTful resource for users.
 * <p>
 * The users resource receives and processes RESTful calls to the user source.
 */
@Path("userSource")
public class DataUserSourceResource extends AbstractDataResource {

  /**
   * The user source.
   */
  private final UserSource _userSource;

  /**
   * Creates the resource, exposing the underlying source over REST.
   *
   * @param userSource  the underlying user source, not null
   */
  public DataUserSourceResource(final UserSource userSource) {
    ArgumentChecker.notNull(userSource, "userSource");
    _userSource = userSource;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the user source.
   *
   * @return the user source, not null
   */
  public UserSource getUserSource() {
    return _userSource;
  }

  //-------------------------------------------------------------------------
  @GET
  public Response getHateaos(@Context final UriInfo uriInfo) {
    return hateoasResponse(uriInfo);
  }

  //-------------------------------------------------------------------------
  @GET
  @Path("users/name/{userName}")
  public Response getAccountByName(
      @PathParam("userName") final String userName) {
    final UserAccount result = getUserSource().getAccount(userName);
    return responseOkObject(result);
  }

}
