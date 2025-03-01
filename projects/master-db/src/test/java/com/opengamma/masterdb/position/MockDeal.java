/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.position;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.master.position.Deal;
import com.opengamma.util.ArgumentChecker;

/**
 * Mock deal for testing purpose
 */
@BeanDefinition
/*package*/class MockDeal extends DirectBean implements Deal {

  @PropertyDefinition
  private String _propertyOne;

  @PropertyDefinition
  private String _propertyTwo;

  protected MockDeal() {
  }

  protected MockDeal(final String propertyOne, final String propertyTwo) {
    ArgumentChecker.notNull(propertyOne, "propertyOne");
    ArgumentChecker.notNull(propertyTwo, "propertyTwo");

    setPropertyOne(propertyOne);
    setPropertyTwo(propertyTwo);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MockDeal}.
   * @return the meta-bean, not null
   */
  public static MockDeal.Meta meta() {
    return MockDeal.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MockDeal.Meta.INSTANCE);
  }

  @Override
  public MockDeal.Meta metaBean() {
    return MockDeal.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the propertyOne.
   * @return the value of the property
   */
  public String getPropertyOne() {
    return _propertyOne;
  }

  /**
   * Sets the propertyOne.
   * @param propertyOne  the new value of the property
   */
  public void setPropertyOne(String propertyOne) {
    this._propertyOne = propertyOne;
  }

  /**
   * Gets the the {@code propertyOne} property.
   * @return the property, not null
   */
  public final Property<String> propertyOne() {
    return metaBean().propertyOne().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the propertyTwo.
   * @return the value of the property
   */
  public String getPropertyTwo() {
    return _propertyTwo;
  }

  /**
   * Sets the propertyTwo.
   * @param propertyTwo  the new value of the property
   */
  public void setPropertyTwo(String propertyTwo) {
    this._propertyTwo = propertyTwo;
  }

  /**
   * Gets the the {@code propertyTwo} property.
   * @return the property, not null
   */
  public final Property<String> propertyTwo() {
    return metaBean().propertyTwo().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public MockDeal clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MockDeal other = (MockDeal) obj;
      return JodaBeanUtils.equal(getPropertyOne(), other.getPropertyOne()) &&
          JodaBeanUtils.equal(getPropertyTwo(), other.getPropertyTwo());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getPropertyOne());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPropertyTwo());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("MockDeal{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("propertyOne").append('=').append(JodaBeanUtils.toString(getPropertyOne())).append(',').append(' ');
    buf.append("propertyTwo").append('=').append(JodaBeanUtils.toString(getPropertyTwo())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MockDeal}.
   */
  static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code propertyOne} property.
     */
    private final MetaProperty<String> _propertyOne = DirectMetaProperty.ofReadWrite(
        this, "propertyOne", MockDeal.class, String.class);
    /**
     * The meta-property for the {@code propertyTwo} property.
     */
    private final MetaProperty<String> _propertyTwo = DirectMetaProperty.ofReadWrite(
        this, "propertyTwo", MockDeal.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "propertyOne",
        "propertyTwo");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1357581393:  // propertyOne
          return _propertyOne;
        case 1357586487:  // propertyTwo
          return _propertyTwo;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MockDeal> builder() {
      return new DirectBeanBuilder<MockDeal>(new MockDeal());
    }

    @Override
    public Class<? extends MockDeal> beanType() {
      return MockDeal.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code propertyOne} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> propertyOne() {
      return _propertyOne;
    }

    /**
     * The meta-property for the {@code propertyTwo} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> propertyTwo() {
      return _propertyTwo;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1357581393:  // propertyOne
          return ((MockDeal) bean).getPropertyOne();
        case 1357586487:  // propertyTwo
          return ((MockDeal) bean).getPropertyTwo();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1357581393:  // propertyOne
          ((MockDeal) bean).setPropertyOne((String) newValue);
          return;
        case 1357586487:  // propertyTwo
          ((MockDeal) bean).setPropertyTwo((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
