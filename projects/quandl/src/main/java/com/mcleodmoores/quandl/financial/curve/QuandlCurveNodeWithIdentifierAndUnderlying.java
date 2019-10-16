/**
 * Copyright (C) 2015 - Present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.quandl.financial.curve;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.analytics.ircurve.strips.CurveNode;
import com.opengamma.financial.analytics.ircurve.strips.CurveNodeWithIdentifier;
import com.opengamma.financial.analytics.ircurve.strips.DataFieldType;
import com.opengamma.id.ExternalId;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Contains a curve node that holds information about the underlying instrument (e.g. the underlying overnight rate for Fed funds futures).
 */
@BeanDefinition
public class QuandlCurveNodeWithIdentifierAndUnderlying extends CurveNodeWithIdentifier {
  /**
   * The underlying market data id.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _underlyingIdentifier;

  /**
   * The underlying market data field.
   */
  @PropertyDefinition(validate = "notNull")
  private String _underlyingDataField;

  /**
   * Creates an instance.
   *
   * @param node
   *          the curve node, not null
   * @param id
   *          the market data id, not null
   * @param dataField
   *          the data field, not null
   * @param fieldType
   *          the field type, not null
   * @param underlyingIdentifier
   *          the underlying market data id, not null
   * @param underlyingDataField
   *          the underlying market data field, not null
   */
  public QuandlCurveNodeWithIdentifierAndUnderlying(final CurveNode node, final ExternalId id, final String dataField, final DataFieldType fieldType,
      final ExternalId underlyingIdentifier, final String underlyingDataField) {
    super(node, id, dataField, fieldType);
    setUnderlyingIdentifier(underlyingIdentifier);
    setUnderlyingDataField(underlyingDataField);
  }

  /**
   * Creates an instance.
   */
  protected QuandlCurveNodeWithIdentifierAndUnderlying() {
    super();
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code QuandlCurveNodeWithIdentifierAndUnderlying}.
   * @return the meta-bean, not null
   */
  public static QuandlCurveNodeWithIdentifierAndUnderlying.Meta meta() {
    return QuandlCurveNodeWithIdentifierAndUnderlying.Meta.INSTANCE;
  }

  static {
    MetaBean.register(QuandlCurveNodeWithIdentifierAndUnderlying.Meta.INSTANCE);
  }

  @Override
  public QuandlCurveNodeWithIdentifierAndUnderlying.Meta metaBean() {
    return QuandlCurveNodeWithIdentifierAndUnderlying.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying market data id.
   * @return the value of the property, not null
   */
  public ExternalId getUnderlyingIdentifier() {
    return _underlyingIdentifier;
  }

  /**
   * Sets the underlying market data id.
   * @param underlyingIdentifier  the new value of the property, not null
   */
  public void setUnderlyingIdentifier(ExternalId underlyingIdentifier) {
    JodaBeanUtils.notNull(underlyingIdentifier, "underlyingIdentifier");
    this._underlyingIdentifier = underlyingIdentifier;
  }

  /**
   * Gets the the {@code underlyingIdentifier} property.
   * @return the property, not null
   */
  public final Property<ExternalId> underlyingIdentifier() {
    return metaBean().underlyingIdentifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying market data field.
   * @return the value of the property, not null
   */
  public String getUnderlyingDataField() {
    return _underlyingDataField;
  }

  /**
   * Sets the underlying market data field.
   * @param underlyingDataField  the new value of the property, not null
   */
  public void setUnderlyingDataField(String underlyingDataField) {
    JodaBeanUtils.notNull(underlyingDataField, "underlyingDataField");
    this._underlyingDataField = underlyingDataField;
  }

  /**
   * Gets the the {@code underlyingDataField} property.
   * @return the property, not null
   */
  public final Property<String> underlyingDataField() {
    return metaBean().underlyingDataField().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public QuandlCurveNodeWithIdentifierAndUnderlying clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      QuandlCurveNodeWithIdentifierAndUnderlying other = (QuandlCurveNodeWithIdentifierAndUnderlying) obj;
      return JodaBeanUtils.equal(getUnderlyingIdentifier(), other.getUnderlyingIdentifier()) &&
          JodaBeanUtils.equal(getUnderlyingDataField(), other.getUnderlyingDataField()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnderlyingIdentifier());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnderlyingDataField());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("QuandlCurveNodeWithIdentifierAndUnderlying{");
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
    buf.append("underlyingIdentifier").append('=').append(JodaBeanUtils.toString(getUnderlyingIdentifier())).append(',').append(' ');
    buf.append("underlyingDataField").append('=').append(JodaBeanUtils.toString(getUnderlyingDataField())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code QuandlCurveNodeWithIdentifierAndUnderlying}.
   */
  public static class Meta extends CurveNodeWithIdentifier.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code underlyingIdentifier} property.
     */
    private final MetaProperty<ExternalId> _underlyingIdentifier = DirectMetaProperty.ofReadWrite(
        this, "underlyingIdentifier", QuandlCurveNodeWithIdentifierAndUnderlying.class, ExternalId.class);
    /**
     * The meta-property for the {@code underlyingDataField} property.
     */
    private final MetaProperty<String> _underlyingDataField = DirectMetaProperty.ofReadWrite(
        this, "underlyingDataField", QuandlCurveNodeWithIdentifierAndUnderlying.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "underlyingIdentifier",
        "underlyingDataField");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 368639974:  // underlyingIdentifier
          return _underlyingIdentifier;
        case -876884845:  // underlyingDataField
          return _underlyingDataField;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends QuandlCurveNodeWithIdentifierAndUnderlying> builder() {
      return new DirectBeanBuilder<>(new QuandlCurveNodeWithIdentifierAndUnderlying());
    }

    @Override
    public Class<? extends QuandlCurveNodeWithIdentifierAndUnderlying> beanType() {
      return QuandlCurveNodeWithIdentifierAndUnderlying.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code underlyingIdentifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> underlyingIdentifier() {
      return _underlyingIdentifier;
    }

    /**
     * The meta-property for the {@code underlyingDataField} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> underlyingDataField() {
      return _underlyingDataField;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 368639974:  // underlyingIdentifier
          return ((QuandlCurveNodeWithIdentifierAndUnderlying) bean).getUnderlyingIdentifier();
        case -876884845:  // underlyingDataField
          return ((QuandlCurveNodeWithIdentifierAndUnderlying) bean).getUnderlyingDataField();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 368639974:  // underlyingIdentifier
          ((QuandlCurveNodeWithIdentifierAndUnderlying) bean).setUnderlyingIdentifier((ExternalId) newValue);
          return;
        case -876884845:  // underlyingDataField
          ((QuandlCurveNodeWithIdentifierAndUnderlying) bean).setUnderlyingDataField((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((QuandlCurveNodeWithIdentifierAndUnderlying) bean)._underlyingIdentifier, "underlyingIdentifier");
      JodaBeanUtils.notNull(((QuandlCurveNodeWithIdentifierAndUnderlying) bean)._underlyingDataField, "underlyingDataField");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
