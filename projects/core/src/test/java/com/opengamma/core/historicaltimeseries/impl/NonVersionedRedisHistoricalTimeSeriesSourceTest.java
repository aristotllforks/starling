/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.historicaltimeseries.impl;

import org.testng.annotations.Test;

import com.opengamma.util.test.AbstractRedisTestCase;
import com.opengamma.util.test.TestGroup;

/**
 * Test.
 */
@Test(groups = TestGroup.UNIT)
public class NonVersionedRedisHistoricalTimeSeriesSourceTest extends AbstractRedisTestCase {
//  private MockJedisPool _pool;
//
//  @Override
//  @BeforeClass
//  public void launchJedisPool() {
//    final GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//    _pool = new MockJedisPool(config, "host");
//  }
//
//  @Override
//  @AfterClass
//  public void clearJedisPool() {
//    if (_pool == null) {
//      return;
//    }
//    _pool.getResource().close();
//    _pool.destroy();
//  }
//
//  @Override
//  @BeforeMethod
//  public void clearRedisDb() {
//    final Jedis jedis = _pool.getResource();
//    jedis.flushDB();
//    _pool.returnResource(jedis);
//  }
//
//  @Override
//  protected JedisPool getJedisPool() {
//    return _pool;
//  }
//
//  @Override
//  protected String getRedisPrefix() {
//    return "prefix";
//  }
//
//  private static final int ITER_SIZE = 50;
//
//  /**
//   * Tests updating and and getting timeseries and data points from the database.
//   */
//  public void basicOperation() {
//    final NonVersionedRedisHistoricalTimeSeriesSource source = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//
//    final UniqueId id1 = UniqueId.of("Test", "1");
//    final UniqueId id2 = UniqueId.of("Test", "2");
//    final UniqueId id3 = UniqueId.of("Test", "3");
//
//    LocalDateDoubleTimeSeriesBuilder tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 14.0);
//    tsBuilder.put(LocalDate.parse("2013-06-05"), 15.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 16.0);
//    tsBuilder.put(LocalDate.parse("2013-06-07"), 17.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 18.0);
//    source.updateTimeSeries(id1, tsBuilder.build());
//
//    tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 24.0);
//    tsBuilder.put(LocalDate.parse("2013-06-05"), 25.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 26.0);
//    tsBuilder.put(LocalDate.parse("2013-06-07"), 27.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 28.0);
//    source.updateTimeSeries(id2, tsBuilder.build());
//
//    tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 34.0);
//    tsBuilder.put(LocalDate.parse("2013-06-05"), 35.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 36.0);
//    tsBuilder.put(LocalDate.parse("2013-06-07"), 37.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 38.0);
//    source.updateTimeSeries(id3, tsBuilder.build());
//
//    Pair<LocalDate, Double> pair = null;
//    HistoricalTimeSeries hts = null;
//    LocalDateDoubleTimeSeries ts = null;
//
//    pair = source.getLatestDataPoint(id3);
//    assertNotNull(pair);
//    assertEquals(LocalDate.parse("2013-06-08"), pair.getFirst());
//    assertEquals(38.0, pair.getSecond(), 0.000001);
//
//    assertNull(source.getHistoricalTimeSeries(UniqueId.of("Test", "5")));
//
//    hts = source.getHistoricalTimeSeries(id2);
//    assertNotNull(hts);
//    assertEquals(id2, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertNotNull(ts);
//    assertEquals(5, ts.size());
//    assertEquals(24.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    assertEquals(25.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(26.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(27.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//    assertEquals(28.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//
//    hts = source.getHistoricalTimeSeries(ExternalIdBundle.of(ExternalId.of("Test", "1")), LocalDate.now(), "Data Source", "Data Provider", "Data Field");
//    assertNotNull(hts);
//    assertEquals(id1, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertNotNull(ts);
//    assertEquals(5, ts.size());
//    assertEquals(14.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    assertEquals(15.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(17.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//    assertEquals(18.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//  }
//
//  /**
//   * Tests that points can be added to time series.
//   */
//  public void updateDataPoint() {
//    final NonVersionedRedisHistoricalTimeSeriesSource source = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//
//    final UniqueId id = UniqueId.of("Test", "1");
//    HistoricalTimeSeries hts = source.getHistoricalTimeSeries(id);
//    assertNull(hts);
//
//    final LocalDateDoubleTimeSeriesBuilder tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 14.0);
//    tsBuilder.put(LocalDate.parse("2013-06-05"), 15.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 16.0);
//    tsBuilder.put(LocalDate.parse("2013-06-07"), 17.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 18.0);
//    source.updateTimeSeries(id, tsBuilder.build());
//
//    hts = source.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    LocalDateDoubleTimeSeries ts = hts.getTimeSeries();
//    assertNotNull(ts);
//    assertEquals(5, ts.size());
//
//    assertEquals(14.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    source.updateTimeSeriesPoint(id, LocalDate.parse("2013-06-04"), 13.0);
//    hts = source.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertNotNull(ts);
//    assertEquals(5, ts.size());
//    assertEquals(13.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    source.updateTimeSeriesPoint(id, LocalDate.parse("2013-06-06"), 12.0);
//    hts = source.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertNotNull(ts);
//    assertEquals(5, ts.size());
//    assertEquals(12.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//
//    assertEquals(18.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//    source.updateTimeSeriesPoint(id, LocalDate.parse("2013-06-08"), 11.0);
//    hts = source.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertNotNull(ts);
//    assertEquals(5, ts.size());
//    assertEquals(11.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//  }
//
//
//  public void appendTimeSeries() {
//    final NonVersionedRedisHistoricalTimeSeriesSource source = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//
//    final UniqueId id = UniqueId.of("Test", "1");
//    HistoricalTimeSeries hts = source.getHistoricalTimeSeries(id);
//    assertNull(hts);
//
//    LocalDateDoubleTimeSeriesBuilder tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 14.0);
//    tsBuilder.put(LocalDate.parse("2013-06-05"), 15.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 16.0);
//    tsBuilder.put(LocalDate.parse("2013-06-07"), 17.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 18.0);
//    source.updateTimeSeries(id, tsBuilder.build());
//
//    hts = source.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    LocalDateDoubleTimeSeries ts = hts.getTimeSeries();
//    assertNotNull(ts);
//    assertEquals(5, ts.size());
//
//    tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-09"), 19.0);
//    tsBuilder.put(LocalDate.parse("2013-06-10"), 20.0);
//    tsBuilder.put(LocalDate.parse("2013-06-11"), 21.0);
//    tsBuilder.put(LocalDate.parse("2013-06-12"), 22.0);
//    tsBuilder.put(LocalDate.parse("2013-06-13"), 23.0);
//    source.updateTimeSeries(id, tsBuilder.build());
//
//    hts = source.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertNotNull(ts);
//    assertEquals(10, ts.size());
//  }
//
//  public void timeseriesRange() {
//    final NonVersionedRedisHistoricalTimeSeriesSource htsSource = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//    final UniqueId id = UniqueId.of("Test", "1");
//    HistoricalTimeSeries hts = htsSource.getHistoricalTimeSeries(id);
//    assertNull(hts);
//
//    final LocalDateDoubleTimeSeriesBuilder tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 14.0);
//    tsBuilder.put(LocalDate.parse("2013-06-05"), 15.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 16.0);
//    tsBuilder.put(LocalDate.parse("2013-06-07"), 17.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 18.0);
//    tsBuilder.put(LocalDate.parse("2013-06-09"), 24.0);
//    tsBuilder.put(LocalDate.parse("2013-06-10"), 25.0);
//    tsBuilder.put(LocalDate.parse("2013-06-11"), 26.0);
//    tsBuilder.put(LocalDate.parse("2013-06-12"), 27.0);
//    tsBuilder.put(LocalDate.parse("2013-06-13"), 28.0);
//    tsBuilder.put(LocalDate.parse("2013-06-14"), 34.0);
//    tsBuilder.put(LocalDate.parse("2013-06-15"), 35.0);
//    tsBuilder.put(LocalDate.parse("2013-06-16"), 36.0);
//    tsBuilder.put(LocalDate.parse("2013-06-17"), 37.0);
//    tsBuilder.put(LocalDate.parse("2013-06-18"), 38.0);
//
//    htsSource.updateTimeSeries(id, tsBuilder.build());
//
//    hts = htsSource.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//
//    LocalDateDoubleTimeSeries ts = hts.getTimeSeries();
//    assertEquals(15, ts.size());
//    assertEquals(14.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    assertEquals(15.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(17.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//    assertEquals(18.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//    assertEquals(24.0, ts.getValue(LocalDate.parse("2013-06-09")), 0.00001);
//    assertEquals(25.0, ts.getValue(LocalDate.parse("2013-06-10")), 0.00001);
//    assertEquals(26.0, ts.getValue(LocalDate.parse("2013-06-11")), 0.00001);
//    assertEquals(27.0, ts.getValue(LocalDate.parse("2013-06-12")), 0.00001);
//    assertEquals(28.0, ts.getValue(LocalDate.parse("2013-06-13")), 0.00001);
//    assertEquals(34.0, ts.getValue(LocalDate.parse("2013-06-14")), 0.00001);
//    assertEquals(35.0, ts.getValue(LocalDate.parse("2013-06-15")), 0.00001);
//    assertEquals(36.0, ts.getValue(LocalDate.parse("2013-06-16")), 0.00001);
//    assertEquals(37.0, ts.getValue(LocalDate.parse("2013-06-17")), 0.00001);
//    assertEquals(38.0, ts.getValue(LocalDate.parse("2013-06-18")), 0.00001);
//
//    hts = htsSource.getHistoricalTimeSeries(id, LocalDate.parse("2013-06-04"), true, LocalDate.parse("2013-06-08"), true);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertEquals(5, ts.size());
//    assertEquals(14.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    assertEquals(15.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(17.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//    assertEquals(18.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//
//    hts = htsSource.getHistoricalTimeSeries(id, LocalDate.parse("2013-06-04"), false, LocalDate.parse("2013-06-08"), true);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertEquals(4, ts.size());
//    assertEquals(15.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(17.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//    assertEquals(18.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//
//    hts = htsSource.getHistoricalTimeSeries(id, LocalDate.parse("2013-06-04"), true, LocalDate.parse("2013-06-08"), false);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertEquals(4, ts.size());
//    assertEquals(14.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    assertEquals(15.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(17.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//
//    hts = htsSource.getHistoricalTimeSeries(id, LocalDate.parse("2013-06-04"), false, LocalDate.parse("2013-06-08"), false);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//    ts = hts.getTimeSeries();
//    assertEquals(3, ts.size());
//    assertEquals(15.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(17.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//
//  }
//
//  public void getLatestDataPoint() {
//    final NonVersionedRedisHistoricalTimeSeriesSource htsSource = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//    final UniqueId id = UniqueId.of("Test", "1");
//    HistoricalTimeSeries hts = htsSource.getHistoricalTimeSeries(id);
//    assertNull(hts);
//
//    final LocalDateDoubleTimeSeriesBuilder tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 14.0);
//    tsBuilder.put(LocalDate.parse("2013-06-05"), 15.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 16.0);
//    tsBuilder.put(LocalDate.parse("2013-06-07"), 17.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 18.0);
//    tsBuilder.put(LocalDate.parse("2013-06-09"), 24.0);
//    tsBuilder.put(LocalDate.parse("2013-06-10"), 25.0);
//    tsBuilder.put(LocalDate.parse("2013-06-11"), 26.0);
//    tsBuilder.put(LocalDate.parse("2013-06-12"), 27.0);
//    tsBuilder.put(LocalDate.parse("2013-06-13"), 28.0);
//    tsBuilder.put(LocalDate.parse("2013-06-14"), 34.0);
//    tsBuilder.put(LocalDate.parse("2013-06-15"), 35.0);
//    tsBuilder.put(LocalDate.parse("2013-06-16"), 36.0);
//    tsBuilder.put(LocalDate.parse("2013-06-17"), 37.0);
//    tsBuilder.put(LocalDate.parse("2013-06-18"), 38.0);
//
//    htsSource.updateTimeSeries(id, tsBuilder.build());
//
//    hts = htsSource.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//
//    final LocalDateDoubleTimeSeries ts = hts.getTimeSeries();
//    assertEquals(15, ts.size());
//    assertEquals(14.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    assertEquals(15.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(17.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//    assertEquals(18.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//    assertEquals(24.0, ts.getValue(LocalDate.parse("2013-06-09")), 0.00001);
//    assertEquals(25.0, ts.getValue(LocalDate.parse("2013-06-10")), 0.00001);
//    assertEquals(26.0, ts.getValue(LocalDate.parse("2013-06-11")), 0.00001);
//    assertEquals(27.0, ts.getValue(LocalDate.parse("2013-06-12")), 0.00001);
//    assertEquals(28.0, ts.getValue(LocalDate.parse("2013-06-13")), 0.00001);
//    assertEquals(34.0, ts.getValue(LocalDate.parse("2013-06-14")), 0.00001);
//    assertEquals(35.0, ts.getValue(LocalDate.parse("2013-06-15")), 0.00001);
//    assertEquals(36.0, ts.getValue(LocalDate.parse("2013-06-16")), 0.00001);
//    assertEquals(37.0, ts.getValue(LocalDate.parse("2013-06-17")), 0.00001);
//    assertEquals(38.0, ts.getValue(LocalDate.parse("2013-06-18")), 0.00001);
//
//    Pair<LocalDate, Double> pair = htsSource.getLatestDataPoint(id);
//    assertNotNull(pair);
//    assertEquals(LocalDate.parse("2013-06-18"), pair.getKey());
//    assertEquals(38.0, pair.getValue(), 0.00001);
//
//    pair = htsSource.getLatestDataPoint(id, LocalDate.parse("2013-06-04"), false, LocalDate.parse("2013-06-08"), false);
//    assertNotNull(pair);
//    assertEquals(LocalDate.parse("2013-06-07"), pair.getKey());
//    assertEquals(17.0, pair.getValue(), 0.00001);
//
//  }
//
//  public void replaceTimeSeries() {
//    final NonVersionedRedisHistoricalTimeSeriesSource htsSource = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//    final UniqueId id = UniqueId.of("Test", "1");
//    HistoricalTimeSeries hts = htsSource.getHistoricalTimeSeries(id);
//    assertNull(hts);
//
//    LocalDateDoubleTimeSeriesBuilder tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 14.0);
//    tsBuilder.put(LocalDate.parse("2013-06-05"), 15.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 16.0);
//    tsBuilder.put(LocalDate.parse("2013-06-07"), 17.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 18.0);
//    tsBuilder.put(LocalDate.parse("2013-06-09"), 24.0);
//    tsBuilder.put(LocalDate.parse("2013-06-10"), 25.0);
//    tsBuilder.put(LocalDate.parse("2013-06-11"), 26.0);
//    tsBuilder.put(LocalDate.parse("2013-06-12"), 27.0);
//    tsBuilder.put(LocalDate.parse("2013-06-13"), 28.0);
//    tsBuilder.put(LocalDate.parse("2013-06-14"), 34.0);
//    tsBuilder.put(LocalDate.parse("2013-06-15"), 35.0);
//    tsBuilder.put(LocalDate.parse("2013-06-16"), 36.0);
//    tsBuilder.put(LocalDate.parse("2013-06-17"), 37.0);
//    tsBuilder.put(LocalDate.parse("2013-06-18"), 38.0);
//
//    htsSource.replaceTimeSeries(id, tsBuilder.build());
//
//    hts = htsSource.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//
//    LocalDateDoubleTimeSeries ts = hts.getTimeSeries();
//    assertEquals(15, ts.size());
//    assertEquals(14.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    assertEquals(15.0, ts.getValue(LocalDate.parse("2013-06-05")), 0.00001);
//    assertEquals(16.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(17.0, ts.getValue(LocalDate.parse("2013-06-07")), 0.00001);
//    assertEquals(18.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//    assertEquals(24.0, ts.getValue(LocalDate.parse("2013-06-09")), 0.00001);
//    assertEquals(25.0, ts.getValue(LocalDate.parse("2013-06-10")), 0.00001);
//    assertEquals(26.0, ts.getValue(LocalDate.parse("2013-06-11")), 0.00001);
//    assertEquals(27.0, ts.getValue(LocalDate.parse("2013-06-12")), 0.00001);
//    assertEquals(28.0, ts.getValue(LocalDate.parse("2013-06-13")), 0.00001);
//    assertEquals(34.0, ts.getValue(LocalDate.parse("2013-06-14")), 0.00001);
//    assertEquals(35.0, ts.getValue(LocalDate.parse("2013-06-15")), 0.00001);
//    assertEquals(36.0, ts.getValue(LocalDate.parse("2013-06-16")), 0.00001);
//    assertEquals(37.0, ts.getValue(LocalDate.parse("2013-06-17")), 0.00001);
//    assertEquals(38.0, ts.getValue(LocalDate.parse("2013-06-18")), 0.00001);
//
//    tsBuilder = ImmutableLocalDateDoubleTimeSeries.builder();
//    tsBuilder.put(LocalDate.parse("2013-06-04"), 114.0);
//    tsBuilder.put(LocalDate.parse("2013-06-06"), 216.0);
//    tsBuilder.put(LocalDate.parse("2013-06-08"), 318.0);
//    tsBuilder.put(LocalDate.parse("2014-06-09"), 24.0);
//
//    htsSource.replaceTimeSeries(id, tsBuilder.build());
//
//    hts = htsSource.getHistoricalTimeSeries(id);
//    assertNotNull(hts);
//    assertEquals(id, hts.getUniqueId());
//
//    ts = hts.getTimeSeries();
//    assertEquals(4, ts.size());
//    assertEquals(114.0, ts.getValue(LocalDate.parse("2013-06-04")), 0.00001);
//    assertEquals(216.0, ts.getValue(LocalDate.parse("2013-06-06")), 0.00001);
//    assertEquals(318.0, ts.getValue(LocalDate.parse("2013-06-08")), 0.00001);
//    assertEquals(24.0, ts.getValue(LocalDate.parse("2014-06-09")), 0.00001);
//  }
//
//  /**
//   * Test how fast we can add large historical timeseries adding one data point at a time.
//   */
//  @Test(enabled = false)
//  public void largePerformanceTestOneDataPoint() {
//    final NonVersionedRedisHistoricalTimeSeriesSource source = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//    final HistoricalTimeSeries hts = createSampleHts();
//    final long start = System.nanoTime();
//    final LocalDateDoubleTimeSeries timeSeries = hts.getTimeSeries();
//    for (final Entry<LocalDate, Double> entry : timeSeries) {
//      source.updateTimeSeriesPoint(hts.getUniqueId(), entry.getKey(), entry.getValue());
//    }
//    final long end = System.nanoTime();
//    final double durationInSec = (end - start) / 1e9;
//    System.out.println("Adding " + hts.getTimeSeries().size() + " datapoints took " + durationInSec + " sec");
//    final HistoricalTimeSeries storedHts = source.getHistoricalTimeSeries(hts.getUniqueId());
//    assertNotNull(storedHts);
//    assertEquals(hts.getUniqueId(), storedHts.getUniqueId());
//    assertEquals(hts.getTimeSeries(), storedHts.getTimeSeries());
//  }
//
//  /**
//   * Test how fast we can add large historical timeseries using bulk insert.
//   */
//  @Test(enabled = false)
//  public void largePerformanceTestBulkInsert() {
//    final NonVersionedRedisHistoricalTimeSeriesSource source = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//    double totalDurationInSec = 0.0;
//    HistoricalTimeSeries hts = null;
//    for (int i = 0; i < ITER_SIZE; i++) {
//      hts = createSampleHts();
//      final long start = System.nanoTime();
//      source.updateTimeSeries(hts.getUniqueId(), hts.getTimeSeries());
//      totalDurationInSec  += (System.nanoTime() - start) / 1e9;
//      final HistoricalTimeSeries storedHts = source.getHistoricalTimeSeries(hts.getUniqueId());
//      assertNotNull(storedHts);
//      assertEquals(hts.getUniqueId(), storedHts.getUniqueId());
//      assertEquals(hts.getTimeSeries(), storedHts.getTimeSeries());
//    }
//    System.out.println("Adding " + (hts == null ? 0 : hts.getTimeSeries().size()) + " datapoints took " + totalDurationInSec / ITER_SIZE + " sec");
//  }
//
//  @Test(enabled = false)
//  public void largePerformanceTestRead() {
//    final NonVersionedRedisHistoricalTimeSeriesSource source = new NonVersionedRedisHistoricalTimeSeriesSource(getJedisPool(), getRedisPrefix());
//    HistoricalTimeSeries hts = null;
//    double totalDurationInSec = 0.0;
//    for (int i = 0; i < ITER_SIZE; i++) {
//      hts = createSampleHts();
//      source.updateTimeSeries(hts.getUniqueId(), hts.getTimeSeries());
//      final long start = System.nanoTime();
//      final HistoricalTimeSeries storedHts = source.getHistoricalTimeSeries(hts.getUniqueId());
//      totalDurationInSec += (System.nanoTime() - start) / 1e9;
//      assertNotNull(storedHts);
//      assertEquals(hts.getUniqueId(), storedHts.getUniqueId());
//      assertEquals(hts.getTimeSeries(), storedHts.getTimeSeries());
//    }
//    System.out.println("Reading " + (hts == null ? "0" : hts.getTimeSeries().size()) + " datapoints took " + totalDurationInSec / ITER_SIZE + " sec");
//  }
//
//  private static HistoricalTimeSeries createSampleHts() {
//    final UniqueId id = UniqueId.of("HTS", UUID.randomUUID().toString());
//    final LocalDateDoubleTimeSeriesBuilder builder = ImmutableLocalDateDoubleTimeSeries.builder();
//    final LocalDate start = LocalDate.now();
//    for (int i = 0; i < 50000; i++) {
//      builder.put(start.plusDays(i), Math.random());
//    }
//    return new SimpleHistoricalTimeSeries(id, builder.build());
//  }
//
}
