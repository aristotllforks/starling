/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.sabr;

import static com.opengamma.engine.value.ValuePropertyNames.CALCULATION_METHOD;
import static com.opengamma.engine.value.ValuePropertyNames.CUBE;
import static com.opengamma.engine.value.ValuePropertyNames.CURVE;
import static com.opengamma.engine.value.ValuePropertyNames.CURVE_EXPOSURES;
import static com.opengamma.engine.value.ValueRequirementNames.BLOCK_CURVE_SENSITIVITIES;
import static com.opengamma.engine.value.ValueRequirementNames.CURVE_SPECIFICATION;
import static com.opengamma.engine.value.ValueRequirementNames.YIELD_CURVE_NODE_SENSITIVITIES;
import static com.opengamma.financial.analytics.model.curve.CurveCalculationPropertyNamesAndValues.DISCOUNTING;
import static com.opengamma.financial.analytics.model.curve.CurveCalculationPropertyNamesAndValues.PROPERTY_CURVE_TYPE;
import static com.opengamma.financial.analytics.model.sabr.SABRPropertyValues.NO_EXTRAPOLATION;
import static com.opengamma.financial.analytics.model.volatility.SmileFittingPropertyNamesAndValues.PROPERTY_VOLATILITY_MODEL;
import static com.opengamma.financial.analytics.model.volatility.SmileFittingPropertyNamesAndValues.SABR;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Instant;

import com.google.common.collect.Iterables;
import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyParameterSensitivity;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.CompiledFunctionDefinition;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.analytics.DoubleLabelledMatrix1D;
import com.opengamma.financial.analytics.curve.CurveSpecification;
import com.opengamma.financial.analytics.model.multicurve.MultiCurveUtils;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.Pair;

/**
 * Calculates the yield curve node sensitivities of instruments using curves constructed using the discounting method.
 */
public class NoExtrapolationSABRDiscountingYCNSFunction extends SABRDiscountingFunction {
  /** The logger */
  private static final Logger LOGGER = LoggerFactory.getLogger(NoExtrapolationSABRDiscountingYCNSFunction.class);

  /**
   * Sets the value requirements to {@link com.opengamma.engine.value.ValueRequirementNames#YIELD_CURVE_NODE_SENSITIVITIES}.
   */
  public NoExtrapolationSABRDiscountingYCNSFunction() {
    super(YIELD_CURVE_NODE_SENSITIVITIES);
  }

  @Override
  public CompiledFunctionDefinition compile(final FunctionCompilationContext context, final Instant atInstant) {
    return new SABRDiscountingCompiledFunction(getTargetToDefinitionConverter(context), getDefinitionToDerivativeConverter(context), true) {

      @Override
      protected Set<ComputedValue> getValues(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target,
          final Set<ValueRequirement> desiredValues, final InstrumentDerivative derivative, final FXMatrix fxMatrix) {
        final MultipleCurrencyParameterSensitivity sensitivities = (MultipleCurrencyParameterSensitivity) inputs.getValue(BLOCK_CURVE_SENSITIVITIES);
        final ValueRequirement desiredValue = Iterables.getOnlyElement(desiredValues);
        final String curveName = desiredValue.getConstraint(CURVE);
        final Map<Pair<String, Currency>, DoubleMatrix1D> entries = sensitivities.getSensitivities();
        for (final Map.Entry<Pair<String, Currency>, DoubleMatrix1D> entry : entries.entrySet()) {
          if (curveName.equals(entry.getKey().getFirst())) {
            final ValueProperties properties = desiredValue.getConstraints().copy().with(CURVE, curveName).get();
            final CurveSpecification curveSpecification = (CurveSpecification) inputs
                .getValue(new ValueRequirement(CURVE_SPECIFICATION, ComputationTargetSpecification.NULL, ValueProperties.builder()
                    .with(CURVE, curveName).get()));
            final ValueSpecification spec = new ValueSpecification(YIELD_CURVE_NODE_SENSITIVITIES, target.toSpecification(), properties);
            final DoubleLabelledMatrix1D ycns = MultiCurveUtils.getLabelledMatrix(entry.getValue(), curveSpecification);
            return Collections.singleton(new ComputedValue(spec, ycns));
          }
        }
        LOGGER.info("Could not get sensitivities to " + curveName + " for " + target.getName());
        return Collections.emptySet();
      }

      @Override
      public Set<ValueRequirement> getRequirements(final FunctionCompilationContext compilationContext, final ComputationTarget target,
          final ValueRequirement desiredValue) {
        final ValueProperties constraints = desiredValue.getConstraints();
        final Set<String> curveNames = constraints.getValues(CURVE);
        if (curveNames == null || curveNames.size() != 1) {
          return null;
        }
        final Set<String> curveExposureConfigs = constraints.getValues(CURVE_EXPOSURES);
        if (curveExposureConfigs == null) {
          return null;
        }
        final Set<String> cubeNames = constraints.getValues(CUBE);
        if (cubeNames == null) {
          return null;
        }
        final ValueProperties properties = ValueProperties.with(PROPERTY_CURVE_TYPE, DISCOUNTING).with(PROPERTY_VOLATILITY_MODEL, SABR).with(CUBE, cubeNames)
            .with(CALCULATION_METHOD, getCalculationMethod()).with(CURVE_EXPOSURES, curveExposureConfigs).get();
        final ValueProperties curveProperties = ValueProperties.with(CURVE, curveNames).get();
        final Set<ValueRequirement> requirements = new HashSet<>();
        final FinancialSecurity security = (FinancialSecurity) target.getTrade().getSecurity();
        final SecuritySource securitySource = OpenGammaCompilationContext.getSecuritySource(context);
        requirements.add(new ValueRequirement(CURVE_SPECIFICATION, ComputationTargetSpecification.NULL, curveProperties));
        requirements.add(new ValueRequirement(BLOCK_CURVE_SENSITIVITIES, target.toSpecification(), properties));
        requirements.addAll(getFXRequirements(security, securitySource));
        final Set<ValueRequirement> tsRequirements = getTimeSeriesRequirements(context, target);
        if (tsRequirements == null) {
          return null;
        }
        requirements.addAll(tsRequirements);
        return requirements;
      }

      @Override
      protected Collection<ValueProperties.Builder> getResultProperties(final FunctionCompilationContext compilationContext, final ComputationTarget target) {
        final Collection<ValueProperties.Builder> properties = super.getResultProperties(compilationContext, target);
        for (final ValueProperties.Builder builder : properties) {
          builder.withAny(CURVE);
        }
        return properties;
      }

      @Override
      protected String getCalculationMethod() {
        return NO_EXTRAPOLATION;
      }
    };
  }
}
