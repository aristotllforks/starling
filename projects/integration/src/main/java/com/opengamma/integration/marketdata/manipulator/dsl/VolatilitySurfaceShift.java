/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.ArgumentChecker;

/**
 * Defines the shift applied to a single point in a volatility surface.
 */
@BeanDefinition
public final class VolatilitySurfaceShift implements ImmutableBean {

  @PropertyDefinition(validate = "notNull")
  private final Object _x;

  @PropertyDefinition(validate = "notNull")
  private final Object _y;

  @PropertyDefinition(validate = "notNull")
  private final Number _shift;

  /**
   * @param x
   * @param y
   * @param shift
   */
  @ImmutableConstructor
  /* package */ VolatilitySurfaceShift(final Object x, final Object y, final Number shift) {
    ArgumentChecker.notNull(x, "x");
    ArgumentChecker.notNull(y, "y");
    ArgumentChecker.notNull(shift, "shift");

    // TODO explain the calls to doubleValue
    if (x instanceof Number) {
      _x = ((Number) x).doubleValue();
    } else {
      _x = x;
    }
    if (y instanceof Number) {
      _y = ((Number) y).doubleValue();
    } else {
      _y = y;
    }
    _shift = shift.doubleValue();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code VolatilitySurfaceShift}.
   * @return the meta-bean, not null
   */
  public static VolatilitySurfaceShift.Meta meta() {
    return VolatilitySurfaceShift.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(VolatilitySurfaceShift.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static VolatilitySurfaceShift.Builder builder() {
    return new VolatilitySurfaceShift.Builder();
  }

  @Override
  public VolatilitySurfaceShift.Meta metaBean() {
    return VolatilitySurfaceShift.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the x.
   * @return the value of the property, not null
   */
  public Object getX() {
    return _x;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the y.
   * @return the value of the property, not null
   */
  public Object getY() {
    return _y;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the shift.
   * @return the value of the property, not null
   */
  public Number getShift() {
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
      VolatilitySurfaceShift other = (VolatilitySurfaceShift) obj;
      return JodaBeanUtils.equal(_x, other._x) &&
          JodaBeanUtils.equal(_y, other._y) &&
          JodaBeanUtils.equal(_shift, other._shift);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_x);
    hash = hash * 31 + JodaBeanUtils.hashCode(_y);
    hash = hash * 31 + JodaBeanUtils.hashCode(_shift);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("VolatilitySurfaceShift{");
    buf.append("x").append('=').append(_x).append(',').append(' ');
    buf.append("y").append('=').append(_y).append(',').append(' ');
    buf.append("shift").append('=').append(JodaBeanUtils.toString(_shift));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code VolatilitySurfaceShift}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code x} property.
     */
    private final MetaProperty<Object> _x = DirectMetaProperty.ofImmutable(
        this, "x", VolatilitySurfaceShift.class, Object.class);
    /**
     * The meta-property for the {@code y} property.
     */
    private final MetaProperty<Object> _y = DirectMetaProperty.ofImmutable(
        this, "y", VolatilitySurfaceShift.class, Object.class);
    /**
     * The meta-property for the {@code shift} property.
     */
    private final MetaProperty<Number> _shift = DirectMetaProperty.ofImmutable(
        this, "shift", VolatilitySurfaceShift.class, Number.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "x",
        "y",
        "shift");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 120:  // x
          return _x;
        case 121:  // y
          return _y;
        case 109407362:  // shift
          return _shift;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public VolatilitySurfaceShift.Builder builder() {
      return new VolatilitySurfaceShift.Builder();
    }

    @Override
    public Class<? extends VolatilitySurfaceShift> beanType() {
      return VolatilitySurfaceShift.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code x} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Object> x() {
      return _x;
    }

    /**
     * The meta-property for the {@code y} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Object> y() {
      return _y;
    }

    /**
     * The meta-property for the {@code shift} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Number> shift() {
      return _shift;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 120:  // x
          return ((VolatilitySurfaceShift) bean).getX();
        case 121:  // y
          return ((VolatilitySurfaceShift) bean).getY();
        case 109407362:  // shift
          return ((VolatilitySurfaceShift) bean).getShift();
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
   * The bean-builder for {@code VolatilitySurfaceShift}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<VolatilitySurfaceShift> {

    private Object _x;
    private Object _y;
    private Number _shift;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(VolatilitySurfaceShift beanToCopy) {
      this._x = beanToCopy.getX();
      this._y = beanToCopy.getY();
      this._shift = beanToCopy.getShift();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 120:  // x
          return _x;
        case 121:  // y
          return _y;
        case 109407362:  // shift
          return _shift;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 120:  // x
          this._x = (Object) newValue;
          break;
        case 121:  // y
          this._y = (Object) newValue;
          break;
        case 109407362:  // shift
          this._shift = (Number) newValue;
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
    public VolatilitySurfaceShift build() {
      return new VolatilitySurfaceShift(
          _x,
          _y,
          _shift);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the x.
     * @param x  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder x(Object x) {
      JodaBeanUtils.notNull(x, "x");
      this._x = x;
      return this;
    }

    /**
     * Sets the y.
     * @param y  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder y(Object y) {
      JodaBeanUtils.notNull(y, "y");
      this._y = y;
      return this;
    }

    /**
     * Sets the shift.
     * @param shift  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder shift(Number shift) {
      JodaBeanUtils.notNull(shift, "shift");
      this._shift = shift;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("VolatilitySurfaceShift.Builder{");
      buf.append("x").append('=').append(JodaBeanUtils.toString(_x)).append(',').append(' ');
      buf.append("y").append('=').append(JodaBeanUtils.toString(_y)).append(',').append(' ');
      buf.append("shift").append('=').append(JodaBeanUtils.toString(_shift));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
