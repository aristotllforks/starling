/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.tool.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


@BeanDefinition
public class ValidationNode extends DirectBean {

  @PropertyDefinition
  private List<ValidationNode> _subNodes = new ArrayList<>();

  @PropertyDefinition(validate = "notNull")
  private Class<?> _type;

  @PropertyDefinition(validate = "notNull")
  private String _name;

  @PropertyDefinition
  private final List<String> _errors = new ArrayList<>();

  @PropertyDefinition
  private final List<String> _warnings = new ArrayList<>();

  @PropertyDefinition
  private boolean _error;

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ValidationNode}.
   * @return the meta-bean, not null
   */
  public static ValidationNode.Meta meta() {
    return ValidationNode.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ValidationNode.Meta.INSTANCE);
  }

  @Override
  public ValidationNode.Meta metaBean() {
    return ValidationNode.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the subNodes.
   * @return the value of the property
   */
  public List<ValidationNode> getSubNodes() {
    return _subNodes;
  }

  /**
   * Sets the subNodes.
   * @param subNodes  the new value of the property
   */
  public void setSubNodes(List<ValidationNode> subNodes) {
    this._subNodes = subNodes;
  }

  /**
   * Gets the the {@code subNodes} property.
   * @return the property, not null
   */
  public final Property<List<ValidationNode>> subNodes() {
    return metaBean().subNodes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the type.
   * @return the value of the property, not null
   */
  public Class<?> getType() {
    return _type;
  }

  /**
   * Sets the type.
   * @param type  the new value of the property, not null
   */
  public void setType(Class<?> type) {
    JodaBeanUtils.notNull(type, "type");
    this._type = type;
  }

  /**
   * Gets the the {@code type} property.
   * @return the property, not null
   */
  public final Property<Class<?>> type() {
    return metaBean().type().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name.
   * @return the value of the property, not null
   */
  public String getName() {
    return _name;
  }

  /**
   * Sets the name.
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
   * Gets the errors.
   * @return the value of the property, not null
   */
  public List<String> getErrors() {
    return _errors;
  }

  /**
   * Sets the errors.
   * @param errors  the new value of the property, not null
   */
  public void setErrors(List<String> errors) {
    JodaBeanUtils.notNull(errors, "errors");
    this._errors.clear();
    this._errors.addAll(errors);
  }

  /**
   * Gets the the {@code errors} property.
   * @return the property, not null
   */
  public final Property<List<String>> errors() {
    return metaBean().errors().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the warnings.
   * @return the value of the property, not null
   */
  public List<String> getWarnings() {
    return _warnings;
  }

  /**
   * Sets the warnings.
   * @param warnings  the new value of the property, not null
   */
  public void setWarnings(List<String> warnings) {
    JodaBeanUtils.notNull(warnings, "warnings");
    this._warnings.clear();
    this._warnings.addAll(warnings);
  }

  /**
   * Gets the the {@code warnings} property.
   * @return the property, not null
   */
  public final Property<List<String>> warnings() {
    return metaBean().warnings().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the error.
   * @return the value of the property
   */
  public boolean isError() {
    return _error;
  }

  /**
   * Sets the error.
   * @param error  the new value of the property
   */
  public void setError(boolean error) {
    this._error = error;
  }

  /**
   * Gets the the {@code error} property.
   * @return the property, not null
   */
  public final Property<Boolean> error() {
    return metaBean().error().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public ValidationNode clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ValidationNode other = (ValidationNode) obj;
      return JodaBeanUtils.equal(getSubNodes(), other.getSubNodes()) &&
          JodaBeanUtils.equal(getType(), other.getType()) &&
          JodaBeanUtils.equal(getName(), other.getName()) &&
          JodaBeanUtils.equal(getErrors(), other.getErrors()) &&
          JodaBeanUtils.equal(getWarnings(), other.getWarnings()) &&
          (isError() == other.isError());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getSubNodes());
    hash = hash * 31 + JodaBeanUtils.hashCode(getType());
    hash = hash * 31 + JodaBeanUtils.hashCode(getName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getErrors());
    hash = hash * 31 + JodaBeanUtils.hashCode(getWarnings());
    hash = hash * 31 + JodaBeanUtils.hashCode(isError());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(224);
    buf.append("ValidationNode{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("subNodes").append('=').append(JodaBeanUtils.toString(getSubNodes())).append(',').append(' ');
    buf.append("type").append('=').append(JodaBeanUtils.toString(getType())).append(',').append(' ');
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
    buf.append("errors").append('=').append(JodaBeanUtils.toString(getErrors())).append(',').append(' ');
    buf.append("warnings").append('=').append(JodaBeanUtils.toString(getWarnings())).append(',').append(' ');
    buf.append("error").append('=').append(JodaBeanUtils.toString(isError())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ValidationNode}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code subNodes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<ValidationNode>> _subNodes = DirectMetaProperty.ofReadWrite(
        this, "subNodes", ValidationNode.class, (Class) List.class);
    /**
     * The meta-property for the {@code type} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Class<?>> _type = DirectMetaProperty.ofReadWrite(
        this, "type", ValidationNode.class, (Class) Class.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", ValidationNode.class, String.class);
    /**
     * The meta-property for the {@code errors} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<String>> _errors = DirectMetaProperty.ofReadWrite(
        this, "errors", ValidationNode.class, (Class) List.class);
    /**
     * The meta-property for the {@code warnings} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<String>> _warnings = DirectMetaProperty.ofReadWrite(
        this, "warnings", ValidationNode.class, (Class) List.class);
    /**
     * The meta-property for the {@code error} property.
     */
    private final MetaProperty<Boolean> _error = DirectMetaProperty.ofReadWrite(
        this, "error", ValidationNode.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "subNodes",
        "type",
        "name",
        "errors",
        "warnings",
        "error");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -2095428527:  // subNodes
          return _subNodes;
        case 3575610:  // type
          return _type;
        case 3373707:  // name
          return _name;
        case -1294635157:  // errors
          return _errors;
        case 498091095:  // warnings
          return _warnings;
        case 96784904:  // error
          return _error;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ValidationNode> builder() {
      return new DirectBeanBuilder<ValidationNode>(new ValidationNode());
    }

    @Override
    public Class<? extends ValidationNode> beanType() {
      return ValidationNode.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code subNodes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<ValidationNode>> subNodes() {
      return _subNodes;
    }

    /**
     * The meta-property for the {@code type} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Class<?>> type() {
      return _type;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
    }

    /**
     * The meta-property for the {@code errors} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<String>> errors() {
      return _errors;
    }

    /**
     * The meta-property for the {@code warnings} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<String>> warnings() {
      return _warnings;
    }

    /**
     * The meta-property for the {@code error} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> error() {
      return _error;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -2095428527:  // subNodes
          return ((ValidationNode) bean).getSubNodes();
        case 3575610:  // type
          return ((ValidationNode) bean).getType();
        case 3373707:  // name
          return ((ValidationNode) bean).getName();
        case -1294635157:  // errors
          return ((ValidationNode) bean).getErrors();
        case 498091095:  // warnings
          return ((ValidationNode) bean).getWarnings();
        case 96784904:  // error
          return ((ValidationNode) bean).isError();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -2095428527:  // subNodes
          ((ValidationNode) bean).setSubNodes((List<ValidationNode>) newValue);
          return;
        case 3575610:  // type
          ((ValidationNode) bean).setType((Class<?>) newValue);
          return;
        case 3373707:  // name
          ((ValidationNode) bean).setName((String) newValue);
          return;
        case -1294635157:  // errors
          ((ValidationNode) bean).setErrors((List<String>) newValue);
          return;
        case 498091095:  // warnings
          ((ValidationNode) bean).setWarnings((List<String>) newValue);
          return;
        case 96784904:  // error
          ((ValidationNode) bean).setError((Boolean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((ValidationNode) bean)._type, "type");
      JodaBeanUtils.notNull(((ValidationNode) bean)._name, "name");
      JodaBeanUtils.notNull(((ValidationNode) bean)._errors, "errors");
      JodaBeanUtils.notNull(((ValidationNode) bean)._warnings, "warnings");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
