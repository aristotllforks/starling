/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.futureoption;

import java.util.Collections;
import java.util.Set;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.equity.StaticReplicationDataBundle;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.riskfactor.ValueDeltaCalculator;
import com.opengamma.analytics.financial.riskfactor.ValueGreekCalculator;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;

/**
 * Calculates the value delta of a commodity future option using the Black delta.
 */
public class CommodityFutureOptionBlackValueDeltaFunction extends CommodityFutureOptionBlackFunction {
  /** Value delta calculator */
  private static final ValueGreekCalculator CALCULATOR = ValueDeltaCalculator.getInstance();

  /**
   * Default constructor.
   */
  public CommodityFutureOptionBlackValueDeltaFunction() {
    super(ValueRequirementNames.VALUE_DELTA);
  }

  @Override
  protected Set<ComputedValue> computeValues(final InstrumentDerivative derivative, final StaticReplicationDataBundle market, final FunctionInputs inputs,
      final Set<ValueRequirement> desiredValues, final ComputationTargetSpecification targetSpec, final ValueProperties resultProperties) {
    final ValueSpecification resultSpec = new ValueSpecification(getValueRequirementNames()[0], targetSpec, resultProperties);
    final Object deltaObject = inputs.getValue(ValueRequirementNames.DELTA);
    if (deltaObject == null) {
      throw new OpenGammaRuntimeException("Could not get delta");
    }
    final double delta = (Double) deltaObject;
    final double valueDelta = CALCULATOR.valueGreek(derivative, market, delta);
    return Collections.singleton(new ComputedValue(resultSpec, valueDelta));
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final Set<ValueRequirement> requirements = super.getRequirements(context, target, desiredValue);
    if (requirements == null) {
      return null;
    }
    requirements.add(new ValueRequirement(ValueRequirementNames.DELTA, target.toSpecification(), desiredValue.getConstraints()));
    return requirements;
  }

}
