/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.swap;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import java.time.ZonedDateTime;

import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.master.security.SecurityDescription;
import com.opengamma.util.ArgumentChecker;

/**
 * A security for a zero-coupon inflation swap.
 */
@BeanDefinition
@SecurityDescription(type = ZeroCouponInflationSwapSecurity.SECURITY_TYPE, description = "Zero coupon inflation swap")
public class ZeroCouponInflationSwapSecurity extends SwapSecurity {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "ZERO_COUPON_INFLATION_SWAP";

  /**
   * For the builder.
   */
  /* package */ ZeroCouponInflationSwapSecurity() {
    super(SECURITY_TYPE);
  }

  /**
   * @param tradeDate
   *          The trade date, not null
   * @param effectiveDate
   *          The effective date, not null
   * @param maturityDate
   *          The maturity date, not null
   * @param counterparty
   *          The counterparty, not null
   * @param payLeg
   *          The pay leg, not null
   * @param receiveLeg
   *          The receive leg, not null
   */
  public ZeroCouponInflationSwapSecurity(final ZonedDateTime tradeDate, final ZonedDateTime effectiveDate, final ZonedDateTime maturityDate,
      final String counterparty, final SwapLeg payLeg, final SwapLeg receiveLeg) {
    super(SECURITY_TYPE, tradeDate, effectiveDate, maturityDate, counterparty, payLeg, receiveLeg);
    setExchangeInitialNotional(false);
    setExchangeFinalNotional(true);
  }

  @Override
  public <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    ArgumentChecker.notNull(visitor, "visitor");
    return visitor.visitZeroCouponInflationSwapSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ZeroCouponInflationSwapSecurity}.
   * @return the meta-bean, not null
   */
  public static ZeroCouponInflationSwapSecurity.Meta meta() {
    return ZeroCouponInflationSwapSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ZeroCouponInflationSwapSecurity.Meta.INSTANCE);
  }

  @Override
  public ZeroCouponInflationSwapSecurity.Meta metaBean() {
    return ZeroCouponInflationSwapSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public ZeroCouponInflationSwapSecurity clone() {
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
    buf.append("ZeroCouponInflationSwapSecurity{");
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
   * The meta-bean for {@code ZeroCouponInflationSwapSecurity}.
   */
  public static class Meta extends SwapSecurity.Meta {
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
    public BeanBuilder<? extends ZeroCouponInflationSwapSecurity> builder() {
      return new DirectBeanBuilder<ZeroCouponInflationSwapSecurity>(new ZeroCouponInflationSwapSecurity());
    }

    @Override
    public Class<? extends ZeroCouponInflationSwapSecurity> beanType() {
      return ZeroCouponInflationSwapSecurity.class;
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
