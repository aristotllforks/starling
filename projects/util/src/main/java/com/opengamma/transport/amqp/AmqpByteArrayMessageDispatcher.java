/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.transport.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.opengamma.transport.ByteArrayMessageReceiver;
import com.opengamma.util.ArgumentChecker;

/**
 * Dispatcher for AMQP.
 */
public class AmqpByteArrayMessageDispatcher implements MessageListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(AmqpByteArrayMessageDispatcher.class);

  private final ByteArrayMessageReceiver _underlying;

  /**
   * Creates an instance.
   *
   * @param underlying  the underlying receiver, not null
   */
  public AmqpByteArrayMessageDispatcher(final ByteArrayMessageReceiver underlying) {
    ArgumentChecker.notNull(underlying, "underlying");
    _underlying = underlying;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the underlying receiver.
   *
   * @return the underlying receiver, not null
   */
  public ByteArrayMessageReceiver getUnderlying() {
    return _underlying;
  }

  //-------------------------------------------------------------------------
  @Override
  public void onMessage(final Message message) {
    final byte[] bytes = message.getBody();
    LOGGER.debug("Dispatching byte array of length {}", bytes.length);
    getUnderlying().messageReceived(bytes);
  }

}
