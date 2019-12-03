/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.util.List;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.Period;
import org.threeten.bp.ZonedDateTime;

import com.google.common.collect.ImmutableList;
import com.opengamma.util.ArgumentChecker;

/**
 * Shifts a volatility surface whose X axis is time.
 */
@BeanDefinition(hierarchy = "immutable")
public final class DateDoubleSurfaceShift extends VolatilitySurfaceShiftManipulator {

  @PropertyDefinition(validate = "notNull")
  private final double[] _yValues;

  @PropertyDefinition(validate = "notNull")
  private final List<Period> _xValues;

  /* package */ DateDoubleSurfaceShift(final ScenarioShiftType shiftType,
                                       final double[] shiftValues,
                                       final List<Period> xValues,
                                       final double[] yValues) {
    super(shiftType, shiftValues);
    _xValues = ArgumentChecker.notEmpty(xValues, "xValues");
    _yValues = ArgumentChecker.notEmpty(yValues, "yValues");
  }

  @Override
  protected double[] getXValues(final ZonedDateTime valuationTime) {
    return yearFractions(_xValues, valuationTime);
  }

  @Override
  protected double[] getYValues(final ZonedDateTime valuationTime) {
    return _yValues;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code DateDoubleSurfaceShift}.
   * @return the meta-bean, not null
   */
  public static DateDoubleSurfaceShift.Meta meta() {
    return DateDoubleSurfaceShift.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(DateDoubleSurfaceShift.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static DateDoubleSurfaceShift.Builder builder() {
    return new DateDoubleSurfaceShift.Builder();
  }

  /**
   * Restricted constructor.
   * @param builder  the builder to copy from, not null
   */
  private DateDoubleSurfaceShift(DateDoubleSurfaceShift.Builder builder) {
    super(builder);
    JodaBeanUtils.notNull(builder._yValues, "yValues");
    JodaBeanUtils.notNull(builder._xValues, "xValues");
    this._yValues = builder._yValues.clone();
    this._xValues = ImmutableList.copyOf(builder._xValues);
  }

  @Override
  public DateDoubleSurfaceShift.Meta metaBean() {
    return DateDoubleSurfaceShift.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the yValues.
   * @return the value of the property, not null
   */
  public double[] getYValues() {
    return _yValues.clone();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the xValues.
   * @return the value of the property, not null
   */
  public List<Period> getXValues() {
    return _xValues;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  @Override
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DateDoubleSurfaceShift other = (DateDoubleSurfaceShift) obj;
      return JodaBeanUtils.equal(_yValues, other._yValues) &&
          JodaBeanUtils.equal(_xValues, other._xValues) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(_yValues);
    hash = hash * 31 + JodaBeanUtils.hashCode(_xValues);
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("DateDoubleSurfaceShift{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
    buf.append("yValues").append('=').append(JodaBeanUtils.toString(_yValues)).append(',').append(' ');
    buf.append("xValues").append('=').append(JodaBeanUtils.toString(_xValues)).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code DateDoubleSurfaceShift}.
   */
  public static final class Meta extends VolatilitySurfaceShiftManipulator.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code yValues} property.
     */
    private final MetaProperty<double[]> _yValues = DirectMetaProperty.ofImmutable(
        this, "yValues", DateDoubleSurfaceShift.class, double[].class);
    /**
     * The meta-property for the {@code xValues} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<Period>> _xValues = DirectMetaProperty.ofImmutable(
        this, "xValues", DateDoubleSurfaceShift.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "yValues",
        "xValues");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1726182661:  // yValues
          return _yValues;
        case 1681280954:  // xValues
          return _xValues;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public DateDoubleSurfaceShift.Builder builder() {
      return new DateDoubleSurfaceShift.Builder();
    }

    @Override
    public Class<? extends DateDoubleSurfaceShift> beanType() {
      return DateDoubleSurfaceShift.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code yValues} property.
     * @return the meta-property, not null
     */
    public MetaProperty<double[]> yValues() {
      return _yValues;
    }

    /**
     * The meta-property for the {@code xValues} property.
     * @return the meta-property, not null
     */
    public MetaProperty<List<Period>> xValues() {
      return _xValues;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1726182661:  // yValues
          return ((DateDoubleSurfaceShift) bean).getYValues();
        case 1681280954:  // xValues
          return ((DateDoubleSurfaceShift) bean).getXValues();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code DateDoubleSurfaceShift}.
   */
  public static final class Builder extends VolatilitySurfaceShiftManipulator.Builder {

    private double[] _yValues;
    private List<Period> _xValues = ImmutableList.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(DateDoubleSurfaceShift beanToCopy) {
      super(beanToCopy);
      this._yValues = beanToCopy.getYValues().clone();
      this._xValues = ImmutableList.copyOf(beanToCopy.getXValues());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1726182661:  // yValues
          return _yValues;
        case 1681280954:  // xValues
          return _xValues;
        default:
          return super.get(propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -1726182661:  // yValues
          this._yValues = (double[]) newValue;
          break;
        case 1681280954:  // xValues
          this._xValues = (List<Period>) newValue;
          break;
        default:
          super.set(propertyName, newValue);
          break;
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    /**
     * @deprecated Use Joda-Convert in application code
     */
    @Override
    @Deprecated
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    /**
     * @deprecated Use Joda-Convert in application code
     */
    @Override
    @Deprecated
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    /**
     * @deprecated Loop in application code
     */
    @Override
    @Deprecated
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public DateDoubleSurfaceShift build() {
      return new DateDoubleSurfaceShift(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the yValues.
     * @param yValues  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder yValues(double... yValues) {
      JodaBeanUtils.notNull(yValues, "yValues");
      this._yValues = yValues;
      return this;
    }

    /**
     * Sets the xValues.
     * @param xValues  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder xValues(List<Period> xValues) {
      JodaBeanUtils.notNull(xValues, "xValues");
      this._xValues = xValues;
      return this;
    }

    /**
     * Sets the {@code xValues} property in the builder
     * from an array of objects.
     * @param xValues  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder xValues(Period... xValues) {
      return xValues(ImmutableList.copyOf(xValues));
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("DateDoubleSurfaceShift.Builder{");
      buf.append("yValues").append('=').append(JodaBeanUtils.toString(_yValues)).append(',').append(' ');
      buf.append("xValues").append('=').append(JodaBeanUtils.toString(_xValues));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
