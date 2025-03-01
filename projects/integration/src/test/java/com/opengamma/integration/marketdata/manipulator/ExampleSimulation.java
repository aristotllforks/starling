/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator;

import static com.opengamma.integration.marketdata.manipulator.dsl.SimulationUtils.volShift;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Instant;
import org.threeten.bp.Period;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.engine.marketdata.spec.LiveMarketDataSpecification;
import com.opengamma.engine.marketdata.spec.MarketDataSpecification;
import com.opengamma.engine.view.ViewComputationResultModel;
import com.opengamma.engine.view.ViewDeltaResultModel;
import com.opengamma.engine.view.ViewProcessor;
import com.opengamma.engine.view.compilation.CompiledViewDefinition;
import com.opengamma.engine.view.listener.AbstractViewResultListener;
import com.opengamma.id.UniqueId;
import com.opengamma.integration.marketdata.manipulator.dsl.Scenario;
import com.opengamma.integration.marketdata.manipulator.dsl.ScenarioShiftType;
import com.opengamma.integration.marketdata.manipulator.dsl.Simulation;
import com.opengamma.integration.marketdata.manipulator.dsl.SimulationUtils;
import com.opengamma.integration.server.RemoteServer;
import com.opengamma.livedata.UserPrincipal;

/**
 * Demonstration of the most straightforward way to run a simulation. Assumes the examples-simulated server is running locally with the default configuration
 * and data.
 */
/* package */ class ExampleSimulation {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleSimulation.class);

  private static final Set<String> CURRENCY_PAIRS = ImmutableSet.of("GBPUSD", "EURUSD", "USDJPY", "CHFUSD");
  private static final List<Double> SCALING_FACTORS = ImmutableList.of(0.95, 1.0, 1.05);

  public static void main(final String[] args) {

    // set up the connection to the server that will run the simulation ------------------------------------------------

    try (RemoteServer server = RemoteServer.create("http://localhost:8080")) {
      final ViewProcessor viewProcessor = server.getViewProcessor();
      final ConfigSource configSource = server.getConfigSource();
      final String viewDefinitionName = "AUD Swaps (3m / 6m basis) (1)";
      final UniqueId viewDefId = SimulationUtils.latestViewDefinitionId(viewDefinitionName, configSource);
      final List<MarketDataSpecification> marketDataSpecs = ImmutableList
          .<MarketDataSpecification> of(LiveMarketDataSpecification.of("Simulated live market data"));

      // define the simulation -----------------------------------------------------------------------------------------

      final Simulation simulation = new Simulation("Example simulation");
      for (final Double scalingFactor : SCALING_FACTORS) {
        // add a scenario (a single calculation cycle and set of results) for each scale factor
        final Scenario scenario = simulation.scenario(Double.toString(scalingFactor));
        for (final String currencyPair : CURRENCY_PAIRS) {
          // bump each spot rate in the scenario by the scale factor
          scenario.marketDataPoint().id("OG_SYNTHETIC_TICKER", currencyPair).apply().scaling(scalingFactor);
        }
        scenario.curve().named("foo").currencies("USD").apply().parallelShift(0.1);
        scenario.surface().named("bar").apply().shifts(ScenarioShiftType.ABSOLUTE, volShift(Period.ofMonths(6), 3.5, 0.1),
            volShift(Period.ofYears(1), 4.5, 0.2));
        scenario.spotRate().currencyPair("EURUSD").apply().scaling(0.1);
      }

      // run the simulation --------------------------------------------------------------------------------------------

      simulation.run(viewDefId, marketDataSpecs, false, new Listener(), viewProcessor);
    }
  }

  private static class Listener extends AbstractViewResultListener {

    @Override
    public UserPrincipal getUser() {
      return UserPrincipal.getTestUser();
    }

    @Override
    public void viewDefinitionCompiled(final CompiledViewDefinition compiledViewDefinition, final boolean hasMarketDataPermissions) {
      LOGGER.info("view definition compiled");
    }

    @Override
    public void viewDefinitionCompilationFailed(final Instant valuationTime, final Exception exception) {
      LOGGER.warn("view definition compilation failed", exception);
    }

    @Override
    public void cycleCompleted(final ViewComputationResultModel fullResult, final ViewDeltaResultModel deltaResult) {
      LOGGER.info("cycle completed");
    }
  }

}
