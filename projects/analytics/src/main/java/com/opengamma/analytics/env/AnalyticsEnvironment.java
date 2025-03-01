/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.env;

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

import com.opengamma.analytics.financial.instrument.annuity.FixedAnnuityDefinitionBuilder;
import com.opengamma.analytics.financial.instrument.annuity.FloatingAnnuityDefinitionBuilder;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCounts;
import com.opengamma.util.ArgumentChecker;

/**
 * Environment holding analytics customisations. This allows custom behaviour to be injected into analytics functions. e.g. the model daycount
 */
@BeanDefinition
public final class AnalyticsEnvironment implements ImmutableBean {

  /**
   * The default environment, which consists of:
   * <ul>
   * <li>model daycount of Act/Act ISDA.
   * <li>standard fixed annuity builder
   * <li>standard floating annuity builder
   * </ul>
   */
  public static final AnalyticsEnvironment DEFAULT = AnalyticsEnvironment.builder()
      .modelDayCount(DayCounts.ACT_ACT_ISDA)
      .fixedAnnuityDefinitionBuilder(new FixedAnnuityDefinitionBuilder())
      .floatingAnnuityDefinitionBuilder(new FloatingAnnuityDefinitionBuilder())
      .build();

  /**
   * The thread-local environment.
   */
  private static ThreadLocal<AnalyticsEnvironment> s_instance = new InheritableThreadLocal<>();

  /**
   * The model daycount. Used to compute fractions of a year when obtaining values from a curve.
   * Different to the daycount used to price an instrument, which comes from the instrument or a convention.
   *
   * @see DayCount
   */
  @PropertyDefinition(validate = "notNull")
  private final DayCount _modelDayCount;

  /**
   * Builder for generating fixed annuities.
   *
   * @see FixedAnnuityDefinitionBuilder
   */
  @PropertyDefinition(validate = "notNull")
  private final FixedAnnuityDefinitionBuilder _fixedAnnuityDefinitionBuilder;

  /**
   * Builder for generating floating (Ibor, OIS etc) annuities.
   *
   * @see FloatingAnnuityDefinitionBuilder
   */
  @PropertyDefinition(validate = "notNull")
  private final FloatingAnnuityDefinitionBuilder _floatingAnnuityDefinitionBuilder;

  /**
   * Returns the AnalyticsEnvironment.
   *
   * @return the {@link AnalyticsEnvironment} from the current thread, if not set return {@link #DEFAULT}
   * @see #DEFAULT
   */
  public static AnalyticsEnvironment getInstance() {
    final AnalyticsEnvironment environment = s_instance.get();
    if (environment != null) {
      return environment;
    }
    return DEFAULT;
  }

  /**
   * Set the current {@link AnalyticsEnvironment} for the current thread.
   *
   * @param analyticsEnvironment
   *          the analytics environment to set, not null
   */
  public static void setInstance(final AnalyticsEnvironment analyticsEnvironment) {
    ArgumentChecker.notNull(analyticsEnvironment, "analyticsEnvironment");
    s_instance.set(analyticsEnvironment);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code AnalyticsEnvironment}.
   * @return the meta-bean, not null
   */
  public static AnalyticsEnvironment.Meta meta() {
    return AnalyticsEnvironment.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(AnalyticsEnvironment.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static AnalyticsEnvironment.Builder builder() {
    return new AnalyticsEnvironment.Builder();
  }

  private AnalyticsEnvironment(
      DayCount modelDayCount,
      FixedAnnuityDefinitionBuilder fixedAnnuityDefinitionBuilder,
      FloatingAnnuityDefinitionBuilder floatingAnnuityDefinitionBuilder) {
    JodaBeanUtils.notNull(modelDayCount, "modelDayCount");
    JodaBeanUtils.notNull(fixedAnnuityDefinitionBuilder, "fixedAnnuityDefinitionBuilder");
    JodaBeanUtils.notNull(floatingAnnuityDefinitionBuilder, "floatingAnnuityDefinitionBuilder");
    this._modelDayCount = modelDayCount;
    this._fixedAnnuityDefinitionBuilder = fixedAnnuityDefinitionBuilder;
    this._floatingAnnuityDefinitionBuilder = floatingAnnuityDefinitionBuilder;
  }

  @Override
  public AnalyticsEnvironment.Meta metaBean() {
    return AnalyticsEnvironment.Meta.INSTANCE;
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
   * Gets the model daycount. Used to compute fractions of a year when obtaining values from a curve.
   * Different to the daycount used to price an instrument, which comes from the instrument or a convention.
   * 
   * @see DayCount
   * @return the value of the property, not null
   */
  public DayCount getModelDayCount() {
    return _modelDayCount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets builder for generating fixed annuities.
   * 
   * @see FixedAnnuityDefinitionBuilder
   * @return the value of the property, not null
   */
  public FixedAnnuityDefinitionBuilder getFixedAnnuityDefinitionBuilder() {
    return _fixedAnnuityDefinitionBuilder;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets builder for generating floating (Ibor, OIS etc) annuities.
   * 
   * @see FloatingAnnuityDefinitionBuilder
   * @return the value of the property, not null
   */
  public FloatingAnnuityDefinitionBuilder getFloatingAnnuityDefinitionBuilder() {
    return _floatingAnnuityDefinitionBuilder;
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
      AnalyticsEnvironment other = (AnalyticsEnvironment) obj;
      return JodaBeanUtils.equal(_modelDayCount, other._modelDayCount) &&
          JodaBeanUtils.equal(_fixedAnnuityDefinitionBuilder, other._fixedAnnuityDefinitionBuilder) &&
          JodaBeanUtils.equal(_floatingAnnuityDefinitionBuilder, other._floatingAnnuityDefinitionBuilder);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_modelDayCount);
    hash = hash * 31 + JodaBeanUtils.hashCode(_fixedAnnuityDefinitionBuilder);
    hash = hash * 31 + JodaBeanUtils.hashCode(_floatingAnnuityDefinitionBuilder);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("AnalyticsEnvironment{");
    buf.append("modelDayCount").append('=').append(_modelDayCount).append(',').append(' ');
    buf.append("fixedAnnuityDefinitionBuilder").append('=').append(_fixedAnnuityDefinitionBuilder).append(',').append(' ');
    buf.append("floatingAnnuityDefinitionBuilder").append('=').append(JodaBeanUtils.toString(_floatingAnnuityDefinitionBuilder));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code AnalyticsEnvironment}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code modelDayCount} property.
     */
    private final MetaProperty<DayCount> _modelDayCount = DirectMetaProperty.ofImmutable(
        this, "modelDayCount", AnalyticsEnvironment.class, DayCount.class);
    /**
     * The meta-property for the {@code fixedAnnuityDefinitionBuilder} property.
     */
    private final MetaProperty<FixedAnnuityDefinitionBuilder> _fixedAnnuityDefinitionBuilder = DirectMetaProperty.ofImmutable(
        this, "fixedAnnuityDefinitionBuilder", AnalyticsEnvironment.class, FixedAnnuityDefinitionBuilder.class);
    /**
     * The meta-property for the {@code floatingAnnuityDefinitionBuilder} property.
     */
    private final MetaProperty<FloatingAnnuityDefinitionBuilder> _floatingAnnuityDefinitionBuilder = DirectMetaProperty.ofImmutable(
        this, "floatingAnnuityDefinitionBuilder", AnalyticsEnvironment.class, FloatingAnnuityDefinitionBuilder.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "modelDayCount",
        "fixedAnnuityDefinitionBuilder",
        "floatingAnnuityDefinitionBuilder");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1885988124:  // modelDayCount
          return _modelDayCount;
        case -2010322942:  // fixedAnnuityDefinitionBuilder
          return _fixedAnnuityDefinitionBuilder;
        case -1269173612:  // floatingAnnuityDefinitionBuilder
          return _floatingAnnuityDefinitionBuilder;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public AnalyticsEnvironment.Builder builder() {
      return new AnalyticsEnvironment.Builder();
    }

    @Override
    public Class<? extends AnalyticsEnvironment> beanType() {
      return AnalyticsEnvironment.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code modelDayCount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DayCount> modelDayCount() {
      return _modelDayCount;
    }

    /**
     * The meta-property for the {@code fixedAnnuityDefinitionBuilder} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FixedAnnuityDefinitionBuilder> fixedAnnuityDefinitionBuilder() {
      return _fixedAnnuityDefinitionBuilder;
    }

    /**
     * The meta-property for the {@code floatingAnnuityDefinitionBuilder} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FloatingAnnuityDefinitionBuilder> floatingAnnuityDefinitionBuilder() {
      return _floatingAnnuityDefinitionBuilder;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1885988124:  // modelDayCount
          return ((AnalyticsEnvironment) bean).getModelDayCount();
        case -2010322942:  // fixedAnnuityDefinitionBuilder
          return ((AnalyticsEnvironment) bean).getFixedAnnuityDefinitionBuilder();
        case -1269173612:  // floatingAnnuityDefinitionBuilder
          return ((AnalyticsEnvironment) bean).getFloatingAnnuityDefinitionBuilder();
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
   * The bean-builder for {@code AnalyticsEnvironment}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<AnalyticsEnvironment> {

    private DayCount _modelDayCount;
    private FixedAnnuityDefinitionBuilder _fixedAnnuityDefinitionBuilder;
    private FloatingAnnuityDefinitionBuilder _floatingAnnuityDefinitionBuilder;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(AnalyticsEnvironment beanToCopy) {
      this._modelDayCount = beanToCopy.getModelDayCount();
      this._fixedAnnuityDefinitionBuilder = beanToCopy.getFixedAnnuityDefinitionBuilder();
      this._floatingAnnuityDefinitionBuilder = beanToCopy.getFloatingAnnuityDefinitionBuilder();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1885988124:  // modelDayCount
          return _modelDayCount;
        case -2010322942:  // fixedAnnuityDefinitionBuilder
          return _fixedAnnuityDefinitionBuilder;
        case -1269173612:  // floatingAnnuityDefinitionBuilder
          return _floatingAnnuityDefinitionBuilder;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 1885988124:  // modelDayCount
          this._modelDayCount = (DayCount) newValue;
          break;
        case -2010322942:  // fixedAnnuityDefinitionBuilder
          this._fixedAnnuityDefinitionBuilder = (FixedAnnuityDefinitionBuilder) newValue;
          break;
        case -1269173612:  // floatingAnnuityDefinitionBuilder
          this._floatingAnnuityDefinitionBuilder = (FloatingAnnuityDefinitionBuilder) newValue;
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
    public AnalyticsEnvironment build() {
      return new AnalyticsEnvironment(
          _modelDayCount,
          _fixedAnnuityDefinitionBuilder,
          _floatingAnnuityDefinitionBuilder);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the model daycount. Used to compute fractions of a year when obtaining values from a curve.
     * Different to the daycount used to price an instrument, which comes from the instrument or a convention.
     * 
     * @see DayCount
     * @param modelDayCount  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder modelDayCount(DayCount modelDayCount) {
      JodaBeanUtils.notNull(modelDayCount, "modelDayCount");
      this._modelDayCount = modelDayCount;
      return this;
    }

    /**
     * Sets builder for generating fixed annuities.
     * 
     * @see FixedAnnuityDefinitionBuilder
     * @param fixedAnnuityDefinitionBuilder  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder fixedAnnuityDefinitionBuilder(FixedAnnuityDefinitionBuilder fixedAnnuityDefinitionBuilder) {
      JodaBeanUtils.notNull(fixedAnnuityDefinitionBuilder, "fixedAnnuityDefinitionBuilder");
      this._fixedAnnuityDefinitionBuilder = fixedAnnuityDefinitionBuilder;
      return this;
    }

    /**
     * Sets builder for generating floating (Ibor, OIS etc) annuities.
     * 
     * @see FloatingAnnuityDefinitionBuilder
     * @param floatingAnnuityDefinitionBuilder  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder floatingAnnuityDefinitionBuilder(FloatingAnnuityDefinitionBuilder floatingAnnuityDefinitionBuilder) {
      JodaBeanUtils.notNull(floatingAnnuityDefinitionBuilder, "floatingAnnuityDefinitionBuilder");
      this._floatingAnnuityDefinitionBuilder = floatingAnnuityDefinitionBuilder;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("AnalyticsEnvironment.Builder{");
      buf.append("modelDayCount").append('=').append(JodaBeanUtils.toString(_modelDayCount)).append(',').append(' ');
      buf.append("fixedAnnuityDefinitionBuilder").append('=').append(JodaBeanUtils.toString(_fixedAnnuityDefinitionBuilder)).append(',').append(' ');
      buf.append("floatingAnnuityDefinitionBuilder").append('=').append(JodaBeanUtils.toString(_floatingAnnuityDefinitionBuilder));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
