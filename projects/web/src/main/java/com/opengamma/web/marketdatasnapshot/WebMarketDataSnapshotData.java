/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.marketdatasnapshot;

import java.util.Map;

import javax.ws.rs.core.UriInfo;

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

import com.opengamma.core.config.ConfigSource;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeriesSource;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.engine.marketdata.NamedMarketDataSpecificationRepository;
import com.opengamma.engine.marketdata.live.LiveMarketDataProviderFactory;
import com.opengamma.engine.view.ViewProcessor;
import com.opengamma.financial.analytics.volatility.cube.VolatilityCubeDefinitionSource;
import com.opengamma.id.UniqueId;
import com.opengamma.master.config.ConfigMaster;
import com.opengamma.master.marketdatasnapshot.MarketDataSnapshotDocument;
import com.opengamma.master.marketdatasnapshot.MarketDataSnapshotMaster;
import com.opengamma.web.WebPerRequestData;

/**
 * Data class for web-based market data snapshot management.
 */
@SuppressWarnings("deprecation")
@BeanDefinition
public class WebMarketDataSnapshotData extends WebPerRequestData {
  /**
   * For obtaining the live market data provider names. Either this or marketDataSpecificationRepository must be set.
   */
  @PropertyDefinition
  private LiveMarketDataProviderFactory _liveMarketDataProviderFactory;
  /**
   * For looking up market data provider specifications by name. Either this or liveMarketDataProviderFactory must be set.
   *
   * @deprecated use liveMarketDataProviderFactory
   */
  @PropertyDefinition
  @Deprecated
  private NamedMarketDataSpecificationRepository _marketDataSpecificationRepository;
  /**
   * The market data snapshot master.
   */
  @PropertyDefinition
  private MarketDataSnapshotMaster _marketDataSnapshotMaster;
  /**
   * The config master.
   */
  @PropertyDefinition
  private ConfigMaster _configMaster;
  /**
   * The view processor.
   */
  @PropertyDefinition
  private ViewProcessor _viewProcessor;
  /**
   * The computation target resolver.
   */
  @PropertyDefinition
  private ComputationTargetResolver _computationTargetResolver;
  /**
   * The hts source, may be null.
   */
  @PropertyDefinition
  private HistoricalTimeSeriesSource _historicalTimeSeriesSource;
  /**
   * The snapshot id from the input URI.
   */
  @PropertyDefinition
  private String _uriSnapshotId;
  /**
   * The snapshot version id from the URI.
   */
  @PropertyDefinition
  private String _uriVersionId;
  /**
   * The market data snapshot.
   */
  @PropertyDefinition
  private MarketDataSnapshotDocument _snapshot;
  /**
   * The config source.
   */
  @PropertyDefinition
  private ConfigSource _configSource;
  /**
   * The volatility (for market data snapshots).
   */
  @PropertyDefinition
  private VolatilityCubeDefinitionSource _volatilityCubeDefinitionSource;
  /**
   * The versioned market data snapshot.
   */
  @PropertyDefinition
  private MarketDataSnapshotDocument _versioned;

  /**
   * Creates an instance.
   */
  public WebMarketDataSnapshotData() {
  }

  /**
   * Creates an instance.
   * 
   * @param uriInfo
   *          the URI information
   */
  public WebMarketDataSnapshotData(final UriInfo uriInfo) {
    setUriInfo(uriInfo);
  }

  // -------------------------------------------------------------------------
  /**
   * Gets the best available snapshot id.
   * 
   * @param overrideId
   *          the override id, null derives the result from the data
   * @return the id, may be null
   */
  public String getBestSnapshotUriId(final UniqueId overrideId) {
    if (overrideId != null) {
      return overrideId.toLatest().toString();
    }
    return getSnapshot() != null ? getSnapshot().getUniqueId().toLatest().toString() : getUriSnapshotId();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code WebMarketDataSnapshotData}.
   * @return the meta-bean, not null
   */
  public static WebMarketDataSnapshotData.Meta meta() {
    return WebMarketDataSnapshotData.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(WebMarketDataSnapshotData.Meta.INSTANCE);
  }

  @Override
  public WebMarketDataSnapshotData.Meta metaBean() {
    return WebMarketDataSnapshotData.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets for obtaining the live market data provider names. Either this or marketDataSpecificationRepository must be set.
   * @return the value of the property
   */
  public LiveMarketDataProviderFactory getLiveMarketDataProviderFactory() {
    return _liveMarketDataProviderFactory;
  }

  /**
   * Sets for obtaining the live market data provider names. Either this or marketDataSpecificationRepository must be set.
   * @param liveMarketDataProviderFactory  the new value of the property
   */
  public void setLiveMarketDataProviderFactory(LiveMarketDataProviderFactory liveMarketDataProviderFactory) {
    this._liveMarketDataProviderFactory = liveMarketDataProviderFactory;
  }

  /**
   * Gets the the {@code liveMarketDataProviderFactory} property.
   * @return the property, not null
   */
  public final Property<LiveMarketDataProviderFactory> liveMarketDataProviderFactory() {
    return metaBean().liveMarketDataProviderFactory().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets for looking up market data provider specifications by name. Either this or liveMarketDataProviderFactory must be set.
   * 
   * @deprecated use liveMarketDataProviderFactory
   * @return the value of the property
   */
  @Deprecated
  public NamedMarketDataSpecificationRepository getMarketDataSpecificationRepository() {
    return _marketDataSpecificationRepository;
  }

  /**
   * Sets for looking up market data provider specifications by name. Either this or liveMarketDataProviderFactory must be set.
   * 
   * @deprecated use liveMarketDataProviderFactory
   * @param marketDataSpecificationRepository  the new value of the property
   */
  @Deprecated
  public void setMarketDataSpecificationRepository(NamedMarketDataSpecificationRepository marketDataSpecificationRepository) {
    this._marketDataSpecificationRepository = marketDataSpecificationRepository;
  }

  /**
   * Gets the the {@code marketDataSpecificationRepository} property.
   * 
   * @deprecated use liveMarketDataProviderFactory
   * @return the property, not null
   */
  @Deprecated
  public final Property<NamedMarketDataSpecificationRepository> marketDataSpecificationRepository() {
    return metaBean().marketDataSpecificationRepository().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the market data snapshot master.
   * @return the value of the property
   */
  public MarketDataSnapshotMaster getMarketDataSnapshotMaster() {
    return _marketDataSnapshotMaster;
  }

  /**
   * Sets the market data snapshot master.
   * @param marketDataSnapshotMaster  the new value of the property
   */
  public void setMarketDataSnapshotMaster(MarketDataSnapshotMaster marketDataSnapshotMaster) {
    this._marketDataSnapshotMaster = marketDataSnapshotMaster;
  }

  /**
   * Gets the the {@code marketDataSnapshotMaster} property.
   * @return the property, not null
   */
  public final Property<MarketDataSnapshotMaster> marketDataSnapshotMaster() {
    return metaBean().marketDataSnapshotMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the config master.
   * @return the value of the property
   */
  public ConfigMaster getConfigMaster() {
    return _configMaster;
  }

  /**
   * Sets the config master.
   * @param configMaster  the new value of the property
   */
  public void setConfigMaster(ConfigMaster configMaster) {
    this._configMaster = configMaster;
  }

  /**
   * Gets the the {@code configMaster} property.
   * @return the property, not null
   */
  public final Property<ConfigMaster> configMaster() {
    return metaBean().configMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the view processor.
   * @return the value of the property
   */
  public ViewProcessor getViewProcessor() {
    return _viewProcessor;
  }

  /**
   * Sets the view processor.
   * @param viewProcessor  the new value of the property
   */
  public void setViewProcessor(ViewProcessor viewProcessor) {
    this._viewProcessor = viewProcessor;
  }

  /**
   * Gets the the {@code viewProcessor} property.
   * @return the property, not null
   */
  public final Property<ViewProcessor> viewProcessor() {
    return metaBean().viewProcessor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the computation target resolver.
   * @return the value of the property
   */
  public ComputationTargetResolver getComputationTargetResolver() {
    return _computationTargetResolver;
  }

  /**
   * Sets the computation target resolver.
   * @param computationTargetResolver  the new value of the property
   */
  public void setComputationTargetResolver(ComputationTargetResolver computationTargetResolver) {
    this._computationTargetResolver = computationTargetResolver;
  }

  /**
   * Gets the the {@code computationTargetResolver} property.
   * @return the property, not null
   */
  public final Property<ComputationTargetResolver> computationTargetResolver() {
    return metaBean().computationTargetResolver().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the hts source, may be null.
   * @return the value of the property
   */
  public HistoricalTimeSeriesSource getHistoricalTimeSeriesSource() {
    return _historicalTimeSeriesSource;
  }

  /**
   * Sets the hts source, may be null.
   * @param historicalTimeSeriesSource  the new value of the property
   */
  public void setHistoricalTimeSeriesSource(HistoricalTimeSeriesSource historicalTimeSeriesSource) {
    this._historicalTimeSeriesSource = historicalTimeSeriesSource;
  }

  /**
   * Gets the the {@code historicalTimeSeriesSource} property.
   * @return the property, not null
   */
  public final Property<HistoricalTimeSeriesSource> historicalTimeSeriesSource() {
    return metaBean().historicalTimeSeriesSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the snapshot id from the input URI.
   * @return the value of the property
   */
  public String getUriSnapshotId() {
    return _uriSnapshotId;
  }

  /**
   * Sets the snapshot id from the input URI.
   * @param uriSnapshotId  the new value of the property
   */
  public void setUriSnapshotId(String uriSnapshotId) {
    this._uriSnapshotId = uriSnapshotId;
  }

  /**
   * Gets the the {@code uriSnapshotId} property.
   * @return the property, not null
   */
  public final Property<String> uriSnapshotId() {
    return metaBean().uriSnapshotId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the snapshot version id from the URI.
   * @return the value of the property
   */
  public String getUriVersionId() {
    return _uriVersionId;
  }

  /**
   * Sets the snapshot version id from the URI.
   * @param uriVersionId  the new value of the property
   */
  public void setUriVersionId(String uriVersionId) {
    this._uriVersionId = uriVersionId;
  }

  /**
   * Gets the the {@code uriVersionId} property.
   * @return the property, not null
   */
  public final Property<String> uriVersionId() {
    return metaBean().uriVersionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the market data snapshot.
   * @return the value of the property
   */
  public MarketDataSnapshotDocument getSnapshot() {
    return _snapshot;
  }

  /**
   * Sets the market data snapshot.
   * @param snapshot  the new value of the property
   */
  public void setSnapshot(MarketDataSnapshotDocument snapshot) {
    this._snapshot = snapshot;
  }

  /**
   * Gets the the {@code snapshot} property.
   * @return the property, not null
   */
  public final Property<MarketDataSnapshotDocument> snapshot() {
    return metaBean().snapshot().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the config source.
   * @return the value of the property
   */
  public ConfigSource getConfigSource() {
    return _configSource;
  }

  /**
   * Sets the config source.
   * @param configSource  the new value of the property
   */
  public void setConfigSource(ConfigSource configSource) {
    this._configSource = configSource;
  }

  /**
   * Gets the the {@code configSource} property.
   * @return the property, not null
   */
  public final Property<ConfigSource> configSource() {
    return metaBean().configSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the volatility (for market data snapshots).
   * @return the value of the property
   */
  public VolatilityCubeDefinitionSource getVolatilityCubeDefinitionSource() {
    return _volatilityCubeDefinitionSource;
  }

  /**
   * Sets the volatility (for market data snapshots).
   * @param volatilityCubeDefinitionSource  the new value of the property
   */
  public void setVolatilityCubeDefinitionSource(VolatilityCubeDefinitionSource volatilityCubeDefinitionSource) {
    this._volatilityCubeDefinitionSource = volatilityCubeDefinitionSource;
  }

  /**
   * Gets the the {@code volatilityCubeDefinitionSource} property.
   * @return the property, not null
   */
  public final Property<VolatilityCubeDefinitionSource> volatilityCubeDefinitionSource() {
    return metaBean().volatilityCubeDefinitionSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the versioned market data snapshot.
   * @return the value of the property
   */
  public MarketDataSnapshotDocument getVersioned() {
    return _versioned;
  }

  /**
   * Sets the versioned market data snapshot.
   * @param versioned  the new value of the property
   */
  public void setVersioned(MarketDataSnapshotDocument versioned) {
    this._versioned = versioned;
  }

  /**
   * Gets the the {@code versioned} property.
   * @return the property, not null
   */
  public final Property<MarketDataSnapshotDocument> versioned() {
    return metaBean().versioned().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public WebMarketDataSnapshotData clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      WebMarketDataSnapshotData other = (WebMarketDataSnapshotData) obj;
      return JodaBeanUtils.equal(getLiveMarketDataProviderFactory(), other.getLiveMarketDataProviderFactory()) &&
          JodaBeanUtils.equal(getMarketDataSpecificationRepository(), other.getMarketDataSpecificationRepository()) &&
          JodaBeanUtils.equal(getMarketDataSnapshotMaster(), other.getMarketDataSnapshotMaster()) &&
          JodaBeanUtils.equal(getConfigMaster(), other.getConfigMaster()) &&
          JodaBeanUtils.equal(getViewProcessor(), other.getViewProcessor()) &&
          JodaBeanUtils.equal(getComputationTargetResolver(), other.getComputationTargetResolver()) &&
          JodaBeanUtils.equal(getHistoricalTimeSeriesSource(), other.getHistoricalTimeSeriesSource()) &&
          JodaBeanUtils.equal(getUriSnapshotId(), other.getUriSnapshotId()) &&
          JodaBeanUtils.equal(getUriVersionId(), other.getUriVersionId()) &&
          JodaBeanUtils.equal(getSnapshot(), other.getSnapshot()) &&
          JodaBeanUtils.equal(getConfigSource(), other.getConfigSource()) &&
          JodaBeanUtils.equal(getVolatilityCubeDefinitionSource(), other.getVolatilityCubeDefinitionSource()) &&
          JodaBeanUtils.equal(getVersioned(), other.getVersioned()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getLiveMarketDataProviderFactory());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMarketDataSpecificationRepository());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMarketDataSnapshotMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getConfigMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getViewProcessor());
    hash = hash * 31 + JodaBeanUtils.hashCode(getComputationTargetResolver());
    hash = hash * 31 + JodaBeanUtils.hashCode(getHistoricalTimeSeriesSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUriSnapshotId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUriVersionId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSnapshot());
    hash = hash * 31 + JodaBeanUtils.hashCode(getConfigSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getVolatilityCubeDefinitionSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getVersioned());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(448);
    buf.append("WebMarketDataSnapshotData{");
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
    buf.append("liveMarketDataProviderFactory").append('=').append(JodaBeanUtils.toString(getLiveMarketDataProviderFactory())).append(',').append(' ');
    buf.append("marketDataSpecificationRepository").append('=').append(JodaBeanUtils.toString(getMarketDataSpecificationRepository())).append(',').append(' ');
    buf.append("marketDataSnapshotMaster").append('=').append(JodaBeanUtils.toString(getMarketDataSnapshotMaster())).append(',').append(' ');
    buf.append("configMaster").append('=').append(JodaBeanUtils.toString(getConfigMaster())).append(',').append(' ');
    buf.append("viewProcessor").append('=').append(JodaBeanUtils.toString(getViewProcessor())).append(',').append(' ');
    buf.append("computationTargetResolver").append('=').append(JodaBeanUtils.toString(getComputationTargetResolver())).append(',').append(' ');
    buf.append("historicalTimeSeriesSource").append('=').append(JodaBeanUtils.toString(getHistoricalTimeSeriesSource())).append(',').append(' ');
    buf.append("uriSnapshotId").append('=').append(JodaBeanUtils.toString(getUriSnapshotId())).append(',').append(' ');
    buf.append("uriVersionId").append('=').append(JodaBeanUtils.toString(getUriVersionId())).append(',').append(' ');
    buf.append("snapshot").append('=').append(JodaBeanUtils.toString(getSnapshot())).append(',').append(' ');
    buf.append("configSource").append('=').append(JodaBeanUtils.toString(getConfigSource())).append(',').append(' ');
    buf.append("volatilityCubeDefinitionSource").append('=').append(JodaBeanUtils.toString(getVolatilityCubeDefinitionSource())).append(',').append(' ');
    buf.append("versioned").append('=').append(JodaBeanUtils.toString(getVersioned())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code WebMarketDataSnapshotData}.
   */
  public static class Meta extends WebPerRequestData.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code liveMarketDataProviderFactory} property.
     */
    private final MetaProperty<LiveMarketDataProviderFactory> _liveMarketDataProviderFactory = DirectMetaProperty.ofReadWrite(
        this, "liveMarketDataProviderFactory", WebMarketDataSnapshotData.class, LiveMarketDataProviderFactory.class);
    /**
     * The meta-property for the {@code marketDataSpecificationRepository} property.
     */
    private final MetaProperty<NamedMarketDataSpecificationRepository> _marketDataSpecificationRepository = DirectMetaProperty.ofReadWrite(
        this, "marketDataSpecificationRepository", WebMarketDataSnapshotData.class, NamedMarketDataSpecificationRepository.class);
    /**
     * The meta-property for the {@code marketDataSnapshotMaster} property.
     */
    private final MetaProperty<MarketDataSnapshotMaster> _marketDataSnapshotMaster = DirectMetaProperty.ofReadWrite(
        this, "marketDataSnapshotMaster", WebMarketDataSnapshotData.class, MarketDataSnapshotMaster.class);
    /**
     * The meta-property for the {@code configMaster} property.
     */
    private final MetaProperty<ConfigMaster> _configMaster = DirectMetaProperty.ofReadWrite(
        this, "configMaster", WebMarketDataSnapshotData.class, ConfigMaster.class);
    /**
     * The meta-property for the {@code viewProcessor} property.
     */
    private final MetaProperty<ViewProcessor> _viewProcessor = DirectMetaProperty.ofReadWrite(
        this, "viewProcessor", WebMarketDataSnapshotData.class, ViewProcessor.class);
    /**
     * The meta-property for the {@code computationTargetResolver} property.
     */
    private final MetaProperty<ComputationTargetResolver> _computationTargetResolver = DirectMetaProperty.ofReadWrite(
        this, "computationTargetResolver", WebMarketDataSnapshotData.class, ComputationTargetResolver.class);
    /**
     * The meta-property for the {@code historicalTimeSeriesSource} property.
     */
    private final MetaProperty<HistoricalTimeSeriesSource> _historicalTimeSeriesSource = DirectMetaProperty.ofReadWrite(
        this, "historicalTimeSeriesSource", WebMarketDataSnapshotData.class, HistoricalTimeSeriesSource.class);
    /**
     * The meta-property for the {@code uriSnapshotId} property.
     */
    private final MetaProperty<String> _uriSnapshotId = DirectMetaProperty.ofReadWrite(
        this, "uriSnapshotId", WebMarketDataSnapshotData.class, String.class);
    /**
     * The meta-property for the {@code uriVersionId} property.
     */
    private final MetaProperty<String> _uriVersionId = DirectMetaProperty.ofReadWrite(
        this, "uriVersionId", WebMarketDataSnapshotData.class, String.class);
    /**
     * The meta-property for the {@code snapshot} property.
     */
    private final MetaProperty<MarketDataSnapshotDocument> _snapshot = DirectMetaProperty.ofReadWrite(
        this, "snapshot", WebMarketDataSnapshotData.class, MarketDataSnapshotDocument.class);
    /**
     * The meta-property for the {@code configSource} property.
     */
    private final MetaProperty<ConfigSource> _configSource = DirectMetaProperty.ofReadWrite(
        this, "configSource", WebMarketDataSnapshotData.class, ConfigSource.class);
    /**
     * The meta-property for the {@code volatilityCubeDefinitionSource} property.
     */
    private final MetaProperty<VolatilityCubeDefinitionSource> _volatilityCubeDefinitionSource = DirectMetaProperty.ofReadWrite(
        this, "volatilityCubeDefinitionSource", WebMarketDataSnapshotData.class, VolatilityCubeDefinitionSource.class);
    /**
     * The meta-property for the {@code versioned} property.
     */
    private final MetaProperty<MarketDataSnapshotDocument> _versioned = DirectMetaProperty.ofReadWrite(
        this, "versioned", WebMarketDataSnapshotData.class, MarketDataSnapshotDocument.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "liveMarketDataProviderFactory",
        "marketDataSpecificationRepository",
        "marketDataSnapshotMaster",
        "configMaster",
        "viewProcessor",
        "computationTargetResolver",
        "historicalTimeSeriesSource",
        "uriSnapshotId",
        "uriVersionId",
        "snapshot",
        "configSource",
        "volatilityCubeDefinitionSource",
        "versioned");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -301472921:  // liveMarketDataProviderFactory
          return _liveMarketDataProviderFactory;
        case 1743800263:  // marketDataSpecificationRepository
          return _marketDataSpecificationRepository;
        case 2090650860:  // marketDataSnapshotMaster
          return _marketDataSnapshotMaster;
        case 10395716:  // configMaster
          return _configMaster;
        case -1697555603:  // viewProcessor
          return _viewProcessor;
        case 1562222174:  // computationTargetResolver
          return _computationTargetResolver;
        case 358729161:  // historicalTimeSeriesSource
          return _historicalTimeSeriesSource;
        case -1254537077:  // uriSnapshotId
          return _uriSnapshotId;
        case 666567687:  // uriVersionId
          return _uriVersionId;
        case 284874180:  // snapshot
          return _snapshot;
        case 195157501:  // configSource
          return _configSource;
        case 1540542824:  // volatilityCubeDefinitionSource
          return _volatilityCubeDefinitionSource;
        case -1407102089:  // versioned
          return _versioned;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends WebMarketDataSnapshotData> builder() {
      return new DirectBeanBuilder<WebMarketDataSnapshotData>(new WebMarketDataSnapshotData());
    }

    @Override
    public Class<? extends WebMarketDataSnapshotData> beanType() {
      return WebMarketDataSnapshotData.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code liveMarketDataProviderFactory} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LiveMarketDataProviderFactory> liveMarketDataProviderFactory() {
      return _liveMarketDataProviderFactory;
    }

    /**
     * The meta-property for the {@code marketDataSpecificationRepository} property.
     * @deprecated use liveMarketDataProviderFactory
     * @return the meta-property, not null
     */
    @Deprecated
    public final MetaProperty<NamedMarketDataSpecificationRepository> marketDataSpecificationRepository() {
      return _marketDataSpecificationRepository;
    }

    /**
     * The meta-property for the {@code marketDataSnapshotMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<MarketDataSnapshotMaster> marketDataSnapshotMaster() {
      return _marketDataSnapshotMaster;
    }

    /**
     * The meta-property for the {@code configMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ConfigMaster> configMaster() {
      return _configMaster;
    }

    /**
     * The meta-property for the {@code viewProcessor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ViewProcessor> viewProcessor() {
      return _viewProcessor;
    }

    /**
     * The meta-property for the {@code computationTargetResolver} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ComputationTargetResolver> computationTargetResolver() {
      return _computationTargetResolver;
    }

    /**
     * The meta-property for the {@code historicalTimeSeriesSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<HistoricalTimeSeriesSource> historicalTimeSeriesSource() {
      return _historicalTimeSeriesSource;
    }

    /**
     * The meta-property for the {@code uriSnapshotId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uriSnapshotId() {
      return _uriSnapshotId;
    }

    /**
     * The meta-property for the {@code uriVersionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uriVersionId() {
      return _uriVersionId;
    }

    /**
     * The meta-property for the {@code snapshot} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<MarketDataSnapshotDocument> snapshot() {
      return _snapshot;
    }

    /**
     * The meta-property for the {@code configSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ConfigSource> configSource() {
      return _configSource;
    }

    /**
     * The meta-property for the {@code volatilityCubeDefinitionSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<VolatilityCubeDefinitionSource> volatilityCubeDefinitionSource() {
      return _volatilityCubeDefinitionSource;
    }

    /**
     * The meta-property for the {@code versioned} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<MarketDataSnapshotDocument> versioned() {
      return _versioned;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -301472921:  // liveMarketDataProviderFactory
          return ((WebMarketDataSnapshotData) bean).getLiveMarketDataProviderFactory();
        case 1743800263:  // marketDataSpecificationRepository
          return ((WebMarketDataSnapshotData) bean).getMarketDataSpecificationRepository();
        case 2090650860:  // marketDataSnapshotMaster
          return ((WebMarketDataSnapshotData) bean).getMarketDataSnapshotMaster();
        case 10395716:  // configMaster
          return ((WebMarketDataSnapshotData) bean).getConfigMaster();
        case -1697555603:  // viewProcessor
          return ((WebMarketDataSnapshotData) bean).getViewProcessor();
        case 1562222174:  // computationTargetResolver
          return ((WebMarketDataSnapshotData) bean).getComputationTargetResolver();
        case 358729161:  // historicalTimeSeriesSource
          return ((WebMarketDataSnapshotData) bean).getHistoricalTimeSeriesSource();
        case -1254537077:  // uriSnapshotId
          return ((WebMarketDataSnapshotData) bean).getUriSnapshotId();
        case 666567687:  // uriVersionId
          return ((WebMarketDataSnapshotData) bean).getUriVersionId();
        case 284874180:  // snapshot
          return ((WebMarketDataSnapshotData) bean).getSnapshot();
        case 195157501:  // configSource
          return ((WebMarketDataSnapshotData) bean).getConfigSource();
        case 1540542824:  // volatilityCubeDefinitionSource
          return ((WebMarketDataSnapshotData) bean).getVolatilityCubeDefinitionSource();
        case -1407102089:  // versioned
          return ((WebMarketDataSnapshotData) bean).getVersioned();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -301472921:  // liveMarketDataProviderFactory
          ((WebMarketDataSnapshotData) bean).setLiveMarketDataProviderFactory((LiveMarketDataProviderFactory) newValue);
          return;
        case 1743800263:  // marketDataSpecificationRepository
          ((WebMarketDataSnapshotData) bean).setMarketDataSpecificationRepository((NamedMarketDataSpecificationRepository) newValue);
          return;
        case 2090650860:  // marketDataSnapshotMaster
          ((WebMarketDataSnapshotData) bean).setMarketDataSnapshotMaster((MarketDataSnapshotMaster) newValue);
          return;
        case 10395716:  // configMaster
          ((WebMarketDataSnapshotData) bean).setConfigMaster((ConfigMaster) newValue);
          return;
        case -1697555603:  // viewProcessor
          ((WebMarketDataSnapshotData) bean).setViewProcessor((ViewProcessor) newValue);
          return;
        case 1562222174:  // computationTargetResolver
          ((WebMarketDataSnapshotData) bean).setComputationTargetResolver((ComputationTargetResolver) newValue);
          return;
        case 358729161:  // historicalTimeSeriesSource
          ((WebMarketDataSnapshotData) bean).setHistoricalTimeSeriesSource((HistoricalTimeSeriesSource) newValue);
          return;
        case -1254537077:  // uriSnapshotId
          ((WebMarketDataSnapshotData) bean).setUriSnapshotId((String) newValue);
          return;
        case 666567687:  // uriVersionId
          ((WebMarketDataSnapshotData) bean).setUriVersionId((String) newValue);
          return;
        case 284874180:  // snapshot
          ((WebMarketDataSnapshotData) bean).setSnapshot((MarketDataSnapshotDocument) newValue);
          return;
        case 195157501:  // configSource
          ((WebMarketDataSnapshotData) bean).setConfigSource((ConfigSource) newValue);
          return;
        case 1540542824:  // volatilityCubeDefinitionSource
          ((WebMarketDataSnapshotData) bean).setVolatilityCubeDefinitionSource((VolatilityCubeDefinitionSource) newValue);
          return;
        case -1407102089:  // versioned
          ((WebMarketDataSnapshotData) bean).setVersioned((MarketDataSnapshotDocument) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
