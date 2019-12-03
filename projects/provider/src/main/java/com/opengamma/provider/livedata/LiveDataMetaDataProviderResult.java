/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.provider.livedata;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.PublicSPI;

/**
 * Result from querying meta-data for a live data provider.
 * <p>
 * This class is mutable and not thread-safe.
 */
@PublicSPI
@BeanDefinition
public class LiveDataMetaDataProviderResult extends DirectBean {

  /**
   * The meta-data.
   */
  @PropertyDefinition(validate = "notNull")
  private LiveDataMetaData _metaData;

  /**
   * Creates an instance.
   */
  protected LiveDataMetaDataProviderResult() {
  }

  /**
   * Creates an instance.
   *
   * @param metaData  the meta data, not null
   */
  public LiveDataMetaDataProviderResult(final LiveDataMetaData metaData) {
    setMetaData(metaData);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code LiveDataMetaDataProviderResult}.
   * @return the meta-bean, not null
   */
  public static LiveDataMetaDataProviderResult.Meta meta() {
    return LiveDataMetaDataProviderResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(LiveDataMetaDataProviderResult.Meta.INSTANCE);
  }

  @Override
  public LiveDataMetaDataProviderResult.Meta metaBean() {
    return LiveDataMetaDataProviderResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the meta-data.
   * @return the value of the property, not null
   */
  public LiveDataMetaData getMetaData() {
    return _metaData;
  }

  /**
   * Sets the meta-data.
   * @param metaData  the new value of the property, not null
   */
  public void setMetaData(LiveDataMetaData metaData) {
    JodaBeanUtils.notNull(metaData, "metaData");
    this._metaData = metaData;
  }

  /**
   * Gets the the {@code metaData} property.
   * @return the property, not null
   */
  public final Property<LiveDataMetaData> metaData() {
    return metaBean().metaData().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public LiveDataMetaDataProviderResult clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      LiveDataMetaDataProviderResult other = (LiveDataMetaDataProviderResult) obj;
      return JodaBeanUtils.equal(getMetaData(), other.getMetaData());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getMetaData());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("LiveDataMetaDataProviderResult{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("metaData").append('=').append(JodaBeanUtils.toString(getMetaData())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code LiveDataMetaDataProviderResult}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code metaData} property.
     */
    private final MetaProperty<LiveDataMetaData> _metaData = DirectMetaProperty.ofReadWrite(
        this, "metaData", LiveDataMetaDataProviderResult.class, LiveDataMetaData.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "metaData");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -450957489:  // metaData
          return _metaData;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends LiveDataMetaDataProviderResult> builder() {
      return new DirectBeanBuilder<LiveDataMetaDataProviderResult>(new LiveDataMetaDataProviderResult());
    }

    @Override
    public Class<? extends LiveDataMetaDataProviderResult> beanType() {
      return LiveDataMetaDataProviderResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code metaData} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LiveDataMetaData> metaData() {
      return _metaData;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -450957489:  // metaData
          return ((LiveDataMetaDataProviderResult) bean).getMetaData();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -450957489:  // metaData
          ((LiveDataMetaDataProviderResult) bean).setMetaData((LiveDataMetaData) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((LiveDataMetaDataProviderResult) bean)._metaData, "metaData");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
