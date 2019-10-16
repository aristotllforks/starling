/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.future;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * An abstract base class for commodity securities..
 */
@BeanDefinition
public abstract class CommodityFutureSecurity extends FutureSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * Quantity of unit.
   */
  @PropertyDefinition
  private Double _unitNumber;
  /**
   * Name of the unit.
   */
  @PropertyDefinition
  private String _unitName;

  /**
   * Creates an empty instance.
   * <p>
   * The security details should be set before use.
   */
  public CommodityFutureSecurity() {
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
   * @deprecated Use the constructor that takes the unit number and name
   */
  @Deprecated
  public CommodityFutureSecurity(final Expiry expiry, final String tradingExchange, final String settlementExchange, final Currency currency,
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
   *          the number of units of the commodity to be delivered (or cash equivalent received)
   * @param unitName
   *          the name of the underlying commodity
   */
  public CommodityFutureSecurity(final Expiry expiry, final String tradingExchange, final String settlementExchange, final Currency currency,
      final double unitAmount, final String category, final Double unitNumber, final String unitName) {
    super(expiry, tradingExchange, settlementExchange, currency, unitAmount, category);
    setUnitNumber(unitNumber);
    setUnitName(unitName);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code CommodityFutureSecurity}.
   * @return the meta-bean, not null
   */
  public static CommodityFutureSecurity.Meta meta() {
    return CommodityFutureSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(CommodityFutureSecurity.Meta.INSTANCE);
  }

  @Override
  public CommodityFutureSecurity.Meta metaBean() {
    return CommodityFutureSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets quantity of unit.
   * @return the value of the property
   */
  public Double getUnitNumber() {
    return _unitNumber;
  }

  /**
   * Sets quantity of unit.
   * @param unitNumber  the new value of the property
   */
  public void setUnitNumber(Double unitNumber) {
    this._unitNumber = unitNumber;
  }

  /**
   * Gets the the {@code unitNumber} property.
   * @return the property, not null
   */
  public final Property<Double> unitNumber() {
    return metaBean().unitNumber().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets name of the unit.
   * @return the value of the property
   */
  public String getUnitName() {
    return _unitName;
  }

  /**
   * Sets name of the unit.
   * @param unitName  the new value of the property
   */
  public void setUnitName(String unitName) {
    this._unitName = unitName;
  }

  /**
   * Gets the the {@code unitName} property.
   * @return the property, not null
   */
  public final Property<String> unitName() {
    return metaBean().unitName().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CommodityFutureSecurity other = (CommodityFutureSecurity) obj;
      return JodaBeanUtils.equal(getUnitNumber(), other.getUnitNumber()) &&
          JodaBeanUtils.equal(getUnitName(), other.getUnitName()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnitNumber());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnitName());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("CommodityFutureSecurity{");
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
    buf.append("unitNumber").append('=').append(JodaBeanUtils.toString(getUnitNumber())).append(',').append(' ');
    buf.append("unitName").append('=').append(JodaBeanUtils.toString(getUnitName())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CommodityFutureSecurity}.
   */
  public static class Meta extends FutureSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code unitNumber} property.
     */
    private final MetaProperty<Double> _unitNumber = DirectMetaProperty.ofReadWrite(
        this, "unitNumber", CommodityFutureSecurity.class, Double.class);
    /**
     * The meta-property for the {@code unitName} property.
     */
    private final MetaProperty<String> _unitName = DirectMetaProperty.ofReadWrite(
        this, "unitName", CommodityFutureSecurity.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "unitNumber",
        "unitName");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2053402093:  // unitNumber
          return _unitNumber;
        case -292854225:  // unitName
          return _unitName;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public boolean isBuildable() {
      return false;
    }

    @Override
    public BeanBuilder<? extends CommodityFutureSecurity> builder() {
      throw new UnsupportedOperationException("CommodityFutureSecurity is an abstract class");
    }

    @Override
    public Class<? extends CommodityFutureSecurity> beanType() {
      return CommodityFutureSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code unitNumber} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> unitNumber() {
      return _unitNumber;
    }

    /**
     * The meta-property for the {@code unitName} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> unitName() {
      return _unitName;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2053402093:  // unitNumber
          return ((CommodityFutureSecurity) bean).getUnitNumber();
        case -292854225:  // unitName
          return ((CommodityFutureSecurity) bean).getUnitName();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2053402093:  // unitNumber
          ((CommodityFutureSecurity) bean).setUnitNumber((Double) newValue);
          return;
        case -292854225:  // unitName
          ((CommodityFutureSecurity) bean).setUnitName((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
