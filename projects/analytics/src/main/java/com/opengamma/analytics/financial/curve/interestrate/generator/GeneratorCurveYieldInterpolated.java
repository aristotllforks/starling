/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.curve.interestrate.generator;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.math.interpolation.Interpolator1D;
import com.opengamma.util.ArgumentChecker;

/**
 * Store the details and generate the required curve. The curve is interpolated on the rate (continuously compounded). Only the
 * lastTimeCalculator is stored. The node are computed from the instruments.
 */
public class GeneratorCurveYieldInterpolated extends GeneratorYDCurve {

  /**
   * Calculator of the node associated to instruments.
   */
  private final InstrumentDerivativeVisitor<Object, Double> _nodeTimeCalculator;
  /**
   * The interpolator used for the curve.
   */
  private final Interpolator1D _interpolator;

  /**
   * Constructor.
   * 
   * @param nodeTimeCalculator
   *          Calculator of the node associated to instruments.
   * @param interpolator
   *          The interpolator used for the curve.
   */
  public GeneratorCurveYieldInterpolated(final InstrumentDerivativeVisitor<Object, Double> nodeTimeCalculator,
      final Interpolator1D interpolator) {
    _nodeTimeCalculator = nodeTimeCalculator;
    _interpolator = interpolator;
  }

  @Override
  public int getNumberOfParameter() {
    throw new UnsupportedOperationException("Cannot return the number of parameter for a GeneratorCurveYieldInterpolated");
  }

  @Override
  public YieldAndDiscountCurve generateCurve(final String name, final double[] parameters) {
    throw new UnsupportedOperationException("Cannot generate curves for a GeneratorCurveYieldInterpolated");
  }

  @Override
  public YieldAndDiscountCurve generateCurve(final String name, final MulticurveProviderInterface bundle, final double[] parameters) {
    throw new UnsupportedOperationException("Cannot generate curves for a GeneratorCurveYieldInterpolated");
  }

  @Override
  public GeneratorYDCurve finalGenerator(final Object data) {
    ArgumentChecker.isTrue(data instanceof InstrumentDerivative[], "data should be an array of InstrumentDerivative");
    final InstrumentDerivative[] instruments = (InstrumentDerivative[]) data;
    final double[] node = new double[instruments.length];
    for (int loopins = 0; loopins < instruments.length; loopins++) {
      node[loopins] = instruments[loopins].accept(_nodeTimeCalculator);
    }
    return new GeneratorCurveYieldInterpolatedNode(node, _interpolator);
  }
}
