/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.provider.historicaltimeseries.impl;

import java.util.Map;
import java.util.Set;

import org.threeten.bp.LocalDate;

import com.opengamma.id.ExternalIdBundle;
import com.opengamma.provider.historicaltimeseries.HistoricalTimeSeriesProvider;
import com.opengamma.provider.historicaltimeseries.HistoricalTimeSeriesProviderGetRequest;
import com.opengamma.provider.historicaltimeseries.HistoricalTimeSeriesProviderGetResult;
import com.opengamma.timeseries.date.localdate.LocalDateDoubleTimeSeries;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.time.LocalDateRange;
import com.opengamma.util.tuple.Pair;
import com.opengamma.util.tuple.Pairs;

/**
 * Abstract implementation of a provider of time-series.
 * <p>
 * This provides default implementations of the interface methods that delegate to a protected method that subclasses must implement.
 */
public abstract class AbstractHistoricalTimeSeriesProvider implements HistoricalTimeSeriesProvider {

  /**
   * The data source name.
   */
  private final String _dataSourceRegex;

  /**
   * Creates an instance suitable for any data source.
   */
  public AbstractHistoricalTimeSeriesProvider() {
    _dataSourceRegex = ".*";
  }

  /**
   * Creates an instance.
   *
   * @param dataSourceRegex
   *          the data source regex, not null
   */
  public AbstractHistoricalTimeSeriesProvider(final String dataSourceRegex) {
    ArgumentChecker.notNull(dataSourceRegex, "dataSourceRegex");
    _dataSourceRegex = dataSourceRegex;
  }

  // -------------------------------------------------------------------------
  @Override
  public LocalDateDoubleTimeSeries getHistoricalTimeSeries(
      final ExternalIdBundle externalIdBundle, final String dataSource, final String dataProvider, final String dataField) {

    final HistoricalTimeSeriesProviderGetRequest request = HistoricalTimeSeriesProviderGetRequest.createGet(externalIdBundle,
        dataSource, dataProvider, dataField);
    final HistoricalTimeSeriesProviderGetResult result = getHistoricalTimeSeries(request);
    return result.getResultMap().get(externalIdBundle);
  }

  @Override
  public LocalDateDoubleTimeSeries getHistoricalTimeSeries(
      final ExternalIdBundle externalIdBundle, final String dataSource, final String dataProvider, final String dataField, final LocalDateRange dateRange) {

    final HistoricalTimeSeriesProviderGetRequest request = HistoricalTimeSeriesProviderGetRequest.createGet(externalIdBundle,
        dataSource, dataProvider, dataField, dateRange);
    final HistoricalTimeSeriesProviderGetResult result = getHistoricalTimeSeries(request);
    return result.getResultMap().get(externalIdBundle);
  }

  @Override
  public Pair<LocalDate, Double> getLatestDataPoint(
      final ExternalIdBundle externalIdBundle, final String dataSource, final String dataProvider, final String dataField) {

    final HistoricalTimeSeriesProviderGetRequest request = HistoricalTimeSeriesProviderGetRequest.createGetLatest(externalIdBundle, dataSource, dataProvider,
        dataField);
    final HistoricalTimeSeriesProviderGetResult result = getHistoricalTimeSeries(request);
    final LocalDateDoubleTimeSeries series = result.getResultMap().get(externalIdBundle);
    if (series == null || series.isEmpty()) {
      return null;
    }
    return Pairs.of(series.getLatestTime(), series.getLatestValueFast());
  }

  @Override
  public Map<ExternalIdBundle, LocalDateDoubleTimeSeries> getHistoricalTimeSeries(
      final Set<ExternalIdBundle> externalIdBundleSet, final String dataSource, final String dataProvider, final String dataField,
      final LocalDateRange dateRange) {

    final HistoricalTimeSeriesProviderGetRequest request = HistoricalTimeSeriesProviderGetRequest.createGetBulk(externalIdBundleSet, dataSource, dataProvider,
        dataField, dateRange);
    final HistoricalTimeSeriesProviderGetResult result = getHistoricalTimeSeries(request);
    return result.getResultMap();
  }

  @Override
  public HistoricalTimeSeriesProviderGetResult getHistoricalTimeSeries(final HistoricalTimeSeriesProviderGetRequest request) {
    ArgumentChecker.notNull(request, "request");
    ArgumentChecker.isTrue(request.getDataSource().matches(_dataSourceRegex), "Unsupported data source: " + request.getDataSource());

    // short-cut empty case
    if (request.getExternalIdBundles().isEmpty()) {
      return new HistoricalTimeSeriesProviderGetResult();
    }

    // get time-series
    return doBulkGet(request);
  }

  // -------------------------------------------------------------------------
  /**
   * Gets the time-series.
   * <p>
   * The data source is checked before this method is invoked.
   *
   * @param request
   *          the request, with a non-empty set of identifiers, not null
   * @return the result, not null
   */
  protected abstract HistoricalTimeSeriesProviderGetResult doBulkGet(HistoricalTimeSeriesProviderGetRequest request);

  // -------------------------------------------------------------------------
  /**
   * Fixes the date range, sorting out unbounded values.
   * <p>
   * This method is used by subclasses to adjust the request.
   *
   * @param request
   *          the request to fix, not null
   * @param earliestStartDate
   *          the earliest start date to set to, not null
   * @return the adjusted date range, not null
   */
  protected LocalDateRange fixRequestDateRange(final HistoricalTimeSeriesProviderGetRequest request, final LocalDate earliestStartDate) {
    LocalDateRange dateRange = request.getDateRange();
    if (dateRange.getStartDateInclusive().isBefore(earliestStartDate)) {
      dateRange = dateRange.withStartDate(earliestStartDate);
    }
    request.setDateRange(dateRange);
    return dateRange;
  }

  /**
   * Filters the resulting bulk data map by the date range.
   * <p>
   * This is used to handle data providers that don't correctly filter.
   *
   * @param result
   *          the result to filter, not null
   * @param dateRange
   *          the date range to filter by, not null
   * @param maxPoints
   *          the maximum number of points required, negative back from the end date, null for all
   * @return the filtered map, not null
   */
  protected HistoricalTimeSeriesProviderGetResult filterResult(
      final HistoricalTimeSeriesProviderGetResult result, final LocalDateRange dateRange, final Integer maxPoints) {

    for (final Map.Entry<ExternalIdBundle, LocalDateDoubleTimeSeries> entry : result.getResultMap().entrySet()) {
      final LocalDateDoubleTimeSeries hts = entry.getValue();
      if (hts != null) {
        final LocalDateDoubleTimeSeries filtered = filterResult(hts, dateRange, maxPoints);
        entry.setValue(filtered);
      }
    }
    return result;
  }

  /**
   * Filters the time-series.
   *
   * @param hts
   *          the time-series to filter, not null
   * @param dateRange
   *          the date range to filter by, not null
   * @param maxPoints
   *          the maximum number of points required, negative back from the end date, null for all
   * @return the filtered time-series, not null
   */
  protected LocalDateDoubleTimeSeries filterResult(final LocalDateDoubleTimeSeries hts, final LocalDateRange dateRange, final Integer maxPoints) {
    // filter by dates
    LocalDateDoubleTimeSeries ts = hts.subSeries(dateRange.getStartDateInclusive(), dateRange.getEndDateExclusive());

    // filter by points
    if (maxPoints != null && ts.size() > Math.abs(maxPoints)) {
      if (maxPoints < 0) {
        ts = ts.tail(-maxPoints);
      } else {
        ts = ts.head(maxPoints);
      }
    }
    return ts;
  }

  // -------------------------------------------------------------------------
  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + _dataSourceRegex + "]";
  }

}
