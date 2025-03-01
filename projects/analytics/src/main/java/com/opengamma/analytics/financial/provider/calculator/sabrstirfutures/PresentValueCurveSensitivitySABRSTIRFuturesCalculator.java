/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.calculator.sabrstirfutures;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorAdapter;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureOptionMarginTransaction;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureOptionPremiumTransaction;
import com.opengamma.analytics.financial.interestrate.future.provider.InterestRateFutureOptionMarginTransactionSABRMethod;
import com.opengamma.analytics.financial.interestrate.future.provider.InterestRateFutureOptionPremiumTransactionSABRMethod;
import com.opengamma.analytics.financial.provider.description.interestrate.SABRSTIRFuturesProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyMulticurveSensitivity;

/**
 * Calculator of the present value as a multiple currency amount.
 */
public final class PresentValueCurveSensitivitySABRSTIRFuturesCalculator
    extends InstrumentDerivativeVisitorAdapter<SABRSTIRFuturesProviderInterface, MultipleCurrencyMulticurveSensitivity> {

  /**
   * The unique instance of the calculator.
   */
  private static final PresentValueCurveSensitivitySABRSTIRFuturesCalculator INSTANCE = new PresentValueCurveSensitivitySABRSTIRFuturesCalculator();

  /**
   * Gets the calculator instance.
   *
   * @return The calculator.
   */
  public static PresentValueCurveSensitivitySABRSTIRFuturesCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Constructor.
   */
  private PresentValueCurveSensitivitySABRSTIRFuturesCalculator() {
  }

  // ----- Futures ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitInterestRateFutureOptionMarginTransaction(
      final InterestRateFutureOptionMarginTransaction futures,
      final SABRSTIRFuturesProviderInterface sabr) {
    return InterestRateFutureOptionMarginTransactionSABRMethod.getInstance().presentValueCurveSensitivity(futures, sabr);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitInterestRateFutureOptionPremiumTransaction(
      final InterestRateFutureOptionPremiumTransaction futures,
      final SABRSTIRFuturesProviderInterface sabr) {
    return InterestRateFutureOptionPremiumTransactionSABRMethod.getInstance().presentValueCurveSensitivity(futures, sabr);
  }
}
