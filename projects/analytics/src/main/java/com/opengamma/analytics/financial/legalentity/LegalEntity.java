/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.legalentity;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * Class containing fields that define a legal entity. The only that field that must be set is the name.
 */
@BeanDefinition
public class LegalEntity implements Bean, Serializable {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The ticker.
   */
  @PropertyDefinition
  private String _ticker;

  /**
   * The short name.
   */
  @PropertyDefinition(validate = "notNull")
  private String _shortName;

  /**
   * A set of credit ratings from various agencies.
   */
  @PropertyDefinition
  private Set<CreditRating> _creditRatings;

  /**
   * The sector.
   */
  @PropertyDefinition
  private Sector _sector;

  /**
   * The region.
   */
  @PropertyDefinition
  private Region _region;
  /**
   * Has defaulted flag.
   */
  @PropertyDefinition
  private boolean _hasDefaulted;

  /**
   * For the builder.
   */
  /* package */ LegalEntity() {
  }

  /**
   * @param ticker
   *          The ticker, not null
   * @param shortName
   *          The short name, not null
   * @param creditRatings
   *          The set of credit ratings, not null
   * @param sector
   *          The sector, not null
   * @param region
   *          The region, not null
   * @param hasDefaulted
   *          The has defaulted flag, not null
   */
  public LegalEntity(final String ticker, final String shortName, final Set<CreditRating> creditRatings, final Sector sector,
      final Region region, final boolean hasDefaulted) {
    setTicker(ticker);
    setShortName(shortName);
    setCreditRatings(creditRatings);
    setSector(sector);
    setRegion(region);
    setHasDefaulted(hasDefaulted);
  }

  /**
   * @param ticker
   *          The ticker, not null
   * @param shortName
   *          The short name, not null
   * @param creditRatings
   *          The set of credit ratings, not null
   * @param sector
   *          The sector, not null
   * @param region
   *          The region, not null
   */
  public LegalEntity(final String ticker, final String shortName, final Set<CreditRating> creditRatings, final Sector sector,
      final Region region) {
    setTicker(ticker);
    setShortName(shortName);
    setCreditRatings(creditRatings);
    setSector(sector);
    setRegion(region);
    setHasDefaulted(false);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code LegalEntity}.
   * @return the meta-bean, not null
   */
  public static LegalEntity.Meta meta() {
    return LegalEntity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(LegalEntity.Meta.INSTANCE);
  }

  @Override
  public LegalEntity.Meta metaBean() {
    return LegalEntity.Meta.INSTANCE;
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
   * Gets the ticker.
   * @return the value of the property
   */
  public String getTicker() {
    return _ticker;
  }

  /**
   * Sets the ticker.
   * @param ticker  the new value of the property
   */
  public void setTicker(String ticker) {
    this._ticker = ticker;
  }

  /**
   * Gets the the {@code ticker} property.
   * @return the property, not null
   */
  public final Property<String> ticker() {
    return metaBean().ticker().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the short name.
   * @return the value of the property, not null
   */
  public String getShortName() {
    return _shortName;
  }

  /**
   * Sets the short name.
   * @param shortName  the new value of the property, not null
   */
  public void setShortName(String shortName) {
    JodaBeanUtils.notNull(shortName, "shortName");
    this._shortName = shortName;
  }

  /**
   * Gets the the {@code shortName} property.
   * @return the property, not null
   */
  public final Property<String> shortName() {
    return metaBean().shortName().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets a set of credit ratings from various agencies.
   * @return the value of the property
   */
  public Set<CreditRating> getCreditRatings() {
    return _creditRatings;
  }

  /**
   * Sets a set of credit ratings from various agencies.
   * @param creditRatings  the new value of the property
   */
  public void setCreditRatings(Set<CreditRating> creditRatings) {
    this._creditRatings = creditRatings;
  }

  /**
   * Gets the the {@code creditRatings} property.
   * @return the property, not null
   */
  public final Property<Set<CreditRating>> creditRatings() {
    return metaBean().creditRatings().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the sector.
   * @return the value of the property
   */
  public Sector getSector() {
    return _sector;
  }

  /**
   * Sets the sector.
   * @param sector  the new value of the property
   */
  public void setSector(Sector sector) {
    this._sector = sector;
  }

  /**
   * Gets the the {@code sector} property.
   * @return the property, not null
   */
  public final Property<Sector> sector() {
    return metaBean().sector().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the region.
   * @return the value of the property
   */
  public Region getRegion() {
    return _region;
  }

  /**
   * Sets the region.
   * @param region  the new value of the property
   */
  public void setRegion(Region region) {
    this._region = region;
  }

  /**
   * Gets the the {@code region} property.
   * @return the property, not null
   */
  public final Property<Region> region() {
    return metaBean().region().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets has defaulted flag.
   * @return the value of the property
   */
  public boolean isHasDefaulted() {
    return _hasDefaulted;
  }

  /**
   * Sets has defaulted flag.
   * @param hasDefaulted  the new value of the property
   */
  public void setHasDefaulted(boolean hasDefaulted) {
    this._hasDefaulted = hasDefaulted;
  }

  /**
   * Gets the the {@code hasDefaulted} property.
   * @return the property, not null
   */
  public final Property<Boolean> hasDefaulted() {
    return metaBean().hasDefaulted().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public LegalEntity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      LegalEntity other = (LegalEntity) obj;
      return JodaBeanUtils.equal(getTicker(), other.getTicker()) &&
          JodaBeanUtils.equal(getShortName(), other.getShortName()) &&
          JodaBeanUtils.equal(getCreditRatings(), other.getCreditRatings()) &&
          JodaBeanUtils.equal(getSector(), other.getSector()) &&
          JodaBeanUtils.equal(getRegion(), other.getRegion()) &&
          (isHasDefaulted() == other.isHasDefaulted());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getTicker());
    hash = hash * 31 + JodaBeanUtils.hashCode(getShortName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCreditRatings());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSector());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegion());
    hash = hash * 31 + JodaBeanUtils.hashCode(isHasDefaulted());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(224);
    buf.append("LegalEntity{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("ticker").append('=').append(JodaBeanUtils.toString(getTicker())).append(',').append(' ');
    buf.append("shortName").append('=').append(JodaBeanUtils.toString(getShortName())).append(',').append(' ');
    buf.append("creditRatings").append('=').append(JodaBeanUtils.toString(getCreditRatings())).append(',').append(' ');
    buf.append("sector").append('=').append(JodaBeanUtils.toString(getSector())).append(',').append(' ');
    buf.append("region").append('=').append(JodaBeanUtils.toString(getRegion())).append(',').append(' ');
    buf.append("hasDefaulted").append('=').append(JodaBeanUtils.toString(isHasDefaulted())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code LegalEntity}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code ticker} property.
     */
    private final MetaProperty<String> _ticker = DirectMetaProperty.ofReadWrite(
        this, "ticker", LegalEntity.class, String.class);
    /**
     * The meta-property for the {@code shortName} property.
     */
    private final MetaProperty<String> _shortName = DirectMetaProperty.ofReadWrite(
        this, "shortName", LegalEntity.class, String.class);
    /**
     * The meta-property for the {@code creditRatings} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<CreditRating>> _creditRatings = DirectMetaProperty.ofReadWrite(
        this, "creditRatings", LegalEntity.class, (Class) Set.class);
    /**
     * The meta-property for the {@code sector} property.
     */
    private final MetaProperty<Sector> _sector = DirectMetaProperty.ofReadWrite(
        this, "sector", LegalEntity.class, Sector.class);
    /**
     * The meta-property for the {@code region} property.
     */
    private final MetaProperty<Region> _region = DirectMetaProperty.ofReadWrite(
        this, "region", LegalEntity.class, Region.class);
    /**
     * The meta-property for the {@code hasDefaulted} property.
     */
    private final MetaProperty<Boolean> _hasDefaulted = DirectMetaProperty.ofReadWrite(
        this, "hasDefaulted", LegalEntity.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "ticker",
        "shortName",
        "creditRatings",
        "sector",
        "region",
        "hasDefaulted");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -873960694:  // ticker
          return _ticker;
        case -2028219097:  // shortName
          return _shortName;
        case 1420088637:  // creditRatings
          return _creditRatings;
        case -906274970:  // sector
          return _sector;
        case -934795532:  // region
          return _region;
        case 1706701094:  // hasDefaulted
          return _hasDefaulted;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends LegalEntity> builder() {
      return new DirectBeanBuilder<LegalEntity>(new LegalEntity());
    }

    @Override
    public Class<? extends LegalEntity> beanType() {
      return LegalEntity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code ticker} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> ticker() {
      return _ticker;
    }

    /**
     * The meta-property for the {@code shortName} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> shortName() {
      return _shortName;
    }

    /**
     * The meta-property for the {@code creditRatings} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Set<CreditRating>> creditRatings() {
      return _creditRatings;
    }

    /**
     * The meta-property for the {@code sector} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Sector> sector() {
      return _sector;
    }

    /**
     * The meta-property for the {@code region} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Region> region() {
      return _region;
    }

    /**
     * The meta-property for the {@code hasDefaulted} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> hasDefaulted() {
      return _hasDefaulted;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -873960694:  // ticker
          return ((LegalEntity) bean).getTicker();
        case -2028219097:  // shortName
          return ((LegalEntity) bean).getShortName();
        case 1420088637:  // creditRatings
          return ((LegalEntity) bean).getCreditRatings();
        case -906274970:  // sector
          return ((LegalEntity) bean).getSector();
        case -934795532:  // region
          return ((LegalEntity) bean).getRegion();
        case 1706701094:  // hasDefaulted
          return ((LegalEntity) bean).isHasDefaulted();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -873960694:  // ticker
          ((LegalEntity) bean).setTicker((String) newValue);
          return;
        case -2028219097:  // shortName
          ((LegalEntity) bean).setShortName((String) newValue);
          return;
        case 1420088637:  // creditRatings
          ((LegalEntity) bean).setCreditRatings((Set<CreditRating>) newValue);
          return;
        case -906274970:  // sector
          ((LegalEntity) bean).setSector((Sector) newValue);
          return;
        case -934795532:  // region
          ((LegalEntity) bean).setRegion((Region) newValue);
          return;
        case 1706701094:  // hasDefaulted
          ((LegalEntity) bean).setHasDefaulted((Boolean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((LegalEntity) bean)._shortName, "shortName");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
