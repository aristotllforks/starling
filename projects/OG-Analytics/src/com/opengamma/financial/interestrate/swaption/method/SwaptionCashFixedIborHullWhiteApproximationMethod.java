/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.interestrate.swaption.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.opengamma.financial.interestrate.CashFlowEquivalentCalculator;
import com.opengamma.financial.interestrate.CashFlowEquivalentCurveSensitivityCalculator;
import com.opengamma.financial.interestrate.InterestRateDerivative;
import com.opengamma.financial.interestrate.PresentValueSensitivity;
import com.opengamma.financial.interestrate.YieldCurveBundle;
import com.opengamma.financial.interestrate.annuity.definition.AnnuityPaymentFixed;
import com.opengamma.financial.interestrate.method.PricingMethod;
import com.opengamma.financial.interestrate.swaption.SwaptionCashFixedIbor;
import com.opengamma.financial.model.interestrate.HullWhiteOneFactorPiecewiseConstantInterestRateModel;
import com.opengamma.financial.model.interestrate.definition.HullWhiteOneFactorPiecewiseConstantDataBundle;
import com.opengamma.math.statistics.distribution.NormalDistribution;
import com.opengamma.math.statistics.distribution.ProbabilityDistribution;
import com.opengamma.util.money.CurrencyAmount;
import com.opengamma.util.tuple.DoublesPair;

/**
 * Method to compute the present value of cash-settled European swaptions with the Hull-White one factor model by a third order approximation.
 * Reference: Henrard, M., Cash-Settled Swaptions: How Wrong are We? (November 2010). Available at SSRN: http://ssrn.com/abstract=1703846
 */
public class SwaptionCashFixedIborHullWhiteApproximationMethod implements PricingMethod {

  /**
   * The model used in computations.
   */
  private static final HullWhiteOneFactorPiecewiseConstantInterestRateModel MODEL = new HullWhiteOneFactorPiecewiseConstantInterestRateModel();
  /**
   * The cash flow equivalent calculator used in computations.
   */
  private static final CashFlowEquivalentCalculator CFEC = CashFlowEquivalentCalculator.getInstance();
  private static final CashFlowEquivalentCurveSensitivityCalculator CFECSC = CashFlowEquivalentCurveSensitivityCalculator.getInstance();
  /**
   * The normal distribution implementation.
   */
  private static final ProbabilityDistribution<Double> NORMAL = new NormalDistribution(0, 1);

  /**
   * Present value method using a third order approximation.
   * @param swaption The cash-settled swaption.
   * @param hwData The Hull-White parameters and the curves.
   * @return The present value.
   */
  public CurrencyAmount presentValue(final SwaptionCashFixedIbor swaption, final HullWhiteOneFactorPiecewiseConstantDataBundle hwData) {
    double expiryTime = swaption.getTimeToExpiry();
    int nbFixed = swaption.getUnderlyingSwap().getFixedLeg().getNumberOfPayments();
    double[] alphaFixed = new double[nbFixed];
    double[] dfFixed = new double[nbFixed];
    double[] discountedCashFlowFixed = new double[nbFixed];
    for (int loopcf = 0; loopcf < nbFixed; loopcf++) {
      alphaFixed[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      dfFixed[loopcf] = hwData.getCurve(swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getFundingCurveName()).getDiscountFactor(
          swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentTime());
      discountedCashFlowFixed[loopcf] = dfFixed[loopcf] * swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentYearFraction()
          * swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getNotional();
    }
    AnnuityPaymentFixed cfeIbor = CFEC.visit(swaption.getUnderlyingSwap().getSecondLeg(), hwData);
    double[] alphaIbor = new double[cfeIbor.getNumberOfPayments()];
    double[] dfIbor = new double[cfeIbor.getNumberOfPayments()];
    double[] discountedCashFlowIbor = new double[cfeIbor.getNumberOfPayments()];
    for (int loopcf = 0; loopcf < cfeIbor.getNumberOfPayments(); loopcf++) {
      alphaIbor[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, cfeIbor.getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      dfIbor[loopcf] = hwData.getCurve(cfeIbor.getDiscountCurve()).getDiscountFactor(cfeIbor.getNthPayment(loopcf).getPaymentTime());
      discountedCashFlowIbor[loopcf] = dfIbor[loopcf] * cfeIbor.getNthPayment(loopcf).getAmount();
    }
    AnnuityPaymentFixed cfe = CFEC.visit(swaption.getUnderlyingSwap(), hwData);
    double[] alpha = new double[cfe.getNumberOfPayments()];
    double[] df = new double[cfe.getNumberOfPayments()];
    double[] discountedCashFlow = new double[cfe.getNumberOfPayments()];
    for (int loopcf = 0; loopcf < cfe.getNumberOfPayments(); loopcf++) {
      alpha[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, cfe.getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      df[loopcf] = hwData.getCurve(cfe.getDiscountCurve()).getDiscountFactor(cfe.getNthPayment(loopcf).getPaymentTime());
      discountedCashFlow[loopcf] = df[loopcf] * cfe.getNthPayment(loopcf).getAmount();
    }
    double kappa = MODEL.kappa(discountedCashFlow, alpha);
    final int nbFixedPaymentYear = (int) Math.round(1.0 / swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(0).getPaymentYearFraction());
    double[] derivativesRate = new double[3];
    double[] derivativesAnnuity = new double[3];
    double x0 = 0.0; //    (swaption.getUnderlyingSwap().getFixedLeg().isPayer()) ? Math.max(kappa, 0) : Math.min(kappa, 0);
    double rate = swapRate(x0, discountedCashFlowFixed, alphaFixed, discountedCashFlowIbor, alphaIbor, derivativesRate);
    double annuity = annuityCash(rate, nbFixedPaymentYear, swaption.getUnderlyingSwap().getFixedLeg().getNumberOfPayments(), derivativesAnnuity);
    double[] u = new double[4];
    u[0] = annuity * (swaption.getStrike() - rate);
    u[1] = (swaption.getStrike() - rate) * derivativesAnnuity[0] * derivativesRate[0] - derivativesRate[0] * annuity;
    u[2] = (swaption.getStrike() - rate) * (derivativesAnnuity[0] * derivativesRate[1] + derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]) - 2 * derivativesAnnuity[0]
        * derivativesRate[0] * derivativesRate[0] - annuity * derivativesRate[1];
    u[3] = -3
        * derivativesRate[0]
        * (derivativesAnnuity[0] * derivativesRate[1] + derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0])
        - 2
        * derivativesAnnuity[0]
        * derivativesRate[0]
        * derivativesRate[1]
        + (swaption.getStrike() - rate)
        * (derivativesAnnuity[0] * derivativesRate[2] + 3 * derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[1] + derivativesAnnuity[2] * derivativesRate[0] * derivativesRate[0]
            * derivativesRate[0]) - rate * derivativesRate[2];
    double kappatilde = kappa + alphaIbor[0];
    double alpha0tilde = alphaIbor[0] + x0;
    double pv;
    if (!swaption.getUnderlyingSwap().getFixedLeg().isPayer()) {
      pv = (u[0] - u[1] * alpha0tilde + u[2] * (1 + alpha[0] * alpha[0]) / 2.0 - u[3] * (alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0) * NORMAL.getCDF(kappatilde)
          + (-u[1] - u[2] * (-2.0 * alpha0tilde + kappatilde) / 2.0 + u[3] * (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0)
          * NORMAL.getPDF(kappatilde);
    } else {
      pv = -(u[0] - u[1] * alpha0tilde + u[2] * (1 + alpha[0] * alpha[0]) / 2.0 - u[3] * (alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0) * NORMAL.getCDF(-kappatilde)
          + (-u[1] - u[2] * (-2.0 * alpha0tilde + kappatilde) / 2.0 + u[3] * (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0)
          * NORMAL.getPDF(kappatilde);
    }
    final double notional = Math.abs(swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(0).getNotional());
    return CurrencyAmount.of(swaption.getCurrency(), pv * notional * dfIbor[0] * (swaption.isLong() ? 1.0 : -1.0));
  }

  @Override
  public CurrencyAmount presentValue(InterestRateDerivative instrument, YieldCurveBundle curves) {
    Validate.isTrue(instrument instanceof SwaptionCashFixedIbor, "Cash delivery swaption");
    Validate.isTrue(curves instanceof HullWhiteOneFactorPiecewiseConstantDataBundle, "Bundle should contain Hull-White data");
    return presentValue((SwaptionCashFixedIbor) instrument, (HullWhiteOneFactorPiecewiseConstantDataBundle) curves);
  }

  /**
   * Present value sensitivity to Hull-White volatility parameters. The present value is computed using a third order approximation.
   * @param swaption The cash-settled swaption.
   * @param hwData The Hull-White parameters and the curves.
   * @return The present value sensitivity.
   */
  public double[] presentValueHullWhiteSensitivity(final SwaptionCashFixedIbor swaption, final HullWhiteOneFactorPiecewiseConstantDataBundle hwData) {
    // Forward sweep
    double expiryTime = swaption.getTimeToExpiry();
    int nbFixed = swaption.getUnderlyingSwap().getFixedLeg().getNumberOfPayments();
    double[] alphaFixed = new double[nbFixed];
    double[] dfFixed = new double[nbFixed];
    double[] discountedCashFlowFixed = new double[nbFixed];
    for (int loopcf = 0; loopcf < nbFixed; loopcf++) {
      alphaFixed[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      dfFixed[loopcf] = hwData.getCurve(swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getFundingCurveName()).getDiscountFactor(
          swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentTime());
      discountedCashFlowFixed[loopcf] = dfFixed[loopcf] * swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentYearFraction()
          * swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getNotional();
    }
    AnnuityPaymentFixed cfeIbor = CFEC.visit(swaption.getUnderlyingSwap().getSecondLeg(), hwData);
    double[] alphaIbor = new double[cfeIbor.getNumberOfPayments()];
    double[] dfIbor = new double[cfeIbor.getNumberOfPayments()];
    double[] discountedCashFlowIbor = new double[cfeIbor.getNumberOfPayments()];
    for (int loopcf = 0; loopcf < cfeIbor.getNumberOfPayments(); loopcf++) {
      alphaIbor[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, cfeIbor.getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      dfIbor[loopcf] = hwData.getCurve(cfeIbor.getDiscountCurve()).getDiscountFactor(cfeIbor.getNthPayment(loopcf).getPaymentTime());
      discountedCashFlowIbor[loopcf] = dfIbor[loopcf] * cfeIbor.getNthPayment(loopcf).getAmount();
    }
    AnnuityPaymentFixed cfe = CFEC.visit(swaption.getUnderlyingSwap(), hwData);
    double[] alpha = new double[cfe.getNumberOfPayments()];
    double[] df = new double[cfe.getNumberOfPayments()];
    double[] discountedCashFlow = new double[cfe.getNumberOfPayments()];
    for (int loopcf = 0; loopcf < cfe.getNumberOfPayments(); loopcf++) {
      alpha[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, cfe.getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      df[loopcf] = hwData.getCurve(cfe.getDiscountCurve()).getDiscountFactor(cfe.getNthPayment(loopcf).getPaymentTime());
      discountedCashFlow[loopcf] = df[loopcf] * cfe.getNthPayment(loopcf).getAmount();
    }
    double kappa = MODEL.kappa(discountedCashFlow, alpha);
    final int nbFixedPaymentYear = (int) Math.round(1.0 / swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(0).getPaymentYearFraction());
    double[] derivativesRate = new double[3];
    double[] derivativesAnnuity = new double[3];
    double x0 = 0.0; //    (swaption.getUnderlyingSwap().getFixedLeg().isPayer()) ? Math.max(kappa, 0) : Math.min(kappa, 0);
    double rate = swapRate(x0, discountedCashFlowFixed, alphaFixed, discountedCashFlowIbor, alphaIbor, derivativesRate);
    double annuity = annuityCash(rate, nbFixedPaymentYear, swaption.getUnderlyingSwap().getFixedLeg().getNumberOfPayments(), derivativesAnnuity);
    double[] u = new double[4];
    u[0] = annuity * (swaption.getStrike() - rate);
    u[1] = (swaption.getStrike() - rate) * derivativesAnnuity[0] * derivativesRate[0] - derivativesRate[0] * annuity;
    u[2] = (swaption.getStrike() - rate) * (derivativesAnnuity[0] * derivativesRate[1] + derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]) - 2 * derivativesAnnuity[0]
        * derivativesRate[0] * derivativesRate[0] - annuity * derivativesRate[1];
    u[3] = (-3 * derivativesRate[0] * (derivativesAnnuity[0] * derivativesRate[1] + derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]))
        - (2 * derivativesAnnuity[0] * derivativesRate[0] * derivativesRate[1])
        + ((swaption.getStrike() - rate) * (derivativesAnnuity[0] * derivativesRate[2] + 3 * derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[1] + derivativesAnnuity[2]
            * derivativesRate[0] * derivativesRate[0] * derivativesRate[0])) - (rate * derivativesRate[2]);
    double kappatilde = kappa + alphaIbor[0];
    double alpha0tilde = alphaIbor[0] + x0;
    double ncdf;
    double npdf = NORMAL.getPDF(kappatilde);
    if (!swaption.getUnderlyingSwap().getFixedLeg().isPayer()) {
      ncdf = NORMAL.getCDF(kappatilde);
    } else {
      ncdf = NORMAL.getCDF(-kappatilde);
    }
    final double notional = Math.abs(swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(0).getNotional());
    // Backward sweep
    double pvTotalBar = 1.0;
    double pvBar = notional * dfIbor[0] * (swaption.isLong() ? 1.0 : -1.0) * pvTotalBar;
    double alpha0tildeBar = 0.0;
    double kappatildeBar = 0.0;
    double[] uBar = new double[4];
    if (!swaption.getUnderlyingSwap().getFixedLeg().isPayer()) {
      alpha0tildeBar = ((-u[1] - u[3] * (3 * alpha0tilde * alpha0tilde + 3.0) / 6.0) * ncdf + (u[2] + u[3] * (-6.0 * alpha0tilde + 3.0 * kappatilde) / 6.0) * npdf) * pvBar;
      kappatildeBar = ((u[0] - u[1] * alpha0tilde + u[2] * (1 + alpha[0] * alpha[0]) / 2.0 - u[3] * (alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0) * npdf
          + (-u[2] / 2.0 + u[3] * (3.0 * alpha0tilde - 2.0 * kappatilde) / 6.0) * npdf + (-u[1] - u[2] * (-2.0 * alpha0tilde + kappatilde) / 2.0 + u[3]
          * (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0)
          * npdf * -kappatilde)
          * pvBar;
      uBar[0] = ncdf * pvBar;
      uBar[1] = (-alpha0tilde * ncdf - npdf) * pvBar;
      uBar[2] = ((1 + alpha[0] * alpha[0]) / 2.0 * ncdf - (-2.0 * alpha0tilde + kappatilde) / 2.0 * npdf) * pvBar;
      uBar[3] = (-(alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0 * ncdf + (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0
          * npdf)
          * pvBar;
    } else {
      alpha0tildeBar = (-(-u[1] - u[3] * (3 * alpha0tilde * alpha0tilde + 3.0) / 6.0) * ncdf + (u[2] + u[3] * (-6.0 * alpha0tilde + 3.0 * kappatilde) / 6.0) * npdf) * pvBar;
      kappatildeBar = ((u[0] - u[1] * alpha0tilde + u[2] * (1 + alpha[0] * alpha[0]) / 2.0 - u[3] * (alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0) * npdf
          + (-u[2] / 2.0 + u[3] * (3.0 * alpha0tilde - 2 * kappatilde) / 6.0) * npdf + (-u[1] - u[2] * (-2.0 * alpha0tilde + kappatilde) / 2.0 + u[3]
          * (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0)
          * npdf * -kappatilde)
          * pvBar;
      uBar[0] = -ncdf * pvBar;
      uBar[1] = (+alpha0tilde * ncdf - npdf) * pvBar;
      uBar[2] = (-(1 + alpha[0] * alpha[0]) / 2.0 * ncdf - (-2.0 * alpha0tilde + kappatilde) / 2.0 * npdf) * pvBar;
      uBar[3] = ((alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0 * ncdf + (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0
          * npdf)
          * pvBar;
    }
    double annuityBar = (swaption.getStrike() - rate) * uBar[0] - derivativesRate[0] * uBar[1] + -derivativesRate[1] * uBar[2];
    double[] derivativesAnnuityBar = new double[3];
    derivativesAnnuityBar[0] = (swaption.getStrike() - rate) * derivativesRate[0] * uBar[1] + ((swaption.getStrike() - rate) * derivativesRate[1] - 2.0 * derivativesRate[0] * derivativesRate[0])
        * uBar[2] + (-3 * derivativesRate[0] * derivativesRate[1] - 2 * derivativesRate[0] * derivativesRate[1] + (swaption.getStrike() - rate) * derivativesRate[2]) * uBar[3];
    derivativesAnnuityBar[1] = (swaption.getStrike() - rate) * derivativesRate[0] * derivativesRate[0] * uBar[2]
        + (-3 * derivativesRate[0] * derivativesRate[0] * derivativesRate[0] + (swaption.getStrike() - rate) * 3 * derivativesRate[0] * derivativesRate[1]) * uBar[3];
    derivativesAnnuityBar[2] = (swaption.getStrike() - rate) * derivativesRate[0] * derivativesRate[0] * derivativesRate[0] * uBar[3];
    double rateBar = (derivativesAnnuity[1] * derivativesAnnuityBar[0])
        + (derivativesAnnuity[2] * derivativesAnnuityBar[1])
        + (derivativesAnnuity[0] * annuityBar)
        - (annuity * uBar[0])
        - (derivativesAnnuity[0] * derivativesRate[0] * uBar[1])
        - ((derivativesAnnuity[0] * derivativesRate[1] + derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]) * uBar[2])
        - (((derivativesAnnuity[0] * derivativesRate[2] + 3 * derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[1] + derivativesAnnuity[2] * derivativesRate[0] * derivativesRate[0]
            * derivativesRate[0]) + derivativesRate[2]) * uBar[3]);
    double[] derivativesRateBar = new double[3];
    derivativesRateBar[0] = ((swaption.getStrike() - rate) * derivativesAnnuity[0] - annuity)
        * uBar[1]
        + ((swaption.getStrike() - rate) * (2.0 * derivativesAnnuity[1] * derivativesRate[0]) - 4 * derivativesAnnuity[0] * derivativesRate[0])
        * uBar[2]
        + (-3 * (derivativesAnnuity[0] * derivativesRate[1] + 3.0 * derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]) - 2 * derivativesAnnuity[0] * derivativesRate[1] + (swaption
            .getStrike() - rate) * (3 * derivativesAnnuity[1] * derivativesRate[1] + derivativesAnnuity[2] * 3.0 * derivativesRate[0] * derivativesRate[0])) * uBar[3];
    derivativesRateBar[1] = ((swaption.getStrike() - rate) * (derivativesAnnuity[0]) - annuity) * uBar[2]
        + (-3 * derivativesRate[0] * (derivativesAnnuity[0]) - 2 * derivativesAnnuity[0] * derivativesRate[0] + (swaption.getStrike() - rate) * (3 * derivativesAnnuity[1] * derivativesRate[0]))
        * uBar[3];
    derivativesRateBar[2] = ((swaption.getStrike() - rate) * derivativesAnnuity[0] - rate) * uBar[3];
    //    double kappaBar = 0.0;
    double[] alphaFixedBar = new double[nbFixed];
    double[] alphaIborBar = new double[cfeIbor.getNumberOfPayments()];
    swapRateAdjointAlpha(x0, discountedCashFlowFixed, alphaFixed, discountedCashFlowIbor, alphaIbor, rateBar, derivativesRateBar, derivativesRate, alphaFixedBar, alphaIborBar);
    alphaIborBar[0] += kappatildeBar + alpha0tildeBar;
    double[] pvsensi = new double[hwData.getHullWhiteParameter().getVolatility().length];
    double[] partialDerivatives = new double[hwData.getHullWhiteParameter().getVolatility().length];
    for (int loopcf = 0; loopcf < nbFixed; loopcf++) {
      MODEL.alpha(0.0, expiryTime, expiryTime, swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter(), partialDerivatives);
      for (int loopsigma = 0; loopsigma < hwData.getHullWhiteParameter().getVolatility().length; loopsigma++) {
        pvsensi[loopsigma] += alphaFixedBar[loopcf] * partialDerivatives[loopsigma];
      }
    }
    for (int loopcf = 0; loopcf < cfe.getNumberOfPayments(); loopcf++) {
      MODEL.alpha(0.0, expiryTime, expiryTime, cfeIbor.getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter(), partialDerivatives);
      for (int loopsigma = 0; loopsigma < hwData.getHullWhiteParameter().getVolatility().length; loopsigma++) {
        pvsensi[loopsigma] += alphaIborBar[loopcf] * partialDerivatives[loopsigma];
      }
    }
    return pvsensi;
  }

  public PresentValueSensitivity presentValueCurveSensitivity(final SwaptionCashFixedIbor swaption, final HullWhiteOneFactorPiecewiseConstantDataBundle hwData) {
    // Forward sweep
    String fundingCurveName = swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(0).getFundingCurveName();
    double expiryTime = swaption.getTimeToExpiry();
    int nbFixed = swaption.getUnderlyingSwap().getFixedLeg().getNumberOfPayments();
    double[] alphaFixed = new double[nbFixed];
    double[] dfFixed = new double[nbFixed];
    //    double[] testdfFixed = new double[nbFixed];
    double[] discountedCashFlowFixed = new double[nbFixed];
    double[] testdiscountedCashFlowFixed = new double[nbFixed];
    //    testdfFixed[4] += 1E-6;
    for (int loopcf = 0; loopcf < nbFixed; loopcf++) {
      alphaFixed[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      dfFixed[loopcf] = hwData.getCurve(fundingCurveName).getDiscountFactor(swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentTime());
      //      testdfFixed[loopcf] += dfFixed[loopcf];
      discountedCashFlowFixed[loopcf] = dfFixed[loopcf] * swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentYearFraction()
          * swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getNotional();
      testdiscountedCashFlowFixed[loopcf] = discountedCashFlowFixed[loopcf];
    }
    testdiscountedCashFlowFixed[0] += 1.0;
    AnnuityPaymentFixed cfeIbor = CFEC.visit(swaption.getUnderlyingSwap().getSecondLeg(), hwData);
    double[] alphaIbor = new double[cfeIbor.getNumberOfPayments()];
    double[] dfIbor = new double[cfeIbor.getNumberOfPayments()];
    double[] discountedCashFlowIbor = new double[cfeIbor.getNumberOfPayments()];
    for (int loopcf = 0; loopcf < cfeIbor.getNumberOfPayments(); loopcf++) {
      alphaIbor[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, cfeIbor.getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      dfIbor[loopcf] = hwData.getCurve(cfeIbor.getDiscountCurve()).getDiscountFactor(cfeIbor.getNthPayment(loopcf).getPaymentTime());
      discountedCashFlowIbor[loopcf] = dfIbor[loopcf] * cfeIbor.getNthPayment(loopcf).getAmount();
    }
    AnnuityPaymentFixed cfe = CFEC.visit(swaption.getUnderlyingSwap(), hwData);
    double[] alpha = new double[cfe.getNumberOfPayments()];
    double[] df = new double[cfe.getNumberOfPayments()];
    double[] discountedCashFlow = new double[cfe.getNumberOfPayments()];
    for (int loopcf = 0; loopcf < cfe.getNumberOfPayments(); loopcf++) {
      alpha[loopcf] = MODEL.alpha(0.0, expiryTime, expiryTime, cfe.getNthPayment(loopcf).getPaymentTime(), hwData.getHullWhiteParameter());
      df[loopcf] = hwData.getCurve(cfe.getDiscountCurve()).getDiscountFactor(cfe.getNthPayment(loopcf).getPaymentTime());
      discountedCashFlow[loopcf] = df[loopcf] * cfe.getNthPayment(loopcf).getAmount();
    }
    double kappa = MODEL.kappa(discountedCashFlow, alpha);
    final int nbFixedPaymentYear = (int) Math.round(1.0 / swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(0).getPaymentYearFraction());
    double[] derivativesRate = new double[3];
    double[] derivativesAnnuity = new double[3];
    double[] testderivativesRate = new double[3];
    double[] testderivativesAnnuity = new double[3];
    double x0 = 0.0; //    (swaption.getUnderlyingSwap().getFixedLeg().isPayer()) ? Math.max(kappa, 0) : Math.min(kappa, 0);
    double rate = swapRate(x0, discountedCashFlowFixed, alphaFixed, discountedCashFlowIbor, alphaIbor, derivativesRate);
    double testrate = swapRate(x0, testdiscountedCashFlowFixed, alphaFixed, discountedCashFlowIbor, alphaIbor, testderivativesRate);
    double annuity = annuityCash(rate, nbFixedPaymentYear, swaption.getUnderlyingSwap().getFixedLeg().getNumberOfPayments(), derivativesAnnuity);
    double testannuity = annuityCash(testrate, nbFixedPaymentYear, swaption.getUnderlyingSwap().getFixedLeg().getNumberOfPayments(), testderivativesAnnuity);
    double[] u = new double[4];
    u[0] = annuity * (swaption.getStrike() - rate);
    u[1] = (swaption.getStrike() - rate) * derivativesAnnuity[0] * derivativesRate[0] - derivativesRate[0] * annuity;
    u[2] = (swaption.getStrike() - rate) * (derivativesAnnuity[0] * derivativesRate[1] + derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]) - 2 * derivativesAnnuity[0]
        * derivativesRate[0] * derivativesRate[0] - annuity * derivativesRate[1];
    u[3] = (-3 * derivativesRate[0] * (derivativesAnnuity[0] * derivativesRate[1] + derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]))
        - (2 * derivativesAnnuity[0] * derivativesRate[0] * derivativesRate[1])
        + ((swaption.getStrike() - rate) * (derivativesAnnuity[0] * derivativesRate[2] + 3 * derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[1] + derivativesAnnuity[2]
            * derivativesRate[0] * derivativesRate[0] * derivativesRate[0])) - (rate * derivativesRate[2]);

    double[] testu = new double[4];
    testu[0] = testannuity * (swaption.getStrike() - testrate);
    testu[1] = (swaption.getStrike() - testrate) * testderivativesAnnuity[0] * testderivativesRate[0] - testderivativesRate[0] * testannuity;
    testu[2] = (swaption.getStrike() - testrate) * (testderivativesAnnuity[0] * testderivativesRate[1] + testderivativesAnnuity[1] * testderivativesRate[0] * testderivativesRate[0]) - 2
        * testderivativesAnnuity[0] * testderivativesRate[0] * testderivativesRate[0] - testannuity * testderivativesRate[1];
    testu[3] = (-3 * testderivativesRate[0] * (testderivativesAnnuity[0] * testderivativesRate[1] + testderivativesAnnuity[1] * testderivativesRate[0] * testderivativesRate[0]))
        - (2 * testderivativesAnnuity[0] * testderivativesRate[0] * testderivativesRate[1])
        + ((swaption.getStrike() - testrate) * (testderivativesAnnuity[0] * testderivativesRate[2] + 3 * testderivativesAnnuity[1] * testderivativesRate[0] * testderivativesRate[1] + testderivativesAnnuity[2]
            * testderivativesRate[0] * testderivativesRate[0] * testderivativesRate[0])) - (testrate * testderivativesRate[2]);

    double kappatilde = kappa + alphaIbor[0];
    double alpha0tilde = alphaIbor[0] + x0;
    double ncdf;
    double npdf = NORMAL.getPDF(kappatilde);
    double pv;
    double testpv;
    if (!swaption.getUnderlyingSwap().getFixedLeg().isPayer()) {
      ncdf = NORMAL.getCDF(kappatilde);
      pv = (u[0] - u[1] * alpha0tilde + u[2] * (1 + alpha[0] * alpha[0]) / 2.0 - u[3] * (alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0) * ncdf
          + (-u[1] - u[2] * (-2.0 * alpha0tilde + kappatilde) / 2.0 + u[3] * (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0) * npdf;
      testpv = (u[0] - u[1] * alpha0tilde + u[2] * (1 + alpha[0] * alpha[0]) / 2.0 - u[3] * (alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0) * ncdf
          + (-u[1] - u[2] * (-2.0 * alpha0tilde + kappatilde) / 2.0 + u[3] * (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0) * npdf;
    } else {
      ncdf = NORMAL.getCDF(-kappatilde);
      pv = -(u[0] - u[1] * alpha0tilde + u[2] * (1 + alpha[0] * alpha[0]) / 2.0 - u[3] * (alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0) * ncdf
          + (-u[1] - u[2] * (-2.0 * alpha0tilde + kappatilde) / 2.0 + u[3] * (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0) * npdf;
      testpv = -(testu[0] - testu[1] * alpha0tilde + testu[2] * (1 + alpha[0] * alpha[0]) / 2.0 - testu[3] * (alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0) * ncdf
          + (-testu[1] - testu[2] * (-2.0 * alpha0tilde + kappatilde) / 2.0 + testu[3] * (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0)
          * npdf;
    }
    final double notional = Math.abs(swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(0).getNotional());
    // Backward sweep
    double pvTotalBar = 1.0;
    double pvBar = notional * dfIbor[0] * (swaption.isLong() ? 1.0 : -1.0) * pvTotalBar;
    double[] uBar = new double[4];
    if (!swaption.getUnderlyingSwap().getFixedLeg().isPayer()) {
      uBar[0] = ncdf * pvBar;
      uBar[1] = (-alpha0tilde * ncdf - npdf) * pvBar;
      uBar[2] = ((1 + alpha[0] * alpha[0]) / 2.0 * ncdf - (-2.0 * alpha0tilde + kappatilde) / 2.0 * npdf) * pvBar;
      uBar[3] = (-(alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0 * ncdf + (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0
          * npdf)
          * pvBar;
    } else {
      uBar[0] = -ncdf * pvBar;
      uBar[1] = (+alpha0tilde * ncdf - npdf) * pvBar;
      uBar[2] = (-(1 + alpha[0] * alpha[0]) / 2.0 * ncdf - (-2.0 * alpha0tilde + kappatilde) / 2.0 * npdf) * pvBar;
      uBar[3] = ((alpha0tilde * alpha0tilde * alpha0tilde + 3.0 * alpha0tilde) / 6.0 * ncdf + (-3 * alpha0tilde * alpha0tilde + 3.0 * kappatilde * alpha0tilde - kappatilde * kappatilde - 2.0) / 6.0
          * npdf)
          * pvBar;
    }
    double annuityBar = (swaption.getStrike() - rate) * uBar[0] - derivativesRate[0] * uBar[1] + -derivativesRate[1] * uBar[2];
    double[] derivativesAnnuityBar = new double[3];
    derivativesAnnuityBar[0] = (swaption.getStrike() - rate) * derivativesRate[0] * uBar[1] + ((swaption.getStrike() - rate) * derivativesRate[1] - 2.0 * derivativesRate[0] * derivativesRate[0])
        * uBar[2] + (-3 * derivativesRate[0] * derivativesRate[1] - 2 * derivativesRate[0] * derivativesRate[1] + (swaption.getStrike() - rate) * derivativesRate[2]) * uBar[3];
    derivativesAnnuityBar[1] = (swaption.getStrike() - rate) * derivativesRate[0] * derivativesRate[0] * uBar[2]
        + (-3 * derivativesRate[0] * derivativesRate[0] * derivativesRate[0] + (swaption.getStrike() - rate) * 3 * derivativesRate[0] * derivativesRate[1]) * uBar[3];
    derivativesAnnuityBar[2] = (swaption.getStrike() - rate) * derivativesRate[0] * derivativesRate[0] * derivativesRate[0] * uBar[3];
    double rateBar = (derivativesAnnuity[1] * derivativesAnnuityBar[0])
        + (derivativesAnnuity[2] * derivativesAnnuityBar[1])
        + (derivativesAnnuity[0] * annuityBar)
        - (annuity * uBar[0])
        - (derivativesAnnuity[0] * derivativesRate[0] * uBar[1])
        - ((derivativesAnnuity[0] * derivativesRate[1] + derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]) * uBar[2])
        - (((derivativesAnnuity[0] * derivativesRate[2] + 3 * derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[1] + derivativesAnnuity[2] * derivativesRate[0] * derivativesRate[0]
            * derivativesRate[0]) + derivativesRate[2]) * uBar[3]);
    double[] derivativesRateBar = new double[3];
    derivativesRateBar[0] = ((swaption.getStrike() - rate) * derivativesAnnuity[0] - annuity)
        * uBar[1]
        + ((swaption.getStrike() - rate) * (2.0 * derivativesAnnuity[1] * derivativesRate[0]) - 4 * derivativesAnnuity[0] * derivativesRate[0])
        * uBar[2]
        + (-3 * (derivativesAnnuity[0] * derivativesRate[1] + 3.0 * derivativesAnnuity[1] * derivativesRate[0] * derivativesRate[0]) - 2 * derivativesAnnuity[0] * derivativesRate[1] + (swaption
            .getStrike() - rate) * (3 * derivativesAnnuity[1] * derivativesRate[1] + derivativesAnnuity[2] * 3.0 * derivativesRate[0] * derivativesRate[0])) * uBar[3];
    derivativesRateBar[1] = ((swaption.getStrike() - rate) * (derivativesAnnuity[0]) - annuity) * uBar[2]
        + (-3 * derivativesRate[0] * (derivativesAnnuity[0]) - 2 * derivativesAnnuity[0] * derivativesRate[0] + (swaption.getStrike() - rate) * (3 * derivativesAnnuity[1] * derivativesRate[0]))
        * uBar[3];
    derivativesRateBar[2] = ((swaption.getStrike() - rate) * derivativesAnnuity[0] - rate) * uBar[3];
    //    double kappaBar = 0.0;
    double[] discountedCashFlowFixedBar = new double[nbFixed];
    double[] discountedCashFlowIborBar = new double[cfeIbor.getNumberOfPayments()];
    //    double rate1 = swapRateAdjointDiscountedCF(x0, discountedCashFlowFixed, alphaFixed, discountedCashFlowIbor, alphaIbor, 0.0, new double[] {1.0, 0.0, 0.0}, derivativesRate,
    //        discountedCashFlowFixedBar, discountedCashFlowIborBar);
    //    double rate2 = swapRateAdjointDiscountedCF(x0, testdiscountedCashFlowFixed, alphaFixed, discountedCashFlowIbor, alphaIbor, 1.0, new double[3], derivativesRate, discountedCashFlowFixedBar,
    //        discountedCashFlowIborBar);

    swapRateAdjointDiscountedCF(x0, discountedCashFlowFixed, alphaFixed, discountedCashFlowIbor, alphaIbor, rateBar, derivativesRateBar, derivativesRate, discountedCashFlowFixedBar,
        discountedCashFlowIborBar);

    double[] dfFixedBar = new double[nbFixed];
    final List<DoublesPair> listDf = new ArrayList<DoublesPair>();
    for (int loopcf = 0; loopcf < nbFixed; loopcf++) {
      dfFixedBar[loopcf] = swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentYearFraction() * swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getNotional()
          * discountedCashFlowFixedBar[loopcf];
      DoublesPair dfSensi = new DoublesPair(swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf).getPaymentTime(), -swaption.getUnderlyingSwap().getFixedLeg().getNthPayment(loopcf)
          .getPaymentTime()
          * dfFixed[loopcf] * dfFixedBar[loopcf]);
      listDf.add(dfSensi);
    }

    double[] dfIborBar = new double[cfeIbor.getNumberOfPayments()];
    double[] cfeAmountIborBar = new double[cfeIbor.getNumberOfPayments()];
    dfIborBar[0] = pv * notional * (swaption.isLong() ? 1.0 : -1.0);
    for (int loopcf = 0; loopcf < cfe.getNumberOfPayments(); loopcf++) {
      dfIborBar[loopcf] += cfeIbor.getNthPayment(loopcf).getAmount() * discountedCashFlowIborBar[loopcf];
      DoublesPair dfSensi = new DoublesPair(cfeIbor.getNthPayment(loopcf).getPaymentTime(), -cfeIbor.getNthPayment(loopcf).getPaymentTime() * dfIbor[loopcf] * dfIborBar[loopcf]);
      listDf.add(dfSensi);
      cfeAmountIborBar[loopcf] = dfIbor[loopcf] * discountedCashFlowIborBar[loopcf];
    }
    final Map<String, List<DoublesPair>> pvsDF = new HashMap<String, List<DoublesPair>>();
    pvsDF.put(fundingCurveName, listDf);
    PresentValueSensitivity sensitivity = new PresentValueSensitivity(pvsDF);

    Map<Double, PresentValueSensitivity> cfeIborCurveSensi = CFECSC.visit(swaption.getUnderlyingSwap().getSecondLeg(), hwData);
    for (int loopcf = 0; loopcf < cfeIbor.getNumberOfPayments(); loopcf++) {
      PresentValueSensitivity sensiCfe = cfeIborCurveSensi.get(cfeIbor.getNthPayment(loopcf).getPaymentTime());
      if (!(sensiCfe == null)) { // There is some sensitivity to that cfe.
        sensitivity = sensitivity.add(sensiCfe.multiply(cfeAmountIborBar[loopcf]));
      }
    }

    return sensitivity;
  }

  /**
   * Computation of the swap rate for a given random variable in the Hull-White one factor model.
   * @param x The random variable.
   * @param discountedCashFlowFixed The discounted cash flows.
   * @param alphaFixed The bond volatilities.
   * @param discountedCashFlowIbor The discounted cash flows.
   * @param alphaIbor The bond volatilities.
   * @param derivatives Array used to return the derivatives of the swap rate with respect to the random variable. The array is changed by the method. 
   * The values are [0] the first order derivative and [1] the second order derivative.
   * @return The swap rate.
   */
  private double swapRate(double x, final double[] discountedCashFlowFixed, final double[] alphaFixed, final double[] discountedCashFlowIbor, final double[] alphaIbor, double[] derivatives) {
    double[] f = new double[3];
    double y1;
    for (int loopcf = 0; loopcf < discountedCashFlowIbor.length; loopcf++) {
      y1 = -discountedCashFlowIbor[loopcf] * Math.exp(-alphaIbor[loopcf] * x - alphaIbor[loopcf] * alphaIbor[loopcf] / 2.0);
      f[0] += y1;
      f[1] += -alphaIbor[loopcf] * y1;
      f[2] += alphaIbor[loopcf] * alphaIbor[loopcf] * y1;
    }
    double[] g = new double[3];
    double y2;
    for (int loopcf = 0; loopcf < discountedCashFlowFixed.length; loopcf++) {
      y2 = discountedCashFlowFixed[loopcf] * Math.exp(-alphaFixed[loopcf] * x - alphaFixed[loopcf] * alphaFixed[loopcf] / 2.0);
      g[0] += y2;
      g[1] += -alphaFixed[loopcf] * y2;
      g[2] += alphaFixed[loopcf] * alphaFixed[loopcf] * y2;
    }
    double swapRate = f[0] / g[0];
    derivatives[0] = (f[1] * g[0] - f[0] * g[1]) / (g[0] * g[0]);
    derivatives[1] = (f[2] * g[0] - f[0] * g[2]) / (g[0] * g[0]) - (f[1] * g[0] - f[0] * g[1]) * 2 * g[1] / (g[0] * g[0] * g[0]);
    return swapRate;
  }

  /**
   * Computation of the swap rate and its derivative with respect to the input parameters for a given random variable in the Hull-White one factor model.
   * @param x The random variable.
   * @param discountedCashFlowFixed The discounted cash flows.
   * @param alphaFixed The bond volatilities.
   * @param discountedCashFlowIbor The discounted cash flows.
   * @param alphaIbor The bond volatilities.
   * @param swapRateBar The sensitivity to the swap rate in the rest of the computation.
   * @param derivativesBar The sensitivity to the swap rate derivatives in the rest of the computation.
   * @param derivatives Array used to return the derivatives of the swap rate with respect to the random variable. The array is changed by the method. 
   * The values are [0] the first order derivative and [1] the second order derivative.
   * @param alphaFixedBar Array used to return the derivatives of the result with respect to the alphaFixed variables.
   * @param alphaIborBar Array used to return the derivatives of the result with respect to the alphaIbor variables.
   * @return The swap rate.
   */
  private double swapRateAdjointAlpha(double x, final double[] discountedCashFlowFixed, final double[] alphaFixed, final double[] discountedCashFlowIbor, final double[] alphaIbor, double swapRateBar,
      double[] derivativesBar, double[] derivatives, double[] alphaFixedBar, double[] alphaIborBar) {
    double[] f = new double[3];
    double[] y1 = new double[discountedCashFlowIbor.length];
    for (int loopcf = 0; loopcf < discountedCashFlowIbor.length; loopcf++) {
      y1[loopcf] = -discountedCashFlowIbor[loopcf] * Math.exp(-alphaIbor[loopcf] * x - alphaIbor[loopcf] * alphaIbor[loopcf] / 2.0);
      f[0] += y1[loopcf];
      f[1] += -alphaIbor[loopcf] * y1[loopcf];
      f[2] += alphaIbor[loopcf] * alphaIbor[loopcf] * y1[loopcf];
    }
    double[] g = new double[3];
    double[] y2 = new double[discountedCashFlowFixed.length];
    for (int loopcf = 0; loopcf < discountedCashFlowFixed.length; loopcf++) {
      y2[loopcf] = discountedCashFlowFixed[loopcf] * Math.exp(-alphaFixed[loopcf] * x - alphaFixed[loopcf] * alphaFixed[loopcf] / 2.0);
      g[0] += y2[loopcf];
      g[1] += -alphaFixed[loopcf] * y2[loopcf];
      g[2] += alphaFixed[loopcf] * alphaFixed[loopcf] * y2[loopcf];
    }
    double swapRate = f[0] / g[0];
    derivatives[0] = (f[1] * g[0] - f[0] * g[1]) / (g[0] * g[0]);
    derivatives[1] = (f[2] * g[0] - f[0] * g[2]) / (g[0] * g[0]) - (f[1] * g[0] - f[0] * g[1]) * 2 * g[1] / (g[0] * g[0] * g[0]);
    // Backward sweep
    double[] gBar = new double[3];
    gBar[0] = -f[0] / (g[0] * g[0]) * swapRateBar + (-f[1] / (g[0] * g[0]) + f[0] * g[1] / (g[0] * g[0] * g[0])) * derivativesBar[0]
        + (-f[2] / (g[0] * g[0]) + 2.0 * (f[0] * g[2] + 2 * g[1] * f[1]) / (g[0] * g[0] * g[0]) - 6.0 * f[0] * g[1] * g[1] / (g[0] * g[0] * g[0] * g[0])) * derivativesBar[1];
    gBar[1] = -f[0] / (g[0] * g[0]) * derivativesBar[0] + (-2 * f[1] / (g[0] * g[0]) + 4 * f[0] * g[1] / (g[0] * g[0] * g[0])) * derivativesBar[1];
    gBar[2] = -f[0] / (g[0] * g[0]) * derivativesBar[1];
    double y2p;
    for (int loopcf = 0; loopcf < discountedCashFlowFixed.length; loopcf++) {
      y2p = y2[loopcf] * (-x - alphaFixed[loopcf]);
      alphaFixedBar[loopcf] = y2p * gBar[0] + (-y2[loopcf] - alphaFixed[loopcf] * y2p) * gBar[1] + (2.0 * alphaFixed[loopcf] * y2[loopcf] + alphaFixed[loopcf] * alphaFixed[loopcf] * y2p) * gBar[2];
    }
    double[] fBar = new double[3];
    fBar[0] = 1.0 / g[0] * swapRateBar - g[1] / (g[0] * g[0]) * derivativesBar[0] + (-g[2] / (g[0] * g[0]) + 2 * g[1] * g[1] / (g[0] * g[0] * g[0])) * derivativesBar[1];
    fBar[1] = 1.0 / g[0] * derivativesBar[0] + -2 * g[1] / (g[0] * g[0]) * derivativesBar[1];
    fBar[2] = 1.0 / g[0] * derivativesBar[1];
    double y1p;
    for (int loopcf = 0; loopcf < discountedCashFlowIbor.length; loopcf++) {
      y1p = y1[loopcf] * (-x - alphaIbor[loopcf]);
      alphaIborBar[loopcf] = y1p * fBar[0] + (-y1[loopcf] - alphaIbor[loopcf] * y1p) * fBar[1] + (2 * alphaIbor[loopcf] * y1[loopcf] + alphaIbor[loopcf] * alphaIbor[loopcf] * y1p) * fBar[2];
    }
    return swapRate;
  }

  private double swapRateAdjointDiscountedCF(double x, final double[] discountedCashFlowFixed, final double[] alphaFixed, final double[] discountedCashFlowIbor, final double[] alphaIbor,
      double swapRateBar, double[] derivativesBar, double[] derivatives, double[] discountedCashFlowFixedBar, double[] discountedCashFlowIborBar) {

    double[] f = new double[3];
    double[] y1 = new double[discountedCashFlowIbor.length];
    for (int loopcf = 0; loopcf < discountedCashFlowIbor.length; loopcf++) {
      y1[loopcf] = -Math.exp(-alphaIbor[loopcf] * x - alphaIbor[loopcf] * alphaIbor[loopcf] / 2.0);
      f[0] += discountedCashFlowIbor[loopcf] * y1[loopcf];
      f[1] += -alphaIbor[loopcf] * discountedCashFlowIbor[loopcf] * y1[loopcf];
      f[2] += alphaIbor[loopcf] * alphaIbor[loopcf] * discountedCashFlowIbor[loopcf] * y1[loopcf];
    }
    double[] g = new double[3];
    double[] y2 = new double[discountedCashFlowFixed.length];
    for (int loopcf = 0; loopcf < discountedCashFlowFixed.length; loopcf++) {
      y2[loopcf] = Math.exp(-alphaFixed[loopcf] * x - alphaFixed[loopcf] * alphaFixed[loopcf] / 2.0);
      g[0] += discountedCashFlowFixed[loopcf] * y2[loopcf];
      g[1] += -alphaFixed[loopcf] * discountedCashFlowFixed[loopcf] * y2[loopcf];
      g[2] += alphaFixed[loopcf] * alphaFixed[loopcf] * discountedCashFlowFixed[loopcf] * y2[loopcf];
    }
    double swapRate = f[0] / g[0];
    derivatives[0] = (f[1] * g[0] - f[0] * g[1]) / (g[0] * g[0]);
    derivatives[1] = (f[2] * g[0] - f[0] * g[2]) / (g[0] * g[0]) - (f[1] * g[0] - f[0] * g[1]) * 2 * g[1] / (g[0] * g[0] * g[0]);
    // Backward sweep
    double[] gBar = new double[3];
    gBar[0] = -f[0] / (g[0] * g[0]) * swapRateBar + (-f[1] / (g[0] * g[0]) + 2 * f[0] * g[1] / (g[0] * g[0] * g[0])) * derivativesBar[0]
        + (-f[2] / (g[0] * g[0]) + 2.0 * (f[0] * g[2] + 2 * g[1] * f[1]) / (g[0] * g[0] * g[0]) - 6.0 * f[0] * g[1] * g[1] / (g[0] * g[0] * g[0] * g[0])) * derivativesBar[1];
    gBar[1] = -f[0] / (g[0] * g[0]) * derivativesBar[0] + (-2 * f[1] / (g[0] * g[0]) + 4 * f[0] * g[1] / (g[0] * g[0] * g[0])) * derivativesBar[1];
    gBar[2] = -f[0] / (g[0] * g[0]) * derivativesBar[1];
    for (int loopcf = 0; loopcf < discountedCashFlowFixed.length; loopcf++) {
      discountedCashFlowFixedBar[loopcf] = y2[loopcf] * gBar[0] + -alphaFixed[loopcf] * y2[loopcf] * gBar[1] + alphaFixed[loopcf] * alphaFixed[loopcf] * y2[loopcf] * gBar[2];
    }
    double[] fBar = new double[3];
    fBar[0] = 1.0 / g[0] * swapRateBar - g[1] / (g[0] * g[0]) * derivativesBar[0] + (-g[2] / (g[0] * g[0]) + 2 * g[1] * g[1] / (g[0] * g[0] * g[0])) * derivativesBar[1];
    fBar[1] = 1.0 / g[0] * derivativesBar[0] + -2 * g[1] / (g[0] * g[0]) * derivativesBar[1];
    fBar[2] = 1.0 / g[0] * derivativesBar[1];
    for (int loopcf = 0; loopcf < discountedCashFlowIbor.length; loopcf++) {
      discountedCashFlowIborBar[loopcf] = y1[loopcf] * fBar[0] + -alphaIbor[loopcf] * y1[loopcf] * fBar[1] + alphaIbor[loopcf] * alphaIbor[loopcf] * y1[loopcf] * fBar[2];
    }
    return swapRate;
  }

  /**
   * Computes the cash annuity from the swap rate and its derivatives.
   * @param swapRate The swap rate.
   * @param nbFixedPaymentYear The number of fixed payment per year.
   * @param nbFixedPeriod The total number of payments.
   * @param derivatives Array used to return the derivatives of the annuity with respect to the swap rate. The array is changed by the method. 
   * The values are [0] the first order derivative, [1] the second order derivative and [2] the third order derivative.
   * @return
   */
  private double annuityCash(double swapRate, int nbFixedPaymentYear, int nbFixedPeriod, double[] derivatives) {
    double invfact = 1 + swapRate / nbFixedPaymentYear;
    double annuity = 1.0 / swapRate * (1.0 - 1.0 / Math.pow(invfact, nbFixedPeriod));
    derivatives[0] = 0.0;
    derivatives[1] = 0.0;
    derivatives[2] = 0.0;
    for (int looppay = 0; looppay < nbFixedPeriod; looppay++) {
      derivatives[0] += -(looppay + 1) * Math.pow(invfact, -looppay - 2) / (nbFixedPaymentYear * nbFixedPaymentYear);
      derivatives[1] += (looppay + 1) * (looppay + 2) * Math.pow(invfact, -looppay - 3) / (nbFixedPaymentYear * nbFixedPaymentYear * nbFixedPaymentYear);
      derivatives[2] += -(looppay + 1) * (looppay + 2) * (looppay + 3) * Math.pow(invfact, -looppay - 4) / (nbFixedPaymentYear * nbFixedPaymentYear * nbFixedPaymentYear * nbFixedPaymentYear);
    }
    return annuity;
  }

}
