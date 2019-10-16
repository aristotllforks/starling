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
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.ArgumentChecker;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Convention for exchange-traded Federal fund futures.
 */
@ConventionMetaData(description = "Federal funds future", group = ConventionGroups.ETF)
@BeanDefinition
public class FederalFundsFutureConvention extends ExchangeTradedFutureAndOptionConvention {

  /**
   * Type of the convention.
   */
  public static final ConventionType TYPE = ConventionType.of("FederalFundsFuture");

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The underlying index convention.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _indexConvention;
  /**
   * The notional for the future.
   */
  @PropertyDefinition
  private double _notional;

  /**
   * For the builder.
   */
  protected FederalFundsFutureConvention() {
    super();
  }

  /**
   * Creates an instance.
   *
   * @param name
   *          the convention name, not null
   * @param externalIdBundle
   *          the external identifiers for this convention, not null
   * @param expiryConvention
   *          the expiry convention, not null
   * @param exchangeCalendar
   *          the exchange calendar, not null
   * @param indexConvention
   *          the index convention, not null
   * @param notional
   *          the notional
   */
  public FederalFundsFutureConvention(final String name, final ExternalIdBundle externalIdBundle, final ExternalId expiryConvention,
      final ExternalId exchangeCalendar, final ExternalId indexConvention, final double notional) {
    super(name, externalIdBundle, expiryConvention, exchangeCalendar);
    setIndexConvention(indexConvention);
    setNotional(notional);
  }

  // -------------------------------------------------------------------------
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
   * @param <T>
   *          the result type of the visitor
   * @param visitor
   *          the visitor, not null
   * @return the result
   */
  @Override
  public <T> T accept(final FinancialConventionVisitor<T> visitor) {
    ArgumentChecker.notNull(visitor, "visitor");
    return visitor.visitFederalFundsFutureConvention(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FederalFundsFutureConvention}.
   * @return the meta-bean, not null
   */
  public static FederalFundsFutureConvention.Meta meta() {
    return FederalFundsFutureConvention.Meta.INSTANCE;
  }

  static {
    MetaBean.register(FederalFundsFutureConvention.Meta.INSTANCE);
  }

  @Override
  public FederalFundsFutureConvention.Meta metaBean() {
    return FederalFundsFutureConvention.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying index convention.
   * @return the value of the property, not null
   */
  public ExternalId getIndexConvention() {
    return _indexConvention;
  }

  /**
   * Sets the underlying index convention.
   * @param indexConvention  the new value of the property, not null
   */
  public void setIndexConvention(ExternalId indexConvention) {
    JodaBeanUtils.notNull(indexConvention, "indexConvention");
    this._indexConvention = indexConvention;
  }

  /**
   * Gets the the {@code indexConvention} property.
   * @return the property, not null
   */
  public final Property<ExternalId> indexConvention() {
    return metaBean().indexConvention().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the notional for the future.
   * @return the value of the property
   */
  public double getNotional() {
    return _notional;
  }

  /**
   * Sets the notional for the future.
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
  @Override
  public FederalFundsFutureConvention clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FederalFundsFutureConvention other = (FederalFundsFutureConvention) obj;
      return JodaBeanUtils.equal(getIndexConvention(), other.getIndexConvention()) &&
          JodaBeanUtils.equal(getNotional(), other.getNotional()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getIndexConvention());
    hash = hash * 31 + JodaBeanUtils.hashCode(getNotional());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("FederalFundsFutureConvention{");
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
    buf.append("indexConvention").append('=').append(JodaBeanUtils.toString(getIndexConvention())).append(',').append(' ');
    buf.append("notional").append('=').append(JodaBeanUtils.toString(getNotional())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FederalFundsFutureConvention}.
   */
  public static class Meta extends ExchangeTradedFutureAndOptionConvention.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code indexConvention} property.
     */
    private final MetaProperty<ExternalId> _indexConvention = DirectMetaProperty.ofReadWrite(
        this, "indexConvention", FederalFundsFutureConvention.class, ExternalId.class);
    /**
     * The meta-property for the {@code notional} property.
     */
    private final MetaProperty<Double> _notional = DirectMetaProperty.ofReadWrite(
        this, "notional", FederalFundsFutureConvention.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "indexConvention",
        "notional");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -668532253:  // indexConvention
          return _indexConvention;
        case 1585636160:  // notional
          return _notional;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FederalFundsFutureConvention> builder() {
      return new DirectBeanBuilder<>(new FederalFundsFutureConvention());
    }

    @Override
    public Class<? extends FederalFundsFutureConvention> beanType() {
      return FederalFundsFutureConvention.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code indexConvention} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> indexConvention() {
      return _indexConvention;
    }

    /**
     * The meta-property for the {@code notional} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> notional() {
      return _notional;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -668532253:  // indexConvention
          return ((FederalFundsFutureConvention) bean).getIndexConvention();
        case 1585636160:  // notional
          return ((FederalFundsFutureConvention) bean).getNotional();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -668532253:  // indexConvention
          ((FederalFundsFutureConvention) bean).setIndexConvention((ExternalId) newValue);
          return;
        case 1585636160:  // notional
          ((FederalFundsFutureConvention) bean).setNotional((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((FederalFundsFutureConvention) bean)._indexConvention, "indexConvention");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
