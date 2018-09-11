/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.exchange.impl;

import java.io.Serializable;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.ZoneId;

import com.opengamma.core.exchange.Exchange;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.MutableUniqueIdentifiable;
import com.opengamma.id.UniqueId;

/**
 * Simple implementation of {@code Exchange}.
 * <p>
 * This is the simplest possible implementation of the {@link Exchange} interface.
 * <p>
 * This class is mutable and not thread-safe.
 * It is intended to be used in the engine via the read-only {@code Exchange} interface.
 */
@BeanDefinition
public class SimpleExchange extends DirectBean
    implements Exchange, MutableUniqueIdentifiable, Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The unique identifier of the exchange.
   */
  @PropertyDefinition(overrideGet = true, overrideSet = true)
  private UniqueId _uniqueId;
  /**
   * The bundle of external identifiers that define the exchange.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private ExternalIdBundle _externalIdBundle = ExternalIdBundle.EMPTY;
  /**
   * The region external identifier bundle that defines where the exchange is located.
   */
  @PropertyDefinition(overrideGet = true)
  private ExternalIdBundle _regionIdBundle;
  /**
   * The time-zone of the exchange.
   */
  @PropertyDefinition(overrideGet = true)
  private ZoneId _timeZone;
  /**
   * The name of the exchange intended for display purposes.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private String _name = "";

  /**
   * Creates an instance.
   */
  public SimpleExchange() {
  }

  //-------------------------------------------------------------------------
  /**
   * Adds an external identifier to the bundle.
   *
   * @param externalId  the external identifier, not null
   */
  public void addExternalId(final ExternalId externalId) {
    setExternalIdBundle(getExternalIdBundle().withExternalId(externalId));
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SimpleExchange}.
   * @return the meta-bean, not null
   */
  public static SimpleExchange.Meta meta() {
    return SimpleExchange.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SimpleExchange.Meta.INSTANCE);
  }

  @Override
  public SimpleExchange.Meta metaBean() {
    return SimpleExchange.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unique identifier of the exchange.
   * @return the value of the property
   */
  @Override
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the unique identifier of the exchange.
   * @param uniqueId  the new value of the property
   */
  @Override
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
   * Gets the bundle of external identifiers that define the exchange.
   * @return the value of the property, not null
   */
  @Override
  public ExternalIdBundle getExternalIdBundle() {
    return _externalIdBundle;
  }

  /**
   * Sets the bundle of external identifiers that define the exchange.
   * @param externalIdBundle  the new value of the property, not null
   */
  public void setExternalIdBundle(ExternalIdBundle externalIdBundle) {
    JodaBeanUtils.notNull(externalIdBundle, "externalIdBundle");
    this._externalIdBundle = externalIdBundle;
  }

  /**
   * Gets the the {@code externalIdBundle} property.
   * @return the property, not null
   */
  public final Property<ExternalIdBundle> externalIdBundle() {
    return metaBean().externalIdBundle().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the region external identifier bundle that defines where the exchange is located.
   * @return the value of the property
   */
  @Override
  public ExternalIdBundle getRegionIdBundle() {
    return _regionIdBundle;
  }

  /**
   * Sets the region external identifier bundle that defines where the exchange is located.
   * @param regionIdBundle  the new value of the property
   */
  public void setRegionIdBundle(ExternalIdBundle regionIdBundle) {
    this._regionIdBundle = regionIdBundle;
  }

  /**
   * Gets the the {@code regionIdBundle} property.
   * @return the property, not null
   */
  public final Property<ExternalIdBundle> regionIdBundle() {
    return metaBean().regionIdBundle().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the time-zone of the exchange.
   * @return the value of the property
   */
  @Override
  public ZoneId getTimeZone() {
    return _timeZone;
  }

  /**
   * Sets the time-zone of the exchange.
   * @param timeZone  the new value of the property
   */
  public void setTimeZone(ZoneId timeZone) {
    this._timeZone = timeZone;
  }

  /**
   * Gets the the {@code timeZone} property.
   * @return the property, not null
   */
  public final Property<ZoneId> timeZone() {
    return metaBean().timeZone().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name of the exchange intended for display purposes.
   * @return the value of the property, not null
   */
  @Override
  public String getName() {
    return _name;
  }

  /**
   * Sets the name of the exchange intended for display purposes.
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
  @Override
  public SimpleExchange clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SimpleExchange other = (SimpleExchange) obj;
      return JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          JodaBeanUtils.equal(getExternalIdBundle(), other.getExternalIdBundle()) &&
          JodaBeanUtils.equal(getRegionIdBundle(), other.getRegionIdBundle()) &&
          JodaBeanUtils.equal(getTimeZone(), other.getTimeZone()) &&
          JodaBeanUtils.equal(getName(), other.getName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExternalIdBundle());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegionIdBundle());
    hash = hash * 31 + JodaBeanUtils.hashCode(getTimeZone());
    hash = hash * 31 + JodaBeanUtils.hashCode(getName());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("SimpleExchange{");
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
    buf.append("externalIdBundle").append('=').append(JodaBeanUtils.toString(getExternalIdBundle())).append(',').append(' ');
    buf.append("regionIdBundle").append('=').append(JodaBeanUtils.toString(getRegionIdBundle())).append(',').append(' ');
    buf.append("timeZone").append('=').append(JodaBeanUtils.toString(getTimeZone())).append(',').append(' ');
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SimpleExchange}.
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
        this, "uniqueId", SimpleExchange.class, UniqueId.class);
    /**
     * The meta-property for the {@code externalIdBundle} property.
     */
    private final MetaProperty<ExternalIdBundle> _externalIdBundle = DirectMetaProperty.ofReadWrite(
        this, "externalIdBundle", SimpleExchange.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code regionIdBundle} property.
     */
    private final MetaProperty<ExternalIdBundle> _regionIdBundle = DirectMetaProperty.ofReadWrite(
        this, "regionIdBundle", SimpleExchange.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code timeZone} property.
     */
    private final MetaProperty<ZoneId> _timeZone = DirectMetaProperty.ofReadWrite(
        this, "timeZone", SimpleExchange.class, ZoneId.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", SimpleExchange.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "uniqueId",
        "externalIdBundle",
        "regionIdBundle",
        "timeZone",
        "name");

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
        case -736922008:  // externalIdBundle
          return _externalIdBundle;
        case 979697809:  // regionIdBundle
          return _regionIdBundle;
        case -2077180903:  // timeZone
          return _timeZone;
        case 3373707:  // name
          return _name;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SimpleExchange> builder() {
      return new DirectBeanBuilder<SimpleExchange>(new SimpleExchange());
    }

    @Override
    public Class<? extends SimpleExchange> beanType() {
      return SimpleExchange.class;
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
     * The meta-property for the {@code externalIdBundle} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalIdBundle> externalIdBundle() {
      return _externalIdBundle;
    }

    /**
     * The meta-property for the {@code regionIdBundle} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalIdBundle> regionIdBundle() {
      return _regionIdBundle;
    }

    /**
     * The meta-property for the {@code timeZone} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZoneId> timeZone() {
      return _timeZone;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return ((SimpleExchange) bean).getUniqueId();
        case -736922008:  // externalIdBundle
          return ((SimpleExchange) bean).getExternalIdBundle();
        case 979697809:  // regionIdBundle
          return ((SimpleExchange) bean).getRegionIdBundle();
        case -2077180903:  // timeZone
          return ((SimpleExchange) bean).getTimeZone();
        case 3373707:  // name
          return ((SimpleExchange) bean).getName();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          ((SimpleExchange) bean).setUniqueId((UniqueId) newValue);
          return;
        case -736922008:  // externalIdBundle
          ((SimpleExchange) bean).setExternalIdBundle((ExternalIdBundle) newValue);
          return;
        case 979697809:  // regionIdBundle
          ((SimpleExchange) bean).setRegionIdBundle((ExternalIdBundle) newValue);
          return;
        case -2077180903:  // timeZone
          ((SimpleExchange) bean).setTimeZone((ZoneId) newValue);
          return;
        case 3373707:  // name
          ((SimpleExchange) bean).setName((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((SimpleExchange) bean)._externalIdBundle, "externalIdBundle");
      JodaBeanUtils.notNull(((SimpleExchange) bean)._name, "name");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
