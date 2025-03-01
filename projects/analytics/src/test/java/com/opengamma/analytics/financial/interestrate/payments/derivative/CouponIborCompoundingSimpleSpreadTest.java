/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.interestrate.payments.derivative;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

import org.testng.annotations.Test;
import org.threeten.bp.Period;
import org.threeten.bp.ZonedDateTime;

import com.mcleodmoores.date.CalendarAdapter;
import com.mcleodmoores.date.WeekendWorkingDayCalendar;
import com.mcleodmoores.date.WorkingDayCalendar;
import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.instrument.index.IndexIborMaster;
import com.opengamma.analytics.financial.schedule.ScheduleCalculator;
import com.opengamma.analytics.util.time.TimeCalculator;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventions;
import com.opengamma.util.money.Currency;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.time.DateUtils;

/**
 * Tests related to the building of compounded Ibor coupons with spread: "Compounding treating spread as simple interest"
 */
@Test(groups = TestGroup.UNIT)
public class CouponIborCompoundingSimpleSpreadTest {

  private static final WorkingDayCalendar CALENDAR = WeekendWorkingDayCalendar.SATURDAY_SUNDAY;
  private static final IndexIborMaster MASTER_IBOR = IndexIborMaster.getInstance();
  private static final IborIndex USDLIBOR1M = MASTER_IBOR.getIndex("USDLIBOR1M");
  private static final BusinessDayConvention PREC = BusinessDayConventions.PRECEDING;

  private static final Period TENOR_3M = Period.ofMonths(3);
  private static final ZonedDateTime START_DATE = DateUtils.getUTCDate(2012, 8, 24);
  private static final double NOTIONAL = 123454321;
  private static final double SPREAD = 0.0010; // 10 bps

  private static final ZonedDateTime[] ACCRUAL_END_DATES = ScheduleCalculator.getAdjustedDateSchedule(START_DATE, TENOR_3M,
      USDLIBOR1M.getTenor(), true, true, PREC, CalendarAdapter.of(CALENDAR), false);
  private static final int NB_SUB_PERIOD = ACCRUAL_END_DATES.length;
  private static final ZonedDateTime[] ACCRUAL_START_DATES = new ZonedDateTime[NB_SUB_PERIOD];
  private static final double[] PAYMENT_ACCRUAL_FACTORS = new double[NB_SUB_PERIOD];
  private static final double PAYMENT_ACCRUAL_FACTOR;

  private static final double TOLERANCE = 1.0E-10;

  static {
    ACCRUAL_START_DATES[0] = START_DATE;
    for (int loopsub = 1; loopsub < NB_SUB_PERIOD; loopsub++) {
      ACCRUAL_START_DATES[loopsub] = ACCRUAL_END_DATES[loopsub - 1];
    }
    double af = 0.0;
    for (int loopsub = 0; loopsub < NB_SUB_PERIOD; loopsub++) {
      PAYMENT_ACCRUAL_FACTORS[loopsub] = USDLIBOR1M.getDayCount().getDayCountFraction(ACCRUAL_START_DATES[loopsub],
          ACCRUAL_END_DATES[loopsub]);
      af += PAYMENT_ACCRUAL_FACTORS[loopsub];
    }
    PAYMENT_ACCRUAL_FACTOR = af;
  }
  private static final ZonedDateTime[] FIXING_DATES = ScheduleCalculator.getAdjustedDate(ACCRUAL_START_DATES, -USDLIBOR1M.getSpotLag(),
      CalendarAdapter.of(CALENDAR));
  private static final ZonedDateTime[] FIXING_PERIOD_END_DATES = ScheduleCalculator.getAdjustedDate(ACCRUAL_START_DATES, USDLIBOR1M,
      CalendarAdapter.of(CALENDAR));
  private static final double[] FIXING_ACCRUAL_FACTORS = new double[NB_SUB_PERIOD];
  static {
    for (int loopsub = 0; loopsub < NB_SUB_PERIOD; loopsub++) {
      FIXING_ACCRUAL_FACTORS[loopsub] = USDLIBOR1M.getDayCount().getDayCountFraction(ACCRUAL_START_DATES[loopsub],
          FIXING_PERIOD_END_DATES[loopsub]);
    }
  }
  private static final ZonedDateTime REFERENCE_DATE = DateUtils.getUTCDate(2012, 8, 17);
  private static final double[] FIXING_TIMES = TimeCalculator.getTimeBetween(REFERENCE_DATE, FIXING_DATES);
  private static final double[] FIXING_PERIOD_END_TIMES = TimeCalculator.getTimeBetween(REFERENCE_DATE, FIXING_PERIOD_END_DATES);
  private static final double[] ACCRUAL_START_TIMES = TimeCalculator.getTimeBetween(REFERENCE_DATE, ACCRUAL_START_DATES);
  private static final double[] ACCRUAL_END_TIMES = TimeCalculator.getTimeBetween(REFERENCE_DATE, ACCRUAL_END_DATES);
  private static final double PAYMENT_TIME = ACCRUAL_END_TIMES[NB_SUB_PERIOD - 1];
  private static final CouponIborCompoundingSimpleSpread CPN = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME,
      PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
      USDLIBOR1M,
      PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullCurrency() {
    new CouponIborCompoundingSimpleSpread(null, PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01, USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
  }

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullIndex() {
    new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01, null,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
  }

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void sizePayFactors() {
    new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        new double[1], FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
  }

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void sizeFixing() {
    new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, new double[25], ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
  }

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void sizeFixStart() {
    new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, new double[25], FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
  }

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void sizeFixEnd() {
    new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, new double[25], FIXING_ACCRUAL_FACTORS, SPREAD);
  }

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void sizeFixFactors() {
    new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, new double[25], SPREAD);
  }

  /**
   *
   */
  @Test
  public void getter() {
    assertEquals("CouponIborCompoundingFlatSpread: getter", USDLIBOR1M.getCurrency(), CPN.getCurrency());
    assertEquals("CouponIborCompoundingFlatSpread: getter", PAYMENT_TIME, CPN.getPaymentTime());
    assertEquals("CouponIborCompoundingFlatSpread: getter", PAYMENT_ACCRUAL_FACTOR, CPN.getPaymentYearFraction());
    assertEquals("CouponIborCompoundingFlatSpread: getter", NOTIONAL, CPN.getNotional());
    assertEquals("CouponIborCompoundingFlatSpread: getter", NOTIONAL * 0.01, CPN.getCompoundingPeriodAmountAccumulated());
    assertEquals("CouponIborCompoundingFlatSpread: getter", USDLIBOR1M, CPN.getIndex());
    assertEquals("CouponIborCompoundingFlatSpread: getter", SPREAD, CPN.getSpread());
    assertArrayEquals("CouponIborCompoundingFlatSpread: getter", PAYMENT_ACCRUAL_FACTORS, CPN.getPaymentPeriodAccrualFactors(), TOLERANCE);
    assertArrayEquals("CouponIborCompoundingFlatSpread: getter", FIXING_TIMES, CPN.getFixingTimes(), TOLERANCE);
    assertArrayEquals("CouponIborCompoundingFlatSpread: getter", ACCRUAL_START_TIMES, CPN.getFixingPeriodStartTimes(), TOLERANCE);
    assertArrayEquals("CouponIborCompoundingFlatSpread: getter", FIXING_PERIOD_END_TIMES, CPN.getFixingPeriodEndTimes(), TOLERANCE);
    assertArrayEquals("CouponIborCompoundingFlatSpread: getter", FIXING_ACCRUAL_FACTORS, CPN.getFixingPeriodAccrualFactors(), TOLERANCE);
  }

  /**
   *
   */
  @Test
  public void testHashCodeEquals() {
    CouponIborCompoundingSimpleSpread other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME,
        PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01, USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertEquals(CPN, other);
    assertEquals(CPN.hashCode(), other.hashCode());
    other = new CouponIborCompoundingSimpleSpread(Currency.AUD, PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01, USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME + 1e-8, PAYMENT_ACCRUAL_FACTOR, NOTIONAL,
        NOTIONAL * 0.01, USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR + 0.01, NOTIONAL,
        NOTIONAL * 0.01, USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL + 1,
        NOTIONAL * 0.01, USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.02,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    final IborIndex index = MASTER_IBOR.getIndex("USDLIBOR3M");
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        index,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        FIXING_TIMES, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, PAYMENT_ACCRUAL_FACTORS, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, FIXING_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, ACCRUAL_START_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_PERIOD_END_TIMES, SPREAD);
    assertFalse(other.equals(CPN));
    other = new CouponIborCompoundingSimpleSpread(USDLIBOR1M.getCurrency(), PAYMENT_TIME, PAYMENT_ACCRUAL_FACTOR, NOTIONAL, NOTIONAL * 0.01,
        USDLIBOR1M,
        PAYMENT_ACCRUAL_FACTORS, FIXING_TIMES, ACCRUAL_START_TIMES, FIXING_PERIOD_END_TIMES, FIXING_ACCRUAL_FACTORS, SPREAD * 1.01);
    assertFalse(other.equals(CPN));
  }
}
