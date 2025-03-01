/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.livedata.LiveDataClient;
import com.opengamma.livedata.LiveDataListener;
import com.opengamma.livedata.LiveDataSpecification;
import com.opengamma.livedata.UserPrincipal;
import com.opengamma.livedata.msg.LiveDataSubscriptionResponse;
import com.opengamma.util.ArgumentChecker;

/**
 * An implementation of {@link LiveDataClient} that delegates all calls to other clients, keyed by the external ID scheme of the items to be loaded. Where
 * requests are made that may be satisfied by any of the underlying clients, the actual underlying client that will be chosen is non-deterministic.
 */
public class DelegatingLiveDataClient implements LiveDataClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingLiveDataClient.class);
  private final Map<String, LiveDataClient> _underlyingClients = new ConcurrentSkipListMap<>();
  private LiveDataClient _defaultClient;

  /**
   * Add a live data client to the underlying client.
   *
   * @param idScheme
   *          the identifier scheme, not null
   * @param liveDataClient
   *          the client to add, not null
   */
  public void addUnderlyingClient(final String idScheme, final LiveDataClient liveDataClient) {
    ArgumentChecker.notNull(idScheme, "idScheme");
    ArgumentChecker.notNull(liveDataClient, "liveDataClient");
    _underlyingClients.put(idScheme, liveDataClient);
  }

  /**
   * Sets the default client.
   *
   * @param defaultClient
   *          the default client, not null
   */
  public void setDefaultClient(final LiveDataClient defaultClient) {
    _defaultClient = defaultClient;
  }

  /**
   * Gets the delegate client that uses the identifier scheme in the specification, or uses the default client. If the default client has not be set, an
   * exception is thrown.
   *
   * @param specification
   *          the live data specification, not null
   * @return the live data client
   */
  protected LiveDataClient identifyUnderlying(final LiveDataSpecification specification) {
    final ExternalIdBundle idBundle = specification.getIdentifiers();

    for (final ExternalId id : idBundle.getExternalIds()) {
      final LiveDataClient underlying = _underlyingClients.get(id.getScheme().getName());
      if (underlying != null) {
        LOGGER.debug("Delegating {} to {}", specification, underlying);
        return underlying;
      }
    }

    if (_defaultClient != null) {
      return _defaultClient;
    }

    throw new OpenGammaRuntimeException("No underlying client configured to handle " + specification);
  }

  /**
   * Splits the specifications into a map from client to list of specifications that that client can provide data for.
   * 
   * @param specifications
   *          the specifications, not null
   * @return a map from client to specifications
   */
  protected Map<LiveDataClient, List<LiveDataSpecification>> splitCollection(final Collection<LiveDataSpecification> specifications) {
    final Map<LiveDataClient, List<LiveDataSpecification>> result = new HashMap<>();
    for (final LiveDataSpecification specification : specifications) {
      final LiveDataClient underlying = identifyUnderlying(specification);
      List<LiveDataSpecification> perUnderlyingSpecs = result.get(underlying);
      if (perUnderlyingSpecs == null) {
        perUnderlyingSpecs = new LinkedList<>();
        result.put(underlying, perUnderlyingSpecs);
      }
      perUnderlyingSpecs.add(specification);
    }
    return result;
  }

  @Override
  public boolean isEntitled(final UserPrincipal user, final LiveDataSpecification requestedSpecification) {
    final LiveDataClient underlying = identifyUnderlying(requestedSpecification);
    return underlying.isEntitled(user, requestedSpecification);
  }

  @Override
  public Map<LiveDataSpecification, Boolean> isEntitled(final UserPrincipal user, final Collection<LiveDataSpecification> requestedSpecifications) {
    final Map<LiveDataClient, List<LiveDataSpecification>> split = splitCollection(requestedSpecifications);

    final Map<LiveDataSpecification, Boolean> result = new HashMap<>();

    for (final Map.Entry<LiveDataClient, List<LiveDataSpecification>> entry : split.entrySet()) {
      final Map<LiveDataSpecification, Boolean> perUnderlyingResult = entry.getKey().isEntitled(user, entry.getValue());
      if (perUnderlyingResult != null) {
        result.putAll(perUnderlyingResult);
      }
    }

    return result;
  }

  @Override
  public void subscribe(final UserPrincipal user, final LiveDataSpecification requestedSpecification, final LiveDataListener listener) {
    final LiveDataClient underlying = identifyUnderlying(requestedSpecification);
    underlying.subscribe(user, requestedSpecification, listener);
  }

  @Override
  public void subscribe(final UserPrincipal user, final Collection<LiveDataSpecification> requestedSpecifications, final LiveDataListener listener) {
    final Map<LiveDataClient, List<LiveDataSpecification>> split = splitCollection(requestedSpecifications);
    for (final Map.Entry<LiveDataClient, List<LiveDataSpecification>> entry : split.entrySet()) {
      entry.getKey().subscribe(user, entry.getValue(), listener);
    }
  }

  @Override
  public void unsubscribe(final UserPrincipal user, final LiveDataSpecification fullyQualifiedSpecification, final LiveDataListener listener) {
    final LiveDataClient underlying = identifyUnderlying(fullyQualifiedSpecification);
    underlying.unsubscribe(user, fullyQualifiedSpecification, listener);
  }

  @Override
  public void unsubscribe(final UserPrincipal user, final Collection<LiveDataSpecification> fullyQualifiedSpecifications, final LiveDataListener listener) {
    final Map<LiveDataClient, List<LiveDataSpecification>> split = splitCollection(fullyQualifiedSpecifications);
    for (final Map.Entry<LiveDataClient, List<LiveDataSpecification>> entry : split.entrySet()) {
      entry.getKey().unsubscribe(user, entry.getValue(), listener);
    }
  }

  @Override
  public LiveDataSubscriptionResponse snapshot(final UserPrincipal user, final LiveDataSpecification requestedSpecification, final long timeout) {
    final LiveDataClient underlying = identifyUnderlying(requestedSpecification);
    return underlying.snapshot(user, requestedSpecification, timeout);
  }

  @Override
  public Collection<LiveDataSubscriptionResponse> snapshot(final UserPrincipal user, final Collection<LiveDataSpecification> requestedSpecifications,
      final long timeout) {
    final Map<LiveDataClient, List<LiveDataSpecification>> split = splitCollection(requestedSpecifications);
    final List<LiveDataSubscriptionResponse> snapshots = new ArrayList<>(requestedSpecifications.size());
    for (final Map.Entry<LiveDataClient, List<LiveDataSpecification>> entry : split.entrySet()) {
      snapshots.addAll(entry.getKey().snapshot(user, entry.getValue(), timeout));
    }
    return snapshots;
  }

  @Override
  public String getDefaultNormalizationRuleSetId() {
    // REVIEW kirk 2012-08-17 -- This probably isn't the best behavior here.
    if (_underlyingClients.isEmpty()) {
      return null;
    }
    return _underlyingClients.values().iterator().next().getDefaultNormalizationRuleSetId();
  }

  @Override
  public void close() {
    for (final LiveDataClient underlying : _underlyingClients.values()) {
      underlying.close();
    }
  }

}
