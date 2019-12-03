/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.examples.bloomberg.component;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.bbg.component.AbstractBloombergLiveDataServerComponentFactory;
import com.opengamma.livedata.entitlement.LiveDataEntitlementChecker;
import com.opengamma.livedata.entitlement.PermissiveLiveDataEntitlementChecker;
import com.opengamma.livedata.resolver.DistributionSpecificationResolver;

/**
 * Component factory for Bloomberg live market data for the purpose of evaluation.
 */
@BeanDefinition
public class ExampleLiveDataServerComponentFactory extends AbstractBloombergLiveDataServerComponentFactory {

  @Override
  protected LiveDataEntitlementChecker initEntitlementChecker(final DistributionSpecificationResolver distSpecResolver) {
    return new PermissiveLiveDataEntitlementChecker();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ExampleLiveDataServerComponentFactory}.
   * @return the meta-bean, not null
   */
  public static ExampleLiveDataServerComponentFactory.Meta meta() {
    return ExampleLiveDataServerComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ExampleLiveDataServerComponentFactory.Meta.INSTANCE);
  }

  @Override
  public ExampleLiveDataServerComponentFactory.Meta metaBean() {
    return ExampleLiveDataServerComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public ExampleLiveDataServerComponentFactory clone() {
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
    buf.append("ExampleLiveDataServerComponentFactory{");
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
   * The meta-bean for {@code ExampleLiveDataServerComponentFactory}.
   */
  public static class Meta extends AbstractBloombergLiveDataServerComponentFactory.Meta {
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
    public BeanBuilder<? extends ExampleLiveDataServerComponentFactory> builder() {
      return new DirectBeanBuilder<ExampleLiveDataServerComponentFactory>(new ExampleLiveDataServerComponentFactory());
    }

    @Override
    public Class<? extends ExampleLiveDataServerComponentFactory> beanType() {
      return ExampleLiveDataServerComponentFactory.class;
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
