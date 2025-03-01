/**
 * Copyright (C) 2014 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.integration.adapter;

import static org.testng.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import com.opengamma.util.test.TestGroup;

import net.finmath.time.daycount.DayCountConventionInterface;
import net.finmath.time.daycount.DayCountConvention_ACT_365;

/**
 * Unit tests for {@link ActThreeSixtyFiveFinmathDayCount}.
 */
@Test(groups = TestGroup.UNIT)
public class ActThreeSixtyFiveFinmathDayCountTest {

  /**
   * Tests that the expected day count is constructed.
   */
  @Test
  public void test() {
    final FinmathDayCount finmathDayCount = new ActThreeSixtyFiveFinmathDayCount();
    assertEquals(finmathDayCount.getName(), "Act/365");
    final LocalDate firstDate = new LocalDate(2012, 1, 1);
    final LocalDate secondDate = new LocalDate(2012, 7, 31);
    final DayCountConventionInterface convention = finmathDayCount.getConvention();
    final double dayCount = convention.getDaycount(firstDate, secondDate);
    final double dayCountFraction = convention.getDaycountFraction(firstDate, secondDate);
    final DayCountConvention_ACT_365 newInstance = new DayCountConvention_ACT_365();
    assertEquals(dayCount, newInstance.getDaycount(firstDate, secondDate));
    assertEquals(dayCount, 212.);
    assertEquals(dayCountFraction, newInstance.getDaycountFraction(firstDate, secondDate));
    assertEquals(dayCountFraction, 212 / 365.);
  }
}
