/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.sensitivity.parameter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.provider.description.interestrate.ParameterProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.ForwardSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MulticurveSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.SimpleParameterSensitivity;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.util.tuple.DoublesPair;

/**
 * For an instrument, computes the sensitivity of a value (often the present value or a par spread) to the parameters used in the curve. The meaning of
 * "parameters" will depend of the way the curve is stored (interpolated yield, function parameters, etc.). The return format is ParameterSensitivity object.
 *
 * @param <DATA_TYPE>
 *          Data type.
 */
public class SimpleParameterSensitivityParameterCalculator<DATA_TYPE extends ParameterProviderInterface>
    extends SimpleParameterSensitivityParameterAbstractCalculator<DATA_TYPE> {

  /**
   * Constructor.
   *
   * @param curveSensitivityCalculator
   *          The curve sensitivity calculator.
   */
  public SimpleParameterSensitivityParameterCalculator(final InstrumentDerivativeVisitor<DATA_TYPE, MulticurveSensitivity> curveSensitivityCalculator) {
    super(curveSensitivityCalculator);
  }

  @Override
  public SimpleParameterSensitivity pointToParameterSensitivity(final MulticurveSensitivity sensitivity, final DATA_TYPE parameterMulticurves,
      final Set<String> curvesSet) {
    SimpleParameterSensitivity result = new SimpleParameterSensitivity();
    // YieldAndDiscount
    final Map<String, List<DoublesPair>> sensitivityDsc = sensitivity.getYieldDiscountingSensitivities();
    for (final Map.Entry<String, List<DoublesPair>> entry : sensitivityDsc.entrySet()) {
      if (curvesSet.contains(entry.getKey())) {
        result = result.plus(entry.getKey(),
            new DoubleMatrix1D(parameterMulticurves.getMulticurveProvider().parameterSensitivity(entry.getKey(), entry.getValue())));
      }
    }
    // Forward
    final Map<String, List<ForwardSensitivity>> sensitivityFwd = sensitivity.getForwardSensitivities();
    for (final Map.Entry<String, List<ForwardSensitivity>> entry : sensitivityFwd.entrySet()) {
      if (curvesSet.contains(entry.getKey())) {
        result = result.plus(entry.getKey(),
            new DoubleMatrix1D(parameterMulticurves.getMulticurveProvider().parameterForwardSensitivity(entry.getKey(), entry.getValue())));
      }
    }
    return result;
  }

}
