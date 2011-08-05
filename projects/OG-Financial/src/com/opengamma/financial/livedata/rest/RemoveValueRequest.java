/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.livedata.rest;

import java.util.Map;

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

import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.id.ExternalId;
import com.opengamma.util.PublicSPI;

/**
 * 
 */
@PublicSPI
@BeanDefinition
public class RemoveValueRequest extends DirectBean {

  @PropertyDefinition
  private ValueRequirement _valueRequirement;
  
  @PropertyDefinition
  private ExternalId _identifier;
  
  @PropertyDefinition
  private String _valueName;
  
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code RemoveValueRequest}.
   * @return the meta-bean, not null
   */
  public static RemoveValueRequest.Meta meta() {
    return RemoveValueRequest.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(RemoveValueRequest.Meta.INSTANCE);
  }

  @Override
  public RemoveValueRequest.Meta metaBean() {
    return RemoveValueRequest.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -755281390:  // valueRequirement
        return getValueRequirement();
      case -1618432855:  // identifier
        return getIdentifier();
      case -765894756:  // valueName
        return getValueName();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -755281390:  // valueRequirement
        setValueRequirement((ValueRequirement) newValue);
        return;
      case -1618432855:  // identifier
        setIdentifier((ExternalId) newValue);
        return;
      case -765894756:  // valueName
        setValueName((String) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      RemoveValueRequest other = (RemoveValueRequest) obj;
      return JodaBeanUtils.equal(getValueRequirement(), other.getValueRequirement()) &&
          JodaBeanUtils.equal(getIdentifier(), other.getIdentifier()) &&
          JodaBeanUtils.equal(getValueName(), other.getValueName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getValueRequirement());
    hash += hash * 31 + JodaBeanUtils.hashCode(getIdentifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(getValueName());
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the valueRequirement.
   * @return the value of the property
   */
  public ValueRequirement getValueRequirement() {
    return _valueRequirement;
  }

  /**
   * Sets the valueRequirement.
   * @param valueRequirement  the new value of the property
   */
  public void setValueRequirement(ValueRequirement valueRequirement) {
    this._valueRequirement = valueRequirement;
  }

  /**
   * Gets the the {@code valueRequirement} property.
   * @return the property, not null
   */
  public final Property<ValueRequirement> valueRequirement() {
    return metaBean().valueRequirement().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the identifier.
   * @return the value of the property
   */
  public ExternalId getIdentifier() {
    return _identifier;
  }

  /**
   * Sets the identifier.
   * @param identifier  the new value of the property
   */
  public void setIdentifier(ExternalId identifier) {
    this._identifier = identifier;
  }

  /**
   * Gets the the {@code identifier} property.
   * @return the property, not null
   */
  public final Property<ExternalId> identifier() {
    return metaBean().identifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the valueName.
   * @return the value of the property
   */
  public String getValueName() {
    return _valueName;
  }

  /**
   * Sets the valueName.
   * @param valueName  the new value of the property
   */
  public void setValueName(String valueName) {
    this._valueName = valueName;
  }

  /**
   * Gets the the {@code valueName} property.
   * @return the property, not null
   */
  public final Property<String> valueName() {
    return metaBean().valueName().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code RemoveValueRequest}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code valueRequirement} property.
     */
    private final MetaProperty<ValueRequirement> _valueRequirement = DirectMetaProperty.ofReadWrite(
        this, "valueRequirement", RemoveValueRequest.class, ValueRequirement.class);
    /**
     * The meta-property for the {@code identifier} property.
     */
    private final MetaProperty<ExternalId> _identifier = DirectMetaProperty.ofReadWrite(
        this, "identifier", RemoveValueRequest.class, ExternalId.class);
    /**
     * The meta-property for the {@code valueName} property.
     */
    private final MetaProperty<String> _valueName = DirectMetaProperty.ofReadWrite(
        this, "valueName", RemoveValueRequest.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
        this, null,
        "valueRequirement",
        "identifier",
        "valueName");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -755281390:  // valueRequirement
          return _valueRequirement;
        case -1618432855:  // identifier
          return _identifier;
        case -765894756:  // valueName
          return _valueName;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends RemoveValueRequest> builder() {
      return new DirectBeanBuilder<RemoveValueRequest>(new RemoveValueRequest());
    }

    @Override
    public Class<? extends RemoveValueRequest> beanType() {
      return RemoveValueRequest.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code valueRequirement} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ValueRequirement> valueRequirement() {
      return _valueRequirement;
    }

    /**
     * The meta-property for the {@code identifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> identifier() {
      return _identifier;
    }

    /**
     * The meta-property for the {@code valueName} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> valueName() {
      return _valueName;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
