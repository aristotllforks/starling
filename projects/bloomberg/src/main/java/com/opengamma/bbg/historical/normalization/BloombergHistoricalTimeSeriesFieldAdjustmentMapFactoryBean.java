/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg.historical.normalization;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.bbg.referencedata.ReferenceDataProvider;
import com.opengamma.bbg.util.BloombergDataUtils;
import com.opengamma.master.historicaltimeseries.impl.HistoricalTimeSeriesFieldAdjustmentMap;
import com.opengamma.util.spring.SpringFactoryBean;

import net.sf.ehcache.CacheManager;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Spring factory bean for {@link HistoricalTimeSeriesFieldAdjustmentMap}.
 */
@BeanDefinition
public class BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean extends SpringFactoryBean<HistoricalTimeSeriesFieldAdjustmentMap> {

  /**
   * The reference data provider.
   */
  @PropertyDefinition(validate = "notNull")
  private ReferenceDataProvider _referenceDataProvider;
  /**
   * The cache manager.
   */
  @PropertyDefinition(validate = "notNull")
  private CacheManager _cacheManager;

  /**
   * Creates an instance.
   */
  public BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean() {
    super(HistoricalTimeSeriesFieldAdjustmentMap.class);
  }

  // -------------------------------------------------------------------------
  @Override
  protected HistoricalTimeSeriesFieldAdjustmentMap createObject() {
    return BloombergDataUtils.createFieldAdjustmentMap(getReferenceDataProvider(), getCacheManager());
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean}.
   * @return the meta-bean, not null
   */
  public static BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean.Meta meta() {
    return BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean.Meta.INSTANCE;
  }

  static {
    MetaBean.register(BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean.Meta.INSTANCE);
  }

  @Override
  public BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean.Meta metaBean() {
    return BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the reference data provider.
   * @return the value of the property, not null
   */
  public ReferenceDataProvider getReferenceDataProvider() {
    return _referenceDataProvider;
  }

  /**
   * Sets the reference data provider.
   * @param referenceDataProvider  the new value of the property, not null
   */
  public void setReferenceDataProvider(ReferenceDataProvider referenceDataProvider) {
    JodaBeanUtils.notNull(referenceDataProvider, "referenceDataProvider");
    this._referenceDataProvider = referenceDataProvider;
  }

  /**
   * Gets the the {@code referenceDataProvider} property.
   * @return the property, not null
   */
  public final Property<ReferenceDataProvider> referenceDataProvider() {
    return metaBean().referenceDataProvider().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cache manager.
   * @return the value of the property, not null
   */
  public CacheManager getCacheManager() {
    return _cacheManager;
  }

  /**
   * Sets the cache manager.
   * @param cacheManager  the new value of the property, not null
   */
  public void setCacheManager(CacheManager cacheManager) {
    JodaBeanUtils.notNull(cacheManager, "cacheManager");
    this._cacheManager = cacheManager;
  }

  /**
   * Gets the the {@code cacheManager} property.
   * @return the property, not null
   */
  public final Property<CacheManager> cacheManager() {
    return metaBean().cacheManager().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean other = (BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean) obj;
      return JodaBeanUtils.equal(getReferenceDataProvider(), other.getReferenceDataProvider()) &&
          JodaBeanUtils.equal(getCacheManager(), other.getCacheManager()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getReferenceDataProvider());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCacheManager());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean{");
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
    buf.append("referenceDataProvider").append('=').append(JodaBeanUtils.toString(getReferenceDataProvider())).append(',').append(' ');
    buf.append("cacheManager").append('=').append(JodaBeanUtils.toString(getCacheManager())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean}.
   */
  public static class Meta extends SpringFactoryBean.Meta<HistoricalTimeSeriesFieldAdjustmentMap> {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code referenceDataProvider} property.
     */
    private final MetaProperty<ReferenceDataProvider> _referenceDataProvider = DirectMetaProperty.ofReadWrite(
        this, "referenceDataProvider", BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean.class, ReferenceDataProvider.class);
    /**
     * The meta-property for the {@code cacheManager} property.
     */
    private final MetaProperty<CacheManager> _cacheManager = DirectMetaProperty.ofReadWrite(
        this, "cacheManager", BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean.class, CacheManager.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "referenceDataProvider",
        "cacheManager");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1788671322:  // referenceDataProvider
          return _referenceDataProvider;
        case -1452875317:  // cacheManager
          return _cacheManager;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean> builder() {
      return new DirectBeanBuilder<>(new BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean());
    }

    @Override
    public Class<? extends BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean> beanType() {
      return BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code referenceDataProvider} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ReferenceDataProvider> referenceDataProvider() {
      return _referenceDataProvider;
    }

    /**
     * The meta-property for the {@code cacheManager} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CacheManager> cacheManager() {
      return _cacheManager;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1788671322:  // referenceDataProvider
          return ((BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean) bean).getReferenceDataProvider();
        case -1452875317:  // cacheManager
          return ((BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean) bean).getCacheManager();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1788671322:  // referenceDataProvider
          ((BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean) bean).setReferenceDataProvider((ReferenceDataProvider) newValue);
          return;
        case -1452875317:  // cacheManager
          ((BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean) bean).setCacheManager((CacheManager) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean) bean)._referenceDataProvider, "referenceDataProvider");
      JodaBeanUtils.notNull(((BloombergHistoricalTimeSeriesFieldAdjustmentMapFactoryBean) bean)._cacheManager, "cacheManager");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
