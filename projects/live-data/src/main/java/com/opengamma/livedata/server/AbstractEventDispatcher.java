/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.livedata.ConnectionUnavailableException;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.TerminatableJob;

/**
 * A job that gets events from the underlying market data API,
 * then sends them to {@code AbstractLiveDataServer}.
 */
public abstract class AbstractEventDispatcher extends TerminatableJob {

  private static final Logger LOGGER = LoggerFactory
    .getLogger(AbstractEventDispatcher.class);

  private static final long MAX_WAIT_MILLISECONDS = 1000;
  /** Default period to wait before retrying if no connection is available. */
  private static final long RETRY_PERIOD = 30000;
  /** Period to wait before retrying if no connection is available. */
  private final long _retryPeriod;
  /** The server associated with this dispatcher. */
  private final StandardLiveDataServer _server;

  /**
   * @param server The server associated with this dispatcher
   */
  public AbstractEventDispatcher(final StandardLiveDataServer server) {
    this(server, RETRY_PERIOD);
  }

  /**
   * @param server The server associated with this dispatcher
   * @param retryPeriod Period to wait before retrying if no connection is available.
   */
  public AbstractEventDispatcher(final StandardLiveDataServer server, final long retryPeriod) {
    ArgumentChecker.notNull(server, "Live Data Server");
    _retryPeriod = retryPeriod;
    _server = server;
  }

  /**
   * @return the server
   */
  public StandardLiveDataServer getServer() {
    return _server;
  }

  @Override
  protected void runOneCycle() {
    try {
      dispatch(MAX_WAIT_MILLISECONDS);
    } catch (final ConnectionUnavailableException e) {
      LOGGER.warn("No connection to underlying data provider available, failed to dispatch. Waiting for "
                        + _retryPeriod + "ms before retrying", e);
      try {
        Thread.sleep(_retryPeriod);
      } catch (final InterruptedException e1) {
        LOGGER.warn("Interrupted waiting to retry", e1);
      }
    } catch (final RuntimeException e) {
      LOGGER.error("Failed to dispatch", e);
    }
  }

  protected void disconnected() {
    _server.setConnectionStatus(StandardLiveDataServer.ConnectionStatus.NOT_CONNECTED);
    terminate();
  }

  protected abstract void dispatch(long maxWaitMilliseconds);

}
