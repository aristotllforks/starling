/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg.replay;

import static com.opengamma.bbg.replay.BloombergTick.BUID_KEY;
import static com.opengamma.bbg.replay.BloombergTick.FIELDS_KEY;
import static com.opengamma.bbg.replay.BloombergTick.RECEIVED_TS_KEY;
import static com.opengamma.bbg.replay.BloombergTick.SECURITY_KEY;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;
import org.threeten.bp.Clock;
import org.threeten.bp.Instant;

import com.bloomberglp.blpapi.CorrelationID;
import com.bloomberglp.blpapi.Session;
import com.bloomberglp.blpapi.Subscription;
import com.bloomberglp.blpapi.SubscriptionList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.bbg.BloombergConnector;
import com.opengamma.bbg.referencedata.ReferenceDataProvider;
import com.opengamma.bbg.util.BloombergDataUtils;
import com.opengamma.bbg.util.BloombergDomainIdentifierResolver;
import com.opengamma.bbg.util.SessionOptionsUtils;
import com.opengamma.id.ExternalId;
import com.opengamma.util.ArgumentChecker;

/**
 * Collects ticks from Bloomberg for later replay.
 */
public class BloombergTicksCollector implements Lifecycle {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(BloombergTicksCollector.class);

  private static final FudgeContext FUDGE_CONTEXT = new FudgeContext();

  private static final String DEFAULT_TRACK_FILE = "/watchList.txt";
  private static final int DEFAULT_SESSION_SIZE = 4;
  private static final StorageMode DEFAULT_STORAGE_MODE = StorageMode.SINGLE;

  /**
   * Default ticks root directory.
   */
  public static final String DEFAULT_ROOT_DIR = "/tickData";

  private final BloombergConnector _bloombergConnector;
  private List<Session> _sessionList = Lists.newArrayList();

  private final List<String> _options = Lists.newArrayList();
  private final AtomicBoolean _bbgSessionStarted = new AtomicBoolean();
  private final List<SubscriptionList> _subscriptionsList = Lists.newArrayList();

  private final ReferenceDataProvider _refDataProvider;
  private final String _rootDir;
  private BloombergTickWriter _ticksWriterJob;
  private Thread _ticksWriterThread;
  private final BlockingQueue<FudgeMsg> _allTicksQueue = new LinkedBlockingQueue<>();
  private final String _trackFile;
  private final int _bbgSessions;

  private final StorageMode _storageMode;

  public BloombergTicksCollector(final BloombergConnector sessionOptions, final ReferenceDataProvider refDataProvider) {
    this(sessionOptions, refDataProvider, DEFAULT_ROOT_DIR, DEFAULT_TRACK_FILE, DEFAULT_SESSION_SIZE, DEFAULT_STORAGE_MODE);
  }

  public BloombergTicksCollector(final BloombergConnector sessionOptions, final ReferenceDataProvider refDataProvider, final String rootDir) {
    this(sessionOptions, refDataProvider, rootDir, DEFAULT_TRACK_FILE, DEFAULT_SESSION_SIZE, DEFAULT_STORAGE_MODE);
  }

  /**
   * @param sessionOptions
   *          the bloomberg session options, not null.
   * @param refDataProvider
   *          the reference data provider, not null
   * @param rootDir
   *          the base directory to write ticks to, not null
   * @param trackFile
   *          the watchList file containing identifiers, not null
   * @param bbgSessions
   *          the number of bloomberg sessions to create, must be positive
   * @param storageMode
   *          the storage mode, not null
   */
  public BloombergTicksCollector(final BloombergConnector sessionOptions, final ReferenceDataProvider refDataProvider,
      final String rootDir, final String trackFile, final int bbgSessions, final StorageMode storageMode) {

    ArgumentChecker.notNull(sessionOptions, "SessionOptions");
    ArgumentChecker.notNull(refDataProvider, "BloombergRefDataProvider");
    ArgumentChecker.notNull(rootDir, "RootDir");
    ArgumentChecker.notNull(trackFile, "trackFile");
    ArgumentChecker.notNull(storageMode, "storageMode");
    if (bbgSessions < 1) {
      throw new IllegalArgumentException("Bloomberg sessions must be greater than zero");
    }
    _storageMode = storageMode;
    _bloombergConnector = sessionOptions;
    _refDataProvider = refDataProvider;
    _rootDir = rootDir;
    _trackFile = trackFile;
    _bbgSessions = bbgSessions;
    checkRootDir();
  }

  // -------------------------------------------------------------------------
  private void checkRootDir() {
    final File file = new File(_rootDir);
    if (!file.isDirectory()) {
      LOGGER.warn("{} root directory does not exist", _rootDir);
      throw new IllegalArgumentException(_rootDir + " is not a directory");
    }
    if (!file.canWrite()) {
      throw new IllegalArgumentException(" cannot write to " + _rootDir);
    }
    if (!file.canRead()) {
      throw new IllegalArgumentException(" cannot read from " + _rootDir);
    }
    if (!file.canExecute()) {
      throw new IllegalArgumentException(" cannot execute " + _rootDir);
    }
  }

  @Override
  public synchronized boolean isRunning() {
    return _bbgSessionStarted.get() || _ticksWriterThread != null && _ticksWriterThread.isAlive();
  }

  @Override
  public synchronized void start() {
    LOGGER.info("starting bloombergTickCollector");
    if (isRunning()) {
      LOGGER.info("BloombergTickStorage already started");
      return;
    }

    final Map<String, String> ticker2Buid = BloombergDataUtils.getBUID(_refDataProvider, loadSecurities());
    doSnapshot(ticker2Buid);

    // setup writer thread
    final BloombergTickWriter tickWriter = new BloombergTickWriter(FUDGE_CONTEXT, _allTicksQueue, ticker2Buid, _rootDir, _storageMode);
    final Thread thread = new Thread(tickWriter, "TicksWriter");
    thread.start();
    _ticksWriterJob = tickWriter;
    _ticksWriterThread = thread;

    createSubscriptions(ticker2Buid.keySet());
    boolean sessionCreated = false;
    try {
      sessionCreated = createSession();
    } catch (final Exception e) {
      throw new OpenGammaRuntimeException("Session cannot be open to Bloomberg", e);
    }
    _bbgSessionStarted.set(sessionCreated);
  }

  private Set<String> loadSecurities() {
    final Set<String> bloombergKeys = Sets.newHashSet();
    try {
      for (final ExternalId identifier : BloombergDataUtils.identifierLoader(new FileReader(_trackFile))) {
        bloombergKeys.add(BloombergDomainIdentifierResolver.toBloombergKey(identifier));
      }
    } catch (final FileNotFoundException ex) {
      throw new OpenGammaRuntimeException(_trackFile + " cannot be found", ex);
    }
    return bloombergKeys;
  }

  private void doSnapshot(final Map<String, String> ticker2Buid) {
    final ReferenceDataProvider refDataProvider = _refDataProvider;
    final Map<String, FudgeMsg> refDataValues = refDataProvider.getReferenceDataIgnoreCache(ticker2Buid.keySet(), BloombergDataUtils.STANDARD_FIELDS_SET);

    for (final String bloombergKey : ticker2Buid.keySet()) {
      final FudgeMsg result = refDataValues.get(bloombergKey);
      if (result == null) {
        throw new OpenGammaRuntimeException("Result for " + bloombergKey + " was not found");
      }

      final MutableFudgeMsg tickMsg = FUDGE_CONTEXT.newMessage();
      final Instant instant = Clock.systemUTC().instant();
      final long epochMillis = instant.toEpochMilli();
      tickMsg.add(RECEIVED_TS_KEY, epochMillis);
      tickMsg.add(SECURITY_KEY, bloombergKey);
      tickMsg.add(FIELDS_KEY, result);
      tickMsg.add(BUID_KEY, ticker2Buid.get(bloombergKey));
      try {
        _allTicksQueue.put(tickMsg);
      } catch (final InterruptedException ex) {
        Thread.interrupted();
        throw new OpenGammaRuntimeException("Unable to do snaphot for " + bloombergKey, ex);
      }
    }
  }

  /**
   * @param bloombergKeys
   */
  private void createSubscriptions(final Set<String> bloombergKeys) {
    LOGGER.debug("creating subscriptions list for {} securities", bloombergKeys.size());
    for (int i = 0; i < _bbgSessions; i++) {
      _subscriptionsList.add(new SubscriptionList());
    }
    int counter = 0;
    for (final String bloombergKey : bloombergKeys) {
      final int index = counter % _bbgSessions;
      final SubscriptionList subscriptions = _subscriptionsList.get(index);
      subscriptions.add(new Subscription(bloombergKey, BloombergDataUtils.STANDARD_FIELDS_LIST, _options,
          new CorrelationID(bloombergKey)));
      counter++;
    }

  }

  @Override
  public synchronized void stop() {
    LOGGER.info("Stopping marketdata storage serivce");
    stopBloombergSession();
    stopTicksWriterThreads();
  }

  /**
   *
   */
  public void stopBloombergSession() {
    LOGGER.info("stopping bloomberg session");
    if (_bbgSessionStarted.get()) {
      for (final Session session : _sessionList) {
        try {
          session.stop();
        } catch (final InterruptedException e) {
          Thread.interrupted();
          LOGGER.warn("Interrupted while waiting for session to stop", e);
        }
      }
      _sessionList = null;
      _bbgSessionStarted.set(false);
    }
  }

  /**
   *
   */
  public void stopTicksWriterThreads() {
    LOGGER.info("Stopping ticks writer thread");
    if (_ticksWriterThread != null && _ticksWriterThread.isAlive()) {
      if (_ticksWriterJob != null) {
        try {
          _allTicksQueue.put(BloombergTickReplayUtils.getTerminateMessage());
        } catch (final InterruptedException e) {
          Thread.interrupted();
          LOGGER.warn("interrupted from waiting to put terminate message on queue");
        }
      }
      try {
        _ticksWriterThread.join();
      } catch (final InterruptedException e) {
        Thread.interrupted();
        LOGGER.warn("Interrupted while waiting for ticks writer thread to terminate", e);
      }
    }
    _ticksWriterJob = null;
    _ticksWriterThread = null;
  }

  private boolean createSession() throws Exception {
    for (final Session session : _sessionList) {
      if (session != null) {
        session.stop();
      }
    }
    LOGGER.info("Connecting to {} ", SessionOptionsUtils.toString(_bloombergConnector.getSessionOptions()));
    final BloombergTickCollectorHandler handler = new BloombergTickCollectorHandler(_allTicksQueue, this);
    for (int i = 0; i < _bbgSessions; i++) {
      final Session session = new Session(_bloombergConnector.getSessionOptions(), handler);
      if (!session.start()) {
        LOGGER.info("Failed to start session");
        return false;
      }
      if (!session.openService("//blp/mktdata")) {
        LOGGER.info("Failed to open service //blp/mktdata");
        session.stop();
        return false;
      }
      _sessionList.add(session);
    }
    LOGGER.info("Connected successfully\n");

    LOGGER.info("Subscribing ...");
    int index = 0;
    for (final Session session : _sessionList) {
      session.subscribe(_subscriptionsList.get(index));
      index++;
    }
    return true;
  }

  public boolean isTickWriterAlive() {
    return _ticksWriterThread != null && _ticksWriterThread.isAlive();
  }

}
