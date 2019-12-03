/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.cashflow;

import java.util.LinkedHashMap;
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

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.core.position.PositionSource;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.engine.view.ViewProcessor;
import com.opengamma.master.config.ConfigMaster;

/**
 *
 */
@BeanDefinition
public class PaymentServiceComponentFactory extends AbstractComponentFactory {

  /**
   * The classifier under which to publish.
   */
  @PropertyDefinition
  private String _classifier;
  /**
   * The view processor.
   */
  @PropertyDefinition
  private ViewProcessor _viewProcessor;
  /**
   * The user config master.
   */
  @PropertyDefinition
  private ConfigMaster _userConfigMaster;
  /**
   * The position source.
   */
  @PropertyDefinition
  private PositionSource _positionSource;
  /**
   * The security source.
   */
  @PropertyDefinition
  private SecuritySource _securitySource;

  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) throws Exception {
    initPaymentService(repo);
  }

  protected PaymentService initPaymentService(final ComponentRepository repo) {
    final PaymentService paymentService = new PaymentService(getViewProcessor(), getUserConfigMaster(), getPositionSource(), getSecuritySource());
    final ComponentInfo info = new ComponentInfo(PaymentService.class, getClassifier());
    repo.registerComponent(info, paymentService);
    return paymentService;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code PaymentServiceComponentFactory}.
   * @return the meta-bean, not null
   */
  public static PaymentServiceComponentFactory.Meta meta() {
    return PaymentServiceComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(PaymentServiceComponentFactory.Meta.INSTANCE);
  }

  @Override
  public PaymentServiceComponentFactory.Meta metaBean() {
    return PaymentServiceComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier under which to publish.
   * @return the value of the property
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier under which to publish.
   * @param classifier  the new value of the property
   */
  public void setClassifier(String classifier) {
    this._classifier = classifier;
  }

  /**
   * Gets the the {@code classifier} property.
   * @return the property, not null
   */
  public final Property<String> classifier() {
    return metaBean().classifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the view processor.
   * @return the value of the property
   */
  public ViewProcessor getViewProcessor() {
    return _viewProcessor;
  }

  /**
   * Sets the view processor.
   * @param viewProcessor  the new value of the property
   */
  public void setViewProcessor(ViewProcessor viewProcessor) {
    this._viewProcessor = viewProcessor;
  }

  /**
   * Gets the the {@code viewProcessor} property.
   * @return the property, not null
   */
  public final Property<ViewProcessor> viewProcessor() {
    return metaBean().viewProcessor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the user config master.
   * @return the value of the property
   */
  public ConfigMaster getUserConfigMaster() {
    return _userConfigMaster;
  }

  /**
   * Sets the user config master.
   * @param userConfigMaster  the new value of the property
   */
  public void setUserConfigMaster(ConfigMaster userConfigMaster) {
    this._userConfigMaster = userConfigMaster;
  }

  /**
   * Gets the the {@code userConfigMaster} property.
   * @return the property, not null
   */
  public final Property<ConfigMaster> userConfigMaster() {
    return metaBean().userConfigMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the position source.
   * @return the value of the property
   */
  public PositionSource getPositionSource() {
    return _positionSource;
  }

  /**
   * Sets the position source.
   * @param positionSource  the new value of the property
   */
  public void setPositionSource(PositionSource positionSource) {
    this._positionSource = positionSource;
  }

  /**
   * Gets the the {@code positionSource} property.
   * @return the property, not null
   */
  public final Property<PositionSource> positionSource() {
    return metaBean().positionSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security source.
   * @return the value of the property
   */
  public SecuritySource getSecuritySource() {
    return _securitySource;
  }

  /**
   * Sets the security source.
   * @param securitySource  the new value of the property
   */
  public void setSecuritySource(SecuritySource securitySource) {
    this._securitySource = securitySource;
  }

  /**
   * Gets the the {@code securitySource} property.
   * @return the property, not null
   */
  public final Property<SecuritySource> securitySource() {
    return metaBean().securitySource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public PaymentServiceComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      PaymentServiceComponentFactory other = (PaymentServiceComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(getViewProcessor(), other.getViewProcessor()) &&
          JodaBeanUtils.equal(getUserConfigMaster(), other.getUserConfigMaster()) &&
          JodaBeanUtils.equal(getPositionSource(), other.getPositionSource()) &&
          JodaBeanUtils.equal(getSecuritySource(), other.getSecuritySource()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash = hash * 31 + JodaBeanUtils.hashCode(getViewProcessor());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUserConfigMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPositionSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecuritySource());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("PaymentServiceComponentFactory{");
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
    buf.append("classifier").append('=').append(JodaBeanUtils.toString(getClassifier())).append(',').append(' ');
    buf.append("viewProcessor").append('=').append(JodaBeanUtils.toString(getViewProcessor())).append(',').append(' ');
    buf.append("userConfigMaster").append('=').append(JodaBeanUtils.toString(getUserConfigMaster())).append(',').append(' ');
    buf.append("positionSource").append('=').append(JodaBeanUtils.toString(getPositionSource())).append(',').append(' ');
    buf.append("securitySource").append('=').append(JodaBeanUtils.toString(getSecuritySource())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code PaymentServiceComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", PaymentServiceComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code viewProcessor} property.
     */
    private final MetaProperty<ViewProcessor> _viewProcessor = DirectMetaProperty.ofReadWrite(
        this, "viewProcessor", PaymentServiceComponentFactory.class, ViewProcessor.class);
    /**
     * The meta-property for the {@code userConfigMaster} property.
     */
    private final MetaProperty<ConfigMaster> _userConfigMaster = DirectMetaProperty.ofReadWrite(
        this, "userConfigMaster", PaymentServiceComponentFactory.class, ConfigMaster.class);
    /**
     * The meta-property for the {@code positionSource} property.
     */
    private final MetaProperty<PositionSource> _positionSource = DirectMetaProperty.ofReadWrite(
        this, "positionSource", PaymentServiceComponentFactory.class, PositionSource.class);
    /**
     * The meta-property for the {@code securitySource} property.
     */
    private final MetaProperty<SecuritySource> _securitySource = DirectMetaProperty.ofReadWrite(
        this, "securitySource", PaymentServiceComponentFactory.class, SecuritySource.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "viewProcessor",
        "userConfigMaster",
        "positionSource",
        "securitySource");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return _classifier;
        case -1697555603:  // viewProcessor
          return _viewProcessor;
        case -763459665:  // userConfigMaster
          return _userConfigMaster;
        case -1655657820:  // positionSource
          return _positionSource;
        case -702456965:  // securitySource
          return _securitySource;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends PaymentServiceComponentFactory> builder() {
      return new DirectBeanBuilder<PaymentServiceComponentFactory>(new PaymentServiceComponentFactory());
    }

    @Override
    public Class<? extends PaymentServiceComponentFactory> beanType() {
      return PaymentServiceComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code classifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> classifier() {
      return _classifier;
    }

    /**
     * The meta-property for the {@code viewProcessor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ViewProcessor> viewProcessor() {
      return _viewProcessor;
    }

    /**
     * The meta-property for the {@code userConfigMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ConfigMaster> userConfigMaster() {
      return _userConfigMaster;
    }

    /**
     * The meta-property for the {@code positionSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PositionSource> positionSource() {
      return _positionSource;
    }

    /**
     * The meta-property for the {@code securitySource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecuritySource> securitySource() {
      return _securitySource;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return ((PaymentServiceComponentFactory) bean).getClassifier();
        case -1697555603:  // viewProcessor
          return ((PaymentServiceComponentFactory) bean).getViewProcessor();
        case -763459665:  // userConfigMaster
          return ((PaymentServiceComponentFactory) bean).getUserConfigMaster();
        case -1655657820:  // positionSource
          return ((PaymentServiceComponentFactory) bean).getPositionSource();
        case -702456965:  // securitySource
          return ((PaymentServiceComponentFactory) bean).getSecuritySource();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          ((PaymentServiceComponentFactory) bean).setClassifier((String) newValue);
          return;
        case -1697555603:  // viewProcessor
          ((PaymentServiceComponentFactory) bean).setViewProcessor((ViewProcessor) newValue);
          return;
        case -763459665:  // userConfigMaster
          ((PaymentServiceComponentFactory) bean).setUserConfigMaster((ConfigMaster) newValue);
          return;
        case -1655657820:  // positionSource
          ((PaymentServiceComponentFactory) bean).setPositionSource((PositionSource) newValue);
          return;
        case -702456965:  // securitySource
          ((PaymentServiceComponentFactory) bean).setSecuritySource((SecuritySource) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
