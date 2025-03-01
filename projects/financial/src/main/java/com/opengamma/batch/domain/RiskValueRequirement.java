/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.batch.domain;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.engine.value.ValueProperties;

/**
 * Data model for a risk value requirement.
 */
@BeanDefinition
public class RiskValueRequirement extends RiskValueProperties {

  @PropertyDefinition
  private RiskValueSpecification _specification;

  public RiskValueRequirement() {
  }

  public RiskValueRequirement(final ValueProperties requirement) {
    super(requirement);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code RiskValueRequirement}.
   * @return the meta-bean, not null
   */
  public static RiskValueRequirement.Meta meta() {
    return RiskValueRequirement.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(RiskValueRequirement.Meta.INSTANCE);
  }

  @Override
  public RiskValueRequirement.Meta metaBean() {
    return RiskValueRequirement.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the specification.
   * @return the value of the property
   */
  public RiskValueSpecification getSpecification() {
    return _specification;
  }

  /**
   * Sets the specification.
   * @param specification  the new value of the property
   */
  public void setSpecification(RiskValueSpecification specification) {
    this._specification = specification;
  }

  /**
   * Gets the the {@code specification} property.
   * @return the property, not null
   */
  public final Property<RiskValueSpecification> specification() {
    return metaBean().specification().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public RiskValueRequirement clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      RiskValueRequirement other = (RiskValueRequirement) obj;
      return JodaBeanUtils.equal(getSpecification(), other.getSpecification()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getSpecification());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("RiskValueRequirement{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
    buf.append("specification").append('=').append(JodaBeanUtils.toString(getSpecification())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code RiskValueRequirement}.
   */
  public static class Meta extends RiskValueProperties.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code specification} property.
     */
    private final MetaProperty<RiskValueSpecification> _specification = DirectMetaProperty.ofReadWrite(
        this, "specification", RiskValueRequirement.class, RiskValueSpecification.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "specification");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1307197699:  // specification
          return _specification;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends RiskValueRequirement> builder() {
      return new DirectBeanBuilder<RiskValueRequirement>(new RiskValueRequirement());
    }

    @Override
    public Class<? extends RiskValueRequirement> beanType() {
      return RiskValueRequirement.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code specification} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<RiskValueSpecification> specification() {
      return _specification;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1307197699:  // specification
          return ((RiskValueRequirement) bean).getSpecification();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1307197699:  // specification
          ((RiskValueRequirement) bean).setSpecification((RiskValueSpecification) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
