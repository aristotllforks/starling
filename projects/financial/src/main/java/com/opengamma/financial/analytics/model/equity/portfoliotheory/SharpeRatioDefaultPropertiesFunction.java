/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity.portfoliotheory;

import java.util.Collections;
import java.util.Set;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.financial.property.DefaultPropertyFunction;
import com.opengamma.util.ArgumentChecker;

/**
 *
 */
public class SharpeRatioDefaultPropertiesFunction extends DefaultPropertyFunction {
  private final String _samplingPeriodName;
  private final String _scheduleCalculatorName;
  private final String _samplingFunctionName;
  private final String _returnCalculatorName;
  private final String _stdDevCalculatorName;
  private final String _excessReturnCalculatorName;

  public SharpeRatioDefaultPropertiesFunction(final String samplingPeriodName, final String scheduleCalculatorName, final String samplingFunctionName,
      final String returnCalculatorName, final String stdDevCalculatorName, final String excessReturnCalculatorName, final ComputationTargetType type) {
    super(type, true);
    ArgumentChecker.notNull(samplingPeriodName, "sampling period name");
    ArgumentChecker.notNull(scheduleCalculatorName, "schedule calculator name");
    ArgumentChecker.notNull(samplingFunctionName, "sampling function name");
    ArgumentChecker.notNull(returnCalculatorName, "return calculator name");
    ArgumentChecker.notNull(stdDevCalculatorName, "standard deviation calculator name");
    ArgumentChecker.notNull(excessReturnCalculatorName, "excess return calculator name");
    _samplingPeriodName = samplingPeriodName;
    _scheduleCalculatorName = scheduleCalculatorName;
    _samplingFunctionName = samplingFunctionName;
    _returnCalculatorName = returnCalculatorName;
    _stdDevCalculatorName = stdDevCalculatorName;
    _excessReturnCalculatorName = excessReturnCalculatorName;
  }

  @Override
  protected void getDefaults(final PropertyDefaults defaults) {
    defaults.addValuePropertyName(ValueRequirementNames.SHARPE_RATIO, ValuePropertyNames.SAMPLING_PERIOD);
    defaults.addValuePropertyName(ValueRequirementNames.SHARPE_RATIO, ValuePropertyNames.SCHEDULE_CALCULATOR);
    defaults.addValuePropertyName(ValueRequirementNames.SHARPE_RATIO, ValuePropertyNames.SAMPLING_FUNCTION);
    defaults.addValuePropertyName(ValueRequirementNames.SHARPE_RATIO, ValuePropertyNames.RETURN_CALCULATOR);
    defaults.addValuePropertyName(ValueRequirementNames.SHARPE_RATIO, ValuePropertyNames.STD_DEV_CALCULATOR);
    defaults.addValuePropertyName(ValueRequirementNames.SHARPE_RATIO, ValuePropertyNames.EXCESS_RETURN_CALCULATOR);
  }

  @Override
  protected Set<String> getDefaultValue(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue,
      final String propertyName) {
    if (ValuePropertyNames.SAMPLING_PERIOD.equals(propertyName)) {
      return Collections.singleton(_samplingPeriodName);
    }
    if (ValuePropertyNames.SCHEDULE_CALCULATOR.equals(propertyName)) {
      return Collections.singleton(_scheduleCalculatorName);
    }
    if (ValuePropertyNames.SAMPLING_FUNCTION.equals(propertyName)) {
      return Collections.singleton(_samplingFunctionName);
    }
    if (ValuePropertyNames.RETURN_CALCULATOR.equals(propertyName)) {
      return Collections.singleton(_returnCalculatorName);
    }
    if (ValuePropertyNames.STD_DEV_CALCULATOR.equals(propertyName)) {
      return Collections.singleton(_stdDevCalculatorName);
    }
    if (ValuePropertyNames.EXCESS_RETURN_CALCULATOR.equals(propertyName)) {
      return Collections.singleton(_excessReturnCalculatorName);
    }
    return null;
  }

}
