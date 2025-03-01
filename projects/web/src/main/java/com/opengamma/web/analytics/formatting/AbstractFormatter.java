/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 *
 * Modified by McLeod Moores Software Limited.
 *
 * Copyright (C) 2015-Present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.web.analytics.formatting;

import java.util.Map;

import com.google.common.collect.Maps;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.ArgumentChecker;

/**
 * Abstract {@link TypeFormatter} that implements {@link #getDataTypeForValue} by delegating to {@link #getDataType()}.
 * @param <T> Type of object formatted by the formatter
 */
public abstract class AbstractFormatter<T> implements TypeFormatter<T> {

  private final Class<T> _type;
  private final Map<Format, Formatter<T>> _formatters = Maps.newHashMap();

  protected AbstractFormatter(final Class<T> type) {
    ArgumentChecker.notNull(type, "type");
    _type = type;
  }

  protected void addFormatter(final Formatter<T> formatter) {
    _formatters.put(formatter.getFormatter(), formatter);
  }

  @Override
  public Object format(final T value, final ValueSpecification valueSpec, final Format format, final Object inlineKey) {
    if (format == Format.CELL) {
      return formatCell(value, valueSpec, inlineKey);
    }
    final Formatter<T> formatter = _formatters.get(format);
    if (formatter != null) {
      return formatter.formatValue(value, valueSpec, inlineKey);
    }
    return new MissingValueFormatter(format + " format not supported for " + value.getClass().getSimpleName());
  }

  @Override
  public Class<T> getType() {
    return _type;
  }

  /**
   * Returns the same format type as {@link #getDataType()}.
   *
   * @param value The value
   * @return The format type returned by {@link #getDataType()}
   */
  @Override
  public DataType getDataTypeForValue(final T value) {
    return getDataType();
  }

  //-------------------------------------------------------------------------
  /**
   * A formatter element.
   * @param <T>  the formatter type
   */
  public abstract static class Formatter<T> {
    private final Format _format;

    protected Formatter(final Format format) {
      _format = format;
    }

    protected Format getFormatter() {
      return _format;
    }

    @Deprecated
    /* package */ Format getFormat() {
      return _format;
    }

    protected abstract Object formatValue(T value, ValueSpecification valueSpec, Object inlineKey);

    @Deprecated
    /* package */ Object format(final T value, final ValueSpecification valueSpec, final Object inlineKey) {
      return formatValue(value, valueSpec, inlineKey);
    }
  }

}
