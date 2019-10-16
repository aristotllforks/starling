/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web;

import java.util.Map;

import javax.ws.rs.core.UriInfo;

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
 * Data class for per-request web-based bundles.
 */
@BeanDefinition
public class WebPerRequestData implements Bean {

  /**
   * The JSR-311 URI information.
   */
  @PropertyDefinition
  private UriInfo _uriInfo;

  /**
   * Creates an instance.
   */
  public WebPerRequestData() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code WebPerRequestData}.
   * @return the meta-bean, not null
   */
  public static WebPerRequestData.Meta meta() {
    return WebPerRequestData.Meta.INSTANCE;
  }

  static {
    MetaBean.register(WebPerRequestData.Meta.INSTANCE);
  }

  @Override
  public WebPerRequestData.Meta metaBean() {
    return WebPerRequestData.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the JSR-311 URI information.
   * @return the value of the property
   */
  public UriInfo getUriInfo() {
    return _uriInfo;
  }

  /**
   * Sets the JSR-311 URI information.
   * @param uriInfo  the new value of the property
   */
  public void setUriInfo(UriInfo uriInfo) {
    this._uriInfo = uriInfo;
  }

  /**
   * Gets the the {@code uriInfo} property.
   * @return the property, not null
   */
  public final Property<UriInfo> uriInfo() {
    return metaBean().uriInfo().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public WebPerRequestData clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      WebPerRequestData other = (WebPerRequestData) obj;
      return JodaBeanUtils.equal(getUriInfo(), other.getUriInfo());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getUriInfo());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("WebPerRequestData{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("uriInfo").append('=').append(JodaBeanUtils.toString(getUriInfo())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code WebPerRequestData}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uriInfo} property.
     */
    private final MetaProperty<UriInfo> _uriInfo = DirectMetaProperty.ofReadWrite(
        this, "uriInfo", WebPerRequestData.class, UriInfo.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "uriInfo");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -173275078:  // uriInfo
          return _uriInfo;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends WebPerRequestData> builder() {
      return new DirectBeanBuilder<>(new WebPerRequestData());
    }

    @Override
    public Class<? extends WebPerRequestData> beanType() {
      return WebPerRequestData.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code uriInfo} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UriInfo> uriInfo() {
      return _uriInfo;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -173275078:  // uriInfo
          return ((WebPerRequestData) bean).getUriInfo();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -173275078:  // uriInfo
          ((WebPerRequestData) bean).setUriInfo((UriInfo) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
