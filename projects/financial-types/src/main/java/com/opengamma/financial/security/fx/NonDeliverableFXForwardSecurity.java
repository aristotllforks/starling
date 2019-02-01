/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.fx;

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
import org.threeten.bp.ZonedDateTime;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.id.ExternalId;
import com.opengamma.master.security.SecurityDescription;
import com.opengamma.util.money.Currency;

/**
 * A security for FX forwards.
 */
@BeanDefinition
@SecurityDescription(type = NonDeliverableFXForwardSecurity.SECURITY_TYPE, description = "Non deliverable fx forward")
public class NonDeliverableFXForwardSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "NONDELIVERABLE_FX_FORWARD";

  /**
   * The payer currency
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _payCurrency;

  /**
   * The pay amount
   */
  @PropertyDefinition
  private double _payAmount;

  /**
   * The receiver currency
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _receiveCurrency;

  /**
   * The receive amount
   */
  @PropertyDefinition
  private double _receiveAmount;

  /**
   * The forward date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _forwardDate;
  /**
   * The region.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _regionId;
  /**
   * Whether to deliver in the receive currency
   */
  @PropertyDefinition
  private boolean _deliverInReceiveCurrency;

  /**
   * For the builder.
   */
  NonDeliverableFXForwardSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * @param payCurrency
   *          the pay currency, not null
   * @param payAmount
   *          the pay amount
   * @param receiveCurrency
   *          the receive currency, not null
   * @param receiveAmount
   *          the receive amount
   * @param forwardDate
   *          the forward date, not null
   * @param region
   *          the holiday region, not null
   * @param deliverInReceiveCurrency
   *          true to deliver in the receive currency
   */
  public NonDeliverableFXForwardSecurity(final Currency payCurrency, final double payAmount, final Currency receiveCurrency, final double receiveAmount,
      final ZonedDateTime forwardDate, final ExternalId region, final boolean deliverInReceiveCurrency) {
    super(SECURITY_TYPE);
    setPayCurrency(payCurrency);
    setPayAmount(payAmount);
    setReceiveCurrency(receiveCurrency);
    setReceiveAmount(receiveAmount);
    setForwardDate(forwardDate);
    setRegionId(region);
    setDeliverInReceiveCurrency(deliverInReceiveCurrency);
  }

  //-------------------------------------------------------------------------
  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitNonDeliverableFXForwardSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code NonDeliverableFXForwardSecurity}.
   * @return the meta-bean, not null
   */
  public static NonDeliverableFXForwardSecurity.Meta meta() {
    return NonDeliverableFXForwardSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(NonDeliverableFXForwardSecurity.Meta.INSTANCE);
  }

  @Override
  public NonDeliverableFXForwardSecurity.Meta metaBean() {
    return NonDeliverableFXForwardSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the payer currency
   * @return the value of the property, not null
   */
  public Currency getPayCurrency() {
    return _payCurrency;
  }

  /**
   * Sets the payer currency
   * @param payCurrency  the new value of the property, not null
   */
  public void setPayCurrency(Currency payCurrency) {
    JodaBeanUtils.notNull(payCurrency, "payCurrency");
    this._payCurrency = payCurrency;
  }

  /**
   * Gets the the {@code payCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> payCurrency() {
    return metaBean().payCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the pay amount
   * @return the value of the property
   */
  public double getPayAmount() {
    return _payAmount;
  }

  /**
   * Sets the pay amount
   * @param payAmount  the new value of the property
   */
  public void setPayAmount(double payAmount) {
    this._payAmount = payAmount;
  }

  /**
   * Gets the the {@code payAmount} property.
   * @return the property, not null
   */
  public final Property<Double> payAmount() {
    return metaBean().payAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the receiver currency
   * @return the value of the property, not null
   */
  public Currency getReceiveCurrency() {
    return _receiveCurrency;
  }

  /**
   * Sets the receiver currency
   * @param receiveCurrency  the new value of the property, not null
   */
  public void setReceiveCurrency(Currency receiveCurrency) {
    JodaBeanUtils.notNull(receiveCurrency, "receiveCurrency");
    this._receiveCurrency = receiveCurrency;
  }

  /**
   * Gets the the {@code receiveCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> receiveCurrency() {
    return metaBean().receiveCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the receive amount
   * @return the value of the property
   */
  public double getReceiveAmount() {
    return _receiveAmount;
  }

  /**
   * Sets the receive amount
   * @param receiveAmount  the new value of the property
   */
  public void setReceiveAmount(double receiveAmount) {
    this._receiveAmount = receiveAmount;
  }

  /**
   * Gets the the {@code receiveAmount} property.
   * @return the property, not null
   */
  public final Property<Double> receiveAmount() {
    return metaBean().receiveAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the forward date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getForwardDate() {
    return _forwardDate;
  }

  /**
   * Sets the forward date.
   * @param forwardDate  the new value of the property, not null
   */
  public void setForwardDate(ZonedDateTime forwardDate) {
    JodaBeanUtils.notNull(forwardDate, "forwardDate");
    this._forwardDate = forwardDate;
  }

  /**
   * Gets the the {@code forwardDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> forwardDate() {
    return metaBean().forwardDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the region.
   * @return the value of the property, not null
   */
  public ExternalId getRegionId() {
    return _regionId;
  }

  /**
   * Sets the region.
   * @param regionId  the new value of the property, not null
   */
  public void setRegionId(ExternalId regionId) {
    JodaBeanUtils.notNull(regionId, "regionId");
    this._regionId = regionId;
  }

  /**
   * Gets the the {@code regionId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> regionId() {
    return metaBean().regionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether to deliver in the receive currency
   * @return the value of the property
   */
  public boolean isDeliverInReceiveCurrency() {
    return _deliverInReceiveCurrency;
  }

  /**
   * Sets whether to deliver in the receive currency
   * @param deliverInReceiveCurrency  the new value of the property
   */
  public void setDeliverInReceiveCurrency(boolean deliverInReceiveCurrency) {
    this._deliverInReceiveCurrency = deliverInReceiveCurrency;
  }

  /**
   * Gets the the {@code deliverInReceiveCurrency} property.
   * @return the property, not null
   */
  public final Property<Boolean> deliverInReceiveCurrency() {
    return metaBean().deliverInReceiveCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public NonDeliverableFXForwardSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      NonDeliverableFXForwardSecurity other = (NonDeliverableFXForwardSecurity) obj;
      return JodaBeanUtils.equal(getPayCurrency(), other.getPayCurrency()) &&
          JodaBeanUtils.equal(getPayAmount(), other.getPayAmount()) &&
          JodaBeanUtils.equal(getReceiveCurrency(), other.getReceiveCurrency()) &&
          JodaBeanUtils.equal(getReceiveAmount(), other.getReceiveAmount()) &&
          JodaBeanUtils.equal(getForwardDate(), other.getForwardDate()) &&
          JodaBeanUtils.equal(getRegionId(), other.getRegionId()) &&
          (isDeliverInReceiveCurrency() == other.isDeliverInReceiveCurrency()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getPayCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPayAmount());
    hash = hash * 31 + JodaBeanUtils.hashCode(getReceiveCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getReceiveAmount());
    hash = hash * 31 + JodaBeanUtils.hashCode(getForwardDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegionId());
    hash = hash * 31 + JodaBeanUtils.hashCode(isDeliverInReceiveCurrency());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(256);
    buf.append("NonDeliverableFXForwardSecurity{");
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
    buf.append("payCurrency").append('=').append(JodaBeanUtils.toString(getPayCurrency())).append(',').append(' ');
    buf.append("payAmount").append('=').append(JodaBeanUtils.toString(getPayAmount())).append(',').append(' ');
    buf.append("receiveCurrency").append('=').append(JodaBeanUtils.toString(getReceiveCurrency())).append(',').append(' ');
    buf.append("receiveAmount").append('=').append(JodaBeanUtils.toString(getReceiveAmount())).append(',').append(' ');
    buf.append("forwardDate").append('=').append(JodaBeanUtils.toString(getForwardDate())).append(',').append(' ');
    buf.append("regionId").append('=').append(JodaBeanUtils.toString(getRegionId())).append(',').append(' ');
    buf.append("deliverInReceiveCurrency").append('=').append(JodaBeanUtils.toString(isDeliverInReceiveCurrency())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code NonDeliverableFXForwardSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code payCurrency} property.
     */
    private final MetaProperty<Currency> _payCurrency = DirectMetaProperty.ofReadWrite(
        this, "payCurrency", NonDeliverableFXForwardSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code payAmount} property.
     */
    private final MetaProperty<Double> _payAmount = DirectMetaProperty.ofReadWrite(
        this, "payAmount", NonDeliverableFXForwardSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code receiveCurrency} property.
     */
    private final MetaProperty<Currency> _receiveCurrency = DirectMetaProperty.ofReadWrite(
        this, "receiveCurrency", NonDeliverableFXForwardSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code receiveAmount} property.
     */
    private final MetaProperty<Double> _receiveAmount = DirectMetaProperty.ofReadWrite(
        this, "receiveAmount", NonDeliverableFXForwardSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code forwardDate} property.
     */
    private final MetaProperty<ZonedDateTime> _forwardDate = DirectMetaProperty.ofReadWrite(
        this, "forwardDate", NonDeliverableFXForwardSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code regionId} property.
     */
    private final MetaProperty<ExternalId> _regionId = DirectMetaProperty.ofReadWrite(
        this, "regionId", NonDeliverableFXForwardSecurity.class, ExternalId.class);
    /**
     * The meta-property for the {@code deliverInReceiveCurrency} property.
     */
    private final MetaProperty<Boolean> _deliverInReceiveCurrency = DirectMetaProperty.ofReadWrite(
        this, "deliverInReceiveCurrency", NonDeliverableFXForwardSecurity.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "payCurrency",
        "payAmount",
        "receiveCurrency",
        "receiveAmount",
        "forwardDate",
        "regionId",
        "deliverInReceiveCurrency");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -295641895:  // payCurrency
          return _payCurrency;
        case -1338781920:  // payAmount
          return _payAmount;
        case -1228590060:  // receiveCurrency
          return _receiveCurrency;
        case 984267035:  // receiveAmount
          return _receiveAmount;
        case 1652755475:  // forwardDate
          return _forwardDate;
        case -690339025:  // regionId
          return _regionId;
        case 2073187722:  // deliverInReceiveCurrency
          return _deliverInReceiveCurrency;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends NonDeliverableFXForwardSecurity> builder() {
      return new DirectBeanBuilder<NonDeliverableFXForwardSecurity>(new NonDeliverableFXForwardSecurity());
    }

    @Override
    public Class<? extends NonDeliverableFXForwardSecurity> beanType() {
      return NonDeliverableFXForwardSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code payCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> payCurrency() {
      return _payCurrency;
    }

    /**
     * The meta-property for the {@code payAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> payAmount() {
      return _payAmount;
    }

    /**
     * The meta-property for the {@code receiveCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> receiveCurrency() {
      return _receiveCurrency;
    }

    /**
     * The meta-property for the {@code receiveAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> receiveAmount() {
      return _receiveAmount;
    }

    /**
     * The meta-property for the {@code forwardDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> forwardDate() {
      return _forwardDate;
    }

    /**
     * The meta-property for the {@code regionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> regionId() {
      return _regionId;
    }

    /**
     * The meta-property for the {@code deliverInReceiveCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> deliverInReceiveCurrency() {
      return _deliverInReceiveCurrency;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -295641895:  // payCurrency
          return ((NonDeliverableFXForwardSecurity) bean).getPayCurrency();
        case -1338781920:  // payAmount
          return ((NonDeliverableFXForwardSecurity) bean).getPayAmount();
        case -1228590060:  // receiveCurrency
          return ((NonDeliverableFXForwardSecurity) bean).getReceiveCurrency();
        case 984267035:  // receiveAmount
          return ((NonDeliverableFXForwardSecurity) bean).getReceiveAmount();
        case 1652755475:  // forwardDate
          return ((NonDeliverableFXForwardSecurity) bean).getForwardDate();
        case -690339025:  // regionId
          return ((NonDeliverableFXForwardSecurity) bean).getRegionId();
        case 2073187722:  // deliverInReceiveCurrency
          return ((NonDeliverableFXForwardSecurity) bean).isDeliverInReceiveCurrency();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -295641895:  // payCurrency
          ((NonDeliverableFXForwardSecurity) bean).setPayCurrency((Currency) newValue);
          return;
        case -1338781920:  // payAmount
          ((NonDeliverableFXForwardSecurity) bean).setPayAmount((Double) newValue);
          return;
        case -1228590060:  // receiveCurrency
          ((NonDeliverableFXForwardSecurity) bean).setReceiveCurrency((Currency) newValue);
          return;
        case 984267035:  // receiveAmount
          ((NonDeliverableFXForwardSecurity) bean).setReceiveAmount((Double) newValue);
          return;
        case 1652755475:  // forwardDate
          ((NonDeliverableFXForwardSecurity) bean).setForwardDate((ZonedDateTime) newValue);
          return;
        case -690339025:  // regionId
          ((NonDeliverableFXForwardSecurity) bean).setRegionId((ExternalId) newValue);
          return;
        case 2073187722:  // deliverInReceiveCurrency
          ((NonDeliverableFXForwardSecurity) bean).setDeliverInReceiveCurrency((Boolean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((NonDeliverableFXForwardSecurity) bean)._payCurrency, "payCurrency");
      JodaBeanUtils.notNull(((NonDeliverableFXForwardSecurity) bean)._receiveCurrency, "receiveCurrency");
      JodaBeanUtils.notNull(((NonDeliverableFXForwardSecurity) bean)._forwardDate, "forwardDate");
      JodaBeanUtils.notNull(((NonDeliverableFXForwardSecurity) bean)._regionId, "regionId");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
