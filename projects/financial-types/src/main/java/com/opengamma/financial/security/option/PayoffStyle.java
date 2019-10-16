/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.option;

import java.io.Serializable;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;

/**
 * The payoff style in an option.
 */
@BeanDefinition
public abstract class PayoffStyle extends DirectBean implements Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * Creates an instance.
   */
  protected PayoffStyle() {
  }

  //-------------------------------------------------------------------------
  /**
   * Accepts a visitor to manage traversal of the hierarchy.
   *
   * @param <T> the result type of the visitor
   * @param visitor  the visitor, not null
   * @return the result
   */
  public abstract <T> T accept(PayoffStyleVisitor<T> visitor);

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code PayoffStyle}.
   * @return the meta-bean, not null
   */
  public static PayoffStyle.Meta meta() {
    return PayoffStyle.Meta.INSTANCE;
  }

  static {
    MetaBean.register(PayoffStyle.Meta.INSTANCE);
  }

  @Override
  public PayoffStyle.Meta metaBean() {
    return PayoffStyle.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public PayoffStyle clone() {
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
    buf.append("PayoffStyle{");
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
   * The meta-bean for {@code PayoffStyle}.
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
    public BeanBuilder<? extends PayoffStyle> builder() {
      throw new UnsupportedOperationException("PayoffStyle is an abstract class");
    }

    @Override
    public Class<? extends PayoffStyle> beanType() {
      return PayoffStyle.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
