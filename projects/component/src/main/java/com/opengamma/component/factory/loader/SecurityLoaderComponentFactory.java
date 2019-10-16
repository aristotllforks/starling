/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.loader;

import java.util.List;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.Lists;
import com.opengamma.component.ComponentRepository;
import com.opengamma.financial.security.DefaultSecurityLoader;
import com.opengamma.master.security.SecurityLoader;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.provider.security.SecurityEnhancer;
import com.opengamma.provider.security.SecurityProvider;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Component factory providing the {@code SecurityLoader}.
 * <p>
 * This implementation uses {@link DefaultSecurityLoader}.
 */
@BeanDefinition
public class SecurityLoaderComponentFactory extends AbstractSecurityLoaderComponentFactory {

  /**
   * The security master.
   */
  @PropertyDefinition(validate = "notNull")
  private SecurityMaster _securityMaster;
  /**
   * The security provider.
   */
  @PropertyDefinition(validate = "notNull")
  private SecurityProvider _securityProvider;
  /**
   * The first security enhancer.
   */
  @PropertyDefinition
  private SecurityEnhancer _securityEnhancer1;
  /**
   * The second security enhancer.
   */
  @PropertyDefinition
  private SecurityEnhancer _securityEnhancer2;
  /**
   * The third security enhancer.
   */
  @PropertyDefinition
  private SecurityEnhancer _securityEnhancer3;

  //-------------------------------------------------------------------------
  /**
   * Creates the loader, without registering it.
   * <p>
   * This implementation uses {@link DefaultSecurityLoader}.
   *
   * @param repo  the component repository, only used to register secondary items like lifecycle, not null
   * @return the loader, not null
   */
  @Override
  protected SecurityLoader createSecurityLoader(final ComponentRepository repo) {
    final List<SecurityEnhancer> enhancers = Lists.newArrayList();
    if (getSecurityEnhancer1() != null) {
      enhancers.add(getSecurityEnhancer1());
    }
    if (getSecurityEnhancer2() != null) {
      enhancers.add(getSecurityEnhancer2());
    }
    if (getSecurityEnhancer3() != null) {
      enhancers.add(getSecurityEnhancer3());
    }
    return new DefaultSecurityLoader(getSecurityMaster(), getSecurityProvider(), enhancers);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code SecurityLoaderComponentFactory}.
   * @return the meta-bean, not null
   */
  public static SecurityLoaderComponentFactory.Meta meta() {
    return SecurityLoaderComponentFactory.Meta.INSTANCE;
  }

  static {
    MetaBean.register(SecurityLoaderComponentFactory.Meta.INSTANCE);
  }

  @Override
  public SecurityLoaderComponentFactory.Meta metaBean() {
    return SecurityLoaderComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security master.
   * @return the value of the property, not null
   */
  public SecurityMaster getSecurityMaster() {
    return _securityMaster;
  }

  /**
   * Sets the security master.
   * @param securityMaster  the new value of the property, not null
   */
  public void setSecurityMaster(SecurityMaster securityMaster) {
    JodaBeanUtils.notNull(securityMaster, "securityMaster");
    this._securityMaster = securityMaster;
  }

  /**
   * Gets the the {@code securityMaster} property.
   * @return the property, not null
   */
  public final Property<SecurityMaster> securityMaster() {
    return metaBean().securityMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security provider.
   * @return the value of the property, not null
   */
  public SecurityProvider getSecurityProvider() {
    return _securityProvider;
  }

  /**
   * Sets the security provider.
   * @param securityProvider  the new value of the property, not null
   */
  public void setSecurityProvider(SecurityProvider securityProvider) {
    JodaBeanUtils.notNull(securityProvider, "securityProvider");
    this._securityProvider = securityProvider;
  }

  /**
   * Gets the the {@code securityProvider} property.
   * @return the property, not null
   */
  public final Property<SecurityProvider> securityProvider() {
    return metaBean().securityProvider().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the first security enhancer.
   * @return the value of the property
   */
  public SecurityEnhancer getSecurityEnhancer1() {
    return _securityEnhancer1;
  }

  /**
   * Sets the first security enhancer.
   * @param securityEnhancer1  the new value of the property
   */
  public void setSecurityEnhancer1(SecurityEnhancer securityEnhancer1) {
    this._securityEnhancer1 = securityEnhancer1;
  }

  /**
   * Gets the the {@code securityEnhancer1} property.
   * @return the property, not null
   */
  public final Property<SecurityEnhancer> securityEnhancer1() {
    return metaBean().securityEnhancer1().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the second security enhancer.
   * @return the value of the property
   */
  public SecurityEnhancer getSecurityEnhancer2() {
    return _securityEnhancer2;
  }

  /**
   * Sets the second security enhancer.
   * @param securityEnhancer2  the new value of the property
   */
  public void setSecurityEnhancer2(SecurityEnhancer securityEnhancer2) {
    this._securityEnhancer2 = securityEnhancer2;
  }

  /**
   * Gets the the {@code securityEnhancer2} property.
   * @return the property, not null
   */
  public final Property<SecurityEnhancer> securityEnhancer2() {
    return metaBean().securityEnhancer2().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the third security enhancer.
   * @return the value of the property
   */
  public SecurityEnhancer getSecurityEnhancer3() {
    return _securityEnhancer3;
  }

  /**
   * Sets the third security enhancer.
   * @param securityEnhancer3  the new value of the property
   */
  public void setSecurityEnhancer3(SecurityEnhancer securityEnhancer3) {
    this._securityEnhancer3 = securityEnhancer3;
  }

  /**
   * Gets the the {@code securityEnhancer3} property.
   * @return the property, not null
   */
  public final Property<SecurityEnhancer> securityEnhancer3() {
    return metaBean().securityEnhancer3().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public SecurityLoaderComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SecurityLoaderComponentFactory other = (SecurityLoaderComponentFactory) obj;
      return JodaBeanUtils.equal(getSecurityMaster(), other.getSecurityMaster()) &&
          JodaBeanUtils.equal(getSecurityProvider(), other.getSecurityProvider()) &&
          JodaBeanUtils.equal(getSecurityEnhancer1(), other.getSecurityEnhancer1()) &&
          JodaBeanUtils.equal(getSecurityEnhancer2(), other.getSecurityEnhancer2()) &&
          JodaBeanUtils.equal(getSecurityEnhancer3(), other.getSecurityEnhancer3()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityProvider());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityEnhancer1());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityEnhancer2());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityEnhancer3());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("SecurityLoaderComponentFactory{");
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
    buf.append("securityMaster").append('=').append(JodaBeanUtils.toString(getSecurityMaster())).append(',').append(' ');
    buf.append("securityProvider").append('=').append(JodaBeanUtils.toString(getSecurityProvider())).append(',').append(' ');
    buf.append("securityEnhancer1").append('=').append(JodaBeanUtils.toString(getSecurityEnhancer1())).append(',').append(' ');
    buf.append("securityEnhancer2").append('=').append(JodaBeanUtils.toString(getSecurityEnhancer2())).append(',').append(' ');
    buf.append("securityEnhancer3").append('=').append(JodaBeanUtils.toString(getSecurityEnhancer3())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SecurityLoaderComponentFactory}.
   */
  public static class Meta extends AbstractSecurityLoaderComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code securityMaster} property.
     */
    private final MetaProperty<SecurityMaster> _securityMaster = DirectMetaProperty.ofReadWrite(
        this, "securityMaster", SecurityLoaderComponentFactory.class, SecurityMaster.class);
    /**
     * The meta-property for the {@code securityProvider} property.
     */
    private final MetaProperty<SecurityProvider> _securityProvider = DirectMetaProperty.ofReadWrite(
        this, "securityProvider", SecurityLoaderComponentFactory.class, SecurityProvider.class);
    /**
     * The meta-property for the {@code securityEnhancer1} property.
     */
    private final MetaProperty<SecurityEnhancer> _securityEnhancer1 = DirectMetaProperty.ofReadWrite(
        this, "securityEnhancer1", SecurityLoaderComponentFactory.class, SecurityEnhancer.class);
    /**
     * The meta-property for the {@code securityEnhancer2} property.
     */
    private final MetaProperty<SecurityEnhancer> _securityEnhancer2 = DirectMetaProperty.ofReadWrite(
        this, "securityEnhancer2", SecurityLoaderComponentFactory.class, SecurityEnhancer.class);
    /**
     * The meta-property for the {@code securityEnhancer3} property.
     */
    private final MetaProperty<SecurityEnhancer> _securityEnhancer3 = DirectMetaProperty.ofReadWrite(
        this, "securityEnhancer3", SecurityLoaderComponentFactory.class, SecurityEnhancer.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "securityMaster",
        "securityProvider",
        "securityEnhancer1",
        "securityEnhancer2",
        "securityEnhancer3");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -887218750:  // securityMaster
          return _securityMaster;
        case 809869649:  // securityProvider
          return _securityProvider;
        case 1142795725:  // securityEnhancer1
          return _securityEnhancer1;
        case 1142795726:  // securityEnhancer2
          return _securityEnhancer2;
        case 1142795727:  // securityEnhancer3
          return _securityEnhancer3;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SecurityLoaderComponentFactory> builder() {
      return new DirectBeanBuilder<>(new SecurityLoaderComponentFactory());
    }

    @Override
    public Class<? extends SecurityLoaderComponentFactory> beanType() {
      return SecurityLoaderComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code securityMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityMaster> securityMaster() {
      return _securityMaster;
    }

    /**
     * The meta-property for the {@code securityProvider} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityProvider> securityProvider() {
      return _securityProvider;
    }

    /**
     * The meta-property for the {@code securityEnhancer1} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityEnhancer> securityEnhancer1() {
      return _securityEnhancer1;
    }

    /**
     * The meta-property for the {@code securityEnhancer2} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityEnhancer> securityEnhancer2() {
      return _securityEnhancer2;
    }

    /**
     * The meta-property for the {@code securityEnhancer3} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityEnhancer> securityEnhancer3() {
      return _securityEnhancer3;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -887218750:  // securityMaster
          return ((SecurityLoaderComponentFactory) bean).getSecurityMaster();
        case 809869649:  // securityProvider
          return ((SecurityLoaderComponentFactory) bean).getSecurityProvider();
        case 1142795725:  // securityEnhancer1
          return ((SecurityLoaderComponentFactory) bean).getSecurityEnhancer1();
        case 1142795726:  // securityEnhancer2
          return ((SecurityLoaderComponentFactory) bean).getSecurityEnhancer2();
        case 1142795727:  // securityEnhancer3
          return ((SecurityLoaderComponentFactory) bean).getSecurityEnhancer3();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -887218750:  // securityMaster
          ((SecurityLoaderComponentFactory) bean).setSecurityMaster((SecurityMaster) newValue);
          return;
        case 809869649:  // securityProvider
          ((SecurityLoaderComponentFactory) bean).setSecurityProvider((SecurityProvider) newValue);
          return;
        case 1142795725:  // securityEnhancer1
          ((SecurityLoaderComponentFactory) bean).setSecurityEnhancer1((SecurityEnhancer) newValue);
          return;
        case 1142795726:  // securityEnhancer2
          ((SecurityLoaderComponentFactory) bean).setSecurityEnhancer2((SecurityEnhancer) newValue);
          return;
        case 1142795727:  // securityEnhancer3
          ((SecurityLoaderComponentFactory) bean).setSecurityEnhancer3((SecurityEnhancer) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((SecurityLoaderComponentFactory) bean)._securityMaster, "securityMaster");
      JodaBeanUtils.notNull(((SecurityLoaderComponentFactory) bean)._securityProvider, "securityProvider");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
