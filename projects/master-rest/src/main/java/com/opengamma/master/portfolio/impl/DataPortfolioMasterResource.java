/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Copyright (C) 2015 - present by McLeod Moores Software Limited.
 *
 * Please see distribution for license.
 */
package com.opengamma.master.portfolio.impl;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.opengamma.core.change.DataChangeManagerResource;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.master.portfolio.PortfolioDocument;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.portfolio.PortfolioSearchRequest;
import com.opengamma.master.portfolio.PortfolioSearchResult;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.rest.AbstractDataResource;

/**
 * RESTful resource for portfolios.
 * <p>
 * The portfolios resource receives and processes RESTful calls to the portfolio master.
 */
@Path("portfolioMaster")
public class DataPortfolioMasterResource extends AbstractDataResource {

  /**
   * The portfolio master.
   */
  private final PortfolioMaster _prtMaster;

  /**
   * Creates dummy resource for the purpose of url resolution.
   *
   */
  DataPortfolioMasterResource() {
    _prtMaster = null;
  }

  /**
   * Creates the resource, exposing the underlying master over REST.
   *
   * @param portfolioMaster  the underlying portfolio master, not null
   */
  public DataPortfolioMasterResource(final PortfolioMaster portfolioMaster) {
    ArgumentChecker.notNull(portfolioMaster, "portfolioMaster");
    _prtMaster = portfolioMaster;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the portfolio master.
   *
   * @return the portfolio master, not null
   */
  public PortfolioMaster getPortfolioMaster() {
    return _prtMaster;
  }

  //-------------------------------------------------------------------------
  @GET
  public Response getHateaos(@Context final UriInfo uriInfo) {
    return hateoasResponse(uriInfo);
  }

  @HEAD
  @Path("portfolios")
  public Response status() {
    // simple HEAD to quickly return, avoiding loading the whole database
    return responseOk();
  }

  @POST
  @Path("portfolioSearches")
  public Response search(final PortfolioSearchRequest request) {
    final PortfolioSearchResult result = getPortfolioMaster().search(request);
    return responseOkObject(result);
  }

  @POST
  @Path("portfolios")
  public Response add(@Context final UriInfo uriInfo, final PortfolioDocument request) {
    final PortfolioDocument result = getPortfolioMaster().add(request);
    final URI createdUri = new DataPortfolioResource().uriVersion(uriInfo.getBaseUri(), result.getUniqueId());
    return responseCreatedObject(createdUri, result);
  }

  //-------------------------------------------------------------------------
  @Path("nodes/{nodeId}")
  public DataPortfolioNodeResource findPortfolioNode(@PathParam("nodeId") final String idStr) {
    final UniqueId id = UniqueId.parse(idStr);
    return new DataPortfolioNodeResource(this, id);
  }

  @Path("portfolios/{portfolioId}")
  public DataPortfolioResource findPortfolio(@PathParam("portfolioId") final String idStr) {
    final ObjectId id = ObjectId.parse(idStr);
    return new DataPortfolioResource(this, id);
  }

  //-------------------------------------------------------------------------
  // REVIEW jonathan 2011-12-28 -- to be removed when the change topic name is exposed as part of the component config
  @Path("portfolios/changeManager")
  public DataChangeManagerResource getChangeManager() {
    return new DataChangeManagerResource(getPortfolioMaster().changeManager());
  }

}
