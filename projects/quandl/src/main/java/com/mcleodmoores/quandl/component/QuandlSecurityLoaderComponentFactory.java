/**
 * Copyright (C) 2015 - Present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.quandl.component;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.loader.AbstractSecurityLoaderComponentFactory;
import com.opengamma.master.security.SecurityLoader;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;

/**
 * Component factory that instantiates the security loader.
 */
@BeanDefinition
public class QuandlSecurityLoaderComponentFactory extends AbstractSecurityLoaderComponentFactory {

  // -------------------------------------------------------------------------
  @Override
  protected SecurityLoader createSecurityLoader(final ComponentRepository repo) {
    return null; // new MockSecurityLoader();
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code QuandlSecurityLoaderComponentFactory}.
   * @return the meta-bean, not null
   */
  public static QuandlSecurityLoaderComponentFactory.Meta meta() {
    return QuandlSecurityLoaderComponentFactory.Meta.INSTANCE;
  }

  static {
    MetaBean.register(QuandlSecurityLoaderComponentFactory.Meta.INSTANCE);
  }

  @Override
  public QuandlSecurityLoaderComponentFactory.Meta metaBean() {
    return QuandlSecurityLoaderComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public QuandlSecurityLoaderComponentFactory clone() {
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
    buf.append("QuandlSecurityLoaderComponentFactory{");
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
   * The meta-bean for {@code QuandlSecurityLoaderComponentFactory}.
   */
  public static class Meta extends AbstractSecurityLoaderComponentFactory.Meta {
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
    public BeanBuilder<? extends QuandlSecurityLoaderComponentFactory> builder() {
      return new DirectBeanBuilder<>(new QuandlSecurityLoaderComponentFactory());
    }

    @Override
    public Class<? extends QuandlSecurityLoaderComponentFactory> beanType() {
      return QuandlSecurityLoaderComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
