/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.math.curve;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;

import com.opengamma.math.function.Function;

/**
 * Class defining a spread curve. The spread curve is in the same hierarchy as the other curves, so a spread curve could be defined on
 * another spread curve.
 */
public class SpreadDoublesCurve extends Curve<Double, Double> {
  private final CurveSpreadFunction _spreadFunction;
  private final Function<Double, Double> _f;
  private final Curve<Double, Double>[] _curves;

  /**
   * Takes an array of curves that are to be operated on by the spread function and the spread function. The name of the spread
   * curve is automatically-generated
   * @param curves The curves 
   * @param spreadFunction The spread function
   * @return The spread curve
   */
  public static SpreadDoublesCurve from(final Curve<Double, Double>[] curves, final CurveSpreadFunction spreadFunction) {
    return new SpreadDoublesCurve(curves, spreadFunction);
  }

  /**
   * Takes an array of curves that are to be operated on by the spread function and the spread function.
   * @param curves The curves 
   * @param spreadFunction The spread function
   * @param name The name of the curve
   * @return The spread curve
   */
  public static SpreadDoublesCurve from(final Curve<Double, Double>[] curves, final CurveSpreadFunction spreadFunction, final String name) {
    return new SpreadDoublesCurve(curves, spreadFunction, name);
  }

  /**
   * The name of the curve is generated automatically.
   * @param curves The curves
   * @param spreadFunction The spread function
   */
  public SpreadDoublesCurve(final Curve<Double, Double>[] curves, final CurveSpreadFunction spreadFunction) {
    super();
    Validate.notNull(curves, "curves");
    Validate.isTrue(curves.length > 1, "curves");
    Validate.notNull(spreadFunction, "spread operator");
    _curves = curves;
    _spreadFunction = spreadFunction;
    _f = spreadFunction.evaluate(curves);
  }

  /**
   * 
   * @param curves The curves
   * @param spreadFunction The spread function
   * @param name The name of the curve
   */
  public SpreadDoublesCurve(final Curve<Double, Double>[] curves, final CurveSpreadFunction spreadFunction, final String name) {
    super(name);
    Validate.notNull(curves, "curves");
    Validate.isTrue(curves.length > 1, "curves");
    Validate.notNull(spreadFunction, "spread operator");
    _curves = curves;
    _spreadFunction = spreadFunction;
    _f = spreadFunction.evaluate(curves);
  }

  /**
   * Returns a set of the unique names of the curves that were used to construct this curve. If a constituent curve is a spread curve,
   * then all of its underlyings are included.
   * @return The set of underlying names
   */
  public Set<String> getUnderlyingNames() {
    final Set<String> result = new HashSet<String>();
    for (final Curve<Double, Double> curve : _curves) {
      if (curve instanceof SpreadDoublesCurve) {
        result.addAll(((SpreadDoublesCurve) curve).getUnderlyingNames());
      } else {
        result.add(curve.getName());
      }
    }
    return result;
  }

  /**
   * Returns a string that represents the mathematical form of this curve
   * @return The long name of this curve
   */
  public String getLongName() {
    final StringBuffer sb = new StringBuffer(getName());
    sb.append("=");
    int i = 0;
    sb.append("(");
    for (final Curve<Double, Double> curve : _curves) {
      if (curve instanceof SpreadDoublesCurve) {
        sb.append(((SpreadDoublesCurve) curve).getLongName().substring(2));
      } else {
        sb.append(curve.getName());
      }
      if (i != _curves.length - 1) {
        sb.append(_spreadFunction.getOperationName());
      }
      i++;
    }
    sb.append(")");
    return sb.toString();
  }

  /**
   * 
   * @return The underlying curves
   */
  public Curve<Double, Double>[] getUnderlyingCurves() {
    return _curves;
  }

  /**
   * @return Not supported
   * @throws UnsupportedOperationException
   */
  @Override
  public Double[] getXData() {
    throw new UnsupportedOperationException();
  }

  /**
   * @return Not supported
   * @throws UnsupportedOperationException
   */
  @Override
  public Double[] getYData() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Double getYValue(final Double x) {
    Validate.notNull(x, "x");
    return _f.evaluate(x);
  }

  /**
   * @return Not supported
   * @throws UnsupportedOperationException
   */
  @Override
  public int size() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.hashCode(_curves);
    result = prime * result + _spreadFunction.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SpreadDoublesCurve other = (SpreadDoublesCurve) obj;
    if (!Arrays.equals(_curves, other._curves)) {
      return false;
    }
    return ObjectUtils.equals(_spreadFunction, other._spreadFunction);
  }

}
