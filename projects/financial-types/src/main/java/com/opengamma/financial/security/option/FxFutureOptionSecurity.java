/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.option;

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
import com.opengamma.id.ExternalId;
import com.opengamma.master.security.SecurityDescription;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * A security for FX future options.
 */
@BeanDefinition
@SecurityDescription(type = FxFutureOptionSecurity.SECURITY_TYPE, description = "Fx future option")
public class FxFutureOptionSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "FXFUTURE_OPTION";

  /**
   * The exchange.
   */
  @PropertyDefinition(validate = "notNull")
  private String _tradingExchange;
  /**
   * The settlement exchange.
   */
  @PropertyDefinition(validate = "notNull")
  private String _settlementExchange;
  /**
   * The expiry.
   */
  @PropertyDefinition(validate = "notNull")
  private Expiry _expiry;
  /**
   * The exercise type.
   */
  @PropertyDefinition(validate = "notNull")
  private ExerciseType _exerciseType;
  /**
   * The underlying identifier.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _underlyingId;
  /**
   * The point value.
   */
  @PropertyDefinition
  private double _pointValue;
  /**
   * The currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;
  /**
   * The strike.
   */
  @PropertyDefinition
  private double _strike;
  /**
   * The option type.
   */
  @PropertyDefinition(validate = "notNull")
  private OptionType _optionType;

  /**
   * For the builder.
   */
  FxFutureOptionSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * @param tradingExchange
   *          the exchange name, not null
   * @param settlementExchange
   *          the exchange name, not null
   * @param expiry
   *          the expiry, not null
   * @param exerciseType
   *          the exercise type, not null
   * @param underlyingIdentifier
   *          the identifier of the underlying index, not null
   * @param pointValue
   *          the value of a point, not null
   * @param currency
   *          the currency, not null
   * @param strike
   *          the strike
   * @param optionType
   *          the option type, not null
   */
  public FxFutureOptionSecurity(final String tradingExchange,
      final String settlementExchange,
      final Expiry expiry,
      final ExerciseType exerciseType,
      final ExternalId underlyingIdentifier,
      final double pointValue,
      final Currency currency,
      final double strike,
      final OptionType optionType) {
    super(SECURITY_TYPE);
    setTradingExchange(tradingExchange);
    setSettlementExchange(settlementExchange);
    setExpiry(expiry);
    setExerciseType(exerciseType);
    setUnderlyingId(underlyingIdentifier);
    setPointValue(pointValue);
    setCurrency(currency);
    setStrike(strike);
    setOptionType(optionType);
  }

  //-------------------------------------------------------------------------
  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitFxFutureOptionSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FxFutureOptionSecurity}.
   * @return the meta-bean, not null
   */
  public static FxFutureOptionSecurity.Meta meta() {
    return FxFutureOptionSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(FxFutureOptionSecurity.Meta.INSTANCE);
  }

  @Override
  public FxFutureOptionSecurity.Meta metaBean() {
    return FxFutureOptionSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange.
   * @return the value of the property, not null
   */
  public String getTradingExchange() {
    return _tradingExchange;
  }

  /**
   * Sets the exchange.
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
   * Gets the underlying identifier.
   * @return the value of the property, not null
   */
  public ExternalId getUnderlyingId() {
    return _underlyingId;
  }

  /**
   * Sets the underlying identifier.
   * @param underlyingId  the new value of the property, not null
   */
  public void setUnderlyingId(ExternalId underlyingId) {
    JodaBeanUtils.notNull(underlyingId, "underlyingId");
    this._underlyingId = underlyingId;
  }

  /**
   * Gets the the {@code underlyingId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> underlyingId() {
    return metaBean().underlyingId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the point value.
   * @return the value of the property
   */
  public double getPointValue() {
    return _pointValue;
  }

  /**
   * Sets the point value.
   * @param pointValue  the new value of the property
   */
  public void setPointValue(double pointValue) {
    this._pointValue = pointValue;
  }

  /**
   * Gets the the {@code pointValue} property.
   * @return the property, not null
   */
  public final Property<Double> pointValue() {
    return metaBean().pointValue().createProperty(this);
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
   * Gets the strike.
   * @return the value of the property
   */
  public double getStrike() {
    return _strike;
  }

  /**
   * Sets the strike.
   * @param strike  the new value of the property
   */
  public void setStrike(double strike) {
    this._strike = strike;
  }

  /**
   * Gets the the {@code strike} property.
   * @return the property, not null
   */
  public final Property<Double> strike() {
    return metaBean().strike().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the option type.
   * @return the value of the property, not null
   */
  public OptionType getOptionType() {
    return _optionType;
  }

  /**
   * Sets the option type.
   * @param optionType  the new value of the property, not null
   */
  public void setOptionType(OptionType optionType) {
    JodaBeanUtils.notNull(optionType, "optionType");
    this._optionType = optionType;
  }

  /**
   * Gets the the {@code optionType} property.
   * @return the property, not null
   */
  public final Property<OptionType> optionType() {
    return metaBean().optionType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public FxFutureOptionSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FxFutureOptionSecurity other = (FxFutureOptionSecurity) obj;
      return JodaBeanUtils.equal(getTradingExchange(), other.getTradingExchange()) &&
          JodaBeanUtils.equal(getSettlementExchange(), other.getSettlementExchange()) &&
          JodaBeanUtils.equal(getExpiry(), other.getExpiry()) &&
          JodaBeanUtils.equal(getExerciseType(), other.getExerciseType()) &&
          JodaBeanUtils.equal(getUnderlyingId(), other.getUnderlyingId()) &&
          JodaBeanUtils.equal(getPointValue(), other.getPointValue()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getStrike(), other.getStrike()) &&
          JodaBeanUtils.equal(getOptionType(), other.getOptionType()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getTradingExchange());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSettlementExchange());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExpiry());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExerciseType());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnderlyingId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPointValue());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getStrike());
    hash = hash * 31 + JodaBeanUtils.hashCode(getOptionType());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(320);
    buf.append("FxFutureOptionSecurity{");
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
    buf.append("tradingExchange").append('=').append(JodaBeanUtils.toString(getTradingExchange())).append(',').append(' ');
    buf.append("settlementExchange").append('=').append(JodaBeanUtils.toString(getSettlementExchange())).append(',').append(' ');
    buf.append("expiry").append('=').append(JodaBeanUtils.toString(getExpiry())).append(',').append(' ');
    buf.append("exerciseType").append('=').append(JodaBeanUtils.toString(getExerciseType())).append(',').append(' ');
    buf.append("underlyingId").append('=').append(JodaBeanUtils.toString(getUnderlyingId())).append(',').append(' ');
    buf.append("pointValue").append('=').append(JodaBeanUtils.toString(getPointValue())).append(',').append(' ');
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency())).append(',').append(' ');
    buf.append("strike").append('=').append(JodaBeanUtils.toString(getStrike())).append(',').append(' ');
    buf.append("optionType").append('=').append(JodaBeanUtils.toString(getOptionType())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FxFutureOptionSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code tradingExchange} property.
     */
    private final MetaProperty<String> _tradingExchange = DirectMetaProperty.ofReadWrite(
        this, "tradingExchange", FxFutureOptionSecurity.class, String.class);
    /**
     * The meta-property for the {@code settlementExchange} property.
     */
    private final MetaProperty<String> _settlementExchange = DirectMetaProperty.ofReadWrite(
        this, "settlementExchange", FxFutureOptionSecurity.class, String.class);
    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<Expiry> _expiry = DirectMetaProperty.ofReadWrite(
        this, "expiry", FxFutureOptionSecurity.class, Expiry.class);
    /**
     * The meta-property for the {@code exerciseType} property.
     */
    private final MetaProperty<ExerciseType> _exerciseType = DirectMetaProperty.ofReadWrite(
        this, "exerciseType", FxFutureOptionSecurity.class, ExerciseType.class);
    /**
     * The meta-property for the {@code underlyingId} property.
     */
    private final MetaProperty<ExternalId> _underlyingId = DirectMetaProperty.ofReadWrite(
        this, "underlyingId", FxFutureOptionSecurity.class, ExternalId.class);
    /**
     * The meta-property for the {@code pointValue} property.
     */
    private final MetaProperty<Double> _pointValue = DirectMetaProperty.ofReadWrite(
        this, "pointValue", FxFutureOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", FxFutureOptionSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code strike} property.
     */
    private final MetaProperty<Double> _strike = DirectMetaProperty.ofReadWrite(
        this, "strike", FxFutureOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code optionType} property.
     */
    private final MetaProperty<OptionType> _optionType = DirectMetaProperty.ofReadWrite(
        this, "optionType", FxFutureOptionSecurity.class, OptionType.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "tradingExchange",
        "settlementExchange",
        "expiry",
        "exerciseType",
        "underlyingId",
        "pointValue",
        "currency",
        "strike",
        "optionType");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -661485980:  // tradingExchange
          return _tradingExchange;
        case 389497452:  // settlementExchange
          return _settlementExchange;
        case -1289159373:  // expiry
          return _expiry;
        case -466331342:  // exerciseType
          return _exerciseType;
        case -771625640:  // underlyingId
          return _underlyingId;
        case 1257391553:  // pointValue
          return _pointValue;
        case 575402001:  // currency
          return _currency;
        case -891985998:  // strike
          return _strike;
        case 1373587791:  // optionType
          return _optionType;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FxFutureOptionSecurity> builder() {
      return new DirectBeanBuilder<>(new FxFutureOptionSecurity());
    }

    @Override
    public Class<? extends FxFutureOptionSecurity> beanType() {
      return FxFutureOptionSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
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
     * The meta-property for the {@code expiry} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Expiry> expiry() {
      return _expiry;
    }

    /**
     * The meta-property for the {@code exerciseType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExerciseType> exerciseType() {
      return _exerciseType;
    }

    /**
     * The meta-property for the {@code underlyingId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> underlyingId() {
      return _underlyingId;
    }

    /**
     * The meta-property for the {@code pointValue} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> pointValue() {
      return _pointValue;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code strike} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> strike() {
      return _strike;
    }

    /**
     * The meta-property for the {@code optionType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<OptionType> optionType() {
      return _optionType;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -661485980:  // tradingExchange
          return ((FxFutureOptionSecurity) bean).getTradingExchange();
        case 389497452:  // settlementExchange
          return ((FxFutureOptionSecurity) bean).getSettlementExchange();
        case -1289159373:  // expiry
          return ((FxFutureOptionSecurity) bean).getExpiry();
        case -466331342:  // exerciseType
          return ((FxFutureOptionSecurity) bean).getExerciseType();
        case -771625640:  // underlyingId
          return ((FxFutureOptionSecurity) bean).getUnderlyingId();
        case 1257391553:  // pointValue
          return ((FxFutureOptionSecurity) bean).getPointValue();
        case 575402001:  // currency
          return ((FxFutureOptionSecurity) bean).getCurrency();
        case -891985998:  // strike
          return ((FxFutureOptionSecurity) bean).getStrike();
        case 1373587791:  // optionType
          return ((FxFutureOptionSecurity) bean).getOptionType();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -661485980:  // tradingExchange
          ((FxFutureOptionSecurity) bean).setTradingExchange((String) newValue);
          return;
        case 389497452:  // settlementExchange
          ((FxFutureOptionSecurity) bean).setSettlementExchange((String) newValue);
          return;
        case -1289159373:  // expiry
          ((FxFutureOptionSecurity) bean).setExpiry((Expiry) newValue);
          return;
        case -466331342:  // exerciseType
          ((FxFutureOptionSecurity) bean).setExerciseType((ExerciseType) newValue);
          return;
        case -771625640:  // underlyingId
          ((FxFutureOptionSecurity) bean).setUnderlyingId((ExternalId) newValue);
          return;
        case 1257391553:  // pointValue
          ((FxFutureOptionSecurity) bean).setPointValue((Double) newValue);
          return;
        case 575402001:  // currency
          ((FxFutureOptionSecurity) bean).setCurrency((Currency) newValue);
          return;
        case -891985998:  // strike
          ((FxFutureOptionSecurity) bean).setStrike((Double) newValue);
          return;
        case 1373587791:  // optionType
          ((FxFutureOptionSecurity) bean).setOptionType((OptionType) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((FxFutureOptionSecurity) bean)._tradingExchange, "tradingExchange");
      JodaBeanUtils.notNull(((FxFutureOptionSecurity) bean)._settlementExchange, "settlementExchange");
      JodaBeanUtils.notNull(((FxFutureOptionSecurity) bean)._expiry, "expiry");
      JodaBeanUtils.notNull(((FxFutureOptionSecurity) bean)._exerciseType, "exerciseType");
      JodaBeanUtils.notNull(((FxFutureOptionSecurity) bean)._underlyingId, "underlyingId");
      JodaBeanUtils.notNull(((FxFutureOptionSecurity) bean)._currency, "currency");
      JodaBeanUtils.notNull(((FxFutureOptionSecurity) bean)._optionType, "optionType");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
