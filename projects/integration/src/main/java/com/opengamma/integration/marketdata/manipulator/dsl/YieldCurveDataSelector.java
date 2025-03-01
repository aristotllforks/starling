/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.util.Set;
import java.util.regex.Pattern;

import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.money.Currency;

/**
 *
 */
public final class YieldCurveDataSelector extends Selector {

  /* package */ YieldCurveDataSelector(final Set<String> calcConfigNames,
                                       final Set<String> names,
                                       final Set<Currency> currencies,
                                       final Pattern nameMatchPattern,
                                       final Pattern nameLikePattern) {
    super(calcConfigNames, names, currencies, nameMatchPattern, nameLikePattern);
  }

  @Override
  boolean matches(final ValueSpecification valueSpecification) {
    if (!ValueRequirementNames.YIELD_CURVE_DATA.equals(valueSpecification.getValueName())) {
      return false;
    }
    final Currency currency = Currency.of(valueSpecification.getTargetSpecification().getUniqueId().getValue());
    final String curve = valueSpecification.getProperties().getStrictValue(ValuePropertyNames.CURVE);
    if (curve == null) {
      return false;
    }
    return matches(curve, currency);
  }
}
