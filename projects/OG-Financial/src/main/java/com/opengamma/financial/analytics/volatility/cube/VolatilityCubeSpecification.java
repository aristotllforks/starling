/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.volatility.cube;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.config.Config;
import com.opengamma.core.config.ConfigGroups;
import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdentifiable;

/**
 * Specification for a volatility cube - contains all available points on the cube.
 */
@Config(description = "Volatility cube specification", group = ConfigGroups.VOL)
@BeanDefinition
public class VolatilityCubeSpecification implements Bean, Serializable, UniqueIdentifiable {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The unique id.
   */
  @PropertyDefinition
  private UniqueId _uniqueId;

  /**
   * The specification name.
   */
  @PropertyDefinition(validate = "notNull")
  private String _name;
  /**
   * The cube quote type.
   */
  @PropertyDefinition(validate = "notNull")
  private String _cubeQuoteType;

  /**
   * The volatility quote units.
   */
  @PropertyDefinition(validate = "notNull")
  private String _volatilityQuoteUnits;

  /**
   * The volatility cube instrument provider.
   */
  @PropertyDefinition(validate = "notNull")
  private CubeInstrumentProvider<?, ?, ?> _cubeInstrumentProvider;

  /**
   * For the builder.
   */
  /* package */ VolatilityCubeSpecification() {
  }

  /**
   * @param name The name of the specification, not null
   * @param cubeQuoteType The cube quote type, not null
   * @param volatilityQuoteUnits The volatility quote units, not null
   * @param cubeInstrumentProvider The instrument provider, not null
   */
  public VolatilityCubeSpecification(final String name, final String cubeQuoteType, final String volatilityQuoteUnits,
      final CubeInstrumentProvider<?, ?, ?> cubeInstrumentProvider) {
    setName(name);
    setCubeQuoteType(cubeQuoteType);
    setVolatilityQuoteUnits(volatilityQuoteUnits);
    setCubeInstrumentProvider(cubeInstrumentProvider);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code VolatilityCubeSpecification}.
   * @return the meta-bean, not null
   */
  public static VolatilityCubeSpecification.Meta meta() {
    return VolatilityCubeSpecification.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(VolatilityCubeSpecification.Meta.INSTANCE);
  }

  @Override
  public VolatilityCubeSpecification.Meta metaBean() {
    return VolatilityCubeSpecification.Meta.INSTANCE;
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
   * Gets the unique id.
   * @return the value of the property
   */
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the unique id.
   * @param uniqueId  the new value of the property
   */
  public void setUniqueId(UniqueId uniqueId) {
    this._uniqueId = uniqueId;
  }

  /**
   * Gets the the {@code uniqueId} property.
   * @return the property, not null
   */
  public final Property<UniqueId> uniqueId() {
    return metaBean().uniqueId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the specification name.
   * @return the value of the property, not null
   */
  public String getName() {
    return _name;
  }

  /**
   * Sets the specification name.
   * @param name  the new value of the property, not null
   */
  public void setName(String name) {
    JodaBeanUtils.notNull(name, "name");
    this._name = name;
  }

  /**
   * Gets the the {@code name} property.
   * @return the property, not null
   */
  public final Property<String> name() {
    return metaBean().name().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cube quote type.
   * @return the value of the property, not null
   */
  public String getCubeQuoteType() {
    return _cubeQuoteType;
  }

  /**
   * Sets the cube quote type.
   * @param cubeQuoteType  the new value of the property, not null
   */
  public void setCubeQuoteType(String cubeQuoteType) {
    JodaBeanUtils.notNull(cubeQuoteType, "cubeQuoteType");
    this._cubeQuoteType = cubeQuoteType;
  }

  /**
   * Gets the the {@code cubeQuoteType} property.
   * @return the property, not null
   */
  public final Property<String> cubeQuoteType() {
    return metaBean().cubeQuoteType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the volatility quote units.
   * @return the value of the property, not null
   */
  public String getVolatilityQuoteUnits() {
    return _volatilityQuoteUnits;
  }

  /**
   * Sets the volatility quote units.
   * @param volatilityQuoteUnits  the new value of the property, not null
   */
  public void setVolatilityQuoteUnits(String volatilityQuoteUnits) {
    JodaBeanUtils.notNull(volatilityQuoteUnits, "volatilityQuoteUnits");
    this._volatilityQuoteUnits = volatilityQuoteUnits;
  }

  /**
   * Gets the the {@code volatilityQuoteUnits} property.
   * @return the property, not null
   */
  public final Property<String> volatilityQuoteUnits() {
    return metaBean().volatilityQuoteUnits().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the volatility cube instrument provider.
   * @return the value of the property, not null
   */
  public CubeInstrumentProvider<?, ?, ?> getCubeInstrumentProvider() {
    return _cubeInstrumentProvider;
  }

  /**
   * Sets the volatility cube instrument provider.
   * @param cubeInstrumentProvider  the new value of the property, not null
   */
  public void setCubeInstrumentProvider(CubeInstrumentProvider<?, ?, ?> cubeInstrumentProvider) {
    JodaBeanUtils.notNull(cubeInstrumentProvider, "cubeInstrumentProvider");
    this._cubeInstrumentProvider = cubeInstrumentProvider;
  }

  /**
   * Gets the the {@code cubeInstrumentProvider} property.
   * @return the property, not null
   */
  public final Property<CubeInstrumentProvider<?, ?, ?>> cubeInstrumentProvider() {
    return metaBean().cubeInstrumentProvider().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public VolatilityCubeSpecification clone() {
    BeanBuilder<? extends VolatilityCubeSpecification> builder = metaBean().builder();
    for (MetaProperty<?> mp : metaBean().metaPropertyIterable()) {
      if (mp.style().isBuildable()) {
        Object value = mp.get(this);
        if (value instanceof Bean) {
          value = ((Bean) value).clone();
        }
        builder.set(mp.name(), value);
      }
    }
    return builder.build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      VolatilityCubeSpecification other = (VolatilityCubeSpecification) obj;
      return JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          JodaBeanUtils.equal(getName(), other.getName()) &&
          JodaBeanUtils.equal(getCubeQuoteType(), other.getCubeQuoteType()) &&
          JodaBeanUtils.equal(getVolatilityQuoteUnits(), other.getVolatilityQuoteUnits()) &&
          JodaBeanUtils.equal(getCubeInstrumentProvider(), other.getCubeInstrumentProvider());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    hash += hash * 31 + JodaBeanUtils.hashCode(getName());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCubeQuoteType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getVolatilityQuoteUnits());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCubeInstrumentProvider());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("VolatilityCubeSpecification{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("uniqueId").append('=').append(JodaBeanUtils.toString(getUniqueId())).append(',').append(' ');
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
    buf.append("cubeQuoteType").append('=').append(JodaBeanUtils.toString(getCubeQuoteType())).append(',').append(' ');
    buf.append("volatilityQuoteUnits").append('=').append(JodaBeanUtils.toString(getVolatilityQuoteUnits())).append(',').append(' ');
    buf.append("cubeInstrumentProvider").append('=').append(JodaBeanUtils.toString(getCubeInstrumentProvider())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code VolatilityCubeSpecification}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueId> _uniqueId = DirectMetaProperty.ofReadWrite(
        this, "uniqueId", VolatilityCubeSpecification.class, UniqueId.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", VolatilityCubeSpecification.class, String.class);
    /**
     * The meta-property for the {@code cubeQuoteType} property.
     */
    private final MetaProperty<String> _cubeQuoteType = DirectMetaProperty.ofReadWrite(
        this, "cubeQuoteType", VolatilityCubeSpecification.class, String.class);
    /**
     * The meta-property for the {@code volatilityQuoteUnits} property.
     */
    private final MetaProperty<String> _volatilityQuoteUnits = DirectMetaProperty.ofReadWrite(
        this, "volatilityQuoteUnits", VolatilityCubeSpecification.class, String.class);
    /**
     * The meta-property for the {@code cubeInstrumentProvider} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<CubeInstrumentProvider<?, ?, ?>> _cubeInstrumentProvider = DirectMetaProperty.ofReadWrite(
        this, "cubeInstrumentProvider", VolatilityCubeSpecification.class, (Class) CubeInstrumentProvider.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "uniqueId",
        "name",
        "cubeQuoteType",
        "volatilityQuoteUnits",
        "cubeInstrumentProvider");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return _uniqueId;
        case 3373707:  // name
          return _name;
        case 1081076513:  // cubeQuoteType
          return _cubeQuoteType;
        case 1024633944:  // volatilityQuoteUnits
          return _volatilityQuoteUnits;
        case -446122995:  // cubeInstrumentProvider
          return _cubeInstrumentProvider;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends VolatilityCubeSpecification> builder() {
      return new DirectBeanBuilder<VolatilityCubeSpecification>(new VolatilityCubeSpecification());
    }

    @Override
    public Class<? extends VolatilityCubeSpecification> beanType() {
      return VolatilityCubeSpecification.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code uniqueId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueId> uniqueId() {
      return _uniqueId;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
    }

    /**
     * The meta-property for the {@code cubeQuoteType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> cubeQuoteType() {
      return _cubeQuoteType;
    }

    /**
     * The meta-property for the {@code volatilityQuoteUnits} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> volatilityQuoteUnits() {
      return _volatilityQuoteUnits;
    }

    /**
     * The meta-property for the {@code cubeInstrumentProvider} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CubeInstrumentProvider<?, ?, ?>> cubeInstrumentProvider() {
      return _cubeInstrumentProvider;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return ((VolatilityCubeSpecification) bean).getUniqueId();
        case 3373707:  // name
          return ((VolatilityCubeSpecification) bean).getName();
        case 1081076513:  // cubeQuoteType
          return ((VolatilityCubeSpecification) bean).getCubeQuoteType();
        case 1024633944:  // volatilityQuoteUnits
          return ((VolatilityCubeSpecification) bean).getVolatilityQuoteUnits();
        case -446122995:  // cubeInstrumentProvider
          return ((VolatilityCubeSpecification) bean).getCubeInstrumentProvider();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          ((VolatilityCubeSpecification) bean).setUniqueId((UniqueId) newValue);
          return;
        case 3373707:  // name
          ((VolatilityCubeSpecification) bean).setName((String) newValue);
          return;
        case 1081076513:  // cubeQuoteType
          ((VolatilityCubeSpecification) bean).setCubeQuoteType((String) newValue);
          return;
        case 1024633944:  // volatilityQuoteUnits
          ((VolatilityCubeSpecification) bean).setVolatilityQuoteUnits((String) newValue);
          return;
        case -446122995:  // cubeInstrumentProvider
          ((VolatilityCubeSpecification) bean).setCubeInstrumentProvider((CubeInstrumentProvider<?, ?, ?>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((VolatilityCubeSpecification) bean)._name, "name");
      JodaBeanUtils.notNull(((VolatilityCubeSpecification) bean)._cubeQuoteType, "cubeQuoteType");
      JodaBeanUtils.notNull(((VolatilityCubeSpecification) bean)._volatilityQuoteUnits, "volatilityQuoteUnits");
      JodaBeanUtils.notNull(((VolatilityCubeSpecification) bean)._cubeInstrumentProvider, "cubeInstrumentProvider");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
