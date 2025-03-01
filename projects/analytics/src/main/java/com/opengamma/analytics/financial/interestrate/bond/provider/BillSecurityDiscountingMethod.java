/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 *
 * Modified by McLeod Moores Software Limited.
 *
 * Copyright (C) 2017 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.analytics.financial.interestrate.bond.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opengamma.analytics.financial.interestrate.bond.definition.BillSecurity;
import com.opengamma.analytics.financial.provider.description.interestrate.IssuerProviderInterface;
import com.opengamma.analytics.financial.provider.description.interestrate.IssuerProviderIssuerDecoratedSpread;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MulticurveSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyMulticurveSensitivity;
import com.opengamma.analytics.math.function.Function1D;
import com.opengamma.analytics.math.rootfinding.BracketRoot;
import com.opengamma.analytics.math.rootfinding.BrentSingleRootFinder;
import com.opengamma.analytics.math.rootfinding.RealSingleRootFinder;
import com.opengamma.financial.convention.yield.SimpleYieldConvention;
import com.opengamma.financial.convention.yield.YieldConvention;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;
import com.opengamma.util.money.MultipleCurrencyAmount;
import com.opengamma.util.tuple.DoublesPair;

/**
 * Class with methods related to bill security valued by discounting.
 * <P>
 * Reference: Bill pricing, version 1.0. OpenGamma documentation, January 2012.
 */
public final class BillSecurityDiscountingMethod {

  /**
   * The unique instance of the class.
   */
  private static final BillSecurityDiscountingMethod INSTANCE = new BillSecurityDiscountingMethod();
  /**
   * The root finder used for yield finding.
   */
  private static final RealSingleRootFinder ROOT_FINDER = new BrentSingleRootFinder();
  /**
   * Brackets a root
   */
  private static final BracketRoot ROOT_BRACKETER = new BracketRoot();

  /**
   * Return the class instance.
   *
   * @return The instance.
   */
  public static BillSecurityDiscountingMethod getInstance() {
    return INSTANCE;
  }

  /**
   * Constructor
   */
  private BillSecurityDiscountingMethod() {
  }

  /**
   * Computes the present value of the bill security by discounting.
   *
   * @param bill
   *          The bill.
   * @param issuer
   *          The issuer and multi-curves provider.
   * @return The present value.
   */
  public MultipleCurrencyAmount presentValue(final BillSecurity bill, final IssuerProviderInterface issuer) {
    ArgumentChecker.notNull(bill, "Bill");
    ArgumentChecker.notNull(issuer, "Issuer and multi-curves provider");
    final double pvBill = bill.getNotional() * issuer.getDiscountFactor(bill.getIssuerEntity(), bill.getEndTime());
    return MultipleCurrencyAmount.of(bill.getCurrency(), pvBill);
  }

  /**
   * Compute the bill price from the yield. The price is the relative price at settlement. The yield is in the bill yield convention.
   *
   * @param bill
   *          The bill.
   * @param yield
   *          The yield in the bill yield convention.
   * @return The price.
   */
  public double priceFromYield(final BillSecurity bill, final double yield) {
    return priceFromYield(bill.getYieldConvention(), yield, bill.getAccrualFactor());
  }

  /**
   * Compute the bill price from the yield. The price is the relative price at settlement.
   *
   * @param convention
   *          The yield convention.
   * @param yield
   *          The yield in the bill yield convention.
   * @param accrualFactor
   *          The accrual factor between settlement and maturity.
   * @return The price.
   */
  public double priceFromYield(final YieldConvention convention, final double yield, final double accrualFactor) {
    if (convention == SimpleYieldConvention.DISCOUNT) {
      return 1.0 - accrualFactor * yield;
    }
    if (convention == SimpleYieldConvention.INTERESTATMTY) {
      return 1.0 / (1 + accrualFactor * yield);
    }
    throw new UnsupportedOperationException("The convention " + convention.getName() + " is not supported.");
  }

  /**
   * Computes the bill yield from the price. The yield is in the bill yield convention.
   *
   * @param bill
   *          The bill.
   * @param price
   *          The price. The price is the relative price at settlement.
   * @return The yield.
   */
  public double yieldFromCleanPrice(final BillSecurity bill, final double price) {
    if (bill.getYieldConvention() == SimpleYieldConvention.DISCOUNT) {
      return (1.0 - price) / bill.getAccrualFactor();
    }
    if (bill.getYieldConvention() == SimpleYieldConvention.INTERESTATMTY) {
      return (1.0 / price - 1) / bill.getAccrualFactor();
    }
    throw new UnsupportedOperationException("The convention " + bill.getYieldConvention().getName() + " is not supported.");
  }

  /**
   * Computes the derivative of the bill yield with respect to the price. The yield is in the bill yield convention.
   *
   * @param bill
   *          The bill.
   * @param price
   *          The price. The price is the relative price at settlement.
   * @return The yield derivative.
   */
  public double yieldFromPriceDerivative(final BillSecurity bill, final double price) {
    if (bill.getYieldConvention() == SimpleYieldConvention.DISCOUNT) {
      return -1.0 / bill.getAccrualFactor();
    }
    if (bill.getYieldConvention() == SimpleYieldConvention.INTERESTATMTY) {
      return -1.0 / (price * price * bill.getAccrualFactor());
    }
    throw new UnsupportedOperationException("The convention " + bill.getYieldConvention().getName() + " is not supported.");
  }

  /**
   * Computes the present value of the bill security by discounting from its yield.
   *
   * @param bill
   *          The bill.
   * @param yield
   *          The bill yield.
   * @param issuer
   *          The issuer and multi-curves provider.
   * @return The present value.
   */
  public MultipleCurrencyAmount presentValueFromYield(final BillSecurity bill, final double yield, final IssuerProviderInterface issuer) {
    ArgumentChecker.notNull(bill, "Bill");
    ArgumentChecker.notNull(issuer, "Issuer and multi-curves provider");
    final double price = priceFromYield(bill, yield);
    return presentValueFromPrice(bill, price, issuer);
  }

  /**
   * Computes the present value of the bill security by discounting from its price.
   *
   * @param bill
   *          The bill.
   * @param price
   *          The (dirty) price at settlement.
   * @param issuer
   *          The issuer and multi-curves provider.
   * @return The present value.
   */
  public MultipleCurrencyAmount presentValueFromPrice(final BillSecurity bill, final double price, final IssuerProviderInterface issuer) {
    ArgumentChecker.notNull(bill, "Bill");
    ArgumentChecker.notNull(issuer, "Issuer and multi-curves provider");
    final double pvBill = bill.getNotional() * price
        * issuer.getMulticurveProvider().getDiscountFactor(bill.getCurrency(), bill.getSettlementTime());
    return MultipleCurrencyAmount.of(bill.getCurrency(), pvBill);
  }

  /**
   * Compute the bill price from the curves. The price is the relative price at settlement.
   *
   * @param bill
   *          The bill.
   * @param issuer
   *          The issuer and multi-curves provider.
   * @return The price.
   */
  public double priceFromCurves(final BillSecurity bill, final IssuerProviderInterface issuer) {
    ArgumentChecker.notNull(bill, "Bill");
    ArgumentChecker.notNull(issuer, "Issuer and multi-curves provider");
    final double pvBill = bill.getNotional() * issuer.getDiscountFactor(bill.getIssuerEntity(), bill.getEndTime());
    final double price = pvBill
        / (bill.getNotional() * issuer.getMulticurveProvider().getDiscountFactor(bill.getCurrency(), bill.getSettlementTime()));
    return price;
  }

  /**
   * Computes the bill yield from the curves. The yield is in the bill yield convention.
   *
   * @param bill
   *          The bill.
   * @param issuer
   *          The issuer and multi-curves provider.
   * @return The yield.
   */
  public double yieldFromCurves(final BillSecurity bill, final IssuerProviderInterface issuer) {
    ArgumentChecker.notNull(bill, "Bill");
    ArgumentChecker.notNull(issuer, "Issuer and multi-curves provider");
    final double pvBill = bill.getNotional() * issuer.getDiscountFactor(bill.getIssuerEntity(), bill.getEndTime());
    final double price = pvBill
        / (bill.getNotional() * issuer.getMulticurveProvider().getDiscountFactor(bill.getCurrency(), bill.getSettlementTime()));
    return yieldFromCleanPrice(bill, price);
  }

  /**
   * Computes the bill present value curve sensitivity.
   *
   * @param bill
   *          The bill.
   * @param issuer
   *          The issuer and multi-curves provider.
   * @return The sensitivity.
   */
  public MultipleCurrencyMulticurveSensitivity presentValueCurveSensitivity(final BillSecurity bill, final IssuerProviderInterface issuer) {
    ArgumentChecker.notNull(bill, "Bill");
    ArgumentChecker.notNull(issuer, "Issuer and multi-curves provider");
    final double dfEnd = issuer.getDiscountFactor(bill.getIssuerEntity(), bill.getEndTime());
    // Backward sweep
    final double pvBar = 1.0;
    final double dfEndBar = bill.getNotional() * pvBar;
    final Map<String, List<DoublesPair>> resultMapCredit = new HashMap<>();
    final List<DoublesPair> listDiscounting = new ArrayList<>();
    listDiscounting.add(DoublesPair.of(bill.getEndTime(), -bill.getEndTime() * dfEnd * dfEndBar));
    resultMapCredit.put(issuer.getName(bill.getIssuerEntity()), listDiscounting);
    final MulticurveSensitivity result = MulticurveSensitivity.ofYieldDiscounting(resultMapCredit);
    return MultipleCurrencyMulticurveSensitivity.of(bill.getCurrency(), result);
  }

  /**
   * Computes a bill z-spread from the curves and a present value. The z-spread is a parallel shift applied to the discounting curve
   * associated to the bill (Issuer Entity) to match the present value.
   *
   * @param bill
   *          The bill.
   * @param issuerMulticurves
   *          The issuer and multi-curves provider.
   * @param pv
   *          The target present value.
   * @return The z-spread.
   */
  public double zSpreadFromCurvesAndPV(final BillSecurity bill, final IssuerProviderInterface issuerMulticurves,
      final MultipleCurrencyAmount pv) {
    ArgumentChecker.notNull(bill, "bill");
    ArgumentChecker.notNull(issuerMulticurves, "Issuer and multi-curves provider");
    final Currency ccy = bill.getCurrency();

    final Function1D<Double, Double> residual = new Function1D<Double, Double>() {
      @Override
      public Double evaluate(final Double z) {
        return presentValueFromZSpread(bill, issuerMulticurves, z).getAmount(ccy) - pv.getAmount(ccy);
      }
    };

    final double[] range = ROOT_BRACKETER.getBracketedPoints(residual, -0.01, 0.01); // Starting range is [-1%, 1%]
    return ROOT_FINDER.getRoot(residual, range[0], range[1]);
  }

  /**
   * Computes the present value of a bill security from z-spread. The z-spread is a parallel shift applied to the discounting curve
   * associated to the bill (Issuer Entity). The parallel shift is done in the curve convention.
   *
   * @param bill
   *          The bill security.
   * @param issuerMulticurves
   *          The issuer and multi-curves provider.
   * @param zSpread
   *          The z-spread.
   * @return The present value.
   */
  public MultipleCurrencyAmount presentValueFromZSpread(final BillSecurity bill, final IssuerProviderInterface issuerMulticurves,
      final double zSpread) {
    final IssuerProviderInterface issuerShifted = new IssuerProviderIssuerDecoratedSpread(issuerMulticurves, bill.getIssuerEntity(),
        zSpread);
    return presentValue(bill, issuerShifted);
  }

  /**
   * Computes a bill z-spread from the curves and a yield.
   *
   * @param bill
   *          The bill.
   * @param issuerMulticurves
   *          The issuer and multi-curves provider.
   * @param yield
   *          The yield.
   * @return The z-spread.
   */
  public double zSpreadFromCurvesAndYield(final BillSecurity bill, final IssuerProviderInterface issuerMulticurves, final double yield) {
    return zSpreadFromCurvesAndPV(bill, issuerMulticurves, presentValueFromYield(bill, yield, issuerMulticurves));
  }

  /**
   * Calculates the Macaulay duration using curve data.
   *
   * @param bill
   *          the bill, not null
   * @param marketData
   *          the market data, not null
   * @return the duration
   */
  public double macaulayDurationFromCurves(final BillSecurity bill, final IssuerProviderInterface marketData) {
    return bill.getEndTime();
  }

  /**
   * Calculates the modified duration using curve data.
   *
   * @param bill
   *          the bill, not null
   * @param marketData
   *          the market data, not null
   * @return the duration
   */
  public double modifiedDurationFromCurves(final BillSecurity bill, final IssuerProviderInterface marketData) {
    return macaulayDurationFromCurves(bill, marketData) / (1 + yieldFromCurves(bill, marketData));
  }

  /**
   * Calculates the convexity using curve data.
   *
   * @param bill
   *          the bill, not null
   * @param marketData
   *          the market data, not null
   * @return the convexity
   */
  public double convexityFromCurves(final BillSecurity bill, final IssuerProviderInterface marketData) {
    // final double yield = yieldFromCurves(bill, marketData);
    return 0; // convexityFromYield(bill, yield);
  }

}
