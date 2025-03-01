/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.carrlee;

import static com.opengamma.engine.value.ValueRequirementNames.VEGA;

import java.util.Collections;
import java.util.Set;

import org.threeten.bp.Instant;

import com.google.common.collect.Iterables;
import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.provider.description.volatilityswap.CarrLeeFXData;
import com.opengamma.analytics.financial.volatilityswap.CarrLeeFXVolatilitySwapVegaCalculator;
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
 *
 */
public class CarrLeeVegaFXVolatilitySwapFunction extends CarrLeeFXVolatilitySwapFunction {

  /** The vega calculator */
  private static final InstrumentDerivativeVisitor<CarrLeeFXData, Double> CALCULATOR = new CarrLeeFXVolatilitySwapVegaCalculator();

  /**
   * Sets the value requirement to
   * {@link com.opengamma.engine.value.ValueRequirementNames#VEGA}.
   */
  public CarrLeeVegaFXVolatilitySwapFunction() {
    super(VEGA);
  }

  @Override
  public CompiledFunctionDefinition compile(final FunctionCompilationContext context, final Instant atInstant) {
    return new CarrLeeFXVolatilitySwapCompiledFunction(getTargetToDefinitionConverter(context), getDefinitionToDerivativeConverter(context), false) {

      @SuppressWarnings("synthetic-access")
      @Override
      protected Set<ComputedValue> getValues(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target,
          final Set<ValueRequirement> desiredValues, final InstrumentDerivative derivative, final FXMatrix fxMatrix) {
        final CarrLeeFXData data = getCarrLeeData(executionContext, inputs, target, fxMatrix);
        final Double vega = derivative.accept(CALCULATOR, data);
        final ValueProperties properties = Iterables.getOnlyElement(desiredValues).getConstraints().copy().get();
        final ValueSpecification spec = new ValueSpecification(VEGA, target.toSpecification(), properties);
        return Collections.singleton(new ComputedValue(spec, vega));
      }

    };
  }
}
