/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.bond;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.convention.yield.YieldConvention;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;

/**
 * A security for municipal bonds.
 */
@BeanDefinition
public class MunicipalBondSecurity extends BondSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  MunicipalBondSecurity() { // For builder
    super();
  }

  public MunicipalBondSecurity(final String issuerName, final String issuerType, final String issuerDomicile, final String market, final Currency currency,
      final YieldConvention yieldConvention, final Expiry lastTradeDate, final String couponType, final double couponRate, final Frequency couponFrequency,
      final DayCount dayCountConvention, final ZonedDateTime interestAccrualDate, final ZonedDateTime settlementDate, final ZonedDateTime firstCouponDate,
      final Double issuancePrice, final double totalAmountIssued, final double minimumAmount, final double minimumIncrement, final double parAmount,
      final double redemptionValue) {
    super(issuerName, issuerType, issuerDomicile, market, currency, yieldConvention, lastTradeDate, couponType, couponRate, couponFrequency,
        dayCountConvention, interestAccrualDate, settlementDate, firstCouponDate, issuancePrice, totalAmountIssued, minimumAmount, minimumIncrement, parAmount,
        redemptionValue);
  }

  @Override
  public <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitMunicipalBondSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code MunicipalBondSecurity}.
   * @return the meta-bean, not null
   */
  public static MunicipalBondSecurity.Meta meta() {
    return MunicipalBondSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(MunicipalBondSecurity.Meta.INSTANCE);
  }

  @Override
  public MunicipalBondSecurity.Meta metaBean() {
    return MunicipalBondSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public MunicipalBondSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(32);
    buf.append("MunicipalBondSecurity{");
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
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MunicipalBondSecurity}.
   */
  public static class Meta extends BondSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends MunicipalBondSecurity> builder() {
      return new DirectBeanBuilder<>(new MunicipalBondSecurity());
    }

    @Override
    public Class<? extends MunicipalBondSecurity> beanType() {
      return MunicipalBondSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
