/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.deposit;

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
 * A security representing a cash deposit with a continually-compounded zero
 * rate.
 */
@BeanDefinition
@SecurityDescription(type = ContinuousZeroDepositSecurity.SECURITY_TYPE, description = "Continous zero deposit")
public class ContinuousZeroDepositSecurity extends FinancialSecurity {
  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /** The security type */
  public static final String SECURITY_TYPE = "CONTINUOUS_ZERO_DEPOSIT";

  /** The currency. */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;

  /** The start date. */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _startDate;

  /** The maturity date. */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _maturityDate;

  /** The rate. */
  @PropertyDefinition
  private double _rate;

  /** The region. */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _region;

  /**
   * For the builder.
   */
  ContinuousZeroDepositSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * @param currency
   *          the currency, not null
   * @param startDate
   *          the start date, not null
   * @param maturityDate
   *          the maturity date, not null
   * @param rate
   *          the rate
   * @param region
   *          the region, not null
   */
  public ContinuousZeroDepositSecurity(final Currency currency, final ZonedDateTime startDate, final ZonedDateTime maturityDate, final double rate, final ExternalId region) {
    super(SECURITY_TYPE);
    setCurrency(currency);
    setStartDate(startDate);
    setMaturityDate(maturityDate);
    setRate(rate);
    setRegion(region);
  }

  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitContinuousZeroDepositSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ContinuousZeroDepositSecurity}.
   * @return the meta-bean, not null
   */
  public static ContinuousZeroDepositSecurity.Meta meta() {
    return ContinuousZeroDepositSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ContinuousZeroDepositSecurity.Meta.INSTANCE);
  }

  @Override
  public ContinuousZeroDepositSecurity.Meta metaBean() {
    return ContinuousZeroDepositSecurity.Meta.INSTANCE;
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
   * Gets the start date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getStartDate() {
    return _startDate;
  }

  /**
   * Sets the start date.
   * @param startDate  the new value of the property, not null
   */
  public void setStartDate(ZonedDateTime startDate) {
    JodaBeanUtils.notNull(startDate, "startDate");
    this._startDate = startDate;
  }

  /**
   * Gets the the {@code startDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> startDate() {
    return metaBean().startDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the maturity date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getMaturityDate() {
    return _maturityDate;
  }

  /**
   * Sets the maturity date.
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
   * Gets the rate.
   * @return the value of the property
   */
  public double getRate() {
    return _rate;
  }

  /**
   * Sets the rate.
   * @param rate  the new value of the property
   */
  public void setRate(double rate) {
    this._rate = rate;
  }

  /**
   * Gets the the {@code rate} property.
   * @return the property, not null
   */
  public final Property<Double> rate() {
    return metaBean().rate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the region.
   * @return the value of the property, not null
   */
  public ExternalId getRegion() {
    return _region;
  }

  /**
   * Sets the region.
   * @param region  the new value of the property, not null
   */
  public void setRegion(ExternalId region) {
    JodaBeanUtils.notNull(region, "region");
    this._region = region;
  }

  /**
   * Gets the the {@code region} property.
   * @return the property, not null
   */
  public final Property<ExternalId> region() {
    return metaBean().region().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public ContinuousZeroDepositSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ContinuousZeroDepositSecurity other = (ContinuousZeroDepositSecurity) obj;
      return JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getStartDate(), other.getStartDate()) &&
          JodaBeanUtils.equal(getMaturityDate(), other.getMaturityDate()) &&
          JodaBeanUtils.equal(getRate(), other.getRate()) &&
          JodaBeanUtils.equal(getRegion(), other.getRegion()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getStartDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMaturityDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegion());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("ContinuousZeroDepositSecurity{");
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
    buf.append("startDate").append('=').append(JodaBeanUtils.toString(getStartDate())).append(',').append(' ');
    buf.append("maturityDate").append('=').append(JodaBeanUtils.toString(getMaturityDate())).append(',').append(' ');
    buf.append("rate").append('=').append(JodaBeanUtils.toString(getRate())).append(',').append(' ');
    buf.append("region").append('=').append(JodaBeanUtils.toString(getRegion())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ContinuousZeroDepositSecurity}.
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
        this, "currency", ContinuousZeroDepositSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code startDate} property.
     */
    private final MetaProperty<ZonedDateTime> _startDate = DirectMetaProperty.ofReadWrite(
        this, "startDate", ContinuousZeroDepositSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code maturityDate} property.
     */
    private final MetaProperty<ZonedDateTime> _maturityDate = DirectMetaProperty.ofReadWrite(
        this, "maturityDate", ContinuousZeroDepositSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code rate} property.
     */
    private final MetaProperty<Double> _rate = DirectMetaProperty.ofReadWrite(
        this, "rate", ContinuousZeroDepositSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code region} property.
     */
    private final MetaProperty<ExternalId> _region = DirectMetaProperty.ofReadWrite(
        this, "region", ContinuousZeroDepositSecurity.class, ExternalId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "currency",
        "startDate",
        "maturityDate",
        "rate",
        "region");

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
        case -2129778896:  // startDate
          return _startDate;
        case -414641441:  // maturityDate
          return _maturityDate;
        case 3493088:  // rate
          return _rate;
        case -934795532:  // region
          return _region;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ContinuousZeroDepositSecurity> builder() {
      return new DirectBeanBuilder<ContinuousZeroDepositSecurity>(new ContinuousZeroDepositSecurity());
    }

    @Override
    public Class<? extends ContinuousZeroDepositSecurity> beanType() {
      return ContinuousZeroDepositSecurity.class;
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
     * The meta-property for the {@code startDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> startDate() {
      return _startDate;
    }

    /**
     * The meta-property for the {@code maturityDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> maturityDate() {
      return _maturityDate;
    }

    /**
     * The meta-property for the {@code rate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> rate() {
      return _rate;
    }

    /**
     * The meta-property for the {@code region} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> region() {
      return _region;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return ((ContinuousZeroDepositSecurity) bean).getCurrency();
        case -2129778896:  // startDate
          return ((ContinuousZeroDepositSecurity) bean).getStartDate();
        case -414641441:  // maturityDate
          return ((ContinuousZeroDepositSecurity) bean).getMaturityDate();
        case 3493088:  // rate
          return ((ContinuousZeroDepositSecurity) bean).getRate();
        case -934795532:  // region
          return ((ContinuousZeroDepositSecurity) bean).getRegion();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          ((ContinuousZeroDepositSecurity) bean).setCurrency((Currency) newValue);
          return;
        case -2129778896:  // startDate
          ((ContinuousZeroDepositSecurity) bean).setStartDate((ZonedDateTime) newValue);
          return;
        case -414641441:  // maturityDate
          ((ContinuousZeroDepositSecurity) bean).setMaturityDate((ZonedDateTime) newValue);
          return;
        case 3493088:  // rate
          ((ContinuousZeroDepositSecurity) bean).setRate((Double) newValue);
          return;
        case -934795532:  // region
          ((ContinuousZeroDepositSecurity) bean).setRegion((ExternalId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((ContinuousZeroDepositSecurity) bean)._currency, "currency");
      JodaBeanUtils.notNull(((ContinuousZeroDepositSecurity) bean)._startDate, "startDate");
      JodaBeanUtils.notNull(((ContinuousZeroDepositSecurity) bean)._maturityDate, "maturityDate");
      JodaBeanUtils.notNull(((ContinuousZeroDepositSecurity) bean)._region, "region");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
