/**
 * Copyright (C) 2017 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.financial.function.trade;

import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.DerivedProperty;
import org.joda.beans.gen.ImmutableConstructor;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.LocalDate;

import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.CurrencyAmount;

@BeanDefinition
public class DiscountBondCashFlows implements ImmutableBean {
  /**
   * The nominal payment date label.
   */
  public static final String NOMINAL_PAYMENT_DATE = "Nominal Payment Date";
  /**
   * The discount factor label.
   */
  public static final String DISCOUNT_FACTOR = "Discount Factor";
  /**
   * The discounted payment amount.
   */
  public static final String DISCOUNTED_PAYMENT_AMOUNT = "Discounted Payment Amount";
  /**
   * The notional label.
   */
  public static final String NOTIONAL = "Nominal Payment Amount";
  /**
   * The payment time label.
   */
  public static final String PAYMENT_TIME = "Payment Time";
  /**
   * The maturity.
   */
  @PropertyDefinition
  private final LocalDate _maturity;
  /**
   * The nominal amount.
   */
  @PropertyDefinition
  private final CurrencyAmount _nominalAmount;
  /**
   * The payment time.
   */
  @PropertyDefinition
  private final double _paymentTime;
  /**
   * The discount factor.
   */
  @PropertyDefinition
  private final double _discountFactor;

  @ImmutableConstructor
  public DiscountBondCashFlows(final LocalDate maturity, final CurrencyAmount paymentAmount, final double paymentTime, final double discountFactor) {
    _maturity = ArgumentChecker.notNull(maturity, "maturity");
    _nominalAmount = ArgumentChecker.notNull(paymentAmount, "paymentAmount");
    _discountFactor = discountFactor;
    _paymentTime = paymentTime;
  }

  @DerivedProperty
  public CurrencyAmount getDiscountedPaymentAmount() {
    return _nominalAmount.multipliedBy(_discountFactor);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code DiscountBondCashFlows}.
   * @return the meta-bean, not null
   */
  public static DiscountBondCashFlows.Meta meta() {
    return DiscountBondCashFlows.Meta.INSTANCE;
  }

  static {
    MetaBean.register(DiscountBondCashFlows.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static DiscountBondCashFlows.Builder builder() {
    return new DiscountBondCashFlows.Builder();
  }

  @Override
  public DiscountBondCashFlows.Meta metaBean() {
    return DiscountBondCashFlows.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the maturity.
   * @return the value of the property
   */
  public LocalDate getMaturity() {
    return _maturity;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the nominal amount.
   * @return the value of the property
   */
  public CurrencyAmount getNominalAmount() {
    return _nominalAmount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the payment time.
   * @return the value of the property
   */
  public double getPaymentTime() {
    return _paymentTime;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the discount factor.
   * @return the value of the property
   */
  public double getDiscountFactor() {
    return _discountFactor;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DiscountBondCashFlows other = (DiscountBondCashFlows) obj;
      return JodaBeanUtils.equal(_maturity, other._maturity) &&
          JodaBeanUtils.equal(_nominalAmount, other._nominalAmount) &&
          JodaBeanUtils.equal(_paymentTime, other._paymentTime) &&
          JodaBeanUtils.equal(_discountFactor, other._discountFactor);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_maturity);
    hash = hash * 31 + JodaBeanUtils.hashCode(_nominalAmount);
    hash = hash * 31 + JodaBeanUtils.hashCode(_paymentTime);
    hash = hash * 31 + JodaBeanUtils.hashCode(_discountFactor);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("DiscountBondCashFlows{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("maturity").append('=').append(JodaBeanUtils.toString(_maturity)).append(',').append(' ');
    buf.append("nominalAmount").append('=').append(JodaBeanUtils.toString(_nominalAmount)).append(',').append(' ');
    buf.append("paymentTime").append('=').append(JodaBeanUtils.toString(_paymentTime)).append(',').append(' ');
    buf.append("discountFactor").append('=').append(JodaBeanUtils.toString(_discountFactor)).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code DiscountBondCashFlows}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code maturity} property.
     */
    private final MetaProperty<LocalDate> _maturity = DirectMetaProperty.ofImmutable(
        this, "maturity", DiscountBondCashFlows.class, LocalDate.class);
    /**
     * The meta-property for the {@code nominalAmount} property.
     */
    private final MetaProperty<CurrencyAmount> _nominalAmount = DirectMetaProperty.ofImmutable(
        this, "nominalAmount", DiscountBondCashFlows.class, CurrencyAmount.class);
    /**
     * The meta-property for the {@code paymentTime} property.
     */
    private final MetaProperty<Double> _paymentTime = DirectMetaProperty.ofImmutable(
        this, "paymentTime", DiscountBondCashFlows.class, Double.TYPE);
    /**
     * The meta-property for the {@code discountFactor} property.
     */
    private final MetaProperty<Double> _discountFactor = DirectMetaProperty.ofImmutable(
        this, "discountFactor", DiscountBondCashFlows.class, Double.TYPE);
    /**
     * The meta-property for the {@code discountedPaymentAmount} property.
     */
    private final MetaProperty<CurrencyAmount> _discountedPaymentAmount = DirectMetaProperty.ofDerived(
        this, "discountedPaymentAmount", DiscountBondCashFlows.class, CurrencyAmount.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "maturity",
        "nominalAmount",
        "paymentTime",
        "discountFactor",
        "discountedPaymentAmount");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 313843601:  // maturity
          return _maturity;
        case 1103949908:  // nominalAmount
          return _nominalAmount;
        case -1540389389:  // paymentTime
          return _paymentTime;
        case -557144592:  // discountFactor
          return _discountFactor;
        case 1529770046:  // discountedPaymentAmount
          return _discountedPaymentAmount;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public DiscountBondCashFlows.Builder builder() {
      return new DiscountBondCashFlows.Builder();
    }

    @Override
    public Class<? extends DiscountBondCashFlows> beanType() {
      return DiscountBondCashFlows.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code maturity} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> maturity() {
      return _maturity;
    }

    /**
     * The meta-property for the {@code nominalAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CurrencyAmount> nominalAmount() {
      return _nominalAmount;
    }

    /**
     * The meta-property for the {@code paymentTime} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> paymentTime() {
      return _paymentTime;
    }

    /**
     * The meta-property for the {@code discountFactor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> discountFactor() {
      return _discountFactor;
    }

    /**
     * The meta-property for the {@code discountedPaymentAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CurrencyAmount> discountedPaymentAmount() {
      return _discountedPaymentAmount;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 313843601:  // maturity
          return ((DiscountBondCashFlows) bean).getMaturity();
        case 1103949908:  // nominalAmount
          return ((DiscountBondCashFlows) bean).getNominalAmount();
        case -1540389389:  // paymentTime
          return ((DiscountBondCashFlows) bean).getPaymentTime();
        case -557144592:  // discountFactor
          return ((DiscountBondCashFlows) bean).getDiscountFactor();
        case 1529770046:  // discountedPaymentAmount
          return ((DiscountBondCashFlows) bean).getDiscountedPaymentAmount();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code DiscountBondCashFlows}.
   */
  public static class Builder extends DirectFieldsBeanBuilder<DiscountBondCashFlows> {

    private LocalDate _maturity;
    private CurrencyAmount _nominalAmount;
    private double _paymentTime;
    private double _discountFactor;

    /**
     * Restricted constructor.
     */
    protected Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    protected Builder(DiscountBondCashFlows beanToCopy) {
      this._maturity = beanToCopy.getMaturity();
      this._nominalAmount = beanToCopy.getNominalAmount();
      this._paymentTime = beanToCopy.getPaymentTime();
      this._discountFactor = beanToCopy.getDiscountFactor();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 313843601:  // maturity
          return _maturity;
        case 1103949908:  // nominalAmount
          return _nominalAmount;
        case -1540389389:  // paymentTime
          return _paymentTime;
        case -557144592:  // discountFactor
          return _discountFactor;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 313843601:  // maturity
          this._maturity = (LocalDate) newValue;
          break;
        case 1103949908:  // nominalAmount
          this._nominalAmount = (CurrencyAmount) newValue;
          break;
        case -1540389389:  // paymentTime
          this._paymentTime = (Double) newValue;
          break;
        case -557144592:  // discountFactor
          this._discountFactor = (Double) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public DiscountBondCashFlows build() {
      return new DiscountBondCashFlows(
          _maturity,
          _nominalAmount,
          _paymentTime,
          _discountFactor);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the maturity.
     * @param maturity  the new value
     * @return this, for chaining, not null
     */
    public Builder maturity(LocalDate maturity) {
      this._maturity = maturity;
      return this;
    }

    /**
     * Sets the nominal amount.
     * @param nominalAmount  the new value
     * @return this, for chaining, not null
     */
    public Builder nominalAmount(CurrencyAmount nominalAmount) {
      this._nominalAmount = nominalAmount;
      return this;
    }

    /**
     * Sets the payment time.
     * @param paymentTime  the new value
     * @return this, for chaining, not null
     */
    public Builder paymentTime(double paymentTime) {
      this._paymentTime = paymentTime;
      return this;
    }

    /**
     * Sets the discount factor.
     * @param discountFactor  the new value
     * @return this, for chaining, not null
     */
    public Builder discountFactor(double discountFactor) {
      this._discountFactor = discountFactor;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("DiscountBondCashFlows.Builder{");
      int len = buf.length();
      toString(buf);
      if (buf.length() > len) {
        buf.setLength(buf.length() - 2);
      }
      buf.append('}');
      return buf.toString();
    }

    protected void toString(StringBuilder buf) {
      buf.append("maturity").append('=').append(JodaBeanUtils.toString(_maturity)).append(',').append(' ');
      buf.append("nominalAmount").append('=').append(JodaBeanUtils.toString(_nominalAmount)).append(',').append(' ');
      buf.append("paymentTime").append('=').append(JodaBeanUtils.toString(_paymentTime)).append(',').append(' ');
      buf.append("discountFactor").append('=').append(JodaBeanUtils.toString(_discountFactor)).append(',').append(' ');
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
