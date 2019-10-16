/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.swap;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.id.ExternalId;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Fixed leg for variance swaps.
 */
@BeanDefinition
public class FixedVarianceSwapLeg extends VarianceSwapLeg {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The strike.
   */
  @PropertyDefinition
  private double _strike;
  /**
   * The variance swap type.
   */
  @PropertyDefinition(validate = "notNull")
  private VarianceSwapType _type;

  /**
   * Creates an instance.
   *
   * @param dayCount The day count convention, not null
   * @param frequency The frequency, not null
   * @param regionId The region ID, not null
   * @param businessDayConvention The business day convention, not null
   * @param notional The notional, not null
   * @param eom The end-of-month flag
   * @param strike The strike
   * @param type The variance swap type, not null
   */
  public FixedVarianceSwapLeg(final DayCount dayCount,
                              final Frequency frequency,
                              final ExternalId regionId,
                              final BusinessDayConvention businessDayConvention,
                              final Notional notional,
                              final boolean eom,
                              final double strike,
                              final VarianceSwapType type) {
    super(dayCount, frequency, regionId, businessDayConvention, notional, eom);
    setStrike(strike);
    setType(type);
  }

  /**
   * Creates an empty instance.
   */
  protected FixedVarianceSwapLeg() {
  }

  @Override
  public <T> T accept(final SwapLegVisitor<T> visitor) {
    return visitor.visitFixedVarianceSwapLeg(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FixedVarianceSwapLeg}.
   * @return the meta-bean, not null
   */
  public static FixedVarianceSwapLeg.Meta meta() {
    return FixedVarianceSwapLeg.Meta.INSTANCE;
  }

  static {
    MetaBean.register(FixedVarianceSwapLeg.Meta.INSTANCE);
  }

  @Override
  public FixedVarianceSwapLeg.Meta metaBean() {
    return FixedVarianceSwapLeg.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the strike.
   * @return the value of the property
   */
  public double getStrike() {
    return _strike;
  }

  /**
   * Sets the strike.
   * @param strike  the new value of the property
   */
  public void setStrike(double strike) {
    this._strike = strike;
  }

  /**
   * Gets the the {@code strike} property.
   * @return the property, not null
   */
  public final Property<Double> strike() {
    return metaBean().strike().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the variance swap type.
   * @return the value of the property, not null
   */
  public VarianceSwapType getType() {
    return _type;
  }

  /**
   * Sets the variance swap type.
   * @param type  the new value of the property, not null
   */
  public void setType(VarianceSwapType type) {
    JodaBeanUtils.notNull(type, "type");
    this._type = type;
  }

  /**
   * Gets the the {@code type} property.
   * @return the property, not null
   */
  public final Property<VarianceSwapType> type() {
    return metaBean().type().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public FixedVarianceSwapLeg clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FixedVarianceSwapLeg other = (FixedVarianceSwapLeg) obj;
      return JodaBeanUtils.equal(getStrike(), other.getStrike()) &&
          JodaBeanUtils.equal(getType(), other.getType()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getStrike());
    hash = hash * 31 + JodaBeanUtils.hashCode(getType());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("FixedVarianceSwapLeg{");
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
    buf.append("strike").append('=').append(JodaBeanUtils.toString(getStrike())).append(',').append(' ');
    buf.append("type").append('=').append(JodaBeanUtils.toString(getType())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FixedVarianceSwapLeg}.
   */
  public static class Meta extends VarianceSwapLeg.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code strike} property.
     */
    private final MetaProperty<Double> _strike = DirectMetaProperty.ofReadWrite(
        this, "strike", FixedVarianceSwapLeg.class, Double.TYPE);
    /**
     * The meta-property for the {@code type} property.
     */
    private final MetaProperty<VarianceSwapType> _type = DirectMetaProperty.ofReadWrite(
        this, "type", FixedVarianceSwapLeg.class, VarianceSwapType.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "strike",
        "type");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -891985998:  // strike
          return _strike;
        case 3575610:  // type
          return _type;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FixedVarianceSwapLeg> builder() {
      return new DirectBeanBuilder<>(new FixedVarianceSwapLeg());
    }

    @Override
    public Class<? extends FixedVarianceSwapLeg> beanType() {
      return FixedVarianceSwapLeg.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code strike} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> strike() {
      return _strike;
    }

    /**
     * The meta-property for the {@code type} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<VarianceSwapType> type() {
      return _type;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -891985998:  // strike
          return ((FixedVarianceSwapLeg) bean).getStrike();
        case 3575610:  // type
          return ((FixedVarianceSwapLeg) bean).getType();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -891985998:  // strike
          ((FixedVarianceSwapLeg) bean).setStrike((Double) newValue);
          return;
        case 3575610:  // type
          ((FixedVarianceSwapLeg) bean).setType((VarianceSwapType) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((FixedVarianceSwapLeg) bean)._type, "type");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
