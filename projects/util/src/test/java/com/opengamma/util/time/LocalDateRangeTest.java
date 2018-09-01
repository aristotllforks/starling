/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.util.time;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import org.threeten.bp.LocalDate;

import com.opengamma.util.test.TestGroup;

/**
 * Test {@link LocalDateRange}.
 */
@Test(groups = TestGroup.UNIT)
public class LocalDateRangeTest {

  /**
   * Tests the ALL range.
   */
  @Test
  public void testAll() {
    final LocalDateRange test = LocalDateRange.ALL;
    assertEquals(test.getStartDateInclusive(), LocalDate.MIN);
    assertEquals(test.getEndDateInclusive(), LocalDate.MAX);
    assertEquals(test.getEndDateExclusive(), LocalDate.MAX);
    assertTrue(test.isStartDateMinimum());
    assertTrue(test.isEndDateMaximum());
  }

  //-------------------------------------------------------------------------
  /**
   * Tests creation with inclusive end.
   */
  @Test
  public void testOfTrue() {
    final LocalDateRange test = LocalDateRange.of(LocalDate.of(2012, 7, 28), LocalDate.of(2012, 7, 30), true);
    assertEquals(test.getStartDateInclusive(), LocalDate.of(2012, 7, 28));
    assertEquals(test.getEndDateInclusive(), LocalDate.of(2012, 7, 30));
    assertEquals(test.getEndDateExclusive(), LocalDate.of(2012, 7, 31));
    assertFalse(test.isStartDateMinimum());
    assertFalse(test.isEndDateMaximum());
  }

  /**
   * Tests creation with exclusive end.
   */
  @Test
  public void testOfFalse() {
    final LocalDateRange test = LocalDateRange.of(LocalDate.of(2012, 7, 28), LocalDate.of(2012, 7, 30), false);
    assertEquals(test.getStartDateInclusive(), LocalDate.of(2012, 7, 28));
    assertEquals(test.getEndDateInclusive(), LocalDate.of(2012, 7, 29));
    assertEquals(test.getEndDateExclusive(), LocalDate.of(2012, 7, 30));
    assertFalse(test.isStartDateMinimum());
    assertFalse(test.isEndDateMaximum());
  }

  /**
   * Tests that the first date cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testOfNull1() {
    LocalDateRange.of(null, LocalDate.of(2012, 7, 30), false);
  }

  /**
   * Tests that the second date cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testOfNull2() {
    LocalDateRange.of(LocalDate.of(2012, 7, 28), null, false);
  }

  /**
   * Tests that the dates must be in order.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testOfOrder() {
    LocalDateRange.of(LocalDate.of(2012, 7, 30), LocalDate.of(2012, 7, 20), false);
  }

  //-------------------------------------------------------------------------
  /**
   * Tests the ofNullUnbounded() method with inclusive end.
   */
  @Test
  public void testOfNullUnboundedTrue() {
    final LocalDateRange test = LocalDateRange.ofNullUnbounded(LocalDate.of(2012, 7, 28), LocalDate.of(2012, 7, 30), true);
    assertEquals(test.getStartDateInclusive(), LocalDate.of(2012, 7, 28));
    assertEquals(test.getEndDateInclusive(), LocalDate.of(2012, 7, 30));
    assertEquals(test.getEndDateExclusive(), LocalDate.of(2012, 7, 31));
    assertEquals(test.getEndDateExclusive(), LocalDate.of(2012, 7, 31));
    assertFalse(test.isStartDateMinimum());
    assertFalse(test.isEndDateMaximum());
  }

  /**
   * Tests the ofNullUnbounded() method with exclusive end.
   */
  @Test
  public void testOfNullUnboundedFalse() {
    final LocalDateRange test = LocalDateRange.ofNullUnbounded(LocalDate.of(2012, 7, 28), LocalDate.of(2012, 7, 30), false);
    assertEquals(test.getStartDateInclusive(), LocalDate.of(2012, 7, 28));
    assertEquals(test.getEndDateInclusive(), LocalDate.of(2012, 7, 29));
    assertEquals(test.getEndDateExclusive(), LocalDate.of(2012, 7, 30));
    assertEquals(test.getEndDateExclusive(), LocalDate.of(2012, 7, 30));
    assertFalse(test.isStartDateMinimum());
    assertFalse(test.isEndDateMaximum());
  }

  /**
   * Tests that a null start date sets the start of the range to the minimum date.
   */
  @Test
  public void testOfNullUnboundedNull1() {
    final LocalDateRange test = LocalDateRange.ofNullUnbounded(null, LocalDate.of(2012, 7, 30), false);
    assertEquals(test.getStartDateInclusive(), LocalDate.MIN);
    assertEquals(test.getEndDateInclusive(), LocalDate.of(2012, 7, 29));
    assertEquals(test.getEndDateExclusive(), LocalDate.of(2012, 7, 30));
    assertTrue(test.isStartDateMinimum());
    assertFalse(test.isEndDateMaximum());
  }

  /**
   * Tests that a null end date sets the end of the range to the maximum date.
   */
  @Test
  public void testOfNullUnboundedNull2() {
    final LocalDateRange test = LocalDateRange.ofNullUnbounded(LocalDate.of(2012, 7, 28), null, false);
    assertEquals(test.getStartDateInclusive(), LocalDate.of(2012, 7, 28));
    assertEquals(test.getEndDateInclusive(), LocalDate.MAX);
    assertEquals(test.getEndDateExclusive(), LocalDate.MAX);
    assertFalse(test.isStartDateMinimum());
    assertTrue(test.isEndDateMaximum());
  }

  /**
   * Tests that the dates must be in order.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testOfNullUnboundedOrder() {
    LocalDateRange.ofNullUnbounded(LocalDate.of(2012, 7, 30), LocalDate.of(2012, 7, 20), false);
  }

  //-------------------------------------------------------------------------
  /**
   * Tests the adjustment of the start date when the start is unbounded.
   */
  @Test
  public void testResolveStartUnbounded() {
    final LocalDateRange base = LocalDateRange.ofNullUnbounded(null, LocalDate.of(2012, 7, 30), false);
    final LocalDateRange test = base.resolveUnboundedStartDate(LocalDate.of(2012, 7, 28));
    assertEquals(LocalDate.of(2012, 7, 28), test.getStartDateInclusive());
    assertEquals(LocalDate.of(2012, 7, 29), test.getEndDateInclusive());
    assertEquals(LocalDate.of(2012, 7, 30), test.getEndDateExclusive());
    assertEquals(false, test.isStartDateMinimum());
    assertEquals(false, test.isEndDateMaximum());
  }

  /**
   * Tests the adjustment of the start date when the start is bounded.
   */
  @Test
  public void testResolveStartBounded() {
    final LocalDateRange base = LocalDateRange.ofNullUnbounded(LocalDate.of(2012, 6, 28), LocalDate.of(2012, 7, 30), false);
    final LocalDateRange test = base.resolveUnboundedStartDate(LocalDate.of(2012, 7, 28));
    assertEquals(LocalDate.of(2012, 6, 28), test.getStartDateInclusive());
    assertEquals(LocalDate.of(2012, 7, 29), test.getEndDateInclusive());
    assertEquals(LocalDate.of(2012, 7, 30), test.getEndDateExclusive());
    assertEquals(false, test.isStartDateMinimum());
    assertEquals(false, test.isEndDateMaximum());
  }

  /**
   * Tests the adjustment of the end date.
   */
  @Test
  public void testResolveEndUnbounded() {
    final LocalDateRange base = LocalDateRange.ofNullUnbounded(LocalDate.of(2012, 7, 28), null, false);
    final LocalDateRange test = base.resolveUnboundedEndDate(LocalDate.of(2012, 7, 30), false);
    assertEquals(LocalDate.of(2012, 7, 28), test.getStartDateInclusive());
    assertEquals(LocalDate.of(2012, 7, 29), test.getEndDateInclusive());
    assertEquals(LocalDate.of(2012, 7, 30), test.getEndDateExclusive());
    assertEquals(false, test.isStartDateMinimum());
    assertEquals(false, test.isEndDateMaximum());
  }

  /**
   * Tests the adjustment of the end date when the end is bounded.
   */
  @Test
  public void testResolveEndBounded() {
    final LocalDateRange base = LocalDateRange.ofNullUnbounded(LocalDate.of(2012, 7, 28), LocalDate.of(2013, 1, 1), false);
    final LocalDateRange test = base.resolveUnboundedEndDate(LocalDate.of(2012, 7, 30), false);
    assertEquals(LocalDate.of(2012, 7, 28), test.getStartDateInclusive());
    assertEquals(LocalDate.of(2012, 12, 31), test.getEndDateInclusive());
    assertEquals(LocalDate.of(2013, 1, 1), test.getEndDateExclusive());
    assertEquals(false, test.isStartDateMinimum());
    assertEquals(false, test.isEndDateMaximum());
  }
//  /**
//   * Tests the bean.
//   */
//  @Test
//  public void testBean() {
//    final DoublesPair pair = DoublesPair.of(2., 3.);
//    assertNotNull(pair.metaBean());
//    assertNotNull(pair.metaBean().first());
//    assertNotNull(pair.metaBean().second());
//    assertEquals(pair.metaBean().first().get(pair), 2.);
//    assertEquals(pair.metaBean().second().get(pair), 3.);
//    assertEquals(pair.property("first").get(), 2.);
//    assertEquals(pair.property("second").get(), 3.);
//  }

}
