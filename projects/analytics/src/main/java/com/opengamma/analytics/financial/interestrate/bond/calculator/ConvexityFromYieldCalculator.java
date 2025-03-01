/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.interestrate.bond.calculator;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorAdapter;
import com.opengamma.analytics.financial.interestrate.bond.definition.BillSecurity;
import com.opengamma.analytics.financial.interestrate.bond.definition.BillTransaction;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondCapitalIndexedSecurity;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondCapitalIndexedTransaction;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondFixedSecurity;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondFixedTransaction;
import com.opengamma.analytics.financial.interestrate.bond.provider.BondCapitalIndexedSecurityDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.bond.provider.BondSecurityDiscountingMethod;
import com.opengamma.util.ArgumentChecker;

/**
 * Calculate convexity for bonds.
 */
public final class ConvexityFromYieldCalculator extends InstrumentDerivativeVisitorAdapter<Double, Double> {

  /**
   * The calculator instance.
   */
  private static final ConvexityFromYieldCalculator INSTANCE = new ConvexityFromYieldCalculator();
  /**
   * The fixed coupon bond method.
   */
  private static final BondSecurityDiscountingMethod METHOD_BOND = BondSecurityDiscountingMethod.getInstance();
  private static final BondCapitalIndexedSecurityDiscountingMethod METHOD_INFLATION_BOND_SECURITY = BondCapitalIndexedSecurityDiscountingMethod.getInstance();

  /**
   * Return the calculator instance.
   * 
   * @return The instance.
   */
  public static ConvexityFromYieldCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Private constructor.
   */
  private ConvexityFromYieldCalculator() {
  }

  @Override
  public Double visitBillTransaction(final BillTransaction bill, final Double yield) {
    ArgumentChecker.notNull(bill, "bill");
    ArgumentChecker.notNull(yield, "yield");
    return 0.;
  }

  @Override
  public Double visitBillSecurity(final BillSecurity bill, final Double yield) {
    ArgumentChecker.notNull(bill, "bill");
    ArgumentChecker.notNull(yield, "yield");
    return 0.;
  }

  @Override
  public Double visitBondFixedSecurity(final BondFixedSecurity bond, final Double yield) {
    ArgumentChecker.notNull(yield, "curves");
    ArgumentChecker.notNull(bond, "yield");
    return METHOD_BOND.convexityFromYield(bond, yield) / 100;
  }

  @Override
  public Double visitBondFixedTransaction(final BondFixedTransaction bond, final Double yield) {
    ArgumentChecker.notNull(yield, "curves");
    ArgumentChecker.notNull(bond, "yield");
    return METHOD_BOND.convexityFromYield(bond.getBondTransaction(), yield) / 100;
  }

  @Override
  public Double visitBondCapitalIndexedTransaction(final BondCapitalIndexedTransaction<?> bond, final Double yield) {
    ArgumentChecker.notNull(bond, "bond");
    ArgumentChecker.notNull(yield, "yield");
    final BondCapitalIndexedSecurity<?> bondSecurity = bond.getBondStandard();
    return METHOD_INFLATION_BOND_SECURITY.convexityFromYieldFiniteDifference(bondSecurity, yield) / 100;
  }

  @Override
  public Double visitBondCapitalIndexedSecurity(final BondCapitalIndexedSecurity<?> bond, final Double yield) {
    ArgumentChecker.notNull(bond, "bond");
    ArgumentChecker.notNull(yield, "yield");
    return METHOD_INFLATION_BOND_SECURITY.convexityFromYieldFiniteDifference(bond, yield) / 100;
  }
}
