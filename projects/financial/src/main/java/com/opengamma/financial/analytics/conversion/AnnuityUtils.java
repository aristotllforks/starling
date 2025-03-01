/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.conversion;

import java.util.List;

import org.threeten.bp.LocalDate;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.instrument.NotionalProvider;
import com.opengamma.analytics.financial.instrument.annuity.AbstractAnnuityDefinitionBuilder.CouponStub;
import com.opengamma.analytics.financial.instrument.annuity.AdjustedDateParameters;
import com.opengamma.analytics.financial.instrument.annuity.AnnuityDefinition;
import com.opengamma.analytics.financial.instrument.annuity.FixedAnnuityDefinitionBuilder;
import com.opengamma.analytics.financial.instrument.annuity.FloatingAnnuityDefinitionBuilder;
import com.opengamma.analytics.financial.instrument.annuity.OffsetAdjustedDateParameters;
import com.opengamma.analytics.financial.instrument.annuity.OffsetType;
import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.instrument.index.IndexDeposit;
import com.opengamma.core.convention.Convention;
import com.opengamma.core.convention.ConventionSource;
import com.opengamma.core.holiday.HolidaySource;
import com.opengamma.core.security.Security;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.financial.analytics.curve.ConverterUtils;
import com.opengamma.financial.convention.HolidaySourceCalendarAdapter;
import com.opengamma.financial.convention.IborIndexConvention;
import com.opengamma.financial.convention.OvernightIndexConvention;
import com.opengamma.financial.convention.StubType;
import com.opengamma.financial.convention.VanillaIborLegConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.rolldate.EndOfMonthRollDateAdjuster;
import com.opengamma.financial.convention.rolldate.RollConvention;
import com.opengamma.financial.convention.rolldate.RollDateAdjuster;
import com.opengamma.financial.security.index.OvernightIndex;
import com.opengamma.financial.security.irs.FixedInterestRateSwapLeg;
import com.opengamma.financial.security.irs.FloatingInterestRateSwapLeg;
import com.opengamma.financial.security.irs.InterestRateSwapLeg;
import com.opengamma.financial.security.irs.InterestRateSwapNotional;
import com.opengamma.financial.security.irs.InterestRateSwapNotionalVisitor;
import com.opengamma.financial.security.irs.NotionalExchange;
import com.opengamma.financial.security.irs.StubCalculationMethod;
import com.opengamma.financial.security.lookup.irs.InterestRateSwapNotionalAmountVisitor;
import com.opengamma.financial.security.swap.FloatingRateType;
import com.opengamma.id.ExternalId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.Pair;
import com.opengamma.util.tuple.Pairs;

/**
 * Class containing utility methods for generating annuities from swap legs.
 */
public class AnnuityUtils {

  /**
   * Builds a floating annuity definition from an {@link InterestRateSwapLeg}.
   * 
   * @param conventionSource
   *          The convention source, not null
   * @param holidaySource
   *          The holiday source, not null
   * @param securitySource
   *          The security source, not null
   * @param payer
   *          True if the swap is a payer (i.e. pays a fixed rate)
   * @param startDate
   *          The start date, not null
   * @param endDate
   *          The end date, not null
   * @param notionalExchange
   *          The type of notional exchange, not null
   * @param leg
   *          The interest rate swap leg, not null
   * @return A floating annuity definition. The coupons are not necessarily the same type.
   */
  public static AnnuityDefinition<?> buildFloatingAnnuityDefinition(final ConventionSource conventionSource, final HolidaySource holidaySource,
      final SecuritySource securitySource,
      final boolean payer, final LocalDate startDate, final LocalDate endDate, final NotionalExchange notionalExchange, final InterestRateSwapLeg leg) {
    ArgumentChecker.notNull(conventionSource, "conventionSource");
    ArgumentChecker.notNull(holidaySource, "holidaySource");
    ArgumentChecker.notNull(securitySource, "securitySource");
    ArgumentChecker.notNull(startDate, "startDate");
    ArgumentChecker.notNull(endDate, "endDate");
    ArgumentChecker.notNull(notionalExchange, "notionalExchange");
    ArgumentChecker.notNull(leg, "leg");
    final FloatingInterestRateSwapLeg floatLeg = (FloatingInterestRateSwapLeg) leg;

    AdjustedDateParameters maturityDateParameters = null;
    if (leg.getMaturityDateCalendars() != null && leg.getMaturityDateBusinessDayConvention() != null) {
      final Calendar maturityDateCalendar = new HolidaySourceCalendarAdapter(holidaySource, leg.getMaturityDateCalendars().toArray(new ExternalId[0]));
      maturityDateParameters = new AdjustedDateParameters(maturityDateCalendar, leg.getMaturityDateBusinessDayConvention());
    }

    AdjustedDateParameters accrualPeriodParameters = null;
    if (leg.getAccrualPeriodCalendars() != null && leg.getAccrualPeriodBusinessDayConvention() != null) {
      final Calendar accrualPeriodCalendar = new HolidaySourceCalendarAdapter(holidaySource, leg.getAccrualPeriodCalendars().toArray(new ExternalId[0]));
      accrualPeriodParameters = new AdjustedDateParameters(accrualPeriodCalendar, leg.getAccrualPeriodBusinessDayConvention());
    }

    RollDateAdjuster rollDateAdjuster = null;
    if (leg.getRollConvention() == RollConvention.EOM) {
      rollDateAdjuster = EndOfMonthRollDateAdjuster.getAdjuster();
    } else {
      rollDateAdjuster = leg.getRollConvention().getRollDateAdjuster(0);
    }

    AdjustedDateParameters resetDateParameters = null;
    if (floatLeg.getResetPeriodCalendars() != null && floatLeg.getResetPeriodBusinessDayConvention() != null) {
      final Calendar resetCalendar = new HolidaySourceCalendarAdapter(holidaySource, floatLeg.getResetPeriodCalendars().toArray(new ExternalId[0]));
      resetDateParameters = new AdjustedDateParameters(resetCalendar, floatLeg.getResetPeriodBusinessDayConvention());
    }

    double spread = Double.NaN;
    if (floatLeg.getSpreadSchedule() != null) {
      spread = floatLeg.getSpreadSchedule().getInitialRate();
    }

    final int paymentOffset = floatLeg.getPaymentOffset();

    final IndexDeposit index;
    final Security sec = securitySource.getSingle(floatLeg.getFloatingReferenceRateId().toBundle());
    if (sec == null) {
      throw new OpenGammaRuntimeException("Failed to resolve security for " + floatLeg.getFloatingReferenceRateId().toBundle());
    }

    if (FloatingRateType.IBOR == floatLeg.getFloatingRateType()) {
      final com.opengamma.financial.security.index.IborIndex indexSecurity = (com.opengamma.financial.security.index.IborIndex) sec;
      final IborIndexConvention indexConvention = conventionSource.getSingle(indexSecurity.getConventionId(), IborIndexConvention.class);
      index = ConverterUtils.indexIbor(indexSecurity.getName(), indexConvention, indexSecurity.getTenor());

    } else if (FloatingRateType.OIS == floatLeg.getFloatingRateType()) {
      final OvernightIndex indexSecurity = (OvernightIndex) sec;
      final OvernightIndexConvention indexConvention = conventionSource.getSingle(indexSecurity.getConventionId(), OvernightIndexConvention.class);
      index = ConverterUtils.indexON(indexSecurity.getName(), indexConvention);
    } else {
      throw new OpenGammaRuntimeException("Unsupported floating rate type " + floatLeg.getFloatingRateType());
    }

    OffsetAdjustedDateParameters paymentDateParameters = null;
    if (leg.getPaymentDateCalendars() != null && leg.getPaymentDateBusinessDayConvention() != null) {
      final Calendar paymentDateCalendar = new HolidaySourceCalendarAdapter(holidaySource,
          leg.getPaymentDateCalendars().toArray(new ExternalId[leg.getPaymentDateCalendars().size()]));
      paymentDateParameters = new OffsetAdjustedDateParameters(paymentOffset, OffsetType.BUSINESS, paymentDateCalendar,
          leg.getPaymentDateBusinessDayConvention());
    }

    OffsetAdjustedDateParameters fixingDateParameters = null;
    if (floatLeg.getFixingDateCalendars() != null && floatLeg.getFixingDateBusinessDayConvention() != null) {
      final Calendar fixingDateCalendar = new HolidaySourceCalendarAdapter(holidaySource,
          floatLeg.getFixingDateCalendars().toArray(new ExternalId[floatLeg.getFixingDateCalendars().size()]));
      fixingDateParameters = new OffsetAdjustedDateParameters(floatLeg.getFixingDateOffset(), floatLeg.getFixingDateOffsetType(), fixingDateCalendar,
          floatLeg.getFixingDateBusinessDayConvention());
    }

    com.opengamma.analytics.financial.instrument.annuity.CompoundingMethod compoundingMethod;
    switch (floatLeg.getCompoundingMethod()) {
      case FLAT:
        compoundingMethod = com.opengamma.analytics.financial.instrument.annuity.CompoundingMethod.FLAT;
        break;
      case STRAIGHT:
        compoundingMethod = com.opengamma.analytics.financial.instrument.annuity.CompoundingMethod.STRAIGHT;
        break;
      case SPREAD_EXCLUSIVE:
        compoundingMethod = com.opengamma.analytics.financial.instrument.annuity.CompoundingMethod.SPREAD_EXCLUSIVE;
        break;
      case NONE:
        compoundingMethod = null;
        break;
      default:
        throw new OpenGammaRuntimeException("Unsupported compounding method");
    }

    CouponStub startStub = null;
    CouponStub endStub = null;
    final StubCalculationMethod stubCalcMethod = leg.getStubCalculationMethod();
    if (stubCalcMethod != null) {
      stubCalcMethod.validate();
      final Pair<CouponStub, CouponStub> stubs = parseStubs(leg.getStubCalculationMethod(), securitySource, conventionSource);
      startStub = stubs.getFirst();
      endStub = stubs.getSecond();
    }

    final List<Double> notionalList = leg.getNotional().getNotionals();
    double[] notionalSchedule;
    if (notionalList.isEmpty()) {
      notionalSchedule = new double[] { (payer ? -1 : 1) * leg.getNotional().getInitialAmount() };
    } else {
      notionalSchedule = new double[notionalList.size()];
      for (int i = 0; i < notionalSchedule.length; i++) {
        notionalSchedule[i] = (payer ? -1 : 1) * notionalList.get(i);
      }
    }

    return new FloatingAnnuityDefinitionBuilder().payer(payer).currency(leg.getNotional().getCurrency())
        .notional(getNotionalProvider(leg.getNotional(), leg.getAccrualPeriodBusinessDayConvention(),
            new HolidaySourceCalendarAdapter(holidaySource, leg.getAccrualPeriodCalendars().toArray(new ExternalId[leg.getAccrualPeriodCalendars().size()]))))
        .startDate(startDate).endDate(endDate).endDateAdjustmentParameters(maturityDateParameters).dayCount(leg.getDayCountConvention())
        .rollDateAdjuster(rollDateAdjuster).exchangeInitialNotional(notionalExchange.isExchangeInitialNotional())
        .exchangeFinalNotional(notionalExchange.isExchangeFinalNotional()).accrualPeriodFrequency(ConversionUtils.getTenor(leg.getPaymentDateFrequency()))
        .accrualPeriodParameters(accrualPeriodParameters).paymentDateRelativeTo(leg.getPaymentDateRelativeTo())
        .paymentDateAdjustmentParameters(paymentDateParameters).spread(spread).index(index).resetDateAdjustmentParameters(resetDateParameters)
        .resetRelativeTo(floatLeg.getResetDateRelativeTo()).fixingDateAdjustmentParameters(fixingDateParameters).compoundingMethod(compoundingMethod)
        .startStub(startStub).endStub(endStub).initialRate(floatLeg.getCustomRates() != null ? floatLeg.getCustomRates().getInitialRate() : Double.NaN).build();
  }

  /**
   * Builds a fixed annuity definition from an {@link InterestRateSwapLeg}.
   * 
   * @param securitySource
   *          The security source, not null
   * @param holidaySource
   *          The holiday source, not null
   * @param conventionSource
   *          The convention source, not null
   * @param payer
   *          True if the swap is a payer (i.e. pays a fixed rate)
   * @param startDate
   *          The start date, not null
   * @param endDate
   *          The end date, not null
   * @param notionalExchange
   *          The type of notional exchange, not null
   * @param leg
   *          The interest rate swap leg, not null
   * @return A fixed annuity definition. The coupons are not necessarily the same type.
   */
  public static AnnuityDefinition<?> buildFixedAnnuityDefinition(final SecuritySource securitySource, final HolidaySource holidaySource,
      final ConventionSource conventionSource, final boolean payer, final LocalDate startDate,
      final LocalDate endDate, final NotionalExchange notionalExchange, final InterestRateSwapLeg leg) {
    ArgumentChecker.notNull(securitySource, "securitySource");
    ArgumentChecker.notNull(holidaySource, "holidaySource");
    ArgumentChecker.notNull(conventionSource, "conventionSource");
    ArgumentChecker.notNull(startDate, "startDate");
    ArgumentChecker.notNull(endDate, "endDate");
    ArgumentChecker.notNull(notionalExchange, "notionalExchange");
    ArgumentChecker.notNull(leg, "leg");
    final FixedInterestRateSwapLeg fixedLeg = (FixedInterestRateSwapLeg) leg;

    AdjustedDateParameters maturityDateParameters = null;
    if (leg.getMaturityDateCalendars() != null && leg.getMaturityDateBusinessDayConvention() != null) {
      final Calendar maturityDateCalendar = new HolidaySourceCalendarAdapter(holidaySource,
          leg.getMaturityDateCalendars().toArray(new ExternalId[leg.getMaturityDateCalendars().size()]));
      maturityDateParameters = new AdjustedDateParameters(maturityDateCalendar, leg.getMaturityDateBusinessDayConvention());
    }

    AdjustedDateParameters accrualPeriodParameters = null;
    if (leg.getAccrualPeriodCalendars() != null && leg.getAccrualPeriodBusinessDayConvention() != null) {
      final Calendar accrualPeriodCalendar = new HolidaySourceCalendarAdapter(holidaySource,
          leg.getAccrualPeriodCalendars().toArray(new ExternalId[leg.getAccrualPeriodCalendars().size()]));
      accrualPeriodParameters = new AdjustedDateParameters(accrualPeriodCalendar, leg.getAccrualPeriodBusinessDayConvention());
    }

    OffsetAdjustedDateParameters paymentDateParameters = null;
    if (leg.getPaymentDateCalendars() != null && leg.getPaymentDateBusinessDayConvention() != null) {
      final Calendar paymentDateCalendar = new HolidaySourceCalendarAdapter(holidaySource,
          leg.getPaymentDateCalendars().toArray(new ExternalId[leg.getPaymentDateCalendars().size()]));
      paymentDateParameters = new OffsetAdjustedDateParameters(
          leg.getPaymentOffset(),
          OffsetType.BUSINESS,
          paymentDateCalendar,
          leg.getPaymentDateBusinessDayConvention());
    }

    RollDateAdjuster rollDateAdjuster = null;
    if (leg.getRollConvention() == RollConvention.EOM) {
      rollDateAdjuster = EndOfMonthRollDateAdjuster.getAdjuster();
    } else {
      rollDateAdjuster = leg.getRollConvention().getRollDateAdjuster(0);
    }

    CouponStub startStub = null;
    CouponStub endStub = null;
    final StubCalculationMethod stubCalcMethod = leg.getStubCalculationMethod();
    if (stubCalcMethod != null) {
      stubCalcMethod.validate();
      final Pair<CouponStub, CouponStub> stubs = parseStubs(leg.getStubCalculationMethod(), securitySource, conventionSource);
      startStub = stubs.getFirst();
      endStub = stubs.getSecond();
    }

    final List<Double> notionalList = leg.getNotional().getNotionals();
    double[] notionalSchedule;
    if (notionalList.isEmpty()) {
      notionalSchedule = new double[] { (payer ? -1 : 1) * leg.getNotional().getInitialAmount() };
    } else {
      notionalSchedule = new double[notionalList.size()];
      for (int i = 0; i < notionalSchedule.length; i++) {
        notionalSchedule[i] = (payer ? -1 : 1) * notionalList.get(i);
      }
    }

    return new FixedAnnuityDefinitionBuilder().payer(payer).currency(leg.getNotional().getCurrency())
        .notional(getNotionalProvider(leg.getNotional(), leg.getAccrualPeriodBusinessDayConvention(),
            new HolidaySourceCalendarAdapter(holidaySource, leg.getAccrualPeriodCalendars().toArray(new ExternalId[leg.getAccrualPeriodCalendars().size()]))))
        .startDate(startDate).endDate(endDate).endDateAdjustmentParameters(maturityDateParameters).dayCount(leg.getDayCountConvention())
        .rollDateAdjuster(rollDateAdjuster).exchangeInitialNotional(notionalExchange.isExchangeInitialNotional())
        .exchangeFinalNotional(notionalExchange.isExchangeFinalNotional()).accrualPeriodFrequency(ConversionUtils.getTenor(leg.getPaymentDateFrequency()))
        .accrualPeriodParameters(accrualPeriodParameters).paymentDateRelativeTo(leg.getPaymentDateRelativeTo())
        .paymentDateAdjustmentParameters(paymentDateParameters).rate(fixedLeg.getRate().getInitialRate()).startStub(startStub).endStub(endStub).build();
  }

  /**
   * Converts the {@link StubCalculationMethod} to a {@link CouponStub}.
   * 
   * @param stubCalcMethod
   *          The stub calculation method, not null
   * @param securitySource
   *          The security source to perform a lookup of indices, not null
   * @param conventionSource
   *          The convention source to perform a lookup of conventions for backwards compatibility, not null
   * @return A pair of front and back stubs
   */
  public static Pair<CouponStub, CouponStub> parseStubs(final StubCalculationMethod stubCalcMethod, final SecuritySource securitySource,
      final ConventionSource conventionSource) {

    // Walk the stub to determine the details; when done - build a CouponStub with those details
    CouponStub startStub = null;
    CouponStub endStub = null;

    if (stubCalcMethod != null) {
      stubCalcMethod.validate();
      final StubType stubType = stubCalcMethod.getType();

      // first stub
      final double firstStubRate = stubCalcMethod.hasFirstStubRate() ? stubCalcMethod.getFirstStubRate() : Double.NaN;
      final LocalDate firstStubDate = stubCalcMethod.getFirstStubEndDate();

      final ExternalId firstStubStartReferenceRateId = stubCalcMethod.getFirstStubStartReferenceRateId();
      IborIndex firstStubStartIndex = null;
      if (stubCalcMethod.hasFirstStubStartReferenceRateId()) {
        firstStubStartIndex = (IborIndex) getIborIndex(firstStubStartReferenceRateId, securitySource, conventionSource);
      }
      final ExternalId firstStubEndReferenceRateId = stubCalcMethod.getFirstStubEndReferenceRateId();
      IborIndex firstStubEndIndex = null;
      if (stubCalcMethod.hasFirstStubEndReferenceRateId()) {
        firstStubEndIndex = (IborIndex) getIborIndex(firstStubEndReferenceRateId, securitySource, conventionSource);
      }

      // last stub
      final double finalStubRate = stubCalcMethod.hasLastStubRate() ? stubCalcMethod.getLastStubRate() : Double.NaN;
      final LocalDate finalStubDate = stubCalcMethod.getLastStubEndDate();

      final ExternalId lastStubStartReferenceRateId = stubCalcMethod.getLastStubStartReferenceRateId();
      IborIndex lastStubStartIndex = null;
      if (stubCalcMethod.hasLastStubStartReferenceRateId()) {
        lastStubStartIndex = (IborIndex) getIborIndex(lastStubStartReferenceRateId, securitySource, conventionSource);
      }

      final ExternalId lastStubEndReferenceRateId = stubCalcMethod.getLastStubEndReferenceRateId();
      IborIndex lastStubEndIndex = null;
      if (stubCalcMethod.hasLastStubEndReferenceRateId()) {
        lastStubEndIndex = (IborIndex) getIborIndex(lastStubEndReferenceRateId, securitySource, conventionSource);
      }

      // Determine the stub type and build with any subtleties encountered
      if (StubType.BOTH == stubType) {
        if (!Double.isNaN(firstStubRate)) {
          startStub = new CouponStub(stubType, firstStubDate, stubCalcMethod.getFirstStubRate());
        } else if (firstStubStartIndex != null && firstStubEndIndex != null) {
          startStub = new CouponStub(stubType, firstStubDate, firstStubStartIndex, firstStubEndIndex);
        } else {
          startStub = new CouponStub(stubType, firstStubDate);
        }

        if (!Double.isNaN(finalStubRate)) {
          endStub = new CouponStub(stubType, finalStubDate, stubCalcMethod.getLastStubRate());
        } else if (lastStubStartIndex != null && lastStubEndIndex != null) {
          endStub = new CouponStub(stubType, finalStubDate, lastStubStartIndex, lastStubEndIndex);
        } else {
          endStub = new CouponStub(stubType, finalStubDate);
        }

      } else if (StubType.LONG_START == stubType || StubType.SHORT_START == stubType) {
        if (!Double.isNaN(firstStubRate)) {
          startStub = new CouponStub(stubType, firstStubDate, firstStubRate);
        } else if (firstStubStartIndex != null && firstStubEndIndex != null) {
          startStub = new CouponStub(stubType, firstStubStartIndex, firstStubEndIndex);
        } else {
          startStub = new CouponStub(stubType);
        }
      } else if (StubType.LONG_END == stubType || StubType.SHORT_END == stubType) {
        if (!Double.isNaN(finalStubRate)) {
          endStub = new CouponStub(stubType, finalStubDate, finalStubRate);
        } else if (lastStubStartIndex != null && lastStubEndIndex != null) {
          endStub = new CouponStub(stubType, lastStubStartIndex, lastStubEndIndex);
        } else {
          endStub = new CouponStub(stubType);
        }
      } else if (stubType != null) {
        startStub = new CouponStub(stubType);
        endStub = new CouponStub(stubType);
      }
    }
    return Pairs.of(startStub, endStub);
  }

  private static IndexDeposit getIborIndex(final ExternalId indexId, final SecuritySource securitySource, final ConventionSource conventionSource) {
    // try security lookup
    final Security sec = securitySource.getSingle(indexId.toBundle());
    if (sec != null) {
      final com.opengamma.financial.security.index.IborIndex indexSecurity = (com.opengamma.financial.security.index.IborIndex) sec;
      final IborIndexConvention indexConvention = conventionSource.getSingle(indexSecurity.getConventionId(), IborIndexConvention.class);
      return ConverterUtils.indexIbor(indexSecurity.getName(), indexConvention, indexSecurity.getTenor());
    }

    // Fallback to convention lookup for old behaviour
    final Convention iborLegConvention = conventionSource.getSingle(indexId);
    if (!(iborLegConvention instanceof VanillaIborLegConvention)) {
      throw new OpenGammaRuntimeException("Could not resolve an index convention for rate reference id: " + indexId.getValue());
    }
    final Convention iborConvention = conventionSource.getSingle(((VanillaIborLegConvention) iborLegConvention).getIborIndexConvention());
    if (iborConvention == null) {
      throw new OpenGammaRuntimeException("Convention not found for " + ((VanillaIborLegConvention) iborLegConvention).getIborIndexConvention());
    }
    final IborIndexConvention iborIndexConvention = (IborIndexConvention) iborConvention;

    return new IborIndex(iborIndexConvention.getCurrency(),
        ((VanillaIborLegConvention) iborLegConvention).getResetTenor().getPeriod(),
        iborIndexConvention.getSettlementDays(), // fixing lag
        iborIndexConvention.getDayCount(),
        iborIndexConvention.getBusinessDayConvention(),
        ((IborIndexConvention) iborConvention).isIsEOM(),
        indexId.getValue());
  }

  /**
   * Returns a notional provider that will supply absolute notionals (i.e. will convert relative or delta notionals to absolute form).
   * 
   * @param notional
   *          The notional, not null
   * @param convention
   *          The business day convention, not null
   * @param calendar
   *          The calendar, not null
   * @return A notional provider supplies absolute notionals
   */
  // TODO: Would be nice to make this support Notional
  public static NotionalProvider getNotionalProvider(final InterestRateSwapNotional notional, final BusinessDayConvention convention, final Calendar calendar) {
    final InterestRateSwapNotionalVisitor<LocalDate, Double> visitor = new InterestRateSwapNotionalAmountVisitor();
    final InterestRateSwapNotional adjustedNotional;
    final List<LocalDate> dates = notional.getDates();
    if (!dates.isEmpty()) {
      for (int i = 0; i < dates.size(); i++) {
        LocalDate date = dates.remove(i);
        date = convention.adjustDate(calendar, date);
        dates.add(i, date);
      }
      adjustedNotional = InterestRateSwapNotional.of(notional.getCurrency(), dates, notional.getNotionals(), notional.getShiftTypes());
    } else {
      adjustedNotional = notional;
    }
    return new NotionalProvider() {
      @Override
      public double getAmount(final LocalDate date) {
        return adjustedNotional.accept(visitor, date);
      }
    };
  }

}
