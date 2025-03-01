/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.security.auditlog;

import java.util.List;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeMsgEnvelope;

import com.opengamma.transport.BatchFudgeMessageReceiver;
import com.opengamma.transport.FudgeMessageReceiver;
import com.opengamma.util.ArgumentChecker;

/**
 * A Fudge message receiver that parses audit log messages from {@link DistributedAuditLogger}
 * and passes them onto a delegate audit logger for persisting into a database.
 */
public class DistributedAuditLoggerServer implements FudgeMessageReceiver, BatchFudgeMessageReceiver {

  private final AbstractAuditLogger _delegate;

  /**
   * Constructs an instance.
   *
   * @param delegate  the delegate, not null
   */
  public DistributedAuditLoggerServer(final AbstractAuditLogger delegate) {
    ArgumentChecker.notNull(delegate, "Delegate audit logger");
    _delegate = delegate;
  }

  @Override
  public void messageReceived(final FudgeContext fudgeContext, final FudgeMsgEnvelope msgEnvelope) {
    // Note - this means that the timestamp in the log will be a server timestamp...
    final AuditLogEntry auditLogEntry = AuditLogEntry.fromFudgeMsg(msgEnvelope.getMessage());
    _delegate.log(auditLogEntry.getUser(),
        auditLogEntry.getOriginatingSystem(),
        auditLogEntry.getObject(),
        auditLogEntry.getOperation(),
        auditLogEntry.getDescription(),
        auditLogEntry.isSuccess());
  }

  @Override
  public void messagesReceived(final FudgeContext fudgeContext, final List<FudgeMsgEnvelope> msgEnvelopes) {
    for (final FudgeMsgEnvelope msgEnvelope : msgEnvelopes) {
      messageReceived(fudgeContext, msgEnvelope);
    }

    _delegate.flushCache();
  }

}
