/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.historicaltimeseries;

import java.util.Map;
import java.util.Set;

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
import org.threeten.bp.LocalDate;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.opengamma.id.ExternalId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.PublicSPI;

/**
 * Request to load one or more time-series.
 * <p>
 * This class is mutable and not thread-safe.
 */
@PublicSPI
@BeanDefinition
public class HistoricalTimeSeriesLoaderRequest extends DirectBean {

  /**
   * The set of time-series external identifiers to load.
   */
  @PropertyDefinition
  private final Set<ExternalId> _externalIds = Sets.newHashSet();
  /**
   * The data provider.
   */
  @PropertyDefinition
  private String _dataProvider;
  /**
   * The data field.
   */
  @PropertyDefinition
  private String _dataField;
  /**
   * The start date.
   */
  @PropertyDefinition
  private LocalDate _startDate;
  /**
   * The end date.
   */
  @PropertyDefinition
  private LocalDate _endDate;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance to load a multiple time-series.
   *
   * @param externalIds  the identifiers, not null
   * @param dataProvider  the data provider, null should default to a sensible value
   * @param dataField  the data field, not null
   * @param startDate  the start date of time-series, null should default to a sensible value
   * @param endDate  the end date of time-series, null should default to a sensible value
   * @return the map of external to unique identifier of loaded time-series, not null
   */
  public static HistoricalTimeSeriesLoaderRequest create(
      final Set<ExternalId> externalIds, final String dataProvider, final String dataField, final LocalDate startDate, final LocalDate endDate) {
    final HistoricalTimeSeriesLoaderRequest request = new HistoricalTimeSeriesLoaderRequest();
    request.addExternalIds(externalIds);
    request.setDataProvider(dataProvider);
    request.setDataField(dataField);
    request.setStartDate(startDate);
    request.setEndDate(endDate);
    return request;
  }

  //-------------------------------------------------------------------------
  /**
   * Creates an instance.
   */
  protected HistoricalTimeSeriesLoaderRequest() {
  }

  //-------------------------------------------------------------------------
  /**
   * Adds an array of historical time-series external identifiers to the collection to load.
   *
   * @param externalIds  the historical time-series identifiers to load, not null
   */
  public void addExternalIds(final ExternalId... externalIds) {
    ArgumentChecker.notNull(externalIds, "externalIds");
    for (final ExternalId externalId : externalIds) {
      getExternalIds().add(externalId);
    }
  }

  /**
   * Adds a collection of historical time-series external identifiers to the collection to load.
   *
   * @param externalIds  the historical time-series identifiers to load, not null
   */
  public void addExternalIds(final Iterable<ExternalId> externalIds) {
    ArgumentChecker.notNull(externalIds, "externalIds");
    Iterables.addAll(getExternalIds(), externalIds);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code HistoricalTimeSeriesLoaderRequest}.
   * @return the meta-bean, not null
   */
  public static HistoricalTimeSeriesLoaderRequest.Meta meta() {
    return HistoricalTimeSeriesLoaderRequest.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(HistoricalTimeSeriesLoaderRequest.Meta.INSTANCE);
  }

  @Override
  public HistoricalTimeSeriesLoaderRequest.Meta metaBean() {
    return HistoricalTimeSeriesLoaderRequest.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the set of time-series external identifiers to load.
   * @return the value of the property, not null
   */
  public Set<ExternalId> getExternalIds() {
    return _externalIds;
  }

  /**
   * Sets the set of time-series external identifiers to load.
   * @param externalIds  the new value of the property, not null
   */
  public void setExternalIds(Set<ExternalId> externalIds) {
    JodaBeanUtils.notNull(externalIds, "externalIds");
    this._externalIds.clear();
    this._externalIds.addAll(externalIds);
  }

  /**
   * Gets the the {@code externalIds} property.
   * @return the property, not null
   */
  public final Property<Set<ExternalId>> externalIds() {
    return metaBean().externalIds().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data provider.
   * @return the value of the property
   */
  public String getDataProvider() {
    return _dataProvider;
  }

  /**
   * Sets the data provider.
   * @param dataProvider  the new value of the property
   */
  public void setDataProvider(String dataProvider) {
    this._dataProvider = dataProvider;
  }

  /**
   * Gets the the {@code dataProvider} property.
   * @return the property, not null
   */
  public final Property<String> dataProvider() {
    return metaBean().dataProvider().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data field.
   * @return the value of the property
   */
  public String getDataField() {
    return _dataField;
  }

  /**
   * Sets the data field.
   * @param dataField  the new value of the property
   */
  public void setDataField(String dataField) {
    this._dataField = dataField;
  }

  /**
   * Gets the the {@code dataField} property.
   * @return the property, not null
   */
  public final Property<String> dataField() {
    return metaBean().dataField().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the start date.
   * @return the value of the property
   */
  public LocalDate getStartDate() {
    return _startDate;
  }

  /**
   * Sets the start date.
   * @param startDate  the new value of the property
   */
  public void setStartDate(LocalDate startDate) {
    this._startDate = startDate;
  }

  /**
   * Gets the the {@code startDate} property.
   * @return the property, not null
   */
  public final Property<LocalDate> startDate() {
    return metaBean().startDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the end date.
   * @return the value of the property
   */
  public LocalDate getEndDate() {
    return _endDate;
  }

  /**
   * Sets the end date.
   * @param endDate  the new value of the property
   */
  public void setEndDate(LocalDate endDate) {
    this._endDate = endDate;
  }

  /**
   * Gets the the {@code endDate} property.
   * @return the property, not null
   */
  public final Property<LocalDate> endDate() {
    return metaBean().endDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public HistoricalTimeSeriesLoaderRequest clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      HistoricalTimeSeriesLoaderRequest other = (HistoricalTimeSeriesLoaderRequest) obj;
      return JodaBeanUtils.equal(getExternalIds(), other.getExternalIds()) &&
          JodaBeanUtils.equal(getDataProvider(), other.getDataProvider()) &&
          JodaBeanUtils.equal(getDataField(), other.getDataField()) &&
          JodaBeanUtils.equal(getStartDate(), other.getStartDate()) &&
          JodaBeanUtils.equal(getEndDate(), other.getEndDate());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getExternalIds());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDataProvider());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDataField());
    hash = hash * 31 + JodaBeanUtils.hashCode(getStartDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getEndDate());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("HistoricalTimeSeriesLoaderRequest{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("externalIds").append('=').append(JodaBeanUtils.toString(getExternalIds())).append(',').append(' ');
    buf.append("dataProvider").append('=').append(JodaBeanUtils.toString(getDataProvider())).append(',').append(' ');
    buf.append("dataField").append('=').append(JodaBeanUtils.toString(getDataField())).append(',').append(' ');
    buf.append("startDate").append('=').append(JodaBeanUtils.toString(getStartDate())).append(',').append(' ');
    buf.append("endDate").append('=').append(JodaBeanUtils.toString(getEndDate())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code HistoricalTimeSeriesLoaderRequest}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code externalIds} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<ExternalId>> _externalIds = DirectMetaProperty.ofReadWrite(
        this, "externalIds", HistoricalTimeSeriesLoaderRequest.class, (Class) Set.class);
    /**
     * The meta-property for the {@code dataProvider} property.
     */
    private final MetaProperty<String> _dataProvider = DirectMetaProperty.ofReadWrite(
        this, "dataProvider", HistoricalTimeSeriesLoaderRequest.class, String.class);
    /**
     * The meta-property for the {@code dataField} property.
     */
    private final MetaProperty<String> _dataField = DirectMetaProperty.ofReadWrite(
        this, "dataField", HistoricalTimeSeriesLoaderRequest.class, String.class);
    /**
     * The meta-property for the {@code startDate} property.
     */
    private final MetaProperty<LocalDate> _startDate = DirectMetaProperty.ofReadWrite(
        this, "startDate", HistoricalTimeSeriesLoaderRequest.class, LocalDate.class);
    /**
     * The meta-property for the {@code endDate} property.
     */
    private final MetaProperty<LocalDate> _endDate = DirectMetaProperty.ofReadWrite(
        this, "endDate", HistoricalTimeSeriesLoaderRequest.class, LocalDate.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "externalIds",
        "dataProvider",
        "dataField",
        "startDate",
        "endDate");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1153096979:  // externalIds
          return _externalIds;
        case 339742651:  // dataProvider
          return _dataProvider;
        case -386794640:  // dataField
          return _dataField;
        case -2129778896:  // startDate
          return _startDate;
        case -1607727319:  // endDate
          return _endDate;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends HistoricalTimeSeriesLoaderRequest> builder() {
      return new DirectBeanBuilder<HistoricalTimeSeriesLoaderRequest>(new HistoricalTimeSeriesLoaderRequest());
    }

    @Override
    public Class<? extends HistoricalTimeSeriesLoaderRequest> beanType() {
      return HistoricalTimeSeriesLoaderRequest.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code externalIds} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Set<ExternalId>> externalIds() {
      return _externalIds;
    }

    /**
     * The meta-property for the {@code dataProvider} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> dataProvider() {
      return _dataProvider;
    }

    /**
     * The meta-property for the {@code dataField} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> dataField() {
      return _dataField;
    }

    /**
     * The meta-property for the {@code startDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> startDate() {
      return _startDate;
    }

    /**
     * The meta-property for the {@code endDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> endDate() {
      return _endDate;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1153096979:  // externalIds
          return ((HistoricalTimeSeriesLoaderRequest) bean).getExternalIds();
        case 339742651:  // dataProvider
          return ((HistoricalTimeSeriesLoaderRequest) bean).getDataProvider();
        case -386794640:  // dataField
          return ((HistoricalTimeSeriesLoaderRequest) bean).getDataField();
        case -2129778896:  // startDate
          return ((HistoricalTimeSeriesLoaderRequest) bean).getStartDate();
        case -1607727319:  // endDate
          return ((HistoricalTimeSeriesLoaderRequest) bean).getEndDate();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1153096979:  // externalIds
          ((HistoricalTimeSeriesLoaderRequest) bean).setExternalIds((Set<ExternalId>) newValue);
          return;
        case 339742651:  // dataProvider
          ((HistoricalTimeSeriesLoaderRequest) bean).setDataProvider((String) newValue);
          return;
        case -386794640:  // dataField
          ((HistoricalTimeSeriesLoaderRequest) bean).setDataField((String) newValue);
          return;
        case -2129778896:  // startDate
          ((HistoricalTimeSeriesLoaderRequest) bean).setStartDate((LocalDate) newValue);
          return;
        case -1607727319:  // endDate
          ((HistoricalTimeSeriesLoaderRequest) bean).setEndDate((LocalDate) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((HistoricalTimeSeriesLoaderRequest) bean)._externalIds, "externalIds");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
