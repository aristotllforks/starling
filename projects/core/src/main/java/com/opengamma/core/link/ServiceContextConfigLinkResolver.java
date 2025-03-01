/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.link;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.opengamma.DataNotFoundException;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.core.config.impl.ConfigItem;
import com.opengamma.id.VersionCorrection;
import com.opengamma.service.ServiceContext;
import com.opengamma.service.VersionCorrectionProvider;

/**
 * Link resolver to resolve config links using a ServiceContext.
 *
 * @param <T> the type of config object to be resolved
 */
/* package */ final class ServiceContextConfigLinkResolver<T> extends SourceLinkResolver<String, T, ConfigSource> {

  /**
   * Logger for the class.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceContextConfigLinkResolver.class);

  /**
   * Creates the resolver using the default service context.
   */
  /* package */ ServiceContextConfigLinkResolver() {
    super();
  }

  /**
   * Creates the resolver using the supplied service context.
   *
   * @param serviceContext the service context to use when resolving the link
   */
  /* package */ ServiceContextConfigLinkResolver(final ServiceContext serviceContext) {
    super(serviceContext);
  }

  @Override
  protected Class<ConfigSource> getSourceClass() {
    return ConfigSource.class;
  }

  @Override
  protected VersionCorrection getVersionCorrection(final VersionCorrectionProvider vcProvider) {
    return vcProvider.getConfigVersionCorrection();
  }

  @Override
  protected T executeQuery(final ConfigSource configSource, final Class<T> type,  final String name, final VersionCorrection versionCorrection) {

    // The database stores config items with exact type, but we may want to search
    // with a more general type. We therefore may need to try the search twice.
    final T result = findWithMatchingType(configSource, type, name, versionCorrection);
    return result != null
        ? result
        : findWithGeneralType(configSource, type, name, versionCorrection);
  }

  private T findWithMatchingType(final ConfigSource configSource, final Class<T> type,
                                 final String identifier, final VersionCorrection versionCorrection) {
    return selectResult(type, identifier, configSource.get(type, identifier, versionCorrection));
  }

  @SuppressWarnings("unchecked")
  private T findWithGeneralType(final ConfigSource configSource, final Class<T> type,
                                final String identifier, final VersionCorrection versionCorrection) {

    // Filter the items so we only have ones with compatible types
    final Collection<ConfigItem<Object>> allMatches = configSource.get(Object.class, identifier, versionCorrection);
    final Iterable<ConfigItem<Object>> results = filterForCorrectType(allMatches, type);

    final T result = (T) selectResult(type, identifier, results);
    if (result != null) {
      return result;
    }
    throw new DataNotFoundException("No config found with type: [" + type.getName() + "], id: ["
                                    + identifier + "] and versionCorrection: [" + versionCorrection + "]");
  }

  private Iterable<ConfigItem<Object>> filterForCorrectType(final Collection<ConfigItem<Object>> allMatches,
                                                            final Class<T> type) {
    return Iterables.filter(allMatches, typeMatcher(type));
  }

  /**
   * Predicate which can be used to check that each item passed to
   * it is of the required type.
   *
   * @param type the type to check items are, subclasses of the type will also match
   * @return the predicate to perform the type matching
   */
  private Predicate<ConfigItem<Object>> typeMatcher(final Class<T> type) {
    return new Predicate<ConfigItem<Object>>() {
      @Override
      public boolean apply(final ConfigItem<Object> item) {
        return type.isAssignableFrom(item.getValue().getClass());
      }
    };
  }

  private <R> R selectResult(final Class<T> type, final String identifier, final Iterable<ConfigItem<R>> results) {
    final Iterator<ConfigItem<R>> iterator = results.iterator();
    return iterator.hasNext() ? selectFirst(type, identifier, iterator) : null;
  }

  private <R> R selectFirst(final Class<T> type, final String identifier, final Iterator<ConfigItem<R>> iterator) {
    final R result = iterator.next().getValue();
    if (iterator.hasNext()) {
      LOGGER.warn("Found multiple matching config results for type: {} and name: {} - returning first found",
                    type.getName(), identifier);
    }
    return result;
  }
}
