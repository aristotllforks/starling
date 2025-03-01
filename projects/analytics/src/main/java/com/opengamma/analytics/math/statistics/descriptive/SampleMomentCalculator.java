/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 *
 * Modified by McLeod Moores Software Limited.
 *
 * Copyright (C) 2017 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.analytics.math.statistics.descriptive;

import com.mcleodmoores.analytics.math.statistics.descriptive.DescriptiveStatistic;
import com.mcleodmoores.analytics.math.statistics.descriptive.DescriptiveStatisticsCalculator;
import com.opengamma.util.ArgumentChecker;

/**
 * Calculates the $n^th$ sample raw moment of a series of data.
 * <p>
 * The sample raw moment $m_n$ of a series of data $x_1, x_2, \dots, x_s$ is given by:
 * $$
 * \begin{align*}
 * m_n = \frac{1}{s}\sum_{i=1}^s x_i^n
 * \end{align*}
 * $$
 */
@DescriptiveStatistic(name = SampleMomentCalculator.NAME, aliases = "Sample Moment")
public class SampleMomentCalculator extends DescriptiveStatisticsCalculator {
  /**
   * The name of this calculator.
   */
  public static final String NAME = "SampleMoment";

  /** The degree */
  private final int _n;

  /**
   * @param n  the degree of the moment to calculate, cannot be negative
   */
  public SampleMomentCalculator(final int n) {
    ArgumentChecker.isTrue(n >= 0, "n must be >= 0");
    _n = n;
  }

  /**
   * @param x  the array of data, not null or empty
   * @return  the sample raw moment
   */
  @Override
  public Double evaluate(final double[] x) {
    ArgumentChecker.notEmpty(x, "x");
    if (_n == 0) {
      return 1.;
    }
    double sum = 0;
    for (final Double d : x) {
      sum += Math.pow(d, _n);
    }
    return sum / (x.length - 1);
  }

  @Override
  public String getName() {
    return NAME;
  }
}
