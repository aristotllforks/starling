/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.AbstractSearchResult;
import com.opengamma.util.PublicSPI;

/**
 * Result from searching for securities.
 * <p>
 * The returned documents will match the search criteria.
 * See {@link SecuritySearchRequest} for more details.
 */
@PublicSPI
@BeanDefinition
public class SecuritySearchResult extends AbstractSearchResult<SecurityDocument> {

  /**
   * The number of items that were removed because the user is not authorized to see them.
   */
  @PropertyDefinition
  private int _unauthorizedCount;

  /**
   * Creates an instance.
   */
  public SecuritySearchResult() {
  }

  /**
   * Creates an instance from a collection of documents.
   *
   * @param coll  the collection of documents to add, not null
   */
  public SecuritySearchResult(final Collection<SecurityDocument> coll) {
    super(coll);
  }

  /**
   * Creates an instance specifying the version-correction searched for.
   *
   * @param versionCorrection  the version-correction of the data, not null
   */
  public SecuritySearchResult(final VersionCorrection versionCorrection) {
    setVersionCorrection(versionCorrection);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the returned securities from within the documents.
   *
   * @return the securities, not null
   */
  public List<ManageableSecurity> getSecurities() {
    final List<ManageableSecurity> result = new ArrayList<>();
    if (getDocuments() != null) {
      for (final SecurityDocument doc : getDocuments()) {
        result.add(doc.getSecurity());
      }
    }
    return result;
  }

  /**
   * Gets the first security, or null if no documents.
   *
   * @return the first security, null if none
   */
  public ManageableSecurity getFirstSecurity() {
    return getDocuments().size() > 0 ? getDocuments().get(0).getSecurity() : null;
  }

  /**
   * Gets the single result expected from a query.
   * <p>
   * This throws an exception if more than 1 result is actually available.
   * Thus, this method implies an assumption about uniqueness of the queried security.
   *
   * @return the matching security, not null
   * @throws IllegalStateException if no security was found
   */
  public ManageableSecurity getSingleSecurity() {
    if (getDocuments().size() != 1) {
      throw new OpenGammaRuntimeException("Expecting zero or single resulting match, and was " + getDocuments().size());
    }
    return getDocuments().get(0).getSecurity();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SecuritySearchResult}.
   * @return the meta-bean, not null
   */
  public static SecuritySearchResult.Meta meta() {
    return SecuritySearchResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SecuritySearchResult.Meta.INSTANCE);
  }

  @Override
  public SecuritySearchResult.Meta metaBean() {
    return SecuritySearchResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the number of items that were removed because the user is not authorized to see them.
   * @return the value of the property
   */
  public int getUnauthorizedCount() {
    return _unauthorizedCount;
  }

  /**
   * Sets the number of items that were removed because the user is not authorized to see them.
   * @param unauthorizedCount  the new value of the property
   */
  public void setUnauthorizedCount(int unauthorizedCount) {
    this._unauthorizedCount = unauthorizedCount;
  }

  /**
   * Gets the the {@code unauthorizedCount} property.
   * @return the property, not null
   */
  public final Property<Integer> unauthorizedCount() {
    return metaBean().unauthorizedCount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public SecuritySearchResult clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SecuritySearchResult other = (SecuritySearchResult) obj;
      return (getUnauthorizedCount() == other.getUnauthorizedCount()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnauthorizedCount());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("SecuritySearchResult{");
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
    buf.append("unauthorizedCount").append('=').append(JodaBeanUtils.toString(getUnauthorizedCount())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SecuritySearchResult}.
   */
  public static class Meta extends AbstractSearchResult.Meta<SecurityDocument> {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code unauthorizedCount} property.
     */
    private final MetaProperty<Integer> _unauthorizedCount = DirectMetaProperty.ofReadWrite(
        this, "unauthorizedCount", SecuritySearchResult.class, Integer.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "unauthorizedCount");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2063040635:  // unauthorizedCount
          return _unauthorizedCount;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SecuritySearchResult> builder() {
      return new DirectBeanBuilder<SecuritySearchResult>(new SecuritySearchResult());
    }

    @Override
    public Class<? extends SecuritySearchResult> beanType() {
      return SecuritySearchResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code unauthorizedCount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Integer> unauthorizedCount() {
      return _unauthorizedCount;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2063040635:  // unauthorizedCount
          return ((SecuritySearchResult) bean).getUnauthorizedCount();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2063040635:  // unauthorizedCount
          ((SecuritySearchResult) bean).setUnauthorizedCount((Integer) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
