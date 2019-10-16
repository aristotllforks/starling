/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * Mock factory.
 */
@BeanDefinition
public class MockComponentFactoryOne implements Bean, ComponentFactory {

  @PropertyDefinition(validate = "notNull")
  private String alpha;
  @PropertyDefinition(validate = "notNull")
  private String beta;
  @PropertyDefinition
  private String gamma;

  // -------------------------------------------------------------------------
  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) throws Exception {
    final ComponentInfo info = new ComponentInfo(MockComponent.class, "one");
    repo.registerComponent(info, new MockComponent());
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code MockComponentFactoryOne}.
   * @return the meta-bean, not null
   */
  public static MockComponentFactoryOne.Meta meta() {
    return MockComponentFactoryOne.Meta.INSTANCE;
  }

  static {
    MetaBean.register(MockComponentFactoryOne.Meta.INSTANCE);
  }

  @Override
  public MockComponentFactoryOne.Meta metaBean() {
    return MockComponentFactoryOne.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the alpha.
   * @return the value of the property, not null
   */
  public String getAlpha() {
    return alpha;
  }

  /**
   * Sets the alpha.
   * @param alpha  the new value of the property, not null
   */
  public void setAlpha(String alpha) {
    JodaBeanUtils.notNull(alpha, "alpha");
    this.alpha = alpha;
  }

  /**
   * Gets the the {@code alpha} property.
   * @return the property, not null
   */
  public final Property<String> alpha() {
    return metaBean().alpha().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the beta.
   * @return the value of the property, not null
   */
  public String getBeta() {
    return beta;
  }

  /**
   * Sets the beta.
   * @param beta  the new value of the property, not null
   */
  public void setBeta(String beta) {
    JodaBeanUtils.notNull(beta, "beta");
    this.beta = beta;
  }

  /**
   * Gets the the {@code beta} property.
   * @return the property, not null
   */
  public final Property<String> beta() {
    return metaBean().beta().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the gamma.
   * @return the value of the property
   */
  public String getGamma() {
    return gamma;
  }

  /**
   * Sets the gamma.
   * @param gamma  the new value of the property
   */
  public void setGamma(String gamma) {
    this.gamma = gamma;
  }

  /**
   * Gets the the {@code gamma} property.
   * @return the property, not null
   */
  public final Property<String> gamma() {
    return metaBean().gamma().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public MockComponentFactoryOne clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MockComponentFactoryOne other = (MockComponentFactoryOne) obj;
      return JodaBeanUtils.equal(getAlpha(), other.getAlpha()) &&
          JodaBeanUtils.equal(getBeta(), other.getBeta()) &&
          JodaBeanUtils.equal(getGamma(), other.getGamma());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getAlpha());
    hash = hash * 31 + JodaBeanUtils.hashCode(getBeta());
    hash = hash * 31 + JodaBeanUtils.hashCode(getGamma());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("MockComponentFactoryOne{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("alpha").append('=').append(JodaBeanUtils.toString(getAlpha())).append(',').append(' ');
    buf.append("beta").append('=').append(JodaBeanUtils.toString(getBeta())).append(',').append(' ');
    buf.append("gamma").append('=').append(JodaBeanUtils.toString(getGamma())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MockComponentFactoryOne}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code alpha} property.
     */
    private final MetaProperty<String> _alpha = DirectMetaProperty.ofReadWrite(
        this, "alpha", MockComponentFactoryOne.class, String.class);
    /**
     * The meta-property for the {@code beta} property.
     */
    private final MetaProperty<String> _beta = DirectMetaProperty.ofReadWrite(
        this, "beta", MockComponentFactoryOne.class, String.class);
    /**
     * The meta-property for the {@code gamma} property.
     */
    private final MetaProperty<String> _gamma = DirectMetaProperty.ofReadWrite(
        this, "gamma", MockComponentFactoryOne.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "alpha",
        "beta",
        "gamma");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 92909918:  // alpha
          return _alpha;
        case 3020272:  // beta
          return _beta;
        case 98120615:  // gamma
          return _gamma;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MockComponentFactoryOne> builder() {
      return new DirectBeanBuilder<>(new MockComponentFactoryOne());
    }

    @Override
    public Class<? extends MockComponentFactoryOne> beanType() {
      return MockComponentFactoryOne.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code alpha} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> alpha() {
      return _alpha;
    }

    /**
     * The meta-property for the {@code beta} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> beta() {
      return _beta;
    }

    /**
     * The meta-property for the {@code gamma} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> gamma() {
      return _gamma;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 92909918:  // alpha
          return ((MockComponentFactoryOne) bean).getAlpha();
        case 3020272:  // beta
          return ((MockComponentFactoryOne) bean).getBeta();
        case 98120615:  // gamma
          return ((MockComponentFactoryOne) bean).getGamma();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 92909918:  // alpha
          ((MockComponentFactoryOne) bean).setAlpha((String) newValue);
          return;
        case 3020272:  // beta
          ((MockComponentFactoryOne) bean).setBeta((String) newValue);
          return;
        case 98120615:  // gamma
          ((MockComponentFactoryOne) bean).setGamma((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((MockComponentFactoryOne) bean).alpha, "alpha");
      JodaBeanUtils.notNull(((MockComponentFactoryOne) bean).beta, "beta");
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
