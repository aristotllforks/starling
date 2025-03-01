/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fudgemsg.FudgeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;
import org.threeten.bp.Instant;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ExternalScheme;
import com.opengamma.livedata.LiveDataSpecification;
import com.opengamma.livedata.LiveDataValueUpdateBean;
import com.opengamma.livedata.entitlement.LiveDataEntitlementChecker;
import com.opengamma.livedata.entitlement.PermissiveLiveDataEntitlementChecker;
import com.opengamma.livedata.msg.LiveDataSubscriptionRequest;
import com.opengamma.livedata.msg.LiveDataSubscriptionResponse;
import com.opengamma.livedata.msg.LiveDataSubscriptionResponseMsg;
import com.opengamma.livedata.msg.LiveDataSubscriptionResult;
import com.opengamma.livedata.msg.SubscriptionType;
import com.opengamma.livedata.normalization.StandardRules;
import com.opengamma.livedata.permission.PermissionUtils;
import com.opengamma.livedata.resolver.DistributionSpecificationResolver;
import com.opengamma.livedata.resolver.NaiveDistributionSpecificationResolver;
import com.opengamma.livedata.server.distribution.EmptyMarketDataSenderFactory;
import com.opengamma.livedata.server.distribution.MarketDataDistributor;
import com.opengamma.livedata.server.distribution.MarketDataSenderFactory;
import com.opengamma.livedata.server.mxbean.DistributorTrace;
import com.opengamma.livedata.server.mxbean.SubscriptionTrace;
import com.opengamma.livedata.server.mxbean.SubscriptionTracer;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.PerformanceCounter;
import com.opengamma.util.PublicAPI;

import net.sf.ehcache.CacheManager;

/**
 * The base class from which most OpenGamma Live Data feed servers should extend. Handles most common cases for distributed contract management.
 */
@PublicAPI
public abstract class StandardLiveDataServer implements LiveDataServer, Lifecycle, SubscriptionTracer {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(StandardLiveDataServer.class);

  private volatile MarketDataSenderFactory _marketDataSenderFactory = new EmptyMarketDataSenderFactory();
  private final Collection<SubscriptionListener> _subscriptionListeners = new CopyOnWriteArrayList<>();

  /** Access controlled via _subscriptionLock */
  private final Set<Subscription> _currentlyActiveSubscriptions = new HashSet<>();

  /** _Write_ access controlled via _subscriptionLock */
  private final Map<String, Subscription> _securityUniqueId2Subscription = new ConcurrentHashMap<>();

  /** Access controlled via _subscriptionLock */
  private final Map<LiveDataSpecification, MarketDataDistributor> _fullyQualifiedSpec2Distributor = new HashMap<>();

  private final AtomicLong _numMarketDataUpdatesReceived = new AtomicLong(0);
  private final PerformanceCounter _performanceCounter;

  private final CacheManager _cacheManager;

  private final Lock _subscriptionLock = new ReentrantLock();

  private DistributionSpecificationResolver _distributionSpecificationResolver = new NaiveDistributionSpecificationResolver();
  private LiveDataEntitlementChecker _entitlementChecker = new PermissiveLiveDataEntitlementChecker();

  /**
   * The entitlement checker to be used for subscription requests. If null, then {@link #_entitlementChecker} will be used.
   */
  private LiveDataEntitlementChecker _subscriptionEntitlementChecker;

  private LastKnownValueStoreProvider _lkvStoreProvider = new MapLastKnownValueStoreProvider();

  private volatile ConnectionStatus _connectionStatus = ConnectionStatus.NOT_CONNECTED;

  /**
   * The subscription expiry manager
   */
  private final ExpirationManager _expirationManager = new ExpirationManager(this);

  /**
   * Creates an instance.
   *
   * @param cacheManager
   *          the cache manager, not null
   */
  protected StandardLiveDataServer(final CacheManager cacheManager) {
    this(cacheManager, true);
  }

  /**
   * Creates an instance controlling performance counting.
   * <p>
   * You may wish to disable performance counting if you expect a high rate of messages, or to process messages on several threads.
   *
   * @param cacheManager
   *          the cache manager, not null
   * @param isPerformanceCountingEnabled
   *          whether to track the message rate here, see {@link #getNumLiveDataUpdatesSentPerSecondOverLastMinute()}
   */
  protected StandardLiveDataServer(final CacheManager cacheManager, final boolean isPerformanceCountingEnabled) {
    ArgumentChecker.notNull(cacheManager, "cacheManager");
    _cacheManager = cacheManager;
    _performanceCounter = isPerformanceCountingEnabled ? new PerformanceCounter(60) : null;
  }

  // -------------------------------------------------------------------------
  /**
   * Gets the cache manager.
   *
   * @return the cache manager
   */
  public CacheManager getCacheManager() {
    return _cacheManager;
  }

  /**
   * Gets the distribution resolver.
   *
   * @return the resolver, not null
   */
  public DistributionSpecificationResolver getDistributionSpecificationResolver() {
    return _distributionSpecificationResolver;
  }

  /**
   * Sets the distribution resolver.
   *
   * @param distributionSpecificationResolver
   *          the distribution resolver, not null
   */
  public void setDistributionSpecificationResolver(final DistributionSpecificationResolver distributionSpecificationResolver) {
    ArgumentChecker.notNull(distributionSpecificationResolver, "distributionSpecificationResolver");
    _distributionSpecificationResolver = distributionSpecificationResolver;
  }

  /**
   * Returns the expiration manager used to housekeep the subscriptions.
   *
   * @return the expiration manager, not null
   */
  public ExpirationManager getExpirationManager() {
    return _expirationManager;
  }

  /**
   * Gets the market data sender factory.
   *
   * @return the factory, not null
   */
  public MarketDataSenderFactory getMarketDataSenderFactory() {
    return _marketDataSenderFactory;
  }

  /**
   * Sets the market data sender factory.
   *
   * @param marketDataSenderFactory
   *          the factory, not null
   */
  public void setMarketDataSenderFactory(final MarketDataSenderFactory marketDataSenderFactory) {
    ArgumentChecker.notNull(marketDataSenderFactory, "marketDataSenderFactory");
    _marketDataSenderFactory = marketDataSenderFactory;
  }

  /**
   * Adds a subscription listener.
   *
   * @param subscriptionListener
   *          the listener, not null
   */
  public void addSubscriptionListener(final SubscriptionListener subscriptionListener) {
    ArgumentChecker.notNull(subscriptionListener, "subscriptionListener");
    _subscriptionListeners.add(subscriptionListener);
  }

  /**
   * Sets the subscription listeners, replacing all existing ones.
   *
   * @param subscriptionListeners
   *          the listeners, not null
   */
  public void setSubscriptionListeners(final Collection<SubscriptionListener> subscriptionListeners) {
    ArgumentChecker.noNulls(subscriptionListeners, "subscriptionListeners");
    _subscriptionListeners.clear();
    for (final SubscriptionListener subscriptionListener : subscriptionListeners) {
      addSubscriptionListener(subscriptionListener);
    }
  }

  /**
   * Gets the entitlement checker.
   *
   * @return the entitlement checker, not null
   */
  public LiveDataEntitlementChecker getEntitlementChecker() {
    return _entitlementChecker;
  }

  /**
   * Sets the entitlement checker.
   *
   * @param entitlementChecker
   *          the entitlement checker, not null
   */
  public void setEntitlementChecker(final LiveDataEntitlementChecker entitlementChecker) {
    ArgumentChecker.notNull(entitlementChecker, "entitlementChecker");
    _entitlementChecker = entitlementChecker;
  }

  /**
   * Sets the entitlement checker to be used when a subscription is being made. If a checker has not been specified then the standard entitlement checker will
   * be used. If null is passed then the standard one is used.
   *
   * @param subscriptionEntitlementChecker
   *          the entitlement checker to be used for subscriptions
   */
  public void setSubscriptionEntitlementChecker(final LiveDataEntitlementChecker subscriptionEntitlementChecker) {
    _subscriptionEntitlementChecker = subscriptionEntitlementChecker;
  }

  /**
   * Gets the default normalization rule set identifier.
   *
   * @return the identifier, not null
   */
  public String getDefaultNormalizationRuleSetId() {
    return StandardRules.getOpenGammaRuleSetId();
  }

  /**
   * Gets the provider for the last known value store.
   *
   * @return the provider, not null
   */
  public LastKnownValueStoreProvider getLkvStoreProvider() {
    return _lkvStoreProvider;
  }

  /**
   * Sets the provider for the last known value store.
   *
   * @param lkvStoreProvider
   *          the provider, not null
   */
  public void setLkvStoreProvider(final LastKnownValueStoreProvider lkvStoreProvider) {
    ArgumentChecker.notNull(lkvStoreProvider, "lkvStoreProvider");
    _lkvStoreProvider = lkvStoreProvider;
  }

  // -------------------------------------------------------------------------

  /**
   * Subscribes to the specified tickers using the underlying market data provider.
   * <p>
   * This returns a map from identifier to subscription handle. The map must contain an entry for each input <code>uniqueId</code>. Failure to subscribe to any
   * <code>uniqueId</code> should result in an exception being thrown.
   *
   * @param uniqueIds
   *          the collection of identifiers to subscribe to, may be empty, not null
   * @return the subscription handles corresponding to the identifiers, not null
   * @throws RuntimeException
   *           if subscribing to any unique IDs failed
   */
  protected abstract Map<String, Object> doSubscribe(Collection<String> uniqueIds);

  /**
   * Unsubscribes to the given tickers using the underlying market data provider.
   * <p>
   * The handles returned by {@link #doSubscribe} are used to unsubscribe.
   *
   * @param subscriptionHandles
   *          the subscription handles to unsubscribe, may be empty, not null
   */
  protected abstract void doUnsubscribe(Collection<Object> subscriptionHandles);

  /**
   * Returns an image (i.e., all fields) from the underlying market data provider.
   * <p>
   * This returns a map from identifier to a message describing the fields. The map must contain an entry for each <code>uniqueId</code>. Failure to snapshot
   * any <code>uniqueId</code> should result in an exception being thrown.
   *
   * @param uniqueIds
   *          the collection of identifiers to query, may be empty, not null
   * @return the snapshot result, not null
   * @throws RuntimeException
   *           if the snapshot could not be obtained
   */
  protected abstract Map<String, FudgeMsg> doSnapshot(Collection<String> uniqueIds);

  /**
   * Gets the external scheme that defines securities for the underlying market data provider.
   *
   * @return the scheme, not null
   */
  protected abstract ExternalScheme getUniqueIdDomain();

  /**
   * Connects to the underlying market data provider. You can rely on the fact that this method is only called when getConnectionStatus() ==
   * ConnectionStatus.NOT_CONNECTED.
   */
  protected abstract void doConnect();

  /**
   * Disconnects from the underlying market data provider. You can rely on the fact that this method is only called when getConnectionStatus() ==
   * ConnectionStatus.CONNECTED.
   */
  protected abstract void doDisconnect();

  /**
   * In some cases, the underlying market data API may not, when a subscription is created, return a full image of all fields. If so, we need to get the full
   * image explicitly.
   *
   * @param subscription
   *          the subscription currently being created, not null
   * @return true if a snapshot should be made when a new subscription is created
   */
  protected abstract boolean snapshotOnSubscriptionStartRequired(Subscription subscription);

  /**
   * In some cases a subscription with no data may indicate that a snapshot will have no data.
   *
   * @param distributior
   *          the currently active distributor for the security being snapshotted, not null
   * @return true if an empty subscription indicates that the snapshot result would be empty
   */
  protected boolean canSatisfySnapshotFromEmptySubscription(final MarketDataDistributor distributior) {
    // NOTE simon 28/11/2011: Only in the case of requiring a snapshot is it safe to use an empty snapshot from a subscription,
    // since in the other case we may still be waiting for values
    return snapshotOnSubscriptionStartRequired(distributior.getSubscription());
  }

  public LiveDataEntitlementChecker getSubscriptionEntitlementChecker() {
    return _subscriptionEntitlementChecker != null
        ? _subscriptionEntitlementChecker
        : _entitlementChecker;
  }

  // -------------------------------------------------------------------------

  /**
   * Is the server connected to underlying market data API?
   */
  public enum ConnectionStatus {
    /** Connection active. */
    CONNECTED,
    /** Connection not active. */
    NOT_CONNECTED;
  }

  /**
   * Gets the current connection status.
   *
   * @return the status, not null
   */
  public ConnectionStatus getConnectionStatus() {
    return _connectionStatus;
  }

  /**
   * Sets the connection status.
   *
   * @param connectionStatus
   *          the status, not null
   */
  public void setConnectionStatus(final ConnectionStatus connectionStatus) {
    _connectionStatus = connectionStatus;
    LOGGER.info("Connection status changed to " + connectionStatus);

    if (connectionStatus == ConnectionStatus.NOT_CONNECTED) {
      for (final Subscription subscription : getSubscriptions()) {
        subscription.setHandle(null);
      }
    }
  }

  // -------------------------------------------------------------------------
  public void reestablishSubscriptions() {
    _subscriptionLock.lock();
    try {
      LOGGER.warn("Attempting to re-establish subscriptions for {} securities", _securityUniqueId2Subscription.size());

      final Set<String> securities = _securityUniqueId2Subscription.keySet();
      try {
        final Map<String, Object> subscriptions = doSubscribe(securities);

        if (securities.size() != subscriptions.size()) {
          LOGGER.warn("Attempting to re-establish security subscriptions - have {} securities "
              + "but only managed to establish subscriptions to {}",
              securities.size(), subscriptions.size());
        }

        for (final Iterator<Map.Entry<String, Subscription>> it = _securityUniqueId2Subscription.entrySet().iterator(); it.hasNext();) {
          final Map.Entry<String, Subscription> entry = it.next();
          final Object handle = subscriptions.get(entry.getKey());
          if (handle != null) {
            LOGGER.debug("Reconnected to {}", entry.getKey());
            entry.getValue().setHandle(handle);
          } else {
            LOGGER.warn("Couldn't reconnect to {} - removing from list of active subscriptions", entry.getKey());
            it.remove();
          }
        }
      } catch (final RuntimeException e) {
        LOGGER.error("Could not reestablish subscription to {}", new Object[] { securities }, e);
      }
    } finally {
      _subscriptionLock.unlock();
    }
  }

  protected void verifyConnectionOk() {
    if (getConnectionStatus() == ConnectionStatus.NOT_CONNECTED) {
      throw new IllegalStateException("Connection to market data API down");
    }
  }

  @Override
  public synchronized boolean isRunning() {
    return getConnectionStatus() == ConnectionStatus.CONNECTED;
  }

  protected void startExpirationManager() {
    getExpirationManager().start();
  }

  @Override
  public synchronized void start() {
    if (getConnectionStatus() == ConnectionStatus.NOT_CONNECTED) {
      connect();
      startExpirationManager();
    }
  }

  protected void stopExpirationManager() {
    getExpirationManager().stop();
  }

  @Override
  public synchronized void stop() {
    if (getConnectionStatus() == ConnectionStatus.CONNECTED) {
      disconnect();
      stopExpirationManager();
    }
  }

  public synchronized void connect() {
    if (getConnectionStatus() != ConnectionStatus.NOT_CONNECTED) {
      throw new IllegalStateException("Can only connect if not connected");
    }
    doConnect();
    setConnectionStatus(ConnectionStatus.CONNECTED);
  }

  public synchronized void disconnect() {
    if (getConnectionStatus() != ConnectionStatus.CONNECTED) {
      throw new IllegalStateException("Can only disconnect if connected");
    }
    doDisconnect();
    setConnectionStatus(ConnectionStatus.NOT_CONNECTED);
  }

  /**
   * @param securityUniqueId
   *          Security unique ID
   * @return A {@code LiveDataSpecification} with default normalization rule used.
   */
  public LiveDataSpecification getLiveDataSpecification(final String securityUniqueId) {
    return new LiveDataSpecification(
        getDefaultNormalizationRuleSetId(),
        ExternalId.of(getUniqueIdDomain(), securityUniqueId));
  }

  /**
   * Subscribes to the market data and creates a default distributor.
   *
   * @param securityUniqueId
   *          Security unique ID
   * @return Whether the subscription succeeded or failed
   * @see #getDefaultNormalizationRuleSetId()
   */
  public LiveDataSubscriptionResponse subscribe(final String securityUniqueId) {
    return subscribe(securityUniqueId, false);
  }

  /**
   * Subscribes to the market data and creates a default distributor.
   *
   * @param securityUniqueId
   *          Security unique ID
   * @param persistent
   *          See {@link MarketDataDistributor#isPersistent()}
   * @return Whether the subscription succeeded or failed
   * @see #getDefaultNormalizationRuleSetId()
   */
  public LiveDataSubscriptionResponse subscribe(final String securityUniqueId, final boolean persistent) {
    final LiveDataSpecification liveDataSpecification = getLiveDataSpecification(securityUniqueId);
    return subscribe(liveDataSpecification, persistent);
  }

  public LiveDataSubscriptionResponse subscribe(final LiveDataSpecification liveDataSpecificationFromClient,
      final boolean persistent) {

    final Collection<LiveDataSubscriptionResponse> results = subscribe(
        Collections.singleton(liveDataSpecificationFromClient),
        persistent);

    if (results == null || results.size() != 1) {
      final String errorMsg = "subscribe() did not fulfill its contract to populate map for each live data spec";
      return buildErrorMessageResponse(liveDataSpecificationFromClient, LiveDataSubscriptionResult.INTERNAL_ERROR, errorMsg);
    }
    final LiveDataSubscriptionResponse result = results.iterator().next();
    if (!liveDataSpecificationFromClient.equals(result.getRequestedSpecification())) {
      final String errorMsg = "Expected a subscription result for " + liveDataSpecificationFromClient
          + " but received one for " + result.getRequestedSpecification();
      return buildErrorMessageResponse(liveDataSpecificationFromClient, LiveDataSubscriptionResult.INTERNAL_ERROR, errorMsg);
    }

    return result;
  }

  public Collection<LiveDataSubscriptionResponse> subscribe(
      final Collection<LiveDataSpecification> liveDataSpecificationsFromClient, final boolean persistent) {
    ArgumentChecker.notNull(liveDataSpecificationsFromClient, "Subscriptions to be created");

    LOGGER.info("Subscribe requested for {}, persistent = {}", liveDataSpecificationsFromClient, persistent);

    verifyConnectionOk();

    final Map<ExternalIdBundle, LiveDataSubscriptionResponse> responses = new HashMap<>();
    final Map<String, Subscription> securityUniqueId2NewSubscription = new HashMap<>();
    final Map<String, LiveDataSpecification> securityUniqueId2SpecFromClient = new HashMap<>();

    _subscriptionLock.lock();
    try {
      final long distributionExpiryTime = System.currentTimeMillis() + getExpirationManager().getTimeoutExtension();
      final Map<LiveDataSpecification, DistributionSpecification> distrSpecs = getDistributionSpecificationResolver().resolve(liveDataSpecificationsFromClient);
      for (final LiveDataSpecification specFromClient : liveDataSpecificationsFromClient) {
        // this is the only place where subscribe() can 'partially' fail
        final DistributionSpecification distributionSpec = distrSpecs.get(specFromClient);
        if (distributionSpec == null) {
          LOGGER.info("Unable to work out distribution spec for specification " + specFromClient);
          responses.put(specFromClient.getIdentifiers(),
              buildErrorMessageResponse(specFromClient, LiveDataSubscriptionResult.NOT_PRESENT, "Unable to work out distribution spec"));
          continue;
        }
        final LiveDataSpecification fullyQualifiedSpec = distributionSpec.getFullyQualifiedLiveDataSpecification();
        Subscription subscription = getSubscription(fullyQualifiedSpec);
        if (subscription != null) {
          LOGGER.info("Already subscribed to {}", fullyQualifiedSpec);
          subscription.createDistributor(distributionSpec, persistent).setExpiry(distributionExpiryTime);
        } else {
          final String securityUniqueId = fullyQualifiedSpec.getIdentifier(getUniqueIdDomain());
          if (securityUniqueId == null) {
            final String errorMsg = "Qualified spec " + fullyQualifiedSpec + " does not contain ID of domain " + getUniqueIdDomain();
            responses.put(specFromClient.getIdentifiers(), buildErrorMessageResponse(specFromClient, LiveDataSubscriptionResult.INTERNAL_ERROR, errorMsg));
            continue;
          }
          subscription = new Subscription(securityUniqueId, getMarketDataSenderFactory(), getLkvStoreProvider());
          securityUniqueId2NewSubscription.put(subscription.getSecurityUniqueId(), subscription);
          securityUniqueId2SpecFromClient.put(subscription.getSecurityUniqueId(), specFromClient);
          final MarketDataDistributor distributor = subscription.createDistributor(distributionSpec, persistent);
          distributor.setExpiry(distributionExpiryTime);

          // PLAT-5958 - make a note of this here so that if another requested live data spec aliases to the same
          // subscription then we reuse this new subscription rather than creating another
          _fullyQualifiedSpec2Distributor.put(distributor.getFullyQualifiedLiveDataSpecification(), distributor);

          LOGGER.info("Created subscription for {}: {}", fullyQualifiedSpec, subscription);
        }
        responses.put(specFromClient.getIdentifiers(), buildSubscriptionResponse(specFromClient, distributionSpec));
      }

      // Allow checks here, before we do the snapshot or the subscribe
      checkSubscribe(securityUniqueId2NewSubscription.keySet());

      // In some cases, the underlying market data API may not, when the subscription is started,
      // return a full image of all fields. If so, we need to get the full image explicitly.
      final Collection<String> newSubscriptionsForWhichSnapshotIsRequired = new ArrayList<>();
      for (final Subscription subscription : securityUniqueId2NewSubscription.values()) {
        if (snapshotOnSubscriptionStartRequired(subscription)) {
          newSubscriptionsForWhichSnapshotIsRequired.add(subscription.getSecurityUniqueId());
        }
      }

      LOGGER.info("Subscription snapshot required for {}", newSubscriptionsForWhichSnapshotIsRequired);
      final Map<String, FudgeMsg> snapshots = doSnapshot(newSubscriptionsForWhichSnapshotIsRequired);
      for (final Map.Entry<String, FudgeMsg> snapshot : snapshots.entrySet()) {
        final Subscription subscription = securityUniqueId2NewSubscription.get(snapshot.getKey());
        if (snapshot.getValue() != null) {
          if (snapshot.getValue().hasField(PermissionUtils.LIVE_DATA_PERMISSION_DENIED_FIELD)) {
            final LiveDataSpecification originalSpec = securityUniqueId2SpecFromClient.get(snapshot.getKey());
            final LiveDataSubscriptionResponse errorRsp = buildErrorMessageResponse(
                originalSpec, LiveDataSubscriptionResult.NOT_AUTHORIZED,
                snapshot.getValue().getString(PermissionUtils.LIVE_DATA_PERMISSION_DENIED_FIELD));
            responses.put(originalSpec.getIdentifiers(), errorRsp);
          } else {
            subscription.initialSnapshotReceived(snapshot.getValue());
          }
        }
      }

      // Setup the subscriptions in the underlying data provider.
      for (final Subscription subscription : securityUniqueId2NewSubscription.values()) {
        // this is necessary so we don't lose any updates immediately after doSubscribe(). See AbstractLiveDataServer#liveDataReceived()
        // and how it calls AbstractLiveDataServer#getSubscription()
        _securityUniqueId2Subscription.put(subscription.getSecurityUniqueId(), subscription);
      }

      LOGGER.info("Creating underlying market data API subscription to {}", securityUniqueId2NewSubscription.keySet());
      final Map<String, Object> subscriptionHandles = doSubscribe(securityUniqueId2NewSubscription.keySet());

      // Set up data structures
      for (final Map.Entry<String, Object> subscriptionHandle : subscriptionHandles.entrySet()) {
        final String securityUniqueId = subscriptionHandle.getKey();
        final Object handle = subscriptionHandle.getValue();

        final Subscription subscription = securityUniqueId2NewSubscription.get(securityUniqueId);
        subscription.setHandle(handle);

        _currentlyActiveSubscriptions.add(subscription);

        notifySubscriptionListeners(subscription);
      }

    } catch (final RuntimeException e) {
      LOGGER.info("Unexpected exception thrown when subscribing. Cleaning up.", e);

      for (final Subscription subscription : securityUniqueId2NewSubscription.values()) {
        _securityUniqueId2Subscription.remove(subscription.getSecurityUniqueId());

        for (final MarketDataDistributor distributor : subscription.getDistributors()) {
          _fullyQualifiedSpec2Distributor.remove(distributor.getFullyQualifiedLiveDataSpecification());
        }
      }
      _currentlyActiveSubscriptions.removeAll(securityUniqueId2NewSubscription.values());

      throw e;

    } finally {
      _subscriptionLock.unlock();
    }

    // notify that subscription data structure is completely built
    subscriptionDone(securityUniqueId2NewSubscription.keySet());

    return responses.values();
  }

  private void notifySubscriptionListeners(final Subscription subscription) {
    for (final SubscriptionListener listener : _subscriptionListeners) {
      try {
        listener.subscribed(subscription);
      } catch (final RuntimeException e) {
        LOGGER.error("Listener " + listener + " subscribe failed", e);
      }
    }
  }

  /**
   * Implement necessary data flow logic after subscription is completed.
   *
   * @param subscriptions
   *          the subscriptions, not null
   */
  protected void subscriptionDone(final Set<String> subscriptions) {
    // Do nothing by default
  }

  /**
   * Check that a subscription request is valid. Will be called before any snapshot or subscribe requests for the keys
   *
   * @param uniqueIds
   *          The unique ids for which a subscribe is being requested
   */
  protected void checkSubscribe(final Set<String> uniqueIds) {
    // Do nothing by default
  }

  /**
   * Returns a snapshot of the requested market data. If the server already subscribes to the market data, the last known value from that subscription is used.
   * Otherwise a snapshot is requested from the underlying market data API.
   *
   * @param liveDataSpecificationsFromClient
   *          What snapshot(s) are being requested. Not empty
   * @return Responses to snapshot requests. Some, or even all, of them might be failures.
   * @throws RuntimeException
   *           If no snapshot could be obtained due to unexpected error.
   */
  public Collection<LiveDataSubscriptionResponse> snapshot(final Collection<LiveDataSpecification> liveDataSpecificationsFromClient) {
    ArgumentChecker.notNull(liveDataSpecificationsFromClient, "Snapshots to be obtained");

    LOGGER.info("Snapshot requested for {}", liveDataSpecificationsFromClient);

    verifyConnectionOk();

    final Collection<LiveDataSubscriptionResponse> responses = new ArrayList<>();

    final Collection<String> snapshotsToActuallyDo = new ArrayList<>();
    final Map<String, LiveDataSpecification> securityUniqueId2LiveDataSpecificationFromClient = new HashMap<>();

    final Map<LiveDataSpecification, DistributionSpecification> resolved = getDistributionSpecificationResolver().resolve(liveDataSpecificationsFromClient);
    for (final LiveDataSpecification liveDataSpecificationFromClient : liveDataSpecificationsFromClient) {
      final DistributionSpecification distributionSpec = resolved.get(liveDataSpecificationFromClient);
      final LiveDataSpecification fullyQualifiedSpec = distributionSpec.getFullyQualifiedLiveDataSpecification();

      final MarketDataDistributor currentlyActiveDistributor = getMarketDataDistributor(distributionSpec);
      if (currentlyActiveDistributor != null) {
        if (currentlyActiveDistributor.getSnapshot() != null) {
          // NOTE simon 28/11/2011: We presume that all the fields were provided in one go, all or nothing.
          LOGGER.debug("Able to satisfy {} from existing LKV", liveDataSpecificationFromClient);
          final LiveDataValueUpdateBean snapshot = currentlyActiveDistributor.getSnapshot();
          responses.add(buildSnapshotResponse(liveDataSpecificationFromClient, snapshot));
          continue;
        } else if (canSatisfySnapshotFromEmptySubscription(currentlyActiveDistributor)) {
          // BBG-91 - don't requery when an existing subscription indicates that the snapshot will fail
          LOGGER.debug("Able to satisfy failed snapshot {} from existing LKV", liveDataSpecificationFromClient);
          final String errorMsg = "Existing subscription for " + currentlyActiveDistributor.getDistributionSpec().getMarketDataId()
              + " failed to retrieve a snapshot.  Perhaps required fields are unavailable.";
          responses.add(buildErrorMessageResponse(liveDataSpecificationFromClient, LiveDataSubscriptionResult.INTERNAL_ERROR, errorMsg));
          continue;
        } else {
          LOGGER.debug("Can't use existing subscription to satisfy {} from existing LKV", liveDataSpecificationFromClient);
        }
      }

      final String securityUniqueId = fullyQualifiedSpec.getIdentifier(getUniqueIdDomain());
      if (securityUniqueId == null) {
        final String errorMsg = "Qualified spec " + fullyQualifiedSpec + " does not contain ID of domain " + getUniqueIdDomain();
        responses.add(buildErrorMessageResponse(liveDataSpecificationFromClient, LiveDataSubscriptionResult.INTERNAL_ERROR, errorMsg));
        continue;
      }

      snapshotsToActuallyDo.add(securityUniqueId);
      securityUniqueId2LiveDataSpecificationFromClient.put(securityUniqueId, liveDataSpecificationFromClient);
    }

    LOGGER.debug("Need to actually snapshot {}", snapshotsToActuallyDo);
    final Map<String, FudgeMsg> snapshots = doSnapshot(snapshotsToActuallyDo);
    for (final Map.Entry<String, FudgeMsg> snapshotEntry : snapshots.entrySet()) {
      final String securityUniqueId = snapshotEntry.getKey();
      final FudgeMsg msg = snapshotEntry.getValue();

      final LiveDataSpecification liveDataSpecFromClient = securityUniqueId2LiveDataSpecificationFromClient.get(securityUniqueId);

      final DistributionSpecification distributionSpec = resolved.get(liveDataSpecFromClient);
      final FudgeMsg normalizedMsg = distributionSpec.getNormalizedMessage(msg, securityUniqueId);
      if (normalizedMsg == null) {
        final String errorMsg = "When snapshot for " + securityUniqueId + " was run through normalization, the message disappeared. "
            + " This indicates there are buggy normalization rules in place, or that buggy (or unexpected) data was"
            + " received from the underlying market data API. Check your normalization rules. Raw, unnormalized msg = " + msg;
        responses.add(buildErrorMessageResponse(liveDataSpecFromClient, LiveDataSubscriptionResult.INTERNAL_ERROR, errorMsg));
        continue;
      }

      final LiveDataValueUpdateBean snapshot = new LiveDataValueUpdateBean(0, distributionSpec.getFullyQualifiedLiveDataSpecification(), normalizedMsg);
      responses.add(buildSnapshotResponse(liveDataSpecFromClient, snapshot));
    }

    return responses;
  }

  /**
   * If you want to force a snapshot - i.e., always a request a snapshot from the underlying API - you can use this method.
   *
   * @param securityUniqueId
   *          Security unique ID
   * @return The snapshot
   */
  public FudgeMsg doSnapshot(final String securityUniqueId) {
    final Map<String, FudgeMsg> snapshots = doSnapshot(Collections.singleton(securityUniqueId));
    final FudgeMsg snapshot = snapshots.get(securityUniqueId);
    if (snapshot == null) {
      throw new OpenGammaRuntimeException("doSnapshot() did not fulfill its contract to populate map for each unique ID");
    }
    return snapshot;
  }

  // -------------------------------------------------------------------------

  /**
   * Processes a market data subscription request by going through the steps of resolution, entitlement check, and subscription.
   *
   * @param subscriptionRequest
   *          the request from the client telling what to subscribe to, not null
   * @return the response sent back to the client of this server, not null
   */
  public LiveDataSubscriptionResponseMsg subscriptionRequestMade(final LiveDataSubscriptionRequest subscriptionRequest) {
    try {
      return subscriptionRequestMadeImpl(subscriptionRequest);

    } catch (final Exception ex) {
      LOGGER.error("Failed to subscribe to " + subscriptionRequest, ex);

      final ArrayList<LiveDataSubscriptionResponse> responses = new ArrayList<>();
      for (final LiveDataSpecification requestedSpecification : subscriptionRequest.getSpecifications()) {
        responses.add(buildErrorResponse(requestedSpecification, ex));
      }
      return new LiveDataSubscriptionResponseMsg(subscriptionRequest.getUser(), responses);
    }
  }

  /**
   * Handles a subscription request.
   *
   * @param subscriptionRequest
   *          the request, not null
   * @return the response, not null
   */
  protected LiveDataSubscriptionResponseMsg subscriptionRequestMadeImpl(final LiveDataSubscriptionRequest subscriptionRequest) {
    final boolean persistent = subscriptionRequest.getType().equals(SubscriptionType.PERSISTENT);
    final ArrayList<LiveDataSubscriptionResponse> responses = new ArrayList<>();

    // build and check the distribution specifications
    final Map<LiveDataSpecification, DistributionSpecification> distributionSpecifications = getDistributionSpecificationResolver().resolve(
        subscriptionRequest.getSpecifications());
    final ArrayList<LiveDataSpecification> distributable = new ArrayList<>();
    for (final LiveDataSpecification requestedSpecification : subscriptionRequest.getSpecifications()) {
      try {
        // Check that this spec can be found
        final DistributionSpecification spec = distributionSpecifications.get(requestedSpecification);
        if (spec == null) {
          final String errorMsg = "Could not build distribution specification for " + requestedSpecification;
          LOGGER.debug(errorMsg);
          responses.add(buildErrorMessageResponse(requestedSpecification,
              LiveDataSubscriptionResult.NOT_PRESENT,
              errorMsg));
        } else {
          distributable.add(requestedSpecification);
        }

      } catch (final Exception ex) {
        LOGGER.error("Failed to subscribe to " + requestedSpecification, ex);
        responses.add(buildErrorResponse(requestedSpecification, ex));
      }
    }

    // check entitlement and sort into snapshots/subscriptions
    final ArrayList<LiveDataSpecification> snapshots = new ArrayList<>();
    final ArrayList<LiveDataSpecification> subscriptions = new ArrayList<>();
    final Map<LiveDataSpecification, Boolean> entitled = getSubscriptionEntitlementChecker().isEntitled(subscriptionRequest.getUser(),
        distributable);
    for (final Entry<LiveDataSpecification, Boolean> entry : entitled.entrySet()) {
      final LiveDataSpecification requestedSpecification = entry.getKey();
      try {
        final Boolean entitlement = entry.getValue();
        if (!entitlement) {
          final String errorMsg = subscriptionRequest.getUser() + " is not entitled to " + requestedSpecification;
          LOGGER.info(errorMsg);
          responses.add(buildErrorMessageResponse(requestedSpecification,
              LiveDataSubscriptionResult.NOT_AUTHORIZED,
              errorMsg));
          continue;
        }

        // Pass to the right bucket by type
        if (subscriptionRequest.getType() == SubscriptionType.SNAPSHOT) {
          snapshots.add(requestedSpecification);
        } else {
          subscriptions.add(requestedSpecification);
        }

      } catch (final Exception ex) {
        LOGGER.error("Failed to subscribe to " + requestedSpecification, ex);
        responses.add(buildErrorResponse(requestedSpecification, ex));
      }
    }

    // handle snapshots
    if (!snapshots.isEmpty()) {
      try {
        responses.addAll(snapshot(snapshots));

      } catch (final Exception ex) {
        LOGGER.error("Error obtaining snapshots for {}: {}", snapshots, ex.getMessage());
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Underlying exception in snapshot error " + snapshots, ex);
        }
        // REVIEW kirk 2012-07-20 -- This doesn't really look like an InternalError,
        // but we have no way to discriminate in the response from doSnapshot at the moment.
        for (final LiveDataSpecification requestedSpecification : snapshots) {
          final String errorMsg = "Problem obtaining snapshot: " + ex.getMessage();
          responses.add(buildErrorMessageResponse(requestedSpecification,
              LiveDataSubscriptionResult.INTERNAL_ERROR,
              errorMsg));
        }
      }
    }

    // handle subscriptions
    if (!subscriptions.isEmpty()) {
      try {
        responses.addAll(subscribe(subscriptions, persistent));

      } catch (final Exception ex) {
        LOGGER.error("Error obtaining subscriptions for {}: {}",
            subscriptions,
            ex.getMessage() != null ? ex.getMessage() : ex.getClass().getName());
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Underlying exception in subscription error " + subscriptions, ex);
        }
        for (final LiveDataSpecification requestedSpecification : subscriptions) {
          responses.add(buildErrorResponse(requestedSpecification, ex));
        }
      }
    }

    return new LiveDataSubscriptionResponseMsg(subscriptionRequest.getUser(), responses);
  }

  // -------------------------------------------------------------------------

  /**
   * Unsubscribes from market data. All distributors related to that subscription will be stopped.
   *
   * @param securityUniqueId
   *          Security unique ID
   * @return true if a market data subscription was actually removed. false otherwise.
   */
  public boolean unsubscribe(final String securityUniqueId) {
    final Subscription sub = getSubscription(securityUniqueId);
    if (sub == null) {
      return false;
    }
    return unsubscribe(sub);
  }

  /**
   * Unsubscribes from market data. All distributors related to that subscription will be stopped.
   *
   * @param subscription
   *          What to unsubscribe from
   * @return true if a market data subscription was actually removed. false otherwise.
   */
  public boolean unsubscribe(final Subscription subscription) {
    ArgumentChecker.notNull(subscription, "Subscription");
    verifyConnectionOk();

    boolean actuallyUnsubscribed = false;

    _subscriptionLock.lock();
    try {
      if (isSubscribedTo(subscription)) {

        LOGGER.info("Unsubscribing from {}", subscription);

        actuallyUnsubscribed = true;

        final Object subscriptionHandle = subscription.getHandle();
        if (subscriptionHandle != null) {
          doUnsubscribe(Collections.singleton(subscriptionHandle)); // todo, optimize to use batch
        }

        _currentlyActiveSubscriptions.remove(subscription);
        _securityUniqueId2Subscription.remove(subscription
            .getSecurityUniqueId());

        for (final MarketDataDistributor distributor : subscription.getDistributors()) {
          _fullyQualifiedSpec2Distributor.remove(distributor.getFullyQualifiedLiveDataSpecification());
        }
        subscription.removeAllDistributors();

        for (final SubscriptionListener listener : _subscriptionListeners) {
          try {
            listener.unsubscribed(subscription);
          } catch (final RuntimeException e) {
            LOGGER.error("Listener unsubscribe failed", e);
          }
        }

        LOGGER.info("Unsubscribed from {}", subscription);

      } else {
        LOGGER.warn("Received unsubscription request for non-active subscription: {}", subscription);
      }

    } finally {
      _subscriptionLock.unlock();
    }

    return actuallyUnsubscribed;
  }

  /**
   * Stops a market data distributor. If the distributor is persistent, this call will be a no-op. If you want to stop a persistent distributor, make it
   * non-persistent first.
   * <p>
   * If the subscription to which the distributor belongs no longer has any active distributors after this, that subscription will be deleted.
   *
   * @param distributor
   *          The distributor to stop
   * @return true if a distributor was actually stopped. false otherwise.
   */
  public boolean stopDistributor(final MarketDataDistributor distributor) {
    ArgumentChecker.notNull(distributor, "Distributor");
    _subscriptionLock.lock();
    try {
      final MarketDataDistributor realDistributor = getMarketDataDistributor(distributor.getDistributionSpec());
      if (realDistributor != distributor) {
        return false;
      }
      if (distributor.isPersistent()) {
        return false;
      }
      distributor.getSubscription().removeDistributor(distributor);
      _fullyQualifiedSpec2Distributor.remove(distributor.getFullyQualifiedLiveDataSpecification());
      if (distributor.getSubscription().getDistributors().isEmpty()) {
        unsubscribe(distributor.getSubscription());
      }
    } finally {
      _subscriptionLock.unlock();
    }
    return true;
  }

  /**
   * Stops any expired, non-persistent, market data distributors.
   * <p>
   * This holds the subscription lock for its duration. Checking each individual distributor for expiry and then calling {@link #stopDistributor} may
   * incorrectly stop the distribution if another thread is currently subscribing to it.
   * <p>
   * This is normally called by the expiration manager.
   *
   * @return the number of expired distributors that were stopped
   */
  public int expireSubscriptions() {
    int expired = 0;
    _subscriptionLock.lock();
    try {
      for (final MarketDataDistributor distributor : new ArrayList<>(_fullyQualifiedSpec2Distributor.values())) {
        if (distributor.hasExpired()) {
          if (stopDistributor(distributor)) {
            expired++;
          }
        }
      }
    } finally {
      _subscriptionLock.unlock();
    }
    return expired;
  }

  public boolean isSubscribedTo(final String securityUniqueId) {
    return _securityUniqueId2Subscription.containsKey(securityUniqueId);
  }

  public boolean isSubscribedTo(final LiveDataSpecification fullyQualifiedSpec) {
    _subscriptionLock.lock();
    try {
      return _fullyQualifiedSpec2Distributor.containsKey(fullyQualifiedSpec);
    } finally {
      _subscriptionLock.unlock();
    }
  }

  public boolean isSubscribedTo(final Subscription subscription) {
    return getSubscriptions().contains(subscription);
  }

  public void liveDataReceived(final String securityUniqueId, final FudgeMsg liveDataFields) {
    LOGGER.debug("Live data received: {}", liveDataFields);

    _numMarketDataUpdatesReceived.incrementAndGet();
    if (_performanceCounter != null) {
      _performanceCounter.hit();
    }

    final Subscription subscription = getSubscription(securityUniqueId);
    if (subscription == null) {
      // REVIEW kirk 2013-04-26 -- Should this really be a WARN? I believe some gateway systems
      // handle unsubscribes asynchronously so it's totally valid to get a few ticks after
      // unsubscribe has pulled it out of the subscription list.
      LOGGER.warn("Unexpectedly got data for security unique ID {} - "
          + "no subscription is held for this data (has it recently expired?)", securityUniqueId);
      return;
    }

    subscription.liveDataReceived(liveDataFields);
  }

  public Set<String> getActiveDistributionSpecs() {
    final Set<String> subscriptions = new HashSet<>();
    for (final Subscription subscription : getSubscriptions()) {
      for (final DistributionSpecification distributionSpec : subscription.getDistributionSpecifications()) {
        subscriptions.add(distributionSpec.toString());
      }
    }
    return subscriptions;
  }

  public Set<String> getActiveSubscriptionIds() {
    final Set<String> subscriptions = new HashSet<>();
    for (final Subscription subscription : getSubscriptions()) {
      subscriptions.add(subscription.getSecurityUniqueId());
    }
    return subscriptions;
  }

  public int getNumActiveSubscriptions() {
    return getSubscriptions().size();
  }

  public long getNumMarketDataUpdatesReceived() {
    return _numMarketDataUpdatesReceived.get();
  }

  /**
   * @return The approximate rate of live data updates received, or -1 if tracking is disabled
   */
  public double getNumLiveDataUpdatesSentPerSecondOverLastMinute() {
    return _performanceCounter == null ? -1.0 : _performanceCounter.getHitsPerSecond();
  }

  public Set<Subscription> getSubscriptions() {
    _subscriptionLock.lock();
    try {
      return new HashSet<>(_currentlyActiveSubscriptions);
    } finally {
      _subscriptionLock.unlock();
    }
  }

  public Subscription getSubscription(final LiveDataSpecification fullyQualifiedSpec) {
    final MarketDataDistributor distributor = getMarketDataDistributor(fullyQualifiedSpec);
    if (distributor == null) {
      return null;
    }
    return distributor.getSubscription();
  }

  public Subscription getSubscription(final String securityUniqueId) {
    // NOTE: don't need lock here, map is safe, and this operation isn't really atomic anyway
    return _securityUniqueId2Subscription.get(securityUniqueId);
  }

  public MarketDataDistributor getMarketDataDistributor(final DistributionSpecification distributionSpec) {
    final Subscription subscription = getSubscription(distributionSpec.getFullyQualifiedLiveDataSpecification());
    if (subscription == null) {
      return null;
    }
    return subscription.getMarketDataDistributor(distributionSpec);
  }

  public Map<LiveDataSpecification, MarketDataDistributor> getMarketDataDistributors(final Collection<LiveDataSpecification> fullyQualifiedSpecs) {
    // NOTE: this is not much (if any) faster here, but for subclasses it can be
    _subscriptionLock.lock();
    try {
      final HashMap<LiveDataSpecification, MarketDataDistributor> hashMap = new HashMap<>();
      for (final LiveDataSpecification liveDataSpecification : fullyQualifiedSpecs) {
        hashMap.put(liveDataSpecification, _fullyQualifiedSpec2Distributor.get(liveDataSpecification));
      }
      return hashMap;
    } finally {
      _subscriptionLock.unlock();
    }
  }

  public MarketDataDistributor getMarketDataDistributor(final LiveDataSpecification fullyQualifiedSpec) {
    _subscriptionLock.lock();
    try {
      return _fullyQualifiedSpec2Distributor.get(fullyQualifiedSpec);
    } finally {
      _subscriptionLock.unlock();
    }
  }

  /**
   * This method is mainly useful in tests.
   *
   * @param securityUniqueId
   *          Security unique ID
   * @return The only market data distributor associated with the security unique ID.
   * @throws OpenGammaRuntimeException
   *           If there is no distributor associated with the given {@code securityUniqueId}, or if there is more than 1 such distributor.
   */
  public MarketDataDistributor getMarketDataDistributor(final String securityUniqueId) {
    final Subscription sub = getSubscription(securityUniqueId);
    if (sub == null) {
      throw new OpenGammaRuntimeException("Subscription " + securityUniqueId + " not found");
    }
    final Collection<MarketDataDistributor> distributors = sub.getDistributors();
    if (distributors.size() != 1) {
      throw new OpenGammaRuntimeException(distributors.size() + " distributors found for subscription " + securityUniqueId);
    }
    return distributors.iterator().next();
  }

  // -------------------------------------------------------------------------

  /**
   * Helper to build an error response.
   *
   * @param liveDataSpecificationFromClient
   *          the original specification
   * @param throwable
   *          the error, not null
   * @return the response, not null
   */
  protected LiveDataSubscriptionResponse buildErrorResponse(
      final LiveDataSpecification liveDataSpecificationFromClient, final Throwable throwable) {
    return buildErrorMessageResponse(liveDataSpecificationFromClient, LiveDataSubscriptionResult.INTERNAL_ERROR, throwable.toString());
  }

  /**
   * Helper to build an error response.
   *
   * @param liveDataSpecificationFromClient
   *          the original specification
   * @param result
   *          the result enum
   * @param message
   *          the error message, not null
   * @return the response, not null
   */
  protected LiveDataSubscriptionResponse buildErrorMessageResponse(
      final LiveDataSpecification liveDataSpecificationFromClient, final LiveDataSubscriptionResult result, final String message) {
    return new LiveDataSubscriptionResponse(liveDataSpecificationFromClient, result, message, null, null, null);
  }

  /**
   * Helper to build a snapshot response.
   *
   * @param liveDataSpecificationFromClient
   *          the original specification
   * @param snapshot
   *          the snapshot, not null
   * @return the response, not null
   */
  protected LiveDataSubscriptionResponse buildSnapshotResponse(
      final LiveDataSpecification liveDataSpecificationFromClient, final LiveDataValueUpdateBean snapshot) {
    return new LiveDataSubscriptionResponse(
        liveDataSpecificationFromClient, LiveDataSubscriptionResult.SUCCESS,
        null, snapshot.getSpecification(), null, snapshot);
  }

  /**
   * Helper to build a subscription response.
   *
   * @param liveDataSpecificationFromClient
   *          the original specification
   * @param distributionSpec
   *          the subscription, not null
   * @return the response, not null
   */
  protected LiveDataSubscriptionResponse buildSubscriptionResponse(
      final LiveDataSpecification liveDataSpecificationFromClient, final DistributionSpecification distributionSpec) {
    return new LiveDataSubscriptionResponse(
        liveDataSpecificationFromClient, LiveDataSubscriptionResult.SUCCESS,
        null, distributionSpec.getFullyQualifiedLiveDataSpecification(), distributionSpec.getJmsTopic(), null);
  }

  @Override
  public SubscriptionTrace getSubscriptionTrace(final String identifier) {

    if (_securityUniqueId2Subscription.containsKey(identifier)) {

      final Subscription sub = _securityUniqueId2Subscription.get(identifier);

      final Date creationTime = sub.getCreationTime();
      final Instant instant = Instant.ofEpochMilli(creationTime.getTime());

      final Set<DistributorTrace> distributors = new HashSet<>();

      for (final MarketDataDistributor distributor : sub.getDistributors()) {
        distributors.add(new DistributorTrace(
            distributor.getDistributionSpec().getJmsTopic(),
            Instant.ofEpochMilli(distributor.getExpiry()).toString(),
            distributor.hasExpired(),
            distributor.isPersistent(),
            distributor.getNumMessagesSent()));
      }

      return new SubscriptionTrace(identifier, instant.toString(), distributors, sub.getLiveDataHistory().getLastKnownValues().toString());
    }
    return new SubscriptionTrace(identifier);
  }
}
