/**
 * Copyright (C) 2017 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.analytics.financial.curve.interestrate.curvebuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZonedDateTime;

import com.mcleodmoores.analytics.financial.curve.interestrate.curvebuilder.CurveTypeSetUpInterface.CurveFunction;
import com.mcleodmoores.analytics.financial.index.IborTypeIndex;
import com.mcleodmoores.analytics.financial.index.OvernightIndex;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveAddYieldExisting;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveDiscountFactorInterpolated;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveDiscountFactorInterpolatedNode;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveYieldInterpolated;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveYieldInterpolatedNode;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveYieldNelsonSiegel;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveYieldPeriodicInterpolated;
import com.opengamma.analytics.math.interpolation.factory.LinearInterpolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.NamedInterpolator1dFactory;
import com.opengamma.financial.convention.businessday.BusinessDayConventions;
import com.opengamma.financial.convention.daycount.DayCounts;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.util.money.Currency;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.time.Tenor;

/**
 * Unit tests for {@link DiscountingMethodBondCurveTypeSetUp}.
 */
@Test(groups = TestGroup.UNIT)
public class DiscountingMethodBondCurveTypeSetUpTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(DiscountingMethodBondCurveTypeSetUpTest.class);
  private static final UniqueIdentifiable DISCOUNTING_ID = Currency.USD;
  private static final IborTypeIndex[] IBOR_INDICES = {
      new IborTypeIndex("A", Currency.USD, Tenor.THREE_MONTHS, 2, DayCounts.ACT_360, BusinessDayConventions.FOLLOWING, true),
      new IborTypeIndex("B", Currency.USD, Tenor.SIX_MONTHS, 2, DayCounts.ACT_360, BusinessDayConventions.FOLLOWING, true) };
  private static final OvernightIndex[] OVERNIGHT_INDICES = { new OvernightIndex("A", Currency.USD, DayCounts.ACT_360, 1),
      new OvernightIndex("B", Currency.USD, DayCounts.ACT_360, 1) };

  /**
   * Tests that the builder to copy cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullBuilder() {
    new DiscountingMethodBondCurveTypeSetUp(null);
  }

  /**
   * Reflective testing of builder methods - assumes that any method that returns the same type is a builder method, and that null inputs are invalid for all.
   * 
   * @throws IllegalAccessException
   *           if the class being tested cannot be accessed
   * @throws InstantiationException
   *           if an exception is created when the class to be tested is created
   */
  @Test
  public void testNullBuilderMethodInputs() throws InstantiationException, IllegalAccessException {
    final Class<?> clazz = DiscountingMethodBondCurveTypeSetUp.class;
    final Method[] methods = CurveTypeSetUpInterface.class.getDeclaredMethods();
    for (final Method method : methods) {
      if (method.getReturnType().isAssignableFrom(clazz) && method.getParameterTypes().length == 1) {
        if (method.getParameterTypes()[0].isPrimitive()) {
          LOGGER.warn("Method {} in {} has a primitive input type, not checking input for null", method.getName(), clazz.getSimpleName());
          continue;
        }
        final Object[] args = new Object[method.getParameterTypes().length]; // initialized with nulls
        // create a new object
        final Object builder = clazz.newInstance(); // relying on no-args constructor - should pass in list of arguments
        try {
          method.invoke(builder, args);
          fail("Method called " + method.getName() + " should check that the input is not null");
        } catch (final InvocationTargetException e) {
          final Throwable cause = e.getTargetException();
          assertTrue(cause instanceof IllegalArgumentException,
              method.getName() + " in " + clazz.getSimpleName() + " threw " + cause + "(" + cause.getMessage() + "): ");
          assertTrue(cause.getMessage().startsWith("Input parameter"));
        } catch (final Exception e) {
          fail(method.getName() + ": " + e);
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Tests that a functional curve type cannot be created if an interpolated / fixed date curve type has already started to be constructed.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testFunctionalCurveState1() {
    new DiscountingMethodBondCurveTypeSetUp()
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .functionalForm(CurveFunction.NELSON_SIEGEL);
  }

  /**
   * Tests that a functional curve type cannot be created if an interpolated / fixed date curve type has already started to be constructed.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testFunctionalCurveState2() {
    new DiscountingMethodBondCurveTypeSetUp()
        .usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) })
        .functionalForm(CurveFunction.NELSON_SIEGEL);
  }

  /**
   * Tests that a functional curve type cannot be created if an interpolated / fixed date curve type has already started to be constructed.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testFunctionalCurveState3() {
    new DiscountingMethodBondCurveTypeSetUp()
        .continuousInterpolationOnYield()
        .functionalForm(CurveFunction.NELSON_SIEGEL);
  }

  /**
   * Tests that a functional curve type cannot be created as a spread over another curve.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testFunctionalCurveState4() {
    new DiscountingMethodBondCurveTypeSetUp()
        .asSpreadOver("BASE")
        .functionalForm(CurveFunction.NELSON_SIEGEL);
  }

  /**
   * Tests the functional form curve generator.
   */
  @Test
  public void testFunctionalFormCurveGenerator1() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .functionalForm(CurveFunction.NELSON_SIEGEL);
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveYieldNelsonSiegel);
  }

  /**
   * Tests that an interpolated curve type cannot be created if a functional form has already been supplied.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testInterpolatedCurveState() {
    new DiscountingMethodBondCurveTypeSetUp()
        .functionalForm(CurveFunction.NELSON_SIEGEL)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME));
  }

  /**
   * Tests the interpolated curve generator.
   */
  @Test
  public void testInterpolatedCurveGenerator1() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME));
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveYieldInterpolated);
  }

  /**
   * Tests the interpolated curve generator.
   */
  @Test
  public void testInterpolatedCurveGenerator2() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .asSpreadOver("BASE")
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME));
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveAddYieldExisting);
  }

  /**
   * Tests that an fixed node curve type cannot be created if a functional form has already been supplied.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testFixedNodeCurveState() {
    new DiscountingMethodBondCurveTypeSetUp()
        .functionalForm(CurveFunction.NELSON_SIEGEL)
        .usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) });
  }

  /**
   * Tests that the fixed node curve generator cannot be created without an interpolator.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testFixedNodeGeneratorNoInterpolator() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) });
    setup.buildCurveGenerator(ZonedDateTime.now());
  }

  /**
   * Tests that there must be at least 2 nodes.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNumberOfNodes() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .usingNodeDates(new LocalDateTime[0]);
  }

  /**
   * Tests the fixed curve node generator.
   */
  @Test
  public void testFixedNodeGenerator1() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) });
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveYieldInterpolatedNode);
  }

  /**
   * Tests the fixed curve node generator.
   */
  @Test
  public void testFixedNodeGenerator2() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .asSpreadOver("BASE")
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) });
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveAddYieldExisting);
  }

  /**
   * Tests that an interpolated on yield curve type cannot be created if a functional form has already been set.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testInterpolatedOnYieldCurveState1() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .functionalForm(CurveFunction.NELSON_SIEGEL)
        .continuousInterpolationOnYield();
  }

  /**
   * Tests that an interpolated on yield curve type cannot be created if the type has already been set.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testInterpolatedOnYieldCurveState2() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .continuousInterpolationOnDiscountFactors()
        .continuousInterpolationOnYield();
  }

  /**
   * Tests that an interpolator must be set to interpolate on yields.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testInterpolatorSetForInterpolatedOnYield() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .continuousInterpolationOnYield()
        .buildCurveGenerator(ZonedDateTime.now());
  }

  /**
   * Tests the curve generator when interpolation on yield is explicitly chosen.
   */
  @Test
  public void testInterpolatedOnYieldCurveGenerator1() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .continuousInterpolationOnYield();
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveYieldInterpolated);
    assertTrue(setup.usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) })
        .buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveYieldInterpolatedNode);
  }

  /**
   * Tests the curve generator when interpolation on yield is explicitly chosen.
   */
  @Test
  public void testInterpolatedOnYieldCurveGenerator2() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .asSpreadOver("BASE")
        .continuousInterpolationOnYield();
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveAddYieldExisting);
    assertTrue(setup.usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) })
        .buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveAddYieldExisting);
  }

  /**
   * Tests that an interpolated on discount factor curve type cannot be created if a functional form has already been set.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testInterpolatedOnDiscountFactorCurveState1() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .functionalForm(CurveFunction.NELSON_SIEGEL)
        .continuousInterpolationOnDiscountFactors();
  }

  /**
   * Tests that an interpolated on discount factor curve type cannot be created if the type has already been set.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testInterpolatedOnDiscountFactorCurveState2() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .continuousInterpolationOnYield()
        .continuousInterpolationOnDiscountFactors();
  }

  /**
   * Tests that an interpolator must be set to interpolate on discount factors.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testInterpolatorSetForInterpolatedOnDiscountFactors() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .continuousInterpolationOnDiscountFactors()
        .buildCurveGenerator(ZonedDateTime.now());
  }

  /**
   * Tests the curve generator when interpolation on discount factors is chosen.
   */
  @Test
  public void testInterpolatedOnDiscountFactorCurveGenerator1() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .continuousInterpolationOnDiscountFactors();
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveDiscountFactorInterpolated);
    assertTrue(setup.usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) })
        .buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveDiscountFactorInterpolatedNode);
  }

  /**
   * Tests the curve generator when interpolation on discount factors is chosen.
   */
  @Test
  public void testInterpolatedOnDiscountFactorCurveGenerator2() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .asSpreadOver("BASE")
        .continuousInterpolationOnDiscountFactors();
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveAddYieldExisting);
    assertTrue(setup.usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) })
        .buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveAddYieldExisting);
  }

  /**
   * Tests that there must be at least one compounding period per year.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testCompoundingPeriodsPerYear() {
    new DiscountingMethodBondCurveTypeSetUp()
        .periodicInterpolationOnYield(0);
  }

  /**
   * Tests that a periodically compounded curve cannot be created if a functional form has already been set.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testPeriodicCurveState1() {
    new DiscountingMethodBondCurveTypeSetUp()
        .functionalForm(CurveFunction.NELSON_SIEGEL)
        .periodicInterpolationOnYield(4);
  }

  /**
   * Tests that a periodically compounded curve cannot be created if the type has already been set.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testPeriodicCurveState2() {
    new DiscountingMethodBondCurveTypeSetUp()
        .continuousInterpolationOnDiscountFactors()
        .periodicInterpolationOnYield(4);
  }

  /**
   * Tests that an interpolator must be set to interpolate on yields.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testInterpolatorSetForPeriodicCurve() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .periodicInterpolationOnYield(4)
        .buildCurveGenerator(ZonedDateTime.now());
  }

  /**
   * Tests that node dates cannot be set for curves with periodic yields.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testNoNodeDatesForPeriodicCurves1() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .periodicInterpolationOnYield(4)
        .usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) });
  }

  /**
   * Tests that node dates cannot be set for curves with periodic yields.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testNoNodeDatesForPeriodicCurves2() {
    new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .usingNodeDates(new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.now().plusDays(1) })
        .periodicInterpolationOnYield(4);
  }

  /**
   * Tests the curve generator when a periodic curve is chosen.
   */
  @Test
  public void testPeriodicCurveGenerator1() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .periodicInterpolationOnYield(4);
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveYieldPeriodicInterpolated);
  }

  /**
   * Tests the curve generator when a periodic curve is chosen.
   */
  @Test
  public void testPeriodicCurveGenerator2() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
        .asSpreadOver("BASE")
        .periodicInterpolationOnYield(4);
    assertTrue(setup.buildCurveGenerator(ZonedDateTime.now()) instanceof GeneratorCurveAddYieldExisting);
  }

  /**
   * Tests the getters.
   */
  @Test
  public void testGetters() {
    final DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
        .forDiscounting(DISCOUNTING_ID)
        .forIndex(IBOR_INDICES)
        .forIndex(OVERNIGHT_INDICES);
    assertEquals(setup.getDiscountingCurveId(), DISCOUNTING_ID);
    assertArrayEquals(setup.getIborCurveIndices().toArray(new IborTypeIndex[0]), IBOR_INDICES);
    assertArrayEquals(setup.getOvernightCurveIndices().toArray(new OvernightIndex[0]), OVERNIGHT_INDICES);
  }

  /**
   * Tests that a functional curve type cannot be created as a spread over another curve.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testSpreadCurveState() {
    new DiscountingMethodBondCurveTypeSetUp()
        .functionalForm(CurveFunction.NELSON_SIEGEL)
        .asSpreadOver("BASE");
  }

  /**
   * Tests that both node time calculators cannot be set.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testBothNodeTimeCalculatorsNotSet1() {
    new DiscountingMethodBondCurveTypeSetUp()
        .usingInstrumentMaturity()
        .usingLastFixingEndTime();
  }

  /**
   * Tests that both node time calculators cannot be set.
   */
  @Test(expectedExceptions = IllegalStateException.class)
  public void testBothNodeTimeCalculatorsNotSet2() {
    new DiscountingMethodBondCurveTypeSetUp()
        .usingLastFixingEndTime()
        .usingInstrumentMaturity();
  }

  /**
   * Tests the toString() method.
   */
  // @Test
  // public void testToString() {
  // DiscountingMethodBondCurveTypeSetUp setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME));
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], interpolator=Linear]");
  // setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .functionalForm(CurveFunction.NELSON_SIEGEL);
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], functionalForm=NELSON_SIEGEL]");
  // setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
  // .periodicInterpolationOnYield(4);
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], interpolator=Linear, "
  // + "periodsPerYear=4, interpolation on yield]");
  // setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
  // .continuousInterpolationOnYield();
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], interpolator=Linear, "
  // + "interpolation on yield]");
  // setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
  // .continuousInterpolationOnDiscountFactors();
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], interpolator=Linear, "
  // + "interpolation on discount factors]");
  // final LocalDateTime date = LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(0, 0));
  // setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
  // .continuousInterpolationOnDiscountFactors()
  // .usingNodeDates(new LocalDateTime[] {date, date.plusDays(1)});
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], interpolator=Linear, "
  // + "nodeDates=[2000-01-01T00:00, 2000-01-02T00:00], "
  // + "interpolation on discount factors]");
  // setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
  // .continuousInterpolationOnDiscountFactors()
  // .usingInstrumentMaturity();
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], interpolator=Linear, "
  // + "interpolation on discount factors, using instrument maturity]");
  // setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
  // .continuousInterpolationOnDiscountFactors()
  // .usingLastFixingEndTime();
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], interpolator=Linear, "
  // + "interpolation on discount factors, using last fixing period end]");
  // setup = new DiscountingMethodBondCurveTypeSetUp()
  // .forDiscounting(DISCOUNTING_ID)
  // .forIndex(IBOR_INDICES)
  // .forIndex(OVERNIGHT_INDICES)
  // .withInterpolator(NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME))
  // .continuousInterpolationOnDiscountFactors()
  // .usingLastFixingEndTime()
  // .asSpreadOver("BASE");
  // assertEquals(setup.toString(), "DiscountingMethodBondCurveTypeSetUp[discountingCurveId=USD, "
  // + "iborIndices=[IborIndex[A, currency=USD, tenor=P3M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month], "
  // + "IborIndex[B, currency=USD, tenor=P6M, day count=Actual/360, business day convention=Following, spot lag=2, end-of-month]], "
  // + "overnightIndices=[OvernightIndex[A, currency=USD, day count=Actual/360, publication lag=1], "
  // + "OvernightIndex[B, currency=USD, day count=Actual/360, publication lag=1]], interpolator=Linear, "
  // + "interpolation on discount factors, using last fixing period end, "
  // + "baseCurve=BASE]");
  // }

  /**
   * Test index array equivalence with multiple calls.
   */
  @Test
  public void testMultipleForIndexCalls() {
    final DiscountingMethodBondCurveTypeSetUp setup1 = new DiscountingMethodBondCurveTypeSetUp()
        .forIndex(IBOR_INDICES)
        .forIndex(OVERNIGHT_INDICES);
    final DiscountingMethodBondCurveTypeSetUp setup2 = new DiscountingMethodBondCurveTypeSetUp()
        .forIndex(IBOR_INDICES[0])
        .forIndex(IBOR_INDICES[1])
        .forIndex(OVERNIGHT_INDICES[0])
        .forIndex(OVERNIGHT_INDICES[1]);
    assertEquals(setup1.getIborCurveIndices(), setup2.getIborCurveIndices());
    assertEquals(setup1.getOvernightCurveIndices(), setup2.getOvernightCurveIndices());
  }
}
