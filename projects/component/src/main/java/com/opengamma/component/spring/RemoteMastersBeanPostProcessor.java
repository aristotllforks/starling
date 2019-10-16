/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.spring;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.HashMap;
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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentServer;
import com.opengamma.component.rest.DataComponentServerUris;
import com.opengamma.component.rest.RemoteComponentServer;
import com.opengamma.engine.calcnode.stats.FunctionCostsMaster;
import com.opengamma.engine.calcnode.stats.RemoteFunctionCostsMaster;
import com.opengamma.master.config.ConfigMaster;
import com.opengamma.master.config.impl.RemoteConfigMaster;
import com.opengamma.master.convention.ConventionMaster;
import com.opengamma.master.convention.impl.RemoteConventionMaster;
import com.opengamma.master.exchange.ExchangeMaster;
import com.opengamma.master.exchange.impl.RemoteExchangeMaster;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesMaster;
import com.opengamma.master.historicaltimeseries.impl.RemoteHistoricalTimeSeriesMaster;
import com.opengamma.master.holiday.HolidayMaster;
import com.opengamma.master.holiday.impl.RemoteHolidayMaster;
import com.opengamma.master.marketdatasnapshot.MarketDataSnapshotMaster;
import com.opengamma.master.marketdatasnapshot.impl.RemoteMarketDataSnapshotMaster;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.portfolio.impl.RemotePortfolioMaster;
import com.opengamma.master.position.PositionMaster;
import com.opengamma.master.position.impl.RemotePositionMaster;
import com.opengamma.master.region.RegionMaster;
import com.opengamma.master.region.impl.RemoteRegionMaster;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.master.security.impl.RemoteSecurityMaster;
import com.opengamma.util.ReflectionUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Spring bean factory post processor to create the remote masters.
 */
@BeanDefinition
public class RemoteMastersBeanPostProcessor extends DirectBean implements BeanFactoryPostProcessor {

  /**
   * The remote wrappers.
   */
  private final Map<Class<?>, Class<?>> _remoteWrappers = new HashMap<>();
  {
    _remoteWrappers.put(ConfigMaster.class, RemoteConfigMaster.class);
    _remoteWrappers.put(ExchangeMaster.class, RemoteExchangeMaster.class);
    _remoteWrappers.put(HolidayMaster.class, RemoteHolidayMaster.class);
    _remoteWrappers.put(RegionMaster.class, RemoteRegionMaster.class);
    _remoteWrappers.put(MarketDataSnapshotMaster.class, RemoteMarketDataSnapshotMaster.class);
    _remoteWrappers.put(SecurityMaster.class, RemoteSecurityMaster.class);
    _remoteWrappers.put(ConventionMaster.class, RemoteConventionMaster.class);
    _remoteWrappers.put(PositionMaster.class, RemotePositionMaster.class);
    _remoteWrappers.put(PortfolioMaster.class, RemotePortfolioMaster.class);
    _remoteWrappers.put(HistoricalTimeSeriesMaster.class, RemoteHistoricalTimeSeriesMaster.class);
    _remoteWrappers.put(FunctionCostsMaster.class, RemoteFunctionCostsMaster.class);
  }

  /**
   * The remote URI.
   */
  @PropertyDefinition
  private URI _baseUri;

  //-------------------------------------------------------------------------
  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
    final RemoteComponentServer remote = new RemoteComponentServer(_baseUri);
    final ComponentServer server = remote.getComponentServer();
    for (final ComponentInfo info : server.getComponentInfos()) {
      initComponent(beanFactory, info);
    }
  }

  protected void initComponent(final ConfigurableListableBeanFactory beanFactory, final ComponentInfo info) {
    final URI componentUri = DataComponentServerUris.uri(_baseUri, info);
    final Class<?> remoteType = _remoteWrappers.get(info.getType());
    if (remoteType != null) {
      final Constructor<?> con = ReflectionUtils.findConstructor(remoteType, URI.class);
      final Object target = ReflectionUtils.newInstance(con, componentUri);
      final String beanName = info.getClassifier() + info.getType().getSimpleName();
      beanFactory.registerSingleton(beanName, target);
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code RemoteMastersBeanPostProcessor}.
   * @return the meta-bean, not null
   */
  public static RemoteMastersBeanPostProcessor.Meta meta() {
    return RemoteMastersBeanPostProcessor.Meta.INSTANCE;
  }

  static {
    MetaBean.register(RemoteMastersBeanPostProcessor.Meta.INSTANCE);
  }

  @Override
  public RemoteMastersBeanPostProcessor.Meta metaBean() {
    return RemoteMastersBeanPostProcessor.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the remote URI.
   * @return the value of the property
   */
  public URI getBaseUri() {
    return _baseUri;
  }

  /**
   * Sets the remote URI.
   * @param baseUri  the new value of the property
   */
  public void setBaseUri(URI baseUri) {
    this._baseUri = baseUri;
  }

  /**
   * Gets the the {@code baseUri} property.
   * @return the property, not null
   */
  public final Property<URI> baseUri() {
    return metaBean().baseUri().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public RemoteMastersBeanPostProcessor clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      RemoteMastersBeanPostProcessor other = (RemoteMastersBeanPostProcessor) obj;
      return JodaBeanUtils.equal(getBaseUri(), other.getBaseUri());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getBaseUri());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("RemoteMastersBeanPostProcessor{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("baseUri").append('=').append(JodaBeanUtils.toString(getBaseUri())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code RemoteMastersBeanPostProcessor}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code baseUri} property.
     */
    private final MetaProperty<URI> _baseUri = DirectMetaProperty.ofReadWrite(
        this, "baseUri", RemoteMastersBeanPostProcessor.class, URI.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "baseUri");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -332625701:  // baseUri
          return _baseUri;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends RemoteMastersBeanPostProcessor> builder() {
      return new DirectBeanBuilder<>(new RemoteMastersBeanPostProcessor());
    }

    @Override
    public Class<? extends RemoteMastersBeanPostProcessor> beanType() {
      return RemoteMastersBeanPostProcessor.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code baseUri} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<URI> baseUri() {
      return _baseUri;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -332625701:  // baseUri
          return ((RemoteMastersBeanPostProcessor) bean).getBaseUri();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -332625701:  // baseUri
          ((RemoteMastersBeanPostProcessor) bean).setBaseUri((URI) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
