/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.curve.interestrate.generator;

import com.opengamma.analytics.financial.model.interestrate.curve.DiscountCurve;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.math.curve.DoublesCurveInterpolatedAnchor;
import com.opengamma.analytics.math.interpolation.Interpolator1D;
import com.opengamma.util.ArgumentChecker;

/**
 * Store the details and generate the required curve. The curve is interpolated on the discount factor. One extra node with value zero is
 * added at the mid point between the first and second point. This extra anchor is required when two translation invariant curves
 * descriptions are added in a spread curve (two translations would create a singular system).
 */
public class GeneratorCurveDiscountFactorInterpolatedAnchorNode extends GeneratorYDCurve {

  /**
   * The nodes (times) on which the interpolated curves is constructed.
   */
  private final double[] _nodePoints;
  /**
   * The anchor point.
   */
  private final double _anchor;
  /**
   * The interpolator used for the curve.
   */
  private final Interpolator1D _interpolator;
  /**
   * The number of points (or nodes). Is the length of _nodePoints.
   */
  private final int _nbPoints;

  /**
   * Constructor.
   * 
   * @param nodePoints
   *          The node points (X) used to define the interpolated curve.
   * @param anchor
   *          The anchor with zero value.
   * @param interpolator
   *          The interpolator.
   */
  public GeneratorCurveDiscountFactorInterpolatedAnchorNode(final double[] nodePoints, final double anchor,
      final Interpolator1D interpolator) {
    ArgumentChecker.notNull(nodePoints, "Node points");
    ArgumentChecker.notNull(interpolator, "Interpolator");
    _nbPoints = nodePoints.length;
    _nodePoints = nodePoints;
    _anchor = anchor;
    _interpolator = interpolator;
  }

  @Override
  public int getNumberOfParameter() {
    return _nbPoints;
  }

  @Override
  public YieldAndDiscountCurve generateCurve(final String name, final double[] x) {
    ArgumentChecker.isTrue(x.length == _nbPoints, "Incorrect dimension for the rates");
    return new DiscountCurve(name, DoublesCurveInterpolatedAnchor.from(_nodePoints, x, _anchor, 1.0, _interpolator, name));
  }

  @Override
  public YieldAndDiscountCurve generateCurve(final String name, final MulticurveProviderInterface multicurve, final double[] parameters) {
    return generateCurve(name, parameters);
  }

  @Override
  public double[] initialGuess(final double[] rates) {
    ArgumentChecker.isTrue(rates.length == _nbPoints, "Rates of incorrect length.");
    final double[] spread = new double[_nbPoints];
    for (int loopg = 0; loopg < _nbPoints; loopg++) {
      spread[loopg] = 1.0;
    }
    // Implementation note: The "anchor" generator is used for spread. The initial guess is a rate spread of 0, i.e. a discount factor of 1.
    return spread;
  }

}
