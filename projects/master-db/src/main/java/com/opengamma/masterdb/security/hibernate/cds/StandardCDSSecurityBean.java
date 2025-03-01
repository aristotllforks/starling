/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.security.hibernate.cds;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.masterdb.security.hibernate.swap.NotionalBean;

/**
 *
 */
@BeanDefinition
public abstract class StandardCDSSecurityBean extends CreditDefaultSwapSecurityBean {

  @PropertyDefinition
  private Double _quotedSpread;
  @PropertyDefinition
  private NotionalBean _upfrontAmount;

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code StandardCDSSecurityBean}.
   * @return the meta-bean, not null
   */
  public static StandardCDSSecurityBean.Meta meta() {
    return StandardCDSSecurityBean.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(StandardCDSSecurityBean.Meta.INSTANCE);
  }

  @Override
  public StandardCDSSecurityBean.Meta metaBean() {
    return StandardCDSSecurityBean.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the quotedSpread.
   * @return the value of the property
   */
  public Double getQuotedSpread() {
    return _quotedSpread;
  }

  /**
   * Sets the quotedSpread.
   * @param quotedSpread  the new value of the property
   */
  public void setQuotedSpread(Double quotedSpread) {
    this._quotedSpread = quotedSpread;
  }

  /**
   * Gets the the {@code quotedSpread} property.
   * @return the property, not null
   */
  public final Property<Double> quotedSpread() {
    return metaBean().quotedSpread().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the upfrontAmount.
   * @return the value of the property
   */
  public NotionalBean getUpfrontAmount() {
    return _upfrontAmount;
  }

  /**
   * Sets the upfrontAmount.
   * @param upfrontAmount  the new value of the property
   */
  public void setUpfrontAmount(NotionalBean upfrontAmount) {
    this._upfrontAmount = upfrontAmount;
  }

  /**
   * Gets the the {@code upfrontAmount} property.
   * @return the property, not null
   */
  public final Property<NotionalBean> upfrontAmount() {
    return metaBean().upfrontAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      StandardCDSSecurityBean other = (StandardCDSSecurityBean) obj;
      return JodaBeanUtils.equal(getQuotedSpread(), other.getQuotedSpread()) &&
          JodaBeanUtils.equal(getUpfrontAmount(), other.getUpfrontAmount()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getQuotedSpread());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUpfrontAmount());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("StandardCDSSecurityBean{");
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
    buf.append("quotedSpread").append('=').append(JodaBeanUtils.toString(getQuotedSpread())).append(',').append(' ');
    buf.append("upfrontAmount").append('=').append(JodaBeanUtils.toString(getUpfrontAmount())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code StandardCDSSecurityBean}.
   */
  public static class Meta extends CreditDefaultSwapSecurityBean.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code quotedSpread} property.
     */
    private final MetaProperty<Double> _quotedSpread = DirectMetaProperty.ofReadWrite(
        this, "quotedSpread", StandardCDSSecurityBean.class, Double.class);
    /**
     * The meta-property for the {@code upfrontAmount} property.
     */
    private final MetaProperty<NotionalBean> _upfrontAmount = DirectMetaProperty.ofReadWrite(
        this, "upfrontAmount", StandardCDSSecurityBean.class, NotionalBean.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "quotedSpread",
        "upfrontAmount");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -963526405:  // quotedSpread
          return _quotedSpread;
        case -716346778:  // upfrontAmount
          return _upfrontAmount;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends StandardCDSSecurityBean> builder() {
      throw new UnsupportedOperationException("StandardCDSSecurityBean is an abstract class");
    }

    @Override
    public Class<? extends StandardCDSSecurityBean> beanType() {
      return StandardCDSSecurityBean.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code quotedSpread} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> quotedSpread() {
      return _quotedSpread;
    }

    /**
     * The meta-property for the {@code upfrontAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<NotionalBean> upfrontAmount() {
      return _upfrontAmount;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -963526405:  // quotedSpread
          return ((StandardCDSSecurityBean) bean).getQuotedSpread();
        case -716346778:  // upfrontAmount
          return ((StandardCDSSecurityBean) bean).getUpfrontAmount();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -963526405:  // quotedSpread
          ((StandardCDSSecurityBean) bean).setQuotedSpread((Double) newValue);
          return;
        case -716346778:  // upfrontAmount
          ((StandardCDSSecurityBean) bean).setUpfrontAmount((NotionalBean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
