/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.normalization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.ehcache.EHCacheUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * A cache wrapping a {@link SecurityRuleProvider}.
 */
public class EHCachingSecurityRuleProvider implements SecurityRuleProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(EHCachingSecurityRuleProvider.class);

  private final SecurityRuleProvider _underlying;
  private final Cache _cache;

  /**
   * Constructs an instance with an in-memory cache.
   *
   * @param underlying  the underlying security rule provider, not null
   * @param cacheManager  the cache manager, not null
   * @param cacheName  the name of the cache, not null
   * @param maxElementsInMemory  the maximum number of security rules to cache
   */
  public EHCachingSecurityRuleProvider(final SecurityRuleProvider underlying, final CacheManager cacheManager, final String cacheName,
      final int maxElementsInMemory) {
    ArgumentChecker.notNull(underlying, "underlying");
    ArgumentChecker.notNull(cacheManager, "cacheManager");
    ArgumentChecker.notNull(cacheName, "cacheName");
    _underlying = underlying;
    EHCacheUtils.addCache(cacheManager, cacheName);
    _cache = EHCacheUtils.getCacheFromManager(cacheManager, cacheName);
  }

  @Override
  public NormalizationRule getRule(final String securityUniqueId) {
    Element e = _cache.get(securityUniqueId);
    if (e != null) {
      LOGGER.debug("Obtained normalization rule for security " + securityUniqueId + " from cache");
      return (NormalizationRule) e.getObjectValue();
    }
    try {
      final NormalizationRule rule = _underlying.getRule(securityUniqueId);
      LOGGER.debug("Obtained normalization rule for security {} from underlying provider", securityUniqueId);
      e = new Element(securityUniqueId, rule);
      _cache.put(e);
      return rule;
    } catch (final Exception ex) {
      // Don't attempt to cache exceptions as:
      //   a) they will cause the subscription to fail so the cache will be of little use
      //   b) if the error can be fixed at runtime then the user may try again and expect success
      LOGGER.warn("Error obtaining normalization rule for security " + securityUniqueId, ex);
      throw new OpenGammaRuntimeException("Error obtaining normalization rule for security " + securityUniqueId, ex);
    }
  }

}
