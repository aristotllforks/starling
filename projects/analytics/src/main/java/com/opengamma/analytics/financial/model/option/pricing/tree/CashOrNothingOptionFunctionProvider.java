/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.model.option.pricing.tree;

/**
 * Cash-or-nothing call option pays 0 if S &lt;= K and 1 if S &gt; K, whereas cash-or-nothing put option pays 1 if S &lt; K and 0 if S &gt;= K, where S is asset
 * price at expiry.
 */
public class CashOrNothingOptionFunctionProvider extends OptionFunctionProvider1D {

  /**
   * @param strike Strike price
   * @param timeToExpiry Time to expiry
   * @param steps Number of steps
   * @param isCall True if call, false if put
   */
  public CashOrNothingOptionFunctionProvider(final double strike, final double timeToExpiry, final int steps, final boolean isCall) {
    super(strike, timeToExpiry, steps, isCall);
  }

  @Override
  public double[] getPayoffAtExpiry(final double assetPrice, final double downFactor, final double upOverDown) {
    final double strike = getStrike();
    final int nSteps = getNumberOfSteps();
    final int nStepsP = nSteps + 1;
    final double sign = getSign();

    final double[] values = new double[nStepsP];
    double priceTmp = assetPrice * Math.pow(downFactor, nSteps);
    for (int i = 0; i < nStepsP; ++i) {
      values[i] = sign * (priceTmp - strike) > 0. ? 1. : 0.;
      priceTmp *= upOverDown;
    }
    return values;
  }

  @Override
  public double[] getPayoffAtExpiryTrinomial(final double assetPrice, final double downFactor, final double middleOverDown) {
    final double strike = getStrike();
    final int nSteps = getNumberOfSteps();
    final int nNodes = 2 * getNumberOfSteps() + 1;
    final double sign = getSign();

    final double[] values = new double[nNodes];
    double priceTmp = assetPrice * Math.pow(downFactor, nSteps);
    for (int i = 0; i < nNodes; ++i) {
      values[i] = sign * (priceTmp - strike) > 0. ? 1. : 0.;
      priceTmp *= middleOverDown;
    }
    return values;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CashOrNothingOptionFunctionProvider)) {
      return false;
    }
    return super.equals(obj);
  }
}
