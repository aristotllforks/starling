/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.future;

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

import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;

/**
 * A security for FX futures.
 */
@BeanDefinition
public class FXFutureSecurity extends FutureSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The numerator currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _numerator;
  /**
   * The denominator currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _denominator;
  /**
   * The multiplication factor.
   */
  @PropertyDefinition
  private double _multiplicationFactor = 1.0;

  /**
   * For the builder.
   */
  FXFutureSecurity() {
    super();
  }

  /**
   * @param expiry
   *          the expiry date, not null
   * @param tradingExchange
   *          the name of the trading exchange, not null
   * @param settlementExchange
   *          the name of the settlement exchange, not null
   * @param currency
   *          the currency, not null
   * @param unitAmount
   *          the unit amount, not null
   * @param numerator
   *          the numerator currency, not null
   * @param denominator
   *          the denominator currency, not null
   * @param category
   *          the future category, not null
   */
  public FXFutureSecurity(final Expiry expiry, final String tradingExchange, final String settlementExchange, final Currency currency, final double unitAmount,
      final Currency numerator, final Currency denominator, final String category) {
    super(expiry, tradingExchange, settlementExchange, currency, unitAmount, category);
    setNumerator(numerator);
    setDenominator(denominator);
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitFXFutureSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FXFutureSecurity}.
   * @return the meta-bean, not null
   */
  public static FXFutureSecurity.Meta meta() {
    return FXFutureSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(FXFutureSecurity.Meta.INSTANCE);
  }

  @Override
  public FXFutureSecurity.Meta metaBean() {
    return FXFutureSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the numerator currency.
   * @return the value of the property, not null
   */
  public Currency getNumerator() {
    return _numerator;
  }

  /**
   * Sets the numerator currency.
   * @param numerator  the new value of the property, not null
   */
  public void setNumerator(Currency numerator) {
    JodaBeanUtils.notNull(numerator, "numerator");
    this._numerator = numerator;
  }

  /**
   * Gets the the {@code numerator} property.
   * @return the property, not null
   */
  public final Property<Currency> numerator() {
    return metaBean().numerator().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the denominator currency.
   * @return the value of the property, not null
   */
  public Currency getDenominator() {
    return _denominator;
  }

  /**
   * Sets the denominator currency.
   * @param denominator  the new value of the property, not null
   */
  public void setDenominator(Currency denominator) {
    JodaBeanUtils.notNull(denominator, "denominator");
    this._denominator = denominator;
  }

  /**
   * Gets the the {@code denominator} property.
   * @return the property, not null
   */
  public final Property<Currency> denominator() {
    return metaBean().denominator().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the multiplication factor.
   * @return the value of the property
   */
  public double getMultiplicationFactor() {
    return _multiplicationFactor;
  }

  /**
   * Sets the multiplication factor.
   * @param multiplicationFactor  the new value of the property
   */
  public void setMultiplicationFactor(double multiplicationFactor) {
    this._multiplicationFactor = multiplicationFactor;
  }

  /**
   * Gets the the {@code multiplicationFactor} property.
   * @return the property, not null
   */
  public final Property<Double> multiplicationFactor() {
    return metaBean().multiplicationFactor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public FXFutureSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FXFutureSecurity other = (FXFutureSecurity) obj;
      return JodaBeanUtils.equal(getNumerator(), other.getNumerator()) &&
          JodaBeanUtils.equal(getDenominator(), other.getDenominator()) &&
          JodaBeanUtils.equal(getMultiplicationFactor(), other.getMultiplicationFactor()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getNumerator());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDenominator());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMultiplicationFactor());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("FXFutureSecurity{");
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
    buf.append("numerator").append('=').append(JodaBeanUtils.toString(getNumerator())).append(',').append(' ');
    buf.append("denominator").append('=').append(JodaBeanUtils.toString(getDenominator())).append(',').append(' ');
    buf.append("multiplicationFactor").append('=').append(JodaBeanUtils.toString(getMultiplicationFactor())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FXFutureSecurity}.
   */
  public static class Meta extends FutureSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code numerator} property.
     */
    private final MetaProperty<Currency> _numerator = DirectMetaProperty.ofReadWrite(
        this, "numerator", FXFutureSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code denominator} property.
     */
    private final MetaProperty<Currency> _denominator = DirectMetaProperty.ofReadWrite(
        this, "denominator", FXFutureSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code multiplicationFactor} property.
     */
    private final MetaProperty<Double> _multiplicationFactor = DirectMetaProperty.ofReadWrite(
        this, "multiplicationFactor", FXFutureSecurity.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "numerator",
        "denominator",
        "multiplicationFactor");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1747334793:  // numerator
          return _numerator;
        case -1983274394:  // denominator
          return _denominator;
        case 866676853:  // multiplicationFactor
          return _multiplicationFactor;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FXFutureSecurity> builder() {
      return new DirectBeanBuilder<FXFutureSecurity>(new FXFutureSecurity());
    }

    @Override
    public Class<? extends FXFutureSecurity> beanType() {
      return FXFutureSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code numerator} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> numerator() {
      return _numerator;
    }

    /**
     * The meta-property for the {@code denominator} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> denominator() {
      return _denominator;
    }

    /**
     * The meta-property for the {@code multiplicationFactor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> multiplicationFactor() {
      return _multiplicationFactor;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1747334793:  // numerator
          return ((FXFutureSecurity) bean).getNumerator();
        case -1983274394:  // denominator
          return ((FXFutureSecurity) bean).getDenominator();
        case 866676853:  // multiplicationFactor
          return ((FXFutureSecurity) bean).getMultiplicationFactor();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1747334793:  // numerator
          ((FXFutureSecurity) bean).setNumerator((Currency) newValue);
          return;
        case -1983274394:  // denominator
          ((FXFutureSecurity) bean).setDenominator((Currency) newValue);
          return;
        case 866676853:  // multiplicationFactor
          ((FXFutureSecurity) bean).setMultiplicationFactor((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((FXFutureSecurity) bean)._numerator, "numerator");
      JodaBeanUtils.notNull(((FXFutureSecurity) bean)._denominator, "denominator");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
