/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.entitlement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.opengamma.id.ExternalId;
import com.opengamma.livedata.LiveDataSpecification;
import com.opengamma.livedata.UserPrincipal;
import com.opengamma.livedata.normalization.NormalizationRuleSet;
import com.opengamma.livedata.normalization.StandardRules;
import com.opengamma.livedata.resolver.FixedDistributionSpecificationResolver;
import com.opengamma.livedata.server.DistributionSpecification;
import com.opengamma.security.user.Authority;
import com.opengamma.security.user.User;
import com.opengamma.security.user.UserGroup;
import com.opengamma.security.user.UserManager;
import com.opengamma.util.test.TestGroup;

/**
 * Test.
 */
@Test(groups = TestGroup.UNIT)
public class UserEntitlementCheckerTest {

  private DistributionSpecification _aaplOnBloomberg;
  private DistributionSpecification _aaplOnBloombergWithNormalization;
  private DistributionSpecification _bondOnBloomberg;
  private DistributionSpecification _bondOnBloombergWithNormalization;
  private DistributionSpecification _fxOnBloomberg;
  private DistributionSpecification _fxOnBloombergWithNormalization;
  private UserPrincipal _john;
  private UserPrincipal _mike;
  private LiveDataEntitlementChecker _userEntitlementChecker;

  /**
   * Sets up the authorities before each method.
   */
  @BeforeMethod
  public void setup() {
    final Set<UserGroup> userGroups = new HashSet<>();

    final UserGroup group1 = new UserGroup(0L, "group1");
    group1.getAuthorities().add(new Authority(0L, "LiveData/Bloomberg/Equity/**")); // ** -> all subpaths OK
    group1.getAuthorities().add(new Authority(1L, "LiveData/Bloomberg/Bond/*")); // * -> only 1 level of subpath OK
    userGroups.add(group1);

    final UserGroup group2 = new UserGroup(1L, "group2");
    group2.getAuthorities().add(new Authority(2L, "LiveData/Reuters/Equity/**"));
    userGroups.add(group2);

    final User user = new User(null,
        "john",
        "pw",
        userGroups,
        new Date());

    final UserManager userManager = mock(UserManager.class);
    when(userManager.getUser("john")).thenReturn(user);

    _aaplOnBloomberg = new DistributionSpecification(
        ExternalId.of("BLOOMBERG_BUID", "EQ12345"),
        StandardRules.getNoNormalization(),
        "LiveData.Bloomberg.Equity.AAPL");

    _aaplOnBloombergWithNormalization = new DistributionSpecification(
        ExternalId.of("BLOOMBERG_BUID", "EQ12345"),
        new NormalizationRuleSet("MyWeirdNormalizationRule"),
        "LiveData.Bloomberg.Equity.AAPL.MyWeirdNormalizationRule");

    _bondOnBloomberg = new DistributionSpecification(
        ExternalId.of("BLOOMBERG_BUID", "BOND12345"),
        StandardRules.getNoNormalization(),
        "LiveData.Bloomberg.Bond.IBMBOND123");

    _bondOnBloombergWithNormalization = new DistributionSpecification(
        ExternalId.of("BLOOMBERG_BUID", "BOND12345"),
        new NormalizationRuleSet("MyWeirdNormalizationRule"),
        "LiveData.Bloomberg.Bond.IBMBOND123.MyWeirdNormalizationRule");

    _fxOnBloomberg = new DistributionSpecification(
        ExternalId.of("BLOOMBERG_BUID", "FX12345"),
        StandardRules.getNoNormalization(),
        "LiveData.Bloomberg.FX.EURUSD");

    _fxOnBloombergWithNormalization = new DistributionSpecification(
        ExternalId.of("BLOOMBERG_BUID", "FX12345"),
        new NormalizationRuleSet("MyWeirdNormalizationRule"),
        "LiveData.Bloomberg.FX.EURUSD.MyWeirdNormalizationRule");

    final Map<LiveDataSpecification, DistributionSpecification> fixes = new HashMap<>();
    fixes.put(_aaplOnBloomberg.getFullyQualifiedLiveDataSpecification(), _aaplOnBloomberg);
    fixes.put(_aaplOnBloombergWithNormalization.getFullyQualifiedLiveDataSpecification(),
        _aaplOnBloombergWithNormalization);
    fixes.put(_bondOnBloomberg.getFullyQualifiedLiveDataSpecification(), _bondOnBloomberg);
    fixes.put(_bondOnBloombergWithNormalization.getFullyQualifiedLiveDataSpecification(),
        _bondOnBloombergWithNormalization);
    fixes.put(_fxOnBloomberg.getFullyQualifiedLiveDataSpecification(), _fxOnBloomberg);
    fixes.put(_fxOnBloombergWithNormalization.getFullyQualifiedLiveDataSpecification(),
        _fxOnBloombergWithNormalization);

    final FixedDistributionSpecificationResolver resolver = new FixedDistributionSpecificationResolver(fixes);

    _userEntitlementChecker = new UserEntitlementChecker(userManager, resolver);

    _john = new UserPrincipal("john", "127.0.0.1");

    // non-existent user
    _mike = new UserPrincipal("mike", "127.0.0.1");
  }

  /**
   * Checks that the user is entitled to access LiveData/Bloomberg/Equity/*.
   */
  @Test
  public void equityWithoutNormalizationForAuthorizedUser() {
    checkUserIsEntitled(_john, _aaplOnBloomberg);
  }

  /**
   * Checks that the user is entitled to access LiveData/Bloomberg/Equity/**.
   */
  @Test
  public void equityWithNormalizationForAuthorizedUser() {
    checkUserIsEntitled(_john, _aaplOnBloombergWithNormalization);
  }

  /**
   * Checks that the user is entitled to access LiveData/Bloomberg/Bond/*.
   */
  @Test
  public void bondWithoutNormalizationForAuthorizedUser() {
    checkUserIsEntitled(_john, _bondOnBloomberg);
  }

  /**
   * Checks that the user is not entitled to access LiveData/Bloomberg/Bond/**.
   */
  @Test
  public void bondWithNormalizationForAuthorizedUser() {
    checkUserIsNotEntitled(_john, _bondOnBloombergWithNormalization);
  }

  /**
   * Checks that the user is not entitled to access LiveData/Bloomberg/FX/*.
   */
  @Test
  public void fxWithoutNormalizationForAuthorizedUser() {
    checkUserIsNotEntitled(_john, _fxOnBloomberg);
  }

  /**
   * Checks that the user is not entitled to access LiveData/Bloomberg/FX/**.
   */
  @Test
  public void fxWithNormalizationForAuthorizedUser() {
    checkUserIsNotEntitled(_john, _fxOnBloombergWithNormalization);
  }

  /**
   * Checks that a non-existent user is not entitled to access
   * LiveData/Bloomberg/FX/*.
   */
  @Test
  public void equityWithoutNormalizationForUnauthorizedUser() {
    checkUserIsNotEntitled(_mike, _aaplOnBloomberg);
  }

  /**
   * Checks that a non-existent user is not entitled to access
   * LiveData/Bloomberg/FX/**.
   */
  @Test
  public void fxWithoutNormalizationForUnauthorizedUser() {
    checkUserIsNotEntitled(_mike, _fxOnBloomberg);
  }

  /**
   * Checks that permission is granted for a non-existent specification.
   */
  @Test
  public void nonExistentSpecIsPermissioned() {
    final LiveDataSpecification bogusSpec =
        new LiveDataSpecification(StandardRules.getOpenGammaRuleSetId(), ExternalId.of("RIC", "bar"));
    checkUserIsEntitled(_john, bogusSpec);
  }

  private void checkUserIsEntitled(final UserPrincipal user, final DistributionSpecification spec) {
    checkEntitlement(user, spec, true);
  }

  private void checkUserIsEntitled(final UserPrincipal user, final LiveDataSpecification spec) {
    checkEntitlement(user, spec, true);
  }

  private void checkUserIsNotEntitled(final UserPrincipal user, final DistributionSpecification spec) {
    checkEntitlement(user, spec, false);
  }

  private void checkEntitlement(final UserPrincipal user, final DistributionSpecification spec, final boolean isEntitled) {
    checkEntitlement(user, spec.getFullyQualifiedLiveDataSpecification(), isEntitled);
  }

  private void checkEntitlement(final UserPrincipal user, final LiveDataSpecification spec, final boolean isEntitled) {
    assertThat(_userEntitlementChecker.isEntitled(user, spec), is(isEntitled));
  }

}
