/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.convention.impl;

import java.util.HashMap;
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

import com.opengamma.core.convention.Convention;
import com.opengamma.core.convention.ConventionType;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.UniqueId;
import com.opengamma.util.ArgumentChecker;

/**
 * Mock convention.
 */
@BeanDefinition
public class MockConvention implements Bean, Convention {

  /**
   * The type.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private ConventionType _conventionType;
  /**
   * The unique identifier of the convention.
   * This must be null when adding to a master and not null when retrieved from a master.
   */
  @PropertyDefinition(overrideGet = true)
  private UniqueId _uniqueId;
  /**
   * The bundle of external identifiers that define the convention.
   * This field must not be null for the object to be valid.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private ExternalIdBundle _externalIdBundle = ExternalIdBundle.EMPTY;
  /**
   * The map of attributes, which can be used for attaching additional application-level information.
   */
  @PropertyDefinition(overrideGet = true, overrideSet = true)
  private final Map<String, String> _attributes = new HashMap<>();
  /**
   * The name of the convention intended for display purposes.
   * This field must not be null for the object to be valid.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private String _name = "";

  /**
   * Creates an instance.
   */
  protected MockConvention() {
  }

  /**
   * Creates an instance.
   *
   * @param type  the type, not null
   */
  public MockConvention(final String type) {
    setConventionType(ConventionType.of(type));
  }

  /**
   * Creates an instance.
   *
   * @param type  the type, not null
   * @param uniqueId  the unique identifier
   * @param name  the name, not null
   * @param bundle  the bundle, not null
   */
  public MockConvention(final String type, final UniqueId uniqueId, final String name, final ExternalIdBundle bundle) {
    setConventionType(ConventionType.of(type));
    setUniqueId(uniqueId);
    setName(name);
    setExternalIdBundle(bundle);
  }

  //-------------------------------------------------------------------------
  /**
   * Adds an external identifier to the bundle representing this convention.
   *
   * @param conventionId  the identifier to add, not null
   */
  public void addExternalId(final ExternalId conventionId) {
    setExternalIdBundle(getExternalIdBundle().withExternalId(conventionId));
  }

  @Override
  public void addAttribute(final String key, final String value) {
    ArgumentChecker.notNull(key, "key");
    ArgumentChecker.notNull(value, "value");
    _attributes.put(key, value);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MockConvention}.
   * @return the meta-bean, not null
   */
  public static MockConvention.Meta meta() {
    return MockConvention.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MockConvention.Meta.INSTANCE);
  }

  @Override
  public MockConvention.Meta metaBean() {
    return MockConvention.Meta.INSTANCE;
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
   * Gets the type.
   * @return the value of the property, not null
   */
  @Override
  public ConventionType getConventionType() {
    return _conventionType;
  }

  /**
   * Sets the type.
   * @param conventionType  the new value of the property, not null
   */
  public void setConventionType(ConventionType conventionType) {
    JodaBeanUtils.notNull(conventionType, "conventionType");
    this._conventionType = conventionType;
  }

  /**
   * Gets the the {@code conventionType} property.
   * @return the property, not null
   */
  public final Property<ConventionType> conventionType() {
    return metaBean().conventionType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unique identifier of the convention.
   * This must be null when adding to a master and not null when retrieved from a master.
   * @return the value of the property
   */
  @Override
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the unique identifier of the convention.
   * This must be null when adding to a master and not null when retrieved from a master.
   * @param uniqueId  the new value of the property
   */
  public void setUniqueId(UniqueId uniqueId) {
    this._uniqueId = uniqueId;
  }

  /**
   * Gets the the {@code uniqueId} property.
   * This must be null when adding to a master and not null when retrieved from a master.
   * @return the property, not null
   */
  public final Property<UniqueId> uniqueId() {
    return metaBean().uniqueId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the bundle of external identifiers that define the convention.
   * This field must not be null for the object to be valid.
   * @return the value of the property, not null
   */
  @Override
  public ExternalIdBundle getExternalIdBundle() {
    return _externalIdBundle;
  }

  /**
   * Sets the bundle of external identifiers that define the convention.
   * This field must not be null for the object to be valid.
   * @param externalIdBundle  the new value of the property, not null
   */
  public void setExternalIdBundle(ExternalIdBundle externalIdBundle) {
    JodaBeanUtils.notNull(externalIdBundle, "externalIdBundle");
    this._externalIdBundle = externalIdBundle;
  }

  /**
   * Gets the the {@code externalIdBundle} property.
   * This field must not be null for the object to be valid.
   * @return the property, not null
   */
  public final Property<ExternalIdBundle> externalIdBundle() {
    return metaBean().externalIdBundle().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the map of attributes, which can be used for attaching additional application-level information.
   * @return the value of the property, not null
   */
  @Override
  public Map<String, String> getAttributes() {
    return _attributes;
  }

  /**
   * Sets the map of attributes, which can be used for attaching additional application-level information.
   * @param attributes  the new value of the property, not null
   */
  @Override
  public void setAttributes(Map<String, String> attributes) {
    JodaBeanUtils.notNull(attributes, "attributes");
    this._attributes.clear();
    this._attributes.putAll(attributes);
  }

  /**
   * Gets the the {@code attributes} property.
   * @return the property, not null
   */
  public final Property<Map<String, String>> attributes() {
    return metaBean().attributes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name of the convention intended for display purposes.
   * This field must not be null for the object to be valid.
   * @return the value of the property, not null
   */
  @Override
  public String getName() {
    return _name;
  }

  /**
   * Sets the name of the convention intended for display purposes.
   * This field must not be null for the object to be valid.
   * @param name  the new value of the property, not null
   */
  public void setName(String name) {
    JodaBeanUtils.notNull(name, "name");
    this._name = name;
  }

  /**
   * Gets the the {@code name} property.
   * This field must not be null for the object to be valid.
   * @return the property, not null
   */
  public final Property<String> name() {
    return metaBean().name().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public MockConvention clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MockConvention other = (MockConvention) obj;
      return JodaBeanUtils.equal(getConventionType(), other.getConventionType()) &&
          JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          JodaBeanUtils.equal(getExternalIdBundle(), other.getExternalIdBundle()) &&
          JodaBeanUtils.equal(getAttributes(), other.getAttributes()) &&
          JodaBeanUtils.equal(getName(), other.getName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getConventionType());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExternalIdBundle());
    hash = hash * 31 + JodaBeanUtils.hashCode(getAttributes());
    hash = hash * 31 + JodaBeanUtils.hashCode(getName());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("MockConvention{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("conventionType").append('=').append(JodaBeanUtils.toString(getConventionType())).append(',').append(' ');
    buf.append("uniqueId").append('=').append(JodaBeanUtils.toString(getUniqueId())).append(',').append(' ');
    buf.append("externalIdBundle").append('=').append(JodaBeanUtils.toString(getExternalIdBundle())).append(',').append(' ');
    buf.append("attributes").append('=').append(JodaBeanUtils.toString(getAttributes())).append(',').append(' ');
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MockConvention}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code conventionType} property.
     */
    private final MetaProperty<ConventionType> _conventionType = DirectMetaProperty.ofReadWrite(
        this, "conventionType", MockConvention.class, ConventionType.class);
    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueId> _uniqueId = DirectMetaProperty.ofReadWrite(
        this, "uniqueId", MockConvention.class, UniqueId.class);
    /**
     * The meta-property for the {@code externalIdBundle} property.
     */
    private final MetaProperty<ExternalIdBundle> _externalIdBundle = DirectMetaProperty.ofReadWrite(
        this, "externalIdBundle", MockConvention.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code attributes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<String, String>> _attributes = DirectMetaProperty.ofReadWrite(
        this, "attributes", MockConvention.class, (Class) Map.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", MockConvention.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "conventionType",
        "uniqueId",
        "externalIdBundle",
        "attributes",
        "name");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1372339787:  // conventionType
          return _conventionType;
        case -294460212:  // uniqueId
          return _uniqueId;
        case -736922008:  // externalIdBundle
          return _externalIdBundle;
        case 405645655:  // attributes
          return _attributes;
        case 3373707:  // name
          return _name;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MockConvention> builder() {
      return new DirectBeanBuilder<MockConvention>(new MockConvention());
    }

    @Override
    public Class<? extends MockConvention> beanType() {
      return MockConvention.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code conventionType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ConventionType> conventionType() {
      return _conventionType;
    }

    /**
     * The meta-property for the {@code uniqueId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueId> uniqueId() {
      return _uniqueId;
    }

    /**
     * The meta-property for the {@code externalIdBundle} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalIdBundle> externalIdBundle() {
      return _externalIdBundle;
    }

    /**
     * The meta-property for the {@code attributes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Map<String, String>> attributes() {
      return _attributes;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1372339787:  // conventionType
          return ((MockConvention) bean).getConventionType();
        case -294460212:  // uniqueId
          return ((MockConvention) bean).getUniqueId();
        case -736922008:  // externalIdBundle
          return ((MockConvention) bean).getExternalIdBundle();
        case 405645655:  // attributes
          return ((MockConvention) bean).getAttributes();
        case 3373707:  // name
          return ((MockConvention) bean).getName();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1372339787:  // conventionType
          ((MockConvention) bean).setConventionType((ConventionType) newValue);
          return;
        case -294460212:  // uniqueId
          ((MockConvention) bean).setUniqueId((UniqueId) newValue);
          return;
        case -736922008:  // externalIdBundle
          ((MockConvention) bean).setExternalIdBundle((ExternalIdBundle) newValue);
          return;
        case 405645655:  // attributes
          ((MockConvention) bean).setAttributes((Map<String, String>) newValue);
          return;
        case 3373707:  // name
          ((MockConvention) bean).setName((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((MockConvention) bean)._conventionType, "conventionType");
      JodaBeanUtils.notNull(((MockConvention) bean)._externalIdBundle, "externalIdBundle");
      JodaBeanUtils.notNull(((MockConvention) bean)._attributes, "attributes");
      JodaBeanUtils.notNull(((MockConvention) bean)._name, "name");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
