/**
 * Copyright (C) 2019 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.financial.convention;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.joda.beans.Bean;
import org.testng.annotations.Test;

import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.AbstractBeanTestCase;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.i18n.Country;
import com.opengamma.util.test.TestGroup;

/**
 * Tests for {@link InterestRateFutureConvention}.
 */
@Test(groups = TestGroup.UNIT)
public class InterestRateFutureConventionTest extends AbstractBeanTestCase {
  private static final String NAME = "name";
  private static final ExternalIdBundle IDS = ExternalIdBundle.of("conv", "US IMM FUTURE");
  private static final ExternalId EXPIRY_CONVENTION = ExternalId.of("conv", "US 3M IMM FUTURE");
  private static final ExternalId EXCHANGE_CALENDAR = ExternalSchemes.countryRegionId(Country.US);
  private static final ExternalId INDEX_CONVENTION = ExternalId.of("conv", "USD 3M LIBOR");
  private static final FinancialConvention CONVENTION = new InterestRateFutureConvention(NAME, IDS, EXPIRY_CONVENTION, EXCHANGE_CALENDAR, INDEX_CONVENTION);

  @Override
  public JodaBeanProperties<? extends Bean> getJodaBeanProperties() {
    return new JodaBeanProperties<>(InterestRateFutureConvention.class,
        Arrays.asList("name", "externalIdBundle", "expiryConvention", "exchangeCalendar", "indexConvention"),
        Arrays.asList(NAME, IDS, EXPIRY_CONVENTION, EXCHANGE_CALENDAR, INDEX_CONVENTION),
        Arrays.asList("other", ExternalIdBundle.of("conv", "IMM SWAP"), EXCHANGE_CALENDAR, INDEX_CONVENTION, EXPIRY_CONVENTION));
  }

  /**
   * Tests the returned type.
   */
  public void testType() {
    assertEquals(CONVENTION.getConventionType(), InterestRateFutureConvention.TYPE);
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
    public String visitInterestRateFutureConvention(final InterestRateFutureConvention convention) {
      return "visited";
    }
  }

}
