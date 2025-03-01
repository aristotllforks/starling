/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.hullwhitediscounting;

import static com.opengamma.engine.value.ValuePropertyNames.CURRENCY;
import static com.opengamma.engine.value.ValuePropertyNames.CURVE_EXPOSURES;
import static com.opengamma.engine.value.ValueRequirementNames.CURVE_BUNDLE;
import static com.opengamma.engine.value.ValueRequirementNames.JACOBIAN_BUNDLE;
import static com.opengamma.financial.analytics.model.curve.CurveCalculationPropertyNamesAndValues.HULL_WHITE_DISCOUNTING;
import static com.opengamma.financial.analytics.model.curve.CurveCalculationPropertyNamesAndValues.PROPERTY_CURVE_TYPE;
import static com.opengamma.financial.analytics.model.curve.CurveCalculationPropertyNamesAndValues.PROPERTY_HULL_WHITE_CURRENCY;
import static com.opengamma.financial.analytics.model.curve.CurveCalculationPropertyNamesAndValues.PROPERTY_HULL_WHITE_PARAMETERS;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.instrument.InstrumentDefinition;
import com.opengamma.analytics.financial.provider.curve.CurveBuildingBlockBundle;
import com.opengamma.analytics.financial.provider.description.interestrate.HullWhiteOneFactorProviderDiscount;
import com.opengamma.analytics.financial.provider.description.interestrate.ProviderUtils;
import com.opengamma.core.convention.ConventionSource;
import com.opengamma.core.holiday.HolidaySource;
import com.opengamma.core.region.RegionSource;
import com.opengamma.core.security.Security;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueProperties.Builder;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.analytics.conversion.CashFlowSecurityConverter;
import com.opengamma.financial.analytics.conversion.CashSecurityConverter;
import com.opengamma.financial.analytics.conversion.DefaultTradeConverter;
import com.opengamma.financial.analytics.conversion.DeliverableSwapFutureSecurityConverter;
import com.opengamma.financial.analytics.conversion.DeliverableSwapFutureTradeConverter;
import com.opengamma.financial.analytics.conversion.FRASecurityConverter;
import com.opengamma.financial.analytics.conversion.FXForwardSecurityConverter;
import com.opengamma.financial.analytics.conversion.FederalFundsFutureTradeConverter;
import com.opengamma.financial.analytics.conversion.FixedIncomeConverterDataProvider;
import com.opengamma.financial.analytics.conversion.FutureTradeConverter;
import com.opengamma.financial.analytics.conversion.InterestRateFutureTradeConverter;
import com.opengamma.financial.analytics.conversion.InterestRateSwapSecurityConverter;
import com.opengamma.financial.analytics.conversion.NonDeliverableFXForwardSecurityConverter;
import com.opengamma.financial.analytics.conversion.SwapSecurityConverter;
import com.opengamma.financial.analytics.conversion.SwaptionSecurityConverter;
import com.opengamma.financial.analytics.fixedincome.InterestRateInstrumentType;
import com.opengamma.financial.analytics.model.forex.ForexVisitors;
import com.opengamma.financial.analytics.model.multicurve.MultiCurvePricingFunction;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityUtils;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.financial.security.FinancialSecurityVisitorAdapter;
import com.opengamma.financial.security.cash.CashSecurity;
import com.opengamma.financial.security.cashflow.CashFlowSecurity;
import com.opengamma.financial.security.fra.FRASecurity;
import com.opengamma.financial.security.future.DeliverableSwapFutureSecurity;
import com.opengamma.financial.security.future.FederalFundsFutureSecurity;
import com.opengamma.financial.security.future.InterestRateFutureSecurity;
import com.opengamma.financial.security.fx.FXForwardSecurity;
import com.opengamma.financial.security.fx.NonDeliverableFXForwardSecurity;
import com.opengamma.financial.security.option.SwaptionSecurity;
import com.opengamma.financial.security.swap.InterestRateNotional;
import com.opengamma.financial.security.swap.SwapSecurity;

/**
 * Base function for all pricing and risk functions that use curves constructed using the Hull-White discounting method. Produces results for trades with
 * following underlying securities:
 * <ul>
 * <li>{@link CashSecurity}
 * <li>{@link CashFlowSecurity}
 * <li>{@link FRASecurity}
 * <li>{@link SwapSecurity}
 * <li>{@link SwaptionSecurity}
 * <li>{@link InterestRateFutureSecurity}
 * <li>{@link FXForwardSecurity}
 * <li>{@link NonDeliverableFXForwardSecurity}
 * <li>{@link DeliverableSwapFutureSecurity}
 * <li>{@link FederalFundsFutureSecurity}
 * </ul>
 */
public abstract class HullWhiteDiscountingFunction extends MultiCurvePricingFunction {

  /**
   * @param valueRequirements
   *          The value requirements, not null
   */
  public HullWhiteDiscountingFunction(final String... valueRequirements) {
    super(valueRequirements);
  }

  @Override
  protected DefaultTradeConverter getTargetToDefinitionConverter(final FunctionCompilationContext context) {
    final SecuritySource securitySource = OpenGammaCompilationContext.getSecuritySource(context);
    final HolidaySource holidaySource = OpenGammaCompilationContext.getHolidaySource(context);
    final RegionSource regionSource = OpenGammaCompilationContext.getRegionSource(context);
    final ConventionSource conventionSource = OpenGammaCompilationContext.getConventionSource(context);
    final CashSecurityConverter cashConverter = new CashSecurityConverter(holidaySource, regionSource);
    final CashFlowSecurityConverter cashFlowConverter = new CashFlowSecurityConverter();
    final FRASecurityConverter fraConverter = new FRASecurityConverter(securitySource, holidaySource, regionSource, conventionSource);
    final SwapSecurityConverter swapConverter = new SwapSecurityConverter(securitySource, holidaySource, conventionSource, regionSource);
    final InterestRateSwapSecurityConverter irsConverter = new InterestRateSwapSecurityConverter(holidaySource, conventionSource, securitySource);
    final SwaptionSecurityConverter swaptionConverter = new SwaptionSecurityConverter(swapConverter, irsConverter);
    final FXForwardSecurityConverter fxForwardSecurityConverter = new FXForwardSecurityConverter();
    final NonDeliverableFXForwardSecurityConverter nonDeliverableFXForwardSecurityConverter = new NonDeliverableFXForwardSecurityConverter();
    final DeliverableSwapFutureSecurityConverter dsfConverter = new DeliverableSwapFutureSecurityConverter(securitySource, swapConverter, irsConverter);
    final FederalFundsFutureTradeConverter federalFundsFutureTradeConverter = new FederalFundsFutureTradeConverter(securitySource, holidaySource,
        conventionSource, regionSource);
    final FinancialSecurityVisitor<InstrumentDefinition<?>> securityConverter = FinancialSecurityVisitorAdapter.<InstrumentDefinition<?>> builder()
        .cashSecurityVisitor(cashConverter)
        .cashFlowSecurityVisitor(cashFlowConverter).deliverableSwapFutureSecurityVisitor(dsfConverter).fraSecurityVisitor(fraConverter)
        .swapSecurityVisitor(swapConverter)
        .fxForwardVisitor(fxForwardSecurityConverter).nonDeliverableFxForwardVisitor(nonDeliverableFXForwardSecurityConverter)
        .swaptionVisitor(swaptionConverter).create();
    final FutureTradeConverter futureTradeConverter = new FutureTradeConverter();
    final InterestRateFutureTradeConverter irFutureTradeConveter = new InterestRateFutureTradeConverter(securitySource, holidaySource, conventionSource,
        regionSource);
    final DeliverableSwapFutureTradeConverter deliverableSwapFutureTradeConverter = new DeliverableSwapFutureTradeConverter(securitySource, swapConverter,
        irsConverter);
    return new DefaultTradeConverter(futureTradeConverter, federalFundsFutureTradeConverter, irFutureTradeConveter, deliverableSwapFutureTradeConverter,
        securityConverter);
  }

  /**
   * Base compiled function for all pricing and risk functions that use the Hull-White one-factor curve construction method.
   */
  protected abstract class HullWhiteCompiledFunction extends MultiCurveCompiledFunction {
    private final boolean _withCurrency;

    /**
     * @param tradeToDefinitionConverter
     *          Converts targets to definitions, not null
     * @param definitionToDerivativeConverter
     *          Converts definitions to derivatives, not null
     * @param withCurrency
     *          True if the result properties set the {@link com.opengamma.engine.value.ValuePropertyNames#CURRENCY} property
     */
    protected HullWhiteCompiledFunction(final DefaultTradeConverter tradeToDefinitionConverter,
        final FixedIncomeConverterDataProvider definitionToDerivativeConverter, final boolean withCurrency) {
      super(tradeToDefinitionConverter, definitionToDerivativeConverter);
      _withCurrency = withCurrency;
    }

    @Override
    public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
      boolean canApplyTo = super.canApplyTo(context, target);
      final Security security = target.getTrade().getSecurity();
      if (security instanceof SwapSecurity && InterestRateInstrumentType.isFixedIncomeInstrumentType((SwapSecurity) security)) {
        canApplyTo &= InterestRateInstrumentType.getInstrumentTypeFromSecurity((SwapSecurity) security) != InterestRateInstrumentType.SWAP_CROSS_CURRENCY;
      }
      return canApplyTo || security instanceof SwaptionSecurity || security instanceof DeliverableSwapFutureSecurity
          || security instanceof FederalFundsFutureSecurity;
    }

    @SuppressWarnings("synthetic-access")
    @Override
    protected Collection<ValueProperties.Builder> getResultProperties(final FunctionCompilationContext compilationContext, final ComputationTarget target) {
      final ValueProperties.Builder properties = createValueProperties().with(PROPERTY_CURVE_TYPE, HULL_WHITE_DISCOUNTING).withAny(CURVE_EXPOSURES)
          .withAny(PROPERTY_HULL_WHITE_PARAMETERS);
      if (_withCurrency) {
        final Security security = target.getTrade().getSecurity();
        if (security instanceof SwapSecurity && InterestRateInstrumentType.isFixedIncomeInstrumentType((SwapSecurity) security)
            && InterestRateInstrumentType.getInstrumentTypeFromSecurity((SwapSecurity) security) == InterestRateInstrumentType.SWAP_CROSS_CURRENCY) {
          final SwapSecurity swapSecurity = (SwapSecurity) security;
          if (swapSecurity.getPayLeg().getNotional() instanceof InterestRateNotional) {
            final String currency = ((InterestRateNotional) swapSecurity.getPayLeg().getNotional()).getCurrency().getCode();
            properties.with(CURRENCY, currency);
            return Collections.singleton(properties);
          }
        } else if (security instanceof FXForwardSecurity || security instanceof NonDeliverableFXForwardSecurity) {
          properties.with(CURRENCY, ((FinancialSecurity) security).accept(ForexVisitors.getPayCurrencyVisitor()).getCode());
        } else {
          properties.with(CURRENCY, FinancialSecurityUtils.getCurrency(target.getTrade().getSecurity()).getCode());
        }
        // TODO: Handle the multiple currency case (SWAP_CROSS_CURRENCY) by returning a collection with more than one element
      }
      return Collections.singleton(properties);
    }

    @Override
    protected boolean requirementsSet(final ValueProperties constraints) {
      final Set<String> curveExposureConfigs = constraints.getValues(CURVE_EXPOSURES);
      if (curveExposureConfigs == null) {
        return false;
      }
      final Set<String> hullWhiteParameters = constraints.getValues(PROPERTY_HULL_WHITE_PARAMETERS);
      if (hullWhiteParameters == null || hullWhiteParameters.size() != 1) {
        return false;
      }
      return true;
    }

    @Override
    protected Builder getCurveConstraints(final ComputationTarget target, final ValueProperties constraints) {
      final String currency = FinancialSecurityUtils.getCurrency(target.getTrade().getSecurity()).getCode();
      final Set<String> hullWhiteParameters = constraints.getValues(PROPERTY_HULL_WHITE_PARAMETERS);
      return ValueProperties.builder().with(PROPERTY_HULL_WHITE_PARAMETERS, hullWhiteParameters).with(PROPERTY_HULL_WHITE_CURRENCY, currency);
    }

    /**
     * Merges any {@link HullWhiteOneFactorProviderDiscount} curve bundles and FX matrices that are present in the inputs and creates a curve bundle with
     * information for pricing using the Hull-White one factor model.
     *
     * @param inputs
     *          The function inputs
     * @param matrix
     *          The FX matrix
     * @return A curve bundle that can be used in Hull-White one factor model pricing functions
     */
    protected HullWhiteOneFactorProviderDiscount getMergedProviders(final FunctionInputs inputs, final FXMatrix matrix) {
      final Collection<HullWhiteOneFactorProviderDiscount> providers = new HashSet<>();
      for (final ComputedValue input : inputs.getAllValues()) {
        final String valueName = input.getSpecification().getValueName();
        if (CURVE_BUNDLE.equals(valueName)) {
          providers.add((HullWhiteOneFactorProviderDiscount) input.getValue());
        }
      }
      final HullWhiteOneFactorProviderDiscount result = ProviderUtils.mergeHullWhiteProviders(providers);
      return ProviderUtils.mergeHullWhiteProviders(result, matrix);
    }

    /**
     * Merges any {@link CurveBuildingBlockBundle}s in the function inputs.
     *
     * @param inputs
     *          The function inputs
     * @return A curve building block bundle that contains all of the information used to construct the curves used in pricing
     */
    protected CurveBuildingBlockBundle getMergedCurveBuildingBlocks(final FunctionInputs inputs) {
      final CurveBuildingBlockBundle result = new CurveBuildingBlockBundle();
      for (final ComputedValue input : inputs.getAllValues()) {
        final String valueName = input.getSpecification().getValueName();
        if (valueName.equals(JACOBIAN_BUNDLE)) {
          result.addAll((CurveBuildingBlockBundle) input.getValue());
        }
      }
      return result;
    }
  }
}
