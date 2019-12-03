/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.irs;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.LocalDate;

import com.opengamma.financial.convention.InterestRateSwapLegConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.frequency.PeriodFrequency;
import com.opengamma.financial.convention.rolldate.RollDateAdjuster;

/**
 * Class to wrap a convention and to allow overrides to the convention to be accessed in a uniform manner.
 *
 * Currently assumes that all overrides are available if provided for a period.
 */
@BeanDefinition
public final class FloatingInterestRateSwapLegSchedule implements ImmutableBean {

  /**
   * Empty schedule.
   */
  public static final FloatingInterestRateSwapLegSchedule NONE = builder().build();

  @PropertyDefinition
  private final InterestRateSwapLegConvention _convention;

  // overrides to convention schedules, if provided for a given period use the provided date
  // and ignore any roll convention etc.

  /**
   * The periods for which custom dates are provided.
   */
  @PropertyDefinition
  private final int[] _dates;

  /**
   * The custom payment dates.
   */
  @PropertyDefinition
  private final LocalDate[] _paymentDates;

  /**
   * The custom calculation dates.
   */
  @PropertyDefinition
  private final LocalDate[] _calculationDates;

  /**
   * Gets the payment date of the nth period. If an override has been provided, this date is used. Otherwise, the date is calculated using the payment frequency
   * of the swap leg convention, the roll date adjuster, the business day convention and the number of settlement days.
   *
   * @param nthPeriod
   *          the period for which to get the payment date
   * @param startDate
   *          the start date
   * @param calendar
   *          the working day calendar
   * @return the payment date
   */
  public LocalDate getPaymentDate(final int nthPeriod, final LocalDate startDate, final Calendar calendar) {
    if (getPaymentDates() != null && getPaymentDates().length != 0) {
      final int index = Arrays.binarySearch(_dates, nthPeriod);
      if (index >= 0) {
        return _paymentDates[index];
      }
    }
    // override not provided - fall back to convention
    final int monthsToAdvance = (int) PeriodFrequency.convertToPeriodFrequency(_convention.getPaymentFrequency()).getPeriod().toTotalMonths() * nthPeriod;
    final RollDateAdjuster adjuster = _convention.getRollConvention().getRollDateAdjuster(monthsToAdvance);
    final BusinessDayConvention convention = _convention.getPaymentDayConvention();
    final int settlementDays = _convention.getSettlementDays();
    return convention.adjustDate(calendar, startDate.plusMonths(adjuster.getMonthsToAdjust()).minusDays(settlementDays).with(adjuster));
  }

  /**
   * Gets the calculation date of the nth period. If an override has been provided, this date is used. Otherwise, the date is calculated using the payment
   * frequency of the swap leg convention, the roll date adjuster, the business day convention and the number of settlement days.
   *
   * @param nthPeriod
   *          the period for which to get the payment date
   * @param startDate
   *          the start date
   * @param calendar
   *          the working day calendar
   * @return the payment date
   */
  public LocalDate getCalculationDate(final int nthPeriod, final LocalDate startDate, final Calendar calendar) {
    if (getCalculationDates() != null && getCalculationDates().length != 0) {
      final int index = Arrays.binarySearch(_dates, nthPeriod);
      if (index >= 0) {
        return _calculationDates[index];
      }
    }
    // override not provided - fall back to convention
    final int monthsToAdvance = (int) PeriodFrequency.convertToPeriodFrequency(_convention.getCalculationFrequency()).getPeriod().toTotalMonths() * nthPeriod;
    final RollDateAdjuster adjuster = _convention.getRollConvention().getRollDateAdjuster(monthsToAdvance);
    final BusinessDayConvention convention = _convention.getCalculationBusinessDayConvention();
    return convention.adjustDate(calendar, startDate.plusMonths(adjuster.getMonthsToAdjust()).with(adjuster));
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FloatingInterestRateSwapLegSchedule}.
   * @return the meta-bean, not null
   */
  public static FloatingInterestRateSwapLegSchedule.Meta meta() {
    return FloatingInterestRateSwapLegSchedule.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(FloatingInterestRateSwapLegSchedule.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static FloatingInterestRateSwapLegSchedule.Builder builder() {
    return new FloatingInterestRateSwapLegSchedule.Builder();
  }

  private FloatingInterestRateSwapLegSchedule(
      InterestRateSwapLegConvention convention,
      int[] dates,
      LocalDate[] paymentDates,
      LocalDate[] calculationDates) {
    this._convention = convention;
    this._dates = (dates != null ? dates.clone() : null);
    this._paymentDates = paymentDates;
    this._calculationDates = calculationDates;
  }

  @Override
  public FloatingInterestRateSwapLegSchedule.Meta metaBean() {
    return FloatingInterestRateSwapLegSchedule.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the convention.
   * @return the value of the property
   */
  public InterestRateSwapLegConvention getConvention() {
    return _convention;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the periods for which custom dates are provided.
   * @return the value of the property
   */
  public int[] getDates() {
    return (_dates != null ? _dates.clone() : null);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the custom payment dates.
   * @return the value of the property
   */
  public LocalDate[] getPaymentDates() {
    return _paymentDates;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the custom calculation dates.
   * @return the value of the property
   */
  public LocalDate[] getCalculationDates() {
    return _calculationDates;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FloatingInterestRateSwapLegSchedule other = (FloatingInterestRateSwapLegSchedule) obj;
      return JodaBeanUtils.equal(_convention, other._convention) &&
          JodaBeanUtils.equal(_dates, other._dates) &&
          JodaBeanUtils.equal(_paymentDates, other._paymentDates) &&
          JodaBeanUtils.equal(_calculationDates, other._calculationDates);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_convention);
    hash = hash * 31 + JodaBeanUtils.hashCode(_dates);
    hash = hash * 31 + JodaBeanUtils.hashCode(_paymentDates);
    hash = hash * 31 + JodaBeanUtils.hashCode(_calculationDates);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("FloatingInterestRateSwapLegSchedule{");
    buf.append("convention").append('=').append(_convention).append(',').append(' ');
    buf.append("dates").append('=').append(_dates).append(',').append(' ');
    buf.append("paymentDates").append('=').append(_paymentDates).append(',').append(' ');
    buf.append("calculationDates").append('=').append(JodaBeanUtils.toString(_calculationDates));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FloatingInterestRateSwapLegSchedule}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code convention} property.
     */
    private final MetaProperty<InterestRateSwapLegConvention> _convention = DirectMetaProperty.ofImmutable(
        this, "convention", FloatingInterestRateSwapLegSchedule.class, InterestRateSwapLegConvention.class);
    /**
     * The meta-property for the {@code dates} property.
     */
    private final MetaProperty<int[]> _dates = DirectMetaProperty.ofImmutable(
        this, "dates", FloatingInterestRateSwapLegSchedule.class, int[].class);
    /**
     * The meta-property for the {@code paymentDates} property.
     */
    private final MetaProperty<LocalDate[]> _paymentDates = DirectMetaProperty.ofImmutable(
        this, "paymentDates", FloatingInterestRateSwapLegSchedule.class, LocalDate[].class);
    /**
     * The meta-property for the {@code calculationDates} property.
     */
    private final MetaProperty<LocalDate[]> _calculationDates = DirectMetaProperty.ofImmutable(
        this, "calculationDates", FloatingInterestRateSwapLegSchedule.class, LocalDate[].class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "convention",
        "dates",
        "paymentDates",
        "calculationDates");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2039569265:  // convention
          return _convention;
        case 95356549:  // dates
          return _dates;
        case -522438625:  // paymentDates
          return _paymentDates;
        case 739970364:  // calculationDates
          return _calculationDates;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public FloatingInterestRateSwapLegSchedule.Builder builder() {
      return new FloatingInterestRateSwapLegSchedule.Builder();
    }

    @Override
    public Class<? extends FloatingInterestRateSwapLegSchedule> beanType() {
      return FloatingInterestRateSwapLegSchedule.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code convention} property.
     * @return the meta-property, not null
     */
    public MetaProperty<InterestRateSwapLegConvention> convention() {
      return _convention;
    }

    /**
     * The meta-property for the {@code dates} property.
     * @return the meta-property, not null
     */
    public MetaProperty<int[]> dates() {
      return _dates;
    }

    /**
     * The meta-property for the {@code paymentDates} property.
     * @return the meta-property, not null
     */
    public MetaProperty<LocalDate[]> paymentDates() {
      return _paymentDates;
    }

    /**
     * The meta-property for the {@code calculationDates} property.
     * @return the meta-property, not null
     */
    public MetaProperty<LocalDate[]> calculationDates() {
      return _calculationDates;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2039569265:  // convention
          return ((FloatingInterestRateSwapLegSchedule) bean).getConvention();
        case 95356549:  // dates
          return ((FloatingInterestRateSwapLegSchedule) bean).getDates();
        case -522438625:  // paymentDates
          return ((FloatingInterestRateSwapLegSchedule) bean).getPaymentDates();
        case 739970364:  // calculationDates
          return ((FloatingInterestRateSwapLegSchedule) bean).getCalculationDates();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code FloatingInterestRateSwapLegSchedule}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<FloatingInterestRateSwapLegSchedule> {

    private InterestRateSwapLegConvention _convention;
    private int[] _dates;
    private LocalDate[] _paymentDates;
    private LocalDate[] _calculationDates;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(FloatingInterestRateSwapLegSchedule beanToCopy) {
      this._convention = beanToCopy.getConvention();
      this._dates = (beanToCopy.getDates() != null ? beanToCopy.getDates().clone() : null);
      this._paymentDates = beanToCopy.getPaymentDates();
      this._calculationDates = beanToCopy.getCalculationDates();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2039569265:  // convention
          return _convention;
        case 95356549:  // dates
          return _dates;
        case -522438625:  // paymentDates
          return _paymentDates;
        case 739970364:  // calculationDates
          return _calculationDates;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 2039569265:  // convention
          this._convention = (InterestRateSwapLegConvention) newValue;
          break;
        case 95356549:  // dates
          this._dates = (int[]) newValue;
          break;
        case -522438625:  // paymentDates
          this._paymentDates = (LocalDate[]) newValue;
          break;
        case 739970364:  // calculationDates
          this._calculationDates = (LocalDate[]) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    /**
     * @deprecated Use Joda-Convert in application code
     */
    @Override
    @Deprecated
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    /**
     * @deprecated Use Joda-Convert in application code
     */
    @Override
    @Deprecated
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    /**
     * @deprecated Loop in application code
     */
    @Override
    @Deprecated
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public FloatingInterestRateSwapLegSchedule build() {
      return new FloatingInterestRateSwapLegSchedule(
          _convention,
          _dates,
          _paymentDates,
          _calculationDates);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the convention.
     * @param convention  the new value
     * @return this, for chaining, not null
     */
    public Builder convention(InterestRateSwapLegConvention convention) {
      this._convention = convention;
      return this;
    }

    /**
     * Sets the periods for which custom dates are provided.
     * @param dates  the new value
     * @return this, for chaining, not null
     */
    public Builder dates(int... dates) {
      this._dates = dates;
      return this;
    }

    /**
     * Sets the custom payment dates.
     * @param paymentDates  the new value
     * @return this, for chaining, not null
     */
    public Builder paymentDates(LocalDate... paymentDates) {
      this._paymentDates = paymentDates;
      return this;
    }

    /**
     * Sets the custom calculation dates.
     * @param calculationDates  the new value
     * @return this, for chaining, not null
     */
    public Builder calculationDates(LocalDate... calculationDates) {
      this._calculationDates = calculationDates;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("FloatingInterestRateSwapLegSchedule.Builder{");
      buf.append("convention").append('=').append(JodaBeanUtils.toString(_convention)).append(',').append(' ');
      buf.append("dates").append('=').append(JodaBeanUtils.toString(_dates)).append(',').append(' ');
      buf.append("paymentDates").append('=').append(JodaBeanUtils.toString(_paymentDates)).append(',').append(' ');
      buf.append("calculationDates").append('=').append(JodaBeanUtils.toString(_calculationDates));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
