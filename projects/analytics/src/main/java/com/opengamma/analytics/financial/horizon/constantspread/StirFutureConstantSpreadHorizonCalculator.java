/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.horizon.constantspread;

import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.horizon.HorizonCalculator;
import com.opengamma.analytics.financial.horizon.rolldown.CurveProviderConstantSpreadRolldown;
import com.opengamma.analytics.financial.instrument.future.InterestRateFutureTransactionDefinition;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueDiscountingCalculator;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.util.time.TimeCalculator;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.MultipleCurrencyAmount;

/**
 *
 */
public class StirFutureConstantSpreadHorizonCalculator
    extends HorizonCalculator<InterestRateFutureTransactionDefinition, MulticurveProviderInterface, Double> {

  @Override
  public MultipleCurrencyAmount getTheta(final InterestRateFutureTransactionDefinition definition, final ZonedDateTime date,
      final MulticurveProviderInterface data, final int daysForward, final Calendar calendar) {
    throw new UnsupportedOperationException("Must supply a last margin price");
  }

  @Override
  public MultipleCurrencyAmount getTheta(final InterestRateFutureTransactionDefinition definition, final ZonedDateTime date,
      final MulticurveProviderInterface data, final int daysForward, final Calendar calendar, final Double lastMarginPrice) {
    ArgumentChecker.notNull(definition, "definition");
    ArgumentChecker.notNull(date, "date");
    ArgumentChecker.notNull(data, "data");
    ArgumentChecker.notNull(lastMarginPrice, "last margin price");
    ArgumentChecker.notNull(calendar, "calendar");
    ArgumentChecker.isTrue(daysForward == 1 || daysForward == -1, "daysForward must be either 1 or -1");
    final InstrumentDerivative instrumentToday = definition.toDerivative(date, lastMarginPrice);
    final ZonedDateTime horizonDate = date.plusDays(daysForward);
    final double shiftTime = TimeCalculator.getTimeBetween(date, horizonDate);
    final InstrumentDerivative instrumentTomorrow = definition.toDerivative(horizonDate, lastMarginPrice);
    final MulticurveProviderInterface tomorrowData = (MulticurveProviderInterface) CurveProviderConstantSpreadRolldown.INSTANCE
        .rollDown(data, shiftTime);
    final PresentValueDiscountingCalculator pvCalculator = PresentValueDiscountingCalculator.getInstance();
    return subtract(instrumentTomorrow.accept(pvCalculator, tomorrowData), instrumentToday.accept(pvCalculator, data));
  }

}
