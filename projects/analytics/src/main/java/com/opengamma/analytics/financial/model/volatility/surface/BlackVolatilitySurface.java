/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.model.volatility.surface;

import com.opengamma.analytics.math.surface.Surface;
import com.opengamma.util.tuple.DoublesPair;

/**
 * A surface with gives Black (implied) volatility as a function of time to maturity and value some abstraction of strike.
 *
 * @param <T>
 *          Parameter that describes the abstraction of strike - this could be the actual strike, the delta (most commonly used in FX), moneyness (defined as
 *          the strike/forward), the logarithm of moneyness or some other parameterisation
 */
public abstract class BlackVolatilitySurface<T extends StrikeType> extends VolatilitySurface {

  /**
   * @param surface
   *          The time to maturity should be the first coordinate and the abstraction of strike the second
   */
  public BlackVolatilitySurface(final Surface<Double, Double, Double> surface) {
    super(surface);
  }

  public double getVolatility(final double t, final T s) {
    final DoublesPair temp = DoublesPair.of(t, s.value());
    return getVolatility(temp);
  }

  @Override
  public abstract double getVolatility(double t, double k);

  public abstract double getAbsoluteStrike(double t, T s);

  public abstract BlackVolatilitySurface<T> withShift(double shift, boolean useAdditive);

  public abstract BlackVolatilitySurface<T> withSurface(Surface<Double, Double, Double> surface);

  public abstract <S, U> U accept(BlackVolatilitySurfaceVisitor<S, U> visitor, S data);

  public abstract <U> U accept(BlackVolatilitySurfaceVisitor<?, U> visitor);

  @Override
  public VolatilitySurface withParallelShift(final double shift) {
    return withSurface(getParallelShiftedSurface(shift));
  }

  @Override
  public VolatilitySurface withSingleAdditiveShift(final double x, final double y, final double shift) {
    return withSurface(getSingleAdditiveShiftSurface(x, y, shift));
  }

  @Override
  public VolatilitySurface withMultipleAdditiveShifts(final double[] x, final double[] y, final double[] shifts) {
    return withSurface(getMultipleAdditiveShiftsSurface(x, y, shifts));
  }

  @Override
  public VolatilitySurface withConstantMultiplicativeShift(final double shift) {
    return withSurface(getConstantMultiplicativeShiftSurface(shift));
  }

  @Override
  public VolatilitySurface withSingleMultiplicativeShift(final double x, final double y, final double shift) {
    return withSurface(getSingleMultiplicativeShiftSurface(x, y, shift));
  }

  @Override
  public VolatilitySurface withMultipleMultiplicativeShifts(final double[] x, final double[] y, final double[] shifts) {
    return withSurface(getMultipleMultiplicativeShiftsSurface(x, y, shifts));
  }

}
