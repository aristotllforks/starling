/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.position;

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
 * A document used to pass into and out of the position master.
 */
@PublicSPI
@BeanDefinition
public class PositionDocument extends AbstractDocument implements Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The position object held by the document.
   */
  @PropertyDefinition
  private ManageablePosition _position;
  /**
   * The position unique identifier.
   * This field is managed by the master but must be set for updates.
   */
  @PropertyDefinition(overrideGet = true, overrideSet = true)
  private UniqueId _uniqueId;

  /**
   * Creates an instance.
   */
  public PositionDocument() {
  }

  /**
   * Creates an instance from a position.
   * @param position  the position, not null
   */
  public PositionDocument(final ManageablePosition position) {
    ArgumentChecker.notNull(position, "position");
    setUniqueId(position.getUniqueId());
    setPosition(position);
  }

  //-------------------------------------------------------------------------
  @Override
  public ManageablePosition getValue() {
    return getPosition();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code PositionDocument}.
   * @return the meta-bean, not null
   */
  public static PositionDocument.Meta meta() {
    return PositionDocument.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(PositionDocument.Meta.INSTANCE);
  }

  @Override
  public PositionDocument.Meta metaBean() {
    return PositionDocument.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the position object held by the document.
   * @return the value of the property
   */
  public ManageablePosition getPosition() {
    return _position;
  }

  /**
   * Sets the position object held by the document.
   * @param position  the new value of the property
   */
  public void setPosition(ManageablePosition position) {
    this._position = position;
  }

  /**
   * Gets the the {@code position} property.
   * @return the property, not null
   */
  public final Property<ManageablePosition> position() {
    return metaBean().position().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the position unique identifier.
   * This field is managed by the master but must be set for updates.
   * @return the value of the property
   */
  @Override
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the position unique identifier.
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
  public PositionDocument clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      PositionDocument other = (PositionDocument) obj;
      return JodaBeanUtils.equal(getPosition(), other.getPosition()) &&
          JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getPosition());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("PositionDocument{");
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
    buf.append("position").append('=').append(JodaBeanUtils.toString(getPosition())).append(',').append(' ');
    buf.append("uniqueId").append('=').append(JodaBeanUtils.toString(getUniqueId())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code PositionDocument}.
   */
  public static class Meta extends AbstractDocument.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code position} property.
     */
    private final MetaProperty<ManageablePosition> _position = DirectMetaProperty.ofReadWrite(
        this, "position", PositionDocument.class, ManageablePosition.class);
    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueId> _uniqueId = DirectMetaProperty.ofReadWrite(
        this, "uniqueId", PositionDocument.class, UniqueId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "position",
        "uniqueId");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 747804969:  // position
          return _position;
        case -294460212:  // uniqueId
          return _uniqueId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends PositionDocument> builder() {
      return new DirectBeanBuilder<PositionDocument>(new PositionDocument());
    }

    @Override
    public Class<? extends PositionDocument> beanType() {
      return PositionDocument.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code position} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ManageablePosition> position() {
      return _position;
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
        case 747804969:  // position
          return ((PositionDocument) bean).getPosition();
        case -294460212:  // uniqueId
          return ((PositionDocument) bean).getUniqueId();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 747804969:  // position
          ((PositionDocument) bean).setPosition((ManageablePosition) newValue);
          return;
        case -294460212:  // uniqueId
          ((PositionDocument) bean).setUniqueId((UniqueId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
