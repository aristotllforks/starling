/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.function.dsl;

import java.util.ArrayList;
import java.util.Collection;
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
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * The set of resolved outputs.
 */
@BeanDefinition
public class OutputsResolution extends DirectBean {

  /**
   * The resolved outputs.
   */
  @PropertyDefinition(validate = "notNull")
  private Collection<ResolvedOutput> _outputs = new ArrayList<>();

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code OutputsResolution}.
   * @return the meta-bean, not null
   */
  public static OutputsResolution.Meta meta() {
    return OutputsResolution.Meta.INSTANCE;
  }

  static {
    MetaBean.register(OutputsResolution.Meta.INSTANCE);
  }

  @Override
  public OutputsResolution.Meta metaBean() {
    return OutputsResolution.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the resolved outputs.
   * @return the value of the property, not null
   */
  public Collection<ResolvedOutput> getOutputs() {
    return _outputs;
  }

  /**
   * Sets the resolved outputs.
   * @param outputs  the new value of the property, not null
   */
  public void setOutputs(Collection<ResolvedOutput> outputs) {
    JodaBeanUtils.notNull(outputs, "outputs");
    this._outputs = outputs;
  }

  /**
   * Gets the the {@code outputs} property.
   * @return the property, not null
   */
  public final Property<Collection<ResolvedOutput>> outputs() {
    return metaBean().outputs().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public OutputsResolution clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      OutputsResolution other = (OutputsResolution) obj;
      return JodaBeanUtils.equal(getOutputs(), other.getOutputs());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getOutputs());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("OutputsResolution{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("outputs").append('=').append(JodaBeanUtils.toString(getOutputs())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code OutputsResolution}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code outputs} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Collection<ResolvedOutput>> _outputs = DirectMetaProperty.ofReadWrite(
        this, "outputs", OutputsResolution.class, (Class) Collection.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "outputs");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1106114670:  // outputs
          return _outputs;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends OutputsResolution> builder() {
      return new DirectBeanBuilder<>(new OutputsResolution());
    }

    @Override
    public Class<? extends OutputsResolution> beanType() {
      return OutputsResolution.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code outputs} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Collection<ResolvedOutput>> outputs() {
      return _outputs;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1106114670:  // outputs
          return ((OutputsResolution) bean).getOutputs();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1106114670:  // outputs
          ((OutputsResolution) bean).setOutputs((Collection<ResolvedOutput>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((OutputsResolution) bean)._outputs, "outputs");
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
