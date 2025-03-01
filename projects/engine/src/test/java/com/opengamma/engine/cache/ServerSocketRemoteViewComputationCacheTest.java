/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.cache;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.fudgemsg.FudgeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.id.UniqueId;
import com.opengamma.transport.socket.ServerSocketFudgeConnectionReceiver;
import com.opengamma.transport.socket.SocketFudgeConnection;
import com.opengamma.util.ThreadUtils;
import com.opengamma.util.ehcache.EHCacheUtils;
import com.opengamma.util.fudgemsg.OpenGammaFudgeContext;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.tuple.Pair;
import com.opengamma.util.tuple.Pairs;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

/**
 * A test of the remote View Computation Cache Source infrastucture operating
 * over proper sockets.
 */
@Test(groups = {TestGroup.INTEGRATION, "ehcache"})
public class ServerSocketRemoteViewComputationCacheTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerSocketRemoteViewComputationCacheTest.class);
  private static final FudgeContext FUDGE_CONTEXT = OpenGammaFudgeContext.getInstance();
  private static final int NUM_THREADS = 5;
  private static final int NUM_LOOKUPS = 1000;
  private static final int FLUSH_DELAY = 600;
  private ViewComputationCacheSource _cacheSource;
  private ServerSocketFudgeConnectionReceiver _serverSocket;
  private SocketFudgeConnection _socket;

  private CacheManager _cacheManager;

  @BeforeClass
  public void setUpClass() {
    _cacheManager = EHCacheUtils.createTestCacheManager(getClass());
  }

  @AfterClass
  public void tearDownClass() {
    EHCacheUtils.shutdownQuiet(_cacheManager);
  }

  @BeforeMethod
  public void setUp() {
    EHCacheUtils.clear(_cacheManager);
  }

  //-------------------------------------------------------------------------
  private void setupCacheSource(final boolean lazyReads, final int cacheSize, final int flushDelay) {
    final InMemoryViewComputationCacheSource cache = new InMemoryViewComputationCacheSource(FUDGE_CONTEXT);
    final ViewComputationCacheServer server = new ViewComputationCacheServer(cache);
    _serverSocket = new ServerSocketFudgeConnectionReceiver(cache.getFudgeContext(), server, Executors
        .newCachedThreadPool());
    _serverSocket.setLazyFudgeMsgReads(lazyReads);
    _serverSocket.start();
    _socket = new SocketFudgeConnection(cache.getFudgeContext());
    _socket.setFlushDelay(flushDelay);
    try {
      _socket.setInetAddress(InetAddress.getLocalHost());
    } catch (final UnknownHostException e) {
      throw new OpenGammaRuntimeException("", e);
    }
    _socket.setPortNumber(_serverSocket.getPortNumber());

    final RemoteCacheClient client = new RemoteCacheClient(_socket);
    final Configuration configuration = new Configuration();
    final CacheConfiguration cacheConfig = new CacheConfiguration();
    cacheConfig.setMaxElementsInMemory(cacheSize);
    configuration.setDefaultCacheConfiguration(cacheConfig);
    _cacheSource = new RemoteViewComputationCacheSource(client, new DefaultFudgeMessageStoreFactory(
        new InMemoryBinaryDataStoreFactory(), FUDGE_CONTEXT), _cacheManager);
  }

  private void shutDown() {
    if (_socket != null) {
      _socket.stop();
    }
    if (_serverSocket != null) {
      _serverSocket.stop();
    }
    _cacheSource = null;
    _socket = null;
    _serverSocket = null;
  }

  @SuppressWarnings("unchecked")
  private void assertLikelyValue(final Object value) {
    assertTrue(value instanceof List<?>);
    for (final Object valueElement : (List<Object>) value) {
      assertTrue(valueElement instanceof double[]);
    }
  }

  private Object createValue(final Random rand) {
    final double[][] data = new double[rand.nextInt(100)][rand.nextInt(100)];
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        data[i][j] = rand.nextDouble();
      }
    }
    return data;
  }

  private Pair<Double, Double> multiThreadedTestImpl() {
    final Random rand = new Random();
    final AtomicBoolean failed = new AtomicBoolean(false);
    final AtomicLong getTime = new AtomicLong(0);
    final AtomicLong putTime = new AtomicLong(0);
    final List<Thread> threads = new ArrayList<>();
    final UniqueId cycle0Id = UniqueId.of("Test", "MultiThreadedTestViewCycle", "0");
    final UniqueId cycle1Id = UniqueId.of("Test", "MultiThreadedTestViewCycle", "1");
    for (int i = 0; i < NUM_THREADS; i++) {
      // Half the threads on one cycle, half on another
      final UniqueId cycleId = (i & 1) == 0 ? cycle0Id : cycle1Id;
      final Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
          final ViewComputationCache cache = _cacheSource.getCache(cycleId, "default");
          try {
            long tGet = 0;
            long tPut = 0;
            for (int j = 0; j < NUM_LOOKUPS; j++) {
              final int randomValue = rand.nextInt(100);
              final String valueName = "Value" + randomValue;
              final ValueSpecification valueSpec = new ValueSpecification("Test Value",
                  ComputationTargetSpecification.of(UniqueId.of("Kirk", valueName)), ValueProperties.with(ValuePropertyNames.FUNCTION, "mockFunctionId").get());
              boolean putValue = true;
              if (j > 0) {
                // Don't try and get on the first attempt as the cache probably isn't created at the server
                tGet -= System.nanoTime();
                final Object ultimateValue = cache.getValue(valueSpec);
                tGet += System.nanoTime();
                if (ultimateValue != null) {
                  assertLikelyValue(ultimateValue);
                  putValue = rand.nextDouble() < 0.3;
                }
              }

              if (putValue) {
                final ComputedValue cv = new ComputedValue(valueSpec, createValue(rand));
                tPut -= System.nanoTime();
                cache.putSharedValue(cv);
                tPut += System.nanoTime();
              }
            }
            LOGGER.debug("Get = {}ms, Put = {}ms", tGet / 1.e6, tPut / 1.e6);
            getTime.addAndGet(tGet);
            putTime.addAndGet(tPut);
          } catch (final Throwable e) {
            LOGGER.error("Failed", e);
            failed.set(true);
          }
        }
      });
      threads.add(t);
    }
    for (final Thread t : threads) {
      t.start();
    }
    for (final Thread t : threads) {
      ThreadUtils.safeJoin(t, 10000L);
    }
    assertFalse("One thread failed. Check logs.", failed.get());
    final double get = getTime.get() / (1e6 * NUM_THREADS * NUM_LOOKUPS);
    final double put = putTime.get() / (1e6 * NUM_THREADS * NUM_LOOKUPS);
    LOGGER.info("{} get operations @ {}ms", NUM_THREADS * NUM_LOOKUPS, get);
    LOGGER.info("{} put operations @ {}ms", NUM_THREADS * NUM_LOOKUPS, put);
    return Pairs.of(get, put);
  }

  @Test
  public void multiThreadedTestLazyReadsNoCache() {
    setupCacheSource(true, 1, FLUSH_DELAY);
    multiThreadedTestImpl();
    shutDown();
  }

  @Test
  public void multiThreadedTestLazyReadsFullCache() {
    setupCacheSource(true, 100, FLUSH_DELAY);
    multiThreadedTestImpl();
    shutDown();
  }

  @Test
  public void multiThreadedTestFullReadsNoCache() {
    setupCacheSource(false, 1, FLUSH_DELAY);
    multiThreadedTestImpl();
    shutDown();
  }

  @Test
  public void multiThreadedTestFullReadsFullCache() {
    setupCacheSource(false, 100, FLUSH_DELAY);
    multiThreadedTestImpl();
    shutDown();
  }

  @Test(enabled = false)
  public void varyingFlushDelays() {
    final int[] delays = new int[] {400, 500, 600, 700};
    // Repeat to allow for timing discrepencies
    for (int i = 0; i < 5; i++) {
      final StringBuilder result = new StringBuilder();
      for (final int delay : delays) {
        setupCacheSource(true, 1, delay);
        final Pair<Double, Double> times = multiThreadedTestImpl();
        shutDown();
        result.append("\r\n").append("Delay=").append(delay).append("ms. Get=").append(times.getFirst()).append(
            "ms, Put=").append(times.getSecond()).append("ms");
      }
      LOGGER.info("{}", result);
    }
  }

}
