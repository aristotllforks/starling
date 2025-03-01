/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.temporal.ChronoUnit;

import com.bloomberglp.blpapi.EventHandler;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapi.Session;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.livedata.ConnectionUnavailableException;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.OpenGammaClock;

/**
 * Supplies a Bloomberg session and service, creating the connection and managaing reconnection if necessary. The main purpose of this class is to throttle the
 * rate of attempts to reconnect. If Bloomberg is down and a connection is attempted every time a request is made the Bloomberg API runs out of memory, possibly
 * due to an unconstrained thread pool.
 * <p>
 * This class creates a session on demand but if connection fails it won't retry until a delay has elapsed to avoid overwhelming the Bloomberg API.
 */
public class SessionProvider implements Lifecycle, BloombergConnector.AvailabilityListener {

  /** The logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(SessionProvider.class);
  /** Default time in ms to wait before retrying after an unsuccessful attempt to connect. */
  private static final long DEFAULT_RETRY_DELAY = 30000;

  /** The connection details. */
  private final BloombergConnector _connector;
  /** Names of the Bloomberg service to create. */
  private final Set<String> _serviceNames = new HashSet<>();
  /** Controls access to the session. */
  private final Object _lock = new Object();
  /** Duration to wait before retrying after an unsuccessful attempt to connect. */
  private final Duration _retryDuration;

  /** The session. */
  private Session _session;
  /** The time of the last connection attempt. Also used as a lifecycle indicator - being null if the provider is not running */
  private final AtomicReference<Instant> _lastRetry = new AtomicReference<>();
  private final EventHandler _eventHandler;

  /**
   * @param connector
   *          Bloomberg connection details
   * @param serviceName
   *          Name of the service to open
   */
  public SessionProvider(final BloombergConnector connector, final String serviceName) {
    this(connector, serviceName, null);
  }

  /**
   * @param connector
   *          Bloomberg connection details
   * @param serviceName
   *          Name of the service to open
   * @param eventHandler
   *          the event handler of bloomberg session. if null session is synchronous
   */
  public SessionProvider(final BloombergConnector connector, final String serviceName, final EventHandler eventHandler) {
    this(connector, Collections.singletonList(ArgumentChecker.notNull(serviceName, "serviceName")), eventHandler);
  }

  /**
   * @param connector
   *          Bloomberg connection details
   * @param serviceNames
   *          List of service names to open, not null.
   */
  public SessionProvider(final BloombergConnector connector, final Iterable<String> serviceNames) {
    this(connector, serviceNames, null);
  }

  /**
   * @param connector
   *          Bloomberg connection details
   * @param serviceNames
   *          List of service names to open, not null.
   * @param eventHandler
   *          the event handler of bloomberg session. if null session is synchronous
   */
  public SessionProvider(final BloombergConnector connector, final Iterable<String> serviceNames, final EventHandler eventHandler) {
    this(connector, DEFAULT_RETRY_DELAY, serviceNames, eventHandler);
  }

  /**
   * @param connector
   *          Bloomberg connection details
   * @param retryDelay
   *          Time to wait between unsuccessful connection attempts
   * @param serviceNames
   *          List of service names to open, not null.
   * @param eventHandler
   *          the event handler of bloomberg session. if null session is synchronous
   */
  public SessionProvider(final BloombergConnector connector, final long retryDelay, final Iterable<String> serviceNames, final EventHandler eventHandler) {
    ArgumentChecker.notNull(connector, "connector");
    ArgumentChecker.notNull(serviceNames, "serviceNames");
    ArgumentChecker.notNull(connector.getSessionOptions(), "connector.sessionOptions");
    _retryDuration = Duration.of(retryDelay, ChronoUnit.MILLIS);
    _connector = connector;

    for (final String serviceName : serviceNames) {
      ArgumentChecker.notEmpty(serviceName, "serviceName");
      _serviceNames.add(serviceName);
    }
    _eventHandler = eventHandler;
  }

  /**
   * Returns a session, creating it if necessary. If this method is called after the provider is closed a new session will be created.
   *
   * @return The session, not null
   * @throws ConnectionUnavailableException
   *           If no connection is available
   */
  public Session getSession() {
    final Session newSession;
    synchronized (_lock) {
      if (_session != null) {
        return _session;
      }
      final Instant lastRetry = _lastRetry.get();
      if (lastRetry == null) {
        throw new ConnectionUnavailableException("Session provider has not been started");
      }
      final Instant now = OpenGammaClock.getInstance().instant();
      if (Duration.between(lastRetry, now).compareTo(_retryDuration) < 0) {
        throw new ConnectionUnavailableException("No Bloomberg connection is available");
      }
      _lastRetry.set(now);
      LOGGER.info("Bloomberg session being opened...");
      Session session = null;
      try {
        try {
          session = _connector.createOpenSession(_eventHandler);
        } catch (final OpenGammaRuntimeException e) {
          throw new ConnectionUnavailableException("Failed to open session", e);
        }
        LOGGER.info("Bloomberg session open");
        LOGGER.info("Bloomberg service being opened...");

        for (final String serviceName : _serviceNames) {
          try {
            if (!session.openService(serviceName)) {
              throw new ConnectionUnavailableException("Bloomberg service failed to start: " + serviceName);
            }
          } catch (final InterruptedException ex) {
            Thread.interrupted();
            throw new ConnectionUnavailableException("Bloomberg service failed to start: " + serviceName, ex);
          } catch (final Exception ex) {
            throw new ConnectionUnavailableException("Bloomberg service failed to start: " + serviceName, ex);
          }
          LOGGER.info("Bloomberg service open: {}", serviceName);
        }
        _session = session;
        newSession = session;
        session = null;
      } finally {
        if (session != null) {
          // If the session was started but the service not opened, then there
          // will be sockets open and threads allocated by
          // the Bloomberg API which need to be killed. Just letting the session
          // fall out of scope doesn't work (PLAT-5309)
          LOGGER.debug("Attempting to stop partially constructed session");
          try {
            session.stop();
          } catch (final Exception e) {
            LOGGER.error("Error stopping partial session", e);
          }
        }
      }
    }
    // We've got a connection open; let any other components that Bloomberg is back up
    _connector.notifyAvailabilityListeners();
    return newSession;
  }

  /**
   * @return The service, not null
   * @param serviceName
   *          the service name, not null
   * @throws ConnectionUnavailableException
   *           If no connection is available
   */
  public Service getService(final String serviceName) {
    ArgumentChecker.notNull(serviceName, "serviceName");
    ArgumentChecker.isTrue(_serviceNames.contains(serviceName), "service name is not recognized");

    return getSession().getService(serviceName);
  }

  /**
   * @return true if there is an open connection to Bloomberg.
   */
  public boolean isConnected() {
    synchronized (_lock) {
      return _session != null;
    }
  }

  /**
   * Stops the session. If {@link #getSession()} is called after this method a new session will be created.
   * <p>
   * Calling this method when there is no active session does nothing.
   */
  public void invalidateSession() {
    synchronized (_lock) {
      if (_session != null) {
        try {
          LOGGER.info("Bloomberg session being stopped...");
          _session.stop();
          LOGGER.info("Bloomberg session stopped");
        } catch (final InterruptedException e) {
          LOGGER.warn("Interrupted closing session " + _session, e);
        } finally {
          _session = null;
        }
      }
    }
  }

  // Lifecycle

  /**
   * Enables the provider, starting it so that {@link #getSession} will connect on demand to Bloomberg.
   * <p>
   * Calling this method when the provider is already started does nothing.
   */
  @Override
  public void start() {
    if (_lastRetry.compareAndSet(null, Instant.EPOCH)) {
      // First call to {@link #getSession} will now attempt to connect
      _connector.addAvailabilityListener(this);
    }
  }

  /**
   * Tests if the provider is running, that is a call has been made to {@link #start}. This does not mean that there is a connection to Bloomberg - use
   * {@link #isConnected} to test for that. If this returns false it will mean there is no active connection and none will be made by calls to
   * {@link #getSession}.
   *
   * @return true if the provider is started, false otherwise.
   */
  @Override
  public boolean isRunning() {
    return _lastRetry.get() != null;
  }

  /**
   * Disables the provider, stopping it so that any active session is closed and {@link #getSession} will no longer connect to Bloomberg.
   * <p>
   * Calling this method when the provider is not started does nothing.
   */
  @Override
  public void stop() {
    _lastRetry.set(null);
    invalidateSession();
    _connector.removeAvailabilityListener(this);
  }

  // AvailabilityListener

  @Override
  public void bloombergAvailable() {
    // If the session is already invalid the next call to {@link #getSession} will try and connect if we set the timestamp to the epoch.
    // If the session is connected (for example we triggered the notify) then we'll be set for immediate reconnection - this recovers from temporary network
    // problems
    Instant lastRetry;
    do {
      lastRetry = _lastRetry.get();
      if (lastRetry == null) {
        // Already stopped
        return;
      }
    } while (!_lastRetry.compareAndSet(lastRetry, Instant.EPOCH));
    LOGGER.info("Bloomberg connection available for {}", _serviceNames);
  }

  /**
   * Gets the connector.
   * 
   * @return the connector
   */
  public BloombergConnector getConnector() {
    return _connector;
  }

}
