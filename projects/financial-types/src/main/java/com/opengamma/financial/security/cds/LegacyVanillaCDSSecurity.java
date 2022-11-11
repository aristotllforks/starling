/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.cds;

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

/**
 * @deprecated Use {@link com.opengamma.financial.security.credit.LegacyCDSSecurity}.
 */
@Deprecated
@BeanDefinition
@SecurityDescription(type = LegacyVanillaCDSSecurity.SECURITY_TYPE, description = "Legacy vanilla cds")
public class LegacyVanillaCDSSecurity extends LegacyCDSSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 2L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "LEGACY_VANILLA_CDS";
  /**
   * The par spread.
   */
  @PropertyDefinition(validate = "notNull")
  private double _parSpread;

  LegacyVanillaCDSSecurity() { // For Fudge builder
    super(SECURITY_TYPE);
  }

  public LegacyVanillaCDSSecurity(final boolean isBuy, final ExternalId protectionSeller, final ExternalId protectionBuyer, final ExternalId referenceEntity,
      final DebtSeniority debtSeniority, final RestructuringClause restructuringClause, final ExternalId regionId, final ZonedDateTime startDate,
      final ZonedDateTime effectiveDate, final ZonedDateTime maturityDate, final StubType stubType, final Frequency couponFrequency, final DayCount dayCount,
      final BusinessDayConvention businessDayConvention, final boolean immAdjustMaturityDate, final boolean adjustEffectiveDate,
      final boolean adjustMaturityDate, final InterestRateNotional notional, final boolean includeAccruedPremium, final boolean protectionStart,
      final double parSpread) {
    super(isBuy, protectionSeller, protectionBuyer, referenceEntity, debtSeniority, restructuringClause, regionId, startDate, effectiveDate, maturityDate,
        stubType, couponFrequency, dayCount, businessDayConvention, immAdjustMaturityDate, adjustEffectiveDate, adjustMaturityDate, notional,
        includeAccruedPremium, protectionStart, SECURITY_TYPE);
    setParSpread(parSpread);
  }

  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitLegacyVanillaCDSSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code LegacyVanillaCDSSecurity}.
   * @return the meta-bean, not null
   */
  public static LegacyVanillaCDSSecurity.Meta meta() {
    return LegacyVanillaCDSSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(LegacyVanillaCDSSecurity.Meta.INSTANCE);
  }

  @Override
  public LegacyVanillaCDSSecurity.Meta metaBean() {
    return LegacyVanillaCDSSecurity.Meta.INSTANCE;
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
  @Override
  public LegacyVanillaCDSSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      LegacyVanillaCDSSecurity other = (LegacyVanillaCDSSecurity) obj;
      return JodaBeanUtils.equal(getParSpread(), other.getParSpread()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getParSpread());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("LegacyVanillaCDSSecurity{");
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
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code LegacyVanillaCDSSecurity}.
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
        this, "parSpread", LegacyVanillaCDSSecurity.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "parSpread");

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
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends LegacyVanillaCDSSecurity> builder() {
      return new DirectBeanBuilder<LegacyVanillaCDSSecurity>(new LegacyVanillaCDSSecurity());
    }

    @Override
    public Class<? extends LegacyVanillaCDSSecurity> beanType() {
      return LegacyVanillaCDSSecurity.class;
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

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1556795764:  // parSpread
          return ((LegacyVanillaCDSSecurity) bean).getParSpread();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1556795764:  // parSpread
          ((LegacyVanillaCDSSecurity) bean).setParSpread((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((LegacyVanillaCDSSecurity) bean)._parSpread, "parSpread");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
