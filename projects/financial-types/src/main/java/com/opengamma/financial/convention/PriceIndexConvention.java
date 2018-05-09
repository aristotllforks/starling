/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import java.util.Map;

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

import com.opengamma.core.convention.ConventionGroups;
import com.opengamma.core.convention.ConventionMetaData;
import com.opengamma.core.convention.ConventionType;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;

/**
 * Convention for price indices.
 */
@ConventionMetaData(description = "Price", group = ConventionGroups.INDEX)
@BeanDefinition
public class PriceIndexConvention extends FinancialConvention {

  /**
   * Type of the convention.
   */
  public static final ConventionType TYPE = ConventionType.of("PriceIndex");

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;
  /**
   * The region.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _region;
  /**
   * The id of the price index.
   * @deprecated  the identifier should be in the ExternalIdBundle
   */
  @PropertyDefinition(validate = "notNull")
  @Deprecated
  private ExternalId _priceIndexId;

  /**
   * Creates an instance.
   */
  protected PriceIndexConvention() {
    super();
  }

  /**
   * Creates an instance.
   *
   * @param name  the convention name, not null
   * @param externalIdBundle  the external identifiers for this convention, not null
   * @param currency  the currency, not null
   * @param region  the region, not null
   * @param priceIndexId  the price time series id, not null
   */
  public PriceIndexConvention(
      final String name, final ExternalIdBundle externalIdBundle, final Currency currency,
      final ExternalId region, final ExternalId priceIndexId) {
    super(name, externalIdBundle);
    setCurrency(currency);
    setRegion(region);
    setPriceIndexId(priceIndexId);
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
    return visitor.visitPriceIndexConvention(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code PriceIndexConvention}.
   * @return the meta-bean, not null
   */
  public static PriceIndexConvention.Meta meta() {
    return PriceIndexConvention.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(PriceIndexConvention.Meta.INSTANCE);
  }

  @Override
  public PriceIndexConvention.Meta metaBean() {
    return PriceIndexConvention.Meta.INSTANCE;
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
   * Gets the region.
   * @return the value of the property, not null
   */
  public ExternalId getRegion() {
    return _region;
  }

  /**
   * Sets the region.
   * @param region  the new value of the property, not null
   */
  public void setRegion(ExternalId region) {
    JodaBeanUtils.notNull(region, "region");
    this._region = region;
  }

  /**
   * Gets the the {@code region} property.
   * @return the property, not null
   */
  public final Property<ExternalId> region() {
    return metaBean().region().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the id of the price index.
   * @deprecated  the identifier should be in the ExternalIdBundle
   * @return the value of the property, not null
   */
  @Deprecated
  public ExternalId getPriceIndexId() {
    return _priceIndexId;
  }

  /**
   * Sets the id of the price index.
   * @deprecated  the identifier should be in the ExternalIdBundle
   * @param priceIndexId  the new value of the property, not null
   */
  @Deprecated
  public void setPriceIndexId(ExternalId priceIndexId) {
    JodaBeanUtils.notNull(priceIndexId, "priceIndexId");
    this._priceIndexId = priceIndexId;
  }

  /**
   * Gets the the {@code priceIndexId} property.
   * @deprecated  the identifier should be in the ExternalIdBundle
   * @return the property, not null
   */
  @Deprecated
  public final Property<ExternalId> priceIndexId() {
    return metaBean().priceIndexId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public PriceIndexConvention clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      PriceIndexConvention other = (PriceIndexConvention) obj;
      return JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getRegion(), other.getRegion()) &&
          JodaBeanUtils.equal(getPriceIndexId(), other.getPriceIndexId()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegion());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPriceIndexId());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("PriceIndexConvention{");
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
    buf.append("region").append('=').append(JodaBeanUtils.toString(getRegion())).append(',').append(' ');
    buf.append("priceIndexId").append('=').append(JodaBeanUtils.toString(getPriceIndexId())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code PriceIndexConvention}.
   */
  public static class Meta extends FinancialConvention.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", PriceIndexConvention.class, Currency.class);
    /**
     * The meta-property for the {@code region} property.
     */
    private final MetaProperty<ExternalId> _region = DirectMetaProperty.ofReadWrite(
        this, "region", PriceIndexConvention.class, ExternalId.class);
    /**
     * The meta-property for the {@code priceIndexId} property.
     */
    private final MetaProperty<ExternalId> _priceIndexId = DirectMetaProperty.ofReadWrite(
        this, "priceIndexId", PriceIndexConvention.class, ExternalId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "currency",
        "region",
        "priceIndexId");

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
        case -934795532:  // region
          return _region;
        case 118085636:  // priceIndexId
          return _priceIndexId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends PriceIndexConvention> builder() {
      return new DirectBeanBuilder<PriceIndexConvention>(new PriceIndexConvention());
    }

    @Override
    public Class<? extends PriceIndexConvention> beanType() {
      return PriceIndexConvention.class;
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
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code region} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> region() {
      return _region;
    }

    /**
     * The meta-property for the {@code priceIndexId} property.
     * @deprecated  the identifier should be in the ExternalIdBundle
     * @return the meta-property, not null
     */
    @Deprecated
    public final MetaProperty<ExternalId> priceIndexId() {
      return _priceIndexId;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return ((PriceIndexConvention) bean).getCurrency();
        case -934795532:  // region
          return ((PriceIndexConvention) bean).getRegion();
        case 118085636:  // priceIndexId
          return ((PriceIndexConvention) bean).getPriceIndexId();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          ((PriceIndexConvention) bean).setCurrency((Currency) newValue);
          return;
        case -934795532:  // region
          ((PriceIndexConvention) bean).setRegion((ExternalId) newValue);
          return;
        case 118085636:  // priceIndexId
          ((PriceIndexConvention) bean).setPriceIndexId((ExternalId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((PriceIndexConvention) bean)._currency, "currency");
      JodaBeanUtils.notNull(((PriceIndexConvention) bean)._region, "region");
      JodaBeanUtils.notNull(((PriceIndexConvention) bean)._priceIndexId, "priceIndexId");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
