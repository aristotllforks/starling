/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.calcnode;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.ArgumentChecker;

/**
 * A builder for {@link CalculationJobResultItem}.
 */
public final class CalculationJobResultItemBuilder {

  private Set<ValueSpecification> _missingInputs;
  private Set<ValueSpecification> _missingOutputs;
  private final MutableExecutionLog _executionLog;

  private CalculationJobResultItemBuilder(final MutableExecutionLog executionLog) {
    ArgumentChecker.notNull(executionLog, "executionLog");
    _executionLog = executionLog;
  }

  //-------------------------------------------------------------------------
  public static CalculationJobResultItemBuilder of(final MutableExecutionLog executionLog) {
    return new CalculationJobResultItemBuilder(executionLog);
  }

  //-------------------------------------------------------------------------
  public CalculationJobResultItemBuilder withMissingInputs(final Set<ValueSpecification> missingInputs) {
    ArgumentChecker.notNull(missingInputs, "missingInputs");
    _missingInputs = missingInputs;
    withException(CalculationJobResultItem.MISSING_INPUTS_FAILURE_CLASS, "Unable to execute because of " + missingInputs.size() + " missing input(s)");
    return this;
  }

  public CalculationJobResultItemBuilder withPartialInputs(final Set<ValueSpecification> missingInputs) {
    ArgumentChecker.notNull(missingInputs, "missingInputs");
    _missingInputs = missingInputs;
    return this;
  }

  public CalculationJobResultItemBuilder withMissingOutputs(final Set<ValueSpecification> missingOutputs) {
    ArgumentChecker.notNull(missingOutputs, "missingOutputs");
    _missingOutputs = missingOutputs;
    return this;
  }

  public CalculationJobResultItemBuilder withSuppression() {
    withException(CalculationJobResultItem.EXECUTION_SUPPRESSED_CLASS, CalculationJobResultItem.EXECUTION_SUPPRESSED_MESSAGE);
    return this;
  }

  public CalculationJobResultItemBuilder withException(final Throwable t) {
    _executionLog.setException(t);
    return this;
  }

  public CalculationJobResultItemBuilder withException(final String exceptionClass, final String exceptionMessage) {
    _executionLog.setException(exceptionClass, exceptionMessage);
    return this;
  }

  //-------------------------------------------------------------------------
  public CalculationJobResultItem toResultItem() {
    final Set<ValueSpecification> missingInputs = _missingInputs != null ? _missingInputs : ImmutableSet.<ValueSpecification>of();
    final Set<ValueSpecification> missingOutputs = _missingOutputs != null ? _missingOutputs : ImmutableSet.<ValueSpecification>of();
    return new CalculationJobResultItem(missingInputs, missingOutputs, _executionLog);
  }

}
