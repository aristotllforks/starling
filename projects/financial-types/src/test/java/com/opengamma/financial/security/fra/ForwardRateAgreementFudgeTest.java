/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.fra;

import java.util.Set;

import org.testng.annotations.Test;
import java.time.LocalDate;

import com.google.common.collect.Sets;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventions;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCounts;
import com.opengamma.financial.convention.frequency.SimpleFrequency;
import com.opengamma.id.ExternalId;
import com.opengamma.util.money.Currency;
import com.opengamma.util.test.AbstractFudgeBuilderTestCase;
import com.opengamma.util.test.TestGroup;

/**
 * Test for IRS fudge encoding & decoding.
 */
@Test(groups = TestGroup.UNIT)
public class ForwardRateAgreementFudgeTest extends AbstractFudgeBuilderTestCase {

  private static final Set<ExternalId> USNYGBLO = Sets.newHashSet(ExternalSchemes.isdaHoliday("USNY,GBLO"));

  private static final BusinessDayConvention MF = BusinessDayConventions.MODIFIED_FOLLOWING;
  private static final DayCount ACT360 = DayCounts.ACT_360;

  /**
   *
   */
  @Test
  public void testSwapSecurity() {
    final ExternalId id = ExternalSchemes.syntheticSecurityId("test");
    final ForwardRateAgreementSecurity fra = new ForwardRateAgreementSecurity(Currency.USD, id, SimpleFrequency.QUARTERLY, LocalDate.now(),
        LocalDate.now().plusMonths(3), 0.00123, 1e6d, LocalDate.now(), ACT360, MF, USNYGBLO, 2);
    assertEncodeDecodeCycle(ForwardRateAgreementSecurity.class, fra);
  }

}
