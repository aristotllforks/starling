/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.security.impl;

import java.io.Serializable;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.Maps;
import com.opengamma.core.security.Security;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.MutableUniqueIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.util.ArgumentChecker;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Simple implementation of {@code Security}.
 * <p>
 * This is the simplest possible implementation of the {@link Security} interface.
 * <p>
 * This class is mutable and not thread-safe. It is intended to be used in the engine via the read-only {@code Security} interface.
 */
@BeanDefinition
public class SimpleSecurity extends DirectBean implements Security, MutableUniqueIdentifiable, Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The unique identifier.
   */
  @PropertyDefinition(overrideGet = true, overrideSet = true)
  private UniqueId _uniqueId;
  /**
   * The external identifier bundle.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private ExternalIdBundle _externalIdBundle = ExternalIdBundle.EMPTY;
  /**
   * The security type.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private String _securityType;
  /**
   * The display name.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private String _name = "";
  /**
   * The general purpose security attributes. These can be used to add arbitrary additional information to the object.
   */
  @PropertyDefinition(validate = "notNull", overrideSet = true, overrideGet = true)
  private final Map<String, String> _attributes = Maps.newHashMap();

  /**
   * Creates an instance.
   */
  private SimpleSecurity() {
  }

  /**
   * Creates an instance.
   *
   * @param securityType
   *          the security type, not null
   */
  public SimpleSecurity(final String securityType) {
    setSecurityType(securityType);
  }

  /**
   * Creates an instance.
   *
   * @param uniqueId
   *          the unique identifier, may be null
   * @param bundle
   *          the external identifier bundle, not null
   * @param securityType
   *          the security type, not null
   * @param name
   *          the display name, not null
   */
  public SimpleSecurity(final UniqueId uniqueId, final ExternalIdBundle bundle, final String securityType, final String name) {
    setUniqueId(uniqueId);
    setExternalIdBundle(bundle);
    setSecurityType(securityType);
    setName(name);
  }

  // -------------------------------------------------------------------------
  /**
   * Adds an external identifier to the bundle.
   *
   * @param externalId
   *          the external identifier, not null
   */
  public void addExternalId(final ExternalId externalId) {
    setExternalIdBundle(getExternalIdBundle().withExternalId(externalId));
  }

  // -------------------------------------------------------------------------
  @Override
  public void addAttribute(final String key, final String value) {
    ArgumentChecker.notNull(key, "key");
    ArgumentChecker.notNull(value, "value");
    _attributes.put(key, value);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code SimpleSecurity}.
   * @return the meta-bean, not null
   */
  public static SimpleSecurity.Meta meta() {
    return SimpleSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(SimpleSecurity.Meta.INSTANCE);
  }

  @Override
  public SimpleSecurity.Meta metaBean() {
    return SimpleSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unique identifier.
   * @return the value of the property
   */
  @Override
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the unique identifier.
   * @param uniqueId  the new value of the property
   */
  @Override
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
   * Gets the external identifier bundle.
   * @return the value of the property, not null
   */
  @Override
  public ExternalIdBundle getExternalIdBundle() {
    return _externalIdBundle;
  }

  /**
   * Sets the external identifier bundle.
   * @param externalIdBundle  the new value of the property, not null
   */
  public void setExternalIdBundle(ExternalIdBundle externalIdBundle) {
    JodaBeanUtils.notNull(externalIdBundle, "externalIdBundle");
    this._externalIdBundle = externalIdBundle;
  }

  /**
   * Gets the the {@code externalIdBundle} property.
   * @return the property, not null
   */
  public final Property<ExternalIdBundle> externalIdBundle() {
    return metaBean().externalIdBundle().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security type.
   * @return the value of the property, not null
   */
  @Override
  public String getSecurityType() {
    return _securityType;
  }

  /**
   * Sets the security type.
   * @param securityType  the new value of the property, not null
   */
  public void setSecurityType(String securityType) {
    JodaBeanUtils.notNull(securityType, "securityType");
    this._securityType = securityType;
  }

  /**
   * Gets the the {@code securityType} property.
   * @return the property, not null
   */
  public final Property<String> securityType() {
    return metaBean().securityType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the display name.
   * @return the value of the property, not null
   */
  @Override
  public String getName() {
    return _name;
  }

  /**
   * Sets the display name.
   * @param name  the new value of the property, not null
   */
  public void setName(String name) {
    JodaBeanUtils.notNull(name, "name");
    this._name = name;
  }

  /**
   * Gets the the {@code name} property.
   * @return the property, not null
   */
  public final Property<String> name() {
    return metaBean().name().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the general purpose security attributes. These can be used to add arbitrary additional information to the object.
   * @return the value of the property, not null
   */
  @Override
  public Map<String, String> getAttributes() {
    return _attributes;
  }

  /**
   * Sets the general purpose security attributes. These can be used to add arbitrary additional information to the object.
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
  @Override
  public SimpleSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SimpleSecurity other = (SimpleSecurity) obj;
      return JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          JodaBeanUtils.equal(getExternalIdBundle(), other.getExternalIdBundle()) &&
          JodaBeanUtils.equal(getSecurityType(), other.getSecurityType()) &&
          JodaBeanUtils.equal(getName(), other.getName()) &&
          JodaBeanUtils.equal(getAttributes(), other.getAttributes());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExternalIdBundle());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityType());
    hash = hash * 31 + JodaBeanUtils.hashCode(getName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getAttributes());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("SimpleSecurity{");
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
    buf.append("externalIdBundle").append('=').append(JodaBeanUtils.toString(getExternalIdBundle())).append(',').append(' ');
    buf.append("securityType").append('=').append(JodaBeanUtils.toString(getSecurityType())).append(',').append(' ');
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
    buf.append("attributes").append('=').append(JodaBeanUtils.toString(getAttributes())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SimpleSecurity}.
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
        this, "uniqueId", SimpleSecurity.class, UniqueId.class);
    /**
     * The meta-property for the {@code externalIdBundle} property.
     */
    private final MetaProperty<ExternalIdBundle> _externalIdBundle = DirectMetaProperty.ofReadWrite(
        this, "externalIdBundle", SimpleSecurity.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code securityType} property.
     */
    private final MetaProperty<String> _securityType = DirectMetaProperty.ofReadWrite(
        this, "securityType", SimpleSecurity.class, String.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", SimpleSecurity.class, String.class);
    /**
     * The meta-property for the {@code attributes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<String, String>> _attributes = DirectMetaProperty.ofReadWrite(
        this, "attributes", SimpleSecurity.class, (Class) Map.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "uniqueId",
        "externalIdBundle",
        "securityType",
        "name",
        "attributes");

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
        case -736922008:  // externalIdBundle
          return _externalIdBundle;
        case 808245914:  // securityType
          return _securityType;
        case 3373707:  // name
          return _name;
        case 405645655:  // attributes
          return _attributes;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SimpleSecurity> builder() {
      return new DirectBeanBuilder<>(new SimpleSecurity());
    }

    @Override
    public Class<? extends SimpleSecurity> beanType() {
      return SimpleSecurity.class;
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
     * The meta-property for the {@code externalIdBundle} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalIdBundle> externalIdBundle() {
      return _externalIdBundle;
    }

    /**
     * The meta-property for the {@code securityType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> securityType() {
      return _securityType;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
    }

    /**
     * The meta-property for the {@code attributes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Map<String, String>> attributes() {
      return _attributes;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return ((SimpleSecurity) bean).getUniqueId();
        case -736922008:  // externalIdBundle
          return ((SimpleSecurity) bean).getExternalIdBundle();
        case 808245914:  // securityType
          return ((SimpleSecurity) bean).getSecurityType();
        case 3373707:  // name
          return ((SimpleSecurity) bean).getName();
        case 405645655:  // attributes
          return ((SimpleSecurity) bean).getAttributes();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          ((SimpleSecurity) bean).setUniqueId((UniqueId) newValue);
          return;
        case -736922008:  // externalIdBundle
          ((SimpleSecurity) bean).setExternalIdBundle((ExternalIdBundle) newValue);
          return;
        case 808245914:  // securityType
          ((SimpleSecurity) bean).setSecurityType((String) newValue);
          return;
        case 3373707:  // name
          ((SimpleSecurity) bean).setName((String) newValue);
          return;
        case 405645655:  // attributes
          ((SimpleSecurity) bean).setAttributes((Map<String, String>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((SimpleSecurity) bean)._externalIdBundle, "externalIdBundle");
      JodaBeanUtils.notNull(((SimpleSecurity) bean)._securityType, "securityType");
      JodaBeanUtils.notNull(((SimpleSecurity) bean)._name, "name");
      JodaBeanUtils.notNull(((SimpleSecurity) bean)._attributes, "attributes");
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
