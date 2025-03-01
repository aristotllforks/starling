/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.index;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import org.joda.beans.Bean;
import org.testng.annotations.Test;

import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.AbstractBeanTestCase;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.test.TestGroup;

/**
 * Tests the fields of an equity index component. This test is intended to pick up any changes
 * before databases are affected.
 */
@Test(groups = TestGroup.UNIT)
public class EquityIndexComponentTest extends AbstractBeanTestCase {
  /** The equity ids */
  private static final ExternalIdBundle IDS = ExternalIdBundle.of(ExternalSchemes.bloombergTickerSecurityId("AAA"),
      ExternalSchemes.bloombergBuidSecurityId("AAAA"));
  /** The weight */
  private static final BigDecimal WEIGHT = new BigDecimal(1234);
  /** The component */
  private static final EquityIndexComponent COMPONENT = new EquityIndexComponent(IDS, WEIGHT);

  /**
   * Tests that the ids cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullIds() {
    new EquityIndexComponent(null, WEIGHT);
  }

  /**
   * Tests that the weight cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullWeight() {
    new EquityIndexComponent(IDS, null);
  }

  /**
   * Tests the number of fields in the index component.
   */
  @Test
  public void testNumberOfFields() {
    assertEquals(3, IndexTestUtils.getFields(COMPONENT.getClass()).size());
  }

  /**
   * Tests that the fields are set correctly.
   */
  @Test
  public void testFields() {
    assertEquals(IDS, COMPONENT.getEquityIdentifier());
    assertEquals(WEIGHT, COMPONENT.getWeight());
  }

  @Override
  public JodaBeanProperties<? extends Bean> getJodaBeanProperties() {
    return new JodaBeanProperties<>(EquityIndexComponent.class, Arrays.asList("equityIdentifier", "weight"), Arrays.asList(IDS, WEIGHT),
        Arrays.asList(ExternalIdBundle.of("eids", "1"), BigDecimal.valueOf(2000)));
  }

}
