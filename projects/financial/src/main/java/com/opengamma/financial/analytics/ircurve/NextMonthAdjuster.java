/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.ircurve;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

/**
 * A {@code TemporalAdjuster} that moves the date to the next March/June/September/December.
 */
public class NextMonthAdjuster implements TemporalAdjuster {

  @Override
  public Temporal adjustInto(final Temporal temporal) {
    return temporal.plus(1, MONTHS);
  }

}
