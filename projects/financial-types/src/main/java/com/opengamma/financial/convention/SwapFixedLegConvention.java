/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.convention.ConventionGroups;
import com.opengamma.core.convention.ConventionMetaData;
import com.opengamma.core.convention.ConventionType;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Tenor;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Convention for a fixed swap leg.
 */
@ConventionMetaData(description = "Fixed swap leg", group = ConventionGroups.SWAP_LEG_CONVENTION)
@BeanDefinition
public class SwapFixedLegConvention extends FinancialConvention {

  /**
   * Type of the convention.
   */
  public static final ConventionType TYPE = ConventionType.of("SwapFixedLeg");

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The payment tenor.
   */
  @PropertyDefinition(validate = "notNull")
  private Tenor _paymentTenor;
  /**
   * The day count.
   */
  @PropertyDefinition(validate = "notNull")
  private DayCount _dayCount;
  /**
   * The business day convention.
   */
  @PropertyDefinition(validate = "notNull")
  private BusinessDayConvention _businessDayConvention;
  /**
   * The currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;
  /**
   * The region calendar.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _regionCalendar;
  /**
   * The number of settlement days.
   */
  @PropertyDefinition
  private int _settlementDays;
  /**
   * Whether dates follow the end-of-month rule.
   */
  @PropertyDefinition
  private boolean _isEOM;
  /**
   * The stub type.
   */
  @PropertyDefinition(validate = "notNull")
  private StubType _stubType;
  /**
   * Whether the notional exchanged.
   */
  @PropertyDefinition
  private boolean _isExchangeNotional;
  /**
   * The payment lag in days.
   */
  @PropertyDefinition
  private int _paymentLag;

  /**
   * Creates an instance.
   */
  protected SwapFixedLegConvention() {
    super();
  }

  /**
   * Creates an instance.
   *
   * @param name  the name of the convention, not null
   * @param externalIdBundle  the external identifiers for this convention, not null
   * @param paymentTenor  the payment tenor, not null
   * @param dayCount  the day-count, not null
   * @param businessDayConvention  the business-day convention, not null
   * @param currency  the currency, not null
   * @param regionCalendar  the region calendar, not null
   * @param settlementDays  the number of days to settle
   * @param isEOM  true if dates follow the end-of-month rule
   * @param stubType  the stub type, not null
   * @param isExchangeNotional  true if notional is to be exchanged
   * @param paymentLag  the payment lag
   */
  public SwapFixedLegConvention(
      final String name, final ExternalIdBundle externalIdBundle, final Tenor paymentTenor,
      final DayCount dayCount, final BusinessDayConvention businessDayConvention, final Currency currency,
      final ExternalId regionCalendar, final int settlementDays, final boolean isEOM,
      final StubType stubType, final boolean isExchangeNotional, final int paymentLag) {
    super(name, externalIdBundle);
    setPaymentTenor(paymentTenor);
    setDayCount(dayCount);
    setBusinessDayConvention(businessDayConvention);
    setCurrency(currency);
    setRegionCalendar(regionCalendar);
    setSettlementDays(settlementDays);
    setIsEOM(isEOM);
    setStubType(stubType);
    setIsExchangeNotional(isExchangeNotional);
    setPaymentLag(paymentLag);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the type identifying this convention.
   *
   * @return the {@link #TYPE} constant, not null
   */
  @Override
  public ConventionType getConventionType() {
    return TYPE;
  }

  /**
   * Accepts a visitor to manage traversal of the hierarchy.
   *
   * @param <T>  the result type of the visitor
   * @param visitor  the visitor, not null
   * @return the result
   */
  @Override
  public <T> T accept(final FinancialConventionVisitor<T> visitor) {
    ArgumentChecker.notNull(visitor, "visitor");
    return visitor.visitSwapFixedLegConvention(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code SwapFixedLegConvention}.
   * @return the meta-bean, not null
   */
  public static SwapFixedLegConvention.Meta meta() {
    return SwapFixedLegConvention.Meta.INSTANCE;
  }

  static {
    MetaBean.register(SwapFixedLegConvention.Meta.INSTANCE);
  }

  @Override
  public SwapFixedLegConvention.Meta metaBean() {
    return SwapFixedLegConvention.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the payment tenor.
   * @return the value of the property, not null
   */
  public Tenor getPaymentTenor() {
    return _paymentTenor;
  }

  /**
   * Sets the payment tenor.
   * @param paymentTenor  the new value of the property, not null
   */
  public void setPaymentTenor(Tenor paymentTenor) {
    JodaBeanUtils.notNull(paymentTenor, "paymentTenor");
    this._paymentTenor = paymentTenor;
  }

  /**
   * Gets the the {@code paymentTenor} property.
   * @return the property, not null
   */
  public final Property<Tenor> paymentTenor() {
    return metaBean().paymentTenor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the day count.
   * @return the value of the property, not null
   */
  public DayCount getDayCount() {
    return _dayCount;
  }

  /**
   * Sets the day count.
   * @param dayCount  the new value of the property, not null
   */
  public void setDayCount(DayCount dayCount) {
    JodaBeanUtils.notNull(dayCount, "dayCount");
    this._dayCount = dayCount;
  }

  /**
   * Gets the the {@code dayCount} property.
   * @return the property, not null
   */
  public final Property<DayCount> dayCount() {
    return metaBean().dayCount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the business day convention.
   * @return the value of the property, not null
   */
  public BusinessDayConvention getBusinessDayConvention() {
    return _businessDayConvention;
  }

  /**
   * Sets the business day convention.
   * @param businessDayConvention  the new value of the property, not null
   */
  public void setBusinessDayConvention(BusinessDayConvention businessDayConvention) {
    JodaBeanUtils.notNull(businessDayConvention, "businessDayConvention");
    this._businessDayConvention = businessDayConvention;
  }

  /**
   * Gets the the {@code businessDayConvention} property.
   * @return the property, not null
   */
  public final Property<BusinessDayConvention> businessDayConvention() {
    return metaBean().businessDayConvention().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency.
   * @param currency  the new value of the property, not null
   */
  public void setCurrency(Currency currency) {
    JodaBeanUtils.notNull(currency, "currency");
    this._currency = currency;
  }

  /**
   * Gets the the {@code currency} property.
   * @return the property, not null
   */
  public final Property<Currency> currency() {
    return metaBean().currency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the region calendar.
   * @return the value of the property, not null
   */
  public ExternalId getRegionCalendar() {
    return _regionCalendar;
  }

  /**
   * Sets the region calendar.
   * @param regionCalendar  the new value of the property, not null
   */
  public void setRegionCalendar(ExternalId regionCalendar) {
    JodaBeanUtils.notNull(regionCalendar, "regionCalendar");
    this._regionCalendar = regionCalendar;
  }

  /**
   * Gets the the {@code regionCalendar} property.
   * @return the property, not null
   */
  public final Property<ExternalId> regionCalendar() {
    return metaBean().regionCalendar().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the number of settlement days.
   * @return the value of the property
   */
  public int getSettlementDays() {
    return _settlementDays;
  }

  /**
   * Sets the number of settlement days.
   * @param settlementDays  the new value of the property
   */
  public void setSettlementDays(int settlementDays) {
    this._settlementDays = settlementDays;
  }

  /**
   * Gets the the {@code settlementDays} property.
   * @return the property, not null
   */
  public final Property<Integer> settlementDays() {
    return metaBean().settlementDays().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether dates follow the end-of-month rule.
   * @return the value of the property
   */
  public boolean isIsEOM() {
    return _isEOM;
  }

  /**
   * Sets whether dates follow the end-of-month rule.
   * @param isEOM  the new value of the property
   */
  public void setIsEOM(boolean isEOM) {
    this._isEOM = isEOM;
  }

  /**
   * Gets the the {@code isEOM} property.
   * @return the property, not null
   */
  public final Property<Boolean> isEOM() {
    return metaBean().isEOM().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the stub type.
   * @return the value of the property, not null
   */
  public StubType getStubType() {
    return _stubType;
  }

  /**
   * Sets the stub type.
   * @param stubType  the new value of the property, not null
   */
  public void setStubType(StubType stubType) {
    JodaBeanUtils.notNull(stubType, "stubType");
    this._stubType = stubType;
  }

  /**
   * Gets the the {@code stubType} property.
   * @return the property, not null
   */
  public final Property<StubType> stubType() {
    return metaBean().stubType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether the notional exchanged.
   * @return the value of the property
   */
  public boolean isIsExchangeNotional() {
    return _isExchangeNotional;
  }

  /**
   * Sets whether the notional exchanged.
   * @param isExchangeNotional  the new value of the property
   */
  public void setIsExchangeNotional(boolean isExchangeNotional) {
    this._isExchangeNotional = isExchangeNotional;
  }

  /**
   * Gets the the {@code isExchangeNotional} property.
   * @return the property, not null
   */
  public final Property<Boolean> isExchangeNotional() {
    return metaBean().isExchangeNotional().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the payment lag in days.
   * @return the value of the property
   */
  public int getPaymentLag() {
    return _paymentLag;
  }

  /**
   * Sets the payment lag in days.
   * @param paymentLag  the new value of the property
   */
  public void setPaymentLag(int paymentLag) {
    this._paymentLag = paymentLag;
  }

  /**
   * Gets the the {@code paymentLag} property.
   * @return the property, not null
   */
  public final Property<Integer> paymentLag() {
    return metaBean().paymentLag().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public SwapFixedLegConvention clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SwapFixedLegConvention other = (SwapFixedLegConvention) obj;
      return JodaBeanUtils.equal(getPaymentTenor(), other.getPaymentTenor()) &&
          JodaBeanUtils.equal(getDayCount(), other.getDayCount()) &&
          JodaBeanUtils.equal(getBusinessDayConvention(), other.getBusinessDayConvention()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getRegionCalendar(), other.getRegionCalendar()) &&
          (getSettlementDays() == other.getSettlementDays()) &&
          (isIsEOM() == other.isIsEOM()) &&
          JodaBeanUtils.equal(getStubType(), other.getStubType()) &&
          (isIsExchangeNotional() == other.isIsExchangeNotional()) &&
          (getPaymentLag() == other.getPaymentLag()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getPaymentTenor());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDayCount());
    hash = hash * 31 + JodaBeanUtils.hashCode(getBusinessDayConvention());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegionCalendar());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSettlementDays());
    hash = hash * 31 + JodaBeanUtils.hashCode(isIsEOM());
    hash = hash * 31 + JodaBeanUtils.hashCode(getStubType());
    hash = hash * 31 + JodaBeanUtils.hashCode(isIsExchangeNotional());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPaymentLag());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(352);
    buf.append("SwapFixedLegConvention{");
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
    buf.append("paymentTenor").append('=').append(JodaBeanUtils.toString(getPaymentTenor())).append(',').append(' ');
    buf.append("dayCount").append('=').append(JodaBeanUtils.toString(getDayCount())).append(',').append(' ');
    buf.append("businessDayConvention").append('=').append(JodaBeanUtils.toString(getBusinessDayConvention())).append(',').append(' ');
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency())).append(',').append(' ');
    buf.append("regionCalendar").append('=').append(JodaBeanUtils.toString(getRegionCalendar())).append(',').append(' ');
    buf.append("settlementDays").append('=').append(JodaBeanUtils.toString(getSettlementDays())).append(',').append(' ');
    buf.append("isEOM").append('=').append(JodaBeanUtils.toString(isIsEOM())).append(',').append(' ');
    buf.append("stubType").append('=').append(JodaBeanUtils.toString(getStubType())).append(',').append(' ');
    buf.append("isExchangeNotional").append('=').append(JodaBeanUtils.toString(isIsExchangeNotional())).append(',').append(' ');
    buf.append("paymentLag").append('=').append(JodaBeanUtils.toString(getPaymentLag())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SwapFixedLegConvention}.
   */
  public static class Meta extends FinancialConvention.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code paymentTenor} property.
     */
    private final MetaProperty<Tenor> _paymentTenor = DirectMetaProperty.ofReadWrite(
        this, "paymentTenor", SwapFixedLegConvention.class, Tenor.class);
    /**
     * The meta-property for the {@code dayCount} property.
     */
    private final MetaProperty<DayCount> _dayCount = DirectMetaProperty.ofReadWrite(
        this, "dayCount", SwapFixedLegConvention.class, DayCount.class);
    /**
     * The meta-property for the {@code businessDayConvention} property.
     */
    private final MetaProperty<BusinessDayConvention> _businessDayConvention = DirectMetaProperty.ofReadWrite(
        this, "businessDayConvention", SwapFixedLegConvention.class, BusinessDayConvention.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", SwapFixedLegConvention.class, Currency.class);
    /**
     * The meta-property for the {@code regionCalendar} property.
     */
    private final MetaProperty<ExternalId> _regionCalendar = DirectMetaProperty.ofReadWrite(
        this, "regionCalendar", SwapFixedLegConvention.class, ExternalId.class);
    /**
     * The meta-property for the {@code settlementDays} property.
     */
    private final MetaProperty<Integer> _settlementDays = DirectMetaProperty.ofReadWrite(
        this, "settlementDays", SwapFixedLegConvention.class, Integer.TYPE);
    /**
     * The meta-property for the {@code isEOM} property.
     */
    private final MetaProperty<Boolean> _isEOM = DirectMetaProperty.ofReadWrite(
        this, "isEOM", SwapFixedLegConvention.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code stubType} property.
     */
    private final MetaProperty<StubType> _stubType = DirectMetaProperty.ofReadWrite(
        this, "stubType", SwapFixedLegConvention.class, StubType.class);
    /**
     * The meta-property for the {@code isExchangeNotional} property.
     */
    private final MetaProperty<Boolean> _isExchangeNotional = DirectMetaProperty.ofReadWrite(
        this, "isExchangeNotional", SwapFixedLegConvention.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code paymentLag} property.
     */
    private final MetaProperty<Integer> _paymentLag = DirectMetaProperty.ofReadWrite(
        this, "paymentLag", SwapFixedLegConvention.class, Integer.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "paymentTenor",
        "dayCount",
        "businessDayConvention",
        "currency",
        "regionCalendar",
        "settlementDays",
        "isEOM",
        "stubType",
        "isExchangeNotional",
        "paymentLag");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -507548582:  // paymentTenor
          return _paymentTenor;
        case 1905311443:  // dayCount
          return _dayCount;
        case -1002835891:  // businessDayConvention
          return _businessDayConvention;
        case 575402001:  // currency
          return _currency;
        case 1932874322:  // regionCalendar
          return _regionCalendar;
        case -295948000:  // settlementDays
          return _settlementDays;
        case 100464505:  // isEOM
          return _isEOM;
        case 1873675528:  // stubType
          return _stubType;
        case 348962765:  // isExchangeNotional
          return _isExchangeNotional;
        case 1612870060:  // paymentLag
          return _paymentLag;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SwapFixedLegConvention> builder() {
      return new DirectBeanBuilder<>(new SwapFixedLegConvention());
    }

    @Override
    public Class<? extends SwapFixedLegConvention> beanType() {
      return SwapFixedLegConvention.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code paymentTenor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Tenor> paymentTenor() {
      return _paymentTenor;
    }

    /**
     * The meta-property for the {@code dayCount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<DayCount> dayCount() {
      return _dayCount;
    }

    /**
     * The meta-property for the {@code businessDayConvention} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BusinessDayConvention> businessDayConvention() {
      return _businessDayConvention;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code regionCalendar} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> regionCalendar() {
      return _regionCalendar;
    }

    /**
     * The meta-property for the {@code settlementDays} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Integer> settlementDays() {
      return _settlementDays;
    }

    /**
     * The meta-property for the {@code isEOM} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> isEOM() {
      return _isEOM;
    }

    /**
     * The meta-property for the {@code stubType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<StubType> stubType() {
      return _stubType;
    }

    /**
     * The meta-property for the {@code isExchangeNotional} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> isExchangeNotional() {
      return _isExchangeNotional;
    }

    /**
     * The meta-property for the {@code paymentLag} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Integer> paymentLag() {
      return _paymentLag;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -507548582:  // paymentTenor
          return ((SwapFixedLegConvention) bean).getPaymentTenor();
        case 1905311443:  // dayCount
          return ((SwapFixedLegConvention) bean).getDayCount();
        case -1002835891:  // businessDayConvention
          return ((SwapFixedLegConvention) bean).getBusinessDayConvention();
        case 575402001:  // currency
          return ((SwapFixedLegConvention) bean).getCurrency();
        case 1932874322:  // regionCalendar
          return ((SwapFixedLegConvention) bean).getRegionCalendar();
        case -295948000:  // settlementDays
          return ((SwapFixedLegConvention) bean).getSettlementDays();
        case 100464505:  // isEOM
          return ((SwapFixedLegConvention) bean).isIsEOM();
        case 1873675528:  // stubType
          return ((SwapFixedLegConvention) bean).getStubType();
        case 348962765:  // isExchangeNotional
          return ((SwapFixedLegConvention) bean).isIsExchangeNotional();
        case 1612870060:  // paymentLag
          return ((SwapFixedLegConvention) bean).getPaymentLag();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -507548582:  // paymentTenor
          ((SwapFixedLegConvention) bean).setPaymentTenor((Tenor) newValue);
          return;
        case 1905311443:  // dayCount
          ((SwapFixedLegConvention) bean).setDayCount((DayCount) newValue);
          return;
        case -1002835891:  // businessDayConvention
          ((SwapFixedLegConvention) bean).setBusinessDayConvention((BusinessDayConvention) newValue);
          return;
        case 575402001:  // currency
          ((SwapFixedLegConvention) bean).setCurrency((Currency) newValue);
          return;
        case 1932874322:  // regionCalendar
          ((SwapFixedLegConvention) bean).setRegionCalendar((ExternalId) newValue);
          return;
        case -295948000:  // settlementDays
          ((SwapFixedLegConvention) bean).setSettlementDays((Integer) newValue);
          return;
        case 100464505:  // isEOM
          ((SwapFixedLegConvention) bean).setIsEOM((Boolean) newValue);
          return;
        case 1873675528:  // stubType
          ((SwapFixedLegConvention) bean).setStubType((StubType) newValue);
          return;
        case 348962765:  // isExchangeNotional
          ((SwapFixedLegConvention) bean).setIsExchangeNotional((Boolean) newValue);
          return;
        case 1612870060:  // paymentLag
          ((SwapFixedLegConvention) bean).setPaymentLag((Integer) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((SwapFixedLegConvention) bean)._paymentTenor, "paymentTenor");
      JodaBeanUtils.notNull(((SwapFixedLegConvention) bean)._dayCount, "dayCount");
      JodaBeanUtils.notNull(((SwapFixedLegConvention) bean)._businessDayConvention, "businessDayConvention");
      JodaBeanUtils.notNull(((SwapFixedLegConvention) bean)._currency, "currency");
      JodaBeanUtils.notNull(((SwapFixedLegConvention) bean)._regionCalendar, "regionCalendar");
      JodaBeanUtils.notNull(((SwapFixedLegConvention) bean)._stubType, "stubType");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
