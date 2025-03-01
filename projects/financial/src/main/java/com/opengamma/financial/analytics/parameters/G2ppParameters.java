/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.parameters;

import java.io.Serializable;
import java.util.Map;
import java.util.SortedMap;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.config.Config;
import com.opengamma.core.config.ConfigGroups;
import com.opengamma.id.ExternalId;
import com.opengamma.id.MutableUniqueIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Tenor;
import com.opengamma.util.tuple.Pair;

/**
 * Config object that contains parameters for the G2++ model.
 */
@Config(description = "G2++ model parameters", group = ConfigGroups.MISC)
@BeanDefinition
public class G2ppParameters extends DirectBean implements Serializable, UniqueIdentifiable, MutableUniqueIdentifiable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The unique id of this configuration.
   */
  @PropertyDefinition
  private UniqueId _uniqueId;

  /**
   * The currency for which these parameters apply.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;

  /**
   * The external id for the first mean reversion parameter.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _firstMeanReversionId;

  /**
   * The external id for the second mean reversion parameter.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _secondMeanReversionId;

  /**
   * The first initial volatility id.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _firstInitialVolatilityId;

  /**
   * The second initial volatility id.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _secondInitialVolatilityId;

  /**
   * A map of tenors to pairs of external ids that represent the volatility term
   * structure for both parameters.
   */
  @PropertyDefinition(validate = "notNull")
  private SortedMap<Tenor, Pair<ExternalId, ExternalId>> _volatilityTermStructure;

  /**
   * The correlation id.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _correlationId;

  /**
   * For the builder.
   */
  /* package */G2ppParameters() {
    super();
  }

  /**
   * @param currency The currency, not null
   * @param firstMeanReversionId The first mean reversion identifier, not null
   * @param secondMeanReversionId The second mean reversion identifier, not null
   * @param firstInitialVolatilityId The first initial volatility identifier, not null
   * @param secondInitialVolatilityId The second initial volatility identifier, not null
   * @param volatilityTermStructure The (tenor, (identifier, identifier)) map that represents
   * the term structure, not null
   * @param correlationId The correlation identifier, not null
   */
  public G2ppParameters(final Currency currency, final ExternalId firstMeanReversionId, final ExternalId secondMeanReversionId,
      final ExternalId firstInitialVolatilityId, final ExternalId secondInitialVolatilityId,
      final SortedMap<Tenor, Pair<ExternalId, ExternalId>> volatilityTermStructure, final ExternalId correlationId) {
    super();
    setCurrency(currency);
    setFirstMeanReversionId(firstMeanReversionId);
    setSecondMeanReversionId(secondMeanReversionId);
    setFirstInitialVolatilityId(firstInitialVolatilityId);
    setSecondInitialVolatilityId(secondInitialVolatilityId);
    setVolatilityTermStructure(volatilityTermStructure);
    setCorrelationId(correlationId);
  }
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code G2ppParameters}.
   * @return the meta-bean, not null
   */
  public static G2ppParameters.Meta meta() {
    return G2ppParameters.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(G2ppParameters.Meta.INSTANCE);
  }

  @Override
  public G2ppParameters.Meta metaBean() {
    return G2ppParameters.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unique id of this configuration.
   * @return the value of the property
   */
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the unique id of this configuration.
   * @param uniqueId  the new value of the property
   */
  public void setUniqueId(UniqueId uniqueId) {
    this._uniqueId = uniqueId;
  }

  /**
   * Gets the the {@code uniqueId} property.
   * @return the property, not null
   */
  public final Property<UniqueId> uniqueId() {
    return metaBean().uniqueId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency for which these parameters apply.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency for which these parameters apply.
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
   * Gets the external id for the first mean reversion parameter.
   * @return the value of the property, not null
   */
  public ExternalId getFirstMeanReversionId() {
    return _firstMeanReversionId;
  }

  /**
   * Sets the external id for the first mean reversion parameter.
   * @param firstMeanReversionId  the new value of the property, not null
   */
  public void setFirstMeanReversionId(ExternalId firstMeanReversionId) {
    JodaBeanUtils.notNull(firstMeanReversionId, "firstMeanReversionId");
    this._firstMeanReversionId = firstMeanReversionId;
  }

  /**
   * Gets the the {@code firstMeanReversionId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> firstMeanReversionId() {
    return metaBean().firstMeanReversionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the external id for the second mean reversion parameter.
   * @return the value of the property, not null
   */
  public ExternalId getSecondMeanReversionId() {
    return _secondMeanReversionId;
  }

  /**
   * Sets the external id for the second mean reversion parameter.
   * @param secondMeanReversionId  the new value of the property, not null
   */
  public void setSecondMeanReversionId(ExternalId secondMeanReversionId) {
    JodaBeanUtils.notNull(secondMeanReversionId, "secondMeanReversionId");
    this._secondMeanReversionId = secondMeanReversionId;
  }

  /**
   * Gets the the {@code secondMeanReversionId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> secondMeanReversionId() {
    return metaBean().secondMeanReversionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the first initial volatility id.
   * @return the value of the property, not null
   */
  public ExternalId getFirstInitialVolatilityId() {
    return _firstInitialVolatilityId;
  }

  /**
   * Sets the first initial volatility id.
   * @param firstInitialVolatilityId  the new value of the property, not null
   */
  public void setFirstInitialVolatilityId(ExternalId firstInitialVolatilityId) {
    JodaBeanUtils.notNull(firstInitialVolatilityId, "firstInitialVolatilityId");
    this._firstInitialVolatilityId = firstInitialVolatilityId;
  }

  /**
   * Gets the the {@code firstInitialVolatilityId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> firstInitialVolatilityId() {
    return metaBean().firstInitialVolatilityId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the second initial volatility id.
   * @return the value of the property, not null
   */
  public ExternalId getSecondInitialVolatilityId() {
    return _secondInitialVolatilityId;
  }

  /**
   * Sets the second initial volatility id.
   * @param secondInitialVolatilityId  the new value of the property, not null
   */
  public void setSecondInitialVolatilityId(ExternalId secondInitialVolatilityId) {
    JodaBeanUtils.notNull(secondInitialVolatilityId, "secondInitialVolatilityId");
    this._secondInitialVolatilityId = secondInitialVolatilityId;
  }

  /**
   * Gets the the {@code secondInitialVolatilityId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> secondInitialVolatilityId() {
    return metaBean().secondInitialVolatilityId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets a map of tenors to pairs of external ids that represent the volatility term
   * structure for both parameters.
   * @return the value of the property, not null
   */
  public SortedMap<Tenor, Pair<ExternalId, ExternalId>> getVolatilityTermStructure() {
    return _volatilityTermStructure;
  }

  /**
   * Sets a map of tenors to pairs of external ids that represent the volatility term
   * structure for both parameters.
   * @param volatilityTermStructure  the new value of the property, not null
   */
  public void setVolatilityTermStructure(SortedMap<Tenor, Pair<ExternalId, ExternalId>> volatilityTermStructure) {
    JodaBeanUtils.notNull(volatilityTermStructure, "volatilityTermStructure");
    this._volatilityTermStructure = volatilityTermStructure;
  }

  /**
   * Gets the the {@code volatilityTermStructure} property.
   * structure for both parameters.
   * @return the property, not null
   */
  public final Property<SortedMap<Tenor, Pair<ExternalId, ExternalId>>> volatilityTermStructure() {
    return metaBean().volatilityTermStructure().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the correlation id.
   * @return the value of the property, not null
   */
  public ExternalId getCorrelationId() {
    return _correlationId;
  }

  /**
   * Sets the correlation id.
   * @param correlationId  the new value of the property, not null
   */
  public void setCorrelationId(ExternalId correlationId) {
    JodaBeanUtils.notNull(correlationId, "correlationId");
    this._correlationId = correlationId;
  }

  /**
   * Gets the the {@code correlationId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> correlationId() {
    return metaBean().correlationId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public G2ppParameters clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      G2ppParameters other = (G2ppParameters) obj;
      return JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getFirstMeanReversionId(), other.getFirstMeanReversionId()) &&
          JodaBeanUtils.equal(getSecondMeanReversionId(), other.getSecondMeanReversionId()) &&
          JodaBeanUtils.equal(getFirstInitialVolatilityId(), other.getFirstInitialVolatilityId()) &&
          JodaBeanUtils.equal(getSecondInitialVolatilityId(), other.getSecondInitialVolatilityId()) &&
          JodaBeanUtils.equal(getVolatilityTermStructure(), other.getVolatilityTermStructure()) &&
          JodaBeanUtils.equal(getCorrelationId(), other.getCorrelationId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFirstMeanReversionId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecondMeanReversionId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFirstInitialVolatilityId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecondInitialVolatilityId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getVolatilityTermStructure());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCorrelationId());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(288);
    buf.append("G2ppParameters{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("uniqueId").append('=').append(JodaBeanUtils.toString(getUniqueId())).append(',').append(' ');
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency())).append(',').append(' ');
    buf.append("firstMeanReversionId").append('=').append(JodaBeanUtils.toString(getFirstMeanReversionId())).append(',').append(' ');
    buf.append("secondMeanReversionId").append('=').append(JodaBeanUtils.toString(getSecondMeanReversionId())).append(',').append(' ');
    buf.append("firstInitialVolatilityId").append('=').append(JodaBeanUtils.toString(getFirstInitialVolatilityId())).append(',').append(' ');
    buf.append("secondInitialVolatilityId").append('=').append(JodaBeanUtils.toString(getSecondInitialVolatilityId())).append(',').append(' ');
    buf.append("volatilityTermStructure").append('=').append(JodaBeanUtils.toString(getVolatilityTermStructure())).append(',').append(' ');
    buf.append("correlationId").append('=').append(JodaBeanUtils.toString(getCorrelationId())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code G2ppParameters}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueId> _uniqueId = DirectMetaProperty.ofReadWrite(
        this, "uniqueId", G2ppParameters.class, UniqueId.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", G2ppParameters.class, Currency.class);
    /**
     * The meta-property for the {@code firstMeanReversionId} property.
     */
    private final MetaProperty<ExternalId> _firstMeanReversionId = DirectMetaProperty.ofReadWrite(
        this, "firstMeanReversionId", G2ppParameters.class, ExternalId.class);
    /**
     * The meta-property for the {@code secondMeanReversionId} property.
     */
    private final MetaProperty<ExternalId> _secondMeanReversionId = DirectMetaProperty.ofReadWrite(
        this, "secondMeanReversionId", G2ppParameters.class, ExternalId.class);
    /**
     * The meta-property for the {@code firstInitialVolatilityId} property.
     */
    private final MetaProperty<ExternalId> _firstInitialVolatilityId = DirectMetaProperty.ofReadWrite(
        this, "firstInitialVolatilityId", G2ppParameters.class, ExternalId.class);
    /**
     * The meta-property for the {@code secondInitialVolatilityId} property.
     */
    private final MetaProperty<ExternalId> _secondInitialVolatilityId = DirectMetaProperty.ofReadWrite(
        this, "secondInitialVolatilityId", G2ppParameters.class, ExternalId.class);
    /**
     * The meta-property for the {@code volatilityTermStructure} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<SortedMap<Tenor, Pair<ExternalId, ExternalId>>> _volatilityTermStructure = DirectMetaProperty.ofReadWrite(
        this, "volatilityTermStructure", G2ppParameters.class, (Class) SortedMap.class);
    /**
     * The meta-property for the {@code correlationId} property.
     */
    private final MetaProperty<ExternalId> _correlationId = DirectMetaProperty.ofReadWrite(
        this, "correlationId", G2ppParameters.class, ExternalId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "uniqueId",
        "currency",
        "firstMeanReversionId",
        "secondMeanReversionId",
        "firstInitialVolatilityId",
        "secondInitialVolatilityId",
        "volatilityTermStructure",
        "correlationId");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return _uniqueId;
        case 575402001:  // currency
          return _currency;
        case 814854507:  // firstMeanReversionId
          return _firstMeanReversionId;
        case 629341991:  // secondMeanReversionId
          return _secondMeanReversionId;
        case -1201097548:  // firstInitialVolatilityId
          return _firstInitialVolatilityId;
        case 340051056:  // secondInitialVolatilityId
          return _secondInitialVolatilityId;
        case -1883573694:  // volatilityTermStructure
          return _volatilityTermStructure;
        case -764983747:  // correlationId
          return _correlationId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends G2ppParameters> builder() {
      return new DirectBeanBuilder<G2ppParameters>(new G2ppParameters());
    }

    @Override
    public Class<? extends G2ppParameters> beanType() {
      return G2ppParameters.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code uniqueId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueId> uniqueId() {
      return _uniqueId;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code firstMeanReversionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> firstMeanReversionId() {
      return _firstMeanReversionId;
    }

    /**
     * The meta-property for the {@code secondMeanReversionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> secondMeanReversionId() {
      return _secondMeanReversionId;
    }

    /**
     * The meta-property for the {@code firstInitialVolatilityId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> firstInitialVolatilityId() {
      return _firstInitialVolatilityId;
    }

    /**
     * The meta-property for the {@code secondInitialVolatilityId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> secondInitialVolatilityId() {
      return _secondInitialVolatilityId;
    }

    /**
     * The meta-property for the {@code volatilityTermStructure} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SortedMap<Tenor, Pair<ExternalId, ExternalId>>> volatilityTermStructure() {
      return _volatilityTermStructure;
    }

    /**
     * The meta-property for the {@code correlationId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> correlationId() {
      return _correlationId;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return ((G2ppParameters) bean).getUniqueId();
        case 575402001:  // currency
          return ((G2ppParameters) bean).getCurrency();
        case 814854507:  // firstMeanReversionId
          return ((G2ppParameters) bean).getFirstMeanReversionId();
        case 629341991:  // secondMeanReversionId
          return ((G2ppParameters) bean).getSecondMeanReversionId();
        case -1201097548:  // firstInitialVolatilityId
          return ((G2ppParameters) bean).getFirstInitialVolatilityId();
        case 340051056:  // secondInitialVolatilityId
          return ((G2ppParameters) bean).getSecondInitialVolatilityId();
        case -1883573694:  // volatilityTermStructure
          return ((G2ppParameters) bean).getVolatilityTermStructure();
        case -764983747:  // correlationId
          return ((G2ppParameters) bean).getCorrelationId();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          ((G2ppParameters) bean).setUniqueId((UniqueId) newValue);
          return;
        case 575402001:  // currency
          ((G2ppParameters) bean).setCurrency((Currency) newValue);
          return;
        case 814854507:  // firstMeanReversionId
          ((G2ppParameters) bean).setFirstMeanReversionId((ExternalId) newValue);
          return;
        case 629341991:  // secondMeanReversionId
          ((G2ppParameters) bean).setSecondMeanReversionId((ExternalId) newValue);
          return;
        case -1201097548:  // firstInitialVolatilityId
          ((G2ppParameters) bean).setFirstInitialVolatilityId((ExternalId) newValue);
          return;
        case 340051056:  // secondInitialVolatilityId
          ((G2ppParameters) bean).setSecondInitialVolatilityId((ExternalId) newValue);
          return;
        case -1883573694:  // volatilityTermStructure
          ((G2ppParameters) bean).setVolatilityTermStructure((SortedMap<Tenor, Pair<ExternalId, ExternalId>>) newValue);
          return;
        case -764983747:  // correlationId
          ((G2ppParameters) bean).setCorrelationId((ExternalId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((G2ppParameters) bean)._currency, "currency");
      JodaBeanUtils.notNull(((G2ppParameters) bean)._firstMeanReversionId, "firstMeanReversionId");
      JodaBeanUtils.notNull(((G2ppParameters) bean)._secondMeanReversionId, "secondMeanReversionId");
      JodaBeanUtils.notNull(((G2ppParameters) bean)._firstInitialVolatilityId, "firstInitialVolatilityId");
      JodaBeanUtils.notNull(((G2ppParameters) bean)._secondInitialVolatilityId, "secondInitialVolatilityId");
      JodaBeanUtils.notNull(((G2ppParameters) bean)._volatilityTermStructure, "volatilityTermStructure");
      JodaBeanUtils.notNull(((G2ppParameters) bean)._correlationId, "correlationId");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
