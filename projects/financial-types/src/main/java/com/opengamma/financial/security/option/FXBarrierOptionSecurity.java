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
import org.threeten.bp.ZonedDateTime;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.financial.security.LongShort;
import com.opengamma.master.security.SecurityDescription;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;

/**
 * A security for FX single-barrier options.
 */
@BeanDefinition
@SecurityDescription(type = FXBarrierOptionSecurity.SECURITY_TYPE, description = "Fx barrier option")
public class FXBarrierOptionSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "FX_BARRIER_OPTION";

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
   * The barrier type.
   */
  @PropertyDefinition(validate = "notNull")
  private BarrierType _barrierType;
  /**
   * The barrier direction.
   */
  @PropertyDefinition(validate = "notNull")
  private BarrierDirection _barrierDirection;
  /**
   * The monitoring type.
   */
  @PropertyDefinition(validate = "notNull")
  private MonitoringType _monitoringType;
  /**
   * The sampling frequency.
   */
  @PropertyDefinition(validate = "notNull")
  private SamplingFrequency _samplingFrequency;
  /**
   * The barrier level.
   */
  @PropertyDefinition(validate = "notNull")
  private double _barrierLevel;
  /**
   * The long/short type.
   */
  @PropertyDefinition(validate = "notNull")
  private LongShort _longShort = LongShort.LONG;

  /**
   * For the builder.
   */
  FXBarrierOptionSecurity() {
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
   * @param barrierType
   *          the barrier type, not null
   * @param barrierDirection
   *          the barrier direction, not null
   * @param monitoringType
   *          the spot rate monitoring type, not null
   * @param samplingFrequency
   *          the spot rate sampling frequency, not null
   * @param barrierLevel
   *          the barrier level
   * @param isLong
   *          true if the option is long, false if it is short
   */
  public FXBarrierOptionSecurity(final Currency putCurrency, final Currency callCurrency, final double putAmount, final double callAmount, final Expiry expiry,
      final ZonedDateTime settlementDate, final BarrierType barrierType, final BarrierDirection barrierDirection, final MonitoringType monitoringType,
      final SamplingFrequency samplingFrequency, final double barrierLevel, final boolean isLong) {
    super(SECURITY_TYPE);
    setPutCurrency(putCurrency);
    setCallCurrency(callCurrency);
    setPutAmount(putAmount);
    setCallAmount(callAmount);
    setExpiry(expiry);
    setSettlementDate(settlementDate);
    setBarrierType(barrierType);
    setBarrierDirection(barrierDirection);
    setMonitoringType(monitoringType);
    setSamplingFrequency(samplingFrequency);
    setBarrierLevel(barrierLevel);
    setLongShort(LongShort.ofLong(isLong));
  }

  //-------------------------------------------------------------------------
  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitFXBarrierOptionSecurity(this);
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
   * The meta-bean for {@code FXBarrierOptionSecurity}.
   * @return the meta-bean, not null
   */
  public static FXBarrierOptionSecurity.Meta meta() {
    return FXBarrierOptionSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(FXBarrierOptionSecurity.Meta.INSTANCE);
  }

  @Override
  public FXBarrierOptionSecurity.Meta metaBean() {
    return FXBarrierOptionSecurity.Meta.INSTANCE;
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
   * Gets the barrier type.
   * @return the value of the property, not null
   */
  public BarrierType getBarrierType() {
    return _barrierType;
  }

  /**
   * Sets the barrier type.
   * @param barrierType  the new value of the property, not null
   */
  public void setBarrierType(BarrierType barrierType) {
    JodaBeanUtils.notNull(barrierType, "barrierType");
    this._barrierType = barrierType;
  }

  /**
   * Gets the the {@code barrierType} property.
   * @return the property, not null
   */
  public final Property<BarrierType> barrierType() {
    return metaBean().barrierType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the barrier direction.
   * @return the value of the property, not null
   */
  public BarrierDirection getBarrierDirection() {
    return _barrierDirection;
  }

  /**
   * Sets the barrier direction.
   * @param barrierDirection  the new value of the property, not null
   */
  public void setBarrierDirection(BarrierDirection barrierDirection) {
    JodaBeanUtils.notNull(barrierDirection, "barrierDirection");
    this._barrierDirection = barrierDirection;
  }

  /**
   * Gets the the {@code barrierDirection} property.
   * @return the property, not null
   */
  public final Property<BarrierDirection> barrierDirection() {
    return metaBean().barrierDirection().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the monitoring type.
   * @return the value of the property, not null
   */
  public MonitoringType getMonitoringType() {
    return _monitoringType;
  }

  /**
   * Sets the monitoring type.
   * @param monitoringType  the new value of the property, not null
   */
  public void setMonitoringType(MonitoringType monitoringType) {
    JodaBeanUtils.notNull(monitoringType, "monitoringType");
    this._monitoringType = monitoringType;
  }

  /**
   * Gets the the {@code monitoringType} property.
   * @return the property, not null
   */
  public final Property<MonitoringType> monitoringType() {
    return metaBean().monitoringType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the sampling frequency.
   * @return the value of the property, not null
   */
  public SamplingFrequency getSamplingFrequency() {
    return _samplingFrequency;
  }

  /**
   * Sets the sampling frequency.
   * @param samplingFrequency  the new value of the property, not null
   */
  public void setSamplingFrequency(SamplingFrequency samplingFrequency) {
    JodaBeanUtils.notNull(samplingFrequency, "samplingFrequency");
    this._samplingFrequency = samplingFrequency;
  }

  /**
   * Gets the the {@code samplingFrequency} property.
   * @return the property, not null
   */
  public final Property<SamplingFrequency> samplingFrequency() {
    return metaBean().samplingFrequency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the barrier level.
   * @return the value of the property, not null
   */
  public double getBarrierLevel() {
    return _barrierLevel;
  }

  /**
   * Sets the barrier level.
   * @param barrierLevel  the new value of the property, not null
   */
  public void setBarrierLevel(double barrierLevel) {
    JodaBeanUtils.notNull(barrierLevel, "barrierLevel");
    this._barrierLevel = barrierLevel;
  }

  /**
   * Gets the the {@code barrierLevel} property.
   * @return the property, not null
   */
  public final Property<Double> barrierLevel() {
    return metaBean().barrierLevel().createProperty(this);
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
  @Override
  public FXBarrierOptionSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FXBarrierOptionSecurity other = (FXBarrierOptionSecurity) obj;
      return JodaBeanUtils.equal(getPutCurrency(), other.getPutCurrency()) &&
          JodaBeanUtils.equal(getCallCurrency(), other.getCallCurrency()) &&
          JodaBeanUtils.equal(getPutAmount(), other.getPutAmount()) &&
          JodaBeanUtils.equal(getCallAmount(), other.getCallAmount()) &&
          JodaBeanUtils.equal(getExpiry(), other.getExpiry()) &&
          JodaBeanUtils.equal(getSettlementDate(), other.getSettlementDate()) &&
          JodaBeanUtils.equal(getBarrierType(), other.getBarrierType()) &&
          JodaBeanUtils.equal(getBarrierDirection(), other.getBarrierDirection()) &&
          JodaBeanUtils.equal(getMonitoringType(), other.getMonitoringType()) &&
          JodaBeanUtils.equal(getSamplingFrequency(), other.getSamplingFrequency()) &&
          JodaBeanUtils.equal(getBarrierLevel(), other.getBarrierLevel()) &&
          JodaBeanUtils.equal(getLongShort(), other.getLongShort()) &&
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
    hash = hash * 31 + JodaBeanUtils.hashCode(getBarrierType());
    hash = hash * 31 + JodaBeanUtils.hashCode(getBarrierDirection());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMonitoringType());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSamplingFrequency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getBarrierLevel());
    hash = hash * 31 + JodaBeanUtils.hashCode(getLongShort());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(416);
    buf.append("FXBarrierOptionSecurity{");
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
    buf.append("barrierType").append('=').append(JodaBeanUtils.toString(getBarrierType())).append(',').append(' ');
    buf.append("barrierDirection").append('=').append(JodaBeanUtils.toString(getBarrierDirection())).append(',').append(' ');
    buf.append("monitoringType").append('=').append(JodaBeanUtils.toString(getMonitoringType())).append(',').append(' ');
    buf.append("samplingFrequency").append('=').append(JodaBeanUtils.toString(getSamplingFrequency())).append(',').append(' ');
    buf.append("barrierLevel").append('=').append(JodaBeanUtils.toString(getBarrierLevel())).append(',').append(' ');
    buf.append("longShort").append('=').append(JodaBeanUtils.toString(getLongShort())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FXBarrierOptionSecurity}.
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
        this, "putCurrency", FXBarrierOptionSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code callCurrency} property.
     */
    private final MetaProperty<Currency> _callCurrency = DirectMetaProperty.ofReadWrite(
        this, "callCurrency", FXBarrierOptionSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code putAmount} property.
     */
    private final MetaProperty<Double> _putAmount = DirectMetaProperty.ofReadWrite(
        this, "putAmount", FXBarrierOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code callAmount} property.
     */
    private final MetaProperty<Double> _callAmount = DirectMetaProperty.ofReadWrite(
        this, "callAmount", FXBarrierOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<Expiry> _expiry = DirectMetaProperty.ofReadWrite(
        this, "expiry", FXBarrierOptionSecurity.class, Expiry.class);
    /**
     * The meta-property for the {@code settlementDate} property.
     */
    private final MetaProperty<ZonedDateTime> _settlementDate = DirectMetaProperty.ofReadWrite(
        this, "settlementDate", FXBarrierOptionSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code barrierType} property.
     */
    private final MetaProperty<BarrierType> _barrierType = DirectMetaProperty.ofReadWrite(
        this, "barrierType", FXBarrierOptionSecurity.class, BarrierType.class);
    /**
     * The meta-property for the {@code barrierDirection} property.
     */
    private final MetaProperty<BarrierDirection> _barrierDirection = DirectMetaProperty.ofReadWrite(
        this, "barrierDirection", FXBarrierOptionSecurity.class, BarrierDirection.class);
    /**
     * The meta-property for the {@code monitoringType} property.
     */
    private final MetaProperty<MonitoringType> _monitoringType = DirectMetaProperty.ofReadWrite(
        this, "monitoringType", FXBarrierOptionSecurity.class, MonitoringType.class);
    /**
     * The meta-property for the {@code samplingFrequency} property.
     */
    private final MetaProperty<SamplingFrequency> _samplingFrequency = DirectMetaProperty.ofReadWrite(
        this, "samplingFrequency", FXBarrierOptionSecurity.class, SamplingFrequency.class);
    /**
     * The meta-property for the {@code barrierLevel} property.
     */
    private final MetaProperty<Double> _barrierLevel = DirectMetaProperty.ofReadWrite(
        this, "barrierLevel", FXBarrierOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code longShort} property.
     */
    private final MetaProperty<LongShort> _longShort = DirectMetaProperty.ofReadWrite(
        this, "longShort", FXBarrierOptionSecurity.class, LongShort.class);
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
        "barrierType",
        "barrierDirection",
        "monitoringType",
        "samplingFrequency",
        "barrierLevel",
        "longShort");

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
        case 1029043089:  // barrierType
          return _barrierType;
        case 502579592:  // barrierDirection
          return _barrierDirection;
        case -1483652190:  // monitoringType
          return _monitoringType;
        case 1178782005:  // samplingFrequency
          return _samplingFrequency;
        case 1827586573:  // barrierLevel
          return _barrierLevel;
        case 116685664:  // longShort
          return _longShort;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FXBarrierOptionSecurity> builder() {
      return new DirectBeanBuilder<FXBarrierOptionSecurity>(new FXBarrierOptionSecurity());
    }

    @Override
    public Class<? extends FXBarrierOptionSecurity> beanType() {
      return FXBarrierOptionSecurity.class;
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
     * The meta-property for the {@code barrierType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BarrierType> barrierType() {
      return _barrierType;
    }

    /**
     * The meta-property for the {@code barrierDirection} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BarrierDirection> barrierDirection() {
      return _barrierDirection;
    }

    /**
     * The meta-property for the {@code monitoringType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<MonitoringType> monitoringType() {
      return _monitoringType;
    }

    /**
     * The meta-property for the {@code samplingFrequency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SamplingFrequency> samplingFrequency() {
      return _samplingFrequency;
    }

    /**
     * The meta-property for the {@code barrierLevel} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> barrierLevel() {
      return _barrierLevel;
    }

    /**
     * The meta-property for the {@code longShort} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LongShort> longShort() {
      return _longShort;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 516393024:  // putCurrency
          return ((FXBarrierOptionSecurity) bean).getPutCurrency();
        case 643534991:  // callCurrency
          return ((FXBarrierOptionSecurity) bean).getCallCurrency();
        case -984864697:  // putAmount
          return ((FXBarrierOptionSecurity) bean).getPutAmount();
        case 1066661974:  // callAmount
          return ((FXBarrierOptionSecurity) bean).getCallAmount();
        case -1289159373:  // expiry
          return ((FXBarrierOptionSecurity) bean).getExpiry();
        case -295948169:  // settlementDate
          return ((FXBarrierOptionSecurity) bean).getSettlementDate();
        case 1029043089:  // barrierType
          return ((FXBarrierOptionSecurity) bean).getBarrierType();
        case 502579592:  // barrierDirection
          return ((FXBarrierOptionSecurity) bean).getBarrierDirection();
        case -1483652190:  // monitoringType
          return ((FXBarrierOptionSecurity) bean).getMonitoringType();
        case 1178782005:  // samplingFrequency
          return ((FXBarrierOptionSecurity) bean).getSamplingFrequency();
        case 1827586573:  // barrierLevel
          return ((FXBarrierOptionSecurity) bean).getBarrierLevel();
        case 116685664:  // longShort
          return ((FXBarrierOptionSecurity) bean).getLongShort();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 516393024:  // putCurrency
          ((FXBarrierOptionSecurity) bean).setPutCurrency((Currency) newValue);
          return;
        case 643534991:  // callCurrency
          ((FXBarrierOptionSecurity) bean).setCallCurrency((Currency) newValue);
          return;
        case -984864697:  // putAmount
          ((FXBarrierOptionSecurity) bean).setPutAmount((Double) newValue);
          return;
        case 1066661974:  // callAmount
          ((FXBarrierOptionSecurity) bean).setCallAmount((Double) newValue);
          return;
        case -1289159373:  // expiry
          ((FXBarrierOptionSecurity) bean).setExpiry((Expiry) newValue);
          return;
        case -295948169:  // settlementDate
          ((FXBarrierOptionSecurity) bean).setSettlementDate((ZonedDateTime) newValue);
          return;
        case 1029043089:  // barrierType
          ((FXBarrierOptionSecurity) bean).setBarrierType((BarrierType) newValue);
          return;
        case 502579592:  // barrierDirection
          ((FXBarrierOptionSecurity) bean).setBarrierDirection((BarrierDirection) newValue);
          return;
        case -1483652190:  // monitoringType
          ((FXBarrierOptionSecurity) bean).setMonitoringType((MonitoringType) newValue);
          return;
        case 1178782005:  // samplingFrequency
          ((FXBarrierOptionSecurity) bean).setSamplingFrequency((SamplingFrequency) newValue);
          return;
        case 1827586573:  // barrierLevel
          ((FXBarrierOptionSecurity) bean).setBarrierLevel((Double) newValue);
          return;
        case 116685664:  // longShort
          ((FXBarrierOptionSecurity) bean).setLongShort((LongShort) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._putCurrency, "putCurrency");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._callCurrency, "callCurrency");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._putAmount, "putAmount");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._callAmount, "callAmount");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._expiry, "expiry");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._settlementDate, "settlementDate");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._barrierType, "barrierType");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._barrierDirection, "barrierDirection");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._monitoringType, "monitoringType");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._samplingFrequency, "samplingFrequency");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._barrierLevel, "barrierLevel");
      JodaBeanUtils.notNull(((FXBarrierOptionSecurity) bean)._longShort, "longShort");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
