/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.PublicSPI;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;

/**
 * Result from a meta-data request for a single master.
 * <p>
 * Some user interfaces require meta-data in order to operate, such as
 * a drop-down list of valid entries to select from. This abstract class
 * provides the basic result object for such meta-data.
 */
@PublicSPI
@BeanDefinition
public abstract class AbstractMetaDataResult extends DirectBean {

  /**
   * Creates an instance.
   */
  public AbstractMetaDataResult() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code AbstractMetaDataResult}.
   * @return the meta-bean, not null
   */
  public static AbstractMetaDataResult.Meta meta() {
    return AbstractMetaDataResult.Meta.INSTANCE;
  }

  static {
    MetaBean.register(AbstractMetaDataResult.Meta.INSTANCE);
  }

  @Override
  public AbstractMetaDataResult.Meta metaBean() {
    return AbstractMetaDataResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public AbstractMetaDataResult clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(32);
    buf.append("AbstractMetaDataResult{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code AbstractMetaDataResult}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null);

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public boolean isBuildable() {
      return false;
    }

    @Override
    public BeanBuilder<? extends AbstractMetaDataResult> builder() {
      throw new UnsupportedOperationException("AbstractMetaDataResult is an abstract class");
    }

    @Override
    public Class<? extends AbstractMetaDataResult> beanType() {
      return AbstractMetaDataResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
