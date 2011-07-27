/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.historicaltimeseries;

import java.io.Serializable;
import java.util.Map;

import javax.time.Instant;
import javax.time.calendar.LocalDate;

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

import com.opengamma.core.historicaltimeseries.HistoricalTimeSeries;
import com.opengamma.id.MutableUniqueIdentifiable;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.PublicSPI;
import com.opengamma.util.timeseries.localdate.LocalDateDoubleTimeSeries;

/**
 * A time-series as stored in a master.
 * <p>
 * The time-series is stored separately from the information describing it.
 * See {@link ManageableHistoricalTimeSeriesInfo}.
 * <p>
 * This class is mutable and not thread-safe.
 */
@PublicSPI
@BeanDefinition
public class ManageableHistoricalTimeSeries extends DirectBean
    implements HistoricalTimeSeries, UniqueIdentifiable, MutableUniqueIdentifiable, Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The historical time-series unique identifier.
   * This field is managed by the master but must be set for updates.
   */
  @PropertyDefinition
  private UniqueIdentifier _uniqueId;
  /**
   * The instant that this version was created.
   */
  @PropertyDefinition
  private Instant _versionInstant;
  /**
   * The instant that this version was corrected at.
   */
  @PropertyDefinition
  private Instant _correctionInstant;
  /**
   * The time-series.
   * This field is only returned if requested from the master, and not all
   * points are necessarily returned.
   */
  @PropertyDefinition
  private LocalDateDoubleTimeSeries _timeSeries;
  /**
   * The earliest date of time-series.
   * This field is only returned if requested from the master.
   */
  @PropertyDefinition
  private LocalDate _earliest;
  /**
   * The latest date of time-series.
   * This field is only returned if requested from the master.
   */
  @PropertyDefinition
  private LocalDate _latest; 

  /**
   * Creates an instance.
   */
  public ManageableHistoricalTimeSeries() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ManageableHistoricalTimeSeries}.
   * @return the meta-bean, not null
   */
  public static ManageableHistoricalTimeSeries.Meta meta() {
    return ManageableHistoricalTimeSeries.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(ManageableHistoricalTimeSeries.Meta.INSTANCE);
  }

  @Override
  public ManageableHistoricalTimeSeries.Meta metaBean() {
    return ManageableHistoricalTimeSeries.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -294460212:  // uniqueId
        return getUniqueId();
      case 2084044265:  // versionInstant
        return getVersionInstant();
      case 434256035:  // correctionInstant
        return getCorrectionInstant();
      case 779431844:  // timeSeries
        return getTimeSeries();
      case -809579181:  // earliest
        return getEarliest();
      case -1109880953:  // latest
        return getLatest();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -294460212:  // uniqueId
        setUniqueId((UniqueIdentifier) newValue);
        return;
      case 2084044265:  // versionInstant
        setVersionInstant((Instant) newValue);
        return;
      case 434256035:  // correctionInstant
        setCorrectionInstant((Instant) newValue);
        return;
      case 779431844:  // timeSeries
        setTimeSeries((LocalDateDoubleTimeSeries) newValue);
        return;
      case -809579181:  // earliest
        setEarliest((LocalDate) newValue);
        return;
      case -1109880953:  // latest
        setLatest((LocalDate) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ManageableHistoricalTimeSeries other = (ManageableHistoricalTimeSeries) obj;
      return JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          JodaBeanUtils.equal(getVersionInstant(), other.getVersionInstant()) &&
          JodaBeanUtils.equal(getCorrectionInstant(), other.getCorrectionInstant()) &&
          JodaBeanUtils.equal(getTimeSeries(), other.getTimeSeries()) &&
          JodaBeanUtils.equal(getEarliest(), other.getEarliest()) &&
          JodaBeanUtils.equal(getLatest(), other.getLatest());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    hash += hash * 31 + JodaBeanUtils.hashCode(getVersionInstant());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCorrectionInstant());
    hash += hash * 31 + JodaBeanUtils.hashCode(getTimeSeries());
    hash += hash * 31 + JodaBeanUtils.hashCode(getEarliest());
    hash += hash * 31 + JodaBeanUtils.hashCode(getLatest());
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the historical time-series unique identifier.
   * This field is managed by the master but must be set for updates.
   * @return the value of the property
   */
  public UniqueIdentifier getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the historical time-series unique identifier.
   * This field is managed by the master but must be set for updates.
   * @param uniqueId  the new value of the property
   */
  public void setUniqueId(UniqueIdentifier uniqueId) {
    this._uniqueId = uniqueId;
  }

  /**
   * Gets the the {@code uniqueId} property.
   * This field is managed by the master but must be set for updates.
   * @return the property, not null
   */
  public final Property<UniqueIdentifier> uniqueId() {
    return metaBean().uniqueId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instant that this version was created.
   * @return the value of the property
   */
  public Instant getVersionInstant() {
    return _versionInstant;
  }

  /**
   * Sets the instant that this version was created.
   * @param versionInstant  the new value of the property
   */
  public void setVersionInstant(Instant versionInstant) {
    this._versionInstant = versionInstant;
  }

  /**
   * Gets the the {@code versionInstant} property.
   * @return the property, not null
   */
  public final Property<Instant> versionInstant() {
    return metaBean().versionInstant().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instant that this version was corrected at.
   * @return the value of the property
   */
  public Instant getCorrectionInstant() {
    return _correctionInstant;
  }

  /**
   * Sets the instant that this version was corrected at.
   * @param correctionInstant  the new value of the property
   */
  public void setCorrectionInstant(Instant correctionInstant) {
    this._correctionInstant = correctionInstant;
  }

  /**
   * Gets the the {@code correctionInstant} property.
   * @return the property, not null
   */
  public final Property<Instant> correctionInstant() {
    return metaBean().correctionInstant().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the time-series.
   * This field is only returned if requested from the master, and not all
   * points are necessarily returned.
   * @return the value of the property
   */
  public LocalDateDoubleTimeSeries getTimeSeries() {
    return _timeSeries;
  }

  /**
   * Sets the time-series.
   * This field is only returned if requested from the master, and not all
   * points are necessarily returned.
   * @param timeSeries  the new value of the property
   */
  public void setTimeSeries(LocalDateDoubleTimeSeries timeSeries) {
    this._timeSeries = timeSeries;
  }

  /**
   * Gets the the {@code timeSeries} property.
   * This field is only returned if requested from the master, and not all
   * points are necessarily returned.
   * @return the property, not null
   */
  public final Property<LocalDateDoubleTimeSeries> timeSeries() {
    return metaBean().timeSeries().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the earliest date of time-series.
   * This field is only returned if requested from the master.
   * @return the value of the property
   */
  public LocalDate getEarliest() {
    return _earliest;
  }

  /**
   * Sets the earliest date of time-series.
   * This field is only returned if requested from the master.
   * @param earliest  the new value of the property
   */
  public void setEarliest(LocalDate earliest) {
    this._earliest = earliest;
  }

  /**
   * Gets the the {@code earliest} property.
   * This field is only returned if requested from the master.
   * @return the property, not null
   */
  public final Property<LocalDate> earliest() {
    return metaBean().earliest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the latest date of time-series.
   * This field is only returned if requested from the master.
   * @return the value of the property
   */
  public LocalDate getLatest() {
    return _latest;
  }

  /**
   * Sets the latest date of time-series.
   * This field is only returned if requested from the master.
   * @param latest  the new value of the property
   */
  public void setLatest(LocalDate latest) {
    this._latest = latest;
  }

  /**
   * Gets the the {@code latest} property.
   * This field is only returned if requested from the master.
   * @return the property, not null
   */
  public final Property<LocalDate> latest() {
    return metaBean().latest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ManageableHistoricalTimeSeries}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueIdentifier> _uniqueId = DirectMetaProperty.ofReadWrite(
        this, "uniqueId", ManageableHistoricalTimeSeries.class, UniqueIdentifier.class);
    /**
     * The meta-property for the {@code versionInstant} property.
     */
    private final MetaProperty<Instant> _versionInstant = DirectMetaProperty.ofReadWrite(
        this, "versionInstant", ManageableHistoricalTimeSeries.class, Instant.class);
    /**
     * The meta-property for the {@code correctionInstant} property.
     */
    private final MetaProperty<Instant> _correctionInstant = DirectMetaProperty.ofReadWrite(
        this, "correctionInstant", ManageableHistoricalTimeSeries.class, Instant.class);
    /**
     * The meta-property for the {@code timeSeries} property.
     */
    private final MetaProperty<LocalDateDoubleTimeSeries> _timeSeries = DirectMetaProperty.ofReadWrite(
        this, "timeSeries", ManageableHistoricalTimeSeries.class, LocalDateDoubleTimeSeries.class);
    /**
     * The meta-property for the {@code earliest} property.
     */
    private final MetaProperty<LocalDate> _earliest = DirectMetaProperty.ofReadWrite(
        this, "earliest", ManageableHistoricalTimeSeries.class, LocalDate.class);
    /**
     * The meta-property for the {@code latest} property.
     */
    private final MetaProperty<LocalDate> _latest = DirectMetaProperty.ofReadWrite(
        this, "latest", ManageableHistoricalTimeSeries.class, LocalDate.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
        this, null,
        "uniqueId",
        "versionInstant",
        "correctionInstant",
        "timeSeries",
        "earliest",
        "latest");

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
        case 2084044265:  // versionInstant
          return _versionInstant;
        case 434256035:  // correctionInstant
          return _correctionInstant;
        case 779431844:  // timeSeries
          return _timeSeries;
        case -809579181:  // earliest
          return _earliest;
        case -1109880953:  // latest
          return _latest;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ManageableHistoricalTimeSeries> builder() {
      return new DirectBeanBuilder<ManageableHistoricalTimeSeries>(new ManageableHistoricalTimeSeries());
    }

    @Override
    public Class<? extends ManageableHistoricalTimeSeries> beanType() {
      return ManageableHistoricalTimeSeries.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code uniqueId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueIdentifier> uniqueId() {
      return _uniqueId;
    }

    /**
     * The meta-property for the {@code versionInstant} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Instant> versionInstant() {
      return _versionInstant;
    }

    /**
     * The meta-property for the {@code correctionInstant} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Instant> correctionInstant() {
      return _correctionInstant;
    }

    /**
     * The meta-property for the {@code timeSeries} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDateDoubleTimeSeries> timeSeries() {
      return _timeSeries;
    }

    /**
     * The meta-property for the {@code earliest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> earliest() {
      return _earliest;
    }

    /**
     * The meta-property for the {@code latest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> latest() {
      return _latest;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
