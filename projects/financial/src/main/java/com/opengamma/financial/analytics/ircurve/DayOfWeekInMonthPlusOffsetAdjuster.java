/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.ircurve;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.DayOfWeek;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 *
 */
public class DayOfWeekInMonthPlusOffsetAdjuster implements TemporalAdjuster {

  private final int _week;
  private final DayOfWeek _day;
  private final int _offset;

  public DayOfWeekInMonthPlusOffsetAdjuster(final int week, final DayOfWeek day, final int offset) {
    _week = week;
    _day = day;
    _offset = offset;
  }

  @Override
  public Temporal adjustInto(final Temporal temporal) {
    final TemporalAdjuster unadjustedDayInMonth = TemporalAdjusters.dayOfWeekInMonth(_week, _day);
    return temporal.with(unadjustedDayInMonth).plus(_offset, DAYS);
  }
}
