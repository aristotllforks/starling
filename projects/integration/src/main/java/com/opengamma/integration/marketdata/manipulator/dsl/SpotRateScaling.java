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
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableSet;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.marketdata.manipulator.function.StructureManipulator;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.currency.CurrencyPair;
import com.opengamma.util.ArgumentChecker;

/**
 * Scales a spot rate.
 */
@BeanDefinition
public final class SpotRateScaling implements StructureManipulator<Double>, ImmutableBean {

  @PropertyDefinition(validate = "notNull")
  private final Double _scalingFactor;

  @PropertyDefinition
  private final double _minRate;

  @PropertyDefinition
  private final double _maxRate;

  @PropertyDefinition(validate = "notNull")
  private final Set<CurrencyPair> _currencyPairs;

  /* package */ SpotRateScaling(final Number scalingFactor,
                                final Number minRate,
                                final Number maxRate,
                                final CurrencyPair currencyPair) {
    _currencyPairs = ImmutableSet.of(ArgumentChecker.notNull(currencyPair, "currencyPair"));
    _scalingFactor = ArgumentChecker.notNull(scalingFactor, "scalingFactor").doubleValue();
    _minRate = ArgumentChecker.notNull(minRate, "minRate").doubleValue();
    _maxRate = ArgumentChecker.notNull(maxRate, "minRate").doubleValue();

    if (_minRate > _maxRate) {
      throw new IllegalArgumentException("Minimum rate must be less than or equal to maximum rate");
    }
    if (_minRate < 0 || _maxRate < 0) {
      throw new IllegalArgumentException("Minimum and maximum rate must be greater than zero");
    }
  }

  /* package */ SpotRateScaling(final Number scalingFactor, final Set<CurrencyPair> currencyPairs) {
    _currencyPairs = ArgumentChecker.notEmpty(currencyPairs, "currencyPairs");
    _scalingFactor = ArgumentChecker.notNull(scalingFactor, "scalingFactor").doubleValue();
    _minRate = 0;
    _maxRate = Double.MAX_VALUE;
  }

  @Override
  public Double execute(final Double spotRate, final ValueSpecification valueSpecification, final FunctionExecutionContext executionContext) {
    final CurrencyPair currencyPair = SimulationUtils.getCurrencyPair(valueSpecification);

    if (_currencyPairs.contains(currencyPair)) {
      return applyScaling(spotRate);
    } else if (_currencyPairs.contains(currencyPair.inverse())) {
      final double inverseRate = 1 / spotRate;
      final double scaledRate = applyScaling(inverseRate);
      return 1 / scaledRate;
    } else {
      throw new IllegalArgumentException("Currency pair " + currencyPair + " shouldn't match " + _currencyPairs);
    }
  }

  private double applyScaling(final double rate) {
    final double scaledRate = rate * _scalingFactor;

    if (scaledRate < _minRate) {
      return _minRate;
    } else if (scaledRate > _maxRate) {
      return _maxRate;
    } else {
      return scaledRate;
    }
  }

  @Override
  public Class<Double> getExpectedType() {
    return Double.class;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SpotRateScaling}.
   * @return the meta-bean, not null
   */
  public static SpotRateScaling.Meta meta() {
    return SpotRateScaling.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SpotRateScaling.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static SpotRateScaling.Builder builder() {
    return new SpotRateScaling.Builder();
  }

  private SpotRateScaling(
      Double scalingFactor,
      double minRate,
      double maxRate,
      Set<CurrencyPair> currencyPairs) {
    JodaBeanUtils.notNull(scalingFactor, "scalingFactor");
    JodaBeanUtils.notNull(currencyPairs, "currencyPairs");
    this._scalingFactor = scalingFactor;
    this._minRate = minRate;
    this._maxRate = maxRate;
    this._currencyPairs = ImmutableSet.copyOf(currencyPairs);
  }

  @Override
  public SpotRateScaling.Meta metaBean() {
    return SpotRateScaling.Meta.INSTANCE;
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
   * Gets the scalingFactor.
   * @return the value of the property, not null
   */
  public Double getScalingFactor() {
    return _scalingFactor;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the minRate.
   * @return the value of the property
   */
  public double getMinRate() {
    return _minRate;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the maxRate.
   * @return the value of the property
   */
  public double getMaxRate() {
    return _maxRate;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currencyPairs.
   * @return the value of the property, not null
   */
  public Set<CurrencyPair> getCurrencyPairs() {
    return _currencyPairs;
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
      SpotRateScaling other = (SpotRateScaling) obj;
      return JodaBeanUtils.equal(_scalingFactor, other._scalingFactor) &&
          JodaBeanUtils.equal(_minRate, other._minRate) &&
          JodaBeanUtils.equal(_maxRate, other._maxRate) &&
          JodaBeanUtils.equal(_currencyPairs, other._currencyPairs);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_scalingFactor);
    hash = hash * 31 + JodaBeanUtils.hashCode(_minRate);
    hash = hash * 31 + JodaBeanUtils.hashCode(_maxRate);
    hash = hash * 31 + JodaBeanUtils.hashCode(_currencyPairs);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("SpotRateScaling{");
    buf.append("scalingFactor").append('=').append(_scalingFactor).append(',').append(' ');
    buf.append("minRate").append('=').append(_minRate).append(',').append(' ');
    buf.append("maxRate").append('=').append(_maxRate).append(',').append(' ');
    buf.append("currencyPairs").append('=').append(JodaBeanUtils.toString(_currencyPairs));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SpotRateScaling}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code scalingFactor} property.
     */
    private final MetaProperty<Double> _scalingFactor = DirectMetaProperty.ofImmutable(
        this, "scalingFactor", SpotRateScaling.class, Double.class);
    /**
     * The meta-property for the {@code minRate} property.
     */
    private final MetaProperty<Double> _minRate = DirectMetaProperty.ofImmutable(
        this, "minRate", SpotRateScaling.class, Double.TYPE);
    /**
     * The meta-property for the {@code maxRate} property.
     */
    private final MetaProperty<Double> _maxRate = DirectMetaProperty.ofImmutable(
        this, "maxRate", SpotRateScaling.class, Double.TYPE);
    /**
     * The meta-property for the {@code currencyPairs} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<CurrencyPair>> _currencyPairs = DirectMetaProperty.ofImmutable(
        this, "currencyPairs", SpotRateScaling.class, (Class) Set.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "scalingFactor",
        "minRate",
        "maxRate",
        "currencyPairs");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -794828874:  // scalingFactor
          return _scalingFactor;
        case 1063841362:  // minRate
          return _minRate;
        case 844043364:  // maxRate
          return _maxRate;
        case 1094810440:  // currencyPairs
          return _currencyPairs;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public SpotRateScaling.Builder builder() {
      return new SpotRateScaling.Builder();
    }

    @Override
    public Class<? extends SpotRateScaling> beanType() {
      return SpotRateScaling.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code scalingFactor} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> scalingFactor() {
      return _scalingFactor;
    }

    /**
     * The meta-property for the {@code minRate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> minRate() {
      return _minRate;
    }

    /**
     * The meta-property for the {@code maxRate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> maxRate() {
      return _maxRate;
    }

    /**
     * The meta-property for the {@code currencyPairs} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Set<CurrencyPair>> currencyPairs() {
      return _currencyPairs;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -794828874:  // scalingFactor
          return ((SpotRateScaling) bean).getScalingFactor();
        case 1063841362:  // minRate
          return ((SpotRateScaling) bean).getMinRate();
        case 844043364:  // maxRate
          return ((SpotRateScaling) bean).getMaxRate();
        case 1094810440:  // currencyPairs
          return ((SpotRateScaling) bean).getCurrencyPairs();
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
   * The bean-builder for {@code SpotRateScaling}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<SpotRateScaling> {

    private Double _scalingFactor;
    private double _minRate;
    private double _maxRate;
    private Set<CurrencyPair> _currencyPairs = ImmutableSet.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(SpotRateScaling beanToCopy) {
      this._scalingFactor = beanToCopy.getScalingFactor();
      this._minRate = beanToCopy.getMinRate();
      this._maxRate = beanToCopy.getMaxRate();
      this._currencyPairs = ImmutableSet.copyOf(beanToCopy.getCurrencyPairs());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -794828874:  // scalingFactor
          return _scalingFactor;
        case 1063841362:  // minRate
          return _minRate;
        case 844043364:  // maxRate
          return _maxRate;
        case 1094810440:  // currencyPairs
          return _currencyPairs;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -794828874:  // scalingFactor
          this._scalingFactor = (Double) newValue;
          break;
        case 1063841362:  // minRate
          this._minRate = (Double) newValue;
          break;
        case 844043364:  // maxRate
          this._maxRate = (Double) newValue;
          break;
        case 1094810440:  // currencyPairs
          this._currencyPairs = (Set<CurrencyPair>) newValue;
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
    public SpotRateScaling build() {
      return new SpotRateScaling(
          _scalingFactor,
          _minRate,
          _maxRate,
          _currencyPairs);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the scalingFactor.
     * @param scalingFactor  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder scalingFactor(Double scalingFactor) {
      JodaBeanUtils.notNull(scalingFactor, "scalingFactor");
      this._scalingFactor = scalingFactor;
      return this;
    }

    /**
     * Sets the minRate.
     * @param minRate  the new value
     * @return this, for chaining, not null
     */
    public Builder minRate(double minRate) {
      this._minRate = minRate;
      return this;
    }

    /**
     * Sets the maxRate.
     * @param maxRate  the new value
     * @return this, for chaining, not null
     */
    public Builder maxRate(double maxRate) {
      this._maxRate = maxRate;
      return this;
    }

    /**
     * Sets the currencyPairs.
     * @param currencyPairs  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder currencyPairs(Set<CurrencyPair> currencyPairs) {
      JodaBeanUtils.notNull(currencyPairs, "currencyPairs");
      this._currencyPairs = currencyPairs;
      return this;
    }

    /**
     * Sets the {@code currencyPairs} property in the builder
     * from an array of objects.
     * @param currencyPairs  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder currencyPairs(CurrencyPair... currencyPairs) {
      return currencyPairs(ImmutableSet.copyOf(currencyPairs));
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("SpotRateScaling.Builder{");
      buf.append("scalingFactor").append('=').append(JodaBeanUtils.toString(_scalingFactor)).append(',').append(' ');
      buf.append("minRate").append('=').append(JodaBeanUtils.toString(_minRate)).append(',').append(' ');
      buf.append("maxRate").append('=').append(JodaBeanUtils.toString(_maxRate)).append(',').append(' ');
      buf.append("currencyPairs").append('=').append(JodaBeanUtils.toString(_currencyPairs));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
