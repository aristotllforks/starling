/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */

package com.opengamma.masterdb.security.hibernate.capfloor;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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

import com.opengamma.masterdb.security.hibernate.CurrencyBean;
import com.opengamma.masterdb.security.hibernate.DayCountBean;
import com.opengamma.masterdb.security.hibernate.ExternalIdBean;
import com.opengamma.masterdb.security.hibernate.FrequencyBean;
import com.opengamma.masterdb.security.hibernate.SecurityBean;
import com.opengamma.masterdb.security.hibernate.ZonedDateTimeBean;

/**
 * A Hibernate bean representation of
 * {@link com.opengamma.financial.security.capfloor.CapFloorSecurity}.
 */
@BeanDefinition
public class CapFloorSecurityBean extends SecurityBean {
  @PropertyDefinition
  private CurrencyBean _currency;
  @PropertyDefinition
  private DayCountBean _dayCount;
  @PropertyDefinition
  private FrequencyBean _frequency;
  @PropertyDefinition
  private boolean _cap;
  @PropertyDefinition
  private boolean _ibor;
  @PropertyDefinition
  private boolean _payer;
  @PropertyDefinition
  private ZonedDateTimeBean _maturityDate;
  @PropertyDefinition
  private double _notional;
  @PropertyDefinition
  private ZonedDateTimeBean _startDate;
  @PropertyDefinition
  private double _strike;
  @PropertyDefinition
  private ExternalIdBean _underlyingIdentifier;

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof CapFloorSecurityBean)) {
      return false;
    }
    final CapFloorSecurityBean capFloor = (CapFloorSecurityBean) other;
    return new EqualsBuilder()
        .append(getId(), capFloor.getId())
        .append(getNotional(), capFloor.getNotional())
        .append(getCurrency(), capFloor.getCurrency())
        .append(getDayCount(), capFloor.getDayCount())
        .append(getFrequency(), capFloor.getFrequency())
        .append(getMaturityDate(), capFloor.getMaturityDate())
        .append(getStartDate(), capFloor.getStartDate())
        .append(getStrike(), capFloor.getStrike())
        .append(getUnderlyingIdentifier(), capFloor.getUnderlyingIdentifier())
        .append(isCap(), capFloor.isCap())
        .append(isIbor(), capFloor.isIbor())
        .append(isPayer(), capFloor.isPayer())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getNotional())
        .append(getCurrency())
        .append(getDayCount())
        .append(getFrequency())
        .append(getMaturityDate())
        .append(getStartDate())
        .append(getStrike())
        .append(getUnderlyingIdentifier())
        .append(isCap())
        .append(isIbor())
        .append(isPayer())
        .toHashCode();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CapFloorSecurityBean}.
   * @return the meta-bean, not null
   */
  public static CapFloorSecurityBean.Meta meta() {
    return CapFloorSecurityBean.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CapFloorSecurityBean.Meta.INSTANCE);
  }

  @Override
  public CapFloorSecurityBean.Meta metaBean() {
    return CapFloorSecurityBean.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency.
   * @return the value of the property
   */
  public CurrencyBean getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency.
   * @param currency  the new value of the property
   */
  public void setCurrency(CurrencyBean currency) {
    this._currency = currency;
  }

  /**
   * Gets the the {@code currency} property.
   * @return the property, not null
   */
  public final Property<CurrencyBean> currency() {
    return metaBean().currency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the dayCount.
   * @return the value of the property
   */
  public DayCountBean getDayCount() {
    return _dayCount;
  }

  /**
   * Sets the dayCount.
   * @param dayCount  the new value of the property
   */
  public void setDayCount(DayCountBean dayCount) {
    this._dayCount = dayCount;
  }

  /**
   * Gets the the {@code dayCount} property.
   * @return the property, not null
   */
  public final Property<DayCountBean> dayCount() {
    return metaBean().dayCount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the frequency.
   * @return the value of the property
   */
  public FrequencyBean getFrequency() {
    return _frequency;
  }

  /**
   * Sets the frequency.
   * @param frequency  the new value of the property
   */
  public void setFrequency(FrequencyBean frequency) {
    this._frequency = frequency;
  }

  /**
   * Gets the the {@code frequency} property.
   * @return the property, not null
   */
  public final Property<FrequencyBean> frequency() {
    return metaBean().frequency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cap.
   * @return the value of the property
   */
  public boolean isCap() {
    return _cap;
  }

  /**
   * Sets the cap.
   * @param cap  the new value of the property
   */
  public void setCap(boolean cap) {
    this._cap = cap;
  }

  /**
   * Gets the the {@code cap} property.
   * @return the property, not null
   */
  public final Property<Boolean> cap() {
    return metaBean().cap().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the ibor.
   * @return the value of the property
   */
  public boolean isIbor() {
    return _ibor;
  }

  /**
   * Sets the ibor.
   * @param ibor  the new value of the property
   */
  public void setIbor(boolean ibor) {
    this._ibor = ibor;
  }

  /**
   * Gets the the {@code ibor} property.
   * @return the property, not null
   */
  public final Property<Boolean> ibor() {
    return metaBean().ibor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the payer.
   * @return the value of the property
   */
  public boolean isPayer() {
    return _payer;
  }

  /**
   * Sets the payer.
   * @param payer  the new value of the property
   */
  public void setPayer(boolean payer) {
    this._payer = payer;
  }

  /**
   * Gets the the {@code payer} property.
   * @return the property, not null
   */
  public final Property<Boolean> payer() {
    return metaBean().payer().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the maturityDate.
   * @return the value of the property
   */
  public ZonedDateTimeBean getMaturityDate() {
    return _maturityDate;
  }

  /**
   * Sets the maturityDate.
   * @param maturityDate  the new value of the property
   */
  public void setMaturityDate(ZonedDateTimeBean maturityDate) {
    this._maturityDate = maturityDate;
  }

  /**
   * Gets the the {@code maturityDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTimeBean> maturityDate() {
    return metaBean().maturityDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the notional.
   * @return the value of the property
   */
  public double getNotional() {
    return _notional;
  }

  /**
   * Sets the notional.
   * @param notional  the new value of the property
   */
  public void setNotional(double notional) {
    this._notional = notional;
  }

  /**
   * Gets the the {@code notional} property.
   * @return the property, not null
   */
  public final Property<Double> notional() {
    return metaBean().notional().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the startDate.
   * @return the value of the property
   */
  public ZonedDateTimeBean getStartDate() {
    return _startDate;
  }

  /**
   * Sets the startDate.
   * @param startDate  the new value of the property
   */
  public void setStartDate(ZonedDateTimeBean startDate) {
    this._startDate = startDate;
  }

  /**
   * Gets the the {@code startDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTimeBean> startDate() {
    return metaBean().startDate().createProperty(this);
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
   * Gets the underlyingIdentifier.
   * @return the value of the property
   */
  public ExternalIdBean getUnderlyingIdentifier() {
    return _underlyingIdentifier;
  }

  /**
   * Sets the underlyingIdentifier.
   * @param underlyingIdentifier  the new value of the property
   */
  public void setUnderlyingIdentifier(ExternalIdBean underlyingIdentifier) {
    this._underlyingIdentifier = underlyingIdentifier;
  }

  /**
   * Gets the the {@code underlyingIdentifier} property.
   * @return the property, not null
   */
  public final Property<ExternalIdBean> underlyingIdentifier() {
    return metaBean().underlyingIdentifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public CapFloorSecurityBean clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(384);
    buf.append("CapFloorSecurityBean{");
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
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency())).append(',').append(' ');
    buf.append("dayCount").append('=').append(JodaBeanUtils.toString(getDayCount())).append(',').append(' ');
    buf.append("frequency").append('=').append(JodaBeanUtils.toString(getFrequency())).append(',').append(' ');
    buf.append("cap").append('=').append(JodaBeanUtils.toString(isCap())).append(',').append(' ');
    buf.append("ibor").append('=').append(JodaBeanUtils.toString(isIbor())).append(',').append(' ');
    buf.append("payer").append('=').append(JodaBeanUtils.toString(isPayer())).append(',').append(' ');
    buf.append("maturityDate").append('=').append(JodaBeanUtils.toString(getMaturityDate())).append(',').append(' ');
    buf.append("notional").append('=').append(JodaBeanUtils.toString(getNotional())).append(',').append(' ');
    buf.append("startDate").append('=').append(JodaBeanUtils.toString(getStartDate())).append(',').append(' ');
    buf.append("strike").append('=').append(JodaBeanUtils.toString(getStrike())).append(',').append(' ');
    buf.append("underlyingIdentifier").append('=').append(JodaBeanUtils.toString(getUnderlyingIdentifier())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CapFloorSecurityBean}.
   */
  public static class Meta extends SecurityBean.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<CurrencyBean> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", CapFloorSecurityBean.class, CurrencyBean.class);
    /**
     * The meta-property for the {@code dayCount} property.
     */
    private final MetaProperty<DayCountBean> _dayCount = DirectMetaProperty.ofReadWrite(
        this, "dayCount", CapFloorSecurityBean.class, DayCountBean.class);
    /**
     * The meta-property for the {@code frequency} property.
     */
    private final MetaProperty<FrequencyBean> _frequency = DirectMetaProperty.ofReadWrite(
        this, "frequency", CapFloorSecurityBean.class, FrequencyBean.class);
    /**
     * The meta-property for the {@code cap} property.
     */
    private final MetaProperty<Boolean> _cap = DirectMetaProperty.ofReadWrite(
        this, "cap", CapFloorSecurityBean.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code ibor} property.
     */
    private final MetaProperty<Boolean> _ibor = DirectMetaProperty.ofReadWrite(
        this, "ibor", CapFloorSecurityBean.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code payer} property.
     */
    private final MetaProperty<Boolean> _payer = DirectMetaProperty.ofReadWrite(
        this, "payer", CapFloorSecurityBean.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code maturityDate} property.
     */
    private final MetaProperty<ZonedDateTimeBean> _maturityDate = DirectMetaProperty.ofReadWrite(
        this, "maturityDate", CapFloorSecurityBean.class, ZonedDateTimeBean.class);
    /**
     * The meta-property for the {@code notional} property.
     */
    private final MetaProperty<Double> _notional = DirectMetaProperty.ofReadWrite(
        this, "notional", CapFloorSecurityBean.class, Double.TYPE);
    /**
     * The meta-property for the {@code startDate} property.
     */
    private final MetaProperty<ZonedDateTimeBean> _startDate = DirectMetaProperty.ofReadWrite(
        this, "startDate", CapFloorSecurityBean.class, ZonedDateTimeBean.class);
    /**
     * The meta-property for the {@code strike} property.
     */
    private final MetaProperty<Double> _strike = DirectMetaProperty.ofReadWrite(
        this, "strike", CapFloorSecurityBean.class, Double.TYPE);
    /**
     * The meta-property for the {@code underlyingIdentifier} property.
     */
    private final MetaProperty<ExternalIdBean> _underlyingIdentifier = DirectMetaProperty.ofReadWrite(
        this, "underlyingIdentifier", CapFloorSecurityBean.class, ExternalIdBean.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "currency",
        "dayCount",
        "frequency",
        "cap",
        "ibor",
        "payer",
        "maturityDate",
        "notional",
        "startDate",
        "strike",
        "underlyingIdentifier");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return _currency;
        case 1905311443:  // dayCount
          return _dayCount;
        case -70023844:  // frequency
          return _frequency;
        case 98258:  // cap
          return _cap;
        case 3225788:  // ibor
          return _ibor;
        case 106443605:  // payer
          return _payer;
        case -414641441:  // maturityDate
          return _maturityDate;
        case 1585636160:  // notional
          return _notional;
        case -2129778896:  // startDate
          return _startDate;
        case -891985998:  // strike
          return _strike;
        case 368639974:  // underlyingIdentifier
          return _underlyingIdentifier;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CapFloorSecurityBean> builder() {
      return new DirectBeanBuilder<CapFloorSecurityBean>(new CapFloorSecurityBean());
    }

    @Override
    public Class<? extends CapFloorSecurityBean> beanType() {
      return CapFloorSecurityBean.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CurrencyBean> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code dayCount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<DayCountBean> dayCount() {
      return _dayCount;
    }

    /**
     * The meta-property for the {@code frequency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<FrequencyBean> frequency() {
      return _frequency;
    }

    /**
     * The meta-property for the {@code cap} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> cap() {
      return _cap;
    }

    /**
     * The meta-property for the {@code ibor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> ibor() {
      return _ibor;
    }

    /**
     * The meta-property for the {@code payer} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> payer() {
      return _payer;
    }

    /**
     * The meta-property for the {@code maturityDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTimeBean> maturityDate() {
      return _maturityDate;
    }

    /**
     * The meta-property for the {@code notional} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> notional() {
      return _notional;
    }

    /**
     * The meta-property for the {@code startDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTimeBean> startDate() {
      return _startDate;
    }

    /**
     * The meta-property for the {@code strike} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> strike() {
      return _strike;
    }

    /**
     * The meta-property for the {@code underlyingIdentifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalIdBean> underlyingIdentifier() {
      return _underlyingIdentifier;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return ((CapFloorSecurityBean) bean).getCurrency();
        case 1905311443:  // dayCount
          return ((CapFloorSecurityBean) bean).getDayCount();
        case -70023844:  // frequency
          return ((CapFloorSecurityBean) bean).getFrequency();
        case 98258:  // cap
          return ((CapFloorSecurityBean) bean).isCap();
        case 3225788:  // ibor
          return ((CapFloorSecurityBean) bean).isIbor();
        case 106443605:  // payer
          return ((CapFloorSecurityBean) bean).isPayer();
        case -414641441:  // maturityDate
          return ((CapFloorSecurityBean) bean).getMaturityDate();
        case 1585636160:  // notional
          return ((CapFloorSecurityBean) bean).getNotional();
        case -2129778896:  // startDate
          return ((CapFloorSecurityBean) bean).getStartDate();
        case -891985998:  // strike
          return ((CapFloorSecurityBean) bean).getStrike();
        case 368639974:  // underlyingIdentifier
          return ((CapFloorSecurityBean) bean).getUnderlyingIdentifier();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          ((CapFloorSecurityBean) bean).setCurrency((CurrencyBean) newValue);
          return;
        case 1905311443:  // dayCount
          ((CapFloorSecurityBean) bean).setDayCount((DayCountBean) newValue);
          return;
        case -70023844:  // frequency
          ((CapFloorSecurityBean) bean).setFrequency((FrequencyBean) newValue);
          return;
        case 98258:  // cap
          ((CapFloorSecurityBean) bean).setCap((Boolean) newValue);
          return;
        case 3225788:  // ibor
          ((CapFloorSecurityBean) bean).setIbor((Boolean) newValue);
          return;
        case 106443605:  // payer
          ((CapFloorSecurityBean) bean).setPayer((Boolean) newValue);
          return;
        case -414641441:  // maturityDate
          ((CapFloorSecurityBean) bean).setMaturityDate((ZonedDateTimeBean) newValue);
          return;
        case 1585636160:  // notional
          ((CapFloorSecurityBean) bean).setNotional((Double) newValue);
          return;
        case -2129778896:  // startDate
          ((CapFloorSecurityBean) bean).setStartDate((ZonedDateTimeBean) newValue);
          return;
        case -891985998:  // strike
          ((CapFloorSecurityBean) bean).setStrike((Double) newValue);
          return;
        case 368639974:  // underlyingIdentifier
          ((CapFloorSecurityBean) bean).setUnderlyingIdentifier((ExternalIdBean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
