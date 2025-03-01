/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security;

import java.util.Collection;
import java.util.Map;

import com.opengamma.core.change.AggregatingChangeManager;
import com.opengamma.core.change.ChangeManager;
import com.opengamma.core.security.AbstractSecuritySource;
import com.opengamma.core.security.Security;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdSchemeDelegator;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.ArgumentChecker;

/**
 * A source of securities that uses the scheme of the unique identifier to determine which underlying source should handle the request.
 * <p>
 * If no scheme-specific handler has been registered, a default is used.
 */
public class DelegatingFinancialSecuritySource extends AbstractSecuritySource implements FinancialSecuritySource {

  /**
   * The change manager
   */
  private final ChangeManager _changeManager;
  /**
   * The uniqueId scheme delegator.
   */
  private final UniqueIdSchemeDelegator<FinancialSecuritySource> _delegator;

  /**
   * Creates an instance specifying the default delegate.
   *
   * @param defaultSource the source to use when no scheme matches, not null
   */
  public DelegatingFinancialSecuritySource(final FinancialSecuritySource defaultSource) {
    _delegator = new UniqueIdSchemeDelegator<>(defaultSource);
    _changeManager = defaultSource.changeManager();
  }

  /**
   * Creates an instance specifying the default delegate.
   *
   * @param defaultSource the source to use when no scheme matches, not null
   * @param schemePrefixToSourceMap the map of sources by scheme to switch on, not null
   */
  public DelegatingFinancialSecuritySource(final FinancialSecuritySource defaultSource, final Map<String, FinancialSecuritySource> schemePrefixToSourceMap) {
    _delegator = new UniqueIdSchemeDelegator<>(defaultSource, schemePrefixToSourceMap);
    final AggregatingChangeManager changeManager = new AggregatingChangeManager();
    changeManager.addChangeManager(defaultSource.changeManager());
    for (final FinancialSecuritySource source : schemePrefixToSourceMap.values()) {
      changeManager.addChangeManager(source.changeManager());
    }
    _changeManager = changeManager;
  }

  //-------------------------------------------------------------------------
  @Override
  public Security get(final UniqueId uid) {
    ArgumentChecker.notNull(uid, "uid");
    return _delegator.chooseDelegate(uid.getScheme()).get(uid);
  }

  @Override
  public Security get(final ObjectId objectId, final VersionCorrection versionCorrection) {
    ArgumentChecker.notNull(objectId, "objectId");
    ArgumentChecker.notNull(versionCorrection, "versionCorrection");
    return _delegator.chooseDelegate(objectId.getScheme()).get(objectId, versionCorrection);
  }

  @Override
  public Collection<Security> get(final ExternalIdBundle bundle) {
    ArgumentChecker.notNull(bundle, "bundle");
    // best implementation is to return first matching result
    for (final SecuritySource delegateSource : _delegator.getDelegates().values()) {
      final Collection<Security> result = delegateSource.get(bundle);
      if (!result.isEmpty()) {
        return result;
      }
    }
    return _delegator.getDefaultDelegate().get(bundle);
  }

  @Override
  public Collection<Security> get(final ExternalIdBundle bundle, final VersionCorrection versionCorrection) {
    ArgumentChecker.notNull(bundle, "bundle");
    ArgumentChecker.notNull(versionCorrection, "versionCorrection");
    // best implementation is to return first matching result
    for (final SecuritySource delegateSource : _delegator.getDelegates().values()) {
      final Collection<Security> result = delegateSource.get(bundle, versionCorrection);
      if (!result.isEmpty()) {
        return result;
      }
    }
    return _delegator.getDefaultDelegate().get(bundle, versionCorrection);
  }

  @Override
  public Security getSingle(final ExternalIdBundle bundle) {
    ArgumentChecker.notNull(bundle, "bundle");
    // best implementation is to return first matching result
    for (final SecuritySource delegateSource : _delegator.getDelegates().values()) {
      final Security result = delegateSource.getSingle(bundle);
      if (result != null) {
        return result;
      }
    }
    return _delegator.getDefaultDelegate().getSingle(bundle);
  }

  @Override
  public Security getSingle(final ExternalIdBundle bundle, final VersionCorrection versionCorrection) {
    ArgumentChecker.notNull(bundle, "bundle");
    ArgumentChecker.notNull(versionCorrection, "versionCorrection");
    ArgumentChecker.notNull(bundle, "bundle");
    // best implementation is to return first matching result
    for (final SecuritySource delegateSource : _delegator.getDelegates().values()) {
      final Security result = delegateSource.getSingle(bundle, versionCorrection);
      if (result != null) {
        return result;
      }
    }
    return _delegator.getDefaultDelegate().getSingle(bundle, versionCorrection);
  }

  @Override
  public Collection<Security> getBondsWithIssuerName(final String issuerName) {
    // best implementation is to return first matching result
    for (final FinancialSecuritySource delegateSource : _delegator.getDelegates().values()) {
      final Collection<Security> result = delegateSource.getBondsWithIssuerName(issuerName);
      if (!result.isEmpty()) {
        return result;
      }
    }
    return _delegator.getDefaultDelegate().getBondsWithIssuerName(issuerName);
  }

  //-------------------------------------------------------------------------
  @Override
  public ChangeManager changeManager() {
    return _changeManager;
  }

}
