/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.future;

import com.opengamma.analytics.financial.future.CostOfCarryFuturesCalculator;
import com.opengamma.engine.value.ValueRequirementNames;

/**
 *
 */
public class CostOfCarryValueDeltaFuturesFunction extends CostOfCarryFuturesFunction<Double> {

  /**
   * @param closingPriceField The field name of the historical time series for price, e.g. "PX_LAST", "Close". Set in *FunctionConfiguration
   * @param costOfCarryField The field name of the historical time series for cost of carry e.g. "COST_OF_CARRY". Set in *FunctionConfiguration
   * @param resolutionKey The key defining how the time series resolution is to occur e.g. "DEFAULT_TSS_CONFIG"
   */
  public CostOfCarryValueDeltaFuturesFunction(final String closingPriceField, final String costOfCarryField, final String resolutionKey) {
    super(ValueRequirementNames.VALUE_DELTA, CostOfCarryFuturesCalculator.SpotDeltaCalculator.getInstance(),
        closingPriceField, costOfCarryField, resolutionKey);
  }
}
