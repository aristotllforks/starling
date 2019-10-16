/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.integration.tool.portfolio.xml.v1_0.conversion.ListedTradeSecurityExtractor;
import com.opengamma.integration.tool.portfolio.xml.v1_0.conversion.TradeSecurityExtractor;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

@XmlRootElement
@BeanDefinition
public class ListedSecurityTrade extends Trade {

  @XmlElement(name = "buySell", required = true)
  @PropertyDefinition
  private BuySell _buySell;

  @XmlElement(name = "numContracts", required = true)
  @PropertyDefinition
  private int _numContracts;

  @XmlElementWrapper(name = "brokers")
  @XmlElement(name = "broker")
  @PropertyDefinition
  private List<Broker> _brokers;

  @XmlElementRef
  @PropertyDefinition
  private ListedSecurityDefinition _listedSecurityDefinition;

  @Override
  public boolean canBePositionAggregated() {
    return true;
  }

  @Override
  public TradeSecurityExtractor getSecurityExtractor() {
    return new ListedTradeSecurityExtractor(this);
  }

  @Override
  public BigDecimal getQuantity() {
    final BigDecimal qty = new BigDecimal(getNumContracts());
    return getBuySell() == BuySell.BUY ? qty : qty.negate();
  }

  @Override
  public IdWrapper getCounterparty() {
    final IdWrapper counterparty = new IdWrapper();
    final ExtId extId = new ExtId();
    extId.setScheme("CPARTY");
    extId.setId(_listedSecurityDefinition.getExchange());
    counterparty.setExternalId(extId);
    return counterparty;
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code ListedSecurityTrade}.
   * @return the meta-bean, not null
   */
  public static ListedSecurityTrade.Meta meta() {
    return ListedSecurityTrade.Meta.INSTANCE;
  }

  static {
    MetaBean.register(ListedSecurityTrade.Meta.INSTANCE);
  }

  @Override
  public ListedSecurityTrade.Meta metaBean() {
    return ListedSecurityTrade.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the buySell.
   * @return the value of the property
   */
  public BuySell getBuySell() {
    return _buySell;
  }

  /**
   * Sets the buySell.
   * @param buySell  the new value of the property
   */
  public void setBuySell(BuySell buySell) {
    this._buySell = buySell;
  }

  /**
   * Gets the the {@code buySell} property.
   * @return the property, not null
   */
  public final Property<BuySell> buySell() {
    return metaBean().buySell().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the numContracts.
   * @return the value of the property
   */
  public int getNumContracts() {
    return _numContracts;
  }

  /**
   * Sets the numContracts.
   * @param numContracts  the new value of the property
   */
  public void setNumContracts(int numContracts) {
    this._numContracts = numContracts;
  }

  /**
   * Gets the the {@code numContracts} property.
   * @return the property, not null
   */
  public final Property<Integer> numContracts() {
    return metaBean().numContracts().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the brokers.
   * @return the value of the property
   */
  public List<Broker> getBrokers() {
    return _brokers;
  }

  /**
   * Sets the brokers.
   * @param brokers  the new value of the property
   */
  public void setBrokers(List<Broker> brokers) {
    this._brokers = brokers;
  }

  /**
   * Gets the the {@code brokers} property.
   * @return the property, not null
   */
  public final Property<List<Broker>> brokers() {
    return metaBean().brokers().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the listedSecurityDefinition.
   * @return the value of the property
   */
  public ListedSecurityDefinition getListedSecurityDefinition() {
    return _listedSecurityDefinition;
  }

  /**
   * Sets the listedSecurityDefinition.
   * @param listedSecurityDefinition  the new value of the property
   */
  public void setListedSecurityDefinition(ListedSecurityDefinition listedSecurityDefinition) {
    this._listedSecurityDefinition = listedSecurityDefinition;
  }

  /**
   * Gets the the {@code listedSecurityDefinition} property.
   * @return the property, not null
   */
  public final Property<ListedSecurityDefinition> listedSecurityDefinition() {
    return metaBean().listedSecurityDefinition().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public ListedSecurityTrade clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ListedSecurityTrade other = (ListedSecurityTrade) obj;
      return JodaBeanUtils.equal(getBuySell(), other.getBuySell()) &&
          (getNumContracts() == other.getNumContracts()) &&
          JodaBeanUtils.equal(getBrokers(), other.getBrokers()) &&
          JodaBeanUtils.equal(getListedSecurityDefinition(), other.getListedSecurityDefinition()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getBuySell());
    hash = hash * 31 + JodaBeanUtils.hashCode(getNumContracts());
    hash = hash * 31 + JodaBeanUtils.hashCode(getBrokers());
    hash = hash * 31 + JodaBeanUtils.hashCode(getListedSecurityDefinition());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("ListedSecurityTrade{");
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
    buf.append("buySell").append('=').append(JodaBeanUtils.toString(getBuySell())).append(',').append(' ');
    buf.append("numContracts").append('=').append(JodaBeanUtils.toString(getNumContracts())).append(',').append(' ');
    buf.append("brokers").append('=').append(JodaBeanUtils.toString(getBrokers())).append(',').append(' ');
    buf.append("listedSecurityDefinition").append('=').append(JodaBeanUtils.toString(getListedSecurityDefinition())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ListedSecurityTrade}.
   */
  public static class Meta extends Trade.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code buySell} property.
     */
    private final MetaProperty<BuySell> _buySell = DirectMetaProperty.ofReadWrite(
        this, "buySell", ListedSecurityTrade.class, BuySell.class);
    /**
     * The meta-property for the {@code numContracts} property.
     */
    private final MetaProperty<Integer> _numContracts = DirectMetaProperty.ofReadWrite(
        this, "numContracts", ListedSecurityTrade.class, Integer.TYPE);
    /**
     * The meta-property for the {@code brokers} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<Broker>> _brokers = DirectMetaProperty.ofReadWrite(
        this, "brokers", ListedSecurityTrade.class, (Class) List.class);
    /**
     * The meta-property for the {@code listedSecurityDefinition} property.
     */
    private final MetaProperty<ListedSecurityDefinition> _listedSecurityDefinition = DirectMetaProperty.ofReadWrite(
        this, "listedSecurityDefinition", ListedSecurityTrade.class, ListedSecurityDefinition.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "buySell",
        "numContracts",
        "brokers",
        "listedSecurityDefinition");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 244977400:  // buySell
          return _buySell;
        case -1107354437:  // numContracts
          return _numContracts;
        case 150569914:  // brokers
          return _brokers;
        case -163631792:  // listedSecurityDefinition
          return _listedSecurityDefinition;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ListedSecurityTrade> builder() {
      return new DirectBeanBuilder<>(new ListedSecurityTrade());
    }

    @Override
    public Class<? extends ListedSecurityTrade> beanType() {
      return ListedSecurityTrade.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code buySell} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BuySell> buySell() {
      return _buySell;
    }

    /**
     * The meta-property for the {@code numContracts} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Integer> numContracts() {
      return _numContracts;
    }

    /**
     * The meta-property for the {@code brokers} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<Broker>> brokers() {
      return _brokers;
    }

    /**
     * The meta-property for the {@code listedSecurityDefinition} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ListedSecurityDefinition> listedSecurityDefinition() {
      return _listedSecurityDefinition;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 244977400:  // buySell
          return ((ListedSecurityTrade) bean).getBuySell();
        case -1107354437:  // numContracts
          return ((ListedSecurityTrade) bean).getNumContracts();
        case 150569914:  // brokers
          return ((ListedSecurityTrade) bean).getBrokers();
        case -163631792:  // listedSecurityDefinition
          return ((ListedSecurityTrade) bean).getListedSecurityDefinition();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 244977400:  // buySell
          ((ListedSecurityTrade) bean).setBuySell((BuySell) newValue);
          return;
        case -1107354437:  // numContracts
          ((ListedSecurityTrade) bean).setNumContracts((Integer) newValue);
          return;
        case 150569914:  // brokers
          ((ListedSecurityTrade) bean).setBrokers((List<Broker>) newValue);
          return;
        case -163631792:  // listedSecurityDefinition
          ((ListedSecurityTrade) bean).setListedSecurityDefinition((ListedSecurityDefinition) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
