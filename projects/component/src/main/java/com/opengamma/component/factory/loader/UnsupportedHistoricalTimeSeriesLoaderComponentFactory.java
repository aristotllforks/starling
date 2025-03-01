/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.loader;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentRepository;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesLoader;
import com.opengamma.master.historicaltimeseries.impl.UnsupportedHistoricalTimeSeriesLoader;

/**
 * Component factory providing the {@code HistoricalTimeSeriesLoader}.
 * <p>
 * This implementation uses {@link UnsupportedHistoricalTimeSeriesLoader}.
 */
@BeanDefinition
public class UnsupportedHistoricalTimeSeriesLoaderComponentFactory extends AbstractHistoricalTimeSeriesLoaderComponentFactory {

  /**
   * Creates the loader, without registering it.
   * <p>
   * This implementation uses {@link UnsupportedHistoricalTimeSeriesLoader}.
   *
   * @param repo  the component repository, only used to register secondary items like lifecycle, not null
   * @return the loader, not null
   */
  @Override
  protected HistoricalTimeSeriesLoader createHistoricalTimeSeriesLoader(final ComponentRepository repo) {
    return new UnsupportedHistoricalTimeSeriesLoader();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code UnsupportedHistoricalTimeSeriesLoaderComponentFactory}.
   * @return the meta-bean, not null
   */
  public static UnsupportedHistoricalTimeSeriesLoaderComponentFactory.Meta meta() {
    return UnsupportedHistoricalTimeSeriesLoaderComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(UnsupportedHistoricalTimeSeriesLoaderComponentFactory.Meta.INSTANCE);
  }

  @Override
  public UnsupportedHistoricalTimeSeriesLoaderComponentFactory.Meta metaBean() {
    return UnsupportedHistoricalTimeSeriesLoaderComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public UnsupportedHistoricalTimeSeriesLoaderComponentFactory clone() {
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
    buf.append("UnsupportedHistoricalTimeSeriesLoaderComponentFactory{");
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
   * The meta-bean for {@code UnsupportedHistoricalTimeSeriesLoaderComponentFactory}.
   */
  public static class Meta extends AbstractHistoricalTimeSeriesLoaderComponentFactory.Meta {
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
    public BeanBuilder<? extends UnsupportedHistoricalTimeSeriesLoaderComponentFactory> builder() {
      return new DirectBeanBuilder<UnsupportedHistoricalTimeSeriesLoaderComponentFactory>(new UnsupportedHistoricalTimeSeriesLoaderComponentFactory());
    }

    @Override
    public Class<? extends UnsupportedHistoricalTimeSeriesLoaderComponentFactory> beanType() {
      return UnsupportedHistoricalTimeSeriesLoaderComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
