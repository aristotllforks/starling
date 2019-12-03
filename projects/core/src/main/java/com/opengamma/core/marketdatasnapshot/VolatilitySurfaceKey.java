/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.marketdatasnapshot;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;
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

import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.util.ArgumentChecker;

/**
 * A key used to identify a volatility surface.
 * <p>
 * This class is immutable and thread-safe.
 */
@BeanDefinition
public final class VolatilitySurfaceKey implements ImmutableBean, StructuredMarketDataKey, Comparable<VolatilitySurfaceKey>, Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 3L;

  /**
   * The target.
   */
  @PropertyDefinition(validate = "notNull")
  private final UniqueId _target;
  /**
   * The surface name.
   */
  @PropertyDefinition
  private final String _name;
  /**
   * The instrument type.
   */
  @PropertyDefinition
  private final String _instrumentType;
  /**
   * The quote type.
   */
  @PropertyDefinition
  private final String _quoteType;
  /**
   * The quote units.
   */
  @PropertyDefinition
  private final String _quoteUnits;

  /**
   * Creates an instance.
   *
   * @param target  the target
   * @param name  the name
   * @param instrumentType  the instrument type
   * @param quoteType the quote type
   * @param quoteUnits the quote units
   * @return the volatility surface key, not null
   */
  public static VolatilitySurfaceKey of(final UniqueIdentifiable target, final String name, final String instrumentType, final String quoteType,
      final String quoteUnits) {
    ArgumentChecker.notNull(target, "target");
    return new VolatilitySurfaceKey(target.getUniqueId(), name, instrumentType, quoteType, quoteUnits);
  }

  //-------------------------------------------------------------------------
  /**
   * Compares this key to another, by currency then name.
   *
   * @param other  the other key, not null
   * @return the comparison value
   */
  @Override
  public int compareTo(final VolatilitySurfaceKey other) {
    if (other == null) {
      throw new NullPointerException();
    }
    int i = _target.compareTo(other.getTarget());
    if (i != 0) {
      return i;
    }
    i = ObjectUtils.compare(_name, other._name);
    if (i != 0) {
      return i;
    }
    i = ObjectUtils.compare(_instrumentType, other._instrumentType);
    if (i != 0) {
      return i;
    }
    i = ObjectUtils.compare(_quoteType, other._quoteType);
    if (i != 0) {
      return i;
    }
    return ObjectUtils.compare(_quoteUnits, other._quoteUnits);
  }

  @Override
  public <T> T accept(final Visitor<T> visitor) {
    return visitor.visitVolatilitySurfaceKey(this);
  }

  /**
   * Converts a key to a Fudge message.
   *
   * @param serializer  the serializer, not null
   * @return  a message
   * @deprecated  use the builder directly
   */
  @Deprecated
  public MutableFudgeMsg toFudgeMsg(final FudgeSerializer serializer) {
    return VolatilitySurfaceKeyFudgeBuilder.INSTANCE.buildMessage(serializer, this);
  }

  /**
   * Converts a Fudge message to a key.
   *
   * @param deserializer  the deserializer, not null
   * @param msg  the message, not null
   * @return  a key
   * @deprecated  use the builder directly
   */
  @Deprecated
  public static VolatilitySurfaceKey fromFudgeMsg(final FudgeDeserializer deserializer, final FudgeMsg msg) {
    return VolatilitySurfaceKeyFudgeBuilder.INSTANCE.buildObject(deserializer, msg);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code VolatilitySurfaceKey}.
   * @return the meta-bean, not null
   */
  public static VolatilitySurfaceKey.Meta meta() {
    return VolatilitySurfaceKey.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(VolatilitySurfaceKey.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static VolatilitySurfaceKey.Builder builder() {
    return new VolatilitySurfaceKey.Builder();
  }

  private VolatilitySurfaceKey(
      UniqueId target,
      String name,
      String instrumentType,
      String quoteType,
      String quoteUnits) {
    JodaBeanUtils.notNull(target, "target");
    this._target = target;
    this._name = name;
    this._instrumentType = instrumentType;
    this._quoteType = quoteType;
    this._quoteUnits = quoteUnits;
  }

  @Override
  public VolatilitySurfaceKey.Meta metaBean() {
    return VolatilitySurfaceKey.Meta.INSTANCE;
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
   * Gets the target.
   * @return the value of the property, not null
   */
  public UniqueId getTarget() {
    return _target;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the surface name.
   * @return the value of the property
   */
  public String getName() {
    return _name;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instrument type.
   * @return the value of the property
   */
  public String getInstrumentType() {
    return _instrumentType;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the quote type.
   * @return the value of the property
   */
  public String getQuoteType() {
    return _quoteType;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the quote units.
   * @return the value of the property
   */
  public String getQuoteUnits() {
    return _quoteUnits;
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
      VolatilitySurfaceKey other = (VolatilitySurfaceKey) obj;
      return JodaBeanUtils.equal(_target, other._target) &&
          JodaBeanUtils.equal(_name, other._name) &&
          JodaBeanUtils.equal(_instrumentType, other._instrumentType) &&
          JodaBeanUtils.equal(_quoteType, other._quoteType) &&
          JodaBeanUtils.equal(_quoteUnits, other._quoteUnits);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_target);
    hash = hash * 31 + JodaBeanUtils.hashCode(_name);
    hash = hash * 31 + JodaBeanUtils.hashCode(_instrumentType);
    hash = hash * 31 + JodaBeanUtils.hashCode(_quoteType);
    hash = hash * 31 + JodaBeanUtils.hashCode(_quoteUnits);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("VolatilitySurfaceKey{");
    buf.append("target").append('=').append(_target).append(',').append(' ');
    buf.append("name").append('=').append(_name).append(',').append(' ');
    buf.append("instrumentType").append('=').append(_instrumentType).append(',').append(' ');
    buf.append("quoteType").append('=').append(_quoteType).append(',').append(' ');
    buf.append("quoteUnits").append('=').append(JodaBeanUtils.toString(_quoteUnits));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code VolatilitySurfaceKey}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code target} property.
     */
    private final MetaProperty<UniqueId> _target = DirectMetaProperty.ofImmutable(
        this, "target", VolatilitySurfaceKey.class, UniqueId.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofImmutable(
        this, "name", VolatilitySurfaceKey.class, String.class);
    /**
     * The meta-property for the {@code instrumentType} property.
     */
    private final MetaProperty<String> _instrumentType = DirectMetaProperty.ofImmutable(
        this, "instrumentType", VolatilitySurfaceKey.class, String.class);
    /**
     * The meta-property for the {@code quoteType} property.
     */
    private final MetaProperty<String> _quoteType = DirectMetaProperty.ofImmutable(
        this, "quoteType", VolatilitySurfaceKey.class, String.class);
    /**
     * The meta-property for the {@code quoteUnits} property.
     */
    private final MetaProperty<String> _quoteUnits = DirectMetaProperty.ofImmutable(
        this, "quoteUnits", VolatilitySurfaceKey.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "target",
        "name",
        "instrumentType",
        "quoteType",
        "quoteUnits");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -880905839:  // target
          return _target;
        case 3373707:  // name
          return _name;
        case 1956846529:  // instrumentType
          return _instrumentType;
        case -1482972202:  // quoteType
          return _quoteType;
        case 1273091667:  // quoteUnits
          return _quoteUnits;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public VolatilitySurfaceKey.Builder builder() {
      return new VolatilitySurfaceKey.Builder();
    }

    @Override
    public Class<? extends VolatilitySurfaceKey> beanType() {
      return VolatilitySurfaceKey.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code target} property.
     * @return the meta-property, not null
     */
    public MetaProperty<UniqueId> target() {
      return _target;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> name() {
      return _name;
    }

    /**
     * The meta-property for the {@code instrumentType} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> instrumentType() {
      return _instrumentType;
    }

    /**
     * The meta-property for the {@code quoteType} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> quoteType() {
      return _quoteType;
    }

    /**
     * The meta-property for the {@code quoteUnits} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> quoteUnits() {
      return _quoteUnits;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -880905839:  // target
          return ((VolatilitySurfaceKey) bean).getTarget();
        case 3373707:  // name
          return ((VolatilitySurfaceKey) bean).getName();
        case 1956846529:  // instrumentType
          return ((VolatilitySurfaceKey) bean).getInstrumentType();
        case -1482972202:  // quoteType
          return ((VolatilitySurfaceKey) bean).getQuoteType();
        case 1273091667:  // quoteUnits
          return ((VolatilitySurfaceKey) bean).getQuoteUnits();
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
   * The bean-builder for {@code VolatilitySurfaceKey}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<VolatilitySurfaceKey> {

    private UniqueId _target;
    private String _name;
    private String _instrumentType;
    private String _quoteType;
    private String _quoteUnits;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(VolatilitySurfaceKey beanToCopy) {
      this._target = beanToCopy.getTarget();
      this._name = beanToCopy.getName();
      this._instrumentType = beanToCopy.getInstrumentType();
      this._quoteType = beanToCopy.getQuoteType();
      this._quoteUnits = beanToCopy.getQuoteUnits();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -880905839:  // target
          return _target;
        case 3373707:  // name
          return _name;
        case 1956846529:  // instrumentType
          return _instrumentType;
        case -1482972202:  // quoteType
          return _quoteType;
        case 1273091667:  // quoteUnits
          return _quoteUnits;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -880905839:  // target
          this._target = (UniqueId) newValue;
          break;
        case 3373707:  // name
          this._name = (String) newValue;
          break;
        case 1956846529:  // instrumentType
          this._instrumentType = (String) newValue;
          break;
        case -1482972202:  // quoteType
          this._quoteType = (String) newValue;
          break;
        case 1273091667:  // quoteUnits
          this._quoteUnits = (String) newValue;
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
    public VolatilitySurfaceKey build() {
      return new VolatilitySurfaceKey(
          _target,
          _name,
          _instrumentType,
          _quoteType,
          _quoteUnits);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the target.
     * @param target  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder target(UniqueId target) {
      JodaBeanUtils.notNull(target, "target");
      this._target = target;
      return this;
    }

    /**
     * Sets the surface name.
     * @param name  the new value
     * @return this, for chaining, not null
     */
    public Builder name(String name) {
      this._name = name;
      return this;
    }

    /**
     * Sets the instrument type.
     * @param instrumentType  the new value
     * @return this, for chaining, not null
     */
    public Builder instrumentType(String instrumentType) {
      this._instrumentType = instrumentType;
      return this;
    }

    /**
     * Sets the quote type.
     * @param quoteType  the new value
     * @return this, for chaining, not null
     */
    public Builder quoteType(String quoteType) {
      this._quoteType = quoteType;
      return this;
    }

    /**
     * Sets the quote units.
     * @param quoteUnits  the new value
     * @return this, for chaining, not null
     */
    public Builder quoteUnits(String quoteUnits) {
      this._quoteUnits = quoteUnits;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(192);
      buf.append("VolatilitySurfaceKey.Builder{");
      buf.append("target").append('=').append(JodaBeanUtils.toString(_target)).append(',').append(' ');
      buf.append("name").append('=').append(JodaBeanUtils.toString(_name)).append(',').append(' ');
      buf.append("instrumentType").append('=').append(JodaBeanUtils.toString(_instrumentType)).append(',').append(' ');
      buf.append("quoteType").append('=').append(JodaBeanUtils.toString(_quoteType)).append(',').append(' ');
      buf.append("quoteUnits").append('=').append(JodaBeanUtils.toString(_quoteUnits));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
