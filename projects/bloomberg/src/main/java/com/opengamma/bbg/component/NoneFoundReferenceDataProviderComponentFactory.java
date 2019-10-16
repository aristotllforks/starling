/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg.component;

import java.util.LinkedHashMap;
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

import com.opengamma.bbg.referencedata.ReferenceDataProvider;
import com.opengamma.bbg.referencedata.impl.NoneFoundReferenceDataProvider;
import com.opengamma.component.ComponentFactory;
import com.opengamma.component.ComponentRepository;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 *
 */
@BeanDefinition
public class NoneFoundReferenceDataProviderComponentFactory extends DirectBean implements ComponentFactory {

  /**
   * The classifier that the factory should publish under.
   */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;

  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) throws Exception {
    repo.registerComponent(ReferenceDataProvider.class, _classifier, new NoneFoundReferenceDataProvider());
  }
  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code NoneFoundReferenceDataProviderComponentFactory}.
   * @return the meta-bean, not null
   */
  public static NoneFoundReferenceDataProviderComponentFactory.Meta meta() {
    return NoneFoundReferenceDataProviderComponentFactory.Meta.INSTANCE;
  }

  static {
    MetaBean.register(NoneFoundReferenceDataProviderComponentFactory.Meta.INSTANCE);
  }

  @Override
  public NoneFoundReferenceDataProviderComponentFactory.Meta metaBean() {
    return NoneFoundReferenceDataProviderComponentFactory.Meta.INSTANCE;
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
  @Override
  public NoneFoundReferenceDataProviderComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      NoneFoundReferenceDataProviderComponentFactory other = (NoneFoundReferenceDataProviderComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("NoneFoundReferenceDataProviderComponentFactory{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("classifier").append('=').append(JodaBeanUtils.toString(getClassifier())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code NoneFoundReferenceDataProviderComponentFactory}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", NoneFoundReferenceDataProviderComponentFactory.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "classifier");

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
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends NoneFoundReferenceDataProviderComponentFactory> builder() {
      return new DirectBeanBuilder<>(new NoneFoundReferenceDataProviderComponentFactory());
    }

    @Override
    public Class<? extends NoneFoundReferenceDataProviderComponentFactory> beanType() {
      return NoneFoundReferenceDataProviderComponentFactory.class;
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

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return ((NoneFoundReferenceDataProviderComponentFactory) bean).getClassifier();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          ((NoneFoundReferenceDataProviderComponentFactory) bean).setClassifier((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((NoneFoundReferenceDataProviderComponentFactory) bean)._classifier, "classifier");
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
