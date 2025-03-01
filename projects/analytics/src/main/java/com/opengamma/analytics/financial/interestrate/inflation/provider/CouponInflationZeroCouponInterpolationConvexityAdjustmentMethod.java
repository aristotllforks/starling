/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.interestrate.inflation.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponInterpolation;
import com.opengamma.analytics.financial.provider.description.inflation.InflationConvexityAdjustmentProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.inflation.InflationSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.inflation.MultipleCurrencyInflationSensitivity;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.MultipleCurrencyAmount;
import com.opengamma.util.tuple.DoublesPair;

/**
 * Pricing method for inflation zero-coupon. The price is computed by index estimation, discounting and using a convexity adjustment. See note "Inflation
 * convexity adjustment" by Arroub Zine-eddine for details.
 */
public class CouponInflationZeroCouponInterpolationConvexityAdjustmentMethod {

  /**
   * The convexity adjustment function used in the pricing.
   */
  private static final InflationMarketModelConvexityAdjustmentForCoupon CONVEXITY_ADJUSTMENT_FUNCTION = new InflationMarketModelConvexityAdjustmentForCoupon();

  /**
   * Computes the estimated index with the weight and the two reference end dates.
   * 
   * @param coupon
   *          The zero-coupon payment.
   * @param inflation
   *          The inflation provider.
   * @return The net amount.
   */
  public double indexEstimation(final CouponInflationZeroCouponInterpolation coupon, final InflationConvexityAdjustmentProviderInterface inflation) {
    final double estimatedIndexMonth0 = inflation.getInflationProvider().getPriceIndex(coupon.getPriceIndex(), coupon.getReferenceEndTime()[0]);
    final double estimatedIndexMonth1 = inflation.getInflationProvider().getPriceIndex(coupon.getPriceIndex(), coupon.getReferenceEndTime()[1]);
    return coupon.getWeight() * estimatedIndexMonth0 + (1 - coupon.getWeight()) * estimatedIndexMonth1;
  }

  /**
   * Computes the net amount of the zero-coupon coupon with reference index at start of the month.
   * 
   * @param coupon
   *          The zero-coupon payment.
   * @param inflation
   *          The inflation provider.
   * @return The net amount.
   */
  public MultipleCurrencyAmount netAmount(final CouponInflationZeroCouponInterpolation coupon, final InflationConvexityAdjustmentProviderInterface inflation) {
    ArgumentChecker.notNull(coupon, "Coupon");
    ArgumentChecker.notNull(inflation, "Inflation");
    final double estimatedIndex = indexEstimation(coupon, inflation);
    final double convextityAdjustment = CONVEXITY_ADJUSTMENT_FUNCTION.getZeroCouponConvexityAdjustment(coupon, inflation);
    final double netAmount = (estimatedIndex / coupon.getIndexStartValue() * convextityAdjustment - (coupon.payNotional() ? 0.0 : 1.0)) * coupon.getNotional();
    return MultipleCurrencyAmount.of(coupon.getCurrency(), netAmount);
  }

  /**
   * Computes the present value of the zero-coupon coupon with reference index at start of the month.
   * 
   * @param coupon
   *          The zero-coupon payment.
   * @param inflation
   *          The inflation provider.
   * @return The present value.
   */
  public MultipleCurrencyAmount presentValue(final CouponInflationZeroCouponInterpolation coupon,
      final InflationConvexityAdjustmentProviderInterface inflation) {
    ArgumentChecker.notNull(coupon, "Coupon");
    ArgumentChecker.notNull(inflation, "Inflation");
    final double discountFactor = inflation.getInflationProvider().getDiscountFactor(coupon.getCurrency(), coupon.getPaymentTime());
    return netAmount(coupon, inflation).multipliedBy(discountFactor);
  }

  /**
   * Computes the par spread of the zero-coupon swap with the fixed rate of the fixed leg of inflation zero-coupon swap.
   * 
   * @param coupon
   *          The zero-coupon payment.
   * @param inflation
   *          The inflation provider.
   * @param tenor
   *          the teno as used in the fixed leg
   * @param fixedRate
   *          the fixed rate
   * @return The present value.
   */
  public double parSpread(final CouponInflationZeroCouponInterpolation coupon, final InflationConvexityAdjustmentProviderInterface inflation, final int tenor,
      final double fixedRate) {
    ArgumentChecker.notNull(tenor, "Tenor");
    ArgumentChecker.notNull(coupon, "Coupon");
    ArgumentChecker.notNull(inflation, "Inflation");
    final double estimatedIndex = indexEstimation(coupon, inflation);
    final double convextityAdjustment = CONVEXITY_ADJUSTMENT_FUNCTION.getZeroCouponConvexityAdjustment(coupon, inflation);
    return Math.pow(estimatedIndex / coupon.getIndexStartValue() * convextityAdjustment - (coupon.payNotional() ? 0.0 : 1.0), 1 / tenor) - 1 - fixedRate;
  }

  /**
   * Compute the present value sensitivity to rates of a Inflation coupon.
   * 
   * @param coupon
   *          The coupon.
   * @param inflation
   *          The inflation provider.
   * @return The present value sensitivity.
   */
  public MultipleCurrencyInflationSensitivity presentValueCurveSensitivity(final CouponInflationZeroCouponInterpolation coupon,
      final InflationConvexityAdjustmentProviderInterface inflation) {
    ArgumentChecker.notNull(coupon, "Coupon");
    ArgumentChecker.notNull(inflation, "Inflation");
    final double estimatedIndexMonth0 = inflation.getInflationProvider().getPriceIndex(coupon.getPriceIndex(), coupon.getReferenceEndTime()[0]);
    final double estimatedIndexMonth1 = inflation.getInflationProvider().getPriceIndex(coupon.getPriceIndex(), coupon.getReferenceEndTime()[1]);
    final double estimatedIndex = coupon.getWeight() * estimatedIndexMonth0 + (1 - coupon.getWeight()) * estimatedIndexMonth1;
    final double convextityAdjustment = CONVEXITY_ADJUSTMENT_FUNCTION.getZeroCouponConvexityAdjustment(coupon, inflation);
    final double discountFactor = inflation.getInflationProvider().getDiscountFactor(coupon.getCurrency(), coupon.getPaymentTime());
    // Backward sweep
    final double pvBar = 1.0;
    final double discountFactorBar = (estimatedIndex / coupon.getIndexStartValue() * convextityAdjustment - (coupon.payNotional() ? 0.0 : 1.0))
        * coupon.getNotional() * pvBar;
    final double estimatedIndexBar = 1.0 / coupon.getIndexStartValue() * convextityAdjustment * discountFactor * coupon.getNotional() * pvBar;
    final double estimatedIndexMonth1Bar = (1 - coupon.getWeight()) * estimatedIndexBar;
    final double estimatedIndexMonth0Bar = coupon.getWeight() * estimatedIndexBar;
    final Map<String, List<DoublesPair>> resultMapDisc = new HashMap<>();
    final List<DoublesPair> listDiscounting = new ArrayList<>();
    listDiscounting.add(DoublesPair.of(coupon.getPaymentTime(), -coupon.getPaymentTime() * discountFactor * discountFactorBar));
    resultMapDisc.put(inflation.getInflationProvider().getName(coupon.getCurrency()), listDiscounting);
    final Map<String, List<DoublesPair>> resultMapPrice = new HashMap<>();
    final List<DoublesPair> listPrice = new ArrayList<>();
    listPrice.add(DoublesPair.of(coupon.getReferenceEndTime()[0], estimatedIndexMonth0Bar));
    listPrice.add(DoublesPair.of(coupon.getReferenceEndTime()[1], estimatedIndexMonth1Bar));
    resultMapPrice.put(inflation.getInflationProvider().getName(coupon.getPriceIndex()), listPrice);
    final InflationSensitivity inflationSensitivity = InflationSensitivity.ofYieldDiscountingAndPriceIndex(resultMapDisc, resultMapPrice);
    return MultipleCurrencyInflationSensitivity.of(coupon.getCurrency(), inflationSensitivity);
  }

}
