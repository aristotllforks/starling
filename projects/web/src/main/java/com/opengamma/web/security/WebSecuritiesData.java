/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.security;

import java.util.Map;
import java.util.SortedMap;

import javax.ws.rs.core.UriInfo;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.id.UniqueId;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesMaster;
import com.opengamma.master.legalentity.LegalEntityMaster;
import com.opengamma.master.security.SecurityDocument;
import com.opengamma.master.security.SecurityLoader;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.web.WebPerRequestData;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Data class for web-based securities.
 */
@BeanDefinition
public class WebSecuritiesData extends WebPerRequestData {

  /**
   * The security master.
   */
  @PropertyDefinition
  private SecurityMaster _securityMaster;
  /**
   * The security loader.
   */
  @PropertyDefinition
  private SecurityLoader _securityLoader;
  /**
   * The time-series source.
   */
  @PropertyDefinition
  private HistoricalTimeSeriesMaster _historicalTimeSeriesMaster;
  /**
   * The security id from the input URI.
   */
  @PropertyDefinition
  private String _uriSecurityId;
  /**
   * The version id from the URI.
   */
  @PropertyDefinition
  private String _uriVersionId;
  /**
   * The security.
   */
  @PropertyDefinition
  private SecurityDocument _security;
  /**
   * The versioned security.
   */
  @PropertyDefinition
  private SecurityDocument _versioned;
  /**
   * The organization master.
   */
  @PropertyDefinition
  private LegalEntityMaster _legalEntityMaster;
  /**
   * The security description to type mappings.
   */
  @PropertyDefinition
  private SortedMap<String, String> _securityTypes;

  /**
   * Creates an instance.
   */
  public WebSecuritiesData() {
  }

  /**
   * Creates an instance.
   * @param uriInfo  the URI information
   */
  public WebSecuritiesData(final UriInfo uriInfo) {
    setUriInfo(uriInfo);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the best available security id.
   * @param overrideId  the override id, null derives the result from the data
   * @return the id, may be null
   */
  public String getBestSecurityUriId(final UniqueId overrideId) {
    if (overrideId != null) {
      return overrideId.toLatest().toString();
    }
    return getSecurity() != null ? getSecurity().getUniqueId().toLatest().toString() : getUriSecurityId();
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code WebSecuritiesData}.
   * @return the meta-bean, not null
   */
  public static WebSecuritiesData.Meta meta() {
    return WebSecuritiesData.Meta.INSTANCE;
  }

  static {
    MetaBean.register(WebSecuritiesData.Meta.INSTANCE);
  }

  @Override
  public WebSecuritiesData.Meta metaBean() {
    return WebSecuritiesData.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security master.
   * @return the value of the property
   */
  public SecurityMaster getSecurityMaster() {
    return _securityMaster;
  }

  /**
   * Sets the security master.
   * @param securityMaster  the new value of the property
   */
  public void setSecurityMaster(SecurityMaster securityMaster) {
    this._securityMaster = securityMaster;
  }

  /**
   * Gets the the {@code securityMaster} property.
   * @return the property, not null
   */
  public final Property<SecurityMaster> securityMaster() {
    return metaBean().securityMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security loader.
   * @return the value of the property
   */
  public SecurityLoader getSecurityLoader() {
    return _securityLoader;
  }

  /**
   * Sets the security loader.
   * @param securityLoader  the new value of the property
   */
  public void setSecurityLoader(SecurityLoader securityLoader) {
    this._securityLoader = securityLoader;
  }

  /**
   * Gets the the {@code securityLoader} property.
   * @return the property, not null
   */
  public final Property<SecurityLoader> securityLoader() {
    return metaBean().securityLoader().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the time-series source.
   * @return the value of the property
   */
  public HistoricalTimeSeriesMaster getHistoricalTimeSeriesMaster() {
    return _historicalTimeSeriesMaster;
  }

  /**
   * Sets the time-series source.
   * @param historicalTimeSeriesMaster  the new value of the property
   */
  public void setHistoricalTimeSeriesMaster(HistoricalTimeSeriesMaster historicalTimeSeriesMaster) {
    this._historicalTimeSeriesMaster = historicalTimeSeriesMaster;
  }

  /**
   * Gets the the {@code historicalTimeSeriesMaster} property.
   * @return the property, not null
   */
  public final Property<HistoricalTimeSeriesMaster> historicalTimeSeriesMaster() {
    return metaBean().historicalTimeSeriesMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security id from the input URI.
   * @return the value of the property
   */
  public String getUriSecurityId() {
    return _uriSecurityId;
  }

  /**
   * Sets the security id from the input URI.
   * @param uriSecurityId  the new value of the property
   */
  public void setUriSecurityId(String uriSecurityId) {
    this._uriSecurityId = uriSecurityId;
  }

  /**
   * Gets the the {@code uriSecurityId} property.
   * @return the property, not null
   */
  public final Property<String> uriSecurityId() {
    return metaBean().uriSecurityId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the version id from the URI.
   * @return the value of the property
   */
  public String getUriVersionId() {
    return _uriVersionId;
  }

  /**
   * Sets the version id from the URI.
   * @param uriVersionId  the new value of the property
   */
  public void setUriVersionId(String uriVersionId) {
    this._uriVersionId = uriVersionId;
  }

  /**
   * Gets the the {@code uriVersionId} property.
   * @return the property, not null
   */
  public final Property<String> uriVersionId() {
    return metaBean().uriVersionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security.
   * @return the value of the property
   */
  public SecurityDocument getSecurity() {
    return _security;
  }

  /**
   * Sets the security.
   * @param security  the new value of the property
   */
  public void setSecurity(SecurityDocument security) {
    this._security = security;
  }

  /**
   * Gets the the {@code security} property.
   * @return the property, not null
   */
  public final Property<SecurityDocument> security() {
    return metaBean().security().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the versioned security.
   * @return the value of the property
   */
  public SecurityDocument getVersioned() {
    return _versioned;
  }

  /**
   * Sets the versioned security.
   * @param versioned  the new value of the property
   */
  public void setVersioned(SecurityDocument versioned) {
    this._versioned = versioned;
  }

  /**
   * Gets the the {@code versioned} property.
   * @return the property, not null
   */
  public final Property<SecurityDocument> versioned() {
    return metaBean().versioned().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the organization master.
   * @return the value of the property
   */
  public LegalEntityMaster getLegalEntityMaster() {
    return _legalEntityMaster;
  }

  /**
   * Sets the organization master.
   * @param legalEntityMaster  the new value of the property
   */
  public void setLegalEntityMaster(LegalEntityMaster legalEntityMaster) {
    this._legalEntityMaster = legalEntityMaster;
  }

  /**
   * Gets the the {@code legalEntityMaster} property.
   * @return the property, not null
   */
  public final Property<LegalEntityMaster> legalEntityMaster() {
    return metaBean().legalEntityMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security description to type mappings.
   * @return the value of the property
   */
  public SortedMap<String, String> getSecurityTypes() {
    return _securityTypes;
  }

  /**
   * Sets the security description to type mappings.
   * @param securityTypes  the new value of the property
   */
  public void setSecurityTypes(SortedMap<String, String> securityTypes) {
    this._securityTypes = securityTypes;
  }

  /**
   * Gets the the {@code securityTypes} property.
   * @return the property, not null
   */
  public final Property<SortedMap<String, String>> securityTypes() {
    return metaBean().securityTypes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public WebSecuritiesData clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      WebSecuritiesData other = (WebSecuritiesData) obj;
      return JodaBeanUtils.equal(getSecurityMaster(), other.getSecurityMaster()) &&
          JodaBeanUtils.equal(getSecurityLoader(), other.getSecurityLoader()) &&
          JodaBeanUtils.equal(getHistoricalTimeSeriesMaster(), other.getHistoricalTimeSeriesMaster()) &&
          JodaBeanUtils.equal(getUriSecurityId(), other.getUriSecurityId()) &&
          JodaBeanUtils.equal(getUriVersionId(), other.getUriVersionId()) &&
          JodaBeanUtils.equal(getSecurity(), other.getSecurity()) &&
          JodaBeanUtils.equal(getVersioned(), other.getVersioned()) &&
          JodaBeanUtils.equal(getLegalEntityMaster(), other.getLegalEntityMaster()) &&
          JodaBeanUtils.equal(getSecurityTypes(), other.getSecurityTypes()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityLoader());
    hash = hash * 31 + JodaBeanUtils.hashCode(getHistoricalTimeSeriesMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUriSecurityId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUriVersionId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurity());
    hash = hash * 31 + JodaBeanUtils.hashCode(getVersioned());
    hash = hash * 31 + JodaBeanUtils.hashCode(getLegalEntityMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityTypes());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(320);
    buf.append("WebSecuritiesData{");
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
    buf.append("securityMaster").append('=').append(JodaBeanUtils.toString(getSecurityMaster())).append(',').append(' ');
    buf.append("securityLoader").append('=').append(JodaBeanUtils.toString(getSecurityLoader())).append(',').append(' ');
    buf.append("historicalTimeSeriesMaster").append('=').append(JodaBeanUtils.toString(getHistoricalTimeSeriesMaster())).append(',').append(' ');
    buf.append("uriSecurityId").append('=').append(JodaBeanUtils.toString(getUriSecurityId())).append(',').append(' ');
    buf.append("uriVersionId").append('=').append(JodaBeanUtils.toString(getUriVersionId())).append(',').append(' ');
    buf.append("security").append('=').append(JodaBeanUtils.toString(getSecurity())).append(',').append(' ');
    buf.append("versioned").append('=').append(JodaBeanUtils.toString(getVersioned())).append(',').append(' ');
    buf.append("legalEntityMaster").append('=').append(JodaBeanUtils.toString(getLegalEntityMaster())).append(',').append(' ');
    buf.append("securityTypes").append('=').append(JodaBeanUtils.toString(getSecurityTypes())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code WebSecuritiesData}.
   */
  public static class Meta extends WebPerRequestData.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code securityMaster} property.
     */
    private final MetaProperty<SecurityMaster> _securityMaster = DirectMetaProperty.ofReadWrite(
        this, "securityMaster", WebSecuritiesData.class, SecurityMaster.class);
    /**
     * The meta-property for the {@code securityLoader} property.
     */
    private final MetaProperty<SecurityLoader> _securityLoader = DirectMetaProperty.ofReadWrite(
        this, "securityLoader", WebSecuritiesData.class, SecurityLoader.class);
    /**
     * The meta-property for the {@code historicalTimeSeriesMaster} property.
     */
    private final MetaProperty<HistoricalTimeSeriesMaster> _historicalTimeSeriesMaster = DirectMetaProperty.ofReadWrite(
        this, "historicalTimeSeriesMaster", WebSecuritiesData.class, HistoricalTimeSeriesMaster.class);
    /**
     * The meta-property for the {@code uriSecurityId} property.
     */
    private final MetaProperty<String> _uriSecurityId = DirectMetaProperty.ofReadWrite(
        this, "uriSecurityId", WebSecuritiesData.class, String.class);
    /**
     * The meta-property for the {@code uriVersionId} property.
     */
    private final MetaProperty<String> _uriVersionId = DirectMetaProperty.ofReadWrite(
        this, "uriVersionId", WebSecuritiesData.class, String.class);
    /**
     * The meta-property for the {@code security} property.
     */
    private final MetaProperty<SecurityDocument> _security = DirectMetaProperty.ofReadWrite(
        this, "security", WebSecuritiesData.class, SecurityDocument.class);
    /**
     * The meta-property for the {@code versioned} property.
     */
    private final MetaProperty<SecurityDocument> _versioned = DirectMetaProperty.ofReadWrite(
        this, "versioned", WebSecuritiesData.class, SecurityDocument.class);
    /**
     * The meta-property for the {@code legalEntityMaster} property.
     */
    private final MetaProperty<LegalEntityMaster> _legalEntityMaster = DirectMetaProperty.ofReadWrite(
        this, "legalEntityMaster", WebSecuritiesData.class, LegalEntityMaster.class);
    /**
     * The meta-property for the {@code securityTypes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<SortedMap<String, String>> _securityTypes = DirectMetaProperty.ofReadWrite(
        this, "securityTypes", WebSecuritiesData.class, (Class) SortedMap.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "securityMaster",
        "securityLoader",
        "historicalTimeSeriesMaster",
        "uriSecurityId",
        "uriVersionId",
        "security",
        "versioned",
        "legalEntityMaster",
        "securityTypes");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -887218750:  // securityMaster
          return _securityMaster;
        case -903470221:  // securityLoader
          return _securityLoader;
        case 173967376:  // historicalTimeSeriesMaster
          return _historicalTimeSeriesMaster;
        case 1433303815:  // uriSecurityId
          return _uriSecurityId;
        case 666567687:  // uriVersionId
          return _uriVersionId;
        case 949122880:  // security
          return _security;
        case -1407102089:  // versioned
          return _versioned;
        case -1944474242:  // legalEntityMaster
          return _legalEntityMaster;
        case -714180327:  // securityTypes
          return _securityTypes;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends WebSecuritiesData> builder() {
      return new DirectBeanBuilder<>(new WebSecuritiesData());
    }

    @Override
    public Class<? extends WebSecuritiesData> beanType() {
      return WebSecuritiesData.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code securityMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityMaster> securityMaster() {
      return _securityMaster;
    }

    /**
     * The meta-property for the {@code securityLoader} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityLoader> securityLoader() {
      return _securityLoader;
    }

    /**
     * The meta-property for the {@code historicalTimeSeriesMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<HistoricalTimeSeriesMaster> historicalTimeSeriesMaster() {
      return _historicalTimeSeriesMaster;
    }

    /**
     * The meta-property for the {@code uriSecurityId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uriSecurityId() {
      return _uriSecurityId;
    }

    /**
     * The meta-property for the {@code uriVersionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uriVersionId() {
      return _uriVersionId;
    }

    /**
     * The meta-property for the {@code security} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityDocument> security() {
      return _security;
    }

    /**
     * The meta-property for the {@code versioned} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityDocument> versioned() {
      return _versioned;
    }

    /**
     * The meta-property for the {@code legalEntityMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LegalEntityMaster> legalEntityMaster() {
      return _legalEntityMaster;
    }

    /**
     * The meta-property for the {@code securityTypes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SortedMap<String, String>> securityTypes() {
      return _securityTypes;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -887218750:  // securityMaster
          return ((WebSecuritiesData) bean).getSecurityMaster();
        case -903470221:  // securityLoader
          return ((WebSecuritiesData) bean).getSecurityLoader();
        case 173967376:  // historicalTimeSeriesMaster
          return ((WebSecuritiesData) bean).getHistoricalTimeSeriesMaster();
        case 1433303815:  // uriSecurityId
          return ((WebSecuritiesData) bean).getUriSecurityId();
        case 666567687:  // uriVersionId
          return ((WebSecuritiesData) bean).getUriVersionId();
        case 949122880:  // security
          return ((WebSecuritiesData) bean).getSecurity();
        case -1407102089:  // versioned
          return ((WebSecuritiesData) bean).getVersioned();
        case -1944474242:  // legalEntityMaster
          return ((WebSecuritiesData) bean).getLegalEntityMaster();
        case -714180327:  // securityTypes
          return ((WebSecuritiesData) bean).getSecurityTypes();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -887218750:  // securityMaster
          ((WebSecuritiesData) bean).setSecurityMaster((SecurityMaster) newValue);
          return;
        case -903470221:  // securityLoader
          ((WebSecuritiesData) bean).setSecurityLoader((SecurityLoader) newValue);
          return;
        case 173967376:  // historicalTimeSeriesMaster
          ((WebSecuritiesData) bean).setHistoricalTimeSeriesMaster((HistoricalTimeSeriesMaster) newValue);
          return;
        case 1433303815:  // uriSecurityId
          ((WebSecuritiesData) bean).setUriSecurityId((String) newValue);
          return;
        case 666567687:  // uriVersionId
          ((WebSecuritiesData) bean).setUriVersionId((String) newValue);
          return;
        case 949122880:  // security
          ((WebSecuritiesData) bean).setSecurity((SecurityDocument) newValue);
          return;
        case -1407102089:  // versioned
          ((WebSecuritiesData) bean).setVersioned((SecurityDocument) newValue);
          return;
        case -1944474242:  // legalEntityMaster
          ((WebSecuritiesData) bean).setLegalEntityMaster((LegalEntityMaster) newValue);
          return;
        case -714180327:  // securityTypes
          ((WebSecuritiesData) bean).setSecurityTypes((SortedMap<String, String>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
