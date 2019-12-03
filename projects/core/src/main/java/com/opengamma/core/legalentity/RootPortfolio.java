/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 * <p>
 * Please see distribution for license.
 */
package com.opengamma.core.legalentity;

import java.util.Map;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.id.ObjectId;

/**
 * Portfolio of a legal entity.
 */
@BeanDefinition
public class RootPortfolio implements Bean {

  @PropertyDefinition
  private ObjectId _portfolio; //TODO refactor <ObjectId> to <PortfolioLink> when PortfolioLink is ready.

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code RootPortfolio}.
   * @return the meta-bean, not null
   */
  public static RootPortfolio.Meta meta() {
    return RootPortfolio.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(RootPortfolio.Meta.INSTANCE);
  }

  @Override
  public RootPortfolio.Meta metaBean() {
    return RootPortfolio.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the portfolio.
   * @return the value of the property
   */
  public ObjectId getPortfolio() {
    return _portfolio;
  }

  /**
   * Sets the portfolio.
   * @param portfolio  the new value of the property
   */
  public void setPortfolio(ObjectId portfolio) {
    this._portfolio = portfolio;
  }

  /**
   * Gets the the {@code portfolio} property.
   * @return the property, not null
   */
  public final Property<ObjectId> portfolio() {
    return metaBean().portfolio().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public RootPortfolio clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      RootPortfolio other = (RootPortfolio) obj;
      return JodaBeanUtils.equal(getPortfolio(), other.getPortfolio());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getPortfolio());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("RootPortfolio{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("portfolio").append('=').append(JodaBeanUtils.toString(getPortfolio())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code RootPortfolio}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code portfolio} property.
     */
    private final MetaProperty<ObjectId> _portfolio = DirectMetaProperty.ofReadWrite(
        this, "portfolio", RootPortfolio.class, ObjectId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "portfolio");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1121781064:  // portfolio
          return _portfolio;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends RootPortfolio> builder() {
      return new DirectBeanBuilder<RootPortfolio>(new RootPortfolio());
    }

    @Override
    public Class<? extends RootPortfolio> beanType() {
      return RootPortfolio.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code portfolio} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ObjectId> portfolio() {
      return _portfolio;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1121781064:  // portfolio
          return ((RootPortfolio) bean).getPortfolio();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1121781064:  // portfolio
          ((RootPortfolio) bean).setPortfolio((ObjectId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
