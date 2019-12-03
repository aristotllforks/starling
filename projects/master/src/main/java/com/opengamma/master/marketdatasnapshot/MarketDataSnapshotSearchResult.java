/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.marketdatasnapshot;

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
import com.opengamma.core.marketdatasnapshot.NamedSnapshot;
import com.opengamma.core.marketdatasnapshot.impl.ManageableMarketDataSnapshot;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.AbstractSearchResult;
import com.opengamma.util.PublicSPI;

/**
 * Result from searching for snapshots.
 * <p>
 * The returned documents will match the search criteria.
 * See {@link MarketDataSnapshotSearchRequest} for more details.
 */
@PublicSPI
@BeanDefinition
public class MarketDataSnapshotSearchResult extends AbstractSearchResult<MarketDataSnapshotDocument> {

  /**
   * Creates an instance.
   */
  public MarketDataSnapshotSearchResult() {
  }

  /**
   * Creates an instance from a collection of documents.
   *
   * @param coll  the collection of documents to add, not null
   */
  public MarketDataSnapshotSearchResult(final Collection<MarketDataSnapshotDocument> coll) {
    super(coll);
  }

  /**
   * Creates an instance specifying the version-correction searched for.
   *
   * @param versionCorrection  the version-correction of the data, not null
   */
  public MarketDataSnapshotSearchResult(final VersionCorrection versionCorrection) {
    setVersionCorrection(versionCorrection);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the returned snapshots from within the documents.
   *
   * @return the snapshots, not null
   * @deprecated use {@link #getNamedSnapshots()} which can handle all snapshot types
   */
  @Deprecated
  public List<ManageableMarketDataSnapshot> getSnapshots() {
    final List<ManageableMarketDataSnapshot> result = new ArrayList<>();
    if (getDocuments() != null) {
      for (final MarketDataSnapshotDocument doc : getDocuments()) {
        result.add(doc.getSnapshot());
      }
    }
    return result;
  }

  /**
   * Gets the returned snapshots from within the documents.
   *
   * @return the snapshots, not null
   */
  public List<NamedSnapshot> getNamedSnapshots() {
    final List<NamedSnapshot> result = new ArrayList<>();
    for (final MarketDataSnapshotDocument doc : getDocuments()) {
      result.add(doc.getNamedSnapshot());
    }
    return result;
  }

  /**
   * Gets the first snapshot, or null if no documents.
   *
   * @return the first snapshot, null if none
   */
  public ManageableMarketDataSnapshot getFirstSnapshot() {
    return getDocuments().size() > 0 ? getDocuments().get(0).getSnapshot() : null;
  }

  /**
   * Gets the single result expected from a query.
   * <p>
   * This throws an exception if more than 1 result is actually available.
   * Thus, this method implies an assumption about uniqueness of the queried snapshot.
   *
   * @return the matching snapshot, not null
   * @throws IllegalStateException if no snapshot was found
   */
  public ManageableMarketDataSnapshot getSingleSnapshot() {
    if (getDocuments().size() != 1) {
      throw new OpenGammaRuntimeException("Expecting zero or single resulting match, and was " + getDocuments().size());
    }
    return getDocuments().get(0).getNamedSnapshot(ManageableMarketDataSnapshot.class);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MarketDataSnapshotSearchResult}.
   * @return the meta-bean, not null
   */
  public static MarketDataSnapshotSearchResult.Meta meta() {
    return MarketDataSnapshotSearchResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MarketDataSnapshotSearchResult.Meta.INSTANCE);
  }

  @Override
  public MarketDataSnapshotSearchResult.Meta metaBean() {
    return MarketDataSnapshotSearchResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public MarketDataSnapshotSearchResult clone() {
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
    buf.append("MarketDataSnapshotSearchResult{");
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
   * The meta-bean for {@code MarketDataSnapshotSearchResult}.
   */
  public static class Meta extends AbstractSearchResult.Meta<MarketDataSnapshotDocument> {
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
    public BeanBuilder<? extends MarketDataSnapshotSearchResult> builder() {
      return new DirectBeanBuilder<MarketDataSnapshotSearchResult>(new MarketDataSnapshotSearchResult());
    }

    @Override
    public Class<? extends MarketDataSnapshotSearchResult> beanType() {
      return MarketDataSnapshotSearchResult.class;
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
