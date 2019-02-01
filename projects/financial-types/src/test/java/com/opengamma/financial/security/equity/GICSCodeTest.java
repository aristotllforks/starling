/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.equity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opengamma.util.test.TestGroup;

/**
 * Test.
 */
@Test(groups = TestGroup.UNIT)
public class GICSCodeTest {

  /**
   * Provides invalid GICS codes.
   *
   * @return invalid codes
   */
  @DataProvider(name = "invalidInt")
  public Object[][] datainvalidInt() {
    return new Object[][] {
      {0},
      {1},
      {100},
      {101},
      {109},
      {110},
      {1000},
      {1001},
      {101000},
      {101001},
      {10101000},
      {10101001},
      {10100010},
      {10100110},
      {10001010},
      {10011010},
      {1101010},
      {9},
      {999},
      {99999},
      {9999999},
      {1010101010},
      {-5},
      {-50},
    };
  }

  /**
   * Tests invalid codes.
   *
   * @param code
   *          the code
   */
  @Test(dataProvider = "invalidInt", expectedExceptions = IllegalArgumentException.class)
  public void testInvalidInt(final int code) {
    GICSCode.of(code);
  }

  /**
   * Provides invalid GICS codes.
   *
   * @return the code
   */
  @DataProvider(name = "invalidString")
  public Object[][] dataInvalidString() {
    return new Object[][] {
      {"0"},
      {"00"},
      {"1"},
      {"01"},
      {"100"},
      {"0100"},
      {"101"},
      {"0101"},
      {"109"},
      {"0109"},
      {"110"},
      {"0110"},
      {"1000"},
      {"1001"},
      {"101000"},
      {"101001"},
      {"10101000"},
      {"10101001"},
      {"10100010"},
      {"10100110"},
      {"10001010"},
      {"10011010"},
      {"1101010"},
      {""},
      {"Rubbish"},
      {"9"},
      {"999"},
      {"99999"},
      {"9999999"},
      {"101010101"},
      {"1010101010"},
      {"-5"},
      {"-50"},
    };
  }

  /**
   * Tests parsing invalid codes.
   *
   * @param code
   *          the code
   */
  @Test(dataProvider = "invalidString", expectedExceptions = IllegalArgumentException.class)
  public void testInvalidString(final String code) {
    GICSCode.of(code);
  }

  /**
   * Tests valid codes.
   */
  @Test
  public void testValid1() {
    for (int i = 10; i <= 99; i++) {
      GICSCode.of(i);
    }
  }

  /**
   * Tests valid codes.
   */
  @Test
  public void testValid2() {
    for (int i = 10; i <= 99; i++) {
      GICSCode.of(1000 + i);
    }
  }

  /**
   * Tests valid codes.
   */
  @Test
  public void testValid3() {
    for (int i = 10; i <= 99; i++) {
      GICSCode.of(101000 + i);
    }
  }

  /**
   * Tests valid codes.
   */
  @Test
  public void testValid4() {
    for (int i = 10; i <= 99; i++) {
      GICSCode.of(10101000 + i);
    }
  }

  /**
   * Tests equality.
   */
  @Test
  public void testEquals() {
    final GICSCode c1 = GICSCode.of("10");
    final GICSCode c2 = GICSCode.of(new String("10"));
    final GICSCode c3 = GICSCode.of("1010");
    assertTrue(c1.equals(c1));
    assertTrue(c1.equals(c2));
    assertFalse(c1.equals(c3));
    assertFalse(c1.equals("String"));
  }

  /**
   * Tests the hashcode.
   */
  @Test
  public void testHashCode() {
    final GICSCode test = GICSCode.of("10");
    assertEquals(test.hashCode(), "10".hashCode());
  }

  //-------------------------------------------------------------------------
  /**
   * Tests deconstruction of the code into sectors.
   */
  @Test
  public void codeDeconstructionSector() {
    final GICSCode test = GICSCode.of("45");
    assertEquals(false, test.isComplete());
    assertEquals(true, test.isPartial());

    assertEquals("45", test.getCode());
    assertEquals(45, test.getCodeInt());
    assertDescription("Information Technology", test.getDescription());

    assertEquals("45", test.getSectorCode());
    assertEquals(45, test.getSectorCodeInt());
    assertDescription("Information Technology", test.getSectorDescription());

    assertEquals("", test.getIndustryGroupCode());
    assertEquals(0, test.getIndustryGroupCodeInt());
    assertEquals("", test.getIndustryGroupDescription());

    assertEquals("", test.getIndustryCode());
    assertEquals(0, test.getIndustryCodeInt());
    assertEquals("", test.getIndustryDescription());

    assertEquals("", test.getSubIndustryCode());
    assertEquals(0, test.getSubIndustryCodeInt());
    assertEquals("", test.getSubIndustryDescription());
  }

  /**
   * Tests deconstruction of the code into industry groups.
   */
  @Test
  public void codeDeconstructionIndustryGroup() {
    final GICSCode test = GICSCode.of("4510");
    assertEquals(false, test.isComplete());
    assertEquals(true, test.isPartial());

    assertEquals("4510", test.getCode());
    assertEquals(4510, test.getCodeInt());
    assertDescription("Software & Services", test.getDescription());

    assertEquals("45", test.getSectorCode());
    assertEquals(45, test.getSectorCodeInt());
    assertDescription("Information Technology", test.getSectorDescription());

    assertEquals("4510", test.getIndustryGroupCode());
    assertEquals(4510, test.getIndustryGroupCodeInt());
    assertDescription("Software & Services", test.getIndustryGroupDescription());

    assertEquals("", test.getIndustryCode());
    assertEquals(0, test.getIndustryCodeInt());
    assertEquals("", test.getIndustryDescription());

    assertEquals("", test.getSubIndustryCode());
    assertEquals(0, test.getSubIndustryCodeInt());
    assertEquals("", test.getSubIndustryDescription());
  }

  /**
   * Tests deconstruction of the code into industry.
   */
  @Test
  public void codeDeconstructionIndustry() {
    final GICSCode test = GICSCode.of("451030");
    assertEquals(false, test.isComplete());
    assertEquals(true, test.isPartial());

    assertEquals("451030", test.getCode());
    assertEquals(451030, test.getCodeInt());
    assertDescription("Software", test.getDescription());

    assertEquals("45", test.getSectorCode());
    assertEquals(45, test.getSectorCodeInt());
    assertDescription("Information Technology", test.getSectorDescription());

    assertEquals("4510", test.getIndustryGroupCode());
    assertEquals(4510, test.getIndustryGroupCodeInt());
    assertDescription("Software & Services", test.getIndustryGroupDescription());

    assertEquals("451030", test.getIndustryCode());
    assertEquals(451030, test.getIndustryCodeInt());
    assertDescription("Software", test.getIndustryDescription());

    assertEquals("", test.getSubIndustryCode());
    assertEquals(0, test.getSubIndustryCodeInt());
    assertEquals("", test.getSubIndustryDescription());
  }

  /**
   * Tests deconstruction of the code into sub-industry.
   */
  @Test
  public void codeDeconstructionSubIndustry() {
    final GICSCode test = GICSCode.of("45103020");
    assertEquals(true, test.isComplete());
    assertEquals(false, test.isPartial());

    assertEquals("45103020", test.getCode());
    assertEquals(45103020, test.getCodeInt());
    assertDescription("Systems Software", test.getDescription());

    assertEquals("45", test.getSectorCode());
    assertEquals(45, test.getSectorCodeInt());
    assertDescription("Information Technology", test.getSectorDescription());

    assertEquals("4510", test.getIndustryGroupCode());
    assertEquals(4510, test.getIndustryGroupCodeInt());
    assertDescription("Software & Services", test.getIndustryGroupDescription());

    assertEquals("451030", test.getIndustryCode());
    assertEquals(451030, test.getIndustryCodeInt());
    assertDescription("Software", test.getIndustryDescription());

    assertEquals("45103020", test.getSubIndustryCode());
    assertEquals(45103020, test.getSubIndustryCodeInt());
    assertDescription("Systems Software", test.getSubIndustryDescription());
  }

  private static void assertDescription(final String expected, final String actual) {
    if ("Unknown".equals(actual)) {
      return;
    }
    assertEquals(expected, actual);
  }

  /**
   * Tests conversion of the code.
   */
  @Test
  public void conversion() {
    final GICSCode sector = GICSCode.of("45");
    final GICSCode group = GICSCode.of("4510");
    final GICSCode industry = GICSCode.of("451030");
    final GICSCode sub = GICSCode.of("45103020");

    assertEquals(sector, sector.toSector());
    assertEquals(sector, group.toSector());
    assertEquals(sector, industry.toSector());
    assertEquals(sector, sub.toSector());

    assertEquals(null, sector.toIndustryGroup());
    assertEquals(group, group.toIndustryGroup());
    assertEquals(group, industry.toIndustryGroup());
    assertEquals(group, sub.toIndustryGroup());

    assertEquals(null, sector.toIndustry());
    assertEquals(null, group.toIndustry());
    assertEquals(industry, industry.toIndustry());
    assertEquals(industry, sub.toIndustry());

    assertEquals(null, sector.toSubIndustry());
    assertEquals(null, group.toSubIndustry());
    assertEquals(null, industry.toSubIndustry());
    assertEquals(sub, sub.toSubIndustry());
  }

}
