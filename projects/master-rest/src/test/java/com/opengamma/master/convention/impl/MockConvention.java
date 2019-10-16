/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.convention.impl;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.convention.ConventionType;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.UniqueId;
import com.opengamma.master.convention.ManageableConvention;
import com.opengamma.util.money.Currency;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Mock convention.
 */
@BeanDefinition
public class MockConvention extends ManageableConvention {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;

  /**
   * Creates an instance.
   */
  protected MockConvention() {
  }

  /**
   * Creates an instance.
   *
   * @param name  the name, not null
   * @param bundle  the bundle, not null
   * @param currency  the currency, not null
   */
  public MockConvention(final String name, final ExternalIdBundle bundle, final Currency currency) {
    super(name, bundle);
    setCurrency(currency);
  }

  /**
   * Creates an instance.
   *
   * @param uniqueId  the unique identifier, not null
   * @param name  the name, not null
   * @param bundle  the bundle, not null
   * @param currency  the currency, not null
   */
  public MockConvention(final UniqueId uniqueId, final String name, final ExternalIdBundle bundle, final Currency currency) {
    super(uniqueId, name, bundle);
    setCurrency(currency);
  }

  //-------------------------------------------------------------------------
  @Override
  public ConventionType getConventionType() {
    return ConventionType.of("MOCK");
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code MockConvention}.
   * @return the meta-bean, not null
   */
  public static MockConvention.Meta meta() {
    return MockConvention.Meta.INSTANCE;
  }

  static {
    MetaBean.register(MockConvention.Meta.INSTANCE);
  }

  @Override
  public MockConvention.Meta metaBean() {
    return MockConvention.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency.
   * @param currency  the new value of the property, not null
   */
  public void setCurrency(Currency currency) {
    JodaBeanUtils.notNull(currency, "currency");
    this._currency = currency;
  }

  /**
   * Gets the the {@code currency} property.
   * @return the property, not null
   */
  public final Property<Currency> currency() {
    return metaBean().currency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public MockConvention clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MockConvention other = (MockConvention) obj;
      return JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("MockConvention{");
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
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MockConvention}.
   */
  public static class Meta extends ManageableConvention.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", MockConvention.class, Currency.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "currency");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return _currency;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MockConvention> builder() {
      return new DirectBeanBuilder<>(new MockConvention());
    }

    @Override
    public Class<? extends MockConvention> beanType() {
      return MockConvention.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return ((MockConvention) bean).getCurrency();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          ((MockConvention) bean).setCurrency((Currency) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((MockConvention) bean)._currency, "currency");
      super.validate(bean);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
