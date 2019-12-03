/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.calculator.inflation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorAdapter;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.Annuity;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.AnnuityCouponFixed;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondCapitalIndexedSecurity;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondCapitalIndexedTransaction;
import com.opengamma.analytics.financial.interestrate.bond.provider.BondCapitalIndexedSecurityDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.bond.provider.BondCapitalIndexedTransactionDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationYearOnYearInterpolation;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationYearOnYearInterpolationWithMargin;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationYearOnYearMonthly;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationYearOnYearMonthlyWithMargin;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponInterpolation;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponInterpolationGearing;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponMonthly;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponMonthlyGearing;
import com.opengamma.analytics.financial.interestrate.inflation.provider.CouponInflationYearOnYearInterpolationDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.inflation.provider.CouponInflationYearOnYearInterpolationWithMarginDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.inflation.provider.CouponInflationYearOnYearMonthlyDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.inflation.provider.CouponInflationYearOnYearMonthlyWithMarginDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.inflation.provider.CouponInflationZeroCouponInterpolationDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.inflation.provider.CouponInflationZeroCouponInterpolationGearingDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.inflation.provider.CouponInflationZeroCouponMonthlyDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.inflation.provider.CouponInflationZeroCouponMonthlyGearingDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponFixed;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponFixedCompounding;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponFixedCompoundingDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponFixedDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.swap.derivative.Swap;
import com.opengamma.analytics.financial.interestrate.swap.derivative.SwapFixedCoupon;
import com.opengamma.analytics.financial.provider.description.inflation.InflationIssuerProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.inflation.InflationSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.inflation.MultipleCurrencyInflationSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyMulticurveSensitivity;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.DoublesPair;

/**
 *
 */
public final class PresentValueCurveSensitivityIssuerDiscountingInflationCalculator
extends InstrumentDerivativeVisitorAdapter<InflationIssuerProviderInterface, MultipleCurrencyInflationSensitivity> {
  /**
   * The unique instance of the calculator.
   */
  private static final PresentValueCurveSensitivityIssuerDiscountingInflationCalculator INSTANCE =
      new PresentValueCurveSensitivityIssuerDiscountingInflationCalculator();

  /**
   * Gets the calculator instance.
   *
   * @return The calculator.
   */
  public static PresentValueCurveSensitivityIssuerDiscountingInflationCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Constructor.
   */
  private PresentValueCurveSensitivityIssuerDiscountingInflationCalculator() {
  }

  /**
   * Pricing method for zero-coupon with monthly reference index.
   */
  private static final CouponInflationZeroCouponMonthlyDiscountingMethod METHOD_ZC_MONTHLY = new CouponInflationZeroCouponMonthlyDiscountingMethod();
  /**
   * Pricing method for zero-coupon with interpolated reference index.
   */
  private static final CouponInflationZeroCouponInterpolationDiscountingMethod METHOD_ZC_INTERPOLATION =
      new CouponInflationZeroCouponInterpolationDiscountingMethod();
  /**
   * Pricing method for zero-coupon with monthly reference index.
   */
  private static final CouponInflationZeroCouponMonthlyGearingDiscountingMethod METHOD_ZC_MONTHLY_GEARING =
      new CouponInflationZeroCouponMonthlyGearingDiscountingMethod();
  /**
   * Pricing method for zero-coupon with interpolated reference index.
   */
  private static final CouponInflationZeroCouponInterpolationGearingDiscountingMethod METHOD_ZC_INTERPOLATION_GEARING =
      new CouponInflationZeroCouponInterpolationGearingDiscountingMethod();
  /**
   * Pricing method for year on year coupon with monthly reference index.
   */
  private static final CouponInflationYearOnYearMonthlyDiscountingMethod METHOD_YEAR_ON_YEAR_MONTHLY = new CouponInflationYearOnYearMonthlyDiscountingMethod();
  /**
   * Pricing method for year on year coupon with interpolated reference index.
   */
  private static final CouponInflationYearOnYearInterpolationDiscountingMethod METHOD_YEAR_ON_YEAR_INTERPOLATION =
      new CouponInflationYearOnYearInterpolationDiscountingMethod();
  /**
   * Pricing method for year on year coupon with monthly and with margin reference index.
   */
  private static final CouponInflationYearOnYearMonthlyWithMarginDiscountingMethod METHOD_YEAR_ON_YEAR_MONTHLY_WITH_MARGIN =
      new CouponInflationYearOnYearMonthlyWithMarginDiscountingMethod();
  /**
   * Pricing method for year on year coupon with interpolated and with margin reference index.
   */
  private static final CouponInflationYearOnYearInterpolationWithMarginDiscountingMethod METHOD_YEAR_ON_YEAR_INTERPOLATION_WITH_MARGIN =
      new CouponInflationYearOnYearInterpolationWithMarginDiscountingMethod();
  /**
   * Pricing method for coupon fixed compounding.
   */
  private static final CouponFixedCompoundingDiscountingMethod METHOD_CPN_FIXED_COMPOUNDING = CouponFixedCompoundingDiscountingMethod.getInstance();

  /**
   * Pricing method for coupon fixed .
   */
  private static final CouponFixedDiscountingMethod METHOD_CPN_FIXED = CouponFixedDiscountingMethod.getInstance();

  /** Method for bond securities */
  private static final BondCapitalIndexedSecurityDiscountingMethod METHOD_BOND_SEC = BondCapitalIndexedSecurityDiscountingMethod.getInstance();
  /** Method for bond transactions */
  private static final BondCapitalIndexedTransactionDiscountingMethod METHOD_BOND_TR = BondCapitalIndexedTransactionDiscountingMethod.getInstance();

  // ----- Inflation Coupon ------

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponInflationZeroCouponMonthly(final CouponInflationZeroCouponMonthly coupon,
      final InflationIssuerProviderInterface inflation) {
    return METHOD_ZC_MONTHLY.presentValueCurveSensitivity(coupon, inflation.getInflationProvider());
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponInflationZeroCouponInterpolation(final CouponInflationZeroCouponInterpolation coupon,
      final InflationIssuerProviderInterface inflation) {
    return METHOD_ZC_INTERPOLATION.presentValueCurveSensitivity(coupon, inflation.getInflationProvider());
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponInflationZeroCouponMonthlyGearing(final CouponInflationZeroCouponMonthlyGearing coupon,
      final InflationIssuerProviderInterface inflation) {
    return METHOD_ZC_MONTHLY_GEARING.presentValueCurveSensitivity(coupon, inflation.getInflationProvider());
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponInflationZeroCouponInterpolationGearing(final CouponInflationZeroCouponInterpolationGearing coupon,
      final InflationIssuerProviderInterface inflation) {
    return METHOD_ZC_INTERPOLATION_GEARING.presentValueCurveSensitivity(coupon, inflation.getInflationProvider());
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponInflationYearOnYearMonthly(final CouponInflationYearOnYearMonthly coupon,
      final InflationIssuerProviderInterface inflation) {
    return METHOD_YEAR_ON_YEAR_MONTHLY.presentValueCurveSensitivity(coupon, inflation.getInflationProvider());
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponInflationYearOnYearInterpolation(final CouponInflationYearOnYearInterpolation coupon,
      final InflationIssuerProviderInterface inflation) {
    return METHOD_YEAR_ON_YEAR_INTERPOLATION.presentValueCurveSensitivity(coupon, inflation.getInflationProvider());
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponInflationYearOnYearMonthlyWithMargin(final CouponInflationYearOnYearMonthlyWithMargin coupon,
      final InflationIssuerProviderInterface inflation) {
    return METHOD_YEAR_ON_YEAR_MONTHLY_WITH_MARGIN.presentValueCurveSensitivity(coupon, inflation.getInflationProvider());
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponInflationYearOnYearInterpolationWithMargin(
      final CouponInflationYearOnYearInterpolationWithMargin coupon,
      final InflationIssuerProviderInterface inflation) {
    return METHOD_YEAR_ON_YEAR_INTERPOLATION_WITH_MARGIN.presentValueCurveSensitivity(coupon, inflation.getInflationProvider());
  }

  // ----- Coupon fix ------

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponFixed(final CouponFixed coupon, final InflationIssuerProviderInterface inflation) {
    final MultipleCurrencyMulticurveSensitivity multipleCurrencyMulticurveSensitivity = METHOD_CPN_FIXED.presentValueCurveSensitivity(coupon,
        inflation.getMulticurveProvider());
    final MultipleCurrencyInflationSensitivity multipleCurrencyInflationSensitivity = new MultipleCurrencyInflationSensitivity();
    for (final Currency loopccy : multipleCurrencyMulticurveSensitivity.getCurrencies()) {
      final Map<String, List<DoublesPair>> sensitivityPriceCurve = new HashMap<>();
      multipleCurrencyInflationSensitivity.plus(loopccy,
          InflationSensitivity.of(multipleCurrencyMulticurveSensitivity.getSensitivity(loopccy), sensitivityPriceCurve));
    }
    return multipleCurrencyInflationSensitivity;
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitCouponFixedCompounding(final CouponFixedCompounding coupon,
      final InflationIssuerProviderInterface inflation) {
    final MultipleCurrencyMulticurveSensitivity multipleCurrencyMulticurveSensitivity = METHOD_CPN_FIXED_COMPOUNDING.presentValueCurveSensitivity(coupon,
        inflation.getMulticurveProvider());
    final MultipleCurrencyInflationSensitivity multipleCurrencyInflationSensitivity = new MultipleCurrencyInflationSensitivity();
    for (final Currency loopccy : multipleCurrencyMulticurveSensitivity.getCurrencies()) {
      final Map<String, List<DoublesPair>> sensitivityPriceCurve = new HashMap<>();
      multipleCurrencyInflationSensitivity.plus(loopccy,
          InflationSensitivity.of(multipleCurrencyMulticurveSensitivity.getSensitivity(loopccy), sensitivityPriceCurve));
    }
    return multipleCurrencyInflationSensitivity;
  }

  // ----- Annuity ------

  @Override
  public MultipleCurrencyInflationSensitivity visitGenericAnnuity(final Annuity<? extends Payment> annuity, final InflationIssuerProviderInterface inflation) {
    ArgumentChecker.notNull(annuity, "Annuity");
    ArgumentChecker.notNull(inflation, "inflation");
    MultipleCurrencyInflationSensitivity cs = annuity.getNthPayment(0).accept(this, inflation);
    for (int loopp = 1; loopp < annuity.getNumberOfPayments(); loopp++) {
      cs = cs.plus(annuity.getNthPayment(loopp).accept(this, inflation));
    }
    return cs;
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitFixedCouponAnnuity(final AnnuityCouponFixed annuity, final InflationIssuerProviderInterface inflation) {
    return visitGenericAnnuity(annuity, inflation);
  }

  // ----- Swap ------

  @Override
  public MultipleCurrencyInflationSensitivity visitSwap(final Swap<?, ?> swap, final InflationIssuerProviderInterface inflation) {
    final MultipleCurrencyInflationSensitivity sensitivity1 = swap.getFirstLeg().accept(this, inflation);
    final MultipleCurrencyInflationSensitivity sensitivity2 = swap.getSecondLeg().accept(this, inflation);
    return sensitivity1.plus(sensitivity2);
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitFixedCouponSwap(final SwapFixedCoupon<?> swap, final InflationIssuerProviderInterface multicurve) {
    return visitSwap(swap, multicurve);
  }

  // ----- Bond -----

  @Override
  public MultipleCurrencyInflationSensitivity visitBondCapitalIndexedSecurity(final BondCapitalIndexedSecurity<?> bond,
      final InflationIssuerProviderInterface curves) {
    return METHOD_BOND_SEC.presentValueCurveSensitivity(bond, curves);
  }

  @Override
  public MultipleCurrencyInflationSensitivity visitBondCapitalIndexedTransaction(final BondCapitalIndexedTransaction<?> bond,
      final InflationIssuerProviderInterface curves) {
    return METHOD_BOND_TR.presentValueCurveSensitivity(bond, curves);
  }

}
