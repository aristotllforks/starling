/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.cash;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.master.security.SecurityDescription;
import com.opengamma.util.money.Currency;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * A security for cash balances.
 */
@BeanDefinition
@SecurityDescription(type = CashBalanceSecurity.SECURITY_TYPE, description = "Cash balance")
public class CashBalanceSecurity extends FinancialSecurity {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "CASH_BALANCE";

  /**
   * The currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;

  /**
   * The amount.
   */
  @PropertyDefinition(validate = "notNull")
  private double _amount;

  /**
   * For the builder.
   */
  /* package */CashBalanceSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * @param currency The currency, not null
   * @param amount The amount
   */
  public CashBalanceSecurity(final Currency currency, final double amount) {
    super(SECURITY_TYPE);
    setCurrency(currency);
    setAmount(amount);
  }

  @Override
  public <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitCashBalanceSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code CashBalanceSecurity}.
   * @return the meta-bean, not null
   */
  public static CashBalanceSecurity.Meta meta() {
    return CashBalanceSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(CashBalanceSecurity.Meta.INSTANCE);
  }

  @Override
  public CashBalanceSecurity.Meta metaBean() {
    return CashBalanceSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency.
   * @param currency  the new value of the property, not null
   */
  public void setCurrency(Currency currency) {
    JodaBeanUtils.notNull(currency, "currency");
    this._currency = currency;
  }

  /**
   * Gets the the {@code currency} property.
   * @return the property, not null
   */
  public final Property<Currency> currency() {
    return metaBean().currency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the amount.
   * @return the value of the property, not null
   */
  public double getAmount() {
    return _amount;
  }

  /**
   * Sets the amount.
   * @param amount  the new value of the property, not null
   */
  public void setAmount(double amount) {
    JodaBeanUtils.notNull(amount, "amount");
    this._amount = amount;
  }

  /**
   * Gets the the {@code amount} property.
   * @return the property, not null
   */
  public final Property<Double> amount() {
    return metaBean().amount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public CashBalanceSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CashBalanceSecurity other = (CashBalanceSecurity) obj;
      return JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getAmount(), other.getAmount()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getAmount());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("CashBalanceSecurity{");
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
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency())).append(',').append(' ');
    buf.append("amount").append('=').append(JodaBeanUtils.toString(getAmount())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CashBalanceSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", CashBalanceSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code amount} property.
     */
    private final MetaProperty<Double> _amount = DirectMetaProperty.ofReadWrite(
        this, "amount", CashBalanceSecurity.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "currency",
        "amount");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return _currency;
        case -1413853096:  // amount
          return _amount;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CashBalanceSecurity> builder() {
      return new DirectBeanBuilder<>(new CashBalanceSecurity());
    }

    @Override
    public Class<? extends CashBalanceSecurity> beanType() {
      return CashBalanceSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code amount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> amount() {
      return _amount;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return ((CashBalanceSecurity) bean).getCurrency();
        case -1413853096:  // amount
          return ((CashBalanceSecurity) bean).getAmount();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          ((CashBalanceSecurity) bean).setCurrency((Currency) newValue);
          return;
        case -1413853096:  // amount
          ((CashBalanceSecurity) bean).setAmount((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((CashBalanceSecurity) bean)._currency, "currency");
      JodaBeanUtils.notNull(((CashBalanceSecurity) bean)._amount, "amount");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
