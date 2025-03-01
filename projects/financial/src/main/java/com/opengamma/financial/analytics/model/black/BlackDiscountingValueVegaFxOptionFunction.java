/**
 * Copyright (C) 2016 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.financial.analytics.model.black;

import static com.opengamma.engine.value.ValuePropertyNames.CURRENCY;
import static com.opengamma.engine.value.ValueRequirementNames.VALUE_VEGA;

import java.util.Collections;
import java.util.Set;

import org.threeten.bp.Instant;

import com.google.common.collect.Iterables;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.provider.calculator.blackforex.ValueVegaForexBlackSmileCalculator;
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
import com.opengamma.util.money.CurrencyAmount;

/**
 * Calculates the value vega (first order derivative with respect to implied volatility)
 * of FX options using a Black surface and curves constructed using the discounting method.
 */
public class BlackDiscountingValueVegaFxOptionFunction extends BlackDiscountingFXOptionFunction {
  /** The value vega calculator. */
  static final InstrumentDerivativeVisitor<BlackForexSmileProviderInterface, CurrencyAmount> CALCULATOR =
      ValueVegaForexBlackSmileCalculator.getInstance();

  /**
   * Sets the value requirement to
   * {@link com.opengamma.engine.value.ValueRequirementNames#VALUE_VEGA}.
   */
  public BlackDiscountingValueVegaFxOptionFunction() {
    super(VALUE_VEGA);
  }

  @Override
  public CompiledFunctionDefinition compile(final FunctionCompilationContext context, final Instant atInstant) {
    return new BlackDiscountingCompiledFunction(getTargetToDefinitionConverter(context), getDefinitionToDerivativeConverter(context), true) {

      @Override
      protected Set<ComputedValue> getValues(final FunctionExecutionContext executionContext, final FunctionInputs inputs,
          final ComputationTarget target, final Set<ValueRequirement> desiredValues, final InstrumentDerivative derivative,
          final FXMatrix fxMatrix) {
        final BlackForexSmileProvider blackData = getBlackSurface(executionContext, inputs, target, fxMatrix);
        final CurrencyAmount valueVomma = derivative.accept(CALCULATOR, blackData);
        final ValueRequirement desiredValue = Iterables.getOnlyElement(desiredValues);
        final ValueProperties properties = desiredValue.getConstraints().copy().get();
        final String currency = Iterables.getOnlyElement(properties.getValues(CURRENCY));
        if (!currency.equals(valueVomma.getCurrency().getCode())) {
          throw new OpenGammaRuntimeException("Currency of result " + valueVomma.getCurrency() + " did not match"
              + " the expected currency " + currency);
        }
        final ValueSpecification spec = new ValueSpecification(VALUE_VEGA, target.toSpecification(), properties);
        return Collections.singleton(new ComputedValue(spec, valueVomma.getAmount()));
      }

    };
  }
}
