/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.marketdatasnapshot;

import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableConstructor;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.marketdatasnapshot.NamedSnapshot;
import com.opengamma.id.UniqueId;

/**
 * Named snapshot used to test database insertion/retrieval.
 */
@BeanDefinition
final class MockAlternativeNamedSnapshot implements NamedSnapshot, ImmutableBean {

  @PropertyDefinition(overrideGet = true)
  private final UniqueId _uniqueId;
  @PropertyDefinition(overrideGet = true)
  private final String _name;
  @PropertyDefinition
  private final int _answer;

  @ImmutableConstructor
  MockAlternativeNamedSnapshot(final UniqueId uniqueId, final String name, final int answer) {
    _uniqueId = uniqueId;
    _name = name;
    _answer = answer;
  }

  @Override
  public NamedSnapshot withUniqueId(final UniqueId uniqueId) {
    return new MockAlternativeNamedSnapshot(uniqueId, _name, _answer);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code MockAlternativeNamedSnapshot}.
   * @return the meta-bean, not null
   */
  public static MockAlternativeNamedSnapshot.Meta meta() {
    return MockAlternativeNamedSnapshot.Meta.INSTANCE;
  }

  static {
    MetaBean.register(MockAlternativeNamedSnapshot.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  static MockAlternativeNamedSnapshot.Builder builder() {
    return new MockAlternativeNamedSnapshot.Builder();
  }

  @Override
  public MockAlternativeNamedSnapshot.Meta metaBean() {
    return MockAlternativeNamedSnapshot.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the uniqueId.
   * @return the value of the property
   */
  @Override
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name.
   * @return the value of the property
   */
  @Override
  public String getName() {
    return _name;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the answer.
   * @return the value of the property
   */
  public int getAnswer() {
    return _answer;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MockAlternativeNamedSnapshot other = (MockAlternativeNamedSnapshot) obj;
      return JodaBeanUtils.equal(_uniqueId, other._uniqueId) &&
          JodaBeanUtils.equal(_name, other._name) &&
          (_answer == other._answer);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(_uniqueId);
    hash = hash * 31 + JodaBeanUtils.hashCode(_name);
    hash = hash * 31 + JodaBeanUtils.hashCode(_answer);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("MockAlternativeNamedSnapshot{");
    buf.append("uniqueId").append('=').append(_uniqueId).append(',').append(' ');
    buf.append("name").append('=').append(_name).append(',').append(' ');
    buf.append("answer").append('=').append(JodaBeanUtils.toString(_answer));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MockAlternativeNamedSnapshot}.
   */
  static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueId> _uniqueId = DirectMetaProperty.ofImmutable(
        this, "uniqueId", MockAlternativeNamedSnapshot.class, UniqueId.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofImmutable(
        this, "name", MockAlternativeNamedSnapshot.class, String.class);
    /**
     * The meta-property for the {@code answer} property.
     */
    private final MetaProperty<Integer> _answer = DirectMetaProperty.ofImmutable(
        this, "answer", MockAlternativeNamedSnapshot.class, Integer.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "uniqueId",
        "name",
        "answer");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return _uniqueId;
        case 3373707:  // name
          return _name;
        case -1412808770:  // answer
          return _answer;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public MockAlternativeNamedSnapshot.Builder builder() {
      return new MockAlternativeNamedSnapshot.Builder();
    }

    @Override
    public Class<? extends MockAlternativeNamedSnapshot> beanType() {
      return MockAlternativeNamedSnapshot.class;
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
    public MetaProperty<UniqueId> uniqueId() {
      return _uniqueId;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> name() {
      return _name;
    }

    /**
     * The meta-property for the {@code answer} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Integer> answer() {
      return _answer;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return ((MockAlternativeNamedSnapshot) bean).getUniqueId();
        case 3373707:  // name
          return ((MockAlternativeNamedSnapshot) bean).getName();
        case -1412808770:  // answer
          return ((MockAlternativeNamedSnapshot) bean).getAnswer();
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
   * The bean-builder for {@code MockAlternativeNamedSnapshot}.
   */
  static final class Builder extends DirectFieldsBeanBuilder<MockAlternativeNamedSnapshot> {

    private UniqueId _uniqueId;
    private String _name;
    private int _answer;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(MockAlternativeNamedSnapshot beanToCopy) {
      this._uniqueId = beanToCopy.getUniqueId();
      this._name = beanToCopy.getName();
      this._answer = beanToCopy.getAnswer();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return _uniqueId;
        case 3373707:  // name
          return _name;
        case -1412808770:  // answer
          return _answer;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          this._uniqueId = (UniqueId) newValue;
          break;
        case 3373707:  // name
          this._name = (String) newValue;
          break;
        case -1412808770:  // answer
          this._answer = (Integer) newValue;
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
    public MockAlternativeNamedSnapshot build() {
      return new MockAlternativeNamedSnapshot(
          _uniqueId,
          _name,
          _answer);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the uniqueId.
     * @param uniqueId  the new value
     * @return this, for chaining, not null
     */
    public Builder uniqueId(UniqueId uniqueId) {
      this._uniqueId = uniqueId;
      return this;
    }

    /**
     * Sets the name.
     * @param name  the new value
     * @return this, for chaining, not null
     */
    public Builder name(String name) {
      this._name = name;
      return this;
    }

    /**
     * Sets the answer.
     * @param answer  the new value
     * @return this, for chaining, not null
     */
    public Builder answer(int answer) {
      this._answer = answer;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("MockAlternativeNamedSnapshot.Builder{");
      buf.append("uniqueId").append('=').append(JodaBeanUtils.toString(_uniqueId)).append(',').append(' ');
      buf.append("name").append('=').append(JodaBeanUtils.toString(_name)).append(',').append(' ');
      buf.append("answer").append('=').append(JodaBeanUtils.toString(_answer));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
