/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 *
 * Modified by McLeod Moores Software Limited.
 *
 * Copyright (C) 2015-Present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.analytics.math.interpolation;

import java.util.HashMap;
import java.util.Map;

import com.opengamma.analytics.financial.credit.cds.ISDAExtrapolator1D;
import com.opengamma.analytics.financial.credit.cds.ISDAInterpolator1D;
import com.opengamma.analytics.math.interpolation.factory.Interpolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.NamedInterpolator1d;
import com.opengamma.analytics.math.interpolation.factory.NamedInterpolator1dFactory;

/**
 * A factory for one-dimensional interpolators.
 * @deprecated  use {@link com.opengamma.analytics.math.interpolation.factory.NamedInterpolator1dFactory}
 */
@Deprecated
public final class Interpolator1DFactory {
  /** Linear. */
  public static final String LINEAR = "Linear";
  /** Exponential. */
  public static final String EXPONENTIAL = "Exponential";
  /** Log-linear. */
  public static final String LOG_LINEAR = "LogLinear";
  /** Natural cubic spline. */
  public static final String NATURAL_CUBIC_SPLINE = "NaturalCubicSpline";
  /** Barycentric rational function. */
  public static final String BARYCENTRIC_RATIONAL_FUNCTION = "BarycentricRationalFunction";
  /** Polynomial. */
  public static final String POLYNOMIAL = "Polynomial";
  /** Rational function. */
  public static final String RATIONAL_FUNCTION = "RationalFunction";
  /** Step. */
  public static final String STEP = "Step";
  /** Step with the value in the interval equal to the value at the upper bound. */
  public static final String STEP_UPPER = "StepUpper";
  /** Double quadratic. */
  public static final String DOUBLE_QUADRATIC = "DoubleQuadratic";
  /** Monotonicity-Preserving-Cubic-Spline.
   * @deprecated Use the name PCHIP instead
   */
  @Deprecated
  public static final String MONOTONIC_CUBIC = "MonotonicityPreservingCubicSpline";
  /**Piecewise Cubic Hermite Interpolating Polynomial (PCHIP). */
  public static final String PCHIP = "PCHIP";
  /**Modified Piecewise Cubic Hermite Interpolating Polynomial (PCHIP) for yield curves. */
  public static final String MOD_PCHIP = "ModifiedPCHIP";
  /** Time square. */
  public static final String TIME_SQUARE = "TimeSquare";
  /** Flat extrapolator. */
  public static final String FLAT_EXTRAPOLATOR = "FlatExtrapolator";
  /** Linear extrapolator. */
  public static final String LINEAR_EXTRAPOLATOR = "LinearExtrapolator";
  /** Log-linear extrapolator. */
  public static final String LOG_LINEAR_EXTRAPOLATOR = "LogLinearExtrapolator";
  /** Quadratic polynomial left extrapolator. */
  public static final String QUADRATIC_LEFT_EXTRAPOLATOR = "QuadraticLeftExtrapolator";
  /** Linear extrapolator. */
  public static final String EXPONENTIAL_EXTRAPOLATOR = "ExponentialExtrapolator";
  /** ISDA interpolator. */
  public static final String ISDA_INTERPOLATOR = "ISDAInterpolator";
  /** ISDA extrapolator. */
  public static final String ISDA_EXTRAPOLATOR = "ISDAExtrapolator";
  /** Linear instance. */
  public static final LinearInterpolator1D LINEAR_INSTANCE = new LinearInterpolator1D();
  /** Exponential instance. */
  public static final ExponentialInterpolator1D EXPONENTIAL_INSTANCE = new ExponentialInterpolator1D();
  /** Log-linear instance. */
  public static final LogLinearInterpolator1D LOG_LINEAR_INSTANCE = new LogLinearInterpolator1D();
  /** Natural cubic spline instance. */
  public static final NaturalCubicSplineInterpolator1D NATURAL_CUBIC_SPLINE_INSTANCE = new NaturalCubicSplineInterpolator1D();
  /** Step instance. */
  public static final StepInterpolator1D STEP_INSTANCE = new StepInterpolator1D();
  /** Step-Upper instance. */
  public static final StepUpperInterpolator1D STEP_UPPER_INSTANCE = new StepUpperInterpolator1D();
  /** Double quadratic instance. */
  public static final DoubleQuadraticInterpolator1D DOUBLE_QUADRATIC_INSTANCE = new DoubleQuadraticInterpolator1D();
  /** MonotonicityPreservingCubicSpline.
   * @deprecated use PCHIP_INSTANCE instead
   */
  @Deprecated
  public static final PCHIPInterpolator1D MONOTONIC_CUBIC_INSTANCE = new PCHIPInterpolator1D();
  /** Piecewise Cubic Hermite Interpolating Polynomial (PCHIP). */
  public static final PCHIPInterpolator1D PCHIP_INSTANCE = new PCHIPInterpolator1D();
  /** Modified Piecewise Cubic Hermite Interpolating Polynomial (PCHIP) for yield curves. */
  public static final PCHIPYieldCurveInterpolator1D MOD_PCHIP_INSTANCE = new PCHIPYieldCurveInterpolator1D();
  /** Time square instance. */
  public static final TimeSquareInterpolator1D TIME_SQUARE_INSTANCE = new TimeSquareInterpolator1D();
  /** Flat extrapolator instance. */
  public static final FlatExtrapolator1D FLAT_EXTRAPOLATOR_INSTANCE = new FlatExtrapolator1D();
  /** Exponential extrapolator instance. */
  public static final ExponentialExtrapolator1D EXPONENTIAL_EXTRAPOLATOR_INSTANCE = new ExponentialExtrapolator1D();
  /** ISDA interpolator instance. */
  public static final ISDAInterpolator1D ISDA_INTERPOLATOR_INSTANCE = new ISDAInterpolator1D();
  /** ISDA extrapolator instance. */
  public static final ISDAExtrapolator1D ISDA_EXTRAPOLATOR_INSTANCE = new ISDAExtrapolator1D();
  /**
   */

  /** Cubic spline with clamped endpoint conditions. */
  public static final String CLAMPED_CUBIC = "ClampedCubicSpline";
  /** Instance of cubic spline with clamped endpoint conditions. */
  public static final ClampedCubicSplineInterpolator1D CLAMPED_CUBIC_INSTANCE = new ClampedCubicSplineInterpolator1D();
  /** Cubic spline with clamped endpoint conditions and monotonicity filter. */
  public static final String CLAMPED_CUBIC_MONOTONE = "ClampedCubicSplineWithMonotonicity";
  /** Instance of cubic spline with clamped endpoint conditions and monotonicity filter. */
  public static final MonotonicityPreservingCubicSplineInterpolator1D CLAMPED_CUBIC_MONOTONE_INSTANCE =
      new MonotonicityPreservingCubicSplineInterpolator1D(new CubicSplineInterpolator());
  /** Cubic spline with clamped endpoint conditions and non-negativity filter. */
  public static final String CLAMPED_CUBIC_NONNEGATIVE = "ClampedCubicSplineWithNonnegativity";
  /** Instance of cubic spline with clamped endpoint conditions and non-negativity filter. */
  public static final NonnegativityPreservingCubicSplineInterpolator1D CLAMPED_CUBIC_NONNEGATIVE_INSTANCE =
      new NonnegativityPreservingCubicSplineInterpolator1D(new CubicSplineInterpolator());
  /** Quintic spline with clamped endpoint conditions and monotonicity filter. */
  public static final String CLAMPED_QUINTIC_MONOTONE = "ClampedQuinticSplineWithMonotonicity";
  /** Instance of quintic spline with clamped endpoint conditions and monotonicity filter. */
  public static final MonotonicityPreservingQuinticSplineInterpolator1D CLAMPED_QUINTIC_MONOTONE_INSTANCE =
      new MonotonicityPreservingQuinticSplineInterpolator1D(new CubicSplineInterpolator());
  /** Quintic spline with clamped endpoint conditions and non-negativity filter. */
  public static final String CLAMPED_QUINTIC_NONNEGATIVE = "ClampedQuinticSplineWithNonnegativity";
  /** Instance of quintic spline with clamped endpoint conditions non-negativity filter. */
  public static final NonnegativityPreservingQuinticSplineInterpolator1D CLAMPED_QUINTIC_NONNEGATIVE_INSTANCE =
      new NonnegativityPreservingQuinticSplineInterpolator1D(new CubicSplineInterpolator());

  /** Cubic spline with natural endpoint conditions and monotonicity filter. */
  public static final String NATURAL_CUBIC_MONOTONE = "NaturalCubicSplineWithMonotonicity";
  /** Instance of cubic spline with clamped endpoint conditions and monotonicity filter. */
  public static final MonotonicityPreservingCubicSplineInterpolator1D NATURAL_CUBIC_MONOTONE_INSTANCE =
      new MonotonicityPreservingCubicSplineInterpolator1D(new NaturalSplineInterpolator());
  /** Cubic spline with natural endpoint conditions and non-negativity filter. */
  public static final String NATURAL_CUBIC_NONNEGATIVE = "NaturalCubicSplineWithNonnegativity";
  /** Instance of cubic spline with clamped endpoint conditions and non-negativity filter. */
  public static final NonnegativityPreservingCubicSplineInterpolator1D NATURAL_CUBIC_NONNEGATIVE_INSTANCE =
      new NonnegativityPreservingCubicSplineInterpolator1D(new NaturalSplineInterpolator());
  /** Quintic spline with natural endpoint conditions and monotonicity filter. */
  public static final String NATURAL_QUINTIC_MONOTONE = "NaturalQuinticSplineWithMonotonicity";
  /** Instance of quintic spline with clamped endpoint conditions and monotonicity filter. */
  public static final MonotonicityPreservingQuinticSplineInterpolator1D NATURAL_QUINTIC_MONOTONE_INSTANCE =
      new MonotonicityPreservingQuinticSplineInterpolator1D(new NaturalSplineInterpolator());
  /** Quintic spline with natural endpoint conditions and nonnegativity filter. */
  public static final String NATURAL_QUINTIC_NONNEGATIVE = "NaturalQuinticSplineWithNonnegativity";
  /** Instance of quintic spline with clamped endpoint conditions and nonnegativity filter. */
  public static final NonnegativityPreservingQuinticSplineInterpolator1D NATURAL_QUINTIC_NONNEGATIVE_INSTANCE =
      new NonnegativityPreservingQuinticSplineInterpolator1D(new NaturalSplineInterpolator());

  /** Cubic spline with not-a-knot endpoint conditions. */
  public static final String NOTAKNOT_CUBIC = "NotAKnotCubicSpline";
  /** Instance of cubic spline with not-a-knot endpoint conditions. */
  public static final NotAKnotCubicSplineInterpolator1D NOTAKNOT_CUBIC_INSTANCE = new NotAKnotCubicSplineInterpolator1D();
  /** Cubic spline with not-a-knot endpoint conditions and monotonicity filter. */
  public static final String NOTAKNOT_CUBIC_MONOTONE = "NotAKnotCubicSplineWithMonotonicity";
  /** Instance of quintic spline with not-a-knot endpoint conditions and monotonicity filter. */
  public static final MonotonicityPreservingCubicSplineInterpolator1D NOTAKNOT_CUBIC_MONOTONE_INSTANCE =
      new MonotonicityPreservingCubicSplineInterpolator1D(new CubicSplineInterpolator());
  /** Cubic spline with not-a-knot endpoint conditions and non-negativity filter. */
  public static final String NOTAKNOT_CUBIC_NONNEGATIVE = "NotAKnotCubicSplineWithNonnegativity";
  /** Instance of quintic spline with not-a-knot endpoint conditions and non-negativity filter. */
  public static final NonnegativityPreservingCubicSplineInterpolator1D NOTAKNOT_CUBIC_NONNEGATIVE_INSTANCE =
      new NonnegativityPreservingCubicSplineInterpolator1D(new CubicSplineInterpolator());
  /** Quintic spline with not-a-knot endpoint conditions and monotonicity filter. */
  public static final String NOTAKNOT_QUINTIC_MONOTONE = "NotAKnotQuinticSplineWithMonotonicity";
  /** Instance of quintic spline with not-a-knot endpoint conditions and monotonicity filter. */
  public static final MonotonicityPreservingQuinticSplineInterpolator1D NOTAKNOT_QUINTIC_MONOTONE_INSTANCE =
      new MonotonicityPreservingQuinticSplineInterpolator1D(new CubicSplineInterpolator());
  /** Quintic spline with not-a-knot endpoint conditions and non-negativity filter. */
  public static final String NOTAKNOT_QUINTIC_NONNEGATIVE = "NotAKnotQuinticSplineWithNonnegativity";
  /** Instance of quintic spline with not-a-knot endpoint conditions and nonnegativity filter. */
  public static final NonnegativityPreservingQuinticSplineInterpolator1D NOTAKNOT_QUINTIC_NONNEGATIVE_INSTANCE =
      new NonnegativityPreservingQuinticSplineInterpolator1D(new CubicSplineInterpolator());

  /** Constrained cubic interpolation. */
  public static final String CONSTRAINED_CUBIC = "ConstrainedCubicSpline";
  /** Instance of constrained cubic interpolation. */
  public static final ConstrainedCubicSplineInterpolator1D CONSTRAINED_CUBIC_INSTANCE = new ConstrainedCubicSplineInterpolator1D();
  /** Constrained cubic interpolation with monotonicity filter. */
  public static final String CONSTRAINED_CUBIC_MONOTONE = "ConstrainedCubicSplineWithMonotonicity";
  /** Instance of constrained cubic interpolation with monotonicity filter. */
  public static final MonotonicityPreservingCubicSplineInterpolator1D CONSTRAINED_CUBIC_MONOTONE_INSTANCE = new MonotonicityPreservingCubicSplineInterpolator1D(
      new ConstrainedCubicSplineInterpolator());
  /** Constrained cubic interpolation with non-negativity filter. */
  public static final String CONSTRAINED_CUBIC_NONNEGATIVE = "ConstrainedCubicSplineWithNonnegativity";
  /** Instance of constrained cubic interpolation with non-negativity filter. */
  public static final NonnegativityPreservingCubicSplineInterpolator1D CONSTRAINED_CUBIC_NONNEGATIVE_INSTANCE =
      new NonnegativityPreservingCubicSplineInterpolator1D(new ConstrainedCubicSplineInterpolator());

  /** Akima cubic interpolation. */
  public static final String AKIMA_CUBIC = "AkimaCubicSpline";
  /** Instance of Akima cubic interpolation. */
  public static final SemiLocalCubicSplineInterpolator1D AKIMA_CUBIC_INSTANCE = new SemiLocalCubicSplineInterpolator1D();
  /** Akima cubic interpolation with monotonicity filter. */
  public static final String AKIMA_CUBIC_MONOTONE = "AkimaCubicSplineWithMonotonicity";
  /** Instance of Akima cubic interpolation with monotonicity filter. */
  public static final MonotonicityPreservingCubicSplineInterpolator1D AKIMA_CUBIC_MONOTONE_INSTANCE =
      new MonotonicityPreservingCubicSplineInterpolator1D(new SemiLocalCubicSplineInterpolator());
  /** Akima cubic interpolation with nonnegativity filter. */
  public static final String AKIMA_CUBIC_NONNEGATIVE = "AkimaCubicSplineWithNonnegativity";
  /** Instance of Akima cubic interpolation with nonnegativity filter. */
  public static final NonnegativityPreservingCubicSplineInterpolator1D AKIMA_CUBIC_NONNEGATIVE_INSTANCE =
      new NonnegativityPreservingCubicSplineInterpolator1D(new SemiLocalCubicSplineInterpolator());

  /** Monotone convex cubic interpolation. */
  public static final String MONOTONE_CONVEX_CUBIC = "MonotoneConvexCubicSpline";
  /** Instance of monotone convex cubic interpolation. */
  public static final MonotoneConvexSplineInterpolator1D MONOTONE_CONVEX_CUBIC_INSTANCE = new MonotoneConvexSplineInterpolator1D();

  /** C2 shape preserving cubic interpolation. */
  public static final String C2_SHAPE_PRESERVING_CUBIC = "C2ShapePreservingCubicSpline";
  /** Instance of C2 shape preserving cubic interpolation. */
  public static final ShapePreservingCubicSplineInterpolator1D C2_SHAPE_PRESERVING_CUBIC_INSTANCE = new ShapePreservingCubicSplineInterpolator1D();

  /** Log natural cubic interpolation with monotonicity filter. */
  public static final String LOG_NATURAL_CUBIC_MONOTONE = "LogNaturalCubicWithMonotonicity";
  /** Instance of log natural cubic interpolation with monotonicity filter. */
  public static final LogNaturalCubicMonotonicityPreservingInterpolator1D LOG_NATURAL_CUBIC_MONOTONE_INSTANCE =
      new LogNaturalCubicMonotonicityPreservingInterpolator1D();
  /** Map of name to instance */
  private static final Map<String, Interpolator1D> STATIC_INSTANCES;
  /** Map from class to name */
  private static final Map<Class<?>, String> INSTANCE_NAMES;

  static {
    final Map<String, Interpolator1D> staticInstances = new HashMap<>();
    final Map<Class<?>, String> instanceNames = new HashMap<>();
    instanceNames.put(LinearInterpolator1D.class, LINEAR);
    instanceNames.put(ExponentialInterpolator1D.class, EXPONENTIAL);
    instanceNames.put(LogLinearInterpolator1D.class, LOG_LINEAR);
    instanceNames.put(NaturalCubicSplineInterpolator1D.class, NATURAL_CUBIC_SPLINE);
    instanceNames.put(StepInterpolator1D.class, STEP);
    instanceNames.put(StepUpperInterpolator1D.class, STEP_UPPER);
    instanceNames.put(DoubleQuadraticInterpolator1D.class, DOUBLE_QUADRATIC);
    instanceNames.put(PCHIPInterpolator1D.class, MONOTONIC_CUBIC);
    instanceNames.put(PCHIPInterpolator1D.class, PCHIP);
    instanceNames.put(PCHIPYieldCurveInterpolator1D.class, MOD_PCHIP);
    instanceNames.put(TimeSquareInterpolator1D.class, TIME_SQUARE);
    instanceNames.put(FlatExtrapolator1D.class, FLAT_EXTRAPOLATOR);
    instanceNames.put(ExponentialExtrapolator1D.class, EXPONENTIAL_EXTRAPOLATOR);
    staticInstances.put(ISDA_INTERPOLATOR, ISDA_INTERPOLATOR_INSTANCE);
    instanceNames.put(ISDAInterpolator1D.class, ISDA_INTERPOLATOR);
    staticInstances.put(ISDA_EXTRAPOLATOR, ISDA_EXTRAPOLATOR_INSTANCE);
    instanceNames.put(ISDAExtrapolator1D.class, ISDA_EXTRAPOLATOR);

    instanceNames.put(ClampedCubicSplineInterpolator1D.class, CLAMPED_CUBIC);
    instanceNames.put(MonotonicityPreservingCubicSplineInterpolator1D.class, CLAMPED_CUBIC_MONOTONE);
    instanceNames.put(NonnegativityPreservingCubicSplineInterpolator1D.class, CLAMPED_CUBIC_NONNEGATIVE);
    instanceNames.put(MonotonicityPreservingQuinticSplineInterpolator1D.class, CLAMPED_QUINTIC_MONOTONE);
    instanceNames.put(NonnegativityPreservingQuinticSplineInterpolator1D.class, CLAMPED_QUINTIC_NONNEGATIVE);

    instanceNames.put(MonotonicityPreservingCubicSplineInterpolator1D.class, NATURAL_CUBIC_MONOTONE);
    instanceNames.put(NonnegativityPreservingCubicSplineInterpolator1D.class, NATURAL_CUBIC_NONNEGATIVE);
    instanceNames.put(MonotonicityPreservingQuinticSplineInterpolator1D.class, NATURAL_QUINTIC_MONOTONE);
    instanceNames.put(NonnegativityPreservingQuinticSplineInterpolator1D.class, NATURAL_QUINTIC_NONNEGATIVE);

    instanceNames.put(NotAKnotCubicSplineInterpolator1D.class, NOTAKNOT_CUBIC);
    instanceNames.put(MonotonicityPreservingCubicSplineInterpolator1D.class, NOTAKNOT_CUBIC_MONOTONE);
    instanceNames.put(NonnegativityPreservingCubicSplineInterpolator1D.class, NOTAKNOT_CUBIC_NONNEGATIVE);
    instanceNames.put(MonotonicityPreservingQuinticSplineInterpolator1D.class, NOTAKNOT_QUINTIC_MONOTONE);
    instanceNames.put(NonnegativityPreservingQuinticSplineInterpolator1D.class, NOTAKNOT_QUINTIC_NONNEGATIVE);

    instanceNames.put(ConstrainedCubicSplineInterpolator1D.class, CONSTRAINED_CUBIC);
    instanceNames.put(MonotonicityPreservingCubicSplineInterpolator1D.class, CONSTRAINED_CUBIC_MONOTONE);
    instanceNames.put(NonnegativityPreservingCubicSplineInterpolator1D.class, CONSTRAINED_CUBIC_NONNEGATIVE);

    instanceNames.put(SemiLocalCubicSplineInterpolator1D.class, AKIMA_CUBIC);
    instanceNames.put(MonotonicityPreservingCubicSplineInterpolator1D.class, AKIMA_CUBIC_MONOTONE);
    instanceNames.put(NonnegativityPreservingCubicSplineInterpolator1D.class, AKIMA_CUBIC_NONNEGATIVE);

    instanceNames.put(MonotoneConvexSplineInterpolator1D.class, MONOTONE_CONVEX_CUBIC);

    instanceNames.put(ShapePreservingCubicSplineInterpolator1D.class, C2_SHAPE_PRESERVING_CUBIC);

    instanceNames.put(LogNaturalCubicMonotonicityPreservingInterpolator1D.class, LOG_NATURAL_CUBIC_MONOTONE);

    STATIC_INSTANCES = new HashMap<>(staticInstances);
    INSTANCE_NAMES = new HashMap<>(instanceNames);
  }

  /**
   * Restricted constructor.
   */
  private Interpolator1DFactory() {
  }

  /**
   * Returns an instance of the interpolator or throws an exception if no interpolator of this name is found.
   * @param interpolatorName  the interpolator name
   * @return  the interpolator
   */
  public static Interpolator1D getInterpolator(final String interpolatorName) {
    try {
      final NamedInterpolator1d interpolatorFromDelegate = NamedInterpolator1dFactory.of(interpolatorName);
      if (interpolatorFromDelegate instanceof Interpolator1dAdapter) {
        return ((Interpolator1dAdapter) interpolatorFromDelegate).getUnderlyingInterpolator();
      }
    } catch (final IllegalArgumentException e) {
      // in case the interpolator was not found
      final Interpolator1D interpolator = STATIC_INSTANCES.get(interpolatorName);
      if (interpolator != null) {
        return interpolator;
      }
    }
    throw new IllegalArgumentException("Interpolator not handled: " + interpolatorName);
  }

  /**
   * Returns the name of the interpolator.
   * @param interpolator  the interpolator
   * @return  the interpolator name or null if not found
   */
  public static String getInterpolatorName(final Interpolator1D interpolator) {
    if (interpolator == null) {
      return null;
    }
    final String interpolatorName = INSTANCE_NAMES.get(interpolator.getClass());
    if (interpolator instanceof LinearExtrapolator1D) {
      return LINEAR_EXTRAPOLATOR;
    }
    if (interpolator instanceof LogLinearExtrapolator1D) {
      return LOG_LINEAR_EXTRAPOLATOR;
    }
    if (interpolator instanceof QuadraticPolynomialLeftExtrapolator) {
      return QUADRATIC_LEFT_EXTRAPOLATOR;
    }
    return interpolatorName;
  }

}
