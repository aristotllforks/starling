/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.calcnode;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.threeten.bp.Instant;

import com.opengamma.engine.cache.CacheSelectHint;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.async.Cancelable;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.test.Timeout;

/**
 *
 */
@Test(groups = TestGroup.INTEGRATION)
public class JobDispatcherTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(JobDispatcherTest.class);
  private static final long TIMEOUT = Timeout.standardTimeoutMillis();

  private final ExecutorService _executorService = Executors.newCachedThreadPool();

  private static final AtomicLong JOB_ID = new AtomicLong();

  protected static CalculationJobSpecification createTestJobSpec() {
    return new CalculationJobSpecification(UniqueId.of("Test", "ViewCycle"), "default", Instant.now(), JOB_ID.incrementAndGet());
  }

  protected static List<CalculationJobItem> createTestJobItems() {
    return Collections.emptyList();
  }

  protected static CalculationJob createTestJob() {
    return new CalculationJob(createTestJobSpec(), 0L, VersionCorrection.LATEST, null, createTestJobItems(), CacheSelectHint.allPrivate());
  }

  protected static CalculationJobResult createTestJobResult(final CalculationJobSpecification jobSpec, final long time, final String nodeId) {
    return new CalculationJobResult(jobSpec, time, new ArrayList<CalculationJobResultItem>(), nodeId);
  }

  private class TestJobInvoker extends AbstractJobInvoker {

    private JobInvokerRegister _callback;
    private boolean _disabled;

    public TestJobInvoker(final String nodeId) {
      super(nodeId);
    }

    @Override
    public boolean invoke(final CalculationJob job, final JobInvocationReceiver receiver) {
      if (_disabled) {
        return false;
      }
      _executorService.execute(new Runnable() {
        @Override
        public void run() {
          receiver.jobCompleted(createTestJobResult(job.getSpecification(), 0, getInvokerId()));
        }
      });
      return true;
    }

    @Override
    public boolean notifyWhenAvailable(final JobInvokerRegister callback) {
      _callback = callback;
      return false;
    }

  }

  @Test
  public void registerInvokerWithJobPending() {
    LOGGER.info("registerInvokerWithJobPending");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    final TestJobResultReceiver result = new TestJobResultReceiver();
    final CalculationJob job = createTestJob();
    jobDispatcher.dispatchJob(job, result);
    assertNull(result.getResult());
    final TestJobInvoker jobInvoker = new TestJobInvoker("Test");
    jobDispatcher.registerJobInvoker(jobInvoker);
    final CalculationJobResult jobResult = result.waitForResult(TIMEOUT);
    assertNotNull(jobResult);
    assertEquals(job.getSpecification(), jobResult.getSpecification());
    assertNull(jobInvoker._callback);
  }

  @Test
  public void registerInvokerWithEmptyQueue() {
    LOGGER.info("registerInvokerWithEmptyQueue");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    final TestJobInvoker jobInvoker = new TestJobInvoker("Test");
    jobDispatcher.registerJobInvoker(jobInvoker);
    final TestJobResultReceiver result = new TestJobResultReceiver();
    final CalculationJob job = createTestJob();
    jobDispatcher.dispatchJob(job, result);
    final CalculationJobResult jobResult = result.waitForResult(TIMEOUT);
    assertNotNull(jobResult);
    assertEquals(job.getSpecification(), jobResult.getSpecification());
    assertNull(jobInvoker._callback);
  }

  private void nodeTest(final String expectedNodeId, final JobDispatcher jobDispatcher) {
    final TestJobResultReceiver result = new TestJobResultReceiver();
    final CalculationJob job = createTestJob();
    jobDispatcher.dispatchJob(job, result);
    final CalculationJobResult jobResult = result.waitForResult(TIMEOUT);
    assertNotNull(jobResult);
    assertEquals(job.getSpecification(), jobResult.getSpecification());
    assertEquals(expectedNodeId, jobResult.getComputeNodeId());
  }

  @Test
  public void invokeInRoundRobinOrder() {
    LOGGER.info("invokeInRoundRobinOrder");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    final TestJobInvoker node1 = new TestJobInvoker("1");
    final TestJobInvoker node2 = new TestJobInvoker("2");
    final TestJobInvoker node3 = new TestJobInvoker("3");
    jobDispatcher.registerJobInvoker(node1);
    jobDispatcher.registerJobInvoker(node2);
    jobDispatcher.registerJobInvoker(node3);
    nodeTest("1", jobDispatcher);
    assertNull(node1._callback);
    nodeTest("2", jobDispatcher);
    assertNull(node2._callback);
    nodeTest("3", jobDispatcher);
    assertNull(node3._callback);
    node1._disabled = true;
    nodeTest("2", jobDispatcher);
    assertNotNull(node1._callback);
    assertNull(node2._callback);
  }

  @Test
  public void saturateInvokers() {
    LOGGER.info("saturateInvokers");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    final JobInvoker[] jobInvokers = new JobInvoker[3];
    for (int i = 0; i < jobInvokers.length; i++) {
      jobDispatcher.registerJobInvoker(new AbstractJobInvoker("" + (i + 1)) {

        private final Random _rnd = new Random();
        private boolean _busy;
        private JobInvokerRegister _callback;

        @Override
        public boolean invoke(final CalculationJob job, final JobInvocationReceiver receiver) {
          final JobInvoker instance = this;
          synchronized (instance) {
            if (_busy) {
              return false;
            }
            _executorService.execute(new Runnable() {
              @Override
              public void run() {
                try {
                  Thread.sleep(_rnd.nextInt(50));
                } catch (final InterruptedException e) {
                  LOGGER.warn("invoker {} interrupted", getInvokerId());
                }
                LOGGER.debug("invoker {} completed job {}", getInvokerId(), job.getSpecification());
                receiver.jobCompleted(createTestJobResult(job.getSpecification(), 0L, instance.toString()));
                synchronized (instance) {
                  _busy = false;
                  if (_callback != null) {
                    LOGGER.debug("re-registering invoker {} with dispatcher", getInvokerId());
                    final JobInvokerRegister callback = _callback;
                    _callback = null;
                    callback.registerJobInvoker(instance);
                  } else {
                    LOGGER.debug("invoker {} completed job without notify", getInvokerId());
                  }
                }
              }
            });
            _busy = true;
            return true;
          }
        }

        @Override
        public boolean notifyWhenAvailable(final JobInvokerRegister callback) {
          synchronized (this) {
            if (_busy) {
              assertNull(_callback);
              LOGGER.debug("invoker {} busy - storing callback", getInvokerId());
              _callback = callback;
              return false;
            }
            LOGGER.debug("invoker {} ready - immediate callback", getInvokerId());
            return true;
          }
        }

      });
    }
    final CalculationJob[] jobs = new CalculationJob[100];
    final TestJobResultReceiver[] resultReceivers = new TestJobResultReceiver[jobs.length];
    LOGGER.debug("Dispatching {} jobs to {} nodes", jobs.length, jobInvokers.length);
    for (int i = 0; i < jobs.length; i++) {
      jobs[i] = createTestJob();
      resultReceivers[i] = new TestJobResultReceiver();
      jobDispatcher.dispatchJob(jobs[i], resultReceivers[i]);
    }
    LOGGER.debug("Jobs dispatched");
    for (int i = 0; i < jobs.length; i++) {
      LOGGER.debug("Waiting for result {}", i);
      final CalculationJobResult result = resultReceivers[i].waitForResult(TIMEOUT * 2);
      assertNotNull(result);
      assertEquals(jobs[i].getSpecification(), result.getSpecification());
    }
    LOGGER.debug("All jobs completed");
  }

  private class FailingJobInvoker extends AbstractJobInvoker {

    private int _failureCount;

    public FailingJobInvoker() {
      super("Failing");
    }

    @Override
    public boolean invoke(final CalculationJob job, final JobInvocationReceiver receiver) {
      _executorService.execute(new Runnable() {
        @Override
        public void run() {
          LOGGER.debug("Failing job {}", job.getSpecification());
          _failureCount++;
          receiver.jobFailed(FailingJobInvoker.this, "Fail", null);
        }
      });
      return true;
    }

    @Override
    public boolean notifyWhenAvailable(final JobInvokerRegister callback) {
      // shouldn't get called
      Assert.fail();
      return true;
    }

  }

  @Test
  public void testJobRetry_failure() {
    LOGGER.info("testJobRetry_failure");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    final TestJobResultReceiver result = new TestJobResultReceiver();
    final FailingJobInvoker failingInvoker = new FailingJobInvoker();
    jobDispatcher.registerJobInvoker(failingInvoker);
    final CalculationJob job = createTestJob();
    jobDispatcher.dispatchJob(job, result);
    final CalculationJobResult jobResult = result.waitForResult(TIMEOUT);
    assertNotNull(jobResult);
    assertEquals(2, failingInvoker._failureCount); // once failed twice at one node, job is aborted
    assertEquals(JobDispatcher.DEFAULT_JOB_FAILURE_NODE_ID, jobResult.getComputeNodeId());
  }

  @Test
  public void testJobRetry_success() {
    LOGGER.info("testJobRetry_sucess");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    final TestJobResultReceiver result = new TestJobResultReceiver();
    final FailingJobInvoker failingInvoker = new FailingJobInvoker();
    final TestJobInvoker workingInvoker = new TestJobInvoker("Test");
    jobDispatcher.registerJobInvoker(failingInvoker);
    jobDispatcher.registerJobInvoker(workingInvoker);
    final CalculationJob job = createTestJob();
    jobDispatcher.dispatchJob(job, result);
    final CalculationJobResult jobResult = result.waitForResult(TIMEOUT);
    assertNotNull(jobResult);
    assertEquals(1, failingInvoker._failureCount); // fail once at this node, then retried on another
    assertEquals("Test", jobResult.getComputeNodeId());
    assertEquals(job.getSpecification(), jobResult.getSpecification());
  }

  private final class BlockingJobInvoker extends AbstractJobInvoker {

    private final long _waitFor;
    private AtomicInteger _isAlive;
    private boolean _cancelled;

    private BlockingJobInvoker(final long waitFor) {
      super("blocking");
      _waitFor = waitFor;
    }

    @Override
    public Collection<Capability> getCapabilities() {
      return Collections.emptySet();
    }

    @Override
    public boolean invoke(final CalculationJob job, final JobInvocationReceiver receiver) {
      _executorService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(_waitFor);
          } catch (final InterruptedException e) {
          }
          receiver.jobCompleted(createTestJobResult(job.getSpecification(), 0, getInvokerId()));
        }
      });
      return true;
    }

    @Override
    public boolean isAlive(final Collection<CalculationJobSpecification> jobs) {
      if (_isAlive != null) {
        _isAlive.incrementAndGet();
        return true;
      }
      return false;
    }

    @Override
    public void cancel(final Collection<CalculationJobSpecification> jobs) {
      _cancelled = true;
    }

    public boolean isCancelled() {
      return _cancelled;
    }

    @Override
    public boolean notifyWhenAvailable(final JobInvokerRegister callback) {
      // Shouldn't get called
      Assert.fail();
      return false;
    }

  }

  @Test(invocationCount = 5, successPercentage = 19)
  public void testJobTimeoutFailure() {
    LOGGER.info("testJobTimeoutFailure");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    try {
      jobDispatcher.setMaxJobExecutionTime(TIMEOUT);
      jobDispatcher.setMaxJobAttempts(1);
      final TestJobResultReceiver result = new TestJobResultReceiver();
      jobDispatcher.dispatchJob(createTestJob(), result);
      assertNull(result.getResult());
      final BlockingJobInvoker blockingInvoker = new BlockingJobInvoker(2 * TIMEOUT);
      jobDispatcher.registerJobInvoker(blockingInvoker);
      final CalculationJobResult jobResult = result.waitForResult(2 * TIMEOUT);
      assertNotNull(jobResult);
      assertEquals(jobDispatcher.getJobFailureNodeId(), jobResult.getComputeNodeId());
    } finally {
      jobDispatcher.getJobTimeoutExecutor().shutdownNow();
    }
  }

  @Test(invocationCount = 5, successPercentage = 19)
  public void testJobTimeoutSuccess() {
    LOGGER.info("testJobTimeoutSuccess");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    try {
      jobDispatcher.setMaxJobExecutionTime(3 * TIMEOUT);
      jobDispatcher.setMaxJobAttempts(1);
      final TestJobResultReceiver result = new TestJobResultReceiver();
      jobDispatcher.dispatchJob(createTestJob(), result);
      assertNull(result.getResult());
      final BlockingJobInvoker blockingInvoker = new BlockingJobInvoker(TIMEOUT);
      jobDispatcher.registerJobInvoker(blockingInvoker);
      final CalculationJobResult jobResult = result.waitForResult(2 * TIMEOUT);
      assertNotNull(jobResult);
      assertEquals(blockingInvoker.getInvokerId(), jobResult.getComputeNodeId());
    } finally {
      jobDispatcher.getJobTimeoutExecutor().shutdownNow();
    }
  }

  @Test(invocationCount = 5, successPercentage = 19)
  public void testJobTimeoutQuerySuccess() {
    LOGGER.info("testJobTimeoutQuerySuccess");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    try {
      jobDispatcher.setMaxJobExecutionTime(4 * TIMEOUT);
      jobDispatcher.setMaxJobExecutionTimeQuery(TIMEOUT);
      jobDispatcher.setMaxJobAttempts(1);
      final TestJobResultReceiver result = new TestJobResultReceiver();
      jobDispatcher.dispatchJob(createTestJob(), result);
      assertNull(result.getResult());
      final BlockingJobInvoker blockingInvoker = new BlockingJobInvoker(2 * TIMEOUT);
      blockingInvoker._isAlive = new AtomicInteger();
      jobDispatcher.registerJobInvoker(blockingInvoker);
      final CalculationJobResult jobResult = result.waitForResult(3 * TIMEOUT);
      assertNotNull(jobResult);
      assertEquals(blockingInvoker.getInvokerId(), jobResult.getComputeNodeId());
      assertTrue(blockingInvoker._isAlive.get() > 0);
    } finally {
      jobDispatcher.getJobTimeoutExecutor().shutdownNow();
    }
  }

  @Test(invocationCount = 5, successPercentage = 19)
  public void testJobTimeoutQueryFailure() {
    LOGGER.info("testJobTimeoutQueryFailure");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    try {
      jobDispatcher.setMaxJobExecutionTime(3 * TIMEOUT);
      jobDispatcher.setMaxJobExecutionTimeQuery(TIMEOUT);
      jobDispatcher.setMaxJobAttempts(1);
      final TestJobResultReceiver result = new TestJobResultReceiver();
      jobDispatcher.dispatchJob(createTestJob(), result);
      assertNull(result.getResult());
      final BlockingJobInvoker blockingInvoker = new BlockingJobInvoker(2 * TIMEOUT);
      jobDispatcher.registerJobInvoker(blockingInvoker);
      final CalculationJobResult jobResult = result.waitForResult(3 * TIMEOUT);
      assertNotNull(jobResult);
      assertEquals(jobDispatcher.getJobFailureNodeId(), jobResult.getComputeNodeId());
    } finally {
      jobDispatcher.getJobTimeoutExecutor().shutdownNow();
    }
  }

  @Test(invocationCount = 5, successPercentage = 19)
  public void testJobCancel() {
    LOGGER.info("testJobCancel");
    final JobDispatcher jobDispatcher = new JobDispatcher();
    try {
      jobDispatcher.setMaxJobExecutionTime(2 * TIMEOUT);
      jobDispatcher.setMaxJobAttempts(1);
      final TestJobResultReceiver result = new TestJobResultReceiver();
      final Cancelable job = jobDispatcher.dispatchJob(createTestJob(), result);
      assertNotNull(job);
      assertNull(result.getResult());
      final BlockingJobInvoker blockingInvoker = new BlockingJobInvoker(TIMEOUT);
      jobDispatcher.registerJobInvoker(blockingInvoker);
      assertTrue(job.cancel(false));
      assertTrue(blockingInvoker.isCancelled());
    } finally {
      jobDispatcher.getJobTimeoutExecutor().shutdownNow();
    }
  }

}
