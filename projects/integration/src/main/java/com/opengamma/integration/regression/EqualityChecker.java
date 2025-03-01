/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.regression;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.joda.beans.Bean;
import org.joda.beans.MetaProperty;

import com.google.common.collect.Maps;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.analytics.math.curve.Curve;
import com.opengamma.analytics.util.serialization.InvokedSerializedForm;
import com.opengamma.core.marketdatasnapshot.VolatilitySurfaceData;
import com.opengamma.financial.analytics.LabelledMatrix1D;
import com.opengamma.util.ClassMap;
import com.opengamma.util.fudgemsg.WriteReplaceHelper;
import com.opengamma.util.money.CurrencyAmount;
import com.opengamma.util.money.MultipleCurrencyAmount;

/**
 * Checks whether values are close enough to equality to satisfy the regression test.
 */
public final class EqualityChecker {

  // TODO static method to populate these from the outside
  private static final Map<Class<?>, TypeHandler<?>> HANDLERS = new ClassMap<>();
  private static final Map<MetaProperty<?>, Comparator<?>> PROPERTY_COMPARATORS = Maps.newHashMap();
  private static final ObjectArrayHandler OBJECT_ARRAY_HANDLER = new ObjectArrayHandler();

  static {
    HANDLERS.put(Double.class, new DoubleHandler());
    HANDLERS.put(double[].class, new PrimitiveDoubleArrayHandler());
    HANDLERS.put(Double[].class, new DoubleArrayHandler());
    HANDLERS.put(Object[].class, OBJECT_ARRAY_HANDLER);
    HANDLERS.put(List.class, new ListHandler());
    HANDLERS.put(YieldCurve.class, new YieldCurveHandler());
    HANDLERS.put(LabelledMatrix1D.class, new LabelledMatrix1DHandler());
    HANDLERS.put(MultipleCurrencyAmount.class, new MultipleCurrencyAmountHandler());
    HANDLERS.put(Bean.class, new BeanHandler());
    HANDLERS.put(InvokedSerializedForm.class, new InvokedSerializedFormHandler());
    HANDLERS.put(VolatilitySurfaceData.class, new VolatilitySurfaceDataHandler());
    HANDLERS.put(Map.class, new MapHandler());

    PROPERTY_COMPARATORS.put(Curve.meta().name(), new AlwaysEqualComparator());
  }

  private EqualityChecker() {
  }

  /**
   * Checks whether two values are close enough to equality to satisfy the regression test.
   * @param o1 A value
   * @param o2 Another value
   * @param delta The maximum allowable difference between two double values
   * @return true If the values are close enough to be considered equal
   */
  public static boolean equals(final Object o1, final Object o2, final double delta) {
    if (o1 == null && o2 == null) {
      return true;
    }
    if (o1 == null || o2 == null) {
      return false;
    }
    if (!o1.getClass().equals(o2.getClass())) {
      return false;
    }
    // for normal classes this method returns the object itself. for instances of anonymous inner classes produced
    // by a factory method it returns an instance of InvokedSerializedForm which encodes the factory method and
    // arguments needed to recreate the object. comparing anonymous classes is obviously fraught with difficulties,
    // but comparing the serialized form will work
    final Object value1 = WriteReplaceHelper.writeReplace(o1);
    final Object value2 = WriteReplaceHelper.writeReplace(o2);
    @SuppressWarnings("unchecked")
    final
    TypeHandler<Object> handler = (TypeHandler<Object>) HANDLERS.get(value1.getClass());
    if (handler != null) {
      return handler.equals(value1, value2, delta);
    }
    // ClassMap doesn't handle subtyping and arrays, this uses the Object[]
    // handler for non-primitive arrays
    if (value1.getClass().isArray() && Object[].class.isAssignableFrom(value1.getClass())) {
      return OBJECT_ARRAY_HANDLER.equals((Object[]) value1, (Object[]) value2, delta);
    }
    return Objects.equals(value1, value2);
  }

  /**
   * Handles equality checking for a specific type.
   * @param <T> The type
   */
  public interface TypeHandler<T> {

    /**
     * Returns true if the values are close enough to equality to satisfy the regression test.
     * @param value1 A value
     * @param value2 Another value
     * @param delta The maximum allowable difference between two double values
     * @return true If the values are close enough to be considered equal
     */
    boolean equals(T value1, T value2, double delta);
  }

  private static final class YieldCurveHandler implements TypeHandler<YieldCurve> {

    @Override
    public boolean equals(final YieldCurve value1, final YieldCurve value2, final double delta) {
      return EqualityChecker.equals(value1.getCurve(), value2.getCurve(), delta);
    }
  }

  private static final class DoubleArrayHandler implements TypeHandler<Double[]> {

    @Override
    public boolean equals(final Double[] value1, final Double[] value2, final double delta) {
      if (value1.length != value2.length) {
        return false;
      }
      for (int i = 0; i < value1.length; i++) {
        final double item1 = value1[i];
        final double item2 = value2[i];
        if (Math.abs(item1 - item2) > delta) {
          return false;
        }
      }
      return true;
    }
  }

  private static final class MultipleCurrencyAmountHandler implements TypeHandler<MultipleCurrencyAmount> {

    @Override
    public boolean equals(final MultipleCurrencyAmount value1, final MultipleCurrencyAmount value2, final double delta) {
      for (final CurrencyAmount currencyAmount : value1) {
        final double amount1 = currencyAmount.getAmount();
        double amount2;
        try {
          amount2 = value2.getAmount(currencyAmount.getCurrency());
        } catch (final IllegalArgumentException e) {
          return false;
        }
        if (!EqualityChecker.equals(amount1, amount2, delta)) {
          return false;
        }
      }
      return true;
    }
  }

  private static final class ObjectArrayHandler implements TypeHandler<Object[]> {

    @Override
    public boolean equals(final Object[] value1, final Object[] value2, final double delta) {
      if (value1.length != value2.length) {
        return false;
      }
      for (int i = 0; i < value1.length; i++) {
        final Object item1 = value1[i];
        final Object item2 = value2[i];
        if (!EqualityChecker.equals(item1, item2, delta)) {
          return false;
        }
      }
      return true;
    }
  }

  private static final class PrimitiveDoubleArrayHandler implements TypeHandler<double[]> {

    @Override
    public boolean equals(final double[] value1, final double[] value2, final double delta) {
      if (value1.length != value2.length) {
        return false;
      }
      for (int i = 0; i < value1.length; i++) {
        final double item1 = value1[i];
        final double item2 = value2[i];
        if (Math.abs(item1 - item2) > delta) {
          return false;
        }
      }
      return true;
    }
  }

  private static final class DoubleHandler implements TypeHandler<Double> {

    @Override
    public boolean equals(final Double value1, final Double value2, final double delta) {
      return Math.abs(value1 - value2) <= delta;
    }
  }

  private static final class ListHandler implements TypeHandler<List<?>> {

    @Override
    public boolean equals(final List<?> value1, final List<?> value2, final double delta) {
      if (value1.size() != value2.size()) {
        return false;
      }
      for (Iterator<?> it1 = value1.iterator(), it2 = value2.iterator(); it1.hasNext();) {
        final Object item1 = it1.next();
        final Object item2 = it2.next();
        if (!EqualityChecker.equals(item1, item2, delta)) {
          return false;
        }
      }
      return true;
    }
  }

  private static class BeanHandler implements TypeHandler<Bean> {

    @Override
    public boolean equals(final Bean bean1, final Bean bean2, final double delta) {
      for (final MetaProperty<?> property : bean1.metaBean().metaPropertyIterable()) {
        final Object value1 = property.get(bean1);
        final Object value2 = property.get(bean2);
        @SuppressWarnings("unchecked")
        final
        Comparator<Object> comparator = (Comparator<Object>) PROPERTY_COMPARATORS.get(property);
        if (comparator == null) {
          if (!EqualityChecker.equals(value1, value2, delta)) {
            return false;
          }
        } else {
          if (value1 == null && value2 == null) {
            continue;
          }
          if (value1 == null || value2 == null) {
            return false;
          }
          if (!value1.getClass().equals(value2.getClass())) {
            return false;
          }
          if (comparator.compare(value1, value2) != 0) {
            return false;
          }
        }
      }
      return true;
    }
  }

  private static class InvokedSerializedFormHandler implements TypeHandler<InvokedSerializedForm> {

    @Override
    public boolean equals(final InvokedSerializedForm value1, final InvokedSerializedForm value2, final double delta) {
      if (!Objects.equals(value1.getOuterClass(), value2.getOuterClass())) {
        return false;
      }
      if (!value1.getMethod().equals(value2.getMethod())) {
        return false;
      }
      if (!EqualityChecker.equals(value1.getOuterInstance(), value2.getOuterInstance(), delta)) {
        return false;
      }
      if (!EqualityChecker.equals(value1.getParameters(), value2.getParameters(), delta)) {
        return false;
      }
      return true;
    }
  }

  private static class VolatilitySurfaceDataHandler implements TypeHandler<VolatilitySurfaceData<?, ?>> {

    @Override
    public boolean equals(final VolatilitySurfaceData<?, ?> value1, final VolatilitySurfaceData<?, ?> value2, final double delta) {
      if (!Objects.equals(value1.getDefinitionName(), value2.getDefinitionName())) {
        return false;
      }
      if (!Objects.equals(value1.getSpecificationName(), value2.getSpecificationName())) {
        return false;
      }
      if (!Objects.equals(value1.getTarget(), value2.getTarget())) {
        return false;
      }
      if (!Objects.equals(value1.getXLabel(), value2.getXLabel())) {
        return false;
      }
      if (!Objects.equals(value1.getYLabel(), value2.getYLabel())) {
        return false;
      }
      if (!EqualityChecker.equals(value1.asMap(), value2.asMap(), delta)) {
        return false;
      }
      return true;
    }
  }

  private static class MapHandler implements TypeHandler<Map<?, ?>> {

    @Override
    public boolean equals(final Map<?, ?> map1, final Map<?, ?> map2, final double delta) {
      if (!map1.keySet().equals(map2.keySet())) {
        return false;
      }
      for (final Map.Entry<?, ?> entry : map1.entrySet()) {
        final Object value1 = entry.getValue();
        final Object value2 = map2.get(entry.getKey());
        if (!EqualityChecker.equals(value1, value2, delta)) {
          return false;
        }
      }
      return true;
    }
  }

  private static class LabelledMatrix1DHandler implements TypeHandler<LabelledMatrix1D<?, ?>> {

    @Override
    public boolean equals(final LabelledMatrix1D<?, ?> value1, final LabelledMatrix1D<?, ?> value2, final double delta) {
      if (!Objects.equals(value1.getLabelsTitle(), value2.getLabelsTitle())) {
        return false;
      }
      if (!Objects.equals(value1.getValuesTitle(), value2.getValuesTitle())) {
        return false;
      }
      if (!EqualityChecker.equals(value1.getKeys(), value2.getKeys(), delta)) {
        return false;
      }
      if (!EqualityChecker.equals(value1.getLabels(), value2.getLabels(), delta)) {
        return false;
      }
      if (!EqualityChecker.equals(value1.getValues(), value2.getValues(), delta)) {
        return false;
      }
      if (!EqualityChecker.equals(value1.getDefaultTolerance(), value2.getDefaultTolerance(), delta)) {
        return false;
      }
      return true;
    }
  }

  private static class AlwaysEqualComparator implements Comparator<Object> {

    @Override
    public int compare(final Object o1, final Object o2) {
      return 0;
    }
  }
}
