/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.cds;

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

import com.opengamma.analytics.financial.credit.DebtSeniority;
import com.opengamma.analytics.financial.credit.RestructuringClause;
import com.opengamma.financial.convention.StubType;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.financial.security.swap.InterestRateNotional;
import com.opengamma.id.ExternalId;
import com.opengamma.master.security.SecurityDescription;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * @deprecated Use {@link com.opengamma.financial.security.credit.LegacyCDSSecurity}.
 */
@Deprecated
@BeanDefinition
@SecurityDescription(type = LegacyFixedRecoveryCDSSecurity.SECURITY_TYPE, description = "Legacy fixed recovery cds")
public class LegacyFixedRecoveryCDSSecurity extends LegacyCDSSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "LEGACY_FIXED_RECOVERY_CDS";
  /**
   * The par spread.
   */
  @PropertyDefinition(validate = "notNull")
  private double _parSpread;

  /**
   * The recovery rate.
   */
  @PropertyDefinition(validate = "notNull")
  private double _recoveryRate;

  LegacyFixedRecoveryCDSSecurity() { // For Fudge builder
    super(SECURITY_TYPE);
  }

  public LegacyFixedRecoveryCDSSecurity(final boolean isBuy, final ExternalId protectionSeller, final ExternalId protectionBuyer,
      final ExternalId referenceEntity, // CSIGNORE
      final DebtSeniority debtSeniority, final RestructuringClause restructuringClause, final ExternalId regionId, final ZonedDateTime startDate,
      final ZonedDateTime effectiveDate, final ZonedDateTime maturityDate, final StubType stubType, final Frequency couponFrequency, final DayCount dayCount,
      final BusinessDayConvention businessDayConvention, final boolean immAdjustMaturityDate, final boolean adjustEffectiveDate,
      final boolean adjustMaturityDate, final InterestRateNotional notional, final double recoveryRate, final boolean includeAccruedPremium,
      final boolean protectionStart, final double parSpread) {
    super(isBuy, protectionSeller, protectionBuyer, referenceEntity, debtSeniority, restructuringClause, regionId, startDate, effectiveDate, maturityDate,
        stubType, couponFrequency, dayCount, businessDayConvention, immAdjustMaturityDate, adjustEffectiveDate, adjustMaturityDate, notional,
        includeAccruedPremium, protectionStart, SECURITY_TYPE);
    setParSpread(parSpread);
    _recoveryRate = recoveryRate;
  }

  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitLegacyFixedRecoveryCDSSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code LegacyFixedRecoveryCDSSecurity}.
   * @return the meta-bean, not null
   */
  public static LegacyFixedRecoveryCDSSecurity.Meta meta() {
    return LegacyFixedRecoveryCDSSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(LegacyFixedRecoveryCDSSecurity.Meta.INSTANCE);
  }

  @Override
  public LegacyFixedRecoveryCDSSecurity.Meta metaBean() {
    return LegacyFixedRecoveryCDSSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the par spread.
   * @return the value of the property, not null
   */
  public double getParSpread() {
    return _parSpread;
  }

  /**
   * Sets the par spread.
   * @param parSpread  the new value of the property, not null
   */
  public void setParSpread(double parSpread) {
    JodaBeanUtils.notNull(parSpread, "parSpread");
    this._parSpread = parSpread;
  }

  /**
   * Gets the the {@code parSpread} property.
   * @return the property, not null
   */
  public final Property<Double> parSpread() {
    return metaBean().parSpread().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the recovery rate.
   * @return the value of the property, not null
   */
  public double getRecoveryRate() {
    return _recoveryRate;
  }

  /**
   * Sets the recovery rate.
   * @param recoveryRate  the new value of the property, not null
   */
  public void setRecoveryRate(double recoveryRate) {
    JodaBeanUtils.notNull(recoveryRate, "recoveryRate");
    this._recoveryRate = recoveryRate;
  }

  /**
   * Gets the the {@code recoveryRate} property.
   * @return the property, not null
   */
  public final Property<Double> recoveryRate() {
    return metaBean().recoveryRate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public LegacyFixedRecoveryCDSSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      LegacyFixedRecoveryCDSSecurity other = (LegacyFixedRecoveryCDSSecurity) obj;
      return JodaBeanUtils.equal(getParSpread(), other.getParSpread()) &&
          JodaBeanUtils.equal(getRecoveryRate(), other.getRecoveryRate()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getParSpread());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRecoveryRate());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("LegacyFixedRecoveryCDSSecurity{");
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
    buf.append("parSpread").append('=').append(JodaBeanUtils.toString(getParSpread())).append(',').append(' ');
    buf.append("recoveryRate").append('=').append(JodaBeanUtils.toString(getRecoveryRate())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code LegacyFixedRecoveryCDSSecurity}.
   */
  public static class Meta extends LegacyCDSSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code parSpread} property.
     */
    private final MetaProperty<Double> _parSpread = DirectMetaProperty.ofReadWrite(
        this, "parSpread", LegacyFixedRecoveryCDSSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code recoveryRate} property.
     */
    private final MetaProperty<Double> _recoveryRate = DirectMetaProperty.ofReadWrite(
        this, "recoveryRate", LegacyFixedRecoveryCDSSecurity.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "parSpread",
        "recoveryRate");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1556795764:  // parSpread
          return _parSpread;
        case 2002873877:  // recoveryRate
          return _recoveryRate;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends LegacyFixedRecoveryCDSSecurity> builder() {
      return new DirectBeanBuilder<>(new LegacyFixedRecoveryCDSSecurity());
    }

    @Override
    public Class<? extends LegacyFixedRecoveryCDSSecurity> beanType() {
      return LegacyFixedRecoveryCDSSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code parSpread} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> parSpread() {
      return _parSpread;
    }

    /**
     * The meta-property for the {@code recoveryRate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> recoveryRate() {
      return _recoveryRate;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1556795764:  // parSpread
          return ((LegacyFixedRecoveryCDSSecurity) bean).getParSpread();
        case 2002873877:  // recoveryRate
          return ((LegacyFixedRecoveryCDSSecurity) bean).getRecoveryRate();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1556795764:  // parSpread
          ((LegacyFixedRecoveryCDSSecurity) bean).setParSpread((Double) newValue);
          return;
        case 2002873877:  // recoveryRate
          ((LegacyFixedRecoveryCDSSecurity) bean).setRecoveryRate((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((LegacyFixedRecoveryCDSSecurity) bean)._parSpread, "parSpread");
      JodaBeanUtils.notNull(((LegacyFixedRecoveryCDSSecurity) bean)._recoveryRate, "recoveryRate");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
