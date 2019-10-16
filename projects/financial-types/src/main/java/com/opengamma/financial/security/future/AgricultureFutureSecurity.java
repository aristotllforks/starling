/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.future;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;

/**
 * A security for agriculture futures.
 */
@BeanDefinition
public class AgricultureFutureSecurity extends CommodityFutureSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * For the builder.
   */
  AgricultureFutureSecurity() {
    super();
  }

  /**
   * @param expiry
   *          the future expiry, not null
   * @param tradingExchange
   *          the trading exchange name, not null
   * @param settlementExchange
   *          the settlement exchange name, not null
   * @param currency
   *          the currency, not null
   * @param unitAmount
   *          the unit amount, not null
   * @param category
   *          the future category, not null
   * @deprecated Use the constructor that takes the unit name and number
   */
  @Deprecated
  public AgricultureFutureSecurity(final Expiry expiry, final String tradingExchange, final String settlementExchange, final Currency currency,
      final double unitAmount, final String category) {
    super(expiry, tradingExchange, settlementExchange, currency, unitAmount, category);
  }

  /**
   * @param expiry
   *          the future expiry, not null
   * @param tradingExchange
   *          the trading exchange name, not null
   * @param settlementExchange
   *          the settlement exchange name, not null
   * @param currency
   *          the currency, not null
   * @param unitAmount
   *          the unit amount, not null
   * @param category
   *          the future category, not null
   * @param unitNumber
   *          the number of units of the commodity to be delivered (or cash
   *          equivalent received)
   * @param unitName
   *          the name of the underlying commodity
   */
  public AgricultureFutureSecurity(final Expiry expiry, final String tradingExchange, final String settlementExchange, final Currency currency,
      final double unitAmount, final String category, final Double unitNumber, final String unitName) {
    super(expiry, tradingExchange, settlementExchange, currency, unitAmount, category, unitNumber, unitName);
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitAgricultureFutureSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code AgricultureFutureSecurity}.
   * @return the meta-bean, not null
   */
  public static AgricultureFutureSecurity.Meta meta() {
    return AgricultureFutureSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(AgricultureFutureSecurity.Meta.INSTANCE);
  }

  @Override
  public AgricultureFutureSecurity.Meta metaBean() {
    return AgricultureFutureSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public AgricultureFutureSecurity clone() {
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
    buf.append("AgricultureFutureSecurity{");
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
   * The meta-bean for {@code AgricultureFutureSecurity}.
   */
  public static class Meta extends CommodityFutureSecurity.Meta {
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
    public BeanBuilder<? extends AgricultureFutureSecurity> builder() {
      return new DirectBeanBuilder<>(new AgricultureFutureSecurity());
    }

    @Override
    public Class<? extends AgricultureFutureSecurity> beanType() {
      return AgricultureFutureSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
