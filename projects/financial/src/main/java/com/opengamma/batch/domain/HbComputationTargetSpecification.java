/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.batch.domain;

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

import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.id.UniqueId;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Data model for the computation target specification.
 */
@BeanDefinition
public class HbComputationTargetSpecification extends DirectBean {

  @PropertyDefinition
  private long _id = -1;

  /**
   * The type of the target.
   */
  @PropertyDefinition
  private ComputationTargetType _type;

  /**
   * The identifier of the target (schema).
   */
  @PropertyDefinition
  private String _uidScheme;

  /**
   * The identifier of the target (value).
   */
  @PropertyDefinition
  private String _uidValue;

  /**
   * The identifier of the target (version).
   */
  @PropertyDefinition
  private String _uidVersion;

  /**
   * Gets the unique id of the base market data.
   * 
   * @return the value of the base market data.
   */
  public UniqueId getUniqueId() {
    return UniqueId.of(getUidScheme(), getUidValue(), getUidVersion());
  }

  /**
   * Sets the unique id of the base market data.
   * 
   * @param baseUid
   *          the new base market data.
   */
  public void setUniqueId(final UniqueId baseUid) {
    setUidScheme(baseUid.getScheme());
    setUidValue(baseUid.getValue());
    setUidVersion(baseUid.getVersion());
  }

  public HbComputationTargetSpecification() {
  }

  public HbComputationTargetSpecification(final ComputationTargetSpecification computationTargetSpecification) {
    setType(computationTargetSpecification.getType());
    setUniqueId(computationTargetSpecification.getUniqueId());
  }

  public ComputationTargetSpecification toComputationTargetSpec() {
    return new ComputationTargetSpecification(_type, getUniqueId());
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code HbComputationTargetSpecification}.
   * @return the meta-bean, not null
   */
  public static HbComputationTargetSpecification.Meta meta() {
    return HbComputationTargetSpecification.Meta.INSTANCE;
  }

  static {
    MetaBean.register(HbComputationTargetSpecification.Meta.INSTANCE);
  }

  @Override
  public HbComputationTargetSpecification.Meta metaBean() {
    return HbComputationTargetSpecification.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the id.
   * @return the value of the property
   */
  public long getId() {
    return _id;
  }

  /**
   * Sets the id.
   * @param id  the new value of the property
   */
  public void setId(long id) {
    this._id = id;
  }

  /**
   * Gets the the {@code id} property.
   * @return the property, not null
   */
  public final Property<Long> id() {
    return metaBean().id().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the type of the target.
   * @return the value of the property
   */
  public ComputationTargetType getType() {
    return _type;
  }

  /**
   * Sets the type of the target.
   * @param type  the new value of the property
   */
  public void setType(ComputationTargetType type) {
    this._type = type;
  }

  /**
   * Gets the the {@code type} property.
   * @return the property, not null
   */
  public final Property<ComputationTargetType> type() {
    return metaBean().type().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the identifier of the target (schema).
   * @return the value of the property
   */
  public String getUidScheme() {
    return _uidScheme;
  }

  /**
   * Sets the identifier of the target (schema).
   * @param uidScheme  the new value of the property
   */
  public void setUidScheme(String uidScheme) {
    this._uidScheme = uidScheme;
  }

  /**
   * Gets the the {@code uidScheme} property.
   * @return the property, not null
   */
  public final Property<String> uidScheme() {
    return metaBean().uidScheme().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the identifier of the target (value).
   * @return the value of the property
   */
  public String getUidValue() {
    return _uidValue;
  }

  /**
   * Sets the identifier of the target (value).
   * @param uidValue  the new value of the property
   */
  public void setUidValue(String uidValue) {
    this._uidValue = uidValue;
  }

  /**
   * Gets the the {@code uidValue} property.
   * @return the property, not null
   */
  public final Property<String> uidValue() {
    return metaBean().uidValue().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the identifier of the target (version).
   * @return the value of the property
   */
  public String getUidVersion() {
    return _uidVersion;
  }

  /**
   * Sets the identifier of the target (version).
   * @param uidVersion  the new value of the property
   */
  public void setUidVersion(String uidVersion) {
    this._uidVersion = uidVersion;
  }

  /**
   * Gets the the {@code uidVersion} property.
   * @return the property, not null
   */
  public final Property<String> uidVersion() {
    return metaBean().uidVersion().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public HbComputationTargetSpecification clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      HbComputationTargetSpecification other = (HbComputationTargetSpecification) obj;
      return (getId() == other.getId()) &&
          JodaBeanUtils.equal(getType(), other.getType()) &&
          JodaBeanUtils.equal(getUidScheme(), other.getUidScheme()) &&
          JodaBeanUtils.equal(getUidValue(), other.getUidValue()) &&
          JodaBeanUtils.equal(getUidVersion(), other.getUidVersion());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getType());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUidScheme());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUidValue());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUidVersion());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("HbComputationTargetSpecification{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("id").append('=').append(JodaBeanUtils.toString(getId())).append(',').append(' ');
    buf.append("type").append('=').append(JodaBeanUtils.toString(getType())).append(',').append(' ');
    buf.append("uidScheme").append('=').append(JodaBeanUtils.toString(getUidScheme())).append(',').append(' ');
    buf.append("uidValue").append('=').append(JodaBeanUtils.toString(getUidValue())).append(',').append(' ');
    buf.append("uidVersion").append('=').append(JodaBeanUtils.toString(getUidVersion())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code HbComputationTargetSpecification}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code id} property.
     */
    private final MetaProperty<Long> _id = DirectMetaProperty.ofReadWrite(
        this, "id", HbComputationTargetSpecification.class, Long.TYPE);
    /**
     * The meta-property for the {@code type} property.
     */
    private final MetaProperty<ComputationTargetType> _type = DirectMetaProperty.ofReadWrite(
        this, "type", HbComputationTargetSpecification.class, ComputationTargetType.class);
    /**
     * The meta-property for the {@code uidScheme} property.
     */
    private final MetaProperty<String> _uidScheme = DirectMetaProperty.ofReadWrite(
        this, "uidScheme", HbComputationTargetSpecification.class, String.class);
    /**
     * The meta-property for the {@code uidValue} property.
     */
    private final MetaProperty<String> _uidValue = DirectMetaProperty.ofReadWrite(
        this, "uidValue", HbComputationTargetSpecification.class, String.class);
    /**
     * The meta-property for the {@code uidVersion} property.
     */
    private final MetaProperty<String> _uidVersion = DirectMetaProperty.ofReadWrite(
        this, "uidVersion", HbComputationTargetSpecification.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "id",
        "type",
        "uidScheme",
        "uidValue",
        "uidVersion");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3355:  // id
          return _id;
        case 3575610:  // type
          return _type;
        case -1680381419:  // uidScheme
          return _uidScheme;
        case -605679871:  // uidValue
          return _uidValue;
        case -2117765528:  // uidVersion
          return _uidVersion;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends HbComputationTargetSpecification> builder() {
      return new DirectBeanBuilder<>(new HbComputationTargetSpecification());
    }

    @Override
    public Class<? extends HbComputationTargetSpecification> beanType() {
      return HbComputationTargetSpecification.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code id} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Long> id() {
      return _id;
    }

    /**
     * The meta-property for the {@code type} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ComputationTargetType> type() {
      return _type;
    }

    /**
     * The meta-property for the {@code uidScheme} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uidScheme() {
      return _uidScheme;
    }

    /**
     * The meta-property for the {@code uidValue} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uidValue() {
      return _uidValue;
    }

    /**
     * The meta-property for the {@code uidVersion} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uidVersion() {
      return _uidVersion;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3355:  // id
          return ((HbComputationTargetSpecification) bean).getId();
        case 3575610:  // type
          return ((HbComputationTargetSpecification) bean).getType();
        case -1680381419:  // uidScheme
          return ((HbComputationTargetSpecification) bean).getUidScheme();
        case -605679871:  // uidValue
          return ((HbComputationTargetSpecification) bean).getUidValue();
        case -2117765528:  // uidVersion
          return ((HbComputationTargetSpecification) bean).getUidVersion();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3355:  // id
          ((HbComputationTargetSpecification) bean).setId((Long) newValue);
          return;
        case 3575610:  // type
          ((HbComputationTargetSpecification) bean).setType((ComputationTargetType) newValue);
          return;
        case -1680381419:  // uidScheme
          ((HbComputationTargetSpecification) bean).setUidScheme((String) newValue);
          return;
        case -605679871:  // uidValue
          ((HbComputationTargetSpecification) bean).setUidValue((String) newValue);
          return;
        case -2117765528:  // uidVersion
          ((HbComputationTargetSpecification) bean).setUidVersion((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
