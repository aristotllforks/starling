/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.source;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.ComponentInfoAttributes;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeriesSource;
import com.opengamma.core.historicaltimeseries.impl.DataHistoricalTimeSeriesSourceResource;
import com.opengamma.core.historicaltimeseries.impl.NonVersionedRedisHistoricalTimeSeriesSource;
import com.opengamma.core.historicaltimeseries.impl.RemoteHistoricalTimeSeriesSource;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesResolver;
import com.opengamma.master.historicaltimeseries.impl.RedisSimulationSeriesResolver;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;

/**
 *
 */
@BeanDefinition
public class NonVersionedHistoricalTimeSeriesSourceComponentFactory extends AbstractNonVersionedRedisSourceComponentFactory {

  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) throws Exception {
    final NonVersionedRedisHistoricalTimeSeriesSource source =
        new NonVersionedRedisHistoricalTimeSeriesSource(getRedisConnector().getJedisPool(), getRedisPrefix());

    ComponentInfo info = new ComponentInfo(HistoricalTimeSeriesSource.class, getClassifier());
    info.addAttribute(ComponentInfoAttributes.LEVEL, 1);
    if (isPublishRest()) {
      info.addAttribute(ComponentInfoAttributes.REMOTE_CLIENT_JAVA, RemoteHistoricalTimeSeriesSource.class);
    }
    repo.registerComponent(info, source);
    if (isPublishRest()) {
      repo.getRestComponents().publish(info, new DataHistoricalTimeSeriesSourceResource(source));
    }

    info = new ComponentInfo(NonVersionedRedisHistoricalTimeSeriesSource.class, getClassifier());
    info.addAttribute(ComponentInfoAttributes.LEVEL, 1);
    repo.registerComponent(info, source);

    if (getResolverClassifier() != null) {
      final HistoricalTimeSeriesResolver resolver = new RedisSimulationSeriesResolver(source);
      info = new ComponentInfo(HistoricalTimeSeriesResolver.class, getResolverClassifier());
      info.addAttribute(ComponentInfoAttributes.LEVEL, 1);
      repo.registerComponent(info, resolver);
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code NonVersionedHistoricalTimeSeriesSourceComponentFactory}.
   * @return the meta-bean, not null
   */
  public static NonVersionedHistoricalTimeSeriesSourceComponentFactory.Meta meta() {
    return NonVersionedHistoricalTimeSeriesSourceComponentFactory.Meta.INSTANCE;
  }

  static {
    MetaBean.register(NonVersionedHistoricalTimeSeriesSourceComponentFactory.Meta.INSTANCE);
  }

  @Override
  public NonVersionedHistoricalTimeSeriesSourceComponentFactory.Meta metaBean() {
    return NonVersionedHistoricalTimeSeriesSourceComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public NonVersionedHistoricalTimeSeriesSourceComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(32);
    buf.append("NonVersionedHistoricalTimeSeriesSourceComponentFactory{");
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
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code NonVersionedHistoricalTimeSeriesSourceComponentFactory}.
   */
  public static class Meta extends AbstractNonVersionedRedisSourceComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends NonVersionedHistoricalTimeSeriesSourceComponentFactory> builder() {
      return new DirectBeanBuilder<>(new NonVersionedHistoricalTimeSeriesSourceComponentFactory());
    }

    @Override
    public Class<? extends NonVersionedHistoricalTimeSeriesSourceComponentFactory> beanType() {
      return NonVersionedHistoricalTimeSeriesSourceComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
