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
 * @deprecated Use {@link com.opengamma.financial.security.credit.StandardCDSSecurity}.
 */
@Deprecated
@BeanDefinition
@SecurityDescription(type = StandardFixedRecoveryCDSSecurity.SECURITY_TYPE, description = "Standard fixed recovery cds")
public class StandardFixedRecoveryCDSSecurity extends StandardCDSSecurity {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "STANDARD_FIXED_RECOVERY_CDS";

  /**
   * The recovery rate.
   */
  @PropertyDefinition(validate = "notNull")
  private double _recoveryRate;

  StandardFixedRecoveryCDSSecurity() { // for fudge
    super(SECURITY_TYPE);
  }

  public StandardFixedRecoveryCDSSecurity(final boolean isBuy, final ExternalId protectionSeller, final ExternalId protectionBuyer,
      final ExternalId referenceEntity, // CSIGNORE
      final DebtSeniority debtSeniority, final RestructuringClause restructuringClause, final ExternalId regionId, final ZonedDateTime startDate,
      final ZonedDateTime effectiveDate, final ZonedDateTime maturityDate, final StubType stubType, final Frequency couponFrequency, final DayCount dayCount,
      final BusinessDayConvention businessDayConvention, final boolean immAdjustMaturityDate, final boolean adjustEffectiveDate,
      final boolean adjustMaturityDate, final InterestRateNotional notional, final double recoveryRate, final boolean includeAccruedPremium,
      final boolean protectionStart, final double quotedSpread, final InterestRateNotional upfrontAmount) {
    super(isBuy, protectionSeller, protectionBuyer, referenceEntity, debtSeniority, restructuringClause, regionId, startDate, effectiveDate, maturityDate,
        stubType, couponFrequency, dayCount, businessDayConvention, immAdjustMaturityDate, adjustEffectiveDate, adjustMaturityDate, notional,
        includeAccruedPremium, protectionStart, quotedSpread, upfrontAmount, SECURITY_TYPE);
    _recoveryRate = recoveryRate;
  }

  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitStandardFixedRecoveryCDSSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code StandardFixedRecoveryCDSSecurity}.
   * @return the meta-bean, not null
   */
  public static StandardFixedRecoveryCDSSecurity.Meta meta() {
    return StandardFixedRecoveryCDSSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(StandardFixedRecoveryCDSSecurity.Meta.INSTANCE);
  }

  @Override
  public StandardFixedRecoveryCDSSecurity.Meta metaBean() {
    return StandardFixedRecoveryCDSSecurity.Meta.INSTANCE;
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
  public StandardFixedRecoveryCDSSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      StandardFixedRecoveryCDSSecurity other = (StandardFixedRecoveryCDSSecurity) obj;
      return JodaBeanUtils.equal(getRecoveryRate(), other.getRecoveryRate()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getRecoveryRate());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("StandardFixedRecoveryCDSSecurity{");
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
    buf.append("recoveryRate").append('=').append(JodaBeanUtils.toString(getRecoveryRate())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code StandardFixedRecoveryCDSSecurity}.
   */
  public static class Meta extends StandardCDSSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code recoveryRate} property.
     */
    private final MetaProperty<Double> _recoveryRate = DirectMetaProperty.ofReadWrite(
        this, "recoveryRate", StandardFixedRecoveryCDSSecurity.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "recoveryRate");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2002873877:  // recoveryRate
          return _recoveryRate;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends StandardFixedRecoveryCDSSecurity> builder() {
      return new DirectBeanBuilder<>(new StandardFixedRecoveryCDSSecurity());
    }

    @Override
    public Class<? extends StandardFixedRecoveryCDSSecurity> beanType() {
      return StandardFixedRecoveryCDSSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
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
        case 2002873877:  // recoveryRate
          return ((StandardFixedRecoveryCDSSecurity) bean).getRecoveryRate();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2002873877:  // recoveryRate
          ((StandardFixedRecoveryCDSSecurity) bean).setRecoveryRate((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((StandardFixedRecoveryCDSSecurity) bean)._recoveryRate, "recoveryRate");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
