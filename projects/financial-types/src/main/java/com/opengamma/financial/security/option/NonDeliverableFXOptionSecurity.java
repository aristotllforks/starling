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
import java.time.ZonedDateTime;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.financial.security.LongShort;
import com.opengamma.master.security.SecurityDescription;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;

/**
 * A security for Non-deliverable FX options.
 */
@BeanDefinition
@SecurityDescription(type = NonDeliverableFXOptionSecurity.SECURITY_TYPE, description = "Non deliverable fx option")
public class NonDeliverableFXOptionSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "NONDELIVERABLE_FX_OPTION";
  /**
   * The put currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _putCurrency;
  /**
   * The call currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _callCurrency;
  /**
   * The put amount.
   */
  @PropertyDefinition(validate = "notNull")
  private double _putAmount;
  /**
   * The call amount.
   */
  @PropertyDefinition(validate = "notNull")
  private double _callAmount;
  /**
   * The expiry.
   */
  @PropertyDefinition(validate = "notNull")
  private Expiry _expiry;
  /**
   * The settlement date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _settlementDate;
  /**
   * The long/short type.
   */
  @PropertyDefinition(validate = "notNull")
  private LongShort _longShort = LongShort.LONG;
  /**
   * The exercise type.
   */
  @PropertyDefinition(validate = "notNull")
  private ExerciseType _exerciseType;

  /**
   * Whether the currency in which the settlement is made is the call currency (otherwise it's the put currency).
   */
  @PropertyDefinition
  private boolean _deliveryInCallCurrency;

  /**
   * For the builder.
   */
  NonDeliverableFXOptionSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * @param putCurrency
   *          the put currency, not null
   * @param callCurrency
   *          the call currency, not null
   * @param putAmount
   *          the put amount
   * @param callAmount
   *          the call amount
   * @param expiry
   *          the expiry, not null
   * @param settlementDate
   *          the settlement date, not null
   * @param isLong
   *          true if the option is long, false if it is short
   * @param exerciseType
   *          the exercise type, not null
   * @param deliveryInCallCurrency
   *          true to deliver the call currency, false to deliver in the put
   *          currency
   */
  public NonDeliverableFXOptionSecurity(final Currency putCurrency, final Currency callCurrency, final double putAmount, final double callAmount,
      final Expiry expiry,
      final ZonedDateTime settlementDate, final boolean isLong, final ExerciseType exerciseType, final boolean deliveryInCallCurrency) {
    super(SECURITY_TYPE);
    setPutCurrency(putCurrency);
    setCallCurrency(callCurrency);
    setPutAmount(putAmount);
    setCallAmount(callAmount);
    setExpiry(expiry);
    setSettlementDate(settlementDate);
    setLongShort(LongShort.ofLong(isLong));
    setExerciseType(exerciseType);
    setDeliveryInCallCurrency(deliveryInCallCurrency);
  }

  /**
   * Gets the delivery currency.
   *
   * @return the delivery currency
   */
  public Currency getDeliveryCurrency() {
    return isDeliveryInCallCurrency() ? getCallCurrency() : getPutCurrency();
  }

  //-------------------------------------------------------------------------
  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitNonDeliverableFXOptionSecurity(this);
  }

  //-------------------------------------------------------------------------
  /**
   * Checks if the long/short type is long.
   *
   * @return true if long, false if short
   */
  public boolean isLong() {
    return getLongShort().isLong();
  }

  /**
   * Checks if the long/short type is short.
   *
   * @return true if short, false if long
   */
  public boolean isShort() {
    return getLongShort().isShort();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code NonDeliverableFXOptionSecurity}.
   * @return the meta-bean, not null
   */
  public static NonDeliverableFXOptionSecurity.Meta meta() {
    return NonDeliverableFXOptionSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(NonDeliverableFXOptionSecurity.Meta.INSTANCE);
  }

  @Override
  public NonDeliverableFXOptionSecurity.Meta metaBean() {
    return NonDeliverableFXOptionSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the put currency.
   * @return the value of the property, not null
   */
  public Currency getPutCurrency() {
    return _putCurrency;
  }

  /**
   * Sets the put currency.
   * @param putCurrency  the new value of the property, not null
   */
  public void setPutCurrency(Currency putCurrency) {
    JodaBeanUtils.notNull(putCurrency, "putCurrency");
    this._putCurrency = putCurrency;
  }

  /**
   * Gets the the {@code putCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> putCurrency() {
    return metaBean().putCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the call currency.
   * @return the value of the property, not null
   */
  public Currency getCallCurrency() {
    return _callCurrency;
  }

  /**
   * Sets the call currency.
   * @param callCurrency  the new value of the property, not null
   */
  public void setCallCurrency(Currency callCurrency) {
    JodaBeanUtils.notNull(callCurrency, "callCurrency");
    this._callCurrency = callCurrency;
  }

  /**
   * Gets the the {@code callCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> callCurrency() {
    return metaBean().callCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the put amount.
   * @return the value of the property, not null
   */
  public double getPutAmount() {
    return _putAmount;
  }

  /**
   * Sets the put amount.
   * @param putAmount  the new value of the property, not null
   */
  public void setPutAmount(double putAmount) {
    JodaBeanUtils.notNull(putAmount, "putAmount");
    this._putAmount = putAmount;
  }

  /**
   * Gets the the {@code putAmount} property.
   * @return the property, not null
   */
  public final Property<Double> putAmount() {
    return metaBean().putAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the call amount.
   * @return the value of the property, not null
   */
  public double getCallAmount() {
    return _callAmount;
  }

  /**
   * Sets the call amount.
   * @param callAmount  the new value of the property, not null
   */
  public void setCallAmount(double callAmount) {
    JodaBeanUtils.notNull(callAmount, "callAmount");
    this._callAmount = callAmount;
  }

  /**
   * Gets the the {@code callAmount} property.
   * @return the property, not null
   */
  public final Property<Double> callAmount() {
    return metaBean().callAmount().createProperty(this);
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
   * Gets the settlement date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getSettlementDate() {
    return _settlementDate;
  }

  /**
   * Sets the settlement date.
   * @param settlementDate  the new value of the property, not null
   */
  public void setSettlementDate(ZonedDateTime settlementDate) {
    JodaBeanUtils.notNull(settlementDate, "settlementDate");
    this._settlementDate = settlementDate;
  }

  /**
   * Gets the the {@code settlementDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> settlementDate() {
    return metaBean().settlementDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the long/short type.
   * @return the value of the property, not null
   */
  public LongShort getLongShort() {
    return _longShort;
  }

  /**
   * Sets the long/short type.
   * @param longShort  the new value of the property, not null
   */
  public void setLongShort(LongShort longShort) {
    JodaBeanUtils.notNull(longShort, "longShort");
    this._longShort = longShort;
  }

  /**
   * Gets the the {@code longShort} property.
   * @return the property, not null
   */
  public final Property<LongShort> longShort() {
    return metaBean().longShort().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exercise type.
   * @return the value of the property, not null
   */
  public ExerciseType getExerciseType() {
    return _exerciseType;
  }

  /**
   * Sets the exercise type.
   * @param exerciseType  the new value of the property, not null
   */
  public void setExerciseType(ExerciseType exerciseType) {
    JodaBeanUtils.notNull(exerciseType, "exerciseType");
    this._exerciseType = exerciseType;
  }

  /**
   * Gets the the {@code exerciseType} property.
   * @return the property, not null
   */
  public final Property<ExerciseType> exerciseType() {
    return metaBean().exerciseType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether the currency in which the settlement is made is the call currency (otherwise it's the put currency).
   * @return the value of the property
   */
  public boolean isDeliveryInCallCurrency() {
    return _deliveryInCallCurrency;
  }

  /**
   * Sets whether the currency in which the settlement is made is the call currency (otherwise it's the put currency).
   * @param deliveryInCallCurrency  the new value of the property
   */
  public void setDeliveryInCallCurrency(boolean deliveryInCallCurrency) {
    this._deliveryInCallCurrency = deliveryInCallCurrency;
  }

  /**
   * Gets the the {@code deliveryInCallCurrency} property.
   * @return the property, not null
   */
  public final Property<Boolean> deliveryInCallCurrency() {
    return metaBean().deliveryInCallCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public NonDeliverableFXOptionSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      NonDeliverableFXOptionSecurity other = (NonDeliverableFXOptionSecurity) obj;
      return JodaBeanUtils.equal(getPutCurrency(), other.getPutCurrency()) &&
          JodaBeanUtils.equal(getCallCurrency(), other.getCallCurrency()) &&
          JodaBeanUtils.equal(getPutAmount(), other.getPutAmount()) &&
          JodaBeanUtils.equal(getCallAmount(), other.getCallAmount()) &&
          JodaBeanUtils.equal(getExpiry(), other.getExpiry()) &&
          JodaBeanUtils.equal(getSettlementDate(), other.getSettlementDate()) &&
          JodaBeanUtils.equal(getLongShort(), other.getLongShort()) &&
          JodaBeanUtils.equal(getExerciseType(), other.getExerciseType()) &&
          (isDeliveryInCallCurrency() == other.isDeliveryInCallCurrency()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getPutCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCallCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPutAmount());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCallAmount());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExpiry());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSettlementDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getLongShort());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExerciseType());
    hash = hash * 31 + JodaBeanUtils.hashCode(isDeliveryInCallCurrency());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(320);
    buf.append("NonDeliverableFXOptionSecurity{");
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
    buf.append("putCurrency").append('=').append(JodaBeanUtils.toString(getPutCurrency())).append(',').append(' ');
    buf.append("callCurrency").append('=').append(JodaBeanUtils.toString(getCallCurrency())).append(',').append(' ');
    buf.append("putAmount").append('=').append(JodaBeanUtils.toString(getPutAmount())).append(',').append(' ');
    buf.append("callAmount").append('=').append(JodaBeanUtils.toString(getCallAmount())).append(',').append(' ');
    buf.append("expiry").append('=').append(JodaBeanUtils.toString(getExpiry())).append(',').append(' ');
    buf.append("settlementDate").append('=').append(JodaBeanUtils.toString(getSettlementDate())).append(',').append(' ');
    buf.append("longShort").append('=').append(JodaBeanUtils.toString(getLongShort())).append(',').append(' ');
    buf.append("exerciseType").append('=').append(JodaBeanUtils.toString(getExerciseType())).append(',').append(' ');
    buf.append("deliveryInCallCurrency").append('=').append(JodaBeanUtils.toString(isDeliveryInCallCurrency())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code NonDeliverableFXOptionSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code putCurrency} property.
     */
    private final MetaProperty<Currency> _putCurrency = DirectMetaProperty.ofReadWrite(
        this, "putCurrency", NonDeliverableFXOptionSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code callCurrency} property.
     */
    private final MetaProperty<Currency> _callCurrency = DirectMetaProperty.ofReadWrite(
        this, "callCurrency", NonDeliverableFXOptionSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code putAmount} property.
     */
    private final MetaProperty<Double> _putAmount = DirectMetaProperty.ofReadWrite(
        this, "putAmount", NonDeliverableFXOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code callAmount} property.
     */
    private final MetaProperty<Double> _callAmount = DirectMetaProperty.ofReadWrite(
        this, "callAmount", NonDeliverableFXOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<Expiry> _expiry = DirectMetaProperty.ofReadWrite(
        this, "expiry", NonDeliverableFXOptionSecurity.class, Expiry.class);
    /**
     * The meta-property for the {@code settlementDate} property.
     */
    private final MetaProperty<ZonedDateTime> _settlementDate = DirectMetaProperty.ofReadWrite(
        this, "settlementDate", NonDeliverableFXOptionSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code longShort} property.
     */
    private final MetaProperty<LongShort> _longShort = DirectMetaProperty.ofReadWrite(
        this, "longShort", NonDeliverableFXOptionSecurity.class, LongShort.class);
    /**
     * The meta-property for the {@code exerciseType} property.
     */
    private final MetaProperty<ExerciseType> _exerciseType = DirectMetaProperty.ofReadWrite(
        this, "exerciseType", NonDeliverableFXOptionSecurity.class, ExerciseType.class);
    /**
     * The meta-property for the {@code deliveryInCallCurrency} property.
     */
    private final MetaProperty<Boolean> _deliveryInCallCurrency = DirectMetaProperty.ofReadWrite(
        this, "deliveryInCallCurrency", NonDeliverableFXOptionSecurity.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "putCurrency",
        "callCurrency",
        "putAmount",
        "callAmount",
        "expiry",
        "settlementDate",
        "longShort",
        "exerciseType",
        "deliveryInCallCurrency");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 516393024:  // putCurrency
          return _putCurrency;
        case 643534991:  // callCurrency
          return _callCurrency;
        case -984864697:  // putAmount
          return _putAmount;
        case 1066661974:  // callAmount
          return _callAmount;
        case -1289159373:  // expiry
          return _expiry;
        case -295948169:  // settlementDate
          return _settlementDate;
        case 116685664:  // longShort
          return _longShort;
        case -466331342:  // exerciseType
          return _exerciseType;
        case 106778472:  // deliveryInCallCurrency
          return _deliveryInCallCurrency;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends NonDeliverableFXOptionSecurity> builder() {
      return new DirectBeanBuilder<NonDeliverableFXOptionSecurity>(new NonDeliverableFXOptionSecurity());
    }

    @Override
    public Class<? extends NonDeliverableFXOptionSecurity> beanType() {
      return NonDeliverableFXOptionSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code putCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> putCurrency() {
      return _putCurrency;
    }

    /**
     * The meta-property for the {@code callCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> callCurrency() {
      return _callCurrency;
    }

    /**
     * The meta-property for the {@code putAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> putAmount() {
      return _putAmount;
    }

    /**
     * The meta-property for the {@code callAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> callAmount() {
      return _callAmount;
    }

    /**
     * The meta-property for the {@code expiry} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Expiry> expiry() {
      return _expiry;
    }

    /**
     * The meta-property for the {@code settlementDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> settlementDate() {
      return _settlementDate;
    }

    /**
     * The meta-property for the {@code longShort} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LongShort> longShort() {
      return _longShort;
    }

    /**
     * The meta-property for the {@code exerciseType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExerciseType> exerciseType() {
      return _exerciseType;
    }

    /**
     * The meta-property for the {@code deliveryInCallCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> deliveryInCallCurrency() {
      return _deliveryInCallCurrency;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 516393024:  // putCurrency
          return ((NonDeliverableFXOptionSecurity) bean).getPutCurrency();
        case 643534991:  // callCurrency
          return ((NonDeliverableFXOptionSecurity) bean).getCallCurrency();
        case -984864697:  // putAmount
          return ((NonDeliverableFXOptionSecurity) bean).getPutAmount();
        case 1066661974:  // callAmount
          return ((NonDeliverableFXOptionSecurity) bean).getCallAmount();
        case -1289159373:  // expiry
          return ((NonDeliverableFXOptionSecurity) bean).getExpiry();
        case -295948169:  // settlementDate
          return ((NonDeliverableFXOptionSecurity) bean).getSettlementDate();
        case 116685664:  // longShort
          return ((NonDeliverableFXOptionSecurity) bean).getLongShort();
        case -466331342:  // exerciseType
          return ((NonDeliverableFXOptionSecurity) bean).getExerciseType();
        case 106778472:  // deliveryInCallCurrency
          return ((NonDeliverableFXOptionSecurity) bean).isDeliveryInCallCurrency();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 516393024:  // putCurrency
          ((NonDeliverableFXOptionSecurity) bean).setPutCurrency((Currency) newValue);
          return;
        case 643534991:  // callCurrency
          ((NonDeliverableFXOptionSecurity) bean).setCallCurrency((Currency) newValue);
          return;
        case -984864697:  // putAmount
          ((NonDeliverableFXOptionSecurity) bean).setPutAmount((Double) newValue);
          return;
        case 1066661974:  // callAmount
          ((NonDeliverableFXOptionSecurity) bean).setCallAmount((Double) newValue);
          return;
        case -1289159373:  // expiry
          ((NonDeliverableFXOptionSecurity) bean).setExpiry((Expiry) newValue);
          return;
        case -295948169:  // settlementDate
          ((NonDeliverableFXOptionSecurity) bean).setSettlementDate((ZonedDateTime) newValue);
          return;
        case 116685664:  // longShort
          ((NonDeliverableFXOptionSecurity) bean).setLongShort((LongShort) newValue);
          return;
        case -466331342:  // exerciseType
          ((NonDeliverableFXOptionSecurity) bean).setExerciseType((ExerciseType) newValue);
          return;
        case 106778472:  // deliveryInCallCurrency
          ((NonDeliverableFXOptionSecurity) bean).setDeliveryInCallCurrency((Boolean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((NonDeliverableFXOptionSecurity) bean)._putCurrency, "putCurrency");
      JodaBeanUtils.notNull(((NonDeliverableFXOptionSecurity) bean)._callCurrency, "callCurrency");
      JodaBeanUtils.notNull(((NonDeliverableFXOptionSecurity) bean)._putAmount, "putAmount");
      JodaBeanUtils.notNull(((NonDeliverableFXOptionSecurity) bean)._callAmount, "callAmount");
      JodaBeanUtils.notNull(((NonDeliverableFXOptionSecurity) bean)._expiry, "expiry");
      JodaBeanUtils.notNull(((NonDeliverableFXOptionSecurity) bean)._settlementDate, "settlementDate");
      JodaBeanUtils.notNull(((NonDeliverableFXOptionSecurity) bean)._longShort, "longShort");
      JodaBeanUtils.notNull(((NonDeliverableFXOptionSecurity) bean)._exerciseType, "exerciseType");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
