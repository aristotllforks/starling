/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.swap;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.id.ExternalId;

/**
 * Base class for the legs of a variance swap.
 */
@BeanDefinition
public abstract class VarianceSwapLeg extends SwapLeg {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * @param dayCount
   *          The day count convention, not null
   * @param frequency
   *          The frequency, not null
   * @param regionId
   *          The region ID, not null
   * @param businessDayConvention
   *          The business day convention, not null
   * @param notional
   *          The notional, not null
   * @param eom
   *          The end-of-month flag
   */
  protected VarianceSwapLeg(final DayCount dayCount, final Frequency frequency, final ExternalId regionId, final BusinessDayConvention businessDayConvention,
      final Notional notional, final boolean eom) {
    super(dayCount, frequency, regionId, businessDayConvention, notional, eom);
  }

  /** For the builder. */
  VarianceSwapLeg() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code VarianceSwapLeg}.
   * @return the meta-bean, not null
   */
  public static VarianceSwapLeg.Meta meta() {
    return VarianceSwapLeg.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(VarianceSwapLeg.Meta.INSTANCE);
  }

  @Override
  public VarianceSwapLeg.Meta metaBean() {
    return VarianceSwapLeg.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
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
    buf.append("VarianceSwapLeg{");
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
   * The meta-bean for {@code VarianceSwapLeg}.
   */
  public static class Meta extends SwapLeg.Meta {
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
    public BeanBuilder<? extends VarianceSwapLeg> builder() {
      throw new UnsupportedOperationException("VarianceSwapLeg is an abstract class");
    }

    @Override
    public Class<? extends VarianceSwapLeg> beanType() {
      return VarianceSwapLeg.class;
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
