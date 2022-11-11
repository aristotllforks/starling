/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view.worker;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;
import org.springframework.jmx.export.MBeanExporter;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.engine.marketdata.MarketDataListener;
import com.opengamma.engine.marketdata.availability.MarketDataAvailabilityProvider;
import com.opengamma.engine.marketdata.resolver.MarketDataProviderResolver;
import com.opengamma.engine.marketdata.spec.MarketDataSpecification;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.livedata.UserPrincipal;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.NamedThreadPoolFactory;
import com.opengamma.util.monitor.OperationTimer;

import net.sf.ehcache.CacheException;

/**
 * Manages market data for a view process, taking care of subscriptions and producing snapshots.
 */
public class MarketDataManager implements MarketDataListener, Lifecycle, SubscriptionStateQuery {

  /**
   * Logger for the class.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(MarketDataManager.class);

  /**
   * Maximum number of subscriptions to make in a single request.
   */
  private static final int MAX_SUBSCRIPTION_BATCH_SIZE = 10000;

  /**
   * How long do wait for a subscription response to come back before giving up on it. Note that retries may well have occurred depending
   * on the value of {@link #SUBSCRIPTION_RETRY_DURATION}
   */
  public static final Duration SUBSCRIPTION_ABANDONMENT_DURATION = Duration.ofMinutes(15);

  /**
   * How long to wait before for a subscription response to come back before retrying a subscription. Note that the scheduler runs at this period too.
   */
  public static final Duration SUBSCRIPTION_RETRY_DURATION = Duration.ofMinutes(5);

  /**
   * Thread pool for subscription operations.
   * <p>
   * The shared "housekeeper" pool is not suitable for use by this class as it will block during the calls while subscriptions are made
   * to the underlying provider, or while waiting for said locks in order to ever do the log reporting.
   */
  private static final ScheduledExecutorService SUBMONITOR = Executors.newScheduledThreadPool(1, new NamedThreadPoolFactory("Subscription Monitor"));

  /**
   * The set of market data subscriptions which have been successfully
   * activated. The time indicates when it became active.
   */
  private final Map<ValueSpecification, ZonedDateTime> _activeSubscriptions = new HashMap<>();

  /**
   * Subscriptions which have been requested but not yet been satisfied by the
   * market data provider. The time indicates when the subscription was first requested.
   */
  private final Map<ValueSpecification, ZonedDateTime> _pendingSubscriptions = new HashMap<>();

  /**
   * Subscriptions which were requested but were failed by the market data
   * provider (they probably couldn't be found). The time indicates when the
   * failure was reported.
   */
  private final Map<ValueSpecification, ZonedDateTime> _failedSubscriptions = new HashMap<>();

  /**
   * Subscriptions which were requested but have subsequently been removed as
   * they were no longer required. The time indicates when the subscription was removed..
   */
  private final Map<ValueSpecification, ZonedDateTime> _removedSubscriptions = new HashMap<>();

  /**
   * Lock for safely adding and removing items from the subscription collections.
   */
  private final Lock _subscriptionsLock = new ReentrantLock();

  /**
   * The listener to notify of market data changes.
   */
  private final MarketDataChangeListener _marketDataChangeListener;

  /**
   * The resolver to be used for market data providers.
   */
  private final MarketDataProviderResolver _marketDataProviderResolver;

  /**
   * The market data provider(s) for the current cycles.
   */
  private SnapshottingViewExecutionDataProvider _marketDataProvider;

  /**
   * Flag indicating the market data provider has changed and any nodes sourcing market data into the dependency graph may now be invalid.
   */
  private boolean _marketDataProviderDirty;

  /**
   * Future corresponding to the scheduled logging task, if any.
   */
  private Future<?> _monitorLoggingTask;

  /**
   * Future corresponding to the scheduled monitor task, if any.
   */
  private Future<?> _monitorTask;

  /**
   * The object name against which this manager will be registered against
   * JMX. If null, then no registration will be done.
   */
  private final ObjectName _objectName;

  /**
   * THe MBean server to register the manager against. If null, then no
   * registration will be done.
   */
  private final MBeanServer _jmxServer;

  /**
   * Create the manager for the market data.
   *
   * @param listener the listener for market data changes, not null
   * @param marketDataProviderResolver the provider resolver, not null
   * @param viewProcessorName the view processor name, used for generating
   * the JMX name. If null, no JMX bean will be registered.
   * @param viewProcessId the view process id, used for generating
   * the JMX name. If null, no JMX bean will be registered.
   */
  public MarketDataManager(final MarketDataChangeListener listener,
      final MarketDataProviderResolver marketDataProviderResolver,
      final String viewProcessorName,
      final String viewProcessId) {

    ArgumentChecker.notNull(listener, "listener");
    ArgumentChecker.notNull(marketDataProviderResolver, "marketDataProviderResolver");
    _marketDataChangeListener = listener;
    _marketDataProviderResolver = marketDataProviderResolver;

    _objectName = viewProcessorName != null && viewProcessId != null
        ? createObjectName(viewProcessId)
            : null;

        _jmxServer = setupJmxServer();
        registerJmx();
  }

  private MBeanServer setupJmxServer() {
    try {
      return ManagementFactory.getPlatformMBeanServer();
    } catch (final SecurityException e) {
      LOGGER.warn("No permissions for platform MBean server - JMX will not be available", e);
      return null;
    }
  }

  /**
   * Creates an object name using the scheme "com.opengamma:type=View,ViewProcessor=<viewProcessorName>,name=<viewProcessId>"
   */
  private ObjectName createObjectName(final String viewProcessId) {
    try {
      return new ObjectName("com.opengamma:type=ViewProcessor,ViewProcesses=ViewProcesses,name=ViewProcessMarketData " + viewProcessId);
    } catch (final MalformedObjectNameException e) {
      throw new CacheException(e);
    }
  }

  private void registerJmx() {

    if (_objectName != null && _jmxServer != null) {
      unregisterFromJmx();
      final MBeanExporter exporter = new MBeanExporter();
      exporter.setServer(_jmxServer);
      exporter.registerManagedResource(this, _objectName);
    }
  }

  private void unregisterFromJmx() {
    if (_objectName != null && _jmxServer != null && _jmxServer.isRegistered(_objectName)) {
      try {
        _jmxServer.unregisterMBean(_objectName);
      } catch (InstanceNotFoundException | MBeanRegistrationException e) {
        LOGGER.warn("Unable to unregister object: {} from MBeanServer", _objectName);
      }
    }
  }

  private Runnable createSubscriptionMonitorLogging() {
    return new Runnable() {

      @Override
      public void run() {
        _subscriptionsLock.lock();
        try {
          LOGGER.info("Pending subscriptions for {}: {}", MarketDataManager.this, _pendingSubscriptions.size());
          if (!_pendingSubscriptions.isEmpty()) {
            LOGGER.info(_pendingSubscriptions.size() > 20 ? "First 20 pending: " : "All {} pending: ", _pendingSubscriptions.size());
            for (final Map.Entry<ValueSpecification, ZonedDateTime> entry : Iterables.limit(_pendingSubscriptions.entrySet(), 20)) {
              LOGGER.info(" - {} in cache since: {}", entry.getKey(), entry.getValue());
            }
          }
        } finally {
          _subscriptionsLock.unlock();
        }
      }
    };
  }

  private Runnable createSubscriptionMonitor() {
    return new Runnable() {
      @Override
      public void run() {
        _subscriptionsLock.lock();
        try {
          final ZonedDateTime abandonLimit = ZonedDateTime.now().minus(SUBSCRIPTION_ABANDONMENT_DURATION);
          final ZonedDateTime retryLimit = ZonedDateTime.now().minus(SUBSCRIPTION_RETRY_DURATION);
          final Set<ValueSpecification> toAbandon = new HashSet<>();
          final Set<ValueSpecification> toRetry = new HashSet<>();
          for (final Map.Entry<ValueSpecification, ZonedDateTime> entry : _pendingSubscriptions.entrySet()) {
            if (entry.getValue().isBefore(abandonLimit)) {
              toAbandon.add(entry.getKey());
            } else if (entry.getValue().isBefore(retryLimit)) {
              toRetry.add(entry.getKey());
            }
            // else it's on the pending list but we're still waiting for it to come back
            // so do nothing
          }
          if (!toAbandon.isEmpty()) {
            LOGGER.warn("Giving up waiting on {} subscriptions - maybe they don't exist", toAbandon.size());
            LOGGER.info("No longer waiting for subscriptions: {}", toAbandon);
            removePendingSubscriptions(toAbandon, false);
          }
          if (!toRetry.isEmpty()) {
            LOGGER.info("Retrying {} subscriptions as no responses received yet", toRetry.size());
            LOGGER.info("Retrying subscriptions: {}", toRetry);
            makeSubscriptionRequest(toRetry);
          }
        } finally {
          _subscriptionsLock.unlock();
        }
      }
    };
  }

  /**
   * Create a snapshot manager for use in the current cycle.
   *
   * @param marketDataUser the market data user, not null
   * @param marketDataSpecifications the market data required for the cycle (and hence to be included in any snapshot created), not null
   * @return new snapshot manager, not null
   */
  public SnapshotManager createSnapshotManagerForCycle(UserPrincipal marketDataUser, final List<MarketDataSpecification> marketDataSpecifications) {
    ArgumentChecker.notNull(marketDataUser, "marketDataUser");
    ArgumentChecker.notNull(marketDataSpecifications, "marketDataSpecifications");
    marketDataUser = ensureUserNameNonEmpty(marketDataUser);
    _subscriptionsLock.lock();
    try {
      if (_marketDataProvider == null || !_marketDataProvider.getSpecifications().equals(marketDataSpecifications)) {
        replaceMarketDataProvider(marketDataUser, marketDataSpecifications);
        if (_marketDataProvider == null) {
          throw new OpenGammaRuntimeException("Market data specifications " + marketDataSpecifications + "invalid");
        }
      }
      // Obtain the snapshot in case it is needed, but don't explicitly initialise it until the data is required
      return new SnapshotManager(_marketDataProvider.snapshot(), this);
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  /**
   * Return the current market data provider.
   *
   * @return the current market data provider, may be null
   */
  public SnapshottingViewExecutionDataProvider getMarketDataProvider() {
    _subscriptionsLock.lock();
    try {
      return _marketDataProvider;
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  @Override
  public void subscriptionsSucceeded(final Collection<ValueSpecification> valueSpecifications) {
    removePendingSubscriptions(valueSpecifications, true);
    LOGGER.info("{} subscription succeeded - {} pending subscriptions remaining", valueSpecifications.size(), _pendingSubscriptions.size());
  }

  @Override
  public void subscriptionFailed(final ValueSpecification valueSpecification, final String msg) {
    removePendingSubscriptions(ImmutableSet.of(valueSpecification), false);
    LOGGER.info("Market data subscription to {} failed. This market data may be missing from computation cycles. Reason: {}", valueSpecification, msg);
    LOGGER.info("{} pending subscriptions remaining", _pendingSubscriptions.size());
  }

  @Override
  public void subscriptionStopped(final ValueSpecification valueSpecification) {
    // No-op
  }

  @Override
  public void valuesChanged(final Collection<ValueSpecification> specifications) {
    LOGGER.debug("Received change notification for {} specifications", specifications.size());
    _marketDataChangeListener.onMarketDataValuesChanged(specifications);
  }

  private void makeSubscriptionRequest(final Set<ValueSpecification> requiredSubscriptions) {
    for (final Set<ValueSpecification> batch : partitionSet(requiredSubscriptions, MAX_SUBSCRIPTION_BATCH_SIZE)) {
      _marketDataProvider.subscribe(batch);
    }
  }

  private <T> Set<Set<T>> partitionSet(final Set<T> originalSet, final int maxBatchSize) {
    ArgumentChecker.notNegativeOrZero(maxBatchSize, "maxBatchSize");
    final int expectedNumberOfBatches = originalSet.size() / maxBatchSize + 1;
    final Set<Set<T>> result = new HashSet<>(expectedNumberOfBatches);
    Set<T> batch = null;
    for (final T item : originalSet) {
      if (batch == null) {
        batch = new HashSet<>(maxBatchSize);
        result.add(batch);
      }
      batch.add(item);
      if (batch.size() == maxBatchSize) {
        batch = null;
      }
    }
    LOGGER.info("Partitioned original set of {} items into {} batches of max size {}", originalSet.size(), result.size(), maxBatchSize);
    return result;
  }

  private void removePendingSubscriptions(final Collection<ValueSpecification> specifications,
      final boolean subscriptionSucceeded) {
    _subscriptionsLock.lock();
    try {
      for (final ValueSpecification specification : specifications) {
        final boolean expected = _pendingSubscriptions.remove(specification) != null;
        if (expected && subscriptionSucceeded) {
          _activeSubscriptions.put(specification, ZonedDateTime.now());
        } else if (!subscriptionSucceeded) {
          // Even it wasn't expected because something else triggered the subscription, use the information to mark any
          // active subscription as failed
          if (_activeSubscriptions.remove(specification) != null || expected) {
            _failedSubscriptions.put(specification, ZonedDateTime.now());
          }
        }

      }
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  private void removeMarketDataSubscriptions(final Set<ValueSpecification> unusedSubscriptions) {
    final OperationTimer timer = new OperationTimer(LOGGER, "Removing {} market data subscriptions", unusedSubscriptions.size());
    _subscriptionsLock.lock();
    try {
      _marketDataProvider.unsubscribe(unusedSubscriptions);
      final ZonedDateTime removalTime = ZonedDateTime.now();
      for (final ValueSpecification subscription : unusedSubscriptions) {
        if (!_removedSubscriptions.containsKey(subscription)) {
          _activeSubscriptions.remove(subscription);
          _pendingSubscriptions.remove(subscription);
          _failedSubscriptions.remove(subscription);
          _removedSubscriptions.put(subscription, removalTime);
        }
      }
    } finally {
      _subscriptionsLock.unlock();
    }
    timer.finished();
  }

  /**
   * Replace the market data provider if required. It will be replaced if it is not already setup or if the user has changed.
   *
   * @param marketDataUser the market data user, not null
   */
  public void replaceMarketDataProviderIfRequired(UserPrincipal marketDataUser) {
    ArgumentChecker.notNull(marketDataUser, "marketDataUser");
    marketDataUser = ensureUserNameNonEmpty(marketDataUser);
    _subscriptionsLock.lock();
    try {
      if (_marketDataProvider != null && !_marketDataProvider.getMarketDataUser().equals(marketDataUser)) {
        // [PLAT-3186] Not a huge overhead, but we could check compatability with the new specs and keep the same provider
        replaceMarketDataProvider(marketDataUser, _marketDataProvider.getSpecifications());
      }
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  private void replaceMarketDataProvider(final UserPrincipal marketDataUser, final List<MarketDataSpecification> specifications) {
    // Lock held by calling methods
    if (_marketDataProvider != null) {
      LOGGER.info("Replacing market data provider between cycles");
    }
    removeMarketDataProvider();
    setMarketDataProvider(marketDataUser, specifications);
  }

  /**
   * Removes the current market data provider and all market data subscriptions.
   */
  private void removeMarketDataProvider() {
    _subscriptionsLock.lock();
    try {
      if (_marketDataProvider == null) {
        return;
      }
      removeMarketDataSubscriptions(ImmutableSet.copyOf(_activeSubscriptions.keySet()));
      removeMarketDataSubscriptions(ImmutableSet.copyOf(_pendingSubscriptions.keySet()));
      _marketDataProvider.removeListener(this);
      _marketDataProvider = null;
      _marketDataProviderDirty = true;
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  private void setMarketDataProvider(final UserPrincipal marketDataUser, final List<MarketDataSpecification> marketDataSpecs) {
    // Lock held by calling methods
    try {
      _marketDataProvider = new SnapshottingViewExecutionDataProvider(marketDataUser, marketDataSpecs, _marketDataProviderResolver);
    } catch (final Exception e) {
      LOGGER.error("Failed to create data provider", e);
      _marketDataProvider = null;
    }
    if (_marketDataProvider != null) {
      _marketDataProvider.addListener(this);
    }
    _marketDataProviderDirty = true;
  }

  /**
   * Request subscriptions for required market data. The request is checked against current
   * subscriptions ensuring subscriptions are only sent for new requests. Any previously
   * requested subscriptions that are no longer required will be removed.
   *
   * @param requiredSubscriptions the required subscriptions, not null
   */
  public void requestMarketDataSubscriptions(final Set<ValueSpecification> requiredSubscriptions) {

    ArgumentChecker.notNull(requiredSubscriptions, "requiredSubscriptions");
    final Set<ValueSpecification> newSubscriptions = manageOngoingSubscriptions(requiredSubscriptions);

    // As the market data provider calls back to ALL listeners (i.e. potentially multiple
    // views), we need to make the subscription request outside the scope of the subscriptions
    // lock otherwise we can hit deadlocks when two similar views (e.g. same view, different
    // valuation times) are launched.
    if (!newSubscriptions.isEmpty()) {
      final OperationTimer timer = new OperationTimer(LOGGER, "Adding {} market data subscriptions", newSubscriptions.size());
      makeSubscriptionRequest(newSubscriptions);
      timer.finished();
    }
  }

  /**
   * Checks the required subscriptions against the currently held subscriptions, removing ones that are
   * no longer required and returning the set of new subscriptions that are required.
   *
   * @param requiredSubscriptions the current set of required subscriptions (some of
   * which may already be subscribed)
   * @return the set of new subscriptions required
   */
  private Set<ValueSpecification> manageOngoingSubscriptions(final Set<ValueSpecification> requiredSubscriptions) {
    _subscriptionsLock.lock();
    try {
      final Set<ValueSpecification> currentSubscriptions =
          ImmutableSet.<ValueSpecification>builder()
          .addAll(_activeSubscriptions.keySet())
          .addAll(_pendingSubscriptions.keySet())
          .addAll(_failedSubscriptions.keySet())
          .build();

      final Set<ValueSpecification> unusedSubscriptions = Sets.difference(currentSubscriptions, requiredSubscriptions).immutableCopy();
      if (!unusedSubscriptions.isEmpty()) {
        LOGGER.info("{} unused market data subscriptions", unusedSubscriptions.size());
        removeMarketDataSubscriptions(unusedSubscriptions);
      }

      final Set<ValueSpecification> newMarketData = Sets.difference(requiredSubscriptions, currentSubscriptions).immutableCopy();
      if (!newMarketData.isEmpty()) {
        LOGGER.info("{} new market data requirements", newMarketData.size());
        final ZonedDateTime now = ZonedDateTime.now();
        for (final ValueSpecification specification : newMarketData) {
          _pendingSubscriptions.put(specification, now);
          _failedSubscriptions.remove(specification);
          _removedSubscriptions.remove(specification);
        }
      }
      return newMarketData;
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  @Override
  public int retryFailedSubscriptions() {
    final Set<ValueSpecification> subscriptions = new HashSet<>(_failedSubscriptions.size());
    _subscriptionsLock.lock();
    try {
      final ZonedDateTime now = ZonedDateTime.now();
      for (final Iterator<ValueSpecification> it = _failedSubscriptions.keySet().iterator(); it.hasNext();) {
        final ValueSpecification specification = it.next();
        it.remove();
        subscriptions.add(specification);
        _pendingSubscriptions.put(specification, now);
      }
    } finally {
      _subscriptionsLock.unlock();
    }
    if (!subscriptions.isEmpty()) {
      final OperationTimer timer = new OperationTimer(LOGGER, "Retrying {} market data subscriptions which have previously failed", subscriptions.size());
      _marketDataProvider.unsubscribe(subscriptions);
      makeSubscriptionRequest(subscriptions);
      timer.finished();
    }
    return subscriptions.size();
  }

  /**
   * Shortcut method to get the availability provider from the market data provider.
   *
   * @return the market data availability provider, may be null.
   */
  public MarketDataAvailabilityProvider getAvailabilityProvider() {
    _subscriptionsLock.lock();
    try {
      return _marketDataProvider != null ? _marketDataProvider.getAvailabilityProvider() : null;
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  /**
   * Indicates if the current market data provider is dirty and thus any nodes sourcing market data into the dependency graph may now be invalid.
   *
   * @return true if the market data provider is dirty
   */
  public boolean isMarketDataProviderDirty() {
    _subscriptionsLock.lock();
    try {
      return _marketDataProviderDirty;
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  /**
   * Marks the current market data provider as clean.
   */
  public void markMarketDataProviderClean() {
    _subscriptionsLock.lock();
    try {
      _marketDataProviderDirty = false;
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  /**
   * Switches {@link UserPrincipal#getTestUser()} for the given userPrincipal if {@link UserPrincipal#getUserName()} is null or empty.
   *
   * @param userPrincipal the object to check
   * @return the resolved object
   */
  private UserPrincipal ensureUserNameNonEmpty(final UserPrincipal userPrincipal) {
    final String userName = userPrincipal.getUserName();
    if (userName == null || "".equals(userName)) {
      final UserPrincipal testUser = UserPrincipal.getTestUser();
      LOGGER.info("UserName undefined for {}. Will use test user {} instead.", userPrincipal, UserPrincipal.getTestUser());
      return testUser;
    }
    return userPrincipal;
  }

  // Lifecycle

  @Override
  public synchronized void start() {
    LOGGER.debug("Starting {}", this);
    if (_monitorLoggingTask != null) {
      if (LOGGER.isInfoEnabled()) {
        _monitorLoggingTask = SUBMONITOR.scheduleAtFixedRate(createSubscriptionMonitorLogging(), 0, 15, TimeUnit.SECONDS);
      }
    }
    if (_monitorTask != null) {
      _monitorTask = SUBMONITOR.scheduleAtFixedRate(createSubscriptionMonitor(), 0, SUBSCRIPTION_RETRY_DURATION.get(ChronoUnit.SECONDS), TimeUnit.SECONDS);
    }
  }

  @Override
  public synchronized void stop() {
    LOGGER.debug("Stopping {}", this);
    if (_monitorLoggingTask != null) {
      _monitorLoggingTask.cancel(true);
      _monitorLoggingTask = null;
    }
    if (_monitorTask != null) {
      _monitorTask.cancel(true);
      _monitorTask = null;
    }

    unregisterFromJmx();

    // removeMarketDataProvider may block until the lock can be obtained; post it to the detach queue instead rather than
    // block the caller
    SUBMONITOR.submit(new Runnable() {
      @Override
      public void run() {
        removeMarketDataProvider();
      }
    });
  }

  @Override
  public synchronized boolean isRunning() {
    return _monitorTask != null;
  }

  @Override
  public Map<String, SubscriptionStatus> queryFailedSubscriptions() {
    return querySubscriptions(_failedSubscriptions, SubscriptionState.FAILED);
  }

  @Override
  public Map<String, SubscriptionStatus> queryPendingSubscriptions() {
    return querySubscriptions(_pendingSubscriptions, SubscriptionState.PENDING);
  }

  @Override
  public Map<String, SubscriptionStatus> queryRemovedSubscriptions() {
    return querySubscriptions(_removedSubscriptions, SubscriptionState.REMOVED);
  }

  @Override
  public Map<String, SubscriptionStatus> queryActiveSubscriptions() {
    return querySubscriptions(_activeSubscriptions, SubscriptionState.ACTIVE);
  }

  private Map<String, SubscriptionStatus> querySubscriptions(final Map<ValueSpecification, ZonedDateTime> subscriptions,
      final SubscriptionState state) {
    // We need the lock as we'll get confused if the collections change underneath our feet
    _subscriptionsLock.lock();
    try {
      return createStateMap(null, subscriptions, state);
    } finally {
      _subscriptionsLock.unlock();
    }
  }

  private Map<String, SubscriptionStatus> createStateMap(final String ticker,
      final Map<ValueSpecification, ZonedDateTime> subscriptions,
      final SubscriptionState state) {

    final Map<String, SubscriptionStatus> results = new HashMap<>();

    for (final Map.Entry<ValueSpecification, ZonedDateTime> entry : subscriptions.entrySet()) {

      // As the ticker could be in the properties or the target spec, just search the whole string
      final String fullSpec = entry.getKey().toString();

      if (ticker == null || ticker.equals("") || fullSpec.contains(ticker)) {
        results.put(fullSpec, new SubscriptionStatus(state, entry.getValue()));
      }
    }

    return results;
  }

  @Override
  public int getFailedSubscriptionCount() {
    return _failedSubscriptions.size();
  }

  @Override
  public int getPendingSubscriptionCount() {
    return _pendingSubscriptions.size();
  }

  @Override
  public int getRemovedSubscriptionCount() {
    return _removedSubscriptions.size();
  }

  @Override
  public int getActiveSubscriptionCount() {
    return _activeSubscriptions.size();
  }

  /**
   * Extract the state of subscriptions which contain the requested ticker. As there is
   * some cost involved in doing this extract and filter, this is not exposed as an attribute.
   *
   * @param ticker the ticker to search for
   * @return a map of matching tickers and the current state of subscription for each
   */
  @Override
  public Map<String, SubscriptionStatus> querySubscriptionState(final String ticker) {

    // We need the lock as we'll get confused if the collections change underneath our feet
    _subscriptionsLock.lock();
    try {
      final Map<String, SubscriptionStatus> results = new HashMap<>();

      results.putAll(createStateMap(ticker, _activeSubscriptions, SubscriptionState.ACTIVE));
      results.putAll(createStateMap(ticker, _pendingSubscriptions, SubscriptionState.PENDING));
      results.putAll(createStateMap(ticker, _failedSubscriptions, SubscriptionState.FAILED));
      results.putAll(createStateMap(ticker, _removedSubscriptions, SubscriptionState.REMOVED));

      return results;

    } finally {
      _subscriptionsLock.unlock();
    }
  }

  /**
   * Represents the state of a particular subscription and the time
   * when it moved to that state. Implemented using Strings so that
   * it is displayable via JMX.
   */
  public class SubscriptionStatus {

    /**
     * The state of the subscription. Maps to the the values in
     * the {@link SubscriptionStatus} enum.
     */
    private final String _state;

    /**
     * The time (as an ISO-format String) when the subscription moved
     * to this state.
     */
    private final String _timestamp;

    /**
     * Create a new subscription state.
     *
     * @param state the state of the subscription
     * @param time the time the subscription got the state
     */
    public SubscriptionStatus(final SubscriptionState state, final ZonedDateTime time) {
      _state = state.name();
      _timestamp = time.toString();
    }

    /**
     * Return the state of the subscription.
     *
     * @return the state of the subscription
     */
    public String getState() {
      return _state;
    }

    /**
     * Return the timestamp when the subscription moved to this state.
     *
     * @return the timestamp when the subscription moved to this state
     */
    public String getTimestamp() {
      return _timestamp;
    }
  }
}
