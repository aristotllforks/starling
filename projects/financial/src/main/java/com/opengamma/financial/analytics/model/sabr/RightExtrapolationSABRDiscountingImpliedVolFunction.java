/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.sabr;

import static com.opengamma.engine.value.ValueRequirementNames.SECURITY_IMPLIED_VOLATILITY;
import static com.opengamma.financial.analytics.model.sabr.SABRPropertyValues.RIGHT_EXTRAPOLATION;

import java.util.Collections;
import java.util.Set;

import org.threeten.bp.Instant;

import com.google.common.collect.Iterables;
import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.provider.calculator.sabrswaption.ImpliedVolatilitySABRSwaptionCalculator;
import com.opengamma.analytics.financial.provider.description.interestrate.SABRSwaptionProvider;
import com.opengamma.analytics.financial.provider.description.interestrate.SABRSwaptionProviderInterface;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.CompiledFunctionDefinition;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;

/**
 * Calculates the (Black Lognormal) {@link com.opengamma.engine.value.ValueRequirementNames#SECURITY_IMPLIED_VOLATILITY} of target Swaption Trade from
 * calibrated SABR model.
 * <p>
 * Uses curves constructed using the discounting method.
 */
public class RightExtrapolationSABRDiscountingImpliedVolFunction extends SABRDiscountingFunction {

  /** The Implied Vol calculator */
  private static final InstrumentDerivativeVisitor<SABRSwaptionProviderInterface, Double> CALCULATOR = ImpliedVolatilitySABRSwaptionCalculator.getInstance();

  /**
   * Sets the value requirements to {@link com.opengamma.engine.value.ValueRequirementNames#SECURITY_IMPLIED_VOLATILITY}.
   */
  public RightExtrapolationSABRDiscountingImpliedVolFunction() {
    super(SECURITY_IMPLIED_VOLATILITY);
  }

  @Override
  public CompiledFunctionDefinition compile(final FunctionCompilationContext context, final Instant atInstant) {
    return new SABRDiscountingCompiledFunction(getTargetToDefinitionConverter(context), getDefinitionToDerivativeConverter(context), true) {

      @Override
      protected Set<ComputedValue> getValues(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target,
          final Set<ValueRequirement> desiredValues, final InstrumentDerivative derivative, final FXMatrix fxMatrix) {
        final SABRSwaptionProvider sabrData = getSABRSurfaces(executionContext, inputs, target, fxMatrix, null);
        final double impliedVol = derivative.accept(CALCULATOR, sabrData);
        final ValueSpecification spec = new ValueSpecification(SECURITY_IMPLIED_VOLATILITY, target.toSpecification(),
            Iterables.getOnlyElement(desiredValues).getConstraints());
        return Collections.singleton(new ComputedValue(spec, impliedVol));
      }

      @Override
      protected String getCalculationMethod() {
        return RIGHT_EXTRAPOLATION;
      }
    };
  }

}
