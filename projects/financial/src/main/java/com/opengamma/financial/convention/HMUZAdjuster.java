/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import java.util.EnumSet;
import java.util.Set;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

import com.opengamma.util.ArgumentChecker;

/**
 *
 */
public final class HMUZAdjuster implements TemporalAdjuster {
  private static final Set<Month> FUTURE_EXPIRY_MONTHS = EnumSet.of(Month.MARCH, Month.JUNE, Month.SEPTEMBER, Month.DECEMBER);
  private static final HMUZAdjuster INSTANCE = new HMUZAdjuster();

  public static HMUZAdjuster getInstance() {
    return INSTANCE;
  }

  private HMUZAdjuster() {
  }

  @Override
  public Temporal adjustInto(final Temporal temporal) {
    ArgumentChecker.notNull(temporal, "temporal");
    LocalDate result = LocalDate.from(temporal);
    while (!FUTURE_EXPIRY_MONTHS.contains(result.getMonth())) {
      result = result.plusMonths(1);
    }
    return temporal.with(result);
  }

}
