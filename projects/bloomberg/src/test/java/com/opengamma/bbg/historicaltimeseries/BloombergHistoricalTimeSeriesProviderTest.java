/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg.historicaltimeseries;

import static com.opengamma.bbg.BloombergConstants.BLOOMBERG_DATA_SOURCE_NAME;
import static com.opengamma.bbg.BloombergConstants.DEFAULT_DATA_PROVIDER;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.threeten.bp.Clock;
import org.threeten.bp.LocalDate;

import com.opengamma.bbg.BloombergConnector;
import com.opengamma.bbg.test.BloombergTestUtils;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.provider.historicaltimeseries.HistoricalTimeSeriesProviderGetRequest;
import com.opengamma.timeseries.date.localdate.LocalDateDoubleTimeSeries;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.time.DateUtils;
import com.opengamma.util.time.LocalDateRange;

/**
 * Test.
 */
@Test(groups = TestGroup.INTEGRATION)
public class BloombergHistoricalTimeSeriesProviderTest {

  private static final ExternalIdBundle SIMPLE_BUNDLE = ExternalIdBundle.of(ExternalSchemes.bloombergTickerSecurityId("IBM US Equity"));
  private static final ExternalIdBundle COMPLEX_BUNDLE = ExternalIdBundle.of(
      ExternalId.of("BLOOMBERG_BUID", "EQ0010121400001000"), ExternalId.of("BLOOMBERG_TICKER", "C US Equity"),
      ExternalId.of("CUSIP", "172967101"), ExternalId.of("ISIN", "US1729671016"), ExternalId.of("SEDOL1", "2297907"));
  private static final String BBG_DATA_SOURCE = BLOOMBERG_DATA_SOURCE_NAME;
  private static final String PX_LAST = "PX_LAST";

  private BloombergHistoricalTimeSeriesProvider _provider;

  @BeforeMethod
  public void setUp() throws Exception {
    final BloombergConnector connector = BloombergTestUtils.getBloombergConnector();
    final BloombergHistoricalTimeSeriesProvider provider = new BloombergHistoricalTimeSeriesProvider(connector);

    provider.start();
    _provider = provider;
  }

  @AfterMethod
  public void tearDown() throws Exception {
    if (_provider != null) {
      _provider.stop();
    }
    _provider = null;
  }

  //-------------------------------------------------------------------------
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_getHistoricalTimeSeries_single_wrongDataSource() {
    final LocalDateRange range = LocalDateRange.of(LocalDate.of(2009, 11, 4), LocalDate.of(2009, 11, 4), true);
    _provider.getHistoricalTimeSeries(SIMPLE_BUNDLE, "RUBBISH", DEFAULT_DATA_PROVIDER, PX_LAST, range);
  }

  @Test
  public void test_getHistoricalTimeSeries_single_sameDate() {
    final LocalDateRange range = LocalDateRange.of(LocalDate.of(2009, 11, 4), LocalDate.of(2009, 11, 4), true);
    final LocalDateDoubleTimeSeries test = _provider.getHistoricalTimeSeries(SIMPLE_BUNDLE, BBG_DATA_SOURCE, DEFAULT_DATA_PROVIDER, PX_LAST, range);
    assertNotNull(test);
    assertEquals(1, test.size());
  }

  @Test
  public void getHistoricalTimeSeriesWithZeroMaxPoints() throws Exception {
    final LocalDate endDate = LocalDate.of(2012, 03, 07);
    final LocalDate startDate = endDate.minusMonths(1);

    final LocalDateRange dateRange = LocalDateRange.of(startDate, endDate, true);
    final HistoricalTimeSeriesProviderGetRequest realRequest = HistoricalTimeSeriesProviderGetRequest.createGet(SIMPLE_BUNDLE, BBG_DATA_SOURCE, DEFAULT_DATA_PROVIDER, PX_LAST, dateRange);
    realRequest.setMaxPoints(0);
    final LocalDateDoubleTimeSeries realHts = _provider.getHistoricalTimeSeries(realRequest).getResultMap().get(SIMPLE_BUNDLE);

    assertNotNull(realHts);
    assertEquals(0, realHts.size());
  }

  @Test
  public void getHistoricalTimeSeriesWithAllMaxPoints() throws Exception {
    final LocalDate endDate = LocalDate.of(2012, 03, 07);
    final LocalDate startDate = endDate.minusMonths(1);

    final LocalDateRange dateRange = LocalDateRange.of(startDate, endDate, true);
    final HistoricalTimeSeriesProviderGetRequest referenceRequest = HistoricalTimeSeriesProviderGetRequest.createGet(SIMPLE_BUNDLE, BBG_DATA_SOURCE, DEFAULT_DATA_PROVIDER, PX_LAST, dateRange);
    final LocalDateDoubleTimeSeries reference = _provider.getHistoricalTimeSeries(referenceRequest).getResultMap().get(SIMPLE_BUNDLE);
    final HistoricalTimeSeriesProviderGetRequest realRequest = HistoricalTimeSeriesProviderGetRequest.createGet(SIMPLE_BUNDLE, BBG_DATA_SOURCE, DEFAULT_DATA_PROVIDER, PX_LAST, dateRange);
    realRequest.setMaxPoints(-9999);
    final LocalDateDoubleTimeSeries realHts = _provider.getHistoricalTimeSeries(realRequest).getResultMap().get(SIMPLE_BUNDLE);

    assertNotNull(realHts);
    assertEquals(reference.size(), realHts.size());
    assertEquals(reference, realHts);
  }

  @Test
  public void getHistoricalTimeSeriesWithTwoMaxPoints() throws Exception {
    final LocalDate endDate = LocalDate.of(2012, 03, 07);
    final LocalDate startDate = endDate.minusMonths(1);

    final LocalDateRange dateRange = LocalDateRange.of(startDate, endDate, true);
    final HistoricalTimeSeriesProviderGetRequest referenceRequest = HistoricalTimeSeriesProviderGetRequest.createGet(SIMPLE_BUNDLE, BBG_DATA_SOURCE, DEFAULT_DATA_PROVIDER, PX_LAST, dateRange);
    final LocalDateDoubleTimeSeries reference = _provider.getHistoricalTimeSeries(referenceRequest).getResultMap().get(SIMPLE_BUNDLE);
    final HistoricalTimeSeriesProviderGetRequest realRequest = HistoricalTimeSeriesProviderGetRequest.createGet(SIMPLE_BUNDLE, BBG_DATA_SOURCE, DEFAULT_DATA_PROVIDER, PX_LAST, dateRange);
    realRequest.setMaxPoints(-2);
    final LocalDateDoubleTimeSeries realHts = _provider.getHistoricalTimeSeries(realRequest).getResultMap().get(SIMPLE_BUNDLE);

    assertNotNull(realHts);
    assertEquals(2, realHts.size());
    assertEquals(reference.tail(2), realHts);
  }

  @Test
  public void test_getHistoricalTimeSeries_single_dates() throws Exception {
    final LocalDate startDate = LocalDate.of(2009, 10, 29);
    final LocalDate endDate = LocalDate.of(2009, 11, 4);
    final LocalDateRange range = LocalDateRange.of(startDate, endDate, true);
    final LocalDateDoubleTimeSeries timeSeriesExpected = _provider.getHistoricalTimeSeries(SIMPLE_BUNDLE,  BBG_DATA_SOURCE, DEFAULT_DATA_PROVIDER, PX_LAST, range);
    assertNotNull(timeSeriesExpected);

    final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    final List<Future<LocalDateDoubleTimeSeries>> results = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      results.add(threadPool.submit(new BHDPgetHistoricalTimeSeriesWithDates(SIMPLE_BUNDLE, BBG_DATA_SOURCE, DEFAULT_DATA_PROVIDER, PX_LAST, startDate, endDate)));
    }

    for (final Future<LocalDateDoubleTimeSeries> future : results) {
      final LocalDateDoubleTimeSeries timeSeriesActual = future.get();
      assertNotNull(timeSeriesActual);
      assertEquals(timeSeriesExpected, timeSeriesActual);
    }
  }

  //-------------------------------------------------------------------------
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_getHistoricalTimeSeries_Map_wrongDataSource() {
    final LocalDateRange range = LocalDateRange.of(LocalDate.of(2009, 11, 4), LocalDate.of(2009, 11, 4), true);
    _provider.getHistoricalTimeSeries(COMPLEX_BUNDLE, "RUBBISH", DEFAULT_DATA_PROVIDER, PX_LAST, range);
  }

  @Test
  public void test_getHistoricalTimeSeries_Map() {
    final LocalDateRange range = LocalDateRange.of(LocalDate.of(2009, 10, 4), LocalDate.of(2009, 11, 29), true);
    final Map<ExternalIdBundle, LocalDateDoubleTimeSeries> test = _provider.getHistoricalTimeSeries(
        Collections.singleton(COMPLEX_BUNDLE), BBG_DATA_SOURCE, "CMPL", "PX_LAST", range);
    assertNotNull(test);
    assertEquals(1, test.size());
    final LocalDateDoubleTimeSeries series = test.get(COMPLEX_BUNDLE);
    assertNotNull(series);
    assertFalse(series.isEmpty());
  }

  //-------------------------------------------------------------------------
  private class BHDPgetHistoricalTimeSeriesWithDates implements Callable<LocalDateDoubleTimeSeries> {
    private final ExternalIdBundle _secDes;
    private final String _dataSource;
    private final String _dataProvider;
    private final String _field;
    private final LocalDate _startDate;
    private final LocalDate _endDate;

    public BHDPgetHistoricalTimeSeriesWithDates(final ExternalIdBundle secDes, final String dataSource, final String dataProvider, final String field, final LocalDate startDate, final LocalDate endDate) {
      assertNotNull(secDes);
      assertNotNull(startDate);
      assertNotNull(endDate);
      _secDes = secDes;
      _dataSource = dataSource;
      _dataProvider = dataProvider;
      _field = field;
      _startDate = startDate;
      _endDate = endDate;
    }

    @Override
    public LocalDateDoubleTimeSeries call() throws Exception {
      final LocalDateRange range = LocalDateRange.of(_startDate, _endDate, true);
      return _provider.getHistoricalTimeSeries(_secDes, _dataSource, _dataProvider, _field, range);
    }
  }

  @Test
  public void test_getAllAvailiableSeries() {
    final LocalDateDoubleTimeSeries hts = _provider.getHistoricalTimeSeries(ExternalIdBundle.of(ExternalSchemes.bloombergTickerSecurityId("JPY3M Curncy")), BBG_DATA_SOURCE, "CMPL", PX_LAST);
    assertNotNull(hts);
    assertEquals(getLatestWeekDay(), hts.getLatestTime());
  }

  private LocalDate getLatestWeekDay() {
    final Clock clock = Clock.systemUTC();
    return DateUtils.previousWeekDay(LocalDate.now(clock).plusDays(1));
  }

}
