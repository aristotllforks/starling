/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.config.impl.ConfigItem;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.AbstractSearchResult;
import com.opengamma.util.PublicSPI;

/**
 * Result from searching for configuration documents.
 * <p>
 * The returned documents will match the search criteria.
 * See {@link ConfigSearchRequest} for more details.
 *
 * @param <T>  the type of the underlying config
 */
@PublicSPI
@BeanDefinition
public class ConfigSearchResult<T> extends AbstractSearchResult<ConfigDocument> {

  /**
   * Creates an instance.
   */
  public ConfigSearchResult() {
  }

  /**
   * Creates an instance from a collection of documents.
   *
   * @param documents  the collection of documents to add, not null
   */
  public ConfigSearchResult(final Collection<ConfigDocument> documents) {
    super(documents);
  }

  /**
   * Creates an instance specifying the version-correction searched for.
   *
   * @param versionCorrection  the version-correction of the data, not null
   */
  public ConfigSearchResult(final VersionCorrection versionCorrection) {
    setVersionCorrection(versionCorrection);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the returned configuration values from within the documents.
   *
   * @return the configuration values, not null
   */
  @SuppressWarnings("unchecked")
  public List<ConfigItem<T>> getValues() {
    final List<ConfigItem<T>> result = new ArrayList<>();
    if (getDocuments() != null) {
      for (final ConfigDocument doc : getDocuments()) {
        result.add((ConfigItem<T>) doc.getConfig());
      }
    }
    return result;
  }

  /**
   * Gets the first configuration document value, or null if no documents.
   *
   * @return the first configuration value, null if none
   */
  public ConfigItem<T> getFirstValue() {
    return getValues().size() > 0 ? getValues().get(0) : null;
  }

  /**
   * Gets the single result expected from a query.
   * <p>
   * This throws an exception if more than 1 result is actually available.
   * Thus, this method implies an assumption about uniqueness of the queried config.
   *
   * @return the matching config, not null
   * @throws IllegalStateException if no config was found
   */
  public ConfigItem<T> getSingleValue() {
    if (getDocuments().size() != 1) {
      throw new OpenGammaRuntimeException("Expecting zero or single resulting match, and was " + getDocuments().size());
    }
    return getValues().get(0);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ConfigSearchResult}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("rawtypes")
  public static ConfigSearchResult.Meta meta() {
    return ConfigSearchResult.Meta.INSTANCE;
  }

  /**
   * The meta-bean for {@code ConfigSearchResult}.
   * @param <R>  the bean's generic type
   * @param cls  the bean's generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R> ConfigSearchResult.Meta<R> metaConfigSearchResult(Class<R> cls) {
    return ConfigSearchResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ConfigSearchResult.Meta.INSTANCE);
  }

  @SuppressWarnings("unchecked")
  @Override
  public ConfigSearchResult.Meta<T> metaBean() {
    return ConfigSearchResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public ConfigSearchResult<T> clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(32);
    buf.append("ConfigSearchResult{");
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
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ConfigSearchResult}.
   * @param <T>  the type
   */
  public static class Meta<T> extends AbstractSearchResult.Meta<ConfigDocument> {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends ConfigSearchResult<T>> builder() {
      return new DirectBeanBuilder<ConfigSearchResult<T>>(new ConfigSearchResult<T>());
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends ConfigSearchResult<T>> beanType() {
      return (Class) ConfigSearchResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
