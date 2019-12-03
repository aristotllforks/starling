/**
 * Copyright (C) 2019 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.financial.convention;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.joda.beans.Bean;
import org.testng.annotations.Test;

import com.opengamma.financial.AbstractBeanTestCase;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.test.TestGroup;

/**
 * Tests for {@link RollDateSwapConvention}.
 */
@Test(groups = TestGroup.UNIT)
public class RollDateSwapConventionTest extends AbstractBeanTestCase {
  private static final String NAME = "name";
  private static final ExternalIdBundle IDS = ExternalIdBundle.of("conv", "US IMM SWAP");
  private static final ExternalId ROLL_DATE_CONVENTION = ExternalId.of("conv", "IMM");
  private static final ExternalId PAY_LEG_CONVENTION = ExternalId.of("conv", "US FIXED");
  private static final ExternalId RECEIVE_LEG_CONVENTION = ExternalId.of("conv", "US 3M LIBOR");
  private static final FinancialConvention CONVENTION = new RollDateSwapConvention(NAME, IDS, ROLL_DATE_CONVENTION, PAY_LEG_CONVENTION, RECEIVE_LEG_CONVENTION);

  @Override
  public JodaBeanProperties<? extends Bean> getJodaBeanProperties() {
    return new JodaBeanProperties<>(RollDateSwapConvention.class,
        Arrays.asList("name", "externalIdBundle", "rollDateConvention", "payLegConvention", "receiveLegConvention"),
        Arrays.asList(NAME, IDS, ROLL_DATE_CONVENTION, PAY_LEG_CONVENTION, RECEIVE_LEG_CONVENTION),
        Arrays.asList("other", ExternalIdBundle.of("conv", "IMM SWAP"), PAY_LEG_CONVENTION, RECEIVE_LEG_CONVENTION, ROLL_DATE_CONVENTION));
  }

  /**
   * Tests the returned type.
   */
  public void testType() {
    assertEquals(CONVENTION.getConventionType(), RollDateSwapConvention.TYPE);
  }

  /**
   * Tests that the accept() method visits the right method.
   */
  public void testVisitor() {
    assertEquals(CONVENTION.accept(TestVisitor.INSTANCE), "visited");
  }

  /**
   *
   */
  private static final class TestVisitor extends FinancialConventionVisitorAdapter<String> {
    public static final TestVisitor INSTANCE = new TestVisitor();

    private TestVisitor() {
    }

    @Override
    public String visitIMMSwapConvention(final RollDateSwapConvention convention) {
      return "visited";
    }

  }

}
