/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.calculator.sabrswaption;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorSameMethodAdapter;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.Annuity;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CapFloorCMS;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CapFloorCMSSpread;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponCMS;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.financial.interestrate.payments.provider.CapFloorCMSSABRReplicationMethod;
import com.opengamma.analytics.financial.interestrate.payments.provider.CapFloorCMSSpreadSABRBinormalMethod;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponCMSSABRReplicationMethod;
import com.opengamma.analytics.financial.interestrate.swaption.derivative.SwaptionCashFixedIbor;
import com.opengamma.analytics.financial.interestrate.swaption.derivative.SwaptionPhysicalFixedIbor;
import com.opengamma.analytics.financial.interestrate.swaption.provider.SwaptionCashFixedIborSABRMethod;
import com.opengamma.analytics.financial.interestrate.swaption.provider.SwaptionPhysicalFixedIborSABRMethod;
import com.opengamma.analytics.financial.model.option.definition.SABRInterestRateCorrelationParameters;
import com.opengamma.analytics.financial.provider.description.interestrate.SABRSwaptionProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyMulticurveSensitivity;
import com.opengamma.util.ArgumentChecker;

/**
 * Calculates the present value of an inflation instruments by discounting for a given MarketBundle.
 */
public final class PresentValueCurveSensitivitySABRSwaptionCalculator
    extends InstrumentDerivativeVisitorSameMethodAdapter<SABRSwaptionProviderInterface, MultipleCurrencyMulticurveSensitivity> {

  /**
   * The unique instance of the calculator.
   */
  private static final PresentValueCurveSensitivitySABRSwaptionCalculator INSTANCE = new PresentValueCurveSensitivitySABRSwaptionCalculator();

  /**
   * Gets the calculator instance.
   *
   * @return The calculator.
   */
  public static PresentValueCurveSensitivitySABRSwaptionCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Constructor.
   */
  private PresentValueCurveSensitivitySABRSwaptionCalculator() {
  }

  /**
   * Pricing methods.
   */
  private static final CouponCMSSABRReplicationMethod METHOD_CMS_CPN = CouponCMSSABRReplicationMethod.getInstance();
  private static final CapFloorCMSSABRReplicationMethod METHOD_CMS_CAP = CapFloorCMSSABRReplicationMethod.getDefaultInstance();
  private static final SwaptionPhysicalFixedIborSABRMethod METHOD_SWT_PHYS = SwaptionPhysicalFixedIborSABRMethod.getInstance();
  private static final SwaptionCashFixedIborSABRMethod METHOD_SWT_CASH = SwaptionCashFixedIborSABRMethod.getInstance();

  @Override
  public MultipleCurrencyMulticurveSensitivity visit(final InstrumentDerivative derivative, final SABRSwaptionProviderInterface sabr) {
    return derivative.accept(this, sabr);
  }

  // ----- Payment/Coupon ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCouponCMS(final CouponCMS payment, final SABRSwaptionProviderInterface sabr) {
    return METHOD_CMS_CPN.presentValueCurveSensitivity(payment, sabr);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCapFloorCMS(final CapFloorCMS payment, final SABRSwaptionProviderInterface sabr) {
    return METHOD_CMS_CAP.presentValueCurveSensitivity(payment, sabr);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCapFloorCMSSpread(final CapFloorCMSSpread payment, final SABRSwaptionProviderInterface sabr) {
    if (sabr.getSABRParameter() instanceof SABRInterestRateCorrelationParameters) {
      // TODO: improve correlation data handling
      final SABRInterestRateCorrelationParameters sabrCorrelation = (SABRInterestRateCorrelationParameters) sabr.getSABRParameter();
      final CapFloorCMSSpreadSABRBinormalMethod method = new CapFloorCMSSpreadSABRBinormalMethod(sabrCorrelation.getCorrelation(), METHOD_CMS_CAP,
          METHOD_CMS_CPN);
      return method.presentValueCurveSensitivity(payment, sabr);
    }
    throw new UnsupportedOperationException(
        "The PresentValueCurveSensitivitySABRSwaptionCalculator visitor visitCapFloorCMSSpread requires a SABRInterestRateCorrelationParameters as data.");
  }

  // ----- Annuity ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitGenericAnnuity(final Annuity<? extends Payment> annuity, final SABRSwaptionProviderInterface sabr) {
    ArgumentChecker.notNull(annuity, "Annuity");
    MultipleCurrencyMulticurveSensitivity cs = visit(annuity.getNthPayment(0), sabr);
    for (int loopp = 1; loopp < annuity.getNumberOfPayments(); loopp++) {
      cs = cs.plus(visit(annuity.getNthPayment(loopp), sabr));
    }
    return cs;
  }

  // ----- Swaption ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitSwaptionPhysicalFixedIbor(final SwaptionPhysicalFixedIbor swaption,
      final SABRSwaptionProviderInterface sabr) {
    return METHOD_SWT_PHYS.presentValueCurveSensitivity(swaption, sabr);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitSwaptionCashFixedIbor(final SwaptionCashFixedIbor swaption, final SABRSwaptionProviderInterface sabr) {
    return METHOD_SWT_CASH.presentValueCurveSensitivity(swaption, sabr);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visit(final InstrumentDerivative derivative) {
    throw new UnsupportedOperationException();
  }

}
