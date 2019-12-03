/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.provider;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.opengamma.component.ComponentServer;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.component.factory.ComponentInfoAttributes;
import com.opengamma.component.rest.RemoteComponentServer;
import com.opengamma.provider.permission.PermissionCheckProvider;
import com.opengamma.provider.permission.impl.ProviderBasedPermissionResolver;
import com.opengamma.util.ReflectionUtils;
import com.opengamma.util.auth.AuthUtils;

/**
 * Component factory for accessing remote providers from the local machine.
 */
@BeanDefinition
public class RemoteProvidersComponentFactory extends AbstractComponentFactory {

  /**
   * The remote URI.
   */
  @PropertyDefinition(validate = "notNull")
  private URI _baseUri;
  /**
   * The flag determining whether the component should be published by REST (default true).
   */
  @PropertyDefinition
  private boolean _publishRest = true;

  //-------------------------------------------------------------------------
  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) {
    final RemoteComponentServer remote = new RemoteComponentServer(_baseUri);
    final ComponentServer server = remote.getComponentServer();
    for (final ComponentInfo info : server.getComponentInfos()) {
      initComponent(repo, info);
    }
  }

  /**
   * Initialize the remote component.
   *
   * @param repo  the local repository, not null
   * @param info  the remote information, not null
   */
  protected void initComponent(final ComponentRepository repo, final ComponentInfo info) {
    final URI componentUri = info.getUri();
    if (info.getAttributes().containsKey(ComponentInfoAttributes.REMOTE_CLIENT_JAVA)
        && info.getAttribute(ComponentInfoAttributes.REMOTE_CLIENT_JAVA).endsWith("Provider")) {
      final String remoteTypeStr = info.getAttribute(ComponentInfoAttributes.REMOTE_CLIENT_JAVA);
      final Class<?> remoteType = ReflectionUtils.loadClass(remoteTypeStr);
      final Constructor<?> con = ReflectionUtils.findConstructor(remoteType, URI.class);
      final Object target = ReflectionUtils.newInstance(con, componentUri);
      repo.registerComponent(info, target);
      if (isPublishRest()) {
        repo.getRestComponents().republish(info);
      }
      if (info.getType() == PermissionCheckProvider.class) {
        connectPermissionCheckProvider(repo, info, (PermissionCheckProvider) target);
      }
    }
  }

  /**
   * Connect the permission check provider to the local authorization system.
   *
   * @param repo  the local repository, not null
   * @param info  the remote information, not null
   * @param provider  the remote provider, not null
   */
  protected void connectPermissionCheckProvider(final ComponentRepository repo, final ComponentInfo info, final PermissionCheckProvider provider) {
    if (!AuthUtils.isPermissive() && info.getAttributes().containsKey(ComponentInfoAttributes.ACCEPTED_TYPES)) {
      final String[] permissionPrefixes = StringUtils.split(info.getAttribute(ComponentInfoAttributes.ACCEPTED_TYPES), ',');
      for (final String permissionPrefix : permissionPrefixes) {
        AuthUtils.getPermissionResolver().register(
            new ProviderBasedPermissionResolver(permissionPrefix, provider));
      }
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code RemoteProvidersComponentFactory}.
   * @return the meta-bean, not null
   */
  public static RemoteProvidersComponentFactory.Meta meta() {
    return RemoteProvidersComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(RemoteProvidersComponentFactory.Meta.INSTANCE);
  }

  @Override
  public RemoteProvidersComponentFactory.Meta metaBean() {
    return RemoteProvidersComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the remote URI.
   * @return the value of the property, not null
   */
  public URI getBaseUri() {
    return _baseUri;
  }

  /**
   * Sets the remote URI.
   * @param baseUri  the new value of the property, not null
   */
  public void setBaseUri(URI baseUri) {
    JodaBeanUtils.notNull(baseUri, "baseUri");
    this._baseUri = baseUri;
  }

  /**
   * Gets the the {@code baseUri} property.
   * @return the property, not null
   */
  public final Property<URI> baseUri() {
    return metaBean().baseUri().createProperty(this);
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
  public RemoteProvidersComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      RemoteProvidersComponentFactory other = (RemoteProvidersComponentFactory) obj;
      return JodaBeanUtils.equal(getBaseUri(), other.getBaseUri()) &&
          (isPublishRest() == other.isPublishRest()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getBaseUri());
    hash = hash * 31 + JodaBeanUtils.hashCode(isPublishRest());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("RemoteProvidersComponentFactory{");
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
    buf.append("baseUri").append('=').append(JodaBeanUtils.toString(getBaseUri())).append(',').append(' ');
    buf.append("publishRest").append('=').append(JodaBeanUtils.toString(isPublishRest())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code RemoteProvidersComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code baseUri} property.
     */
    private final MetaProperty<URI> _baseUri = DirectMetaProperty.ofReadWrite(
        this, "baseUri", RemoteProvidersComponentFactory.class, URI.class);
    /**
     * The meta-property for the {@code publishRest} property.
     */
    private final MetaProperty<Boolean> _publishRest = DirectMetaProperty.ofReadWrite(
        this, "publishRest", RemoteProvidersComponentFactory.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "baseUri",
        "publishRest");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -332625701:  // baseUri
          return _baseUri;
        case -614707837:  // publishRest
          return _publishRest;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends RemoteProvidersComponentFactory> builder() {
      return new DirectBeanBuilder<RemoteProvidersComponentFactory>(new RemoteProvidersComponentFactory());
    }

    @Override
    public Class<? extends RemoteProvidersComponentFactory> beanType() {
      return RemoteProvidersComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code baseUri} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<URI> baseUri() {
      return _baseUri;
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
        case -332625701:  // baseUri
          return ((RemoteProvidersComponentFactory) bean).getBaseUri();
        case -614707837:  // publishRest
          return ((RemoteProvidersComponentFactory) bean).isPublishRest();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -332625701:  // baseUri
          ((RemoteProvidersComponentFactory) bean).setBaseUri((URI) newValue);
          return;
        case -614707837:  // publishRest
          ((RemoteProvidersComponentFactory) bean).setPublishRest((Boolean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((RemoteProvidersComponentFactory) bean)._baseUri, "baseUri");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
