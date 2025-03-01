/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.interpolation;

/**
 *
 */
public class NonnegativityPreservingQuinticSplineInterpolator1D extends PiecewisePolynomialInterpolator1D {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * If the primary interpolation method is not specified, the cubic spline interpolation with natrual endpoint conditions is used.
   */
  public NonnegativityPreservingQuinticSplineInterpolator1D() {
    super(new NonnegativityPreservingQuinticSplineInterpolator(new NaturalSplineInterpolator()));
  }

  /**
   * @param method
   *          Primary interpolation method whose first derivative values are modified according to the non-negativity conditions
   */
  public NonnegativityPreservingQuinticSplineInterpolator1D(final PiecewisePolynomialInterpolator method) {
    super(new NonnegativityPreservingQuinticSplineInterpolator(method));
  }
}
