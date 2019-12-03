/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.ircurve.strips;

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

import com.opengamma.id.ExternalId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.time.Tenor;

/**
 * A swap future curve node.
 */
@BeanDefinition
public class DeliverableSwapFutureNode extends CurveNode {

  /** Serialization verison */
  private static final long serialVersionUID = 1L;

  /**
   * The future number.
   */
  @PropertyDefinition
  private int _futureNumber;

  /**
   * The start tenor.
   */
  @PropertyDefinition(validate = "notNull")
  private Tenor _startTenor;

  /**
   * The future tenor.
   */
  @PropertyDefinition(validate = "notNull")
  private Tenor _futureTenor;

  /**
   * The underlying tenor.
   */
  @PropertyDefinition(validate = "notNull")
  private Tenor _underlyingTenor;

  /**
   * The future convention.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _futureConvention;

  /**
   * The swap convention.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _swapConvention;

  /**
   * For the builder.
   */
  DeliverableSwapFutureNode() {
    super();
  }

  /**
   * @param futureNumber
   *          The future number, not null, greater than zero
   * @param startTenor
   *          The start tenor, not null
   * @param futureTenor
   *          The future tenor, not null
   * @param underlyingTenor
   *          The underlying tenor, not null
   * @param futureConvention
   *          The future convention, not null
   * @param underlyingConvention
   *          The underlying convention, not null
   * @param curveNodeIdMapperName
   *          The curve node id mapper name, not null
   */
  public DeliverableSwapFutureNode(final int futureNumber, final Tenor startTenor, final Tenor futureTenor, final Tenor underlyingTenor,
      final ExternalId futureConvention,
      final ExternalId underlyingConvention, final String curveNodeIdMapperName) {
    super(curveNodeIdMapperName);
    ArgumentChecker.notNegativeOrZero(futureNumber, "future number");
    setFutureNumber(futureNumber);
    setStartTenor(startTenor);
    setFutureTenor(futureTenor);
    setUnderlyingTenor(underlyingTenor);
    setFutureConvention(futureConvention);
    setSwapConvention(underlyingConvention);
  }

  /**
   * @param futureNumber
   *          The future number, not null, greater than zero
   * @param startTenor
   *          The start tenor, not null
   * @param futureTenor
   *          The future tenor, not null
   * @param underlyingTenor
   *          The underlying tenor, not null
   * @param futureConvention
   *          The future convention, not null
   * @param underlyingConvention
   *          The underlying convention, not null
   * @param curveNodeIdMapperName
   *          The curve node id mapper name, not null
   * @param name
   *          The node name
   */
  public DeliverableSwapFutureNode(final int futureNumber, final Tenor startTenor, final Tenor futureTenor, final Tenor underlyingTenor,
      final ExternalId futureConvention,
      final ExternalId underlyingConvention, final String curveNodeIdMapperName, final String name) {
    super(curveNodeIdMapperName, name);
    ArgumentChecker.notNegativeOrZero(futureNumber, "future number");
    setFutureNumber(futureNumber);
    setStartTenor(startTenor);
    setFutureTenor(futureTenor);
    setUnderlyingTenor(underlyingTenor);
    setFutureConvention(futureConvention);
    setSwapConvention(underlyingConvention);
  }

  @Override
  public Tenor getResolvedMaturity() {
    final int m = getFutureTenor().getPeriod().getMonths();
    return Tenor.of(getStartTenor().getPeriod().plusMonths(m * getFutureNumber()).plus(_underlyingTenor.getPeriod()));
  }

  @Override
  public <T> T accept(final CurveNodeVisitor<T> visitor) {
    ArgumentChecker.notNull(visitor, "visitor");
    return visitor.visitDeliverableSwapFutureNode(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code DeliverableSwapFutureNode}.
   * @return the meta-bean, not null
   */
  public static DeliverableSwapFutureNode.Meta meta() {
    return DeliverableSwapFutureNode.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(DeliverableSwapFutureNode.Meta.INSTANCE);
  }

  @Override
  public DeliverableSwapFutureNode.Meta metaBean() {
    return DeliverableSwapFutureNode.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the future number.
   * @return the value of the property
   */
  public int getFutureNumber() {
    return _futureNumber;
  }

  /**
   * Sets the future number.
   * @param futureNumber  the new value of the property
   */
  public void setFutureNumber(int futureNumber) {
    this._futureNumber = futureNumber;
  }

  /**
   * Gets the the {@code futureNumber} property.
   * @return the property, not null
   */
  public final Property<Integer> futureNumber() {
    return metaBean().futureNumber().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the start tenor.
   * @return the value of the property, not null
   */
  public Tenor getStartTenor() {
    return _startTenor;
  }

  /**
   * Sets the start tenor.
   * @param startTenor  the new value of the property, not null
   */
  public void setStartTenor(Tenor startTenor) {
    JodaBeanUtils.notNull(startTenor, "startTenor");
    this._startTenor = startTenor;
  }

  /**
   * Gets the the {@code startTenor} property.
   * @return the property, not null
   */
  public final Property<Tenor> startTenor() {
    return metaBean().startTenor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the future tenor.
   * @return the value of the property, not null
   */
  public Tenor getFutureTenor() {
    return _futureTenor;
  }

  /**
   * Sets the future tenor.
   * @param futureTenor  the new value of the property, not null
   */
  public void setFutureTenor(Tenor futureTenor) {
    JodaBeanUtils.notNull(futureTenor, "futureTenor");
    this._futureTenor = futureTenor;
  }

  /**
   * Gets the the {@code futureTenor} property.
   * @return the property, not null
   */
  public final Property<Tenor> futureTenor() {
    return metaBean().futureTenor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying tenor.
   * @return the value of the property, not null
   */
  public Tenor getUnderlyingTenor() {
    return _underlyingTenor;
  }

  /**
   * Sets the underlying tenor.
   * @param underlyingTenor  the new value of the property, not null
   */
  public void setUnderlyingTenor(Tenor underlyingTenor) {
    JodaBeanUtils.notNull(underlyingTenor, "underlyingTenor");
    this._underlyingTenor = underlyingTenor;
  }

  /**
   * Gets the the {@code underlyingTenor} property.
   * @return the property, not null
   */
  public final Property<Tenor> underlyingTenor() {
    return metaBean().underlyingTenor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the future convention.
   * @return the value of the property, not null
   */
  public ExternalId getFutureConvention() {
    return _futureConvention;
  }

  /**
   * Sets the future convention.
   * @param futureConvention  the new value of the property, not null
   */
  public void setFutureConvention(ExternalId futureConvention) {
    JodaBeanUtils.notNull(futureConvention, "futureConvention");
    this._futureConvention = futureConvention;
  }

  /**
   * Gets the the {@code futureConvention} property.
   * @return the property, not null
   */
  public final Property<ExternalId> futureConvention() {
    return metaBean().futureConvention().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the swap convention.
   * @return the value of the property, not null
   */
  public ExternalId getSwapConvention() {
    return _swapConvention;
  }

  /**
   * Sets the swap convention.
   * @param swapConvention  the new value of the property, not null
   */
  public void setSwapConvention(ExternalId swapConvention) {
    JodaBeanUtils.notNull(swapConvention, "swapConvention");
    this._swapConvention = swapConvention;
  }

  /**
   * Gets the the {@code swapConvention} property.
   * @return the property, not null
   */
  public final Property<ExternalId> swapConvention() {
    return metaBean().swapConvention().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public DeliverableSwapFutureNode clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DeliverableSwapFutureNode other = (DeliverableSwapFutureNode) obj;
      return (getFutureNumber() == other.getFutureNumber()) &&
          JodaBeanUtils.equal(getStartTenor(), other.getStartTenor()) &&
          JodaBeanUtils.equal(getFutureTenor(), other.getFutureTenor()) &&
          JodaBeanUtils.equal(getUnderlyingTenor(), other.getUnderlyingTenor()) &&
          JodaBeanUtils.equal(getFutureConvention(), other.getFutureConvention()) &&
          JodaBeanUtils.equal(getSwapConvention(), other.getSwapConvention()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getFutureNumber());
    hash = hash * 31 + JodaBeanUtils.hashCode(getStartTenor());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFutureTenor());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUnderlyingTenor());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFutureConvention());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSwapConvention());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(224);
    buf.append("DeliverableSwapFutureNode{");
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
    buf.append("futureNumber").append('=').append(JodaBeanUtils.toString(getFutureNumber())).append(',').append(' ');
    buf.append("startTenor").append('=').append(JodaBeanUtils.toString(getStartTenor())).append(',').append(' ');
    buf.append("futureTenor").append('=').append(JodaBeanUtils.toString(getFutureTenor())).append(',').append(' ');
    buf.append("underlyingTenor").append('=').append(JodaBeanUtils.toString(getUnderlyingTenor())).append(',').append(' ');
    buf.append("futureConvention").append('=').append(JodaBeanUtils.toString(getFutureConvention())).append(',').append(' ');
    buf.append("swapConvention").append('=').append(JodaBeanUtils.toString(getSwapConvention())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code DeliverableSwapFutureNode}.
   */
  public static class Meta extends CurveNode.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code futureNumber} property.
     */
    private final MetaProperty<Integer> _futureNumber = DirectMetaProperty.ofReadWrite(
        this, "futureNumber", DeliverableSwapFutureNode.class, Integer.TYPE);
    /**
     * The meta-property for the {@code startTenor} property.
     */
    private final MetaProperty<Tenor> _startTenor = DirectMetaProperty.ofReadWrite(
        this, "startTenor", DeliverableSwapFutureNode.class, Tenor.class);
    /**
     * The meta-property for the {@code futureTenor} property.
     */
    private final MetaProperty<Tenor> _futureTenor = DirectMetaProperty.ofReadWrite(
        this, "futureTenor", DeliverableSwapFutureNode.class, Tenor.class);
    /**
     * The meta-property for the {@code underlyingTenor} property.
     */
    private final MetaProperty<Tenor> _underlyingTenor = DirectMetaProperty.ofReadWrite(
        this, "underlyingTenor", DeliverableSwapFutureNode.class, Tenor.class);
    /**
     * The meta-property for the {@code futureConvention} property.
     */
    private final MetaProperty<ExternalId> _futureConvention = DirectMetaProperty.ofReadWrite(
        this, "futureConvention", DeliverableSwapFutureNode.class, ExternalId.class);
    /**
     * The meta-property for the {@code swapConvention} property.
     */
    private final MetaProperty<ExternalId> _swapConvention = DirectMetaProperty.ofReadWrite(
        this, "swapConvention", DeliverableSwapFutureNode.class, ExternalId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "futureNumber",
        "startTenor",
        "futureTenor",
        "underlyingTenor",
        "futureConvention",
        "swapConvention");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1052030700:  // futureNumber
          return _futureNumber;
        case -1583746178:  // startTenor
          return _startTenor;
        case -515187011:  // futureTenor
          return _futureTenor;
        case -824175261:  // underlyingTenor
          return _underlyingTenor;
        case 1736486292:  // futureConvention
          return _futureConvention;
        case 1414180196:  // swapConvention
          return _swapConvention;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends DeliverableSwapFutureNode> builder() {
      return new DirectBeanBuilder<DeliverableSwapFutureNode>(new DeliverableSwapFutureNode());
    }

    @Override
    public Class<? extends DeliverableSwapFutureNode> beanType() {
      return DeliverableSwapFutureNode.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code futureNumber} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Integer> futureNumber() {
      return _futureNumber;
    }

    /**
     * The meta-property for the {@code startTenor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Tenor> startTenor() {
      return _startTenor;
    }

    /**
     * The meta-property for the {@code futureTenor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Tenor> futureTenor() {
      return _futureTenor;
    }

    /**
     * The meta-property for the {@code underlyingTenor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Tenor> underlyingTenor() {
      return _underlyingTenor;
    }

    /**
     * The meta-property for the {@code futureConvention} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> futureConvention() {
      return _futureConvention;
    }

    /**
     * The meta-property for the {@code swapConvention} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> swapConvention() {
      return _swapConvention;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1052030700:  // futureNumber
          return ((DeliverableSwapFutureNode) bean).getFutureNumber();
        case -1583746178:  // startTenor
          return ((DeliverableSwapFutureNode) bean).getStartTenor();
        case -515187011:  // futureTenor
          return ((DeliverableSwapFutureNode) bean).getFutureTenor();
        case -824175261:  // underlyingTenor
          return ((DeliverableSwapFutureNode) bean).getUnderlyingTenor();
        case 1736486292:  // futureConvention
          return ((DeliverableSwapFutureNode) bean).getFutureConvention();
        case 1414180196:  // swapConvention
          return ((DeliverableSwapFutureNode) bean).getSwapConvention();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1052030700:  // futureNumber
          ((DeliverableSwapFutureNode) bean).setFutureNumber((Integer) newValue);
          return;
        case -1583746178:  // startTenor
          ((DeliverableSwapFutureNode) bean).setStartTenor((Tenor) newValue);
          return;
        case -515187011:  // futureTenor
          ((DeliverableSwapFutureNode) bean).setFutureTenor((Tenor) newValue);
          return;
        case -824175261:  // underlyingTenor
          ((DeliverableSwapFutureNode) bean).setUnderlyingTenor((Tenor) newValue);
          return;
        case 1736486292:  // futureConvention
          ((DeliverableSwapFutureNode) bean).setFutureConvention((ExternalId) newValue);
          return;
        case 1414180196:  // swapConvention
          ((DeliverableSwapFutureNode) bean).setSwapConvention((ExternalId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((DeliverableSwapFutureNode) bean)._startTenor, "startTenor");
      JodaBeanUtils.notNull(((DeliverableSwapFutureNode) bean)._futureTenor, "futureTenor");
      JodaBeanUtils.notNull(((DeliverableSwapFutureNode) bean)._underlyingTenor, "underlyingTenor");
      JodaBeanUtils.notNull(((DeliverableSwapFutureNode) bean)._futureConvention, "futureConvention");
      JodaBeanUtils.notNull(((DeliverableSwapFutureNode) bean)._swapConvention, "swapConvention");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
