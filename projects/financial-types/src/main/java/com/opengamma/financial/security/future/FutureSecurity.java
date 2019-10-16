/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.future;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.master.security.SecurityDescription;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * An abstract base class for future securities.
 */
@BeanDefinition
@SecurityDescription(type = FutureSecurity.SECURITY_TYPE, description = "Future")
public abstract class FutureSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "FUTURE";

  /**
   * The expiry.
   */
  @PropertyDefinition(validate = "notNull")
  private Expiry _expiry;
  /**
   * The trading exchange.
   */
  @PropertyDefinition(validate = "notNull")
  private String _tradingExchange;
  /**
   * The settlement exchange.
   */
  @PropertyDefinition(validate = "notNull")
  private String _settlementExchange;
  /**
   * The currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;
  /**
   * The unit amount. This represents the PNL of a single long contract if its price increases by 1.0. Also known as the 'Point Value'.
   */
  @PropertyDefinition
  private double _unitAmount;

  /**
   * The future category.
   */
  @PropertyDefinition(validate = "notNull")
  private String _contractCategory;

  /**
   * Creates an empty instance.
   * <p>
   * The security details should be set before use.
   */
  protected FutureSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * @param expiry
   *          the expiry of the future, not null
   * @param tradingExchange
   *          the name of the trading exchange, not null
   * @param settlementExchange
   *          the name of the settlement exchange, not null
   * @param currency
   *          the currency, not null
   * @param unitAmount
   *          the unit amount, not null. This represents the PNL of a single long contract if its price increases by 1.0. Also known as the 'Point Value'.
   * @param category
   *          the future category, not null
   */
  protected FutureSecurity(final Expiry expiry, final String tradingExchange, final String settlementExchange, final Currency currency, final double unitAmount,
      final String category) {
    super(SECURITY_TYPE);
    setExpiry(expiry);
    setTradingExchange(tradingExchange);
    setSettlementExchange(settlementExchange);
    setCurrency(currency);
    setUnitAmount(unitAmount);
    setContractCategory(category);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FutureSecurity}.
   * @return the meta-bean, not null
   */
  public static FutureSecurity.Meta meta() {
    return FutureSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(FutureSecurity.Meta.INSTANCE);
  }

  @Override
  public FutureSecurity.Meta metaBean() {
    return FutureSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expiry.
   * @return the value of the property, not null
   */
  public Expiry getExpiry() {
    return _expiry;
  }

  /**
   * Sets the expiry.
   * @param expiry  the new value of the property, not null
   */
  public void setExpiry(Expiry expiry) {
    JodaBeanUtils.notNull(expiry, "expiry");
    this._expiry = expiry;
  }

  /**
   * Gets the the {@code expiry} property.
   * @return the property, not null
   */
  public final Property<Expiry> expiry() {
    return metaBean().expiry().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the trading exchange.
   * @return the value of the property, not null
   */
  public String getTradingExchange() {
    return _tradingExchange;
  }

  /**
   * Sets the trading exchange.
   * @param tradingExchange  the new value of the property, not null
   */
  public void setTradingExchange(String tradingExchange) {
    JodaBeanUtils.notNull(tradingExchange, "tradingExchange");
    this._tradingExchange = tradingExchange;
  }

  /**
   * Gets the the {@code tradingExchange} property.
   * @return the property, not null
   */
  public final Property<String> tradingExchange() {
    return metaBean().tradingExchange().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the settlement exchange.
   * @return the value of the property, not null
   */
  public String getSettlementExchange() {
    return _settlementExchange;
  }

  /**
   * Sets the settlement exchange.
   * @param settlementExchange  the new value of the property, not null
   */
  public void setSettlementExchange(String settlementExchange) {
    JodaBeanUtils.notNull(settlementExchange, "settlementExchange");
    this._settlementExchange = settlementExchange;
  }

  /**
   * Gets the the {@code settlementExchange} property.
   * @return the property, not null
   */
  public final Property<String> settlementExchange() {
    return metaBean().settlementExchange().createProperty(this);
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
   * Gets the unit amount. This represents the PNL of a single long contract if its price increases by 1.0. Also known as the 'Point Value'.
   * @return the value of the property
   */
  public double getUnitAmount() {
    return _unitAmount;
  }

  /**
   * Sets the unit amount. This represents the PNL of a single long contract if its price increases by 1.0. Also known as the 'Point Value'.
   * @param unitAmount  the new value of the property
   */
  public void setUnitAmount(double unitAmount) {
    this._unitAmount = unitAmount;
  }

  /**
   * Gets the the {@code unitAmount} property.
   * @return the property, not null
   */
  public final Property<Double> unitAmount() {
    return metaBean().unitAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the future category.
   * @return the value of the property, not null
   */
  public String getContractCategory() {
    return _contractCategory;
  }

  /**
   * Sets the future category.
   * @param contractCategory  the new value of the property, not null
   */
  public void setContractCategory(String contractCategory) {
    JodaBeanUtils.notNull(contractCategory, "contractCategory");
    this._contractCategory = contractCategory;
  }

  /**
   * Gets the the {@code contractCategory} property.
   * @return the property, not null
   */
  public final Property<String> contractCategory() {
    return metaBean().contractCategory().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FutureSecurity other = (FutureSecurity) obj;
      return JodaBeanUtils.equal(getExpiry(), other.getExpiry()) &&
          JodaBeanUtils.equal(getTradingExchange(), other.getTradingExchange()) &&
          JodaBeanUtils.equal(getSettlementExchange(), other.getSettlementExchange()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getUnitAmount(), other.getUnitAmount()) &&
          JodaBeanUtils.equal(getContractCategory(), other.getContractCategory()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getExpiry());
    hash = hash * 31 + JodaBeanUtils.hashCode(getTradingExchange());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSettlementExchange());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnitAmount());
    hash = hash * 31 + JodaBeanUtils.hashCode(getContractCategory());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(224);
    buf.append("FutureSecurity{");
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
    buf.append("expiry").append('=').append(JodaBeanUtils.toString(getExpiry())).append(',').append(' ');
    buf.append("tradingExchange").append('=').append(JodaBeanUtils.toString(getTradingExchange())).append(',').append(' ');
    buf.append("settlementExchange").append('=').append(JodaBeanUtils.toString(getSettlementExchange())).append(',').append(' ');
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency())).append(',').append(' ');
    buf.append("unitAmount").append('=').append(JodaBeanUtils.toString(getUnitAmount())).append(',').append(' ');
    buf.append("contractCategory").append('=').append(JodaBeanUtils.toString(getContractCategory())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FutureSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<Expiry> _expiry = DirectMetaProperty.ofReadWrite(
        this, "expiry", FutureSecurity.class, Expiry.class);
    /**
     * The meta-property for the {@code tradingExchange} property.
     */
    private final MetaProperty<String> _tradingExchange = DirectMetaProperty.ofReadWrite(
        this, "tradingExchange", FutureSecurity.class, String.class);
    /**
     * The meta-property for the {@code settlementExchange} property.
     */
    private final MetaProperty<String> _settlementExchange = DirectMetaProperty.ofReadWrite(
        this, "settlementExchange", FutureSecurity.class, String.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", FutureSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code unitAmount} property.
     */
    private final MetaProperty<Double> _unitAmount = DirectMetaProperty.ofReadWrite(
        this, "unitAmount", FutureSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code contractCategory} property.
     */
    private final MetaProperty<String> _contractCategory = DirectMetaProperty.ofReadWrite(
        this, "contractCategory", FutureSecurity.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "expiry",
        "tradingExchange",
        "settlementExchange",
        "currency",
        "unitAmount",
        "contractCategory");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1289159373:  // expiry
          return _expiry;
        case -661485980:  // tradingExchange
          return _tradingExchange;
        case 389497452:  // settlementExchange
          return _settlementExchange;
        case 575402001:  // currency
          return _currency;
        case 1673913084:  // unitAmount
          return _unitAmount;
        case -666828752:  // contractCategory
          return _contractCategory;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public boolean isBuildable() {
      return false;
    }

    @Override
    public BeanBuilder<? extends FutureSecurity> builder() {
      throw new UnsupportedOperationException("FutureSecurity is an abstract class");
    }

    @Override
    public Class<? extends FutureSecurity> beanType() {
      return FutureSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code expiry} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Expiry> expiry() {
      return _expiry;
    }

    /**
     * The meta-property for the {@code tradingExchange} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> tradingExchange() {
      return _tradingExchange;
    }

    /**
     * The meta-property for the {@code settlementExchange} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> settlementExchange() {
      return _settlementExchange;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code unitAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> unitAmount() {
      return _unitAmount;
    }

    /**
     * The meta-property for the {@code contractCategory} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> contractCategory() {
      return _contractCategory;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1289159373:  // expiry
          return ((FutureSecurity) bean).getExpiry();
        case -661485980:  // tradingExchange
          return ((FutureSecurity) bean).getTradingExchange();
        case 389497452:  // settlementExchange
          return ((FutureSecurity) bean).getSettlementExchange();
        case 575402001:  // currency
          return ((FutureSecurity) bean).getCurrency();
        case 1673913084:  // unitAmount
          return ((FutureSecurity) bean).getUnitAmount();
        case -666828752:  // contractCategory
          return ((FutureSecurity) bean).getContractCategory();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1289159373:  // expiry
          ((FutureSecurity) bean).setExpiry((Expiry) newValue);
          return;
        case -661485980:  // tradingExchange
          ((FutureSecurity) bean).setTradingExchange((String) newValue);
          return;
        case 389497452:  // settlementExchange
          ((FutureSecurity) bean).setSettlementExchange((String) newValue);
          return;
        case 575402001:  // currency
          ((FutureSecurity) bean).setCurrency((Currency) newValue);
          return;
        case 1673913084:  // unitAmount
          ((FutureSecurity) bean).setUnitAmount((Double) newValue);
          return;
        case -666828752:  // contractCategory
          ((FutureSecurity) bean).setContractCategory((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((FutureSecurity) bean)._expiry, "expiry");
      JodaBeanUtils.notNull(((FutureSecurity) bean)._tradingExchange, "tradingExchange");
      JodaBeanUtils.notNull(((FutureSecurity) bean)._settlementExchange, "settlementExchange");
      JodaBeanUtils.notNull(((FutureSecurity) bean)._currency, "currency");
      JodaBeanUtils.notNull(((FutureSecurity) bean)._contractCategory, "contractCategory");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
