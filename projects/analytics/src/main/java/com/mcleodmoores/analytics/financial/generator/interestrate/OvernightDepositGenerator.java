/**
 * Copyright (C) 2017 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.analytics.financial.generator.interestrate;

import org.threeten.bp.Period;
import org.threeten.bp.ZonedDateTime;

import com.mcleodmoores.date.CalendarAdapter;
import com.mcleodmoores.date.WorkingDayCalendar;
import com.opengamma.analytics.financial.instrument.cash.CashDefinition;
import com.opengamma.analytics.financial.schedule.ScheduleCalculator;
import com.opengamma.analytics.util.time.TenorUtils;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Tenor;

/**
 * An extension of the cash generator that represents an overnight deposit generator. The required fields are:
 * <ul>
 * <li>A working day calendar that contains information about weekends and holidays ({@link WorkingDayCalendar}).
 * <li>The currency for which the generator is used.
 * <li>A day count generator that calculates the times between dates ({@link DayCount}).
 * </ul>
 */
public class OvernightDepositGenerator extends CashGenerator {

  /**
   * Gets the generator builder.
   *
   * @return the builder
   */
  public static OvernightDepositGenerator.Builder builder() {
    return new Builder();
  }

  /**
   * A builder for this generator with methods overridden for covariant returns.
   */
  public static class Builder extends CashGenerator.Builder {

    @Override
    public Builder withCurrency(final Currency currency) {
      super.withCurrency(currency);
      return this;
    }

    @Override
    public Builder withCalendar(final WorkingDayCalendar calendar) {
      super.withCalendar(calendar);
      return this;
    }

    @Override
    public Builder withDayCount(final DayCount dayCount) {
      super.withDayCount(dayCount);
      return this;
    }

    @Override
    public OvernightDepositGenerator build() {
      if (getCurrency() == null) {
        throw new IllegalStateException("The currency must be supplied");
      }
      if (getCalendar() == null) {
        throw new IllegalStateException("The calendar must be supplied");
      }
      if (getDayCount() == null) {
        throw new IllegalStateException("The day count must be supplied");
      }
      return new OvernightDepositGenerator(this);
    }

  }

  /**
   * Constructs the generator.
   *
   * @param builder
   *          the builder
   */
  OvernightDepositGenerator(final Builder builder) {
    super(builder);
  }

  @Override
  public CashDefinition toCurveInstrument(final ZonedDateTime date, final Tenor startTenor, final Tenor endTenor, final double notional,
      final double fixedRate) {
    ArgumentChecker.notNull(date, "date");
    final ZonedDateTime startDate = TenorUtils.adjustDateByTenor(date, Tenor.of(Period.ZERO), getCalendar(), 0);
    final ZonedDateTime endDate = ScheduleCalculator.getAdjustedDate(startDate, 1, getCalendar());
    final double accrualFactor = getDayCount().getDayCountFraction(startDate, endDate, CalendarAdapter.of(getCalendar()));
    return new CashDefinition(getCurrency(), startDate, endDate, notional, fixedRate, accrualFactor);
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("OvernightDepositGenerator [currency=");
    builder.append(getCurrency().getCode());
    builder.append(", calendar=");
    builder.append(getCalendar().getName());
    builder.append(", dayCount=");
    builder.append(getDayCount().getName());
    builder.append("]");
    return builder.toString();
  }

}
