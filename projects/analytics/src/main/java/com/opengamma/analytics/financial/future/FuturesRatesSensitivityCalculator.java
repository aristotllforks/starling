/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.future;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorSameMethodAdapter;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.analytics.financial.simpleinstruments.pricing.SimpleFutureDataBundle;
import com.opengamma.analytics.math.curve.InterpolatedDoublesCurve;
import com.opengamma.analytics.math.interpolation.Interpolator1D;
import com.opengamma.analytics.math.interpolation.data.Interpolator1DDataBundle;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.DoublesPair;

/**
 * This calculates the sensitivity of the present value (PV) to the continuously-compounded interest rates at the knot points of the funding
 * curve.
 * <p>
 * The return format is a DoubleMatrix1D (i.e. a vector) with length equal to the total number of knots in the curve
 * <p>
 * The change of a curve due to the movement of a single knot is interpolator-dependent, so an instrument can have sensitivity to knots at
 * times beyond its maturity
 */
@SuppressWarnings("deprecation")
public final class FuturesRatesSensitivityCalculator
    extends InstrumentDerivativeVisitorSameMethodAdapter<SimpleFutureDataBundle, DoubleMatrix1D> {
  private static final SettlementTimeCalculator SETTLEMENT_TIME = SettlementTimeCalculator.getInstance();
  private final InstrumentDerivativeVisitor<SimpleFutureDataBundle, Double> _presentValueCalculator;

  public static InstrumentDerivativeVisitor<SimpleFutureDataBundle, DoubleMatrix1D> getInstance(
      final InstrumentDerivativeVisitor<SimpleFutureDataBundle, Double> presentValueCalculator) {
    return new FuturesRatesSensitivityCalculator(presentValueCalculator);
  }

  private FuturesRatesSensitivityCalculator(final InstrumentDerivativeVisitor<SimpleFutureDataBundle, Double> presentValueCalculator) {
    ArgumentChecker.notNull(presentValueCalculator, "present value calculator");
    _presentValueCalculator = presentValueCalculator;
  }

  /**
   * @param future
   *          the derivative, not null
   * @param dataBundle
   *          the data bundle, not null
   * @return A DoubleMatrix1D containing bucketed delta in order and length of the yield curve. Currency amount per unit amount change in
   *         rate
   */
  @Override
  public DoubleMatrix1D visit(final InstrumentDerivative future, final SimpleFutureDataBundle dataBundle) {
    ArgumentChecker.notNull(future, "future");
    ArgumentChecker.notNull(dataBundle, "data bundle");
    ArgumentChecker.isTrue(dataBundle.getFundingCurve() instanceof YieldCurve, "Calculator expects a YieldCurve, have {}",
        dataBundle.getFundingCurve().getClass());
    final YieldCurve discCrv = (YieldCurve) dataBundle.getFundingCurve();
    final double settlement = future.accept(SETTLEMENT_TIME);
    final double rhoSettle = -1 * settlement * future.accept(_presentValueCalculator, dataBundle);

    final List<DoublesPair> sensitivityList = Arrays.asList(DoublesPair.of(settlement, rhoSettle));
    final InterpolatedDoublesCurve interpolatedCurve = (InterpolatedDoublesCurve) discCrv.getCurve();
    final Interpolator1D interpolator = interpolatedCurve.getInterpolator();
    final Interpolator1DDataBundle data = interpolatedCurve.getDataBundle();
    final List<Double> result = new ArrayList<>();
    if (sensitivityList != null && sensitivityList.size() > 0) {
      final double[][] sensitivityYY = new double[sensitivityList.size()][];
      // Implementation note: Sensitivity of the interpolated yield to the node yields
      int k = 0;
      for (final DoublesPair timeAndS : sensitivityList) {
        sensitivityYY[k++] = interpolator.getNodeSensitivitiesForValue(data, timeAndS.getFirst());
      }
      for (int j = 0; j < sensitivityYY[0].length; j++) {
        double temp = 0.0;
        k = 0;
        for (final DoublesPair timeAndS : sensitivityList) {
          temp += timeAndS.getSecond() * sensitivityYY[k++][j];
        }
        result.add(temp);
      }
    } else {
      for (int i = 0; i < interpolatedCurve.size(); i++) {
        result.add(0.);
      }
    }
    return new DoubleMatrix1D(result.toArray(new Double[result.size()]));

  }

  @Override
  public DoubleMatrix1D visit(final InstrumentDerivative derivative) {
    throw new UnsupportedOperationException("Need a SimpleFutureDataBundle to calculated bucketed delta");
  }

}
