/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.kahadb.util.ByteArrayInputStream;
import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.mapping.FudgeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.bbg.model.SecurityMasterRequestMessage;
import com.opengamma.bbg.model.SecurityMasterRequestMessage.MessageType;
import com.opengamma.bbg.model.SecurityMasterResponseMessage;
import com.opengamma.core.change.ChangeManager;
import com.opengamma.core.change.DummyChangeManager;
import com.opengamma.core.security.AbstractSecuritySource;
import com.opengamma.core.security.Security;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.transport.ByteArrayMessageReceiver;
import com.opengamma.transport.ByteArrayRequestSender;
import com.opengamma.util.ArgumentChecker;

/**
 * Provides remote access to the {@link BloombergSecuritySource}.
 */
public class RemoteBloombergSecuritySource extends AbstractSecuritySource {
  // TODO: Needs better javadoc to explain why class is needed

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(RemoteBloombergSecuritySource.class);
  /**
   * The request sender.
   */
  private final ByteArrayRequestSender _byteArrayRequestSender;
  /**
   * The fudge context.
   */
  private final FudgeContext _fudgeContext;

  /**
   * Creates an instance.
   *
   * @param byteArrayRequestSender
   *          the sender
   */
  public RemoteBloombergSecuritySource(final ByteArrayRequestSender byteArrayRequestSender) {
    this(byteArrayRequestSender, new FudgeContext());
  }

  /**
   * Creates an instance.
   *
   * @param byteArrayRequestSender
   *          the sender
   * @param fudgeContext
   *          the context, not null
   */
  public RemoteBloombergSecuritySource(final ByteArrayRequestSender byteArrayRequestSender, final FudgeContext fudgeContext) {
    ArgumentChecker.notNull(byteArrayRequestSender, "byteArrayRequestSender");
    ArgumentChecker.notNull(fudgeContext, "fudgeContext");
    _byteArrayRequestSender = byteArrayRequestSender;
    _fudgeContext = fudgeContext;
  }

  // -------------------------------------------------------------------------
  /**
   * Gets the sender.
   *
   * @return the sender
   */
  public ByteArrayRequestSender getByteArrayRequestSender() {
    return _byteArrayRequestSender;
  }

  /**
   * Gets the Fudge context.
   *
   * @return the Fudge context
   */
  public FudgeContext getFudgeContext() {
    return _fudgeContext;
  }

  @Override
  public Collection<Security> get(final ExternalIdBundle secKey) {
    ArgumentChecker.notNull(secKey, "security key");
    final RemoteSecurityMasterReceiver receiver = new RemoteSecurityMasterReceiver();
    LOGGER.debug("sending getSecurities for {} to remote securityMaster", secKey);
    final SecurityMasterRequestMessage requestMessage = new SecurityMasterRequestMessage();
    requestMessage.setMessageType(MessageType.GET_SECURITIES_BY_KEY);
    requestMessage.setSecKey(secKey);
    final FudgeMsg fudgeMsg = requestMessage.toFudgeMsg(new FudgeSerializer(_fudgeContext));
    _byteArrayRequestSender.sendRequest(_fudgeContext.toByteArray(fudgeMsg), receiver);
    try {
      receiver.getLatch().await();
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.info("InterruptedException, request cannot be serviced right now");
      throw new OpenGammaRuntimeException("Unable to getSecurities because of InterruptedException", e);
    }
    final byte[] data = receiver.getMessage();
    final SecurityMasterResponseMessage response = toSecurityMasterResponseMessage(data);
    return response.getSecurities();
  }

  @Override
  public Collection<Security> get(final ExternalIdBundle bundle, final VersionCorrection versionCorrection) {
    return get(bundle);
  }

  @Override
  public Security getSingle(final ExternalIdBundle secKey) {
    ArgumentChecker.notNull(secKey, "security key");
    final RemoteSecurityMasterReceiver receiver = new RemoteSecurityMasterReceiver();
    LOGGER.debug("sending getSecurity for {} to remote securityMaster", secKey);
    final SecurityMasterRequestMessage requestMessage = new SecurityMasterRequestMessage();
    requestMessage.setMessageType(MessageType.GET_SECURITY_BY_KEY);
    requestMessage.setSecKey(secKey);
    final FudgeMsg fudgeMsg = requestMessage.toFudgeMsg(new FudgeSerializer(_fudgeContext));
    _byteArrayRequestSender.sendRequest(_fudgeContext.toByteArray(fudgeMsg), receiver);
    try {
      receiver.getLatch().await();
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.info("InterruptedException, request cannot be serviced right now");
      throw new OpenGammaRuntimeException("Unable to getSecurity because of InterruptedException", e);
    }
    final byte[] data = receiver.getMessage();
    final SecurityMasterResponseMessage response = toSecurityMasterResponseMessage(data);
    return response.getSecurity();
  }

  @Override
  public Security getSingle(final ExternalIdBundle bundle, final VersionCorrection versionCorrection) {
    return getSingle(bundle);
  }

  @Override
  public Security get(final UniqueId uid) {
    ArgumentChecker.notNull(uid, "unique identifier");
    final RemoteSecurityMasterReceiver receiver = new RemoteSecurityMasterReceiver();
    LOGGER.debug("sending getSecurity for {} to remote securityMaster", uid);
    final SecurityMasterRequestMessage requestMessage = new SecurityMasterRequestMessage();
    requestMessage.setMessageType(MessageType.GET_SECURITY_BY_IDENTITY);
    requestMessage.setUniqueId(uid);
    final FudgeMsg fudgeMsg = requestMessage.toFudgeMsg(new FudgeSerializer(_fudgeContext));
    _byteArrayRequestSender.sendRequest(_fudgeContext.toByteArray(fudgeMsg), receiver);
    try {
      receiver.getLatch().await();
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.info("InterruptedException, request cannot be serviced right now");
      throw new OpenGammaRuntimeException("Unable to getSecurity because of InterruptedException", e);
    }
    final byte[] data = receiver.getMessage();
    final SecurityMasterResponseMessage response = toSecurityMasterResponseMessage(data);
    return response.getSecurity();
  }

  @Override
  public Security get(final ObjectId objectId, final VersionCorrection versionCorrection) {
    // This is wrong
    return get(objectId.atLatestVersion());
  }

  // -------------------------------------------------------------------------
  @Override
  public ChangeManager changeManager() {
    return DummyChangeManager.INSTANCE;
  }

  // -------------------------------------------------------------------------
  public Set<String> getOptionChain(final ExternalId identifier) {
    ArgumentChecker.notNull(identifier, "identifier");
    final RemoteSecurityMasterReceiver receiver = new RemoteSecurityMasterReceiver();
    LOGGER.debug("sending getSecurity for {} to remote securityMaster", identifier);
    final SecurityMasterRequestMessage requestMessage = new SecurityMasterRequestMessage();
    requestMessage.setMessageType(MessageType.GET_OPTION_CHAIN);
    requestMessage.setSecKey(ExternalIdBundle.of(identifier));
    final FudgeMsg fudgeMsg = requestMessage.toFudgeMsg(new FudgeSerializer(_fudgeContext));
    _byteArrayRequestSender.sendRequest(_fudgeContext.toByteArray(fudgeMsg), receiver);
    try {
      receiver.getLatch().await();
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.info("InterruptedException, request cannot be serviced right now");
      throw new OpenGammaRuntimeException("Unable to getOptionChain because of InterruptedException", e);
    }
    final byte[] data = receiver.getMessage();
    final SecurityMasterResponseMessage response = toSecurityMasterResponseMessage(data);
    return response.getOptionChain();
  }

  // @Override
  // public Collection<Security> getAllBondsOfIssuerType(String issuerType) {
  // ArgumentChecker.notNull(issuerType, "issuer type");
  // RemoteSecurityMasterReceiver receiver = new RemoteSecurityMasterReceiver();
  // LOGGER.debug("sending getAllBondsOfIssuerType for {} to remote securityMaster", issuerType);
  // SecurityMasterRequestMessage requestMessage = new SecurityMasterRequestMessage();
  // requestMessage.setMessageType(MessageType.GET_SECURITIES_BY_BOND_ISSUER_TYPE);
  // requestMessage.setBondIssuerType(issuerType);
  // FudgeFieldContainer fudgeMsg = requestMessage.toFudgeMsg(_fudgeContext);
  // _byteArrayRequestSender.sendRequest(_fudgeContext.toByteArray(fudgeMsg), receiver);
  // try {
  // receiver.getLatch().await();
  // } catch (InterruptedException e) {
  // Thread.currentThread().interrupt();
  // LOGGER.info("InterruptedException, request cannot be serviced right now");
  // throw new OpenGammaRuntimeException("Unable to getSecurities because of InterruptedException", e);
  // }
  // byte[] data = receiver.getMessage();
  // SecurityMasterResponseMessage response = toSecurityMasterResponseMessage(data);
  // return response.getSecurities();
  //
  // }

  /**
   * @param data
   *          the input data
   * @return the response
   */
  protected SecurityMasterResponseMessage toSecurityMasterResponseMessage(final byte[] data) {
    return _fudgeContext.readObject(SecurityMasterResponseMessage.class, new ByteArrayInputStream(data));
  }

  private final class RemoteSecurityMasterReceiver implements ByteArrayMessageReceiver {
    private final CountDownLatch _latch = new CountDownLatch(1);
    private byte[] _message;

    @Override
    public void messageReceived(final byte[] message) {
      _message = message;
      _latch.countDown();
    }

    /**
     * @return the message
     */
    public byte[] getMessage() {
      return _message;
    }

    /**
     * @return the latch
     */
    public CountDownLatch getLatch() {
      return _latch;
    }
  }

  @Override
  public Map<UniqueId, Security> get(final Collection<UniqueId> uniqueIds) {
    throw new UnsupportedOperationException("Bulk loading of security not supported yet!");
  }

}
