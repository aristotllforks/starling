/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.id.ExternalBundleIdentifiable;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.PoolExecutor;

/**
 * A partial implementation of {@link SourceWithExternalBundle}.
 *
 * @param <V> the type returned by the source
 */
public abstract class AbstractSourceWithExternalBundle<V extends UniqueIdentifiable & ExternalBundleIdentifiable>
extends AbstractSource<V> implements SourceWithExternalBundle<V> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSourceWithExternalBundle.class);

  private static final PoolExecutor.CompletionListener<Void> REPORT_EXCEPTIONS = new PoolExecutor.CompletionListener<Void>() {

    @Override
    public void success(final Void result) {
      LOGGER.debug("Asynchronous job completed");
    }

    @Override
    public void failure(final Throwable error) {
      LOGGER.error("Asynchronous job error: {}", error.getMessage());
      LOGGER.warn("Caught exception", error);
    }
  };

  /**
   * Gets objects from the source. If there is more than one match for the id bundle then any one of the matches
   * could be returned.
   *
   * @param executor  a pooled executor
   * @param source  the source
   * @param bundles  the bundles for which to get the objects
   * @param versionCorrection  the version/correction of the objects
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> Map<ExternalIdBundle, Collection<V>> getAllMultiThread(final PoolExecutor executor,
      final SourceWithExternalBundle<V> source, final Collection<ExternalIdBundle> bundles,
      final VersionCorrection versionCorrection) {
    final PoolExecutor.Service<Void> jobs = executor.createService(REPORT_EXCEPTIONS);
    final Map<ExternalIdBundle, Collection<V>> results = Maps.newHashMapWithExpectedSize(bundles.size());
    for (final ExternalIdBundle bundle : bundles) {
      jobs.execute(new Runnable() {
        @Override
        public void run() {
          final Collection<V> result = source.get(bundle, versionCorrection);
          if (result != null && !result.isEmpty()) {
            synchronized (results) {
              results.put(bundle, result);
            }
          }
        }
      });
    }
    try {
      LOGGER.debug("Joining asynchronous jobs in getAllMultiThread");
      jobs.join();
    } catch (final InterruptedException e) {
      throw new OpenGammaRuntimeException("Interrupted", e);
    }
    LOGGER.debug("Returning {} results from getAllMultiThread", results.size());
    return results;
  }

  /**
   * Gets objects from the source. If there is more than one match for the id bundle then any one of the matches
   * could be returned.
   *
   * @param source  the source
   * @param bundles  the bundles for which to get the objects
   * @param versionCorrection  the version/correction of the objects
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> Map<ExternalIdBundle, Collection<V>> getAllSingleThread(
      final SourceWithExternalBundle<V> source, final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    final Map<ExternalIdBundle, Collection<V>> results = Maps.newHashMapWithExpectedSize(bundles.size());
    for (final ExternalIdBundle bundle : bundles) {
      final Collection<V> result = source.get(bundle, versionCorrection);
      if (result != null && !result.isEmpty()) {
        results.put(bundle, result);
      }
    }
    return results;
  }

  /**
   * Gets objects from the source. If there is more than one match for the id bundle then all of the matches
   * are be returned.
   *
   * @param source  the source
   * @param bundles  the bundles for which to get the objects
   * @param versionCorrection  the version/correction of the objects
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> Map<ExternalIdBundle, Collection<V>> getAll(
      final SourceWithExternalBundle<V> source, final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    if (bundles.isEmpty()) {
      return Collections.emptyMap();
    } else if (bundles.size() == 1) {
      final ExternalIdBundle bundle = bundles.iterator().next();
      final Collection<V> result = source.get(bundle, versionCorrection);
      if (result != null && !result.isEmpty()) {
        return Collections.<ExternalIdBundle, Collection<V>>singletonMap(bundle, result);
      }
      return Collections.emptyMap();
    }
    final PoolExecutor executor = PoolExecutor.instance();
    if (executor != null) {
      return getAllMultiThread(executor, source, bundles, versionCorrection);
    }
    return getAllSingleThread(source, bundles, versionCorrection);
  }

  @Override
  public Map<ExternalIdBundle, Collection<V>> getAll(final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    return getAll(this, bundles, versionCorrection);
  }

  /**
   * Gets the latest version of an object from the source. If there is more than one match for the id bundle then any one of the matches
   * could be returned.
   *
   * @param source  the source
   * @param bundle  the identifiers of the object
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> Collection<V> get(final SourceWithExternalBundle<V> source,
      final ExternalIdBundle bundle) {
    return source.get(bundle, VersionCorrection.LATEST);
  }

  @Override
  public Collection<V> get(final ExternalIdBundle bundle) {
    return get(this, bundle);
  }

  /**
   * Gets the latest version of an object from the source. If there is more than one match for the id bundle then any one of the matches
   * could be returned.
   *
   * @param source  the source
   * @param bundle  the identifiers of the object
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> V getSingle(final SourceWithExternalBundle<V> source,
      final ExternalIdBundle bundle) {
    return source.getSingle(bundle, VersionCorrection.LATEST);
  }

  @Override
  public V getSingle(final ExternalIdBundle bundle) {
    return getSingle(this, bundle);
  }

  /**
   * Gets the an object from the source. If there is more than one match for the id bundle then any one of the matches
   * could be returned.
   *
   * @param source  the source
   * @param bundle  the identifiers of the object
   * @param versionCorrection  the version of the object
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> V getSingle(final SourceWithExternalBundle<V> source, final ExternalIdBundle bundle,
      final VersionCorrection versionCorrection) {
    final Collection<V> results = source.get(bundle, versionCorrection);
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.iterator().next();
  }

  @Override
  public V getSingle(final ExternalIdBundle bundle, final VersionCorrection versionCorrection) {
    return getSingle(this, bundle, versionCorrection);
  }

  /**
   * Gets objects from the source. If there is more than one match for the id bundle then any one of the matches
   * could be returned.
   *
   * @param executor  a pool executor
   * @param source  the source
   * @param bundles  the bundles for which to get the objects
   * @param versionCorrection  the version/correction of the objects
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> Map<ExternalIdBundle, V> getSingleMultiThread(
      final PoolExecutor executor, final SourceWithExternalBundle<V> source,
      final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    final PoolExecutor.Service<Void> jobs = executor.createService(REPORT_EXCEPTIONS);
    final Map<ExternalIdBundle, V> results = Maps.newHashMapWithExpectedSize(bundles.size());
    for (final ExternalIdBundle bundle : bundles) {
      jobs.execute(new Runnable() {
        @Override
        public void run() {
          final V result = source.getSingle(bundle, versionCorrection);
          if (result != null) {
            synchronized (results) {
              results.put(bundle, result);
            }
          }
        }
      });
    }
    try {
      LOGGER.debug("Joining asynchronous jobs in getSingleMultiThread");
      jobs.join();
    } catch (final InterruptedException e) {
      throw new OpenGammaRuntimeException("Interrupted", e);
    }
    LOGGER.debug("Returning {} results from getSingleMultiThread", results.size());
    return results;
  }

  /**
   * Gets objects from the source. If there is more than one match for the id bundle then any one of the matches
   * could be returned.
   *
   * @param source  the source
   * @param bundles  the bundles for which to get the objects
   * @param versionCorrection  the version/correction of the objects
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> Map<ExternalIdBundle, V> getSingleSingleThread(
      final SourceWithExternalBundle<V> source, final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    final Map<ExternalIdBundle, V> results = Maps.newHashMapWithExpectedSize(bundles.size());
    for (final ExternalIdBundle bundle : bundles) {
      final V result = source.getSingle(bundle, versionCorrection);
      if (result != null) {
        results.put(bundle, result);
      }
    }
    return results;
  }

  /**
   * Gets objects from the source. If there is more than one match for the id bundle then any one of the matches
   * could be returned.
   *
   * @param source  the source
   * @param bundles  the bundles for which to get the objects
   * @param versionCorrection  the version/correction of the objects
   * @return  the objects
   * @param <V>  the type of the objects
   */
  public static <V extends UniqueIdentifiable & ExternalBundleIdentifiable> Map<ExternalIdBundle, V> getSingle(final SourceWithExternalBundle<V> source,
      final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    if (bundles.isEmpty()) {
      return Collections.emptyMap();
    } else if (bundles.size() == 1) {
      final ExternalIdBundle bundle = bundles.iterator().next();
      final V object = source.getSingle(bundle, versionCorrection);
      if (object != null) {
        return Collections.<ExternalIdBundle, V>singletonMap(bundle, object);
      }
      return Collections.emptyMap();
    }
    final PoolExecutor executor = PoolExecutor.instance();
    if (executor != null) {
      return getSingleMultiThread(executor, source, bundles, versionCorrection);
    }
    return getSingleSingleThread(source, bundles, versionCorrection);
  }

  @Override
  public Map<ExternalIdBundle, V> getSingle(final Collection<ExternalIdBundle> bundles, final VersionCorrection versionCorrection) {
    return getSingle(this, bundles, versionCorrection);
  }

}
