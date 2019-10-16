/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.provider.security;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.google.common.collect.Lists;
import com.opengamma.core.security.Security;
import com.opengamma.util.PublicSPI;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Result from enhancing one or more securities.
 * <p>
 * This class is mutable and not thread-safe.
 */
@PublicSPI
@BeanDefinition
public class SecurityEnhancerResult extends DirectBean {

  /**
   * The securities that were enhanced.
   * These are in the same order as the securities passed to the request.
   */
  @PropertyDefinition
  private final List<Security> _resultList = Lists.newArrayList();

  /**
   * Creates an instance.
   */
  public SecurityEnhancerResult() {
  }

  /**
   * Creates an instance.
   *
   * @param result  the map of results, not null
   */
  public SecurityEnhancerResult(final List<? extends Security> result) {
    getResultList().addAll(result);
  }

  //-------------------------------------------------------------------------
  /**
   * Inserts the results into the specified map.
   * <p>
   * This is used to allow a map of securities to be enhanced. The map must have a fixed order, such as with {@link java.util.LinkedHashMap}.
   * <p>
   * There are three steps. Firstly, the values from the map are passed to the {@link SecurityEnhancerRequest}. Secondly, the securities are enhanced. Thirdly,
   * this method is used to re-populate the map.
   *
   * @param map
   *          the map to push the results into, not null
   */
  public void insertIntoMapValues(final Map<?, Security> map) {
    if (map.size() != getResultList().size()) {
      throw new IllegalArgumentException("Map specified cannot be matched");
    }
    int i = 0;
    for (final Entry<?, Security> entry : map.entrySet()) {
      entry.setValue(getResultList().get(i++));
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code SecurityEnhancerResult}.
   * @return the meta-bean, not null
   */
  public static SecurityEnhancerResult.Meta meta() {
    return SecurityEnhancerResult.Meta.INSTANCE;
  }

  static {
    MetaBean.register(SecurityEnhancerResult.Meta.INSTANCE);
  }

  @Override
  public SecurityEnhancerResult.Meta metaBean() {
    return SecurityEnhancerResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the securities that were enhanced.
   * These are in the same order as the securities passed to the request.
   * @return the value of the property, not null
   */
  public List<Security> getResultList() {
    return _resultList;
  }

  /**
   * Sets the securities that were enhanced.
   * These are in the same order as the securities passed to the request.
   * @param resultList  the new value of the property, not null
   */
  public void setResultList(List<Security> resultList) {
    JodaBeanUtils.notNull(resultList, "resultList");
    this._resultList.clear();
    this._resultList.addAll(resultList);
  }

  /**
   * Gets the the {@code resultList} property.
   * These are in the same order as the securities passed to the request.
   * @return the property, not null
   */
  public final Property<List<Security>> resultList() {
    return metaBean().resultList().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public SecurityEnhancerResult clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SecurityEnhancerResult other = (SecurityEnhancerResult) obj;
      return JodaBeanUtils.equal(getResultList(), other.getResultList());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getResultList());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("SecurityEnhancerResult{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("resultList").append('=').append(JodaBeanUtils.toString(getResultList())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SecurityEnhancerResult}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code resultList} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<Security>> _resultList = DirectMetaProperty.ofReadWrite(
        this, "resultList", SecurityEnhancerResult.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "resultList");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -572090789:  // resultList
          return _resultList;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SecurityEnhancerResult> builder() {
      return new DirectBeanBuilder<>(new SecurityEnhancerResult());
    }

    @Override
    public Class<? extends SecurityEnhancerResult> beanType() {
      return SecurityEnhancerResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code resultList} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<Security>> resultList() {
      return _resultList;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -572090789:  // resultList
          return ((SecurityEnhancerResult) bean).getResultList();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -572090789:  // resultList
          ((SecurityEnhancerResult) bean).setResultList((List<Security>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((SecurityEnhancerResult) bean)._resultList, "resultList");
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
