/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.legalentity;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.Instant;

import com.opengamma.id.ObjectIdentifiable;
import com.opengamma.master.AbstractHistoryRequest;
import com.opengamma.util.PublicSPI;

/**
 * Request for the history of a legal entity.
 * <p>
 * A full legal entity master implements historical storage of data. History can
 * be stored in two dimensions and this request provides searching.
 * <p>
 * The first historic dimension is the classic series of versions. Each new
 * version is stored in such a manor that previous versions can be accessed.
 * <p>
 * The second historic dimension is corrections. A correction occurs when it is
 * realized that the original data stored was incorrect. A simple legal entity
 * master might simply replace the original version with the corrected value. A
 * full implementation will store the correction in such a manner that it is
 * still possible to obtain the value before the correction was made.
 * <p>
 * For example, a legal entity added on Monday and updated on Thursday has two
 * versions. If it is realized on Friday that the version stored on Monday was
 * incorrect, then a correction may be applied. There are now two versions, the
 * first of which has one correction. This may continue, with multiple
 * corrections allowed for each version.
 * <p>
 * Versions and corrections are represented by instants in the search.
 */
@PublicSPI
@BeanDefinition
public class LegalEntityHistoryRequest extends AbstractHistoryRequest {

  /**
   * Creates an instance.
   * The object identifier must be added before searching.
   */
  public LegalEntityHistoryRequest() {
    super();
  }

  /**
   * Creates an instance with object identifier.
   * This will retrieve all versions and corrections unless the relevant fields are set.
   *
   * @param objectId the object identifier, not null
   */
  public LegalEntityHistoryRequest(final ObjectIdentifiable objectId) {
    super(objectId);
  }

  /**
   * Creates an instance with object identifier and optional version and correction.
   *
   * @param objectId           the object identifier, not null
   * @param versionInstant     the version instant to retrieve, null for all versions
   * @param correctedToInstant the instant that the data should be corrected to, null for all corrections
   */
  public LegalEntityHistoryRequest(final ObjectIdentifiable objectId, final Instant versionInstant, final Instant correctedToInstant) {
    super(objectId, versionInstant, correctedToInstant);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code LegalEntityHistoryRequest}.
   * @return the meta-bean, not null
   */
  public static LegalEntityHistoryRequest.Meta meta() {
    return LegalEntityHistoryRequest.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(LegalEntityHistoryRequest.Meta.INSTANCE);
  }

  @Override
  public LegalEntityHistoryRequest.Meta metaBean() {
    return LegalEntityHistoryRequest.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public LegalEntityHistoryRequest clone() {
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
    buf.append("LegalEntityHistoryRequest{");
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
   * The meta-bean for {@code LegalEntityHistoryRequest}.
   */
  public static class Meta extends AbstractHistoryRequest.Meta {
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
    public BeanBuilder<? extends LegalEntityHistoryRequest> builder() {
      return new DirectBeanBuilder<LegalEntityHistoryRequest>(new LegalEntityHistoryRequest());
    }

    @Override
    public Class<? extends LegalEntityHistoryRequest> beanType() {
      return LegalEntityHistoryRequest.class;
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
