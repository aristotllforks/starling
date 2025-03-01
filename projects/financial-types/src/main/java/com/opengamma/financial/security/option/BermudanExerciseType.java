/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.option;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * The Bermudan exercise type.
 */
@BeanDefinition
public class BermudanExerciseType extends ExerciseType {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;
  /**
   * Creates an empty instance.
   */
  public BermudanExerciseType() {
  }

  @Override
  public String getName() {
    return ExerciseTypeNameVisitor.BERMUDAN;
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(final ExerciseTypeVisitor<T> visitor) {
    return visitor.visitBermudanExerciseType(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code BermudanExerciseType}.
   * @return the meta-bean, not null
   */
  public static BermudanExerciseType.Meta meta() {
    return BermudanExerciseType.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(BermudanExerciseType.Meta.INSTANCE);
  }

  @Override
  public BermudanExerciseType.Meta metaBean() {
    return BermudanExerciseType.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public BermudanExerciseType clone() {
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
    buf.append("BermudanExerciseType{");
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
   * The meta-bean for {@code BermudanExerciseType}.
   */
  public static class Meta extends ExerciseType.Meta {
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
    public BeanBuilder<? extends BermudanExerciseType> builder() {
      return new DirectBeanBuilder<BermudanExerciseType>(new BermudanExerciseType());
    }

    @Override
    public Class<? extends BermudanExerciseType> beanType() {
      return BermudanExerciseType.class;
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
