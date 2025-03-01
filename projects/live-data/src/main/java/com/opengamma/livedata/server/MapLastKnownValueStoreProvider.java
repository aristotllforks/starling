/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.server;

import java.util.Set;

import com.opengamma.id.ExternalId;

/**
 *
 */
public class MapLastKnownValueStoreProvider implements LastKnownValueStoreProvider {

  @Override
  public LastKnownValueStore newInstance(final ExternalId security, final String normalizationRuleSetId) {
    // Ignore the inputs.
    return new MapLastKnownValueStore();
  }

  @Override
  public Set<String> getAllIdentifiers(final String identifierScheme) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isAvailable(final ExternalId security, final String normalizationRuleSetId) {
    return true;
  }

}
