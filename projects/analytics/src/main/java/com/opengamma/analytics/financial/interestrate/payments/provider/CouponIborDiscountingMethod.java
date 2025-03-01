/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.interestrate.payments.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponIbor;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.ForwardSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MulticurveSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyMulticurveSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.SimplyCompoundedForwardSensitivity;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.MultipleCurrencyAmount;
import com.opengamma.util.tuple.DoublesPair;

/**
 * Method to compute present value and present value sensitivity for Ibor coupon with gearing factor and spread.
 */
public final class CouponIborDiscountingMethod {

  /**
   * The method unique instance.
   */
  private static final CouponIborDiscountingMethod INSTANCE = new CouponIborDiscountingMethod();

  /**
   * Return the unique instance of the class.
   *
   * @return The instance.
   */
  public static CouponIborDiscountingMethod getInstance() {
    return INSTANCE;
  }

  /**
   * Private constructor.
   */
  private CouponIborDiscountingMethod() {
  }

  /**
   * Compute the present value of a Ibor coupon by discounting.
   *
   * @param coupon
   *          The coupon.
   * @param multicurves
   *          The multi-curve provider.
   * @return The present value.
   */
  public MultipleCurrencyAmount presentValue(final CouponIbor coupon, final MulticurveProviderInterface multicurves) {
    return presentValue(coupon, multicurves, IborForwardRateProvider.getInstance());
  }

  /**
   * Calculates the present value.
   *
   * @param coupon
   *          the coupon, not null
   * @param multicurves
   *          the market data, not null
   * @param forwardRateProvider
   *          gets the forward rate from the market data, not null
   * @return the present value
   */
  public MultipleCurrencyAmount presentValue(
      final CouponIbor coupon,
      final MulticurveProviderInterface multicurves,
      final ForwardRateProvider<IborIndex> forwardRateProvider) {
    ArgumentChecker.notNull(coupon, "coupon");
    ArgumentChecker.notNull(multicurves, "multicurves");
    ArgumentChecker.notNull(forwardRateProvider, "forwardRateProvider");
    final double forward = forwardRateProvider.getRate(
        multicurves,
        coupon,
        coupon.getFixingPeriodStartTime(),
        coupon.getFixingPeriodEndTime(),
        coupon.getFixingAccrualFactor());
    final double df = multicurves.getDiscountFactor(coupon.getCurrency(), coupon.getPaymentTime());
    final double pv = coupon.getNotional() * coupon.getPaymentYearFraction() * forward * df;
    return MultipleCurrencyAmount.of(coupon.getCurrency(), pv);
  }

  /**
   * Compute the present value sensitivity to yield for discounting curve and forward rate (in index convention) for forward curve.
   *
   * @param coupon
   *          The coupon.
   * @param multicurve
   *          The multi-curve provider.
   * @return The present value sensitivity.
   */
  public MultipleCurrencyMulticurveSensitivity presentValueCurveSensitivity(final CouponIbor coupon,
      final MulticurveProviderInterface multicurve) {
    ArgumentChecker.notNull(coupon, "Coupon");
    ArgumentChecker.notNull(multicurve, "Curves");
    final double forward = multicurve.getSimplyCompoundForwardRate(coupon.getIndex(), coupon.getFixingPeriodStartTime(),
        coupon.getFixingPeriodEndTime(),
        coupon.getFixingAccrualFactor());
    final double df = multicurve.getDiscountFactor(coupon.getCurrency(), coupon.getPaymentTime());
    // Backward sweep
    final double pvBar = 1.0;
    final double forwardBar = coupon.getNotional() * coupon.getPaymentYearFraction() * df * pvBar;
    final double dfBar = coupon.getNotional() * coupon.getPaymentYearFraction() * forward * pvBar;
    final Map<String, List<DoublesPair>> mapDsc = new HashMap<>();
    final List<DoublesPair> listDiscounting = new ArrayList<>();
    listDiscounting.add(DoublesPair.of(coupon.getPaymentTime(), -coupon.getPaymentTime() * df * dfBar));
    mapDsc.put(multicurve.getName(coupon.getCurrency()), listDiscounting);
    final Map<String, List<ForwardSensitivity>> mapFwd = new HashMap<>();
    final List<ForwardSensitivity> listForward = new ArrayList<>();
    listForward.add(new SimplyCompoundedForwardSensitivity(coupon.getFixingPeriodStartTime(), coupon.getFixingPeriodEndTime(),
        coupon.getFixingAccrualFactor(),
        forwardBar));
    mapFwd.put(multicurve.getName(coupon.getIndex()), listForward);
    return MultipleCurrencyMulticurveSensitivity.of(coupon.getCurrency(), MulticurveSensitivity.of(mapDsc, mapFwd));
  }

  /**
   * Calculates the second order sensitivity of the coupon to the curve.
   *
   * @param coupon
   *          the coupon, not null
   * @param multicurve
   *          the market data, not null
   * @return the sensitivity
   */
  public MultipleCurrencyMulticurveSensitivity presentValueSecondOrderCurveSensitivity(final CouponIbor coupon,
      final MulticurveProviderInterface multicurve) {
    ArgumentChecker.notNull(coupon, "Coupon");
    ArgumentChecker.notNull(multicurve, "Curves");
    final double forward = multicurve.getSimplyCompoundForwardRate(coupon.getIndex(), coupon.getFixingPeriodStartTime(),
        coupon.getFixingPeriodEndTime(),
        coupon.getFixingAccrualFactor());
    final double df = multicurve.getDiscountFactor(coupon.getCurrency(), coupon.getPaymentTime());
    // Backward sweep
    final double pvBar = 1.0;
    final double forwardBar = coupon.getNotional() * coupon.getPaymentYearFraction() * df * pvBar;
    final double dfBar = coupon.getNotional() * coupon.getPaymentYearFraction() * forward * pvBar;
    final Map<String, List<DoublesPair>> mapDsc = new HashMap<>();
    final List<DoublesPair> listDiscounting = new ArrayList<>();
    listDiscounting.add(DoublesPair.of(coupon.getPaymentTime(), coupon.getPaymentTime() * coupon.getPaymentTime() * df * dfBar));
    mapDsc.put(multicurve.getName(coupon.getCurrency()), listDiscounting);
    final Map<String, List<ForwardSensitivity>> mapFwd = new HashMap<>();
    final List<ForwardSensitivity> listForward = new ArrayList<>();
    listForward.add(new SimplyCompoundedForwardSensitivity(coupon.getFixingPeriodStartTime(), coupon.getFixingPeriodEndTime(),
        coupon.getFixingAccrualFactor(),
        -2. * coupon.getPaymentTime()
            * forwardBar));
    mapFwd.put(multicurve.getName(coupon.getIndex()), listForward);
    return MultipleCurrencyMulticurveSensitivity.of(coupon.getCurrency(), MulticurveSensitivity.of(mapDsc, mapFwd));
  }

  /**
   * Compute the par rate (Ibor forward) of a Ibor coupon by discounting.
   *
   * @param coupon
   *          The coupon.
   * @param curves
   *          The yield curves. Should contain the discounting and forward curves associated.
   * @return The present value.
   */
  public double parRate(final CouponIbor coupon, final MulticurveProviderInterface curves) {
    ArgumentChecker.notNull(coupon, "coupon");
    ArgumentChecker.notNull(curves, "curves");
    return curves.getSimplyCompoundForwardRate(coupon.getIndex(), coupon.getFixingPeriodStartTime(), coupon.getFixingPeriodEndTime(),
        coupon.getFixingAccrualFactor());
  }

  // /**
  // * Compute the par rate (Ibor forward) sensitivity to rates of a Ibor coupon by discounting.
  // *
  // * @param coupon
  // * The coupon.
  // * @param curves
  // * The yield curves.
  // * @return The par rate curve sensitivity.
  // */
  // public MulticurveSensitivity parRateCurveSensitivity(final CouponIbor coupon, final MulticurveProviderInterface curves) {
  // ArgumentChecker.notNull(coupon, "coupon");
  // ArgumentChecker.notNull(curves, "curves");
  // final double dfForwardStart = curves.getDiscountFactor(coupon.getIndex(), coupon.getFixingPeriodStartTime());
  // final double dfForwardEnd = curves.getDiscountFactor(coupon.getIndex(), coupon.getFixingPeriodEndTime());
  // // Backward sweep
  // final double parRateBar = 1.0;
  // final double dfForwardEndBar = -dfForwardStart / (dfForwardEnd * dfForwardEnd) / coupon.getFixingAccrualFactor() * parRateBar;
  // final double dfForwardStartBar = 1.0 / (coupon.getFixingAccrualFactor() * dfForwardEnd) * parRateBar;
  // return result;
  // }

}
