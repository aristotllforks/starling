/**
 * Copyright (C) 2017 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.analytics.financial.curve.interestrate.curvebuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;
import java.time.ZonedDateTime;

import com.mcleodmoores.analytics.financial.curve.interestrate.curvebuilder.CurveSetUpInterface.RootFinderSetUp;
import com.mcleodmoores.analytics.financial.index.IborTypeIndex;
import com.mcleodmoores.analytics.financial.index.OvernightIndex;
import com.mcleodmoores.date.CalendarAdapter;
import com.mcleodmoores.date.WeekendWorkingDayCalendar;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorYDCurve;
import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.instrument.InstrumentDefinition;
import com.opengamma.analytics.financial.instrument.cash.DepositZeroDefinition;
import com.opengamma.analytics.financial.interestrate.PeriodicInterestRate;
import com.opengamma.analytics.financial.legalentity.LegalEntity;
import com.opengamma.analytics.financial.legalentity.LegalEntityFilter;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.analytics.financial.provider.curve.CurveBuildingBlockBundle;
import com.opengamma.analytics.financial.provider.curve.MultiCurveBundle;
import com.opengamma.analytics.math.interpolation.factory.NamedInterpolator1dFactory;
import com.opengamma.financial.convention.businessday.BusinessDayConventions;
import com.opengamma.financial.convention.daycount.DayCounts;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.util.i18n.Country;
import com.opengamma.util.money.Currency;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.time.DateUtils;
import com.opengamma.util.time.Tenor;
import com.opengamma.util.tuple.Pair;
import com.opengamma.util.tuple.Pairs;

/**
 * Unit tests for {@link DiscountingMethodBondCurveBuilder}.
 */
@Test(groups = TestGroup.UNIT)
public class DiscountingMethodBondCurveBuilderTest {
  private static final RootFinderSetUp ROOT_FINDER = new RootFinderSetUp();
  private static final List<List<String>> CURVE_NAMES = Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("C"));
  private static final List<Pair<String, UniqueIdentifiable>> DISCOUNTING = Arrays.asList(Pairs.<String, UniqueIdentifiable> of("A", Currency.USD),
      Pairs.<String, UniqueIdentifiable> of("C", Currency.EUR));
  private static final List<Pair<String, List<IborTypeIndex>>> IBOR = Arrays.asList(Pairs.of("B",
      Arrays.asList(new IborTypeIndex("Z", Currency.USD, Tenor.THREE_MONTHS, 2, DayCounts.ACT_360, BusinessDayConventions.FOLLOWING, false),
          new IborTypeIndex("Z", Currency.USD, Tenor.SIX_MONTHS, 2, DayCounts.ACT_360, BusinessDayConventions.FOLLOWING, false))));
  private static final List<Pair<String, List<OvernightIndex>>> OVERNIGHT = Arrays.asList(
      Pairs.of("A", Arrays.asList(new OvernightIndex("X", Currency.USD, DayCounts.ACT_360, 1))),
      Pairs.of("C", Arrays.asList(new OvernightIndex("V", Currency.EUR, DayCounts.ACT_360, 0))));
  private static final List<Pair<String, List<Pair<Object, LegalEntityFilter<LegalEntity>>>>> ISSUERS = Collections.emptyList();
  private static final Map<String, List<InstrumentDefinition<?>>> NODES = new HashMap<>();
  static {
    NODES.put("A",
        Arrays.asList(
            new DepositZeroDefinition(Currency.USD, DateUtils.getUTCDate(2020, 1, 1), DateUtils.getUTCDate(2020, 2, 1), 1,
                1. / 12, new PeriodicInterestRate(0.01, 12), CalendarAdapter.of(WeekendWorkingDayCalendar.SATURDAY_SUNDAY), DayCounts.ACT_360),
            new DepositZeroDefinition(Currency.USD, DateUtils.getUTCDate(2020, 1, 1), DateUtils.getUTCDate(2020, 4, 1), 1,
                1. / 4, new PeriodicInterestRate(0.012, 12), CalendarAdapter.of(WeekendWorkingDayCalendar.SATURDAY_SUNDAY), DayCounts.ACT_360)));
    NODES.put("B",
        Arrays.asList(
            new DepositZeroDefinition(Currency.USD, DateUtils.getUTCDate(2020, 1, 1), DateUtils.getUTCDate(2020, 2, 1), 1,
                1. / 12, new PeriodicInterestRate(0.01, 12), CalendarAdapter.of(WeekendWorkingDayCalendar.SATURDAY_SUNDAY), DayCounts.ACT_360),
            new DepositZeroDefinition(Currency.USD, DateUtils.getUTCDate(2020, 1, 1), DateUtils.getUTCDate(2020, 4, 1), 1,
                1. / 4, new PeriodicInterestRate(0.012, 12), CalendarAdapter.of(WeekendWorkingDayCalendar.SATURDAY_SUNDAY), DayCounts.ACT_360)));
    NODES.put("C",
        Arrays.asList(
            new DepositZeroDefinition(Currency.USD, DateUtils.getUTCDate(2020, 1, 1), DateUtils.getUTCDate(2020, 2, 1), 1,
                1. / 12, new PeriodicInterestRate(0.01, 12), CalendarAdapter.of(WeekendWorkingDayCalendar.SATURDAY_SUNDAY), DayCounts.ACT_360),
            new DepositZeroDefinition(Currency.USD, DateUtils.getUTCDate(2020, 1, 1), DateUtils.getUTCDate(2020, 4, 1), 1,
                1. / 4, new PeriodicInterestRate(0.014, 12), CalendarAdapter.of(WeekendWorkingDayCalendar.SATURDAY_SUNDAY), DayCounts.ACT_360)));
  }
  private static final FXMatrix FX = new FXMatrix(Currency.USD, Currency.EUR, 0.7);
  private static final Map<String, BondCurveTypeSetUpInterface> TYPES = new HashMap<>();
  static {
    TYPES.put("A",
        new DiscountingMethodBondCurveTypeSetUp().forDiscounting(Currency.USD).withInterpolator(NamedInterpolator1dFactory.of("Linear", "FlatExtrapolator")));
    TYPES.put("B",
        new DiscountingMethodBondCurveTypeSetUp().forDiscounting(Currency.USD).withInterpolator(NamedInterpolator1dFactory.of("Linear", "FlatExtrapolator")));
    TYPES.put("C",
        new DiscountingMethodBondCurveTypeSetUp().forDiscounting(Currency.USD).withInterpolator(NamedInterpolator1dFactory.of("Linear", "FlatExtrapolator")));
  }
  private static final Map<? extends PreConstructedBondCurveTypeSetUp, YieldAndDiscountCurve> KNOWN_CURVES = new HashMap<>();
  private static final CurveBuildingBlockBundle SENSITIVITIES = new CurveBuildingBlockBundle();
  private static final DiscountingMethodBondCurveBuilder BUILDER = new DiscountingMethodBondCurveBuilder(CURVE_NAMES, DISCOUNTING, IBOR, OVERNIGHT, ISSUERS,
      NODES,
      TYPES, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);

  /**
   * Tests that the curve names cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullBondCurveNames() {
    new DiscountingMethodBondCurveBuilder(null, DISCOUNTING, IBOR, OVERNIGHT, ISSUERS, NODES, TYPES, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);
  }

  /**
   * Tests that the curve names cannot be empty.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testEmptyBondCurveNames() {
    new DiscountingMethodBondCurveBuilder(new ArrayList<List<String>>(), DISCOUNTING, IBOR, OVERNIGHT, ISSUERS, NODES, TYPES, FX, KNOWN_CURVES, SENSITIVITIES,
        ROOT_FINDER);
  }

  /**
   * Tests that the discounting curve map cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullDiscountingIds() {
    new DiscountingMethodBondCurveBuilder(CURVE_NAMES, null, IBOR, OVERNIGHT, ISSUERS, NODES, TYPES, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);
  }

  /**
   * Tests that the ibor curve map cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullIborIndices() {
    new DiscountingMethodBondCurveBuilder(CURVE_NAMES, DISCOUNTING, null, OVERNIGHT, ISSUERS, NODES, TYPES, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);
  }

  /**
   * Tests that the overnight curve map cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullOvernightIndices() {
    new DiscountingMethodBondCurveBuilder(CURVE_NAMES, DISCOUNTING, IBOR, null, ISSUERS, NODES, TYPES, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);
  }

  /**
   * Tests that the issuer curve map cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullIssuers() {
    new DiscountingMethodBondCurveBuilder(CURVE_NAMES, DISCOUNTING, IBOR, OVERNIGHT, null, NODES, TYPES, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);
  }

  /**
   * Tests that the node instruments cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullNodes() {
    new DiscountingMethodBondCurveBuilder(CURVE_NAMES, DISCOUNTING, IBOR, OVERNIGHT, ISSUERS, null, TYPES, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);
  }

  /**
   * Tests that the curve types cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullBondCurveTypes() {
    new DiscountingMethodBondCurveBuilder(CURVE_NAMES, DISCOUNTING, IBOR, OVERNIGHT, ISSUERS, NODES, null, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);
  }

  /**
   * Tests that the root finder cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullRootFinder() {
    new DiscountingMethodBondCurveBuilder(CURVE_NAMES, DISCOUNTING, IBOR, OVERNIGHT, ISSUERS, NODES, TYPES, FX, KNOWN_CURVES, SENSITIVITIES, null);
  }

  /**
   * Tests that the curve bundles cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullCurveBundle() {
    BUILDER.buildCurves((List<MultiCurveBundle<GeneratorYDCurve>>) null);
  }

  /**
   * Tests that the valuation date cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullValuationDate1() {
    BUILDER.buildCurves(null, Collections.emptyMap());
  }

  /**
   * Tests that the valuation date cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullValuationDate2() {
    BUILDER.buildCurves((ZonedDateTime) null);
  }

  /**
   * Tests that the fixing map cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullFixingMap() {
    BUILDER.buildCurves(DateUtils.getUTCDate(2020, 1, 1), null);
  }

  /**
   * Tests that the discounting ids must all be currencies.
   */
  @Test(expectedExceptions = UnsupportedOperationException.class)
  public void testDiscountingIdsAreCurrencies() {
    final List<Pair<String, UniqueIdentifiable>> discountingBondCurves = Arrays.asList(Pairs.<String, UniqueIdentifiable> of("A", Currency.USD),
        Pairs.<String, UniqueIdentifiable> of("B", Country.AR), Pairs.<String, UniqueIdentifiable> of("C", Currency.EUR));
    final DiscountingMethodBondCurveBuilder builder = new DiscountingMethodBondCurveBuilder(CURVE_NAMES, discountingBondCurves, IBOR, OVERNIGHT, ISSUERS, NODES,
        TYPES, FX, KNOWN_CURVES, SENSITIVITIES, ROOT_FINDER);
    builder.buildCurves(DateUtils.getUTCDate(2020, 1, 1));
  }

}
