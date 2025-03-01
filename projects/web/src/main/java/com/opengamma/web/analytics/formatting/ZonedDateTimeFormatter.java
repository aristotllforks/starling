/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics.formatting;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.opengamma.engine.value.ValueSpecification;

/**
 *
 */
/* package */ class ZonedDateTimeFormatter extends AbstractFormatter<ZonedDateTime> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  /* package */ ZonedDateTimeFormatter() {
    super(ZonedDateTime.class);
  }

  @Override
  public Object formatCell(final ZonedDateTime value, final ValueSpecification valueSpec, final Object inlineKey) {
    return FORMATTER.format(value);
  }

  @Override
  public DataType getDataType() {
    return DataType.STRING;
  }
}
