/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.batch;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.batch.BatchRunWriter;
import com.opengamma.batch.RunCreationMode;
import com.opengamma.batch.SnapshotMode;
import com.opengamma.batch.domain.RiskRun;
import com.opengamma.engine.view.ViewComputationResultModel;
import com.opengamma.engine.view.ViewDeltaResultModel;
import com.opengamma.engine.view.cycle.ViewCycleMetadata;
import com.opengamma.engine.view.execution.ViewCycleExecutionOptions;
import com.opengamma.engine.view.listener.AbstractViewResultListener;
import com.opengamma.id.UniqueId;
import com.opengamma.livedata.UserPrincipal;
import com.opengamma.util.ArgumentChecker;

/**
 * View result listener implementation for batch runs.
 */
public class BatchDbViewResultListener extends AbstractViewResultListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(BatchDbViewResultListener.class);

  private final BatchRunWriter _batchRunWriter;
  private final UserPrincipal _user;
  private final Map<UniqueId, RiskRun> _riskRuns = Maps.newConcurrentMap();

  public BatchDbViewResultListener(final BatchRunWriter batchRunWriter, final UserPrincipal user) {
    ArgumentChecker.notNull(batchRunWriter, "batchRunWriter");
    ArgumentChecker.notNull(user, "user");
    _batchRunWriter = batchRunWriter;
    _user = user;
  }

  private RiskRun getRiskRun(final ViewComputationResultModel fullFragment, final ViewDeltaResultModel deltaFragment) {
    return _riskRuns.get(getCycleId(fullFragment, deltaFragment));
  }

  private static UniqueId getCycleId(final ViewComputationResultModel fullFragment, final ViewDeltaResultModel deltaFragment) {
    UniqueId cycleId;
    if (fullFragment != null) {
      cycleId = fullFragment.getViewCycleId();
    } else if (deltaFragment != null) {
      cycleId = deltaFragment.getViewCycleId();
    } else {
      throw new OpenGammaRuntimeException("both results fragments were null");
    }
    return cycleId;
  }

  @Override
  public UserPrincipal getUser() {
    return _user;
  }

  @Override
  public void cycleStarted(final ViewCycleMetadata cycleMetadata) {
    try {
      LOGGER.info("Starting new risk run for cycle ID {}", cycleMetadata.getViewCycleId());
      final RiskRun riskRun = _batchRunWriter.startRiskRun(cycleMetadata,
                                                     Collections.<String, String>emptyMap(),
                                                     RunCreationMode.CREATE_NEW,
                                                     SnapshotMode.WRITE_THROUGH);
      _riskRuns.put(cycleMetadata.getViewCycleId(), riskRun);
      LOGGER.info("New risk run started with ID {}, cycle ID {}",
                    riskRun.getId(), cycleMetadata.getViewCycleId());
    } catch (final Exception e) {
      LOGGER.error("Failed to write start of batch job. No results will be recorded.", e);
    }
  }

  @Override
  public void cycleCompleted(final ViewComputationResultModel fullResult, final ViewDeltaResultModel deltaResult) {
    try {
      final RiskRun riskRun = getRiskRun(fullResult, deltaResult);
      _batchRunWriter.endRiskRun(riskRun.getObjectId());
      LOGGER.info("Risk run ended, ID {}, cycle ID {}",
                    riskRun.getId(), getCycleId(fullResult, deltaResult));
    } catch (final Exception e) {
      LOGGER.error("Failed to write end of batch job. Job will appear incomplete.", e);
    }
  }

  @Override
  public void cycleFragmentCompleted(final ViewComputationResultModel fullFragment, final ViewDeltaResultModel deltaFragment) {
    final RiskRun riskRun = getRiskRun(fullFragment, deltaFragment);
    if (riskRun == null) {
      LOGGER.warn("Skipping writing batch result fragment due to earlier failure to write start of batch job");
      return;
    }
    try {
      _batchRunWriter.addJobResults(riskRun.getObjectId(), fullFragment);
      LOGGER.info("Added fragment results, ID {}, cycle ID {}",
                    riskRun.getId(), getCycleId(fullFragment, deltaFragment));
    } catch (final Exception e) {
      LOGGER.error("Error writing batch result fragment", e);
    }
  }

  @Override
  public void cycleExecutionFailed(final ViewCycleExecutionOptions executionOptions, final Exception exception) {
    LOGGER.error("Batch cycle execution failed", exception);
    // TODO there's no way of knowing which cycle has failed, need an extra parameter. PLAT-4173
    /*if (getRiskRun() == null) {
      LOGGER.warn("Skipping writing batch cycle failure due to earlier failure to write start of batch job");
      return;
    }
    try {
      _batchRunWriter.endRiskRun(getRiskRun().getObjectId());
    } catch (Exception e) {
      LOGGER.error("Error writing batch cycle failure", e);
    }*/
  }
}
