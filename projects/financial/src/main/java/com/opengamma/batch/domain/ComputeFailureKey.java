/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.batch.domain;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.ArgumentChecker;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Data model for a compute failure key.
 */
@BeanDefinition
public class ComputeFailureKey extends DirectBean {

  @PropertyDefinition
  private String _functionId;

  @PropertyDefinition
  private String _exceptionClass;

  @PropertyDefinition
  private String _exceptionMsg;

  @PropertyDefinition
  private String _stackTrace;

  public ComputeFailureKey(final String functionId, final String exceptionClass, final String exceptionMsg, final String stackTrace) {
    ArgumentChecker.notNull(functionId, "functionId");
    ArgumentChecker.notNull(exceptionClass, "exceptionClass");
    ArgumentChecker.notNull(stackTrace, "stackTrace");
    _functionId = functionId;
    _exceptionClass = exceptionClass;
    _stackTrace = stackTrace.substring(0, Math.min(stackTrace.length(), 2000));
    if (exceptionMsg == null) {
      _exceptionMsg = ""; // although Throwable.getMessage() can return null, our db doesn't allow nulls
    } else {
      _exceptionMsg = exceptionMsg.substring(0, Math.min(exceptionMsg.length(), 255));
    }
  }

  private ComputeFailureKey() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code ComputeFailureKey}.
   * @return the meta-bean, not null
   */
  public static ComputeFailureKey.Meta meta() {
    return ComputeFailureKey.Meta.INSTANCE;
  }

  static {
    MetaBean.register(ComputeFailureKey.Meta.INSTANCE);
  }

  @Override
  public ComputeFailureKey.Meta metaBean() {
    return ComputeFailureKey.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the functionId.
   * @return the value of the property
   */
  public String getFunctionId() {
    return _functionId;
  }

  /**
   * Sets the functionId.
   * @param functionId  the new value of the property
   */
  public void setFunctionId(String functionId) {
    this._functionId = functionId;
  }

  /**
   * Gets the the {@code functionId} property.
   * @return the property, not null
   */
  public final Property<String> functionId() {
    return metaBean().functionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exceptionClass.
   * @return the value of the property
   */
  public String getExceptionClass() {
    return _exceptionClass;
  }

  /**
   * Sets the exceptionClass.
   * @param exceptionClass  the new value of the property
   */
  public void setExceptionClass(String exceptionClass) {
    this._exceptionClass = exceptionClass;
  }

  /**
   * Gets the the {@code exceptionClass} property.
   * @return the property, not null
   */
  public final Property<String> exceptionClass() {
    return metaBean().exceptionClass().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exceptionMsg.
   * @return the value of the property
   */
  public String getExceptionMsg() {
    return _exceptionMsg;
  }

  /**
   * Sets the exceptionMsg.
   * @param exceptionMsg  the new value of the property
   */
  public void setExceptionMsg(String exceptionMsg) {
    this._exceptionMsg = exceptionMsg;
  }

  /**
   * Gets the the {@code exceptionMsg} property.
   * @return the property, not null
   */
  public final Property<String> exceptionMsg() {
    return metaBean().exceptionMsg().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the stackTrace.
   * @return the value of the property
   */
  public String getStackTrace() {
    return _stackTrace;
  }

  /**
   * Sets the stackTrace.
   * @param stackTrace  the new value of the property
   */
  public void setStackTrace(String stackTrace) {
    this._stackTrace = stackTrace;
  }

  /**
   * Gets the the {@code stackTrace} property.
   * @return the property, not null
   */
  public final Property<String> stackTrace() {
    return metaBean().stackTrace().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public ComputeFailureKey clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ComputeFailureKey other = (ComputeFailureKey) obj;
      return JodaBeanUtils.equal(getFunctionId(), other.getFunctionId()) &&
          JodaBeanUtils.equal(getExceptionClass(), other.getExceptionClass()) &&
          JodaBeanUtils.equal(getExceptionMsg(), other.getExceptionMsg()) &&
          JodaBeanUtils.equal(getStackTrace(), other.getStackTrace());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getFunctionId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExceptionClass());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExceptionMsg());
    hash = hash * 31 + JodaBeanUtils.hashCode(getStackTrace());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("ComputeFailureKey{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("functionId").append('=').append(JodaBeanUtils.toString(getFunctionId())).append(',').append(' ');
    buf.append("exceptionClass").append('=').append(JodaBeanUtils.toString(getExceptionClass())).append(',').append(' ');
    buf.append("exceptionMsg").append('=').append(JodaBeanUtils.toString(getExceptionMsg())).append(',').append(' ');
    buf.append("stackTrace").append('=').append(JodaBeanUtils.toString(getStackTrace())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ComputeFailureKey}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code functionId} property.
     */
    private final MetaProperty<String> _functionId = DirectMetaProperty.ofReadWrite(
        this, "functionId", ComputeFailureKey.class, String.class);
    /**
     * The meta-property for the {@code exceptionClass} property.
     */
    private final MetaProperty<String> _exceptionClass = DirectMetaProperty.ofReadWrite(
        this, "exceptionClass", ComputeFailureKey.class, String.class);
    /**
     * The meta-property for the {@code exceptionMsg} property.
     */
    private final MetaProperty<String> _exceptionMsg = DirectMetaProperty.ofReadWrite(
        this, "exceptionMsg", ComputeFailureKey.class, String.class);
    /**
     * The meta-property for the {@code stackTrace} property.
     */
    private final MetaProperty<String> _stackTrace = DirectMetaProperty.ofReadWrite(
        this, "stackTrace", ComputeFailureKey.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "functionId",
        "exceptionClass",
        "exceptionMsg",
        "stackTrace");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -62789869:  // functionId
          return _functionId;
        case -71056791:  // exceptionClass
          return _exceptionClass;
        case -268220238:  // exceptionMsg
          return _exceptionMsg;
        case 2026279837:  // stackTrace
          return _stackTrace;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ComputeFailureKey> builder() {
      return new DirectBeanBuilder<>(new ComputeFailureKey());
    }

    @Override
    public Class<? extends ComputeFailureKey> beanType() {
      return ComputeFailureKey.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code functionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> functionId() {
      return _functionId;
    }

    /**
     * The meta-property for the {@code exceptionClass} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> exceptionClass() {
      return _exceptionClass;
    }

    /**
     * The meta-property for the {@code exceptionMsg} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> exceptionMsg() {
      return _exceptionMsg;
    }

    /**
     * The meta-property for the {@code stackTrace} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> stackTrace() {
      return _stackTrace;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -62789869:  // functionId
          return ((ComputeFailureKey) bean).getFunctionId();
        case -71056791:  // exceptionClass
          return ((ComputeFailureKey) bean).getExceptionClass();
        case -268220238:  // exceptionMsg
          return ((ComputeFailureKey) bean).getExceptionMsg();
        case 2026279837:  // stackTrace
          return ((ComputeFailureKey) bean).getStackTrace();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -62789869:  // functionId
          ((ComputeFailureKey) bean).setFunctionId((String) newValue);
          return;
        case -71056791:  // exceptionClass
          ((ComputeFailureKey) bean).setExceptionClass((String) newValue);
          return;
        case -268220238:  // exceptionMsg
          ((ComputeFailureKey) bean).setExceptionMsg((String) newValue);
          return;
        case 2026279837:  // stackTrace
          ((ComputeFailureKey) bean).setStackTrace((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
