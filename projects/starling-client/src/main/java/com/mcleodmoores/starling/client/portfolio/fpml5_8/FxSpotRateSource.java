/**
 * Copyright (C) 2016 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.starling.client.portfolio.fpml5_8;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * An object containing information to source FX spot rates containing the data source, the fixing time and the business centre time zone.
 */
@BeanDefinition
public class FxSpotRateSource implements ImmutableBean {

  /**
   * The rate source.
   */
  @PropertyDefinition(validate = "notNull")
  private final PrimaryRateSource _primaryRateSource;

  /**
   * The fixing time.
   */
  @PropertyDefinition(validate = "notNull")
  private final LocalTime _fixingTime;

  /**
   * The fixing business center time zone.
   */
  @PropertyDefinition(validate = "notNull")
  private final ZoneId _businessCenterZone;

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FxSpotRateSource}.
   * @return the meta-bean, not null
   */
  public static FxSpotRateSource.Meta meta() {
    return FxSpotRateSource.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(FxSpotRateSource.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static FxSpotRateSource.Builder builder() {
    return new FxSpotRateSource.Builder();
  }

  /**
   * Restricted constructor.
   * @param builder  the builder to copy from, not null
   */
  protected FxSpotRateSource(FxSpotRateSource.Builder builder) {
    JodaBeanUtils.notNull(builder._primaryRateSource, "primaryRateSource");
    JodaBeanUtils.notNull(builder._fixingTime, "fixingTime");
    JodaBeanUtils.notNull(builder._businessCenterZone, "businessCenterZone");
    this._primaryRateSource = builder._primaryRateSource;
    this._fixingTime = builder._fixingTime;
    this._businessCenterZone = builder._businessCenterZone;
  }

  @Override
  public FxSpotRateSource.Meta metaBean() {
    return FxSpotRateSource.Meta.INSTANCE;
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
   * Gets the rate source.
   * @return the value of the property, not null
   */
  public PrimaryRateSource getPrimaryRateSource() {
    return _primaryRateSource;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the fixing time.
   * @return the value of the property, not null
   */
  public LocalTime getFixingTime() {
    return _fixingTime;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the fixing business center time zone.
   * @return the value of the property, not null
   */
  public ZoneId getBusinessCenterZone() {
    return _businessCenterZone;
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
      FxSpotRateSource other = (FxSpotRateSource) obj;
      return JodaBeanUtils.equal(getPrimaryRateSource(), other.getPrimaryRateSource()) &&
          JodaBeanUtils.equal(getFixingTime(), other.getFixingTime()) &&
          JodaBeanUtils.equal(getBusinessCenterZone(), other.getBusinessCenterZone());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getPrimaryRateSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFixingTime());
    hash = hash * 31 + JodaBeanUtils.hashCode(getBusinessCenterZone());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("FxSpotRateSource{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("primaryRateSource").append('=').append(JodaBeanUtils.toString(getPrimaryRateSource())).append(',').append(' ');
    buf.append("fixingTime").append('=').append(JodaBeanUtils.toString(getFixingTime())).append(',').append(' ');
    buf.append("businessCenterZone").append('=').append(JodaBeanUtils.toString(getBusinessCenterZone())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FxSpotRateSource}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code primaryRateSource} property.
     */
    private final MetaProperty<PrimaryRateSource> _primaryRateSource = DirectMetaProperty.ofImmutable(
        this, "primaryRateSource", FxSpotRateSource.class, PrimaryRateSource.class);
    /**
     * The meta-property for the {@code fixingTime} property.
     */
    private final MetaProperty<LocalTime> _fixingTime = DirectMetaProperty.ofImmutable(
        this, "fixingTime", FxSpotRateSource.class, LocalTime.class);
    /**
     * The meta-property for the {@code businessCenterZone} property.
     */
    private final MetaProperty<ZoneId> _businessCenterZone = DirectMetaProperty.ofImmutable(
        this, "businessCenterZone", FxSpotRateSource.class, ZoneId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "primaryRateSource",
        "fixingTime",
        "businessCenterZone");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -489404227:  // primaryRateSource
          return _primaryRateSource;
        case 1255686170:  // fixingTime
          return _fixingTime;
        case -2019615359:  // businessCenterZone
          return _businessCenterZone;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public FxSpotRateSource.Builder builder() {
      return new FxSpotRateSource.Builder();
    }

    @Override
    public Class<? extends FxSpotRateSource> beanType() {
      return FxSpotRateSource.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code primaryRateSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PrimaryRateSource> primaryRateSource() {
      return _primaryRateSource;
    }

    /**
     * The meta-property for the {@code fixingTime} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalTime> fixingTime() {
      return _fixingTime;
    }

    /**
     * The meta-property for the {@code businessCenterZone} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZoneId> businessCenterZone() {
      return _businessCenterZone;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -489404227:  // primaryRateSource
          return ((FxSpotRateSource) bean).getPrimaryRateSource();
        case 1255686170:  // fixingTime
          return ((FxSpotRateSource) bean).getFixingTime();
        case -2019615359:  // businessCenterZone
          return ((FxSpotRateSource) bean).getBusinessCenterZone();
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
   * The bean-builder for {@code FxSpotRateSource}.
   */
  public static class Builder extends DirectFieldsBeanBuilder<FxSpotRateSource> {

    private PrimaryRateSource _primaryRateSource;
    private LocalTime _fixingTime;
    private ZoneId _businessCenterZone;

    /**
     * Restricted constructor.
     */
    protected Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    protected Builder(FxSpotRateSource beanToCopy) {
      this._primaryRateSource = beanToCopy.getPrimaryRateSource();
      this._fixingTime = beanToCopy.getFixingTime();
      this._businessCenterZone = beanToCopy.getBusinessCenterZone();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -489404227:  // primaryRateSource
          return _primaryRateSource;
        case 1255686170:  // fixingTime
          return _fixingTime;
        case -2019615359:  // businessCenterZone
          return _businessCenterZone;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -489404227:  // primaryRateSource
          this._primaryRateSource = (PrimaryRateSource) newValue;
          break;
        case 1255686170:  // fixingTime
          this._fixingTime = (LocalTime) newValue;
          break;
        case -2019615359:  // businessCenterZone
          this._businessCenterZone = (ZoneId) newValue;
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
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public FxSpotRateSource build() {
      return new FxSpotRateSource(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the rate source.
     * @param primaryRateSource  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder primaryRateSource(PrimaryRateSource primaryRateSource) {
      JodaBeanUtils.notNull(primaryRateSource, "primaryRateSource");
      this._primaryRateSource = primaryRateSource;
      return this;
    }

    /**
     * Sets the fixing time.
     * @param fixingTime  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder fixingTime(LocalTime fixingTime) {
      JodaBeanUtils.notNull(fixingTime, "fixingTime");
      this._fixingTime = fixingTime;
      return this;
    }

    /**
     * Sets the fixing business center time zone.
     * @param businessCenterZone  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder businessCenterZone(ZoneId businessCenterZone) {
      JodaBeanUtils.notNull(businessCenterZone, "businessCenterZone");
      this._businessCenterZone = businessCenterZone;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("FxSpotRateSource.Builder{");
      int len = buf.length();
      toString(buf);
      if (buf.length() > len) {
        buf.setLength(buf.length() - 2);
      }
      buf.append('}');
      return buf.toString();
    }

    protected void toString(StringBuilder buf) {
      buf.append("primaryRateSource").append('=').append(JodaBeanUtils.toString(_primaryRateSource)).append(',').append(' ');
      buf.append("fixingTime").append('=').append(JodaBeanUtils.toString(_fixingTime)).append(',').append(' ');
      buf.append("businessCenterZone").append('=').append(JodaBeanUtils.toString(_businessCenterZone)).append(',').append(' ');
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
