/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.marketdata.spec;

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

import com.opengamma.engine.marketdata.historical.HistoricalShockMarketDataSnapshot;

/**
 * Specification for market data derived from 3 underlying providers. The values are derived by finding the difference between values in the first two providers
 * and applying it to the value from the third provider. The change applied to the base value can be the proportional or absolute difference between the two
 * other values.
 */
@BeanDefinition
public final class HistoricalShockMarketDataSpecification implements ImmutableBean, MarketDataSpecification {

  private static final long serialVersionUID = 1L;

  @PropertyDefinition(validate = "notNull")
  private final HistoricalShockMarketDataSnapshot.ShockType _shockType;

  @PropertyDefinition(validate = "notNull")
  private final MarketDataSpecification _historicalSpecification1;

  @PropertyDefinition(validate = "notNull")
  private final MarketDataSpecification _historicalSpecification2;

  @PropertyDefinition(validate = "notNull")
  private final MarketDataSpecification _baseSpecification;

  public static HistoricalShockMarketDataSpecification of(final HistoricalShockMarketDataSnapshot.ShockType shockType,
      final MarketDataSpecification historicalSpec1,
      final MarketDataSpecification historicalSpec2,
      final MarketDataSpecification baseSpec) {
    return new HistoricalShockMarketDataSpecification(shockType, historicalSpec1, historicalSpec2, baseSpec);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code HistoricalShockMarketDataSpecification}.
   * @return the meta-bean, not null
   */
  public static HistoricalShockMarketDataSpecification.Meta meta() {
    return HistoricalShockMarketDataSpecification.Meta.INSTANCE;
  }

  static {
    MetaBean.register(HistoricalShockMarketDataSpecification.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static HistoricalShockMarketDataSpecification.Builder builder() {
    return new HistoricalShockMarketDataSpecification.Builder();
  }

  private HistoricalShockMarketDataSpecification(
      HistoricalShockMarketDataSnapshot.ShockType shockType,
      MarketDataSpecification historicalSpecification1,
      MarketDataSpecification historicalSpecification2,
      MarketDataSpecification baseSpecification) {
    JodaBeanUtils.notNull(shockType, "shockType");
    JodaBeanUtils.notNull(historicalSpecification1, "historicalSpecification1");
    JodaBeanUtils.notNull(historicalSpecification2, "historicalSpecification2");
    JodaBeanUtils.notNull(baseSpecification, "baseSpecification");
    this._shockType = shockType;
    this._historicalSpecification1 = historicalSpecification1;
    this._historicalSpecification2 = historicalSpecification2;
    this._baseSpecification = baseSpecification;
  }

  @Override
  public HistoricalShockMarketDataSpecification.Meta metaBean() {
    return HistoricalShockMarketDataSpecification.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the shockType.
   * @return the value of the property, not null
   */
  public HistoricalShockMarketDataSnapshot.ShockType getShockType() {
    return _shockType;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the historicalSpecification1.
   * @return the value of the property, not null
   */
  public MarketDataSpecification getHistoricalSpecification1() {
    return _historicalSpecification1;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the historicalSpecification2.
   * @return the value of the property, not null
   */
  public MarketDataSpecification getHistoricalSpecification2() {
    return _historicalSpecification2;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the baseSpecification.
   * @return the value of the property, not null
   */
  public MarketDataSpecification getBaseSpecification() {
    return _baseSpecification;
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
      HistoricalShockMarketDataSpecification other = (HistoricalShockMarketDataSpecification) obj;
      return JodaBeanUtils.equal(_shockType, other._shockType) &&
          JodaBeanUtils.equal(_historicalSpecification1, other._historicalSpecification1) &&
          JodaBeanUtils.equal(_historicalSpecification2, other._historicalSpecification2) &&
          JodaBeanUtils.equal(_baseSpecification, other._baseSpecification);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_shockType);
    hash = hash * 31 + JodaBeanUtils.hashCode(_historicalSpecification1);
    hash = hash * 31 + JodaBeanUtils.hashCode(_historicalSpecification2);
    hash = hash * 31 + JodaBeanUtils.hashCode(_baseSpecification);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("HistoricalShockMarketDataSpecification{");
    buf.append("shockType").append('=').append(_shockType).append(',').append(' ');
    buf.append("historicalSpecification1").append('=').append(_historicalSpecification1).append(',').append(' ');
    buf.append("historicalSpecification2").append('=').append(_historicalSpecification2).append(',').append(' ');
    buf.append("baseSpecification").append('=').append(JodaBeanUtils.toString(_baseSpecification));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code HistoricalShockMarketDataSpecification}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code shockType} property.
     */
    private final MetaProperty<HistoricalShockMarketDataSnapshot.ShockType> _shockType = DirectMetaProperty.ofImmutable(
        this, "shockType", HistoricalShockMarketDataSpecification.class, HistoricalShockMarketDataSnapshot.ShockType.class);
    /**
     * The meta-property for the {@code historicalSpecification1} property.
     */
    private final MetaProperty<MarketDataSpecification> _historicalSpecification1 = DirectMetaProperty.ofImmutable(
        this, "historicalSpecification1", HistoricalShockMarketDataSpecification.class, MarketDataSpecification.class);
    /**
     * The meta-property for the {@code historicalSpecification2} property.
     */
    private final MetaProperty<MarketDataSpecification> _historicalSpecification2 = DirectMetaProperty.ofImmutable(
        this, "historicalSpecification2", HistoricalShockMarketDataSpecification.class, MarketDataSpecification.class);
    /**
     * The meta-property for the {@code baseSpecification} property.
     */
    private final MetaProperty<MarketDataSpecification> _baseSpecification = DirectMetaProperty.ofImmutable(
        this, "baseSpecification", HistoricalShockMarketDataSpecification.class, MarketDataSpecification.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "shockType",
        "historicalSpecification1",
        "historicalSpecification2",
        "baseSpecification");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1829201148:  // shockType
          return _shockType;
        case 1567137528:  // historicalSpecification1
          return _historicalSpecification1;
        case 1567137529:  // historicalSpecification2
          return _historicalSpecification2;
        case 1357301170:  // baseSpecification
          return _baseSpecification;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public HistoricalShockMarketDataSpecification.Builder builder() {
      return new HistoricalShockMarketDataSpecification.Builder();
    }

    @Override
    public Class<? extends HistoricalShockMarketDataSpecification> beanType() {
      return HistoricalShockMarketDataSpecification.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code shockType} property.
     * @return the meta-property, not null
     */
    public MetaProperty<HistoricalShockMarketDataSnapshot.ShockType> shockType() {
      return _shockType;
    }

    /**
     * The meta-property for the {@code historicalSpecification1} property.
     * @return the meta-property, not null
     */
    public MetaProperty<MarketDataSpecification> historicalSpecification1() {
      return _historicalSpecification1;
    }

    /**
     * The meta-property for the {@code historicalSpecification2} property.
     * @return the meta-property, not null
     */
    public MetaProperty<MarketDataSpecification> historicalSpecification2() {
      return _historicalSpecification2;
    }

    /**
     * The meta-property for the {@code baseSpecification} property.
     * @return the meta-property, not null
     */
    public MetaProperty<MarketDataSpecification> baseSpecification() {
      return _baseSpecification;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1829201148:  // shockType
          return ((HistoricalShockMarketDataSpecification) bean).getShockType();
        case 1567137528:  // historicalSpecification1
          return ((HistoricalShockMarketDataSpecification) bean).getHistoricalSpecification1();
        case 1567137529:  // historicalSpecification2
          return ((HistoricalShockMarketDataSpecification) bean).getHistoricalSpecification2();
        case 1357301170:  // baseSpecification
          return ((HistoricalShockMarketDataSpecification) bean).getBaseSpecification();
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
   * The bean-builder for {@code HistoricalShockMarketDataSpecification}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<HistoricalShockMarketDataSpecification> {

    private HistoricalShockMarketDataSnapshot.ShockType _shockType;
    private MarketDataSpecification _historicalSpecification1;
    private MarketDataSpecification _historicalSpecification2;
    private MarketDataSpecification _baseSpecification;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(HistoricalShockMarketDataSpecification beanToCopy) {
      this._shockType = beanToCopy.getShockType();
      this._historicalSpecification1 = beanToCopy.getHistoricalSpecification1();
      this._historicalSpecification2 = beanToCopy.getHistoricalSpecification2();
      this._baseSpecification = beanToCopy.getBaseSpecification();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1829201148:  // shockType
          return _shockType;
        case 1567137528:  // historicalSpecification1
          return _historicalSpecification1;
        case 1567137529:  // historicalSpecification2
          return _historicalSpecification2;
        case 1357301170:  // baseSpecification
          return _baseSpecification;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 1829201148:  // shockType
          this._shockType = (HistoricalShockMarketDataSnapshot.ShockType) newValue;
          break;
        case 1567137528:  // historicalSpecification1
          this._historicalSpecification1 = (MarketDataSpecification) newValue;
          break;
        case 1567137529:  // historicalSpecification2
          this._historicalSpecification2 = (MarketDataSpecification) newValue;
          break;
        case 1357301170:  // baseSpecification
          this._baseSpecification = (MarketDataSpecification) newValue;
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
    public HistoricalShockMarketDataSpecification build() {
      return new HistoricalShockMarketDataSpecification(
          _shockType,
          _historicalSpecification1,
          _historicalSpecification2,
          _baseSpecification);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the shockType.
     * @param shockType  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder shockType(HistoricalShockMarketDataSnapshot.ShockType shockType) {
      JodaBeanUtils.notNull(shockType, "shockType");
      this._shockType = shockType;
      return this;
    }

    /**
     * Sets the historicalSpecification1.
     * @param historicalSpecification1  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder historicalSpecification1(MarketDataSpecification historicalSpecification1) {
      JodaBeanUtils.notNull(historicalSpecification1, "historicalSpecification1");
      this._historicalSpecification1 = historicalSpecification1;
      return this;
    }

    /**
     * Sets the historicalSpecification2.
     * @param historicalSpecification2  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder historicalSpecification2(MarketDataSpecification historicalSpecification2) {
      JodaBeanUtils.notNull(historicalSpecification2, "historicalSpecification2");
      this._historicalSpecification2 = historicalSpecification2;
      return this;
    }

    /**
     * Sets the baseSpecification.
     * @param baseSpecification  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder baseSpecification(MarketDataSpecification baseSpecification) {
      JodaBeanUtils.notNull(baseSpecification, "baseSpecification");
      this._baseSpecification = baseSpecification;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("HistoricalShockMarketDataSpecification.Builder{");
      buf.append("shockType").append('=').append(JodaBeanUtils.toString(_shockType)).append(',').append(' ');
      buf.append("historicalSpecification1").append('=').append(JodaBeanUtils.toString(_historicalSpecification1)).append(',').append(' ');
      buf.append("historicalSpecification2").append('=').append(JodaBeanUtils.toString(_historicalSpecification2)).append(',').append(' ');
      buf.append("baseSpecification").append('=').append(JodaBeanUtils.toString(_baseSpecification));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
