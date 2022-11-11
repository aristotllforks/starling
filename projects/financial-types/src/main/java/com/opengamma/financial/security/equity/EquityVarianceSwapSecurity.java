/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.equity;

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

import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.id.ExternalId;
import com.opengamma.master.security.SecurityDescription;
import com.opengamma.util.money.Currency;

/**
 * A security for equity variance swaps.
 */
@BeanDefinition
@SecurityDescription(type = EquityVarianceSwapSecurity.SECURITY_TYPE, description = "Equity variance swap")
public class EquityVarianceSwapSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "EQUITY VARIANCE SWAP";

  /**
   * The underlying identifier.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _spotUnderlyingId;
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
   * The notional.
   */
  // TODO document how the sign of the notional implies pay / receive / fixed /
  // realized
  @PropertyDefinition
  private double _notional;
  /**
   * The parameterized as variance flag.
   */
  @PropertyDefinition
  private boolean _parameterizedAsVariance;
  /**
   * The annualization factor.
   */
  @PropertyDefinition
  private double _annualizationFactor;
  /**
   * The first observation date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _firstObservationDate;
  /**
   * The last observation date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _lastObservationDate;
  /**
   * The settlement date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _settlementDate;
  /**
   * The region.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _regionId;
  /**
   * The observation frequency.
   */
  @PropertyDefinition(validate = "notNull")
  private Frequency _observationFrequency;

  /**
   * For the builder.
   */
  EquityVarianceSwapSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * @param spotUnderlyingId
   *          the identifier of the spot price, not null
   * @param currency
   *          the currency, not null
   * @param strike
   *          the strike
   * @param notional
   *          the notional
   * @param parameterizedAsVariance
   *          true if the strike is parameterized as variance, false if the
   *          strike is parameterized as volatility
   * @param annualizationFactor
   *          the annualization factor
   * @param firstObservationDate
   *          the first observation date, not null
   * @param lastObservationDate
   *          the last observation date, not null
   * @param settlementDate
   *          the settlement date, not null
   * @param regionId
   *          the region for non-working days, not null
   * @param observationFrequency
   *          the observation frequency, not null
   */
  public EquityVarianceSwapSecurity(final ExternalId spotUnderlyingId, final Currency currency, final double strike, final double notional,
      final boolean parameterizedAsVariance, final double annualizationFactor, final ZonedDateTime firstObservationDate,
      final ZonedDateTime lastObservationDate, final ZonedDateTime settlementDate, final ExternalId regionId,
      final Frequency observationFrequency) {
    super(SECURITY_TYPE);
    setSpotUnderlyingId(spotUnderlyingId);
    setCurrency(currency);
    setStrike(strike);
    setNotional(notional);
    setParameterizedAsVariance(parameterizedAsVariance);
    setAnnualizationFactor(annualizationFactor);
    setFirstObservationDate(firstObservationDate);
    setLastObservationDate(lastObservationDate);
    setSettlementDate(settlementDate);
    setRegionId(regionId);
    setObservationFrequency(observationFrequency);
  }

  //-------------------------------------------------------------------------
  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitEquityVarianceSwapSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code EquityVarianceSwapSecurity}.
   * @return the meta-bean, not null
   */
  public static EquityVarianceSwapSecurity.Meta meta() {
    return EquityVarianceSwapSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(EquityVarianceSwapSecurity.Meta.INSTANCE);
  }

  @Override
  public EquityVarianceSwapSecurity.Meta metaBean() {
    return EquityVarianceSwapSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying identifier.
   * @return the value of the property, not null
   */
  public ExternalId getSpotUnderlyingId() {
    return _spotUnderlyingId;
  }

  /**
   * Sets the underlying identifier.
   * @param spotUnderlyingId  the new value of the property, not null
   */
  public void setSpotUnderlyingId(ExternalId spotUnderlyingId) {
    JodaBeanUtils.notNull(spotUnderlyingId, "spotUnderlyingId");
    this._spotUnderlyingId = spotUnderlyingId;
  }

  /**
   * Gets the the {@code spotUnderlyingId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> spotUnderlyingId() {
    return metaBean().spotUnderlyingId().createProperty(this);
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
   * Gets the notional.
   * @return the value of the property
   */
  public double getNotional() {
    return _notional;
  }

  /**
   * Sets the notional.
   * @param notional  the new value of the property
   */
  public void setNotional(double notional) {
    this._notional = notional;
  }

  /**
   * Gets the the {@code notional} property.
   * @return the property, not null
   */
  public final Property<Double> notional() {
    return metaBean().notional().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the parameterized as variance flag.
   * @return the value of the property
   */
  public boolean isParameterizedAsVariance() {
    return _parameterizedAsVariance;
  }

  /**
   * Sets the parameterized as variance flag.
   * @param parameterizedAsVariance  the new value of the property
   */
  public void setParameterizedAsVariance(boolean parameterizedAsVariance) {
    this._parameterizedAsVariance = parameterizedAsVariance;
  }

  /**
   * Gets the the {@code parameterizedAsVariance} property.
   * @return the property, not null
   */
  public final Property<Boolean> parameterizedAsVariance() {
    return metaBean().parameterizedAsVariance().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the annualization factor.
   * @return the value of the property
   */
  public double getAnnualizationFactor() {
    return _annualizationFactor;
  }

  /**
   * Sets the annualization factor.
   * @param annualizationFactor  the new value of the property
   */
  public void setAnnualizationFactor(double annualizationFactor) {
    this._annualizationFactor = annualizationFactor;
  }

  /**
   * Gets the the {@code annualizationFactor} property.
   * @return the property, not null
   */
  public final Property<Double> annualizationFactor() {
    return metaBean().annualizationFactor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the first observation date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getFirstObservationDate() {
    return _firstObservationDate;
  }

  /**
   * Sets the first observation date.
   * @param firstObservationDate  the new value of the property, not null
   */
  public void setFirstObservationDate(ZonedDateTime firstObservationDate) {
    JodaBeanUtils.notNull(firstObservationDate, "firstObservationDate");
    this._firstObservationDate = firstObservationDate;
  }

  /**
   * Gets the the {@code firstObservationDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> firstObservationDate() {
    return metaBean().firstObservationDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the last observation date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getLastObservationDate() {
    return _lastObservationDate;
  }

  /**
   * Sets the last observation date.
   * @param lastObservationDate  the new value of the property, not null
   */
  public void setLastObservationDate(ZonedDateTime lastObservationDate) {
    JodaBeanUtils.notNull(lastObservationDate, "lastObservationDate");
    this._lastObservationDate = lastObservationDate;
  }

  /**
   * Gets the the {@code lastObservationDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> lastObservationDate() {
    return metaBean().lastObservationDate().createProperty(this);
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
   * Gets the observation frequency.
   * @return the value of the property, not null
   */
  public Frequency getObservationFrequency() {
    return _observationFrequency;
  }

  /**
   * Sets the observation frequency.
   * @param observationFrequency  the new value of the property, not null
   */
  public void setObservationFrequency(Frequency observationFrequency) {
    JodaBeanUtils.notNull(observationFrequency, "observationFrequency");
    this._observationFrequency = observationFrequency;
  }

  /**
   * Gets the the {@code observationFrequency} property.
   * @return the property, not null
   */
  public final Property<Frequency> observationFrequency() {
    return metaBean().observationFrequency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public EquityVarianceSwapSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      EquityVarianceSwapSecurity other = (EquityVarianceSwapSecurity) obj;
      return JodaBeanUtils.equal(getSpotUnderlyingId(), other.getSpotUnderlyingId()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getStrike(), other.getStrike()) &&
          JodaBeanUtils.equal(getNotional(), other.getNotional()) &&
          (isParameterizedAsVariance() == other.isParameterizedAsVariance()) &&
          JodaBeanUtils.equal(getAnnualizationFactor(), other.getAnnualizationFactor()) &&
          JodaBeanUtils.equal(getFirstObservationDate(), other.getFirstObservationDate()) &&
          JodaBeanUtils.equal(getLastObservationDate(), other.getLastObservationDate()) &&
          JodaBeanUtils.equal(getSettlementDate(), other.getSettlementDate()) &&
          JodaBeanUtils.equal(getRegionId(), other.getRegionId()) &&
          JodaBeanUtils.equal(getObservationFrequency(), other.getObservationFrequency()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getSpotUnderlyingId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getStrike());
    hash = hash * 31 + JodaBeanUtils.hashCode(getNotional());
    hash = hash * 31 + JodaBeanUtils.hashCode(isParameterizedAsVariance());
    hash = hash * 31 + JodaBeanUtils.hashCode(getAnnualizationFactor());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFirstObservationDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getLastObservationDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSettlementDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegionId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getObservationFrequency());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(384);
    buf.append("EquityVarianceSwapSecurity{");
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
    buf.append("spotUnderlyingId").append('=').append(JodaBeanUtils.toString(getSpotUnderlyingId())).append(',').append(' ');
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency())).append(',').append(' ');
    buf.append("strike").append('=').append(JodaBeanUtils.toString(getStrike())).append(',').append(' ');
    buf.append("notional").append('=').append(JodaBeanUtils.toString(getNotional())).append(',').append(' ');
    buf.append("parameterizedAsVariance").append('=').append(JodaBeanUtils.toString(isParameterizedAsVariance())).append(',').append(' ');
    buf.append("annualizationFactor").append('=').append(JodaBeanUtils.toString(getAnnualizationFactor())).append(',').append(' ');
    buf.append("firstObservationDate").append('=').append(JodaBeanUtils.toString(getFirstObservationDate())).append(',').append(' ');
    buf.append("lastObservationDate").append('=').append(JodaBeanUtils.toString(getLastObservationDate())).append(',').append(' ');
    buf.append("settlementDate").append('=').append(JodaBeanUtils.toString(getSettlementDate())).append(',').append(' ');
    buf.append("regionId").append('=').append(JodaBeanUtils.toString(getRegionId())).append(',').append(' ');
    buf.append("observationFrequency").append('=').append(JodaBeanUtils.toString(getObservationFrequency())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code EquityVarianceSwapSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code spotUnderlyingId} property.
     */
    private final MetaProperty<ExternalId> _spotUnderlyingId = DirectMetaProperty.ofReadWrite(
        this, "spotUnderlyingId", EquityVarianceSwapSecurity.class, ExternalId.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", EquityVarianceSwapSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code strike} property.
     */
    private final MetaProperty<Double> _strike = DirectMetaProperty.ofReadWrite(
        this, "strike", EquityVarianceSwapSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code notional} property.
     */
    private final MetaProperty<Double> _notional = DirectMetaProperty.ofReadWrite(
        this, "notional", EquityVarianceSwapSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code parameterizedAsVariance} property.
     */
    private final MetaProperty<Boolean> _parameterizedAsVariance = DirectMetaProperty.ofReadWrite(
        this, "parameterizedAsVariance", EquityVarianceSwapSecurity.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code annualizationFactor} property.
     */
    private final MetaProperty<Double> _annualizationFactor = DirectMetaProperty.ofReadWrite(
        this, "annualizationFactor", EquityVarianceSwapSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code firstObservationDate} property.
     */
    private final MetaProperty<ZonedDateTime> _firstObservationDate = DirectMetaProperty.ofReadWrite(
        this, "firstObservationDate", EquityVarianceSwapSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code lastObservationDate} property.
     */
    private final MetaProperty<ZonedDateTime> _lastObservationDate = DirectMetaProperty.ofReadWrite(
        this, "lastObservationDate", EquityVarianceSwapSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code settlementDate} property.
     */
    private final MetaProperty<ZonedDateTime> _settlementDate = DirectMetaProperty.ofReadWrite(
        this, "settlementDate", EquityVarianceSwapSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code regionId} property.
     */
    private final MetaProperty<ExternalId> _regionId = DirectMetaProperty.ofReadWrite(
        this, "regionId", EquityVarianceSwapSecurity.class, ExternalId.class);
    /**
     * The meta-property for the {@code observationFrequency} property.
     */
    private final MetaProperty<Frequency> _observationFrequency = DirectMetaProperty.ofReadWrite(
        this, "observationFrequency", EquityVarianceSwapSecurity.class, Frequency.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "spotUnderlyingId",
        "currency",
        "strike",
        "notional",
        "parameterizedAsVariance",
        "annualizationFactor",
        "firstObservationDate",
        "lastObservationDate",
        "settlementDate",
        "regionId",
        "observationFrequency");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -2099525766:  // spotUnderlyingId
          return _spotUnderlyingId;
        case 575402001:  // currency
          return _currency;
        case -891985998:  // strike
          return _strike;
        case 1585636160:  // notional
          return _notional;
        case 1488612956:  // parameterizedAsVariance
          return _parameterizedAsVariance;
        case 663363412:  // annualizationFactor
          return _annualizationFactor;
        case -1644595926:  // firstObservationDate
          return _firstObservationDate;
        case -1362285436:  // lastObservationDate
          return _lastObservationDate;
        case -295948169:  // settlementDate
          return _settlementDate;
        case -690339025:  // regionId
          return _regionId;
        case -213041520:  // observationFrequency
          return _observationFrequency;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends EquityVarianceSwapSecurity> builder() {
      return new DirectBeanBuilder<EquityVarianceSwapSecurity>(new EquityVarianceSwapSecurity());
    }

    @Override
    public Class<? extends EquityVarianceSwapSecurity> beanType() {
      return EquityVarianceSwapSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code spotUnderlyingId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> spotUnderlyingId() {
      return _spotUnderlyingId;
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
     * The meta-property for the {@code notional} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> notional() {
      return _notional;
    }

    /**
     * The meta-property for the {@code parameterizedAsVariance} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> parameterizedAsVariance() {
      return _parameterizedAsVariance;
    }

    /**
     * The meta-property for the {@code annualizationFactor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> annualizationFactor() {
      return _annualizationFactor;
    }

    /**
     * The meta-property for the {@code firstObservationDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> firstObservationDate() {
      return _firstObservationDate;
    }

    /**
     * The meta-property for the {@code lastObservationDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> lastObservationDate() {
      return _lastObservationDate;
    }

    /**
     * The meta-property for the {@code settlementDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> settlementDate() {
      return _settlementDate;
    }

    /**
     * The meta-property for the {@code regionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> regionId() {
      return _regionId;
    }

    /**
     * The meta-property for the {@code observationFrequency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Frequency> observationFrequency() {
      return _observationFrequency;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -2099525766:  // spotUnderlyingId
          return ((EquityVarianceSwapSecurity) bean).getSpotUnderlyingId();
        case 575402001:  // currency
          return ((EquityVarianceSwapSecurity) bean).getCurrency();
        case -891985998:  // strike
          return ((EquityVarianceSwapSecurity) bean).getStrike();
        case 1585636160:  // notional
          return ((EquityVarianceSwapSecurity) bean).getNotional();
        case 1488612956:  // parameterizedAsVariance
          return ((EquityVarianceSwapSecurity) bean).isParameterizedAsVariance();
        case 663363412:  // annualizationFactor
          return ((EquityVarianceSwapSecurity) bean).getAnnualizationFactor();
        case -1644595926:  // firstObservationDate
          return ((EquityVarianceSwapSecurity) bean).getFirstObservationDate();
        case -1362285436:  // lastObservationDate
          return ((EquityVarianceSwapSecurity) bean).getLastObservationDate();
        case -295948169:  // settlementDate
          return ((EquityVarianceSwapSecurity) bean).getSettlementDate();
        case -690339025:  // regionId
          return ((EquityVarianceSwapSecurity) bean).getRegionId();
        case -213041520:  // observationFrequency
          return ((EquityVarianceSwapSecurity) bean).getObservationFrequency();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -2099525766:  // spotUnderlyingId
          ((EquityVarianceSwapSecurity) bean).setSpotUnderlyingId((ExternalId) newValue);
          return;
        case 575402001:  // currency
          ((EquityVarianceSwapSecurity) bean).setCurrency((Currency) newValue);
          return;
        case -891985998:  // strike
          ((EquityVarianceSwapSecurity) bean).setStrike((Double) newValue);
          return;
        case 1585636160:  // notional
          ((EquityVarianceSwapSecurity) bean).setNotional((Double) newValue);
          return;
        case 1488612956:  // parameterizedAsVariance
          ((EquityVarianceSwapSecurity) bean).setParameterizedAsVariance((Boolean) newValue);
          return;
        case 663363412:  // annualizationFactor
          ((EquityVarianceSwapSecurity) bean).setAnnualizationFactor((Double) newValue);
          return;
        case -1644595926:  // firstObservationDate
          ((EquityVarianceSwapSecurity) bean).setFirstObservationDate((ZonedDateTime) newValue);
          return;
        case -1362285436:  // lastObservationDate
          ((EquityVarianceSwapSecurity) bean).setLastObservationDate((ZonedDateTime) newValue);
          return;
        case -295948169:  // settlementDate
          ((EquityVarianceSwapSecurity) bean).setSettlementDate((ZonedDateTime) newValue);
          return;
        case -690339025:  // regionId
          ((EquityVarianceSwapSecurity) bean).setRegionId((ExternalId) newValue);
          return;
        case -213041520:  // observationFrequency
          ((EquityVarianceSwapSecurity) bean).setObservationFrequency((Frequency) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((EquityVarianceSwapSecurity) bean)._spotUnderlyingId, "spotUnderlyingId");
      JodaBeanUtils.notNull(((EquityVarianceSwapSecurity) bean)._currency, "currency");
      JodaBeanUtils.notNull(((EquityVarianceSwapSecurity) bean)._firstObservationDate, "firstObservationDate");
      JodaBeanUtils.notNull(((EquityVarianceSwapSecurity) bean)._lastObservationDate, "lastObservationDate");
      JodaBeanUtils.notNull(((EquityVarianceSwapSecurity) bean)._settlementDate, "settlementDate");
      JodaBeanUtils.notNull(((EquityVarianceSwapSecurity) bean)._regionId, "regionId");
      JodaBeanUtils.notNull(((EquityVarianceSwapSecurity) bean)._observationFrequency, "observationFrequency");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
