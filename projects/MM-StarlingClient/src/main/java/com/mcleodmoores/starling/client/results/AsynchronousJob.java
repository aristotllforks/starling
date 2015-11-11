package com.mcleodmoores.starling.client.results;

/**
 * Interface for asynchronous analytic job.
 */
public interface AsynchronousJob extends AutoCloseable {
  /**
   * Start the job notifying the listner when the result is available.  The caller is responsible for closing the
   * job by calling close() explcitly or using in a try-with-resources block.
   * @param resultListener  the result listener, to be called when the calculation is complete, not null
   */
  void start(final ResultListener resultListener);
}
