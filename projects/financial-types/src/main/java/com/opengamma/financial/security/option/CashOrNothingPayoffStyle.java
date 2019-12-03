/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.option;

import java.util.Map;

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

/**
 * The cash or nothing payoff style.
 */
@BeanDefinition
public class CashOrNothingPayoffStyle extends PayoffStyle {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The payment.
   */
  @PropertyDefinition
  private double _payment;

  /**
   * Creates an instance.
   */
  private CashOrNothingPayoffStyle() {
  }

  /**
   * Creates an instance.
   *
   * @param payment  the payment
   */
  public CashOrNothingPayoffStyle(final double payment) {
    setPayment(payment);
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(final PayoffStyleVisitor<T> visitor) {
    return visitor.visitCashOrNothingPayoffStyle(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CashOrNothingPayoffStyle}.
   * @return the meta-bean, not null
   */
  public static CashOrNothingPayoffStyle.Meta meta() {
    return CashOrNothingPayoffStyle.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CashOrNothingPayoffStyle.Meta.INSTANCE);
  }

  @Override
  public CashOrNothingPayoffStyle.Meta metaBean() {
    return CashOrNothingPayoffStyle.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the payment.
   * @return the value of the property
   */
  public double getPayment() {
    return _payment;
  }

  /**
   * Sets the payment.
   * @param payment  the new value of the property
   */
  public void setPayment(double payment) {
    this._payment = payment;
  }

  /**
   * Gets the the {@code payment} property.
   * @return the property, not null
   */
  public final Property<Double> payment() {
    return metaBean().payment().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public CashOrNothingPayoffStyle clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CashOrNothingPayoffStyle other = (CashOrNothingPayoffStyle) obj;
      return JodaBeanUtils.equal(getPayment(), other.getPayment()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getPayment());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("CashOrNothingPayoffStyle{");
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
    buf.append("payment").append('=').append(JodaBeanUtils.toString(getPayment())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CashOrNothingPayoffStyle}.
   */
  public static class Meta extends PayoffStyle.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code payment} property.
     */
    private final MetaProperty<Double> _payment = DirectMetaProperty.ofReadWrite(
        this, "payment", CashOrNothingPayoffStyle.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "payment");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -786681338:  // payment
          return _payment;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CashOrNothingPayoffStyle> builder() {
      return new DirectBeanBuilder<CashOrNothingPayoffStyle>(new CashOrNothingPayoffStyle());
    }

    @Override
    public Class<? extends CashOrNothingPayoffStyle> beanType() {
      return CashOrNothingPayoffStyle.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code payment} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> payment() {
      return _payment;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -786681338:  // payment
          return ((CashOrNothingPayoffStyle) bean).getPayment();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -786681338:  // payment
          ((CashOrNothingPayoffStyle) bean).setPayment((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
