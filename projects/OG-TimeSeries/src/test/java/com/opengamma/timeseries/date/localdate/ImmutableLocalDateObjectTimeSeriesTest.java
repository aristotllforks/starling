/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.timeseries.date.localdate;

import static org.testng.AssertJUnit.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.testng.annotations.Test;
import org.threeten.bp.LocalDate;

import com.opengamma.timeseries.ObjectTimeSeries;
import com.opengamma.timeseries.object.LocalDateObjectTimeSeriesTest;

/**
 * Test.
 */
@Test(groups = "unit")
public class ImmutableLocalDateObjectTimeSeriesTest extends LocalDateObjectTimeSeriesTest {

  @Override
  protected LocalDateObjectTimeSeries<BigDecimal> createEmptyTimeSeries() {
    return ImmutableLocalDateObjectTimeSeries.ofEmpty();
  }

  protected LocalDateObjectTimeSeries<BigDecimal> createStandardTimeSeries() {
    return (LocalDateObjectTimeSeries<BigDecimal>) super.createStandardTimeSeries();
  }

  protected LocalDateObjectTimeSeries<BigDecimal> createStandardTimeSeries2() {
    return (LocalDateObjectTimeSeries<BigDecimal>) super.createStandardTimeSeries2();
  }

  @Override
  protected ObjectTimeSeries<LocalDate, BigDecimal> createTimeSeries(LocalDate[] times, BigDecimal[] values) {
    return ImmutableLocalDateObjectTimeSeries.of(times, values);
  }

  @Override
  protected LocalDateObjectTimeSeries<BigDecimal> createTimeSeries(List<LocalDate> times, List<BigDecimal> values) {
    return ImmutableLocalDateObjectTimeSeries.of(times, values);
  }

  @Override
  protected ObjectTimeSeries<LocalDate, BigDecimal> createTimeSeries(ObjectTimeSeries<LocalDate, BigDecimal> dts) {
    return ImmutableLocalDateObjectTimeSeries.from(dts);
  }

//  //-------------------------------------------------------------------------
//  //-------------------------------------------------------------------------
//  //-------------------------------------------------------------------------
//  public void test_of_LD_double() {
//    LocalDateDoubleTimeSeries ts= ImmutableLocalDateDoubleTimeSeries.of(LocalDate.of(2012, 6, 30), 2.0);
//    assertEquals(ts.size(), 1);
//    assertEquals(ts.getTimeAtIndex(0), LocalDate.of(2012, 6, 30));
//    assertEquals(ts.getValueAtIndex(0), 2.0);
//  }
//
//  @Test(expectedExceptions = NullPointerException.class)
//  public void test_of_LD_double_null() {
//    ImmutableLocalDateDoubleTimeSeries.of((LocalDate) null, 2.0);
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_of_LDArray_DoubleArray() {
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    Double[] inValues = new Double[] {2.0, 3.0};
//    LocalDateDoubleTimeSeries ts= ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    assertEquals(ts.size(), 2);
//    assertEquals(ts.getTimeAtIndex(0), LocalDate.of(2012, 6, 30));
//    assertEquals(ts.getValueAtIndex(0), 2.0);
//    assertEquals(ts.getTimeAtIndex(1), LocalDate.of(2012, 7, 1));
//    assertEquals(ts.getValueAtIndex(1), 3.0);
//  }
//
//  @Test(expectedExceptions = IllegalArgumentException.class)
//  public void test_of_LDArray_DoubleArray_wrongOrder() {
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1), LocalDate.of(2012, 6, 1)};
//    Double[] inValues = new Double[] {2.0, 3.0, 1.0};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//  }
//
//  @Test(expectedExceptions = IllegalArgumentException.class)
//  public void test_of_LDArray_DoubleArray_mismatchedArrays() {
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30)};
//    double[] inValues = new double[] {2.0, 3.0};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//  }
//
//  @Test(expectedExceptions = NullPointerException.class)
//  public void test_of_LDArray_DoubleArray_nullDates() {
//    Double[] inValues = new Double[] {2.0, 3.0, 1.0};
//    ImmutableLocalDateDoubleTimeSeries.of((LocalDate[]) null, inValues);
//  }
//
//  @Test(expectedExceptions = NullPointerException.class)
//  public void test_of_LDArray_DoubleArray_nullValues() {
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1), LocalDate.of(2012, 6, 1)};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, (Double[]) null);
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_of_LDArray_doubleArray() {
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] inValues = new double[] {2.0, 3.0};
//    LocalDateDoubleTimeSeries ts= ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    assertEquals(ts.size(), 2);
//    assertEquals(ts.getTimeAtIndex(0), LocalDate.of(2012, 6, 30));
//    assertEquals(ts.getValueAtIndex(0), 2.0);
//    assertEquals(ts.getTimeAtIndex(1), LocalDate.of(2012, 7, 1));
//    assertEquals(ts.getValueAtIndex(1), 3.0);
//  }
//
//  @Test(expectedExceptions = IllegalArgumentException.class)
//  public void test_of_LDArray_doubleArray_wrongOrder() {
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1), LocalDate.of(2012, 6, 1)};
//    double[] inValues = new double[] {2.0, 3.0, 1.0};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//  }
//
//  @Test(expectedExceptions = IllegalArgumentException.class)
//  public void test_of_LDArray_doubleArray_mismatchedArrays() {
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30)};
//    double[] inValues = new double[] {2.0, 3.0};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//  }
//
//  @Test(expectedExceptions = NullPointerException.class)
//  public void test_of_LDArray_doubleArray_nullDates() {
//    double[] inValues = new double[] {2.0, 3.0, 1.0};
//    ImmutableLocalDateDoubleTimeSeries.of((LocalDate[]) null, inValues);
//  }
//
//  @Test(expectedExceptions = NullPointerException.class)
//  public void test_of_LDArray_doubleArray_nullValues() {
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1), LocalDate.of(2012, 6, 1)};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, (double[]) null);
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_of_intArray_doubleArray() {
//    int[] inDates = new int[] {20120630, 20120701};
//    double[] inValues = new double[] {2.0, 3.0};
//    LocalDateDoubleTimeSeries ts= ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    assertEquals(ts.size(), 2);
//    assertEquals(ts.getTimeAtIndex(0), LocalDate.of(2012, 6, 30));
//    assertEquals(ts.getValueAtIndex(0), 2.0);
//    assertEquals(ts.getTimeAtIndex(1), LocalDate.of(2012, 7, 1));
//    assertEquals(ts.getValueAtIndex(1), 3.0);
//  }
//
//  @Test(expectedExceptions = IllegalArgumentException.class)
//  public void test_of_intArray_doubleArray_wrongOrder() {
//    int[] inDates = new int[] {20120630, 20120701, 20120601};
//    double[] inValues = new double[] {2.0, 3.0, 1.0};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//  }
//
//  @Test(expectedExceptions = IllegalArgumentException.class)
//  public void test_of_intArray_doubleArray_mismatchedArrays() {
//    int[] inDates = new int[] {20120630};
//    double[] inValues = new double[] {2.0, 3.0};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//  }
//
//  @Test(expectedExceptions = NullPointerException.class)
//  public void test_of_intArray_doubleArray_nullDates() {
//    double[] inValues = new double[] {2.0, 3.0, 1.0};
//    ImmutableLocalDateDoubleTimeSeries.of((int[]) null, inValues);
//  }
//
//  @Test(expectedExceptions = NullPointerException.class)
//  public void test_of_intArray_doubleArray_nullValues() {
//    int[] inDates = new int[] {20120630, 20120701};
//    ImmutableLocalDateDoubleTimeSeries.of(inDates, (double[]) null);
//  }
//
//  //-------------------------------------------------------------------------
//  @Test
//  public void test_intersectionFirstValue_selectFirst() {
//    final LocalDateDoubleTimeSeries dts = createStandardTimeSeries();
//    final LocalDateDoubleTimeSeries dts2 = createStandardTimeSeries2();
//    final LocalDateDoubleTimeSeries dts3 = ImmutableLocalDateDoubleTimeSeries.builder()
//        .putAll(dts2).put(dts2.getEarliestTime(), -1.0).build();
//    
//    final LocalDateDoubleTimeSeries result1 = dts.intersectionFirstValue(dts3);
//    assertEquals(3, result1.size());
//    assertEquals(Double.valueOf(4.0), result1.getValueAtIndex(0));
//    assertEquals(Double.valueOf(5.0), result1.getValueAtIndex(1));
//    assertEquals(Double.valueOf(6.0), result1.getValueAtIndex(2));
//    assertEquals(dts.getTimeAtIndex(3), result1.getTimeAtIndex(0));
//    assertEquals(dts.getTimeAtIndex(4), result1.getTimeAtIndex(1));
//    assertEquals(dts.getTimeAtIndex(5), result1.getTimeAtIndex(2));
//    
//    final LocalDateDoubleTimeSeries result2 = dts3.intersectionFirstValue(dts);
//    assertEquals(3, result2.size());
//    assertEquals(Double.valueOf(-1.0), result2.getValueAtIndex(0));
//    assertEquals(Double.valueOf(5.0), result2.getValueAtIndex(1));
//    assertEquals(Double.valueOf(6.0), result2.getValueAtIndex(2));
//    assertEquals(dts.getTimeAtIndex(3), result2.getTimeAtIndex(0));
//    assertEquals(dts.getTimeAtIndex(4), result2.getTimeAtIndex(1));
//    assertEquals(dts.getTimeAtIndex(5), result2.getTimeAtIndex(2));
//  }
//
//  @Test
//  public void test_intersectionSecondValue_selectSecond() {
//    final LocalDateDoubleTimeSeries dts = createStandardTimeSeries();
//    final LocalDateDoubleTimeSeries dts2 = createStandardTimeSeries2();
//    final LocalDateDoubleTimeSeries dts3 = ImmutableLocalDateDoubleTimeSeries.builder()
//        .putAll(dts2).put(dts2.getEarliestTime(), -1.0).build();
//    
//    final LocalDateDoubleTimeSeries result2 = dts.intersectionSecondValue(dts3);
//    assertEquals(3, result2.size());
//    assertEquals(Double.valueOf(-1.0), result2.getValueAtIndex(0));
//    assertEquals(Double.valueOf(5.0), result2.getValueAtIndex(1));
//    assertEquals(Double.valueOf(6.0), result2.getValueAtIndex(2));
//    assertEquals(dts.getTimeAtIndex(3), result2.getTimeAtIndex(0));
//    assertEquals(dts.getTimeAtIndex(4), result2.getTimeAtIndex(1));
//    assertEquals(dts.getTimeAtIndex(5), result2.getTimeAtIndex(2));
//    
//    final LocalDateDoubleTimeSeries result1 = dts3.intersectionSecondValue(dts);
//    assertEquals(3, result1.size());
//    assertEquals(Double.valueOf(4.0), result1.getValueAtIndex(0));
//    assertEquals(Double.valueOf(5.0), result1.getValueAtIndex(1));
//    assertEquals(Double.valueOf(6.0), result1.getValueAtIndex(2));
//    assertEquals(dts.getTimeAtIndex(3), result1.getTimeAtIndex(0));
//    assertEquals(dts.getTimeAtIndex(4), result1.getTimeAtIndex(1));
//    assertEquals(dts.getTimeAtIndex(5), result1.getTimeAtIndex(2));
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_toString() {
//    LocalDateDoubleTimeSeries ts= ImmutableLocalDateDoubleTimeSeries.of(LocalDate.of(2012, 6, 30), 2.0);
//    assertEquals("ImmutableLocalDateDoubleTimeSeries[(2012-06-30, 2.0)]", ts.toString());
//  }
//
//  //-------------------------------------------------------------------------
//  //-------------------------------------------------------------------------
//  //-------------------------------------------------------------------------
//  public void test_builder_nothingAdded() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.EMPTY_SERIES, bld.build());
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_builder_put_LD() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    bld.put(LocalDate.of(2012, 6, 30), 2.0).put(LocalDate.of(2012, 7, 1), 3.0).put(LocalDate.of(2012, 6, 1), 1.0);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 1), LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {1.0, 2.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  public void test_builder_put_LD_alreadyThere() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    bld.put(LocalDate.of(2012, 6, 30), 2.0).put(LocalDate.of(2012, 7, 1), 3.0).put(LocalDate.of(2012, 6, 30), 1.0);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {1.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_builder_put_int() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    bld.put(20120630, 2.0).put(20120701, 3.0).put(20120601, 1.0);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 1), LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {1.0, 2.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  public void test_builder_put_int_alreadyThere() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    bld.put(20120630, 2.0).put(20120701, 3.0).put(20120630, 1.0);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {1.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  public void test_builder_put_int_big() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] outDates = new int[600];
//    double[] outValues = new double[600];
//    for (int i = 0; i < 600; i++) {
//      bld.put(20120630 + i, i);
//      outDates[i] = 20120630 + i;
//      outValues[i] = i;
//    }
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_builder_putAll_LD() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1), LocalDate.of(2012, 6, 1)};
//    double[] inValues = new double[] {2.0, 3.0, 1.0};
//    bld.putAll(inDates, inValues);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 1), LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {1.0, 2.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  @Test(expectedExceptions = IllegalArgumentException.class)
//  public void test_builder_putAll_LD_mismatchedArrays() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    LocalDate[] inDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] inValues = new double[] {2.0, 3.0, 1.0};
//    bld.putAll(inDates, inValues);
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_builder_putAll_int() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120630, 20120701, 20120601};
//    double[] inValues = new double[] {2.0, 3.0, 1.0};
//    bld.putAll(inDates, inValues);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 1), LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {1.0, 2.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  @Test(expectedExceptions = IllegalArgumentException.class)
//  public void test_builder_putAll_int_mismatchedArrays() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120630, 20120701};
//    double[] inValues = new double[] {2.0, 3.0, 1.0};
//    bld.putAll(inDates, inValues);
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_builder_putAll_DDTS() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.putAll(ddts);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 1), LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {1.0, 2.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_builder_putAll_DDTS_range_allNonEmptyBuilder() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.put(LocalDate.of(2012, 5, 1), 0.5).putAll(ddts, 0, 3);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 5, 1), LocalDate.of(2012, 6, 1), LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {0.5, 1.0, 2.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  public void test_builder_putAll_DDTS_range_fromStart() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.putAll(ddts, 0, 1);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 1)};
//    double[] outValues = new double[] {1.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  public void test_builder_putAll_DDTS_range_toEnd() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.putAll(ddts, 1, 3);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {2.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  public void test_builder_putAll_DDTS_range_empty() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.put(LocalDate.of(2012, 5, 1), 0.5).putAll(ddts, 1, 1);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 5, 1)};
//    double[] outValues = new double[] {0.5};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  @Test(expectedExceptions = IndexOutOfBoundsException.class)
//  public void test_builder_putAll_DDTS_range_startInvalidLow() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.putAll(ddts, -1, 3);
//  }
//
//  @Test(expectedExceptions = IndexOutOfBoundsException.class)
//  public void test_builder_putAll_DDTS_range_startInvalidHigh() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.putAll(ddts, 4, 2);
//  }
//
//  @Test(expectedExceptions = IndexOutOfBoundsException.class)
//  public void test_builder_putAll_DDTS_range_endInvalidLow() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.putAll(ddts, 1, -1);
//  }
//
//  @Test(expectedExceptions = IndexOutOfBoundsException.class)
//  public void test_builder_putAll_DDTS_range_endInvalidHigh() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.putAll(ddts, 3, 4);
//  }
//
//  @Test(expectedExceptions = IndexOutOfBoundsException.class)
//  public void test_builder_putAll_DDTS_range_startEndOrder() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    int[] inDates = new int[] {20120601, 20120630, 20120701};
//    double[] inValues = new double[] {1.0, 2.0, 3.0};
//    DateDoubleTimeSeries<?> ddts = ImmutableLocalDateDoubleTimeSeries.of(inDates, inValues);
//    bld.putAll(ddts, 3, 2);
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_builder_putAll_Map() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    Map<LocalDate, Double> map = new HashMap<>();
//    map.put(LocalDate.of(2012, 6, 30), 2.0d);
//    map.put(LocalDate.of(2012, 7, 1), 3.0d);
//    map.put(LocalDate.of(2012, 6, 1), 1.0d);
//    bld.putAll(map);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 6, 1), LocalDate.of(2012, 6, 30), LocalDate.of(2012, 7, 1)};
//    double[] outValues = new double[] {1.0, 2.0, 3.0};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  public void test_builder_putAll_Map_empty() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    Map<LocalDate, Double> map = new HashMap<>();
//    bld.put(LocalDate.of(2012, 5, 1), 0.5).putAll(map);
//    LocalDate[] outDates = new LocalDate[] {LocalDate.of(2012, 5, 1)};
//    double[] outValues = new double[] {0.5};
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.of(outDates, outValues), bld.build());
//  }
//
//  //-------------------------------------------------------------------------
//  public void test_builder_clearEmpty() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    bld.clear();
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.EMPTY_SERIES, bld.build());
//  }
//
//  public void test_builder_clearSomething() {
//    LocalDateDoubleTimeSeriesBuilder bld = ImmutableLocalDateDoubleTimeSeries.builder();
//    bld.put(20120630, 1.0).clear();
//    assertEquals(ImmutableLocalDateDoubleTimeSeries.EMPTY_SERIES, bld.build());
//  }

  //-------------------------------------------------------------------------
  public void test_builder_toString() {
    LocalDateObjectTimeSeriesBuilder<BigDecimal> bld = ImmutableLocalDateObjectTimeSeries.builder();
    assertEquals("Builder[size=1]", bld.put(20120630, BigDecimal.valueOf(1.0)).toString());
  }

}
