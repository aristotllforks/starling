/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity.portfoliotheory;

import com.opengamma.engine.target.ComputationTargetType;


/**
 *
 */
public class CAPMBetaDefaultPropertiesSecurityFunction extends CAPMBetaDefaultPropertiesFunction {

  public CAPMBetaDefaultPropertiesSecurityFunction(final String samplingPeriodName, final String scheduleCalculatorName, final String samplingFunctionName,
      final String returnCalculatorName, final String covarianceCalculatorName, final String varianceCalculatorName) {
    super(samplingPeriodName, scheduleCalculatorName, samplingFunctionName, returnCalculatorName, covarianceCalculatorName, varianceCalculatorName,
        ComputationTargetType.POSITION);
  }
}
