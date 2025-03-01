/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Copyright (C) 2015 - present by McLeod Moores Software Limited.
 *
 * Please see distribution for license.
 */
package com.opengamma.master.exchange.impl;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.opengamma.id.ObjectId;
import com.opengamma.master.exchange.ExchangeDocument;
import com.opengamma.master.exchange.ExchangeMaster;
import com.opengamma.master.exchange.ExchangeSearchRequest;
import com.opengamma.master.exchange.ExchangeSearchResult;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.rest.AbstractDataResource;

/**
 * RESTful resource for exchanges.
 * <p>
 * The exchanges resource receives and processes RESTful calls to the exchange master.
 */
@Path("exchangeMaster")
public class DataExchangeMasterResource extends AbstractDataResource {

  /**
   * The exchange master.
   */
  private final ExchangeMaster _exgMaster;

  /**
   * Creates the resource, exposing the underlying master over REST.
   *
   * @param exchangeMaster  the underlying exchange master, not null
   */
  public DataExchangeMasterResource(final ExchangeMaster exchangeMaster) {
    ArgumentChecker.notNull(exchangeMaster, "exchangeMaster");
    _exgMaster = exchangeMaster;
  }

  //-------------------------------------------------------------------------

  /**
   * Gets the exchange master.
   *
   * @return the exchange master, not null
   */
  public ExchangeMaster getExchangeMaster() {
    return _exgMaster;
  }

  //-------------------------------------------------------------------------
  @GET
  public Response getHateaos(@Context final UriInfo uriInfo) {
    return hateoasResponse(uriInfo);
  }

  @HEAD
  @Path("exchanges")
  public Response status() {
    // simple HEAD to quickly return, avoiding loading the whole database
    return responseOk();
  }

  @POST
  @Path("exchangeSearches")
  public Response search(final ExchangeSearchRequest request) {
    final ExchangeSearchResult result = getExchangeMaster().search(request);
    return responseOkObject(result);
  }

  @POST
  @Path("exchanges")
  public Response add(@Context final UriInfo uriInfo, final ExchangeDocument request) {
    final ExchangeDocument result = getExchangeMaster().add(request);
    final URI createdUri = new DataExchangeResource().uriVersion(uriInfo.getBaseUri(), result.getUniqueId());
    return responseCreatedObject(createdUri, result);
  }

  //-------------------------------------------------------------------------
  @Path("exchanges/{exchangeId}")
  public DataExchangeResource findExchange(@PathParam("exchangeId") final String idStr) {
    final ObjectId id = ObjectId.parse(idStr);
    return new DataExchangeResource(this, id);
  }

}
