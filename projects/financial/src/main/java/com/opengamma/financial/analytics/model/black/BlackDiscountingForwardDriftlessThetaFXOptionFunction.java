/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.black;

import static com.opengamma.engine.value.ValueRequirementNames.FORWARD_DRIFTLESS_THETA;

import java.util.Collections;
import java.util.Set;

import org.threeten.bp.Instant;

import com.google.common.collect.Iterables;
import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.provider.calculator.blackforex.ForwardDriftlessThetaForexBlackSmileCalculator;
import com.opengamma.analytics.financial.provider.description.forex.BlackForexSmileProvider;
import com.opengamma.analytics.financial.provider.description.forex.BlackForexSmileProviderInterface;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.CompiledFunctionDefinition;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;

/**
 * Calculates the forward driftless theta of FX options using a Black surface and
 * curves constructed using the discounting method.
 */
public class BlackDiscountingForwardDriftlessThetaFXOptionFunction extends BlackDiscountingFXOptionFunction {
  /** The forward driftless theta calculator. */
  static final InstrumentDerivativeVisitor<BlackForexSmileProviderInterface, Double> CALCULATOR =
      ForwardDriftlessThetaForexBlackSmileCalculator.getInstance();

  /**
   * Sets the value requirement to
   * {@link com.opengamma.engine.value.ValueRequirementNames#FORWARD_DRIFTLESS_THETA}.
   */
  public BlackDiscountingForwardDriftlessThetaFXOptionFunction() {
    super(FORWARD_DRIFTLESS_THETA);
  }

  @Override
  public CompiledFunctionDefinition compile(final FunctionCompilationContext context, final Instant atInstant) {
    return new BlackDiscountingCompiledFunction(getTargetToDefinitionConverter(context), getDefinitionToDerivativeConverter(context), false) {

      @Override
      protected Set<ComputedValue> getValues(final FunctionExecutionContext executionContext, final FunctionInputs inputs,
          final ComputationTarget target, final Set<ValueRequirement> desiredValues, final InstrumentDerivative derivative,
          final FXMatrix fxMatrix) {
        final BlackForexSmileProvider blackData = getBlackSurface(executionContext, inputs, target, fxMatrix);
        final double forwardTheta = derivative.accept(CALCULATOR, blackData);
        final ValueRequirement desiredValue = Iterables.getOnlyElement(desiredValues);
        final ValueProperties properties = desiredValue.getConstraints().copy().get();
        final ValueSpecification spec = new ValueSpecification(FORWARD_DRIFTLESS_THETA, target.toSpecification(), properties);
        return Collections.singleton(new ComputedValue(spec, forwardTheta));
      }

    };
  }
}
