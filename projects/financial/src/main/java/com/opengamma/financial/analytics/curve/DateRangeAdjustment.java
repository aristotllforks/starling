/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.curve;

import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableMap;
import com.opengamma.core.config.Config;
import com.opengamma.core.config.ConfigGroups;
import com.opengamma.util.time.LocalDateRange;

/**
 * Config item to store adjustments to apply over a particular date range, e.g. year-end.
 */
@Config(description = "Date range adjustment", group = ConfigGroups.CURVES)
@BeanDefinition
public class DateRangeAdjustment implements ImmutableBean {

  @PropertyDefinition(validate = "notNull")
  private final Map<LocalDateRange, Double> _adjustments;

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code DateRangeAdjustment}.
   * @return the meta-bean, not null
   */
  public static DateRangeAdjustment.Meta meta() {
    return DateRangeAdjustment.Meta.INSTANCE;
  }

  static {
    MetaBean.register(DateRangeAdjustment.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static DateRangeAdjustment.Builder builder() {
    return new DateRangeAdjustment.Builder();
  }

  /**
   * Restricted constructor.
   * @param builder  the builder to copy from, not null
   */
  protected DateRangeAdjustment(DateRangeAdjustment.Builder builder) {
    JodaBeanUtils.notNull(builder._adjustments, "adjustments");
    this._adjustments = ImmutableMap.copyOf(builder._adjustments);
  }

  @Override
  public DateRangeAdjustment.Meta metaBean() {
    return DateRangeAdjustment.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the adjustments.
   * @return the value of the property, not null
   */
  public Map<LocalDateRange, Double> getAdjustments() {
    return _adjustments;
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
      DateRangeAdjustment other = (DateRangeAdjustment) obj;
      return JodaBeanUtils.equal(_adjustments, other._adjustments);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_adjustments);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("DateRangeAdjustment{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("adjustments").append('=').append(JodaBeanUtils.toString(_adjustments)).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code DateRangeAdjustment}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code adjustments} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<LocalDateRange, Double>> _adjustments = DirectMetaProperty.ofImmutable(
        this, "adjustments", DateRangeAdjustment.class, (Class) Map.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "adjustments");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1160102054:  // adjustments
          return _adjustments;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public DateRangeAdjustment.Builder builder() {
      return new DateRangeAdjustment.Builder();
    }

    @Override
    public Class<? extends DateRangeAdjustment> beanType() {
      return DateRangeAdjustment.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code adjustments} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Map<LocalDateRange, Double>> adjustments() {
      return _adjustments;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1160102054:  // adjustments
          return ((DateRangeAdjustment) bean).getAdjustments();
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
   * The bean-builder for {@code DateRangeAdjustment}.
   */
  public static class Builder extends DirectFieldsBeanBuilder<DateRangeAdjustment> {

    private Map<LocalDateRange, Double> _adjustments = ImmutableMap.of();

    /**
     * Restricted constructor.
     */
    protected Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    protected Builder(DateRangeAdjustment beanToCopy) {
      this._adjustments = ImmutableMap.copyOf(beanToCopy.getAdjustments());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1160102054:  // adjustments
          return _adjustments;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 1160102054:  // adjustments
          this._adjustments = (Map<LocalDateRange, Double>) newValue;
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
    public DateRangeAdjustment build() {
      return new DateRangeAdjustment(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the adjustments.
     * @param adjustments  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder adjustments(Map<LocalDateRange, Double> adjustments) {
      JodaBeanUtils.notNull(adjustments, "adjustments");
      this._adjustments = adjustments;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("DateRangeAdjustment.Builder{");
      int len = buf.length();
      toString(buf);
      if (buf.length() > len) {
        buf.setLength(buf.length() - 2);
      }
      buf.append('}');
      return buf.toString();
    }

    protected void toString(StringBuilder buf) {
      buf.append("adjustments").append('=').append(JodaBeanUtils.toString(_adjustments)).append(',').append(' ');
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
