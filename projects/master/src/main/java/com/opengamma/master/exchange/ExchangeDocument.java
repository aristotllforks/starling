/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.exchange;

import java.io.Serializable;
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

import com.opengamma.id.UniqueId;
import com.opengamma.master.AbstractDocument;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.PublicSPI;

/**
 * A document used to pass into and out of the exchange master.
 */
@PublicSPI
@BeanDefinition
public class ExchangeDocument extends AbstractDocument implements Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The exchange object held by the document.
   */
  @PropertyDefinition
  private ManageableExchange _exchange;
  /**
   * The exchange unique identifier.
   * This field is managed by the master but must be set for updates.
   */
  @PropertyDefinition(overrideGet = true, overrideSet = true)
  private UniqueId _uniqueId;

  /**
   * Creates an instance.
   */
  public ExchangeDocument() {
  }

  /**
   * Creates an instance from an exchange.
   * @param exchange  the exchange, not null
   */
  public ExchangeDocument(final ManageableExchange exchange) {
    ArgumentChecker.notNull(exchange, "exchange");
    setUniqueId(exchange.getUniqueId());
    setExchange(exchange);
  }

  //-------------------------------------------------------------------------
  @Override
  public ManageableExchange getValue() {
    return getExchange();
  }

  /**
   * Gets the name of the exchange.
   * <p>
   * This is derived from the exchange itself.
   *
   * @return the name, null if no name
   */
  public String getName() {
    return getExchange() != null ? getExchange().getName() : null;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ExchangeDocument}.
   * @return the meta-bean, not null
   */
  public static ExchangeDocument.Meta meta() {
    return ExchangeDocument.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ExchangeDocument.Meta.INSTANCE);
  }

  @Override
  public ExchangeDocument.Meta metaBean() {
    return ExchangeDocument.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange object held by the document.
   * @return the value of the property
   */
  public ManageableExchange getExchange() {
    return _exchange;
  }

  /**
   * Sets the exchange object held by the document.
   * @param exchange  the new value of the property
   */
  public void setExchange(ManageableExchange exchange) {
    this._exchange = exchange;
  }

  /**
   * Gets the the {@code exchange} property.
   * @return the property, not null
   */
  public final Property<ManageableExchange> exchange() {
    return metaBean().exchange().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange unique identifier.
   * This field is managed by the master but must be set for updates.
   * @return the value of the property
   */
  @Override
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the exchange unique identifier.
   * This field is managed by the master but must be set for updates.
   * @param uniqueId  the new value of the property
   */
  @Override
  public void setUniqueId(UniqueId uniqueId) {
    this._uniqueId = uniqueId;
  }

  /**
   * Gets the the {@code uniqueId} property.
   * This field is managed by the master but must be set for updates.
   * @return the property, not null
   */
  public final Property<UniqueId> uniqueId() {
    return metaBean().uniqueId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public ExchangeDocument clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ExchangeDocument other = (ExchangeDocument) obj;
      return JodaBeanUtils.equal(getExchange(), other.getExchange()) &&
          JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getExchange());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("ExchangeDocument{");
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
    buf.append("exchange").append('=').append(JodaBeanUtils.toString(getExchange())).append(',').append(' ');
    buf.append("uniqueId").append('=').append(JodaBeanUtils.toString(getUniqueId())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ExchangeDocument}.
   */
  public static class Meta extends AbstractDocument.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code exchange} property.
     */
    private final MetaProperty<ManageableExchange> _exchange = DirectMetaProperty.ofReadWrite(
        this, "exchange", ExchangeDocument.class, ManageableExchange.class);
    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueId> _uniqueId = DirectMetaProperty.ofReadWrite(
        this, "uniqueId", ExchangeDocument.class, UniqueId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "exchange",
        "uniqueId");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1989774883:  // exchange
          return _exchange;
        case -294460212:  // uniqueId
          return _uniqueId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ExchangeDocument> builder() {
      return new DirectBeanBuilder<ExchangeDocument>(new ExchangeDocument());
    }

    @Override
    public Class<? extends ExchangeDocument> beanType() {
      return ExchangeDocument.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code exchange} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ManageableExchange> exchange() {
      return _exchange;
    }

    /**
     * The meta-property for the {@code uniqueId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueId> uniqueId() {
      return _uniqueId;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1989774883:  // exchange
          return ((ExchangeDocument) bean).getExchange();
        case -294460212:  // uniqueId
          return ((ExchangeDocument) bean).getUniqueId();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1989774883:  // exchange
          ((ExchangeDocument) bean).setExchange((ManageableExchange) newValue);
          return;
        case -294460212:  // uniqueId
          ((ExchangeDocument) bean).setUniqueId((UniqueId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
