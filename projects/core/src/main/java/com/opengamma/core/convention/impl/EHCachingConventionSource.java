/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.convention.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;
import com.opengamma.core.change.ChangeManager;
import com.opengamma.core.convention.Convention;
import com.opengamma.core.convention.ConventionSource;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.ehcache.EHCacheUtils;
import com.opengamma.util.tuple.Pair;
import com.opengamma.util.tuple.Pairs;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * A cache decorating a {@code ConventionSource}.
 * <p>
 * The cache is implemented using {@code EHCache}.
 * <p>
 * Any requests with a "latest" version/correction or unversioned unique identifier are not cached
 * and will always hit the underlying. This should not be an issue in practice as the engine components
 * which use the convention source will always specify an exact version/correction and versioned unique identifiers.
 */
public class EHCachingConventionSource implements ConventionSource {

  /**
   * Cache key for conventions.
   */
  private static final String CONVENTION_CACHE = "convention";

  /**
   * The underlying convention source.
   */
  private final ConventionSource _underlying;
  /**
   * The cache manager.
   */
  private final CacheManager _cacheManager;
  /**
   * The convention cache.
   */
  private final Cache _conventionCache;
  /**
   * The front cache.
   */
  private final ConcurrentMap<Object, Convention> _frontCache = new MapMaker().weakValues().makeMap();

  /**
   * Creates the cache around an underlying convention source.
   *
   * @param underlying  the underlying data, not null
   * @param cacheManager  the cache manager, not null
   */
  public EHCachingConventionSource(final ConventionSource underlying, final CacheManager cacheManager) {
    ArgumentChecker.notNull(underlying, "underlying");
    ArgumentChecker.notNull(cacheManager, "cacheManager");
    _underlying = underlying;
    _cacheManager = cacheManager;
    EHCacheUtils.addCache(cacheManager, CONVENTION_CACHE);
    _conventionCache = EHCacheUtils.getCacheFromManager(cacheManager, CONVENTION_CACHE);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the underlying source of conventions.
   *
   * @return the underlying source of conventions, not null
   */
  protected ConventionSource getUnderlying() {
    return _underlying;
  }

  /**
   * Gets the cache manager.
   *
   * @return the cache manager, not null
   */
  protected CacheManager getCacheManager() {
    return _cacheManager;
  }

  /**
   * For use by test methods only to control the front cache.
   */
  void emptyFrontCache() {
    _frontCache.clear();
  }

  /**
   * For use by test methods only to control the EH cache.
   */
  void emptyEHCache() {
    EHCacheUtils.clear(getCacheManager(), CONVENTION_CACHE);
  }

  //-------------------------------------------------------------------------
  /**
   * Adds an object to the front cache unless it is the latest version.
   *
   * @param convention  the convention
   * @param versionCorrection  the version / correction
   * @return  the legal entity
   */
  protected Convention addToFrontCache(final Convention convention, final VersionCorrection versionCorrection) {
    if (convention.getUniqueId().isLatest()) {
      return convention;
    }
    final Convention existing = _frontCache.putIfAbsent(convention.getUniqueId(), convention);
    if (existing != null) {
      return existing;
    }
    if (versionCorrection != null) {
      _frontCache.put(Pairs.of(convention.getExternalIdBundle(), versionCorrection), convention);
      _frontCache.put(Pairs.of(convention.getUniqueId().getObjectId(), versionCorrection), convention);
    }
    return convention;
  }

  /**
   * Adds an object to the EH cache unless it is the latest version.
   *
   * @param convention  the convention
   * @param versionCorrection  the version / correction
   * @return  the legal entity
   */
  protected Convention addToCache(final Convention convention, final VersionCorrection versionCorrection) {
    if (convention != null) {
      final Convention front = addToFrontCache(convention, null);
      if (front == convention) {
        if (convention.getUniqueId().isVersioned()) {
          _conventionCache.put(new Element(convention.getUniqueId(), convention));
        }
        if (versionCorrection != null) {
          _conventionCache.put(new Element(Pairs.of(convention.getExternalIdBundle(), versionCorrection), convention));
          _conventionCache.put(new Element(Pairs.of(convention.getUniqueId().getObjectId(), versionCorrection), convention));
        }
      }
      return front;
    }
    return null;
  }

  //-------------------------------------------------------------------------
  @Override
  public Convention get(final UniqueId uniqueId) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");
    // check cache, but not if latest
    if (uniqueId.isVersioned()) {
      Convention cached = _frontCache.get(uniqueId);
      if (cached != null) {
        return cached;
      }
      final Element e = _conventionCache.get(uniqueId);
      if (e != null) {
        cached = (Convention) e.getObjectValue();
        return addToFrontCache(cached, null);
      }
    }
    // query underlying
    final Convention convention = getUnderlying().get(uniqueId);
    return addToCache(convention, null);
  }

  @Override
  public <T extends Convention> T get(final UniqueId uniqueId, final Class<T> type) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");
    ArgumentChecker.notNull(type, "type");
    return type.cast(get(uniqueId));
  }

  @Override
  public Convention get(final ObjectId objectId, final VersionCorrection versionCorrection) {
    ArgumentChecker.notNull(objectId, "objectId");
    ArgumentChecker.notNull(versionCorrection, "versionCorrection");
    // latest not in cache, can cache only by uniqueId
    if (versionCorrection.containsLatest()) {
      return addToCache(getUnderlying().get(objectId, versionCorrection), null);
    }
    // check cache
    final Pair<ObjectId, VersionCorrection> key = Pairs.of(objectId, versionCorrection);
    Convention cached = _frontCache.get(key);
    if (cached != null) {
      return cached;
    }
    final Element e = _conventionCache.get(key);
    if (e != null) {
      cached = (Convention) e.getObjectValue();
      return addToFrontCache(cached, versionCorrection);
    }
    // query underlying
    final Convention convention = getUnderlying().get(objectId, versionCorrection);
    return addToCache(convention, versionCorrection);
  }

  @Override
  public <T extends Convention> T get(final ObjectId objectId, final VersionCorrection versionCorrection, final Class<T> type) {
    ArgumentChecker.notNull(objectId, "objectId");
    ArgumentChecker.notNull(versionCorrection, "versionCorrection");
    ArgumentChecker.notNull(type, "type");
    return type.cast(get(objectId, versionCorrection));
  }

  //-------------------------------------------------------------------------
  @Override
  public Convention getSingle(final ExternalId externalId) {
    ArgumentChecker.notNull(externalId, "externalId");
    return getSingle(externalId.toBundle(), VersionCorrection.LATEST);
  }

  @Override
  public <T extends Convention> T getSingle(final ExternalId externalId, final Class<T> type) {
    ArgumentChecker.notNull(externalId, "externalId");
    ArgumentChecker.notNull(type, "type");
    return type.cast(getSingle(externalId.toBundle(), VersionCorrection.LATEST));
  }

  @Override
  public Convention getSingle(final ExternalIdBundle bundle) {
    ArgumentChecker.notNull(bundle, "bundle");
    return getSingle(bundle, VersionCorrection.LATEST);
  }

  @Override
  public <T extends Convention> T getSingle(final ExternalIdBundle bundle, final Class<T> type) {
    ArgumentChecker.notNull(bundle, "bundle");
    ArgumentChecker.notNull(type, "type");
    return type.cast(getSingle(bundle));
  }

  @Override
  public Convention getSingle(final ExternalIdBundle bundle, final VersionCorrection versionCorrection) {
    ArgumentChecker.notNull(bundle, "bundle");
    ArgumentChecker.notNull(versionCorrection, "versionCorrection");
    // latest not in cache, can cache only by uniqueId
    if (versionCorrection.containsLatest()) {
      return addToCache(getUnderlying().getSingle(bundle, versionCorrection), null);
    }
    // check cache
    final Pair<ExternalIdBundle, VersionCorrection> key = Pairs.of(bundle, versionCorrection);
    Convention cached = _frontCache.get(key);
    if (cached != null) {
      return cached;
    }
    final Element e = _conventionCache.get(key);
    if (e != null) {
      cached = (Convention) e.getObjectValue();
      return addToFrontCache(cached, versionCorrection);
    }
    // query underlying
    final Convention convention = getUnderlying().getSingle(bundle, versionCorrection);
    return addToCache(convention, versionCorrection);
  }

  @Override
  public <T extends Convention> T getSingle(final ExternalIdBundle bundle, final VersionCorrection versionCorrection, final Class<T> type) {
    ArgumentChecker.notNull(bundle, "bundle");
    ArgumentChecker.notNull(versionCorrection, "versionCorrection");
    ArgumentChecker.notNull(type, "type");
    return type.cast(getSingle(bundle, versionCorrection));
  }

  //-------------------------------------------------------------------------
  @Override
  @SuppressWarnings("deprecation")
  public Collection<Convention> get(final ExternalIdBundle bundle) {
    return getUnderlying().get(bundle);
  }

  @Override
  public Collection<Convention> get(final ExternalIdBundle bundle, final VersionCorrection versionCorrection) {
    return getUnderlying().get(bundle, versionCorrection);
  }

  //-------------------------------------------------------------------------
  @Override
  public Map<UniqueId, Convention> get(final Collection<UniqueId> uniqueIds) {
    final Map<UniqueId, Convention> map = getUnderlying().get(uniqueIds);
    for (final Entry<UniqueId, Convention> entry : map.entrySet()) {
      entry.setValue(addToCache(entry.getValue(), null));
    }
    return map;
  }

  @Override
  public Map<ObjectId, Convention> get(final Collection<ObjectId> objectIds, final VersionCorrection versionCorrection) {
    final Map<ObjectId, Convention> map = getUnderlying().get(objectIds, versionCorrection);
    for (final Entry<ObjectId, Convention> entry : map.entrySet()) {
      entry.setValue(addToCache(entry.getValue(), versionCorrection));
    }
    return map;
  }

  @Override
  public Map<ExternalIdBundle, Collection<Convention>> getAll(final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    return getUnderlying().getAll(bundles, versionCorrection);
  }

  @Override
  public Map<ExternalIdBundle, Convention> getSingle(final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    final Map<ExternalIdBundle, Convention> map = getUnderlying().getSingle(bundles, versionCorrection);
    for (final Entry<ExternalIdBundle, Convention> entry : map.entrySet()) {
      entry.setValue(addToCache(entry.getValue(), versionCorrection));
    }
    return map;
  }

  //-------------------------------------------------------------------------
  @Override
  public ChangeManager changeManager() {
    return getUnderlying().changeManager();
  }

  /**
   * Call this at the end of a unit test run to clear the state of EHCache.
   * It should not be part of a generic lifecycle method.
   */
  protected void shutdown() {
    _cacheManager.removeCache(CONVENTION_CACHE);
    _frontCache.clear();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + getUnderlying() + "]";
  }

}
