/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.analyticservice;

import java.util.Collection;
import java.util.Map;

import com.opengamma.livedata.resolver.Resolver;

/**
 * Produces a JMS topic name where to send computed value.
 */
public interface JmsTopicNameResolver extends Resolver<JmsTopicNameResolveRequest, String> {

  /** Separator for hierarchical topic names. **/
  String SEPARATOR = ".";

  /**
   * Resolves a JMS topic name.
   *
   * @param request
   *          what market data will be published
   * @return null if the JMS topic name could not be built, a valid JMS topic name otherwise.
   */
  @Override
  String resolve(JmsTopicNameResolveRequest request);

  /**
   * Resolves a JMS topic name in bulk.
   * <p>
   * This is the same as calling {@link #resolve(JmsTopicNameResolveRequest)} individually, but since it works in bulk, may be more efficient. For each input
   * request, there must be an entry in the result map.
   *
   * @param requests
   *          the set of requests, not null
   * @return map the map from request to result, not null
   */
  @Override
  Map<JmsTopicNameResolveRequest, String> resolve(Collection<JmsTopicNameResolveRequest> requests);

}
