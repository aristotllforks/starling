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
 * The European exercise type.
 */
@BeanDefinition
public class EuropeanExerciseType extends ExerciseType {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;
  /**
   * Creates an empty instance.
   */
  public EuropeanExerciseType() {
  }

  @Override
  public String getName() {
    return ExerciseTypeNameVisitor.EUROPEAN;
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(final ExerciseTypeVisitor<T> visitor) {
    return visitor.visitEuropeanExerciseType(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code EuropeanExerciseType}.
   * @return the meta-bean, not null
   */
  public static EuropeanExerciseType.Meta meta() {
    return EuropeanExerciseType.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(EuropeanExerciseType.Meta.INSTANCE);
  }

  @Override
  public EuropeanExerciseType.Meta metaBean() {
    return EuropeanExerciseType.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public EuropeanExerciseType clone() {
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
    buf.append("EuropeanExerciseType{");
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
   * The meta-bean for {@code EuropeanExerciseType}.
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
    public BeanBuilder<? extends EuropeanExerciseType> builder() {
      return new DirectBeanBuilder<EuropeanExerciseType>(new EuropeanExerciseType());
    }

    @Override
    public Class<? extends EuropeanExerciseType> beanType() {
      return EuropeanExerciseType.class;
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
