/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.core.AbstractEHCachingSourceWithExternalBundle;
import com.opengamma.core.security.Security;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.ehcache.EHCacheUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * A cache decorating a {@code FinancialSecuritySource}.
 * <p>
 * The cache is implemented using {@code EHCache}.
 */
public class EHCachingFinancialSecuritySource
    extends AbstractEHCachingSourceWithExternalBundle<Security, FinancialSecuritySource>
    implements FinancialSecuritySource {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(EHCachingFinancialSecuritySource.class);

  /** The mulitple bonds cache key. */
  /* package for testing */static final String MULTI_BONDS_CACHE = "multi-bonds-cache";

  /**
   * The bond cache.
   */
  private final Cache _bondCache;

  /**
   * Creates an instance over an underlying source specifying the cache manager.
   *
   * @param underlying
   *          the underlying security source, not null
   * @param cacheManager
   *          the cache manager, not null
   */
  public EHCachingFinancialSecuritySource(final FinancialSecuritySource underlying, final CacheManager cacheManager) {
    super(underlying, cacheManager);

    EHCacheUtils.addCache(cacheManager, MULTI_BONDS_CACHE);
    _bondCache = EHCacheUtils.getCacheFromManager(cacheManager, MULTI_BONDS_CACHE);
  }

  // -------------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  @Override
  public Collection<Security> getBondsWithIssuerName(final String issuerType) {
    ArgumentChecker.notNull(issuerType, "issuerType");
    final Element e = _bondCache.get(issuerType);
    Collection<Security> result = new HashSet<>();
    if (e != null) {
      if (e.getObjectValue() instanceof Collection<?>) {
        result.addAll((Collection<Security>) e.getObjectValue());
      } else {
        LOGGER.warn("returned object {} from bond cache is not a Collection<Security>", e.getObjectValue());
      }
    } else {
      result = getUnderlying().getBondsWithIssuerName(issuerType);
      if (result != null) {
        _bondCache.put(new Element(issuerType, result));
        cacheItems(result);
      }
    }
    return result;
  }

}
