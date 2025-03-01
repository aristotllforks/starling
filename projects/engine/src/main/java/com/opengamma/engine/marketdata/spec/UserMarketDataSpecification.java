/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.marketdata.spec;

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

import com.opengamma.id.UniqueId;

/**
 *
 */
@BeanDefinition
public final class UserMarketDataSpecification implements ImmutableBean, MarketDataSpecification {

  private static final long serialVersionUID = 1L;

  @PropertyDefinition(validate = "notNull")
  private final UniqueId _userSnapshotId;

  /**
   * Creates an instance.
   *
   * @param userSnapshotId
   *          the user snapshot id, not-null
   * @return the user market data specification
   */
  public static UserMarketDataSpecification of(final UniqueId userSnapshotId) {
    return new UserMarketDataSpecification(userSnapshotId);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code UserMarketDataSpecification}.
   * @return the meta-bean, not null
   */
  public static UserMarketDataSpecification.Meta meta() {
    return UserMarketDataSpecification.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(UserMarketDataSpecification.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static UserMarketDataSpecification.Builder builder() {
    return new UserMarketDataSpecification.Builder();
  }

  private UserMarketDataSpecification(
      UniqueId userSnapshotId) {
    JodaBeanUtils.notNull(userSnapshotId, "userSnapshotId");
    this._userSnapshotId = userSnapshotId;
  }

  @Override
  public UserMarketDataSpecification.Meta metaBean() {
    return UserMarketDataSpecification.Meta.INSTANCE;
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
   * Gets the userSnapshotId.
   * @return the value of the property, not null
   */
  public UniqueId getUserSnapshotId() {
    return _userSnapshotId;
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
      UserMarketDataSpecification other = (UserMarketDataSpecification) obj;
      return JodaBeanUtils.equal(_userSnapshotId, other._userSnapshotId);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_userSnapshotId);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("UserMarketDataSpecification{");
    buf.append("userSnapshotId").append('=').append(JodaBeanUtils.toString(_userSnapshotId));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code UserMarketDataSpecification}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code userSnapshotId} property.
     */
    private final MetaProperty<UniqueId> _userSnapshotId = DirectMetaProperty.ofImmutable(
        this, "userSnapshotId", UserMarketDataSpecification.class, UniqueId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "userSnapshotId");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -359316438:  // userSnapshotId
          return _userSnapshotId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public UserMarketDataSpecification.Builder builder() {
      return new UserMarketDataSpecification.Builder();
    }

    @Override
    public Class<? extends UserMarketDataSpecification> beanType() {
      return UserMarketDataSpecification.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code userSnapshotId} property.
     * @return the meta-property, not null
     */
    public MetaProperty<UniqueId> userSnapshotId() {
      return _userSnapshotId;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -359316438:  // userSnapshotId
          return ((UserMarketDataSpecification) bean).getUserSnapshotId();
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
   * The bean-builder for {@code UserMarketDataSpecification}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<UserMarketDataSpecification> {

    private UniqueId _userSnapshotId;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(UserMarketDataSpecification beanToCopy) {
      this._userSnapshotId = beanToCopy.getUserSnapshotId();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -359316438:  // userSnapshotId
          return _userSnapshotId;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -359316438:  // userSnapshotId
          this._userSnapshotId = (UniqueId) newValue;
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
    public UserMarketDataSpecification build() {
      return new UserMarketDataSpecification(
          _userSnapshotId);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the userSnapshotId.
     * @param userSnapshotId  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder userSnapshotId(UniqueId userSnapshotId) {
      JodaBeanUtils.notNull(userSnapshotId, "userSnapshotId");
      this._userSnapshotId = userSnapshotId;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("UserMarketDataSpecification.Builder{");
      buf.append("userSnapshotId").append('=').append(JodaBeanUtils.toString(_userSnapshotId));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
