/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.depgraph.ambiguity;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Instant;

import com.opengamma.core.position.PortfolioNode;
import com.opengamma.core.position.Position;
import com.opengamma.core.position.Trade;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.exclusion.FunctionExclusionGroups;
import com.opengamma.engine.function.resolver.FunctionResolver;
import com.opengamma.engine.marketdata.availability.DefaultMarketDataAvailabilityProvider;
import com.opengamma.engine.marketdata.availability.MarketDataAvailabilityProvider;
import com.opengamma.engine.marketdata.availability.OptimisticMarketDataAvailabilityFilter;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.view.ViewCalculationConfiguration;
import com.opengamma.engine.view.ViewDefinition;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.PoolExecutor;
import com.opengamma.util.PoolExecutor.CompletionListener;
import com.opengamma.util.tuple.Pair;

/**
 * Base class for an integration test that can scan view definitions for ambiguities.
 */
public abstract class ViewDefinitionAmbiguityTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ViewDefinitionAmbiguityTest.class);

  protected MarketDataAvailabilityProvider createMarketDataAvailabilityProvider() {
    return new OptimisticMarketDataAvailabilityFilter().withProvider(new DefaultMarketDataAvailabilityProvider());
  }

  protected FunctionCompilationContext createFunctionCompilationContext() {
    return new FunctionCompilationContext();
  }

  protected abstract FunctionResolver createFunctionResolver();

  protected FunctionExclusionGroups createFunctionExclusionGroups() {
    return null;
  }

  protected AmbiguityCheckerContext createAmbiguityCheckerContext() {
    return new AmbiguityCheckerContext(createMarketDataAvailabilityProvider(), createFunctionCompilationContext(), createFunctionResolver(),
        createFunctionExclusionGroups());
  }

  protected void configureChecker(final SimpleRequirementAmbiguityChecker checker) {
    checker.setGreedyCaching(true);
    checker.setSharedCaching(true);
  }

  protected void report(final FullRequirementResolution resolution, final PrintStream out) {
    new FullRequirementResolutionPrinter(out).print(resolution);
  }

  protected void directAmbiguity(final FullRequirementResolution resolution) {
    synchronized (System.err) {
      System.err.println("Got ambiguity on " + resolution.getRequirement());
      report(resolution, System.err);
    }
  }

  protected void deepAmbiguity(final FullRequirementResolution resolution) {
    synchronized (System.err) {
      System.err.println("Got deep ambiguity on " + resolution.getRequirement());
      report(resolution, System.err);
    }
  }

  protected void resolved(final FullRequirementResolution resolution) {
    if (resolution.isAmbiguous()) {
      directAmbiguity(resolution);
    } else if (resolution.isDeeplyAmbiguous()) {
      deepAmbiguity(resolution);
    } else if (resolution.isResolved()) {
      unresolved(resolution.getRequirement());
    } else {
      LOGGER.debug("Resolved {} to {}", resolution.getRequirement(), resolution);
    }
  }

  protected void unresolved(final ValueRequirement requirement) {
    LOGGER.debug("Couldn't resolve {}", requirement);
  }

  protected void check(final PoolExecutor.Service<FullRequirementResolution> executor, final RequirementAmbiguityChecker checker,
      final ValueRequirement requirement) {
    executor.execute(new Callable<FullRequirementResolution>() {
      @Override
      public FullRequirementResolution call() throws Exception {
        return checker.resolve(requirement);
      }
    });
  }

  protected void checkRequirements(final PoolExecutor.Service<FullRequirementResolution> executor, final RequirementAmbiguityChecker checker,
      final Set<Pair<String, ValueProperties>> requirements,
      final Trade trade) {
    for (final Pair<String, ValueProperties> requirement : requirements) {
      check(executor, checker, new ValueRequirement(requirement.getFirst(),
          new ComputationTargetSpecification(ComputationTargetType.TRADE, trade.getUniqueId()), requirement.getSecond()));
    }
  }

  protected void checkRequirements(final PoolExecutor.Service<FullRequirementResolution> executor, final RequirementAmbiguityChecker checker,
      final Map<String, Set<Pair<String, ValueProperties>>> requirements, final PortfolioNode node, final Position position,
      final Set<Pair<String, ValueProperties>> aggregate) {
    final Set<Pair<String, ValueProperties>> securityRequirements = requirements.get(position.getSecurity().getSecurityType());
    if (securityRequirements != null) {
      for (final Trade trade : position.getTrades()) {
        checkRequirements(executor, checker, securityRequirements, trade);
      }
      final ComputationTargetSpecification nodeSpec = new ComputationTargetSpecification(ComputationTargetType.PORTFOLIO_NODE, node.getUniqueId());
      for (final Pair<String, ValueProperties> requirement : securityRequirements) {
        check(executor, checker,
            new ValueRequirement(requirement.getFirst(), nodeSpec.containing(ComputationTargetType.POSITION, position.getUniqueId()), requirement.getSecond()));
      }
      aggregate.addAll(securityRequirements);
    }
  }

  protected void checkRequirements(final PoolExecutor.Service<FullRequirementResolution> executor, final RequirementAmbiguityChecker checker,
      final Map<String, Set<Pair<String, ValueProperties>>> requirements, final PortfolioNode node, final Set<Pair<String, ValueProperties>> aggregate) {
    for (final PortfolioNode childNode : node.getChildNodes()) {
      checkRequirements(executor, checker, requirements, childNode, aggregate);
    }
    for (final Position position : node.getPositions()) {
      checkRequirements(executor, checker, requirements, node, position, aggregate);
    }
    for (final Pair<String, ValueProperties> requirement : aggregate) {
      check(executor, checker, new ValueRequirement(requirement.getFirst(),
          new ComputationTargetSpecification(ComputationTargetType.PORTFOLIO_NODE, node.getUniqueId()), requirement.getSecond()));
    }
  }

  public void runAmbiguityTest(final ViewDefinition view) throws InterruptedException {
    final PoolExecutor executor = new PoolExecutor(Runtime.getRuntime().availableProcessors(), "AmbiguityCheck");
    try {
      final PoolExecutor.Service<FullRequirementResolution> service = executor.createService(new CompletionListener<FullRequirementResolution>() {

        @Override
        public void success(final FullRequirementResolution result) {
          try {
            resolved(result);
          } catch (final Throwable t) {
            failure(t);
          }
        }

        @Override
        public void failure(final Throwable error) {
          LOGGER.error("Internal failure", error);
        }

      });
      final AmbiguityCheckerContext context = createAmbiguityCheckerContext();
      final long tStart = System.nanoTime();
      for (final ViewCalculationConfiguration calcConfig : view.getAllCalculationConfigurations()) {
        LOGGER.info("Testing {}.{}", view.getName(), calcConfig.getName());
        final SimpleRequirementAmbiguityChecker checker = new SimpleRequirementAmbiguityChecker(context, Instant.now(), VersionCorrection.LATEST, calcConfig);
        final Set<Pair<String, ValueProperties>> aggregate = new HashSet<>();
        checkRequirements(service, checker, calcConfig.getPortfolioRequirementsBySecurityType(),
            checker.getCompilationContext().getPortfolio().getRootNode(), aggregate);
      }
      service.join();
      LOGGER.info("View {} tested in {}s", view.getName(), (System.nanoTime() - tStart) / 1e9);
    } finally {
      executor.asService().shutdown();
    }
  }

}
