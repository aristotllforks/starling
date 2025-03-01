/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention.expirycalc;

import java.util.EnumSet;
import java.util.Set;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalAdjusters;

import com.mcleodmoores.date.WorkingDayCalendar;
import com.mcleodmoores.date.WorkingDayCalendarAdapter;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.financial.analytics.ircurve.NextQuarterAdjuster;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.util.ArgumentChecker;

/**
 * Expiry calculator for futures (not options) on the Russell 2000 Index.
 * <p>
 * TF Futures, traded on the ICE, are specified here: https://www.theice.com/productguide/ProductSpec.shtml?specId=86
 * <p>
 * Contract Months = Four months in the March/June/September/December quarterly expiration cycle.
 * <p>
 * Last Trading Day = Third Friday of the expiration month. Trading in the expiring contract ceases at 9:30 a.m. ET on the Last Trading Day.
 */
@ExpiryCalculator
public final class RussellFutureExpiryCalculator implements ExchangeTradedInstrumentExpiryCalculator {

  private static final Set<Month> QUARTERLY_CYCLE_MONTHS = EnumSet.of(Month.MARCH, Month.JUNE, Month.SEPTEMBER, Month.DECEMBER);
  private static final NextQuarterAdjuster NEXT_QUARTER_ADJUSTER = new NextQuarterAdjuster(QUARTERLY_CYCLE_MONTHS);
  private static final TemporalAdjuster DAY_OF_MONTH_ADJUSTER = TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.FRIDAY);

  private static final RussellFutureExpiryCalculator INSTANCE = new RussellFutureExpiryCalculator();

  /** @return the singleton instance of the calculator, not null */
  public static RussellFutureExpiryCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Restricted constructor.
   */
  private RussellFutureExpiryCalculator() {
  }

  /**
   * Quarterly expiries along March cycle.
   *
   * @param n
   *          nth future
   * @param today
   *          The date from which to start
   * @param holidayCalendar
   *          holiday information
   * @return the expiry date of the nth option
   */
  @Deprecated
  @Override
  public LocalDate getExpiryDate(final int n, final LocalDate today, final Calendar holidayCalendar) {
    return getExpiryDate(n, today, WorkingDayCalendarAdapter.of(holidayCalendar));
  }

  /**
   * Quarterly expiries along March cycle.
   *
   * @param n
   *          nth future
   * @param today
   *          The date from which to start
   * @param holidayCalendar
   *          holiday information
   * @return the expiry date of the nth option
   */
  @Override
  public LocalDate getExpiryDate(final int n, final LocalDate today, final WorkingDayCalendar holidayCalendar) {
    ArgumentChecker.notNegativeOrZero(n, "nth expiry");
    ArgumentChecker.notNull(today, "date");
    ArgumentChecker.notNull(holidayCalendar, "holidayCalendar");

    LocalDate thirdFriday = getThirdFriday(today, holidayCalendar);
    if (today.isAfter(thirdFriday)) { // If it is not on or after valuationDate...
      thirdFriday = getThirdFriday(today.plusMonths(1), holidayCalendar);
    }
    final int nQuartersRemaining = QUARTERLY_CYCLE_MONTHS.contains(Month.from(thirdFriday)) ? n - 1 : n;
    if (nQuartersRemaining == 0) {
      return thirdFriday;
    }
    return getQuarterlyExpiry(nQuartersRemaining, thirdFriday, holidayCalendar);

  }

  @Override
  public LocalDate getExpiryMonth(final int n, final LocalDate today) {
    throw new OpenGammaRuntimeException("Russell 2000 Index Mini Futures do not have monthly expiries");
  }

  // Return expiryDate that is the 3rd Friday (or previous good day if holiday) of month in which date falls
  private static LocalDate getThirdFriday(final LocalDate date, final WorkingDayCalendar holidayCalendar) {
    // Compute the expiry of valuationDate's month
    LocalDate following3rdFriday = date.with(DAY_OF_MONTH_ADJUSTER); // 3rd Friday of month in which date falls
    while (!holidayCalendar.isWorkingDay(following3rdFriday)) {
      following3rdFriday = following3rdFriday.minusDays(1); // previous good day
    }
    return following3rdFriday; // expiry is 30 days before
  }

  private static LocalDate getQuarterlyExpiry(final int nthExpiryAfterSerialContracts, final LocalDate lastSerialExpiry,
      final WorkingDayCalendar holidayCalendar) {
    // First find the nth quarter after the lastSerialExpiry
    LocalDate nthExpiryMonth = lastSerialExpiry;
    for (int n = nthExpiryAfterSerialContracts; n > 0; n--) {
      nthExpiryMonth = nthExpiryMonth.with(NEXT_QUARTER_ADJUSTER);
    }
    // Then find the expiry date in that month
    return getThirdFriday(nthExpiryMonth, holidayCalendar);
  }

  @Override
  public String getName() {
    return "Russell2000IndexFutureExpiryCalculator";
  }
}
