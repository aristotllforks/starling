/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.exchange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.ZoneId;

import com.opengamma.core.exchange.Exchange;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.UniqueId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.PublicSPI;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * An exchange on which financial products can be traded or settled.
 * <p>
 * Financial products are often traded at a specific location known as an exchange. This class represents details of the exchange, including region and opening
 * hours.
 */
@PublicSPI
@BeanDefinition
public class ManageableExchange extends DirectBean implements Exchange, Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The unique identifier of the exchange. This must be null when adding to a master and not null when retrieved from a master.
   */
  @PropertyDefinition(overrideGet = true)
  private UniqueId _uniqueId;
  /**
   * The bundle of external identifiers that define the exchange. This field must not be null for the object to be valid.
   */
  @PropertyDefinition(overrideGet = true, validate = "notNull")
  private ExternalIdBundle _externalIdBundle = ExternalIdBundle.EMPTY;
  /**
   * The name of the exchange intended for display purposes. This field must not be null for the object to be valid.
   */
  @PropertyDefinition(overrideGet = true, validate = "notNull")
  private String _name;
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
   * The detailed information about when an exchange is open or closed, not null.
   */
  @PropertyDefinition
  private final List<ManageableExchangeDetail> _detail = new ArrayList<>();

  /**
   * Creates an exchange.
   */
  public ManageableExchange() {
  }

  /**
   * Creates an exchange specifying the values of the main fields.
   *
   * @param identifiers
   *          the bundle of identifiers that define the exchange, not null
   * @param name
   *          the name of the exchange, for display purposes, not null
   * @param regionBundle
   *          the region external identifier bundle where the exchange is located, null if not applicable (dark pool, electronic, ...)
   * @param timeZone
   *          the time-zone, may be null
   */
  public ManageableExchange(final ExternalIdBundle identifiers, final String name, final ExternalIdBundle regionBundle, final ZoneId timeZone) {
    ArgumentChecker.notNull(identifiers, "identifiers");
    ArgumentChecker.notNull(name, "name");
    setExternalIdBundle(identifiers);
    setName(name);
    setRegionIdBundle(regionBundle);
    setTimeZone(timeZone);
  }

  /**
   * Returns an independent clone of this exchange.
   *
   * @return the clone, not null
   */
  @Override
  public ManageableExchange clone() {
    final ManageableExchange cloned = new ManageableExchange();
    cloned._uniqueId = _uniqueId;
    cloned._name = _name;
    cloned._externalIdBundle = _externalIdBundle;
    cloned._regionIdBundle = _regionIdBundle;
    cloned._detail.addAll(_detail);
    cloned._timeZone = _timeZone;
    return cloned;
  }

  // -------------------------------------------------------------------------
  /**
   * Adds an external identifier to the bundle representing this exchange.
   *
   * @param exchangeId
   *          the identifier to add, not null
   */
  public void addExternalId(final ExternalId exchangeId) {
    setExternalIdBundle(getExternalIdBundle().withExternalId(exchangeId));
  }

  // -------------------------------------------------------------------------
  /**
   * Gets the ISO MIC code.
   *
   * @return the value of the property
   */
  public String getISOMic() {
    return _externalIdBundle.getValue(ExternalSchemes.ISO_MIC);
  }

  /**
   * Sets the ISO MIC code, stored in the identifier set.
   *
   * @param isoMicCode
   *          the exchange MIC to set, null to remove any defined ISO MIC
   */
  public void setISOMic(final String isoMicCode) {
    setExternalIdBundle(getExternalIdBundle().withoutScheme(ExternalSchemes.ISO_MIC));
    if (isoMicCode != null) {
      addExternalId(ExternalSchemes.isoMicExchangeId(isoMicCode));
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code ManageableExchange}.
   * @return the meta-bean, not null
   */
  public static ManageableExchange.Meta meta() {
    return ManageableExchange.Meta.INSTANCE;
  }

  static {
    MetaBean.register(ManageableExchange.Meta.INSTANCE);
  }

  @Override
  public ManageableExchange.Meta metaBean() {
    return ManageableExchange.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unique identifier of the exchange. This must be null when adding to a master and not null when retrieved from a master.
   * @return the value of the property
   */
  @Override
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the unique identifier of the exchange. This must be null when adding to a master and not null when retrieved from a master.
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
   * Gets the bundle of external identifiers that define the exchange. This field must not be null for the object to be valid.
   * @return the value of the property, not null
   */
  @Override
  public ExternalIdBundle getExternalIdBundle() {
    return _externalIdBundle;
  }

  /**
   * Sets the bundle of external identifiers that define the exchange. This field must not be null for the object to be valid.
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
   * Gets the name of the exchange intended for display purposes. This field must not be null for the object to be valid.
   * @return the value of the property, not null
   */
  @Override
  public String getName() {
    return _name;
  }

  /**
   * Sets the name of the exchange intended for display purposes. This field must not be null for the object to be valid.
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
   * Gets the detailed information about when an exchange is open or closed, not null.
   * @return the value of the property, not null
   */
  public List<ManageableExchangeDetail> getDetail() {
    return _detail;
  }

  /**
   * Sets the detailed information about when an exchange is open or closed, not null.
   * @param detail  the new value of the property, not null
   */
  public void setDetail(List<ManageableExchangeDetail> detail) {
    JodaBeanUtils.notNull(detail, "detail");
    this._detail.clear();
    this._detail.addAll(detail);
  }

  /**
   * Gets the the {@code detail} property.
   * @return the property, not null
   */
  public final Property<List<ManageableExchangeDetail>> detail() {
    return metaBean().detail().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ManageableExchange other = (ManageableExchange) obj;
      return JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          JodaBeanUtils.equal(getExternalIdBundle(), other.getExternalIdBundle()) &&
          JodaBeanUtils.equal(getName(), other.getName()) &&
          JodaBeanUtils.equal(getRegionIdBundle(), other.getRegionIdBundle()) &&
          JodaBeanUtils.equal(getTimeZone(), other.getTimeZone()) &&
          JodaBeanUtils.equal(getDetail(), other.getDetail());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExternalIdBundle());
    hash = hash * 31 + JodaBeanUtils.hashCode(getName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegionIdBundle());
    hash = hash * 31 + JodaBeanUtils.hashCode(getTimeZone());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDetail());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(224);
    buf.append("ManageableExchange{");
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
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
    buf.append("regionIdBundle").append('=').append(JodaBeanUtils.toString(getRegionIdBundle())).append(',').append(' ');
    buf.append("timeZone").append('=').append(JodaBeanUtils.toString(getTimeZone())).append(',').append(' ');
    buf.append("detail").append('=').append(JodaBeanUtils.toString(getDetail())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ManageableExchange}.
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
        this, "uniqueId", ManageableExchange.class, UniqueId.class);
    /**
     * The meta-property for the {@code externalIdBundle} property.
     */
    private final MetaProperty<ExternalIdBundle> _externalIdBundle = DirectMetaProperty.ofReadWrite(
        this, "externalIdBundle", ManageableExchange.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", ManageableExchange.class, String.class);
    /**
     * The meta-property for the {@code regionIdBundle} property.
     */
    private final MetaProperty<ExternalIdBundle> _regionIdBundle = DirectMetaProperty.ofReadWrite(
        this, "regionIdBundle", ManageableExchange.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code timeZone} property.
     */
    private final MetaProperty<ZoneId> _timeZone = DirectMetaProperty.ofReadWrite(
        this, "timeZone", ManageableExchange.class, ZoneId.class);
    /**
     * The meta-property for the {@code detail} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<ManageableExchangeDetail>> _detail = DirectMetaProperty.ofReadWrite(
        this, "detail", ManageableExchange.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "uniqueId",
        "externalIdBundle",
        "name",
        "regionIdBundle",
        "timeZone",
        "detail");

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
        case 3373707:  // name
          return _name;
        case 979697809:  // regionIdBundle
          return _regionIdBundle;
        case -2077180903:  // timeZone
          return _timeZone;
        case -1335224239:  // detail
          return _detail;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ManageableExchange> builder() {
      return new DirectBeanBuilder<>(new ManageableExchange());
    }

    @Override
    public Class<? extends ManageableExchange> beanType() {
      return ManageableExchange.class;
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
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
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
     * The meta-property for the {@code detail} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<ManageableExchangeDetail>> detail() {
      return _detail;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return ((ManageableExchange) bean).getUniqueId();
        case -736922008:  // externalIdBundle
          return ((ManageableExchange) bean).getExternalIdBundle();
        case 3373707:  // name
          return ((ManageableExchange) bean).getName();
        case 979697809:  // regionIdBundle
          return ((ManageableExchange) bean).getRegionIdBundle();
        case -2077180903:  // timeZone
          return ((ManageableExchange) bean).getTimeZone();
        case -1335224239:  // detail
          return ((ManageableExchange) bean).getDetail();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          ((ManageableExchange) bean).setUniqueId((UniqueId) newValue);
          return;
        case -736922008:  // externalIdBundle
          ((ManageableExchange) bean).setExternalIdBundle((ExternalIdBundle) newValue);
          return;
        case 3373707:  // name
          ((ManageableExchange) bean).setName((String) newValue);
          return;
        case 979697809:  // regionIdBundle
          ((ManageableExchange) bean).setRegionIdBundle((ExternalIdBundle) newValue);
          return;
        case -2077180903:  // timeZone
          ((ManageableExchange) bean).setTimeZone((ZoneId) newValue);
          return;
        case -1335224239:  // detail
          ((ManageableExchange) bean).setDetail((List<ManageableExchangeDetail>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((ManageableExchange) bean)._externalIdBundle, "externalIdBundle");
      JodaBeanUtils.notNull(((ManageableExchange) bean)._name, "name");
      JodaBeanUtils.notNull(((ManageableExchange) bean)._detail, "detail");
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
