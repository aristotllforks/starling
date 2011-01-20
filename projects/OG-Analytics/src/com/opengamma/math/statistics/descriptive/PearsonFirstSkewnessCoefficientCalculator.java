/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.math.statistics.descriptive;

import org.apache.commons.lang.Validate;

import com.opengamma.math.function.Function1D;

/**
 * 
 */
public class PearsonFirstSkewnessCoefficientCalculator extends Function1D<double[], Double> {
  private final Function1D<double[], Double> _mean = new MeanCalculator();
  private final Function1D<double[], Double> _mode = new ModeCalculator();
  private final Function1D<double[], Double> _stdDev = new SampleStandardDeviationCalculator();

  @Override
  public Double evaluate(final double[] x) {
    Validate.notNull(x);
    if (x.length < 2) {
      throw new IllegalArgumentException("Need at least 2 data points to calculate moment");
    }
    return 3 * (_mean.evaluate(x) - _mode.evaluate(x)) / _stdDev.evaluate(x);
  }

}
