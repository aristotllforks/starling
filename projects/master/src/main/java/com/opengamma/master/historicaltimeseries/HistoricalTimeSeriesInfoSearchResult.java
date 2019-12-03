/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.historicaltimeseries;

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
 * Result from searching for historical time-series information.
 * <p>
 * The returned documents will match the search criteria.
 * See {@link HistoricalTimeSeriesInfoSearchRequest} for more details.
 * <p>
 * This class is mutable and not thread-safe.
 */
@PublicSPI
@BeanDefinition
public class HistoricalTimeSeriesInfoSearchResult extends AbstractSearchResult<HistoricalTimeSeriesInfoDocument> {

  /**
   * The number of items that were removed because the user is not authorized to see them.
   */
  @PropertyDefinition
  private int _unauthorizedCount;

  /**
   * Creates an instance.
   */
  public HistoricalTimeSeriesInfoSearchResult() {
  }

  /**
   * Creates an instance from a collection of documents.
   *
   * @param coll  the collection of documents to add, not null
   */
  public HistoricalTimeSeriesInfoSearchResult(final Collection<HistoricalTimeSeriesInfoDocument> coll) {
    super(coll);
  }

  /**
   * Creates an instance specifying the version-correction searched for.
   *
   * @param versionCorrection  the version-correction of the data, not null
   */
  public HistoricalTimeSeriesInfoSearchResult(final VersionCorrection versionCorrection) {
    setVersionCorrection(versionCorrection);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the returned series information from within the documents.
   *
   * @return the series, not null
   */
  public List<ManageableHistoricalTimeSeriesInfo> getInfoList() {
    final List<ManageableHistoricalTimeSeriesInfo> result = new ArrayList<>();
    if (getDocuments() != null) {
      for (final HistoricalTimeSeriesInfoDocument doc : getDocuments()) {
        result.add(doc.getInfo());
      }
    }
    return result;
  }

  /**
   * Gets the first series information, or null if no documents.
   *
   * @return the first series information, null if none
   */
  public ManageableHistoricalTimeSeriesInfo getFirstInfo() {
    return getDocuments().size() > 0 ? getDocuments().get(0).getInfo() : null;
  }

  /**
   * Gets the single result expected from a query.
   * <p>
   * This throws an exception if more than 1 result is actually available.
   * Thus, this method implies an assumption about uniqueness of the queried series.
   *
   * @return the matching exchange, not null
   * @throws IllegalStateException if no series was found
   */
  public ManageableHistoricalTimeSeriesInfo getSingleInfo() {
    if (getDocuments().size() != 1) {
      throw new OpenGammaRuntimeException("Expecting zero or single resulting match, and was " + getDocuments().size());
    }
    return getDocuments().get(0).getInfo();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code HistoricalTimeSeriesInfoSearchResult}.
   * @return the meta-bean, not null
   */
  public static HistoricalTimeSeriesInfoSearchResult.Meta meta() {
    return HistoricalTimeSeriesInfoSearchResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(HistoricalTimeSeriesInfoSearchResult.Meta.INSTANCE);
  }

  @Override
  public HistoricalTimeSeriesInfoSearchResult.Meta metaBean() {
    return HistoricalTimeSeriesInfoSearchResult.Meta.INSTANCE;
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
  public HistoricalTimeSeriesInfoSearchResult clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      HistoricalTimeSeriesInfoSearchResult other = (HistoricalTimeSeriesInfoSearchResult) obj;
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
    buf.append("HistoricalTimeSeriesInfoSearchResult{");
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
   * The meta-bean for {@code HistoricalTimeSeriesInfoSearchResult}.
   */
  public static class Meta extends AbstractSearchResult.Meta<HistoricalTimeSeriesInfoDocument> {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code unauthorizedCount} property.
     */
    private final MetaProperty<Integer> _unauthorizedCount = DirectMetaProperty.ofReadWrite(
        this, "unauthorizedCount", HistoricalTimeSeriesInfoSearchResult.class, Integer.TYPE);
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
    public BeanBuilder<? extends HistoricalTimeSeriesInfoSearchResult> builder() {
      return new DirectBeanBuilder<HistoricalTimeSeriesInfoSearchResult>(new HistoricalTimeSeriesInfoSearchResult());
    }

    @Override
    public Class<? extends HistoricalTimeSeriesInfoSearchResult> beanType() {
      return HistoricalTimeSeriesInfoSearchResult.class;
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
          return ((HistoricalTimeSeriesInfoSearchResult) bean).getUnauthorizedCount();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2063040635:  // unauthorizedCount
          ((HistoricalTimeSeriesInfoSearchResult) bean).setUnauthorizedCount((Integer) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
