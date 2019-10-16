/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.exchange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.AbstractSearchResult;
import com.opengamma.util.PublicSPI;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;

/**
 * Result from searching for exchanges.
 * <p>
 * The returned documents will match the search criteria.
 * See {@link ExchangeSearchRequest} for more details.
 */
@PublicSPI
@BeanDefinition
public class ExchangeSearchResult extends AbstractSearchResult<ExchangeDocument> {

  /**
   * Creates an instance.
   */
  public ExchangeSearchResult() {
  }

  /**
   * Creates an instance from a collection of documents.
   *
   * @param coll  the collection of documents to add, not null
   */
  public ExchangeSearchResult(final Collection<ExchangeDocument> coll) {
    super(coll);
  }

  /**
   * Creates an instance specifying the version-correction searched for.
   *
   * @param versionCorrection  the version-correction of the data, not null
   */
  public ExchangeSearchResult(final VersionCorrection versionCorrection) {
    setVersionCorrection(versionCorrection);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the returned exchanges from within the documents.
   *
   * @return the exchanges, not null
   */
  public List<ManageableExchange> getExchanges() {
    final List<ManageableExchange> result = new ArrayList<>();
    if (getDocuments() != null) {
      for (final ExchangeDocument doc : getDocuments()) {
        result.add(doc.getExchange());
      }
    }
    return result;
  }

  /**
   * Gets the first exchange, or null if no documents.
   *
   * @return the first exchange, null if none
   */
  public ManageableExchange getFirstExchange() {
    return getDocuments().size() > 0 ? getDocuments().get(0).getExchange() : null;
  }

  /**
   * Gets the single result expected from a query.
   * <p>
   * This throws an exception if more than 1 result is actually available.
   * Thus, this method implies an assumption about uniqueness of the queried exchange.
   *
   * @return the matching exchange, not null
   * @throws IllegalStateException if no exchange was found
   */
  public ManageableExchange getSingleExchange() {
    if (getDocuments().size() != 1) {
      throw new OpenGammaRuntimeException("Expecting zero or single resulting match, and was " + getDocuments().size());
    }
    return getDocuments().get(0).getExchange();
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code ExchangeSearchResult}.
   * @return the meta-bean, not null
   */
  public static ExchangeSearchResult.Meta meta() {
    return ExchangeSearchResult.Meta.INSTANCE;
  }

  static {
    MetaBean.register(ExchangeSearchResult.Meta.INSTANCE);
  }

  @Override
  public ExchangeSearchResult.Meta metaBean() {
    return ExchangeSearchResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public ExchangeSearchResult clone() {
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
    buf.append("ExchangeSearchResult{");
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
   * The meta-bean for {@code ExchangeSearchResult}.
   */
  public static class Meta extends AbstractSearchResult.Meta<ExchangeDocument> {
    /**
     * The singleton instance of the meta-bean.
     */
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
    public BeanBuilder<? extends ExchangeSearchResult> builder() {
      return new DirectBeanBuilder<>(new ExchangeSearchResult());
    }

    @Override
    public Class<? extends ExchangeSearchResult> beanType() {
      return ExchangeSearchResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
