/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.ircurve.strips;

import java.io.Serializable;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.time.Tenor;

/**
 * Base class describing a node on a curve.
 */
@BeanDefinition
public abstract class CurveNode extends DirectBean implements Serializable, Comparable<CurveNode>, VisitableCurveNode {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * The curve node id mapper name, can be null.
   */
  // TODO move onto its own abstract subclass, e.g. CurveNodeWithIdMapper: PLAT-6504
  @PropertyDefinition
  private String _curveNodeIdMapperName;

  /**
   * The name, can be null.
   */
  @PropertyDefinition
  private String _name;

  /**
   * For the builder.
   */
  protected CurveNode() {
    super();
  }

  /**
   * @param curveNodeIdMapperName
   *          The name of the id mapper, not null
   */
  public CurveNode(final String curveNodeIdMapperName) {
    setCurveNodeIdMapperName(curveNodeIdMapperName);
  }

  /**
   * @param curveNodeIdMapperName
   *          The name of the id mapper, not null
   * @param name
   *          The name of the node, not null
   */
  public CurveNode(final String curveNodeIdMapperName, final String name) {
    setCurveNodeIdMapperName(curveNodeIdMapperName);
    setName(name);
  }

  /**
   * Gets the resolved maturity of the node.
   * 
   * @return The resolved maturity of the node
   */
  public abstract Tenor getResolvedMaturity();

  @Override
  public int compareTo(final CurveNode other) {
    final int result = getResolvedMaturity().compareTo(other.getResolvedMaturity());
    if (result != 0) {
      return result;
    }
    return getClass().getName().compareTo(other.getClass().getName());
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CurveNode}.
   * @return the meta-bean, not null
   */
  public static CurveNode.Meta meta() {
    return CurveNode.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CurveNode.Meta.INSTANCE);
  }

  @Override
  public CurveNode.Meta metaBean() {
    return CurveNode.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the curveNodeIdMapperName.
   * @return the value of the property
   */
  public String getCurveNodeIdMapperName() {
    return _curveNodeIdMapperName;
  }

  /**
   * Sets the curveNodeIdMapperName.
   * @param curveNodeIdMapperName  the new value of the property
   */
  public void setCurveNodeIdMapperName(String curveNodeIdMapperName) {
    this._curveNodeIdMapperName = curveNodeIdMapperName;
  }

  /**
   * Gets the the {@code curveNodeIdMapperName} property.
   * @return the property, not null
   */
  public final Property<String> curveNodeIdMapperName() {
    return metaBean().curveNodeIdMapperName().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name, can be null.
   * @return the value of the property
   */
  public String getName() {
    return _name;
  }

  /**
   * Sets the name, can be null.
   * @param name  the new value of the property
   */
  public void setName(String name) {
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
  @Override
  public CurveNode clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CurveNode other = (CurveNode) obj;
      return JodaBeanUtils.equal(getCurveNodeIdMapperName(), other.getCurveNodeIdMapperName()) &&
          JodaBeanUtils.equal(getName(), other.getName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurveNodeIdMapperName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getName());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("CurveNode{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("curveNodeIdMapperName").append('=').append(JodaBeanUtils.toString(getCurveNodeIdMapperName())).append(',').append(' ');
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CurveNode}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code curveNodeIdMapperName} property.
     */
    private final MetaProperty<String> _curveNodeIdMapperName = DirectMetaProperty.ofReadWrite(
        this, "curveNodeIdMapperName", CurveNode.class, String.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", CurveNode.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "curveNodeIdMapperName",
        "name");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 305053208:  // curveNodeIdMapperName
          return _curveNodeIdMapperName;
        case 3373707:  // name
          return _name;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CurveNode> builder() {
      throw new UnsupportedOperationException("CurveNode is an abstract class");
    }

    @Override
    public Class<? extends CurveNode> beanType() {
      return CurveNode.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code curveNodeIdMapperName} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> curveNodeIdMapperName() {
      return _curveNodeIdMapperName;
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
        case 305053208:  // curveNodeIdMapperName
          return ((CurveNode) bean).getCurveNodeIdMapperName();
        case 3373707:  // name
          return ((CurveNode) bean).getName();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 305053208:  // curveNodeIdMapperName
          ((CurveNode) bean).setCurveNodeIdMapperName((String) newValue);
          return;
        case 3373707:  // name
          ((CurveNode) bean).setName((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
