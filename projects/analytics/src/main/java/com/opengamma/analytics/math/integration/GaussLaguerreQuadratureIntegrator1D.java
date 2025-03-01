/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.integration;

import com.opengamma.analytics.math.function.Function1D;

/**
 * Gauss-Laguerre quadrature approximates the value of integrals of the form
 * $$
 * \begin{align*}
 * \int_{0}^{\infty} e^{-x}f(x) dx
 * \end{align*}
 * $$
 * The weights and abscissas are generated by {@link GaussLaguerreWeightAndAbscissaFunction}.
 * <p>
 * The function to integrate is scaled in such a way as to allow any values for
 * the limits of integration. At present, this integrator can only be used for
 * the limits $[0, \infty]$.
 */
public class GaussLaguerreQuadratureIntegrator1D extends GaussianQuadratureIntegrator1D {
  private static final Double[] LIMITS = new Double[] {0., Double.POSITIVE_INFINITY};

  public GaussLaguerreQuadratureIntegrator1D(final int n) {
    super(n, new GaussLaguerreWeightAndAbscissaFunction());
  }

  public GaussLaguerreQuadratureIntegrator1D(final int n, final double alpha) {
    super(n, new GaussLaguerreWeightAndAbscissaFunction(alpha));
  }

  @Override
  public Double[] getLimits() {
    return LIMITS;
  }

  /**
   * {@inheritDoc}
   * @throws UnsupportedOperationException If the lower limit is not $-\infty$ or the upper limit is not $\infty$
   */
  // * The function $f(x)$ that is to be integrated is transformed into a form
  // * suitable for this quadrature method using:
  // * $$
  // * \begin{align*}
  // * \int_{0}^{\infty} f(x) dx
  // * &= \int_{0}^{\infty} f(x) e^x e^{-x} dx\\
  // * &= \int_{0}^{\infty} g(x) e^{-x} dx
  // * \end{align*}
  // * $$
  @Override
  public Function1D<Double, Double> getIntegralFunction(final Function1D<Double, Double> function, final Double lower, final Double upper) {
    if (lower.equals(LIMITS[0]) && upper.equals(LIMITS[1])) {
      return new Function1D<Double, Double>() {

        @Override
        public Double evaluate(final Double x) {
          return function.evaluate(x) * Math.exp(x);
        }

      };
    }
    throw new UnsupportedOperationException("Limits for Gauss-Laguerre integration are 0 and +infinity");
  }
}
