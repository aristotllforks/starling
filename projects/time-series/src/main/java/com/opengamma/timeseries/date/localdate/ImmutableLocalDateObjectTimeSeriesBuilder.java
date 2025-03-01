/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.timeseries.date.localdate;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.threeten.bp.LocalDate;

import com.opengamma.timeseries.date.DateObjectTimeSeries;

/**
 * Builder implementation for {@code ImmutableLocalDateObjectTimeSeries}.
 *
 * @param <V>  the type of the values
 */
final class ImmutableLocalDateObjectTimeSeriesBuilder<V>
    implements LocalDateObjectTimeSeriesBuilder<V> {

  /**
   * The time-series.
   */
  private final SortedMap<Integer, V> _series = new ConcurrentSkipListMap<>();  // use this map to block nulls

  /**
   * Creates an instance.
   */
  protected ImmutableLocalDateObjectTimeSeriesBuilder() {
  }

  //-------------------------------------------------------------------------
  private static int convertToInt(final LocalDate date) {
    return LocalDateToIntConverter.convertToInt(date);
  }

  private static LocalDate convertFromInt(final int date) {
    return LocalDateToIntConverter.convertToLocalDate(date);
  }

  //-------------------------------------------------------------------------
  @Override
  public int size() {
    return _series.size();
  }

  @SuppressWarnings("synthetic-access")
  @Override
  public LocalDateObjectEntryIterator<V> iterator() {
    return new LocalDateObjectEntryIterator<V>() {
      private final Iterator<Entry<Integer, V>> _iterator = _series.entrySet().iterator();
      private int _index = -1;
      private Entry<Integer, V> _current;

      @Override
      public boolean hasNext() {
        return _iterator.hasNext();
      }

      @Override
      public Entry<LocalDate, V> next() {
        return new SimpleImmutableEntry<>(nextTime(), currentValue());
      }

      @Override
      public int nextTimeFast() {
        if (!hasNext()) {
          throw new NoSuchElementException("No more elements in the iteration");
        }
        _index++;
        _current = _iterator.next();
        return _current.getKey();
      }

      @Override
      public LocalDate nextTime() {
        return convertFromInt(nextTimeFast());
      }

      @Override
      public int currentTimeFast() {
        if (_index < 0) {
          throw new IllegalStateException("Iterator has not yet been started");
        }
        if (_current == null) {
          throw new IllegalStateException("Element has been removed");
        }
        return _current.getKey();
      }

      @Override
      public LocalDate currentTime() {
        return convertFromInt(currentTimeFast());
      }

      @Override
      public V currentValue() {
        if (_index < 0) {
          throw new IllegalStateException("Iterator has not yet been started");
        }
        if (_current == null) {
          throw new IllegalStateException("Element has been removed");
        }
        return _current.getValue();
      }

      @Override
      public int currentIndex() {
        return _index;
      }

      @Override
      public void remove() {
        if (_index < 0) {
          throw new IllegalStateException("Iterator has not yet been started");
        }
        if (_current == null) {
          throw new IllegalStateException("Element has been removed");
        }
        _iterator.remove();
        _current = null;
        _index--;
      }
    };
  }

  //-------------------------------------------------------------------------
  @Override
  public LocalDateObjectTimeSeriesBuilder<V> put(final LocalDate time, final V value) {
    return put(convertToInt(time), value);
  }

  @Override
  public LocalDateObjectTimeSeriesBuilder<V> put(final int time, final V value) {
    _series.put(time, value);
    return this;
  }

  @Override
  public LocalDateObjectTimeSeriesBuilder<V> putAll(final LocalDate[] times, final V[] values) {
    if (times.length != values.length) {
      throw new IllegalArgumentException("Arrays are of different sizes: " + times.length + ", " + values.length);
    }
    for (int i = 0; i < times.length; i++) {
      put(times[i], values[i]);
    }
    return this;
  }

  @Override
  public LocalDateObjectTimeSeriesBuilder<V> putAll(final int[] times, final V[] values) {
    if (times.length != values.length) {
      throw new IllegalArgumentException("Arrays are of different sizes: " + times.length + ", " + values.length);
    }
    for (int i = 0; i < times.length; i++) {
      put(times[i], values[i]);
    }
    return this;
  }

  //-------------------------------------------------------------------------
  @Override
  public LocalDateObjectTimeSeriesBuilder<V> putAll(final DateObjectTimeSeries<?, V> timeSeries) {
    return putAll(timeSeries, 0, timeSeries.size());
  }

  @Override
  public LocalDateObjectTimeSeriesBuilder<V> putAll(final DateObjectTimeSeries<?, V> timeSeries, final int startPos, final int endPos) {
    if (startPos < 0 || startPos > timeSeries.size()) {
      throw new IndexOutOfBoundsException("Invalid start index: " + startPos);
    }
    if (endPos < 0 || endPos > timeSeries.size()) {
      throw new IndexOutOfBoundsException("Invalid end index: " + endPos);
    }
    if (startPos > endPos) {
      throw new IndexOutOfBoundsException("End index not be less than start index");
    }
    if (startPos == endPos) {
      return this;
    }
    for (int i = startPos; i < endPos; i++) {
      put(timeSeries.getTimeAtIndexFast(i), timeSeries.getValueAtIndex(i));
    }
    return this;
  }

  @Override
  public LocalDateObjectTimeSeriesBuilder<V> putAll(final Map<LocalDate, V> timeSeriesMap) {
    if (timeSeriesMap.size() == 0) {
      return this;
    }
    for (final Entry<LocalDate, V> entry : timeSeriesMap.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
    return this;
  }

  //-------------------------------------------------------------------------
  @Override
  public LocalDateObjectTimeSeriesBuilder<V> clear() {
    _series.clear();
    return this;
  }

  //-------------------------------------------------------------------------
  @Override
  public ImmutableLocalDateObjectTimeSeries<V> build() {
    final int[] times = new int[_series.size()];
    @SuppressWarnings("unchecked")
    final
    V[] values = (V[]) new Object[_series.size()];
    int i = 0;
    for (final Entry<Integer, V> entry : _series.entrySet()) {
      times[i] = entry.getKey();
      values[i++] = entry.getValue();
    }
    return new ImmutableLocalDateObjectTimeSeries<>(times, values);
  }

  //-------------------------------------------------------------------------
  @Override
  public String toString() {
    return "Builder[size=" + _series.size() + "]";
  }

}
