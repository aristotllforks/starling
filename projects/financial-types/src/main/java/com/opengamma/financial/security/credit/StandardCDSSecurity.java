/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.credit;

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
import org.threeten.bp.LocalDate;

import com.opengamma.analytics.financial.credit.DebtSeniority;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.financial.security.swap.InterestRateNotional;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.master.security.SecurityDescription;

/**
 *  A standard (i.e post big bang) credit default swap.
 */
@BeanDefinition
@SecurityDescription(type = StandardCDSSecurity.SECURITY_TYPE, description = "cds")
public class StandardCDSSecurity extends FinancialSecurity {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The security type
   */
  public static final String SECURITY_TYPE = "STANDARD_CDS";

  /**
   * The trade date, aka T.
   */
  @PropertyDefinition(validate = "notNull")
  private LocalDate _tradeDate;

  /**
   * The maturity date.
   */
  @PropertyDefinition(validate = "notNull")
  private LocalDate _maturityDate;

  /**
   * The reference entity.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _referenceEntity;

  /**
   * Is protection being bought?
   */
  @PropertyDefinition(validate = "notNull")
  private boolean _buyProtection;

  /**
   * The notional.
   */
  @PropertyDefinition(validate = "notNull")
  private InterestRateNotional _notional;

  /**
   * The debt seniority.
   */
  @PropertyDefinition(validate = "notNull")
  private DebtSeniority _debtSeniority;

  /**
   * The premium leg coupon (fractional i.e. 100 bps = 0.01)
   */
  @PropertyDefinition(validate = "notNull")
  private double _coupon;


  /**
   * For the builder.
   */
  StandardCDSSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * A standard CDS security.
   *
   * @param ids
   *          identifier for this security
   * @param tradeDate
   *          the trade date
   * @param maturityDate
   *          the maturity date
   * @param referenceEntity
   *          the reference entity
   * @param notional
   *          the notional
   * @param isBuy
   *          is protecting being bought
   * @param coupon
   *          the premium leg coupon (fractional i.e. 100 bps = 0.01)
   * @param debtSeniority
   *          the debt seniority
   */
  public StandardCDSSecurity(final ExternalIdBundle ids, final LocalDate tradeDate, final LocalDate maturityDate, final ExternalId referenceEntity,
      final InterestRateNotional notional, final boolean isBuy, final double coupon, final DebtSeniority debtSeniority) {
    super(SECURITY_TYPE);
    setExternalIdBundle(ids);
    setTradeDate(tradeDate);
    setMaturityDate(maturityDate);
    setReferenceEntity(referenceEntity);
    setNotional(notional);
    setBuyProtection(isBuy);
    setCoupon(coupon);
    setDebtSeniority(debtSeniority);
  }

  /**
   * A standard CDS security.
   *
   * @param ids
   *          identifier for this security
   * @param name
   *          descriptive name
   * @param tradeDate
   *          the trade date
   * @param maturityDate
   *          the maturity date
   * @param referenceEntity
   *          the reference entity
   * @param notional
   *          the notional
   * @param isBuy
   *          is protecting being bought
   * @param coupon
   *          the premium leg coupon (fractional i.e. 100 bps = 0.01)
   * @param debtSeniority
   *          the debt seniority
   */
  public StandardCDSSecurity(final ExternalIdBundle ids, final String name, final LocalDate tradeDate, final LocalDate maturityDate,
      final ExternalId referenceEntity, final InterestRateNotional notional, final boolean isBuy, final double coupon,
      final DebtSeniority debtSeniority) {
    super(SECURITY_TYPE);
    setExternalIdBundle(ids);
    setName(name);
    setTradeDate(tradeDate);
    setMaturityDate(maturityDate);
    setReferenceEntity(referenceEntity);
    setNotional(notional);
    setBuyProtection(isBuy);
    setCoupon(coupon);
    setDebtSeniority(debtSeniority);
  }

  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitStandardCDSSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code StandardCDSSecurity}.
   * @return the meta-bean, not null
   */
  public static StandardCDSSecurity.Meta meta() {
    return StandardCDSSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(StandardCDSSecurity.Meta.INSTANCE);
  }

  @Override
  public StandardCDSSecurity.Meta metaBean() {
    return StandardCDSSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the trade date, aka T.
   * @return the value of the property, not null
   */
  public LocalDate getTradeDate() {
    return _tradeDate;
  }

  /**
   * Sets the trade date, aka T.
   * @param tradeDate  the new value of the property, not null
   */
  public void setTradeDate(LocalDate tradeDate) {
    JodaBeanUtils.notNull(tradeDate, "tradeDate");
    this._tradeDate = tradeDate;
  }

  /**
   * Gets the the {@code tradeDate} property.
   * @return the property, not null
   */
  public final Property<LocalDate> tradeDate() {
    return metaBean().tradeDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the maturity date.
   * @return the value of the property, not null
   */
  public LocalDate getMaturityDate() {
    return _maturityDate;
  }

  /**
   * Sets the maturity date.
   * @param maturityDate  the new value of the property, not null
   */
  public void setMaturityDate(LocalDate maturityDate) {
    JodaBeanUtils.notNull(maturityDate, "maturityDate");
    this._maturityDate = maturityDate;
  }

  /**
   * Gets the the {@code maturityDate} property.
   * @return the property, not null
   */
  public final Property<LocalDate> maturityDate() {
    return metaBean().maturityDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the reference entity.
   * @return the value of the property, not null
   */
  public ExternalId getReferenceEntity() {
    return _referenceEntity;
  }

  /**
   * Sets the reference entity.
   * @param referenceEntity  the new value of the property, not null
   */
  public void setReferenceEntity(ExternalId referenceEntity) {
    JodaBeanUtils.notNull(referenceEntity, "referenceEntity");
    this._referenceEntity = referenceEntity;
  }

  /**
   * Gets the the {@code referenceEntity} property.
   * @return the property, not null
   */
  public final Property<ExternalId> referenceEntity() {
    return metaBean().referenceEntity().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets is protection being bought?
   * @return the value of the property, not null
   */
  public boolean isBuyProtection() {
    return _buyProtection;
  }

  /**
   * Sets is protection being bought?
   * @param buyProtection  the new value of the property, not null
   */
  public void setBuyProtection(boolean buyProtection) {
    JodaBeanUtils.notNull(buyProtection, "buyProtection");
    this._buyProtection = buyProtection;
  }

  /**
   * Gets the the {@code buyProtection} property.
   * @return the property, not null
   */
  public final Property<Boolean> buyProtection() {
    return metaBean().buyProtection().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the notional.
   * @return the value of the property, not null
   */
  public InterestRateNotional getNotional() {
    return _notional;
  }

  /**
   * Sets the notional.
   * @param notional  the new value of the property, not null
   */
  public void setNotional(InterestRateNotional notional) {
    JodaBeanUtils.notNull(notional, "notional");
    this._notional = notional;
  }

  /**
   * Gets the the {@code notional} property.
   * @return the property, not null
   */
  public final Property<InterestRateNotional> notional() {
    return metaBean().notional().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the debt seniority.
   * @return the value of the property, not null
   */
  public DebtSeniority getDebtSeniority() {
    return _debtSeniority;
  }

  /**
   * Sets the debt seniority.
   * @param debtSeniority  the new value of the property, not null
   */
  public void setDebtSeniority(DebtSeniority debtSeniority) {
    JodaBeanUtils.notNull(debtSeniority, "debtSeniority");
    this._debtSeniority = debtSeniority;
  }

  /**
   * Gets the the {@code debtSeniority} property.
   * @return the property, not null
   */
  public final Property<DebtSeniority> debtSeniority() {
    return metaBean().debtSeniority().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the premium leg coupon (fractional i.e. 100 bps = 0.01)
   * @return the value of the property, not null
   */
  public double getCoupon() {
    return _coupon;
  }

  /**
   * Sets the premium leg coupon (fractional i.e. 100 bps = 0.01)
   * @param coupon  the new value of the property, not null
   */
  public void setCoupon(double coupon) {
    JodaBeanUtils.notNull(coupon, "coupon");
    this._coupon = coupon;
  }

  /**
   * Gets the the {@code coupon} property.
   * @return the property, not null
   */
  public final Property<Double> coupon() {
    return metaBean().coupon().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public StandardCDSSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      StandardCDSSecurity other = (StandardCDSSecurity) obj;
      return JodaBeanUtils.equal(getTradeDate(), other.getTradeDate()) &&
          JodaBeanUtils.equal(getMaturityDate(), other.getMaturityDate()) &&
          JodaBeanUtils.equal(getReferenceEntity(), other.getReferenceEntity()) &&
          (isBuyProtection() == other.isBuyProtection()) &&
          JodaBeanUtils.equal(getNotional(), other.getNotional()) &&
          JodaBeanUtils.equal(getDebtSeniority(), other.getDebtSeniority()) &&
          JodaBeanUtils.equal(getCoupon(), other.getCoupon()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getTradeDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMaturityDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getReferenceEntity());
    hash = hash * 31 + JodaBeanUtils.hashCode(isBuyProtection());
    hash = hash * 31 + JodaBeanUtils.hashCode(getNotional());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDebtSeniority());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCoupon());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(256);
    buf.append("StandardCDSSecurity{");
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
    buf.append("maturityDate").append('=').append(JodaBeanUtils.toString(getMaturityDate())).append(',').append(' ');
    buf.append("referenceEntity").append('=').append(JodaBeanUtils.toString(getReferenceEntity())).append(',').append(' ');
    buf.append("buyProtection").append('=').append(JodaBeanUtils.toString(isBuyProtection())).append(',').append(' ');
    buf.append("notional").append('=').append(JodaBeanUtils.toString(getNotional())).append(',').append(' ');
    buf.append("debtSeniority").append('=').append(JodaBeanUtils.toString(getDebtSeniority())).append(',').append(' ');
    buf.append("coupon").append('=').append(JodaBeanUtils.toString(getCoupon())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code StandardCDSSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code tradeDate} property.
     */
    private final MetaProperty<LocalDate> _tradeDate = DirectMetaProperty.ofReadWrite(
        this, "tradeDate", StandardCDSSecurity.class, LocalDate.class);
    /**
     * The meta-property for the {@code maturityDate} property.
     */
    private final MetaProperty<LocalDate> _maturityDate = DirectMetaProperty.ofReadWrite(
        this, "maturityDate", StandardCDSSecurity.class, LocalDate.class);
    /**
     * The meta-property for the {@code referenceEntity} property.
     */
    private final MetaProperty<ExternalId> _referenceEntity = DirectMetaProperty.ofReadWrite(
        this, "referenceEntity", StandardCDSSecurity.class, ExternalId.class);
    /**
     * The meta-property for the {@code buyProtection} property.
     */
    private final MetaProperty<Boolean> _buyProtection = DirectMetaProperty.ofReadWrite(
        this, "buyProtection", StandardCDSSecurity.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code notional} property.
     */
    private final MetaProperty<InterestRateNotional> _notional = DirectMetaProperty.ofReadWrite(
        this, "notional", StandardCDSSecurity.class, InterestRateNotional.class);
    /**
     * The meta-property for the {@code debtSeniority} property.
     */
    private final MetaProperty<DebtSeniority> _debtSeniority = DirectMetaProperty.ofReadWrite(
        this, "debtSeniority", StandardCDSSecurity.class, DebtSeniority.class);
    /**
     * The meta-property for the {@code coupon} property.
     */
    private final MetaProperty<Double> _coupon = DirectMetaProperty.ofReadWrite(
        this, "coupon", StandardCDSSecurity.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "tradeDate",
        "maturityDate",
        "referenceEntity",
        "buyProtection",
        "notional",
        "debtSeniority",
        "coupon");

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
        case -414641441:  // maturityDate
          return _maturityDate;
        case 480652046:  // referenceEntity
          return _referenceEntity;
        case 1154909695:  // buyProtection
          return _buyProtection;
        case 1585636160:  // notional
          return _notional;
        case 1737168171:  // debtSeniority
          return _debtSeniority;
        case -1354573786:  // coupon
          return _coupon;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends StandardCDSSecurity> builder() {
      return new DirectBeanBuilder<StandardCDSSecurity>(new StandardCDSSecurity());
    }

    @Override
    public Class<? extends StandardCDSSecurity> beanType() {
      return StandardCDSSecurity.class;
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
    public final MetaProperty<LocalDate> tradeDate() {
      return _tradeDate;
    }

    /**
     * The meta-property for the {@code maturityDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> maturityDate() {
      return _maturityDate;
    }

    /**
     * The meta-property for the {@code referenceEntity} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> referenceEntity() {
      return _referenceEntity;
    }

    /**
     * The meta-property for the {@code buyProtection} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> buyProtection() {
      return _buyProtection;
    }

    /**
     * The meta-property for the {@code notional} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<InterestRateNotional> notional() {
      return _notional;
    }

    /**
     * The meta-property for the {@code debtSeniority} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<DebtSeniority> debtSeniority() {
      return _debtSeniority;
    }

    /**
     * The meta-property for the {@code coupon} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> coupon() {
      return _coupon;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 752419634:  // tradeDate
          return ((StandardCDSSecurity) bean).getTradeDate();
        case -414641441:  // maturityDate
          return ((StandardCDSSecurity) bean).getMaturityDate();
        case 480652046:  // referenceEntity
          return ((StandardCDSSecurity) bean).getReferenceEntity();
        case 1154909695:  // buyProtection
          return ((StandardCDSSecurity) bean).isBuyProtection();
        case 1585636160:  // notional
          return ((StandardCDSSecurity) bean).getNotional();
        case 1737168171:  // debtSeniority
          return ((StandardCDSSecurity) bean).getDebtSeniority();
        case -1354573786:  // coupon
          return ((StandardCDSSecurity) bean).getCoupon();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 752419634:  // tradeDate
          ((StandardCDSSecurity) bean).setTradeDate((LocalDate) newValue);
          return;
        case -414641441:  // maturityDate
          ((StandardCDSSecurity) bean).setMaturityDate((LocalDate) newValue);
          return;
        case 480652046:  // referenceEntity
          ((StandardCDSSecurity) bean).setReferenceEntity((ExternalId) newValue);
          return;
        case 1154909695:  // buyProtection
          ((StandardCDSSecurity) bean).setBuyProtection((Boolean) newValue);
          return;
        case 1585636160:  // notional
          ((StandardCDSSecurity) bean).setNotional((InterestRateNotional) newValue);
          return;
        case 1737168171:  // debtSeniority
          ((StandardCDSSecurity) bean).setDebtSeniority((DebtSeniority) newValue);
          return;
        case -1354573786:  // coupon
          ((StandardCDSSecurity) bean).setCoupon((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((StandardCDSSecurity) bean)._tradeDate, "tradeDate");
      JodaBeanUtils.notNull(((StandardCDSSecurity) bean)._maturityDate, "maturityDate");
      JodaBeanUtils.notNull(((StandardCDSSecurity) bean)._referenceEntity, "referenceEntity");
      JodaBeanUtils.notNull(((StandardCDSSecurity) bean)._buyProtection, "buyProtection");
      JodaBeanUtils.notNull(((StandardCDSSecurity) bean)._notional, "notional");
      JodaBeanUtils.notNull(((StandardCDSSecurity) bean)._debtSeniority, "debtSeniority");
      JodaBeanUtils.notNull(((StandardCDSSecurity) bean)._coupon, "coupon");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
