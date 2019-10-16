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
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.id.ExternalId;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * A security for interest rate futures.
 */
@BeanDefinition
public class InterestRateFutureSecurity extends FutureSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The underlying identifier.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _underlyingId;

  /**
   * Creates an empty instance.
   */
  protected InterestRateFutureSecurity() {
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
   * @param underlyingIdentifier
   *          the identifier of the underlying
   * @param category
   *          the future category, not null
   */
  public InterestRateFutureSecurity(final Expiry expiry, final String tradingExchange, final String settlementExchange, final Currency currency,
      final double unitAmount, final ExternalId underlyingIdentifier, final String category) {
    super(expiry, tradingExchange, settlementExchange, currency, unitAmount, category);
    setUnderlyingId(underlyingIdentifier);
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitInterestRateFutureSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code InterestRateFutureSecurity}.
   * @return the meta-bean, not null
   */
  public static InterestRateFutureSecurity.Meta meta() {
    return InterestRateFutureSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(InterestRateFutureSecurity.Meta.INSTANCE);
  }

  @Override
  public InterestRateFutureSecurity.Meta metaBean() {
    return InterestRateFutureSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying identifier.
   * @return the value of the property, not null
   */
  public ExternalId getUnderlyingId() {
    return _underlyingId;
  }

  /**
   * Sets the underlying identifier.
   * @param underlyingId  the new value of the property, not null
   */
  public void setUnderlyingId(ExternalId underlyingId) {
    JodaBeanUtils.notNull(underlyingId, "underlyingId");
    this._underlyingId = underlyingId;
  }

  /**
   * Gets the the {@code underlyingId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> underlyingId() {
    return metaBean().underlyingId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public InterestRateFutureSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      InterestRateFutureSecurity other = (InterestRateFutureSecurity) obj;
      return JodaBeanUtils.equal(getUnderlyingId(), other.getUnderlyingId()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnderlyingId());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("InterestRateFutureSecurity{");
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
    buf.append("underlyingId").append('=').append(JodaBeanUtils.toString(getUnderlyingId())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code InterestRateFutureSecurity}.
   */
  public static class Meta extends FutureSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code underlyingId} property.
     */
    private final MetaProperty<ExternalId> _underlyingId = DirectMetaProperty.ofReadWrite(
        this, "underlyingId", InterestRateFutureSecurity.class, ExternalId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "underlyingId");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -771625640:  // underlyingId
          return _underlyingId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends InterestRateFutureSecurity> builder() {
      return new DirectBeanBuilder<>(new InterestRateFutureSecurity());
    }

    @Override
    public Class<? extends InterestRateFutureSecurity> beanType() {
      return InterestRateFutureSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code underlyingId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> underlyingId() {
      return _underlyingId;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -771625640:  // underlyingId
          return ((InterestRateFutureSecurity) bean).getUnderlyingId();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -771625640:  // underlyingId
          ((InterestRateFutureSecurity) bean).setUnderlyingId((ExternalId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((InterestRateFutureSecurity) bean)._underlyingId, "underlyingId");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
