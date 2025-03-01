/**
 * Copyright (C) 2014 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.date;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalAdjusters;

import com.opengamma.analytics.util.time.TenorUtils;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventions;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.time.Tenor;

@SuppressWarnings("deprecation")
public final class EndOfMonthDateAdjustmentCalculator implements TenorOffsetDateAdjustmentCalculator {
  private static final TenorOffsetDateAdjustmentCalculator INSTANCE = new EndOfMonthDateAdjustmentCalculator();

  /**
   * Gets an instance.
   * @return  the instance
   */
  public static TenorOffsetDateAdjustmentCalculator getInstance() {
    return INSTANCE;
  }

  private EndOfMonthDateAdjustmentCalculator() {
  }

  @Override
  public LocalDate getSettlementDate(final LocalDate date, final Tenor tenor, final BusinessDayConvention convention,
      final WorkingDayCalendar workingDayCalendar) {
    ArgumentChecker.notNull(date, "date");
    ArgumentChecker.notNull(tenor, "tenor");
    ArgumentChecker.notNull(convention, "convention");
    ArgumentChecker.notNull(workingDayCalendar, "workingDayCalendar");
    final Calendar calendar = CalendarAdapter.of(workingDayCalendar);
    final LocalDate endDate = TenorUtils.adjustDateByTenor(date, tenor, calendar, 0);
    if (tenor.isBusinessDayTenor()) { // This handles tenor of the type ON, TN
      return endDate;
    }
    // Adjusted to month-end: when start date is last business day of the month, the end date is the last business day of the month.
    final LocalDate nextWorkingDay = DefaultSettlementDateCalculator.getInstance().getSettlementDate(date, 1, workingDayCalendar);
    final boolean isDateEom = date.getMonth() != nextWorkingDay.getMonth();
    if (isDateEom && tenor.getPeriod().getDays() == 0) {
      final BusinessDayConvention preceding = BusinessDayConventions.PRECEDING;
      return preceding.adjustDate(calendar, endDate.with(TemporalAdjusters.lastDayOfMonth()));
    }
    return convention.adjustDate(calendar, endDate);
  }

}
