/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.security.hibernate;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Hibernate bean for storing an enum with description.
 */
public class EnumWithDescriptionBean extends EnumBean {

  private String _description;

  public EnumWithDescriptionBean() {
    super();
  }

  public EnumWithDescriptionBean(final String name, final String description) {
    super(name);
    _description = description;
  }


  public String getDescription() {
    return _description;
  }

  public void setDescription(final String description) {
    _description = description;
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof EnumWithDescriptionBean)) {
      return false;
    }
    final EnumWithDescriptionBean ewd = (EnumWithDescriptionBean) o;
    if (getId() != -1 && ewd.getId() != -1) {
      return getId().longValue() == ewd.getId().longValue();
    }
    return new EqualsBuilder().append(getName(), ewd.getName()).append(getDescription(), ewd.getDescription()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getName()).append(getDescription()).toHashCode();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
