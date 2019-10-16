/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableConstructor;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.Period;

/**
 * Applies a point shift to a {@link com.opengamma.analytics.financial.model.interestrate.curve.YieldAndDiscountCurve}.
 */
@BeanDefinition
public final class YieldCurveDataPointShift implements ImmutableBean {

  /** The tenor of the shift. */
  @PropertyDefinition(validate = "notNull")
  private final Period _tenor;

  /** The shift magnitude. */
  @PropertyDefinition
  private final double _shift;

  @ImmutableConstructor
  /* package */ YieldCurveDataPointShift(final Period tenor, final double shift) {
    _tenor = tenor;
    _shift = shift;
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code YieldCurveDataPointShift}.
   * @return the meta-bean, not null
   */
  public static YieldCurveDataPointShift.Meta meta() {
    return YieldCurveDataPointShift.Meta.INSTANCE;
  }

  static {
    MetaBean.register(YieldCurveDataPointShift.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static YieldCurveDataPointShift.Builder builder() {
    return new YieldCurveDataPointShift.Builder();
  }

  @Override
  public YieldCurveDataPointShift.Meta metaBean() {
    return YieldCurveDataPointShift.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the tenor of the shift.
   * @return the value of the property, not null
   */
  public Period getTenor() {
    return _tenor;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the shift magnitude.
   * @return the value of the property
   */
  public double getShift() {
    return _shift;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      YieldCurveDataPointShift other = (YieldCurveDataPointShift) obj;
      return JodaBeanUtils.equal(_tenor, other._tenor) &&
          JodaBeanUtils.equal(_shift, other._shift);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_tenor);
    hash = hash * 31 + JodaBeanUtils.hashCode(_shift);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("YieldCurveDataPointShift{");
    buf.append("tenor").append('=').append(_tenor).append(',').append(' ');
    buf.append("shift").append('=').append(JodaBeanUtils.toString(_shift));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code YieldCurveDataPointShift}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code tenor} property.
     */
    private final MetaProperty<Period> _tenor = DirectMetaProperty.ofImmutable(
        this, "tenor", YieldCurveDataPointShift.class, Period.class);
    /**
     * The meta-property for the {@code shift} property.
     */
    private final MetaProperty<Double> _shift = DirectMetaProperty.ofImmutable(
        this, "shift", YieldCurveDataPointShift.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "tenor",
        "shift");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 110246592:  // tenor
          return _tenor;
        case 109407362:  // shift
          return _shift;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public YieldCurveDataPointShift.Builder builder() {
      return new YieldCurveDataPointShift.Builder();
    }

    @Override
    public Class<? extends YieldCurveDataPointShift> beanType() {
      return YieldCurveDataPointShift.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code tenor} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Period> tenor() {
      return _tenor;
    }

    /**
     * The meta-property for the {@code shift} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> shift() {
      return _shift;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 110246592:  // tenor
          return ((YieldCurveDataPointShift) bean).getTenor();
        case 109407362:  // shift
          return ((YieldCurveDataPointShift) bean).getShift();
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
   * The bean-builder for {@code YieldCurveDataPointShift}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<YieldCurveDataPointShift> {

    private Period _tenor;
    private double _shift;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(YieldCurveDataPointShift beanToCopy) {
      this._tenor = beanToCopy.getTenor();
      this._shift = beanToCopy.getShift();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 110246592:  // tenor
          return _tenor;
        case 109407362:  // shift
          return _shift;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 110246592:  // tenor
          this._tenor = (Period) newValue;
          break;
        case 109407362:  // shift
          this._shift = (Double) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public YieldCurveDataPointShift build() {
      return new YieldCurveDataPointShift(
          _tenor,
          _shift);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the tenor of the shift.
     * @param tenor  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder tenor(Period tenor) {
      JodaBeanUtils.notNull(tenor, "tenor");
      this._tenor = tenor;
      return this;
    }

    /**
     * Sets the shift magnitude.
     * @param shift  the new value
     * @return this, for chaining, not null
     */
    public Builder shift(double shift) {
      this._shift = shift;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("YieldCurveDataPointShift.Builder{");
      buf.append("tenor").append('=').append(JodaBeanUtils.toString(_tenor)).append(',').append(' ');
      buf.append("shift").append('=').append(JodaBeanUtils.toString(_shift));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
