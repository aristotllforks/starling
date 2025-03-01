/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.historicaltimeseries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.master.AbstractMetaDataResult;
import com.opengamma.util.PublicSPI;

/**
 * Result from obtaining meta-data for the time-series master.
 * <p>
 * Meta-data is only returned if requested.
 */
@PublicSPI
@BeanDefinition
public class HistoricalTimeSeriesInfoMetaDataResult extends AbstractMetaDataResult {

  /**
   * The list of valid data fields. This is only populated if requested.
   */
  @PropertyDefinition
  private final List<String> _dataFields = new ArrayList<>();
  /**
   * The list of valid data sources. This is only populated if requested.
   */
  @PropertyDefinition
  private final List<String> _dataSources = new ArrayList<>();
  /**
   * The list of valid data providers. This is only populated if requested.
   */
  @PropertyDefinition
  private final List<String> _dataProviders = new ArrayList<>();
  /**
   * The list of valid observation times. This is only populated if requested.
   */
  @PropertyDefinition
  private final List<String> _observationTimes = new ArrayList<>();

  /**
   * Creates an instance.
   */
  public HistoricalTimeSeriesInfoMetaDataResult() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code HistoricalTimeSeriesInfoMetaDataResult}.
   * @return the meta-bean, not null
   */
  public static HistoricalTimeSeriesInfoMetaDataResult.Meta meta() {
    return HistoricalTimeSeriesInfoMetaDataResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(HistoricalTimeSeriesInfoMetaDataResult.Meta.INSTANCE);
  }

  @Override
  public HistoricalTimeSeriesInfoMetaDataResult.Meta metaBean() {
    return HistoricalTimeSeriesInfoMetaDataResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the list of valid data fields. This is only populated if requested.
   * @return the value of the property, not null
   */
  public List<String> getDataFields() {
    return _dataFields;
  }

  /**
   * Sets the list of valid data fields. This is only populated if requested.
   * @param dataFields  the new value of the property, not null
   */
  public void setDataFields(List<String> dataFields) {
    JodaBeanUtils.notNull(dataFields, "dataFields");
    this._dataFields.clear();
    this._dataFields.addAll(dataFields);
  }

  /**
   * Gets the the {@code dataFields} property.
   * @return the property, not null
   */
  public final Property<List<String>> dataFields() {
    return metaBean().dataFields().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the list of valid data sources. This is only populated if requested.
   * @return the value of the property, not null
   */
  public List<String> getDataSources() {
    return _dataSources;
  }

  /**
   * Sets the list of valid data sources. This is only populated if requested.
   * @param dataSources  the new value of the property, not null
   */
  public void setDataSources(List<String> dataSources) {
    JodaBeanUtils.notNull(dataSources, "dataSources");
    this._dataSources.clear();
    this._dataSources.addAll(dataSources);
  }

  /**
   * Gets the the {@code dataSources} property.
   * @return the property, not null
   */
  public final Property<List<String>> dataSources() {
    return metaBean().dataSources().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the list of valid data providers. This is only populated if requested.
   * @return the value of the property, not null
   */
  public List<String> getDataProviders() {
    return _dataProviders;
  }

  /**
   * Sets the list of valid data providers. This is only populated if requested.
   * @param dataProviders  the new value of the property, not null
   */
  public void setDataProviders(List<String> dataProviders) {
    JodaBeanUtils.notNull(dataProviders, "dataProviders");
    this._dataProviders.clear();
    this._dataProviders.addAll(dataProviders);
  }

  /**
   * Gets the the {@code dataProviders} property.
   * @return the property, not null
   */
  public final Property<List<String>> dataProviders() {
    return metaBean().dataProviders().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the list of valid observation times. This is only populated if requested.
   * @return the value of the property, not null
   */
  public List<String> getObservationTimes() {
    return _observationTimes;
  }

  /**
   * Sets the list of valid observation times. This is only populated if requested.
   * @param observationTimes  the new value of the property, not null
   */
  public void setObservationTimes(List<String> observationTimes) {
    JodaBeanUtils.notNull(observationTimes, "observationTimes");
    this._observationTimes.clear();
    this._observationTimes.addAll(observationTimes);
  }

  /**
   * Gets the the {@code observationTimes} property.
   * @return the property, not null
   */
  public final Property<List<String>> observationTimes() {
    return metaBean().observationTimes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public HistoricalTimeSeriesInfoMetaDataResult clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      HistoricalTimeSeriesInfoMetaDataResult other = (HistoricalTimeSeriesInfoMetaDataResult) obj;
      return JodaBeanUtils.equal(getDataFields(), other.getDataFields()) &&
          JodaBeanUtils.equal(getDataSources(), other.getDataSources()) &&
          JodaBeanUtils.equal(getDataProviders(), other.getDataProviders()) &&
          JodaBeanUtils.equal(getObservationTimes(), other.getObservationTimes()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getDataFields());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDataSources());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDataProviders());
    hash = hash * 31 + JodaBeanUtils.hashCode(getObservationTimes());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("HistoricalTimeSeriesInfoMetaDataResult{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
    buf.append("dataFields").append('=').append(JodaBeanUtils.toString(getDataFields())).append(',').append(' ');
    buf.append("dataSources").append('=').append(JodaBeanUtils.toString(getDataSources())).append(',').append(' ');
    buf.append("dataProviders").append('=').append(JodaBeanUtils.toString(getDataProviders())).append(',').append(' ');
    buf.append("observationTimes").append('=').append(JodaBeanUtils.toString(getObservationTimes())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code HistoricalTimeSeriesInfoMetaDataResult}.
   */
  public static class Meta extends AbstractMetaDataResult.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code dataFields} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<String>> _dataFields = DirectMetaProperty.ofReadWrite(
        this, "dataFields", HistoricalTimeSeriesInfoMetaDataResult.class, (Class) List.class);
    /**
     * The meta-property for the {@code dataSources} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<String>> _dataSources = DirectMetaProperty.ofReadWrite(
        this, "dataSources", HistoricalTimeSeriesInfoMetaDataResult.class, (Class) List.class);
    /**
     * The meta-property for the {@code dataProviders} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<String>> _dataProviders = DirectMetaProperty.ofReadWrite(
        this, "dataProviders", HistoricalTimeSeriesInfoMetaDataResult.class, (Class) List.class);
    /**
     * The meta-property for the {@code observationTimes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<String>> _observationTimes = DirectMetaProperty.ofReadWrite(
        this, "observationTimes", HistoricalTimeSeriesInfoMetaDataResult.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "dataFields",
        "dataSources",
        "dataProviders",
        "observationTimes");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 894268163:  // dataFields
          return _dataFields;
        case 791883950:  // dataSources
          return _dataSources;
        case 1942087704:  // dataProviders
          return _dataProviders;
        case -576554374:  // observationTimes
          return _observationTimes;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends HistoricalTimeSeriesInfoMetaDataResult> builder() {
      return new DirectBeanBuilder<HistoricalTimeSeriesInfoMetaDataResult>(new HistoricalTimeSeriesInfoMetaDataResult());
    }

    @Override
    public Class<? extends HistoricalTimeSeriesInfoMetaDataResult> beanType() {
      return HistoricalTimeSeriesInfoMetaDataResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code dataFields} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<String>> dataFields() {
      return _dataFields;
    }

    /**
     * The meta-property for the {@code dataSources} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<String>> dataSources() {
      return _dataSources;
    }

    /**
     * The meta-property for the {@code dataProviders} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<String>> dataProviders() {
      return _dataProviders;
    }

    /**
     * The meta-property for the {@code observationTimes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<String>> observationTimes() {
      return _observationTimes;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 894268163:  // dataFields
          return ((HistoricalTimeSeriesInfoMetaDataResult) bean).getDataFields();
        case 791883950:  // dataSources
          return ((HistoricalTimeSeriesInfoMetaDataResult) bean).getDataSources();
        case 1942087704:  // dataProviders
          return ((HistoricalTimeSeriesInfoMetaDataResult) bean).getDataProviders();
        case -576554374:  // observationTimes
          return ((HistoricalTimeSeriesInfoMetaDataResult) bean).getObservationTimes();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 894268163:  // dataFields
          ((HistoricalTimeSeriesInfoMetaDataResult) bean).setDataFields((List<String>) newValue);
          return;
        case 791883950:  // dataSources
          ((HistoricalTimeSeriesInfoMetaDataResult) bean).setDataSources((List<String>) newValue);
          return;
        case 1942087704:  // dataProviders
          ((HistoricalTimeSeriesInfoMetaDataResult) bean).setDataProviders((List<String>) newValue);
          return;
        case -576554374:  // observationTimes
          ((HistoricalTimeSeriesInfoMetaDataResult) bean).setObservationTimes((List<String>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((HistoricalTimeSeriesInfoMetaDataResult) bean)._dataFields, "dataFields");
      JodaBeanUtils.notNull(((HistoricalTimeSeriesInfoMetaDataResult) bean)._dataSources, "dataSources");
      JodaBeanUtils.notNull(((HistoricalTimeSeriesInfoMetaDataResult) bean)._dataProviders, "dataProviders");
      JodaBeanUtils.notNull(((HistoricalTimeSeriesInfoMetaDataResult) bean)._observationTimes, "observationTimes");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
