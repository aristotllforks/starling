/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.master;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.component.factory.ComponentInfoAttributes;
import com.opengamma.master.portfolio.PortfolioDocument;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.portfolio.impl.DataPortfolioMasterResource;
import com.opengamma.master.portfolio.impl.DynamicDelegatingPortfolioMaster;
import com.opengamma.master.portfolio.impl.RemotePortfolioMaster;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Component factory for delegating master that maintains a map of delegate masters.
 * <p>
 * Register as both a generic PortfolioMaster as well as the concrete type DynamicDelegatingPortfolioMasterComponentFactory, to allow command processor to
 * access out of band methods like: {@link DynamicDelegatingPortfolioMaster#register(String, PortfolioMaster)},
 * {@link DynamicDelegatingPortfolioMaster#deregister(String)} and {@link DynamicDelegatingPortfolioMaster#add(String, PortfolioDocument)}
 */
@BeanDefinition
public class DynamicDelegatingPortfolioMasterComponentFactory extends AbstractComponentFactory {

  /** The classifier that the factory should publish under. */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;
  /** The flag determining whether the component should be published by REST (default true). */
  @PropertyDefinition
  private boolean _publishRest = true;

  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) {

    final DynamicDelegatingPortfolioMaster master = new DynamicDelegatingPortfolioMaster();

    final ComponentInfo info = new ComponentInfo(PortfolioMaster.class, getClassifier());
    info.addAttribute(ComponentInfoAttributes.LEVEL, 1);
    if (isPublishRest()) {
      info.addAttribute(ComponentInfoAttributes.REMOTE_CLIENT_JAVA, RemotePortfolioMaster.class);
    }
    repo.registerComponent(info, master);

    if (isPublishRest()) {
      repo.getRestComponents().publish(info, new DataPortfolioMasterResource(master));
    }

    final ComponentInfo concreteInfo = new ComponentInfo(DynamicDelegatingPortfolioMaster.class, getClassifier());
    concreteInfo.addAttribute(ComponentInfoAttributes.LEVEL, 1);

    repo.registerComponent(concreteInfo, master);
  }


  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code DynamicDelegatingPortfolioMasterComponentFactory}.
   * @return the meta-bean, not null
   */
  public static DynamicDelegatingPortfolioMasterComponentFactory.Meta meta() {
    return DynamicDelegatingPortfolioMasterComponentFactory.Meta.INSTANCE;
  }

  static {
    MetaBean.register(DynamicDelegatingPortfolioMasterComponentFactory.Meta.INSTANCE);
  }

  @Override
  public DynamicDelegatingPortfolioMasterComponentFactory.Meta metaBean() {
    return DynamicDelegatingPortfolioMasterComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier that the factory should publish under.
   * @return the value of the property, not null
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier that the factory should publish under.
   * @param classifier  the new value of the property, not null
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notNull(classifier, "classifier");
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
   * Gets the flag determining whether the component should be published by REST (default true).
   * @return the value of the property
   */
  public boolean isPublishRest() {
    return _publishRest;
  }

  /**
   * Sets the flag determining whether the component should be published by REST (default true).
   * @param publishRest  the new value of the property
   */
  public void setPublishRest(boolean publishRest) {
    this._publishRest = publishRest;
  }

  /**
   * Gets the the {@code publishRest} property.
   * @return the property, not null
   */
  public final Property<Boolean> publishRest() {
    return metaBean().publishRest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public DynamicDelegatingPortfolioMasterComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DynamicDelegatingPortfolioMasterComponentFactory other = (DynamicDelegatingPortfolioMasterComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          (isPublishRest() == other.isPublishRest()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash = hash * 31 + JodaBeanUtils.hashCode(isPublishRest());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("DynamicDelegatingPortfolioMasterComponentFactory{");
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
    buf.append("publishRest").append('=').append(JodaBeanUtils.toString(isPublishRest())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code DynamicDelegatingPortfolioMasterComponentFactory}.
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
        this, "classifier", DynamicDelegatingPortfolioMasterComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code publishRest} property.
     */
    private final MetaProperty<Boolean> _publishRest = DirectMetaProperty.ofReadWrite(
        this, "publishRest", DynamicDelegatingPortfolioMasterComponentFactory.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "publishRest");

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
        case -614707837:  // publishRest
          return _publishRest;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends DynamicDelegatingPortfolioMasterComponentFactory> builder() {
      return new DirectBeanBuilder<>(new DynamicDelegatingPortfolioMasterComponentFactory());
    }

    @Override
    public Class<? extends DynamicDelegatingPortfolioMasterComponentFactory> beanType() {
      return DynamicDelegatingPortfolioMasterComponentFactory.class;
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
     * The meta-property for the {@code publishRest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> publishRest() {
      return _publishRest;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return ((DynamicDelegatingPortfolioMasterComponentFactory) bean).getClassifier();
        case -614707837:  // publishRest
          return ((DynamicDelegatingPortfolioMasterComponentFactory) bean).isPublishRest();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          ((DynamicDelegatingPortfolioMasterComponentFactory) bean).setClassifier((String) newValue);
          return;
        case -614707837:  // publishRest
          ((DynamicDelegatingPortfolioMasterComponentFactory) bean).setPublishRest((Boolean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((DynamicDelegatingPortfolioMasterComponentFactory) bean)._classifier, "classifier");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
