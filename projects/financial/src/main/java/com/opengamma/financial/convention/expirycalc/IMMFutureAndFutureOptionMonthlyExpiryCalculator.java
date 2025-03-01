/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention.expirycalc;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalAdjusters;

import com.mcleodmoores.date.WorkingDayCalendar;
import com.mcleodmoores.date.WorkingDayCalendarAdapter;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.util.ArgumentChecker;

/**
 * Expiry calculator for IMM futures.
 */
@ExpiryCalculator
public final class IMMFutureAndFutureOptionMonthlyExpiryCalculator implements ExchangeTradedInstrumentExpiryCalculator {

  /** Name of the calculator. */
  public static final String NAME = "IMMFutureOptionMonthlyExpiryCalculator";
  /** Singleton. */
  private static final IMMFutureAndFutureOptionMonthlyExpiryCalculator INSTANCE = new IMMFutureAndFutureOptionMonthlyExpiryCalculator();
  /** Adjuster. */
  private static final TemporalAdjuster THIRD_WEDNESDAY_ADJUSTER = TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.WEDNESDAY);
  /** Adjuster. */
  private static final TemporalAdjuster THIRD_MONDAY_ADJUSTER = TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY);
  /** Working days to settle. */
  private static final int WORKING_DAYS_TO_SETTLE = 2;

  /**
   * Gets the singleton instance.
   *
   * @return the instance, not null
   */
  public static IMMFutureAndFutureOptionMonthlyExpiryCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Restricted constructor.
   */
  private IMMFutureAndFutureOptionMonthlyExpiryCalculator() {
  }

  // -------------------------------------------------------------------------
  @Deprecated
  @Override
  public LocalDate getExpiryDate(final int n, final LocalDate today, final Calendar holidayCalendar) {
    return getExpiryDate(n, today, WorkingDayCalendarAdapter.of(holidayCalendar));
  }

  @Override
  public LocalDate getExpiryDate(final int n, final LocalDate today, final WorkingDayCalendar holidayCalendar) {
    ArgumentChecker.isTrue(n > 0, "n must be greater than zero");
    ArgumentChecker.notNull(today, "today");
    ArgumentChecker.notNull(holidayCalendar, "holiday calendar");
    LocalDate result;
    final LocalDate thirdWednesday = today.with(THIRD_WEDNESDAY_ADJUSTER);
    if (today.isAfter(adjustForSettlement(thirdWednesday, holidayCalendar))) {
      result = today.plusMonths(n).with(THIRD_WEDNESDAY_ADJUSTER);
    } else {
      result = today.plusMonths(n - 1).with(THIRD_WEDNESDAY_ADJUSTER);
    }
    return adjustForSettlement(result, holidayCalendar);
  }

  @Override
  public LocalDate getExpiryMonth(final int n, final LocalDate today) {
    ArgumentChecker.isTrue(n > 0, "n must be greater than zero");
    ArgumentChecker.notNull(today, "today");
    if (today.isAfter(today.with(THIRD_MONDAY_ADJUSTER))) {
      return today.plusMonths(n);
    }
    return today.plusMonths(n - 1);
  }

  private static LocalDate adjustForSettlement(final LocalDate date, final WorkingDayCalendar holidayCalendar) {
    int days = 0;
    LocalDate result = date;
    while (days < WORKING_DAYS_TO_SETTLE) {
      result = result.minusDays(1);
      if (holidayCalendar.isWorkingDay(result)) {
        days++;
      }
    }
    return result;
  }

  @Override
  public String getName() {
    return NAME;
  }

}
