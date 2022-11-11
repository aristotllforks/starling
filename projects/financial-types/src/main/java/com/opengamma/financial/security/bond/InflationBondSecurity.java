/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.bond;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import java.time.ZonedDateTime;

import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.convention.yield.YieldConvention;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;

/**
 * A security for inflation bonds.
 */
@BeanDefinition
public class InflationBondSecurity extends BondSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  InflationBondSecurity() { // For builder
    super();
  }

  public InflationBondSecurity(final String issuerName, final String issuerType, final String issuerDomicile, final String market, final Currency currency,
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
    return visitor.visitInflationBondSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code InflationBondSecurity}.
   * @return the meta-bean, not null
   */
  public static InflationBondSecurity.Meta meta() {
    return InflationBondSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(InflationBondSecurity.Meta.INSTANCE);
  }

  @Override
  public InflationBondSecurity.Meta metaBean() {
    return InflationBondSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public InflationBondSecurity clone() {
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
    buf.append("InflationBondSecurity{");
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
   * The meta-bean for {@code InflationBondSecurity}.
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
    public BeanBuilder<? extends InflationBondSecurity> builder() {
      return new DirectBeanBuilder<InflationBondSecurity>(new InflationBondSecurity());
    }

    @Override
    public Class<? extends InflationBondSecurity> beanType() {
      return InflationBondSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
