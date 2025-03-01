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
import com.opengamma.component.factory.ComponentInfoAttributes;
import com.opengamma.id.ObjectIdSupplier;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.master.security.impl.DataSecurityMasterResource;
import com.opengamma.master.security.impl.InMemorySecurityMaster;
import com.opengamma.master.security.impl.RemoteSecurityMaster;

/**
 * Component factory for an in-memory security master.
 */
@BeanDefinition
public class InMemorySecurityMasterComponentFactory extends AbstractComponentFactory {

  /**
   * The classifier that the factory should publish under.
   */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;
  /**
   * The flag determining whether the component should be published by REST (default true).
   */
  @PropertyDefinition
  private boolean _publishRest = true;
  /**
   * Optional scheme to use for unique and object ids on this master.  If not set, the default will be used.
   */
  @PropertyDefinition
  private String _idScheme;

  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) {
    final SecurityMaster master;
    if (_idScheme == null) {
      master = new InMemorySecurityMaster();
    } else {
      master = new InMemorySecurityMaster(new ObjectIdSupplier(_idScheme));
    }
    final ComponentInfo info = new ComponentInfo(SecurityMaster.class, getClassifier());
    info.addAttribute(ComponentInfoAttributes.LEVEL, 1);
    if (isPublishRest()) {
      info.addAttribute(ComponentInfoAttributes.REMOTE_CLIENT_JAVA, RemoteSecurityMaster.class);
    }
    if (_idScheme == null) {
      info.addAttribute(ComponentInfoAttributes.UNIQUE_ID_SCHEME, InMemorySecurityMaster.DEFAULT_OID_SCHEME);
    } else {
      info.addAttribute(ComponentInfoAttributes.UNIQUE_ID_SCHEME, _idScheme);
    }
    repo.registerComponent(info, master);

    if (isPublishRest()) {
      repo.getRestComponents().publish(info, new DataSecurityMasterResource(master));
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code InMemorySecurityMasterComponentFactory}.
   * @return the meta-bean, not null
   */
  public static InMemorySecurityMasterComponentFactory.Meta meta() {
    return InMemorySecurityMasterComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(InMemorySecurityMasterComponentFactory.Meta.INSTANCE);
  }

  @Override
  public InMemorySecurityMasterComponentFactory.Meta metaBean() {
    return InMemorySecurityMasterComponentFactory.Meta.INSTANCE;
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
  /**
   * Gets optional scheme to use for unique and object ids on this master.  If not set, the default will be used.
   * @return the value of the property
   */
  public String getIdScheme() {
    return _idScheme;
  }

  /**
   * Sets optional scheme to use for unique and object ids on this master.  If not set, the default will be used.
   * @param idScheme  the new value of the property
   */
  public void setIdScheme(String idScheme) {
    this._idScheme = idScheme;
  }

  /**
   * Gets the the {@code idScheme} property.
   * @return the property, not null
   */
  public final Property<String> idScheme() {
    return metaBean().idScheme().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public InMemorySecurityMasterComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      InMemorySecurityMasterComponentFactory other = (InMemorySecurityMasterComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          (isPublishRest() == other.isPublishRest()) &&
          JodaBeanUtils.equal(getIdScheme(), other.getIdScheme()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash = hash * 31 + JodaBeanUtils.hashCode(isPublishRest());
    hash = hash * 31 + JodaBeanUtils.hashCode(getIdScheme());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("InMemorySecurityMasterComponentFactory{");
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
    buf.append("idScheme").append('=').append(JodaBeanUtils.toString(getIdScheme())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code InMemorySecurityMasterComponentFactory}.
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
        this, "classifier", InMemorySecurityMasterComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code publishRest} property.
     */
    private final MetaProperty<Boolean> _publishRest = DirectMetaProperty.ofReadWrite(
        this, "publishRest", InMemorySecurityMasterComponentFactory.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code idScheme} property.
     */
    private final MetaProperty<String> _idScheme = DirectMetaProperty.ofReadWrite(
        this, "idScheme", InMemorySecurityMasterComponentFactory.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "publishRest",
        "idScheme");

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
        case -661606752:  // idScheme
          return _idScheme;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends InMemorySecurityMasterComponentFactory> builder() {
      return new DirectBeanBuilder<InMemorySecurityMasterComponentFactory>(new InMemorySecurityMasterComponentFactory());
    }

    @Override
    public Class<? extends InMemorySecurityMasterComponentFactory> beanType() {
      return InMemorySecurityMasterComponentFactory.class;
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

    /**
     * The meta-property for the {@code idScheme} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> idScheme() {
      return _idScheme;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return ((InMemorySecurityMasterComponentFactory) bean).getClassifier();
        case -614707837:  // publishRest
          return ((InMemorySecurityMasterComponentFactory) bean).isPublishRest();
        case -661606752:  // idScheme
          return ((InMemorySecurityMasterComponentFactory) bean).getIdScheme();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          ((InMemorySecurityMasterComponentFactory) bean).setClassifier((String) newValue);
          return;
        case -614707837:  // publishRest
          ((InMemorySecurityMasterComponentFactory) bean).setPublishRest((Boolean) newValue);
          return;
        case -661606752:  // idScheme
          ((InMemorySecurityMasterComponentFactory) bean).setIdScheme((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((InMemorySecurityMasterComponentFactory) bean)._classifier, "classifier");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
