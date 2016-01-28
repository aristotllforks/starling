/**
 * Copyright (C) 2015 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.starling.client.marketdata;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutablePreBuild;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.id.ExternalIdBundle;

/**
 * Key to represent a particular piece of market data.
 */
@BeanDefinition
public final class MarketDataKey implements ImmutableBean {
  /**
   * The external id bundle for the market data.
   */
  @PropertyDefinition(validate = "notNull")
  private final ExternalIdBundle _externalIdBundle;
  /**
   * The data field.
   */
  @PropertyDefinition(validate = "notNull")
  private final DataField _field;
  /**
   * The data source.
   */
  @PropertyDefinition(validate = "notNull")
  private final DataSource _source;
  /**
   * The data provider.
   */
  @PropertyDefinition(validate = "notNull")
  private final DataProvider _provider;
  /**
   * The market data normalizer.
   */
  @PropertyDefinition(validate = "notNull")
  private final String _normalizer;

  // CHECKSTYLE:OFF
  @ImmutablePreBuild
  private static void preBuild(Builder builder) {
    if (builder.get(Meta.INSTANCE.field()) == null) {
      builder.field(DataField.PRICE);
    }
    if (builder.get(Meta.INSTANCE.source()) == null) {
      builder.source(DataSource.DEFAULT);
    }
    if (builder.get(Meta.INSTANCE.provider()) == null) {
      builder.provider(DataProvider.DEFAULT);
    }
    if (builder.get(Meta.INSTANCE.normalizer()) == null) {
      builder.normalizer(UnitNormalizer.INSTANCE.getName());
    }
  }

  /**
   * Static factory method for creating instances where you only want to provide the id bundle.
   * In this case, all the other properties will assume their default values.
   * @param idBundle  the bundle of ids associated with this market data
   * @return a MarketDataKey instance, not null
   */
  public static MarketDataKey of(final ExternalIdBundle idBundle) {
    return builder().externalIdBundle(idBundle).build();
  }

  /**
   * Static factory method for creating instances where you only want to provide the id bundle
   * and the data field.  All the other properties will assume their default values.
   * @param idBundle  the bundle of ids associated with this market data
   * @param field  the data field of this market data
   * @return a MarketDataKey instance, not null
   */
  public static MarketDataKey of(final ExternalIdBundle idBundle, final DataField field) {
    return builder().externalIdBundle(idBundle).field(field).build();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MarketDataKey}.
   * @return the meta-bean, not null
   */
  public static MarketDataKey.Meta meta() {
    return MarketDataKey.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MarketDataKey.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static MarketDataKey.Builder builder() {
    return new MarketDataKey.Builder();
  }

  private MarketDataKey(
      ExternalIdBundle externalIdBundle,
      DataField field,
      DataSource source,
      DataProvider provider,
      String normalizer) {
    JodaBeanUtils.notNull(externalIdBundle, "externalIdBundle");
    JodaBeanUtils.notNull(field, "field");
    JodaBeanUtils.notNull(source, "source");
    JodaBeanUtils.notNull(provider, "provider");
    JodaBeanUtils.notNull(normalizer, "normalizer");
    this._externalIdBundle = externalIdBundle;
    this._field = field;
    this._source = source;
    this._provider = provider;
    this._normalizer = normalizer;
  }

  @Override
  public MarketDataKey.Meta metaBean() {
    return MarketDataKey.Meta.INSTANCE;
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
   * Gets the external id bundle for the market data.
   * @return the value of the property, not null
   */
  public ExternalIdBundle getExternalIdBundle() {
    return _externalIdBundle;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data field.
   * @return the value of the property, not null
   */
  public DataField getField() {
    return _field;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data source.
   * @return the value of the property, not null
   */
  public DataSource getSource() {
    return _source;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data provider.
   * @return the value of the property, not null
   */
  public DataProvider getProvider() {
    return _provider;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the market data normalizer.
   * @return the value of the property, not null
   */
  public String getNormalizer() {
    return _normalizer;
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
      MarketDataKey other = (MarketDataKey) obj;
      return JodaBeanUtils.equal(getExternalIdBundle(), other.getExternalIdBundle()) &&
          JodaBeanUtils.equal(getField(), other.getField()) &&
          JodaBeanUtils.equal(getSource(), other.getSource()) &&
          JodaBeanUtils.equal(getProvider(), other.getProvider()) &&
          JodaBeanUtils.equal(getNormalizer(), other.getNormalizer());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getExternalIdBundle());
    hash = hash * 31 + JodaBeanUtils.hashCode(getField());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getProvider());
    hash = hash * 31 + JodaBeanUtils.hashCode(getNormalizer());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("MarketDataKey{");
    buf.append("externalIdBundle").append('=').append(getExternalIdBundle()).append(',').append(' ');
    buf.append("field").append('=').append(getField()).append(',').append(' ');
    buf.append("source").append('=').append(getSource()).append(',').append(' ');
    buf.append("provider").append('=').append(getProvider()).append(',').append(' ');
    buf.append("normalizer").append('=').append(JodaBeanUtils.toString(getNormalizer()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MarketDataKey}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code externalIdBundle} property.
     */
    private final MetaProperty<ExternalIdBundle> _externalIdBundle = DirectMetaProperty.ofImmutable(
        this, "externalIdBundle", MarketDataKey.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code field} property.
     */
    private final MetaProperty<DataField> _field = DirectMetaProperty.ofImmutable(
        this, "field", MarketDataKey.class, DataField.class);
    /**
     * The meta-property for the {@code source} property.
     */
    private final MetaProperty<DataSource> _source = DirectMetaProperty.ofImmutable(
        this, "source", MarketDataKey.class, DataSource.class);
    /**
     * The meta-property for the {@code provider} property.
     */
    private final MetaProperty<DataProvider> _provider = DirectMetaProperty.ofImmutable(
        this, "provider", MarketDataKey.class, DataProvider.class);
    /**
     * The meta-property for the {@code normalizer} property.
     */
    private final MetaProperty<String> _normalizer = DirectMetaProperty.ofImmutable(
        this, "normalizer", MarketDataKey.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "externalIdBundle",
        "field",
        "source",
        "provider",
        "normalizer");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -736922008:  // externalIdBundle
          return _externalIdBundle;
        case 97427706:  // field
          return _field;
        case -896505829:  // source
          return _source;
        case -987494927:  // provider
          return _provider;
        case -1255046395:  // normalizer
          return _normalizer;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public MarketDataKey.Builder builder() {
      return new MarketDataKey.Builder();
    }

    @Override
    public Class<? extends MarketDataKey> beanType() {
      return MarketDataKey.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code externalIdBundle} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ExternalIdBundle> externalIdBundle() {
      return _externalIdBundle;
    }

    /**
     * The meta-property for the {@code field} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DataField> field() {
      return _field;
    }

    /**
     * The meta-property for the {@code source} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DataSource> source() {
      return _source;
    }

    /**
     * The meta-property for the {@code provider} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DataProvider> provider() {
      return _provider;
    }

    /**
     * The meta-property for the {@code normalizer} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> normalizer() {
      return _normalizer;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -736922008:  // externalIdBundle
          return ((MarketDataKey) bean).getExternalIdBundle();
        case 97427706:  // field
          return ((MarketDataKey) bean).getField();
        case -896505829:  // source
          return ((MarketDataKey) bean).getSource();
        case -987494927:  // provider
          return ((MarketDataKey) bean).getProvider();
        case -1255046395:  // normalizer
          return ((MarketDataKey) bean).getNormalizer();
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
   * The bean-builder for {@code MarketDataKey}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<MarketDataKey> {

    private ExternalIdBundle _externalIdBundle;
    private DataField _field;
    private DataSource _source;
    private DataProvider _provider;
    private String _normalizer;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(MarketDataKey beanToCopy) {
      this._externalIdBundle = beanToCopy.getExternalIdBundle();
      this._field = beanToCopy.getField();
      this._source = beanToCopy.getSource();
      this._provider = beanToCopy.getProvider();
      this._normalizer = beanToCopy.getNormalizer();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -736922008:  // externalIdBundle
          return _externalIdBundle;
        case 97427706:  // field
          return _field;
        case -896505829:  // source
          return _source;
        case -987494927:  // provider
          return _provider;
        case -1255046395:  // normalizer
          return _normalizer;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -736922008:  // externalIdBundle
          this._externalIdBundle = (ExternalIdBundle) newValue;
          break;
        case 97427706:  // field
          this._field = (DataField) newValue;
          break;
        case -896505829:  // source
          this._source = (DataSource) newValue;
          break;
        case -987494927:  // provider
          this._provider = (DataProvider) newValue;
          break;
        case -1255046395:  // normalizer
          this._normalizer = (String) newValue;
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

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public MarketDataKey build() {
      preBuild(this);
      return new MarketDataKey(
          _externalIdBundle,
          _field,
          _source,
          _provider,
          _normalizer);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the external id bundle for the market data.
     * @param externalIdBundle  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder externalIdBundle(ExternalIdBundle externalIdBundle) {
      JodaBeanUtils.notNull(externalIdBundle, "externalIdBundle");
      this._externalIdBundle = externalIdBundle;
      return this;
    }

    /**
     * Sets the data field.
     * @param field  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder field(DataField field) {
      JodaBeanUtils.notNull(field, "field");
      this._field = field;
      return this;
    }

    /**
     * Sets the data source.
     * @param source  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder source(DataSource source) {
      JodaBeanUtils.notNull(source, "source");
      this._source = source;
      return this;
    }

    /**
     * Sets the data provider.
     * @param provider  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder provider(DataProvider provider) {
      JodaBeanUtils.notNull(provider, "provider");
      this._provider = provider;
      return this;
    }

    /**
     * Sets the market data normalizer.
     * @param normalizer  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder normalizer(String normalizer) {
      JodaBeanUtils.notNull(normalizer, "normalizer");
      this._normalizer = normalizer;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(192);
      buf.append("MarketDataKey.Builder{");
      buf.append("externalIdBundle").append('=').append(JodaBeanUtils.toString(_externalIdBundle)).append(',').append(' ');
      buf.append("field").append('=').append(JodaBeanUtils.toString(_field)).append(',').append(' ');
      buf.append("source").append('=').append(JodaBeanUtils.toString(_source)).append(',').append(' ');
      buf.append("provider").append('=').append(JodaBeanUtils.toString(_provider)).append(',').append(' ');
      buf.append("normalizer").append('=').append(JodaBeanUtils.toString(_normalizer));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
