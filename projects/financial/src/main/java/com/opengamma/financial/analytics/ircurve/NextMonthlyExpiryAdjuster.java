/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.ircurve;

import java.util.EnumSet;
import java.util.Set;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * {@code DatAdjuster} that finds the next Expiry in Interest Rate Futures Options. This is the 3rd Wednesday of the next IMM Future Expiry Month.
 */
public class NextMonthlyExpiryAdjuster implements TemporalAdjuster {

  /** An adjuster finding the 3rd Wednesday in a month. May be before or after date */
  private static final TemporalAdjuster DAY_OF_MONTH = TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.WEDNESDAY);

  /** An adjuster moving to the next quarter. */
  private static final TemporalAdjuster NEXT_MONTH_ADJUSTER = new NextMonthAdjuster();

  /** The IMM Expiry months */
  private final Set<Month> _futureQuarters = EnumSet.of(Month.MARCH, Month.JUNE, Month.SEPTEMBER, Month.DECEMBER);

  @Override
  public Temporal adjustInto(final Temporal temporal) {
    final LocalDate date = LocalDate.from(temporal);
    if (_futureQuarters.contains(date.getMonth())
        && date.with(DAY_OF_MONTH).isAfter(date)) { // in a quarter
      return temporal.with(date.with(DAY_OF_MONTH));
    }
    return temporal.with(date.with(NEXT_MONTH_ADJUSTER).with(DAY_OF_MONTH));
  }

}
