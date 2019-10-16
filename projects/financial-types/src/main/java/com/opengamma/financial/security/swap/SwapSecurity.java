/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.swap;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * A security for a swap.
 */
@BeanDefinition
public class SwapSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "SWAP";

  /**
   * The trade date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _tradeDate;
  /**
   * The effective date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _effectiveDate;
  /**
   * The maturity.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _maturityDate;
  /**
   * The counterparty.
   */
  @PropertyDefinition(validate = "notNull")
  private String _counterparty;
  /**
   * Whether the initial notional is exchanged.
   */
  @PropertyDefinition
  private boolean _exchangeInitialNotional;
  /**
   * Whether the final notional is exchanged.
   */
  @PropertyDefinition
  private boolean _exchangeFinalNotional;
  /**
   * The pay leg.
   */
  @PropertyDefinition(validate = "notNull")
  private SwapLeg _payLeg;
  /**
   * The receive leg.
   */
  @PropertyDefinition(validate = "notNull")
  private SwapLeg _receiveLeg;

  /**
   * For the builder.
   */
  SwapSecurity() { // For builder
    super(SECURITY_TYPE);
  }

  /**
   * @param tradeDate
   *          The trade date, not null
   * @param effectiveDate
   *          The effective date, not null
   * @param maturityDate
   *          The maturity date, not null
   * @param counterparty
   *          The counterparty, not null
   * @param payLeg
   *          The pay leg, not null
   * @param receiveLeg
   *          The receive leg, not null
   */
  public SwapSecurity(final ZonedDateTime tradeDate, final ZonedDateTime effectiveDate, final ZonedDateTime maturityDate, final String counterparty,
      final SwapLeg payLeg, final SwapLeg receiveLeg) {
    super(SECURITY_TYPE);
    setTradeDate(tradeDate);
    setEffectiveDate(effectiveDate);
    setMaturityDate(maturityDate);
    setCounterparty(counterparty);
    setPayLeg(payLeg);
    setReceiveLeg(receiveLeg);
    setExchangeInitialNotional(false);
    setExchangeFinalNotional(false);
  }

  /**
   * For the builder - used by subclasses to set the correct security type.
   *
   * @param securityType
   *          The security type, not null
   */
  /* package */ SwapSecurity(final String securityType) {
    super(securityType);
  }

  /**
   * Used by subclasses to set the correct security type.
   *
   * @param securityType
   *          The security type, not null
   * @param tradeDate
   *          The trade date, not null
   * @param effectiveDate
   *          The effective date, not null
   * @param maturityDate
   *          The maturity date, not null
   * @param counterparty
   *          The counterparty, not null
   * @param payLeg
   *          The pay leg, not null
   * @param receiveLeg
   *          The receive leg, not null
   */
  protected SwapSecurity(final String securityType, final ZonedDateTime tradeDate, final ZonedDateTime effectiveDate, final ZonedDateTime maturityDate,
      final String counterparty, final SwapLeg payLeg, final SwapLeg receiveLeg) {
    super(securityType);
    setTradeDate(tradeDate);
    setEffectiveDate(effectiveDate);
    setMaturityDate(maturityDate);
    setCounterparty(counterparty);
    setPayLeg(payLeg);
    setReceiveLeg(receiveLeg);
    setExchangeInitialNotional(false);
    setExchangeFinalNotional(false);
  }

  // -------------------------------------------------------------------------
  @Override
  public <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitSwapSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code SwapSecurity}.
   * @return the meta-bean, not null
   */
  public static SwapSecurity.Meta meta() {
    return SwapSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(SwapSecurity.Meta.INSTANCE);
  }

  @Override
  public SwapSecurity.Meta metaBean() {
    return SwapSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the trade date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getTradeDate() {
    return _tradeDate;
  }

  /**
   * Sets the trade date.
   * @param tradeDate  the new value of the property, not null
   */
  public void setTradeDate(ZonedDateTime tradeDate) {
    JodaBeanUtils.notNull(tradeDate, "tradeDate");
    this._tradeDate = tradeDate;
  }

  /**
   * Gets the the {@code tradeDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> tradeDate() {
    return metaBean().tradeDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the effective date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getEffectiveDate() {
    return _effectiveDate;
  }

  /**
   * Sets the effective date.
   * @param effectiveDate  the new value of the property, not null
   */
  public void setEffectiveDate(ZonedDateTime effectiveDate) {
    JodaBeanUtils.notNull(effectiveDate, "effectiveDate");
    this._effectiveDate = effectiveDate;
  }

  /**
   * Gets the the {@code effectiveDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> effectiveDate() {
    return metaBean().effectiveDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the maturity.
   * @return the value of the property, not null
   */
  public ZonedDateTime getMaturityDate() {
    return _maturityDate;
  }

  /**
   * Sets the maturity.
   * @param maturityDate  the new value of the property, not null
   */
  public void setMaturityDate(ZonedDateTime maturityDate) {
    JodaBeanUtils.notNull(maturityDate, "maturityDate");
    this._maturityDate = maturityDate;
  }

  /**
   * Gets the the {@code maturityDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> maturityDate() {
    return metaBean().maturityDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the counterparty.
   * @return the value of the property, not null
   */
  public String getCounterparty() {
    return _counterparty;
  }

  /**
   * Sets the counterparty.
   * @param counterparty  the new value of the property, not null
   */
  public void setCounterparty(String counterparty) {
    JodaBeanUtils.notNull(counterparty, "counterparty");
    this._counterparty = counterparty;
  }

  /**
   * Gets the the {@code counterparty} property.
   * @return the property, not null
   */
  public final Property<String> counterparty() {
    return metaBean().counterparty().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether the initial notional is exchanged.
   * @return the value of the property
   */
  public boolean isExchangeInitialNotional() {
    return _exchangeInitialNotional;
  }

  /**
   * Sets whether the initial notional is exchanged.
   * @param exchangeInitialNotional  the new value of the property
   */
  public void setExchangeInitialNotional(boolean exchangeInitialNotional) {
    this._exchangeInitialNotional = exchangeInitialNotional;
  }

  /**
   * Gets the the {@code exchangeInitialNotional} property.
   * @return the property, not null
   */
  public final Property<Boolean> exchangeInitialNotional() {
    return metaBean().exchangeInitialNotional().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether the final notional is exchanged.
   * @return the value of the property
   */
  public boolean isExchangeFinalNotional() {
    return _exchangeFinalNotional;
  }

  /**
   * Sets whether the final notional is exchanged.
   * @param exchangeFinalNotional  the new value of the property
   */
  public void setExchangeFinalNotional(boolean exchangeFinalNotional) {
    this._exchangeFinalNotional = exchangeFinalNotional;
  }

  /**
   * Gets the the {@code exchangeFinalNotional} property.
   * @return the property, not null
   */
  public final Property<Boolean> exchangeFinalNotional() {
    return metaBean().exchangeFinalNotional().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the pay leg.
   * @return the value of the property, not null
   */
  public SwapLeg getPayLeg() {
    return _payLeg;
  }

  /**
   * Sets the pay leg.
   * @param payLeg  the new value of the property, not null
   */
  public void setPayLeg(SwapLeg payLeg) {
    JodaBeanUtils.notNull(payLeg, "payLeg");
    this._payLeg = payLeg;
  }

  /**
   * Gets the the {@code payLeg} property.
   * @return the property, not null
   */
  public final Property<SwapLeg> payLeg() {
    return metaBean().payLeg().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the receive leg.
   * @return the value of the property, not null
   */
  public SwapLeg getReceiveLeg() {
    return _receiveLeg;
  }

  /**
   * Sets the receive leg.
   * @param receiveLeg  the new value of the property, not null
   */
  public void setReceiveLeg(SwapLeg receiveLeg) {
    JodaBeanUtils.notNull(receiveLeg, "receiveLeg");
    this._receiveLeg = receiveLeg;
  }

  /**
   * Gets the the {@code receiveLeg} property.
   * @return the property, not null
   */
  public final Property<SwapLeg> receiveLeg() {
    return metaBean().receiveLeg().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public SwapSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SwapSecurity other = (SwapSecurity) obj;
      return JodaBeanUtils.equal(getTradeDate(), other.getTradeDate()) &&
          JodaBeanUtils.equal(getEffectiveDate(), other.getEffectiveDate()) &&
          JodaBeanUtils.equal(getMaturityDate(), other.getMaturityDate()) &&
          JodaBeanUtils.equal(getCounterparty(), other.getCounterparty()) &&
          (isExchangeInitialNotional() == other.isExchangeInitialNotional()) &&
          (isExchangeFinalNotional() == other.isExchangeFinalNotional()) &&
          JodaBeanUtils.equal(getPayLeg(), other.getPayLeg()) &&
          JodaBeanUtils.equal(getReceiveLeg(), other.getReceiveLeg()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getTradeDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getEffectiveDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMaturityDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCounterparty());
    hash = hash * 31 + JodaBeanUtils.hashCode(isExchangeInitialNotional());
    hash = hash * 31 + JodaBeanUtils.hashCode(isExchangeFinalNotional());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPayLeg());
    hash = hash * 31 + JodaBeanUtils.hashCode(getReceiveLeg());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(288);
    buf.append("SwapSecurity{");
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
    buf.append("tradeDate").append('=').append(JodaBeanUtils.toString(getTradeDate())).append(',').append(' ');
    buf.append("effectiveDate").append('=').append(JodaBeanUtils.toString(getEffectiveDate())).append(',').append(' ');
    buf.append("maturityDate").append('=').append(JodaBeanUtils.toString(getMaturityDate())).append(',').append(' ');
    buf.append("counterparty").append('=').append(JodaBeanUtils.toString(getCounterparty())).append(',').append(' ');
    buf.append("exchangeInitialNotional").append('=').append(JodaBeanUtils.toString(isExchangeInitialNotional())).append(',').append(' ');
    buf.append("exchangeFinalNotional").append('=').append(JodaBeanUtils.toString(isExchangeFinalNotional())).append(',').append(' ');
    buf.append("payLeg").append('=').append(JodaBeanUtils.toString(getPayLeg())).append(',').append(' ');
    buf.append("receiveLeg").append('=').append(JodaBeanUtils.toString(getReceiveLeg())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SwapSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code tradeDate} property.
     */
    private final MetaProperty<ZonedDateTime> _tradeDate = DirectMetaProperty.ofReadWrite(
        this, "tradeDate", SwapSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code effectiveDate} property.
     */
    private final MetaProperty<ZonedDateTime> _effectiveDate = DirectMetaProperty.ofReadWrite(
        this, "effectiveDate", SwapSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code maturityDate} property.
     */
    private final MetaProperty<ZonedDateTime> _maturityDate = DirectMetaProperty.ofReadWrite(
        this, "maturityDate", SwapSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code counterparty} property.
     */
    private final MetaProperty<String> _counterparty = DirectMetaProperty.ofReadWrite(
        this, "counterparty", SwapSecurity.class, String.class);
    /**
     * The meta-property for the {@code exchangeInitialNotional} property.
     */
    private final MetaProperty<Boolean> _exchangeInitialNotional = DirectMetaProperty.ofReadWrite(
        this, "exchangeInitialNotional", SwapSecurity.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code exchangeFinalNotional} property.
     */
    private final MetaProperty<Boolean> _exchangeFinalNotional = DirectMetaProperty.ofReadWrite(
        this, "exchangeFinalNotional", SwapSecurity.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code payLeg} property.
     */
    private final MetaProperty<SwapLeg> _payLeg = DirectMetaProperty.ofReadWrite(
        this, "payLeg", SwapSecurity.class, SwapLeg.class);
    /**
     * The meta-property for the {@code receiveLeg} property.
     */
    private final MetaProperty<SwapLeg> _receiveLeg = DirectMetaProperty.ofReadWrite(
        this, "receiveLeg", SwapSecurity.class, SwapLeg.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "tradeDate",
        "effectiveDate",
        "maturityDate",
        "counterparty",
        "exchangeInitialNotional",
        "exchangeFinalNotional",
        "payLeg",
        "receiveLeg");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 752419634:  // tradeDate
          return _tradeDate;
        case -930389515:  // effectiveDate
          return _effectiveDate;
        case -414641441:  // maturityDate
          return _maturityDate;
        case -1651301782:  // counterparty
          return _counterparty;
        case -1304307199:  // exchangeInitialNotional
          return _exchangeInitialNotional;
        case -1976228493:  // exchangeFinalNotional
          return _exchangeFinalNotional;
        case -995239866:  // payLeg
          return _payLeg;
        case 209233963:  // receiveLeg
          return _receiveLeg;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SwapSecurity> builder() {
      return new DirectBeanBuilder<>(new SwapSecurity());
    }

    @Override
    public Class<? extends SwapSecurity> beanType() {
      return SwapSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code tradeDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> tradeDate() {
      return _tradeDate;
    }

    /**
     * The meta-property for the {@code effectiveDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> effectiveDate() {
      return _effectiveDate;
    }

    /**
     * The meta-property for the {@code maturityDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> maturityDate() {
      return _maturityDate;
    }

    /**
     * The meta-property for the {@code counterparty} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> counterparty() {
      return _counterparty;
    }

    /**
     * The meta-property for the {@code exchangeInitialNotional} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> exchangeInitialNotional() {
      return _exchangeInitialNotional;
    }

    /**
     * The meta-property for the {@code exchangeFinalNotional} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> exchangeFinalNotional() {
      return _exchangeFinalNotional;
    }

    /**
     * The meta-property for the {@code payLeg} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SwapLeg> payLeg() {
      return _payLeg;
    }

    /**
     * The meta-property for the {@code receiveLeg} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SwapLeg> receiveLeg() {
      return _receiveLeg;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 752419634:  // tradeDate
          return ((SwapSecurity) bean).getTradeDate();
        case -930389515:  // effectiveDate
          return ((SwapSecurity) bean).getEffectiveDate();
        case -414641441:  // maturityDate
          return ((SwapSecurity) bean).getMaturityDate();
        case -1651301782:  // counterparty
          return ((SwapSecurity) bean).getCounterparty();
        case -1304307199:  // exchangeInitialNotional
          return ((SwapSecurity) bean).isExchangeInitialNotional();
        case -1976228493:  // exchangeFinalNotional
          return ((SwapSecurity) bean).isExchangeFinalNotional();
        case -995239866:  // payLeg
          return ((SwapSecurity) bean).getPayLeg();
        case 209233963:  // receiveLeg
          return ((SwapSecurity) bean).getReceiveLeg();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 752419634:  // tradeDate
          ((SwapSecurity) bean).setTradeDate((ZonedDateTime) newValue);
          return;
        case -930389515:  // effectiveDate
          ((SwapSecurity) bean).setEffectiveDate((ZonedDateTime) newValue);
          return;
        case -414641441:  // maturityDate
          ((SwapSecurity) bean).setMaturityDate((ZonedDateTime) newValue);
          return;
        case -1651301782:  // counterparty
          ((SwapSecurity) bean).setCounterparty((String) newValue);
          return;
        case -1304307199:  // exchangeInitialNotional
          ((SwapSecurity) bean).setExchangeInitialNotional((Boolean) newValue);
          return;
        case -1976228493:  // exchangeFinalNotional
          ((SwapSecurity) bean).setExchangeFinalNotional((Boolean) newValue);
          return;
        case -995239866:  // payLeg
          ((SwapSecurity) bean).setPayLeg((SwapLeg) newValue);
          return;
        case 209233963:  // receiveLeg
          ((SwapSecurity) bean).setReceiveLeg((SwapLeg) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((SwapSecurity) bean)._tradeDate, "tradeDate");
      JodaBeanUtils.notNull(((SwapSecurity) bean)._effectiveDate, "effectiveDate");
      JodaBeanUtils.notNull(((SwapSecurity) bean)._maturityDate, "maturityDate");
      JodaBeanUtils.notNull(((SwapSecurity) bean)._counterparty, "counterparty");
      JodaBeanUtils.notNull(((SwapSecurity) bean)._payLeg, "payLeg");
      JodaBeanUtils.notNull(((SwapSecurity) bean)._receiveLeg, "receiveLeg");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
