/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity.option;

import java.util.Collections;
import java.util.Set;

import com.opengamma.analytics.financial.equity.EquityOptionBlackPresentValueCalculator;
import com.opengamma.analytics.financial.equity.StaticReplicationDataBundle;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;

/**
 * Calculates the present value of an equity index or equity option using the Black formula. In BLACK_BASIC_METHOD, volatility is implied from MARKET_VALUE.
 */
public class EquityOptionBlackBasicPresentValueFunction extends EquityOptionBlackBasicFunction {
  /** The Black present value calculator */
  private static final EquityOptionBlackPresentValueCalculator CALCULATOR = EquityOptionBlackPresentValueCalculator.getInstance();

  /**
   * Default constructor.
   */
  public EquityOptionBlackBasicPresentValueFunction() {
    super(ValueRequirementNames.PRESENT_VALUE);
  }

  @Override
  protected Set<ComputedValue> computeValues(final InstrumentDerivative derivative, final StaticReplicationDataBundle market, final FunctionInputs inputs,
      final Set<ValueRequirement> desiredValues, final ComputationTargetSpecification targetSpec, final ValueProperties resultProperties) {
    final ValueSpecification resultSpec = new ValueSpecification(getValueRequirementNames()[0], targetSpec, resultProperties);
    final double pv = derivative.accept(CALCULATOR, market);
    return Collections.singleton(new ComputedValue(resultSpec, pv));
  }
}
