/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.horizon.forwardslide;

import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.equity.trs.definition.EquityTotalReturnSwap;
import com.opengamma.analytics.financial.equity.trs.definition.EquityTotalReturnSwapDefinition;
import com.opengamma.analytics.financial.horizon.HorizonCalculator;
import com.opengamma.analytics.financial.horizon.rolldown.CurveProviderForwardSlideRolldown;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueDiscountingCalculator;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.util.time.TimeCalculator;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.timeseries.precise.zdt.ImmutableZonedDateTimeDoubleTimeSeries;
import com.opengamma.timeseries.precise.zdt.ZonedDateTimeDoubleTimeSeries;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.MultipleCurrencyAmount;

/**
 * Calculates the difference in the present value of an equity total return swap between two dates with rate slide.
 * <p>
 * Only the funding leg is considered in this calculation.
 */
public final class EquityTrsForwardSlideHorizonCalculator
    extends HorizonCalculator<EquityTotalReturnSwapDefinition, MulticurveProviderInterface, ZonedDateTimeDoubleTimeSeries> {
  /** The present value calculator */
  private static final InstrumentDerivativeVisitor<MulticurveProviderInterface, MultipleCurrencyAmount> PV_CALCULATOR = PresentValueDiscountingCalculator
      .getInstance();
  /** The singleton instance */
  private static final HorizonCalculator<EquityTotalReturnSwapDefinition, MulticurveProviderInterface, ZonedDateTimeDoubleTimeSeries> INSTANCE = new EquityTrsForwardSlideHorizonCalculator();

  /**
   * Gets the singleton instance.
   *
   * @return The instance
   */
  public static HorizonCalculator<EquityTotalReturnSwapDefinition, MulticurveProviderInterface, ZonedDateTimeDoubleTimeSeries> getInstance() {
    return INSTANCE;
  }

  /**
   * Private constructor
   */
  private EquityTrsForwardSlideHorizonCalculator() {
  }

  @Override
  public MultipleCurrencyAmount getTheta(final EquityTotalReturnSwapDefinition definition, final ZonedDateTime date,
      final MulticurveProviderInterface data,
      final int daysForward, final Calendar calendar, final ZonedDateTimeDoubleTimeSeries fixingSeries) {
    ArgumentChecker.notNull(definition, "definition");
    ArgumentChecker.notNull(date, "date");
    ArgumentChecker.notNull(data, "data");
    ArgumentChecker.isTrue(daysForward == 1 || daysForward == -1, "daysForward must be either 1 or -1");
    final EquityTotalReturnSwap instrumentToday = definition.toDerivative(date, fixingSeries);
    final ZonedDateTime horizonDate = date.plusDays(daysForward);
    final double shiftTime = TimeCalculator.getTimeBetween(date, horizonDate);
    final EquityTotalReturnSwap instrumentTomorrow = definition.toDerivative(horizonDate, fixingSeries);
    final MulticurveProviderInterface dataTomorrow = (MulticurveProviderInterface) CurveProviderForwardSlideRolldown.INSTANCE
        .rollDown(data, shiftTime);
    final MultipleCurrencyAmount pvTomorrow = instrumentTomorrow.getFundingLeg().accept(PV_CALCULATOR, dataTomorrow);
    final MultipleCurrencyAmount pvToday = instrumentToday.getFundingLeg().accept(PV_CALCULATOR, data);
    return subtract(pvTomorrow, pvToday);
  }

  @Override
  public MultipleCurrencyAmount getTheta(final EquityTotalReturnSwapDefinition definition, final ZonedDateTime date,
      final MulticurveProviderInterface data,
      final int daysForward, final Calendar calendar) {
    return getTheta(definition, date, data, daysForward, calendar, ImmutableZonedDateTimeDoubleTimeSeries.ofEmptyUTC());
  }

}
