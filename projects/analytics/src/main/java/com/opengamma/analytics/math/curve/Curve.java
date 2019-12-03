/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.curve;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.ObjectUtils;
import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.analytics.math.function.Function1D;
import com.opengamma.util.ArgumentChecker;

/**
 * Defines a general curve <i>(x, y)</i> class. The <i>x</i> and <i>y</i> data can be any type.
 * The curves are named; if a name is not provided then a unique ID will be used.
 *
 * @param <T>  the type of the <i>x</i> data
 * @param <U>  the type of the <i>y</i> data
 */
@BeanDefinition
public abstract class Curve<T extends Comparable<T>, U>
    implements Bean {

  /**
   * Atomic used to generate a name.
   */
  private static final AtomicLong ATOMIC = new AtomicLong();

  /**
   * The curve name.
   */
  @PropertyDefinition(validate = "notNull", set = "private")
  private String _name;

  /**
   * Constructs a curve with an automatically-generated name.
   */
  protected Curve() {
    this(Long.toString(ATOMIC.getAndIncrement()));
  }

  /**
   * Constructs a curve with the given name.
   *
   * @param name  the name of the curve, not null
   */
  protected Curve(final String name) {
    ArgumentChecker.notNull(name, "name");
    _name = name;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the <i>x</i> data for this curve.
   *
   * @return the <i>x</i> data for this curve, not null
   */
  public abstract T[] getXData();

  /**
   * Gets the <i>y</i> data for this curve.
   *
   * @return the <i>y</i> data for this curve, not null
   */
  public abstract U[] getYData();

  /**
   * Gets the number of data points used to construct this curve.
   *
   * @return the number of data points used to construct this curve
   */
  public abstract int size();

  //-------------------------------------------------------------------------
  /**
   * Given an <i>x</i> value, return the <i>y</i> value from this curve.
   *
   * @param x  the <i>x</i> value, not null
   * @return the <i>y</i> value, not null
   */
  public abstract U getYValue(T x);

  /**
   * Converts a curve to a Function1D.
   *
   * @return the curve as a mapping {@code f(x) -> y}, not null
   */
  public Function1D<T, U> toFunction1D() {
    return new Function1D<T, U>() {
      @Override
      public U evaluate(final T x) {
        return Curve.this.getYValue(x);
      }
    };
  }

  //-------------------------------------------------------------------------
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (_name == null ? 0 : _name.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Curve<?, ?> other = (Curve<?, ?>) obj;
    return ObjectUtils.equals(_name, other._name);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code Curve}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("rawtypes")
  public static Curve.Meta meta() {
    return Curve.Meta.INSTANCE;
  }

  /**
   * The meta-bean for {@code Curve}.
   * @param <R>  the first generic type
   * @param <S>  the second generic type
   * @param cls1  the first generic type
   * @param cls2  the second generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R extends Comparable<R>, S> Curve.Meta<R, S> metaCurve(Class<R> cls1, Class<S> cls2) {
    return Curve.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(Curve.Meta.INSTANCE);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Curve.Meta<T, U> metaBean() {
    return Curve.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the curve name.
   * @return the value of the property, not null
   */
  public String getName() {
    return _name;
  }

  /**
   * Sets the curve name.
   * @param name  the new value of the property, not null
   */
  private void setName(String name) {
    JodaBeanUtils.notNull(name, "name");
    this._name = name;
  }

  /**
   * Gets the the {@code name} property.
   * @return the property, not null
   */
  public final Property<String> name() {
    return metaBean().name().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public Curve<T, U> clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("Curve{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code Curve}.
   * @param <T>  the type
   * @param <U>  the type
   */
  public static class Meta<T extends Comparable<T>, U> extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", Curve.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "name");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return _name;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends Curve<T, U>> builder() {
      throw new UnsupportedOperationException("Curve is an abstract class");
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends Curve<T, U>> beanType() {
      return (Class) Curve.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return ((Curve<?, ?>) bean).getName();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          ((Curve<T, U>) bean).setName((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((Curve<?, ?>) bean)._name, "name");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
