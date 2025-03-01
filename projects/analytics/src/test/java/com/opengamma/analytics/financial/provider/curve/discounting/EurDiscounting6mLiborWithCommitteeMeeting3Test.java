/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.curve.discounting;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.ZonedDateTime;

import com.mcleodmoores.analytics.financial.curve.CurveUtils;
import com.mcleodmoores.analytics.financial.index.Index;
import com.mcleodmoores.date.WeekendWorkingDayCalendar;
import com.mcleodmoores.date.WorkingDayCalendar;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveAddYield;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveAddYieldExisting;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveAddYieldFixed;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveDiscountFactorInterpolatedAnchorNode;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveDiscountFactorInterpolatedNumber;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveYieldInterpolated;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorCurveYieldInterpolatedAnchor;
import com.opengamma.analytics.financial.curve.interestrate.generator.GeneratorYDCurve;
import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.instrument.InstrumentDefinition;
import com.opengamma.analytics.financial.instrument.index.GeneratorAttribute;
import com.opengamma.analytics.financial.instrument.index.GeneratorAttributeIR;
import com.opengamma.analytics.financial.instrument.index.GeneratorDepositIbor;
import com.opengamma.analytics.financial.instrument.index.GeneratorDepositON;
import com.opengamma.analytics.financial.instrument.index.GeneratorFRA;
import com.opengamma.analytics.financial.instrument.index.GeneratorInstrument;
import com.opengamma.analytics.financial.instrument.index.GeneratorSwapFixedIbor;
import com.opengamma.analytics.financial.instrument.index.GeneratorSwapFixedIborMaster;
import com.opengamma.analytics.financial.instrument.index.GeneratorSwapFixedON;
import com.opengamma.analytics.financial.instrument.index.GeneratorSwapFixedONMaster;
import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.instrument.index.IndexON;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.model.interestrate.curve.DiscountCurve;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.analytics.financial.provider.calculator.discounting.ParSpreadMarketQuoteCurveSensitivityDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.discounting.ParSpreadMarketQuoteDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.generic.LastTimeCalculator;
import com.opengamma.analytics.financial.provider.curve.CurveBuildingBlockBundle;
import com.opengamma.analytics.financial.provider.curve.MultiCurveBundle;
import com.opengamma.analytics.financial.provider.curve.SingleCurveBundle;
import com.opengamma.analytics.financial.provider.curve.multicurve.MulticurveDiscountBuildingRepository;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderDiscount;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MulticurveSensitivity;
import com.opengamma.analytics.financial.schedule.ScheduleCalculator;
import com.opengamma.analytics.math.curve.InterpolatedDoublesCurve;
import com.opengamma.analytics.math.interpolation.Interpolator1D;
import com.opengamma.analytics.math.interpolation.factory.DoubleQuadraticInterpolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.ExponentialExtrapolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.FlatExtrapolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.LinearInterpolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.LogLinearInterpolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.NamedInterpolator1dFactory;
import com.opengamma.analytics.util.time.TimeCalculator;
import com.opengamma.timeseries.precise.zdt.ImmutableZonedDateTimeDoubleTimeSeries;
import com.opengamma.timeseries.precise.zdt.ZonedDateTimeDoubleTimeSeries;
import com.opengamma.util.money.Currency;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.time.DateUtils;
import com.opengamma.util.tuple.Pair;

/**
 * Build of curve in several blocks with relevant Jacobian matrices. Two curves in EUR; no futures; EONIA curve with ECB meeting dates.
 */
@Test(groups = TestGroup.UNIT)
public class EurDiscounting6mLiborWithCommitteeMeeting3Test {

  private static final Interpolator1D INTERPOLATOR_LINEAR = NamedInterpolator1dFactory.of(LinearInterpolator1dAdapter.NAME,
      FlatExtrapolator1dAdapter.NAME, FlatExtrapolator1dAdapter.NAME);
  // Log-linear on the discount factor = step on the instantaneous rates
  private static final Interpolator1D INTERPOLATOR_LL = NamedInterpolator1dFactory.of(LogLinearInterpolator1dAdapter.NAME,
      ExponentialExtrapolator1dAdapter.NAME, ExponentialExtrapolator1dAdapter.NAME);
  private static final Interpolator1D INTERPOLATOR_DQ = NamedInterpolator1dFactory.of(DoubleQuadraticInterpolator1dAdapter.NAME,
      FlatExtrapolator1dAdapter.NAME, FlatExtrapolator1dAdapter.NAME);

  private static final LastTimeCalculator MATURITY_CALCULATOR = LastTimeCalculator.getInstance();
  private static final double TOLERANCE_ROOT = 1.0E-10;
  private static final int STEP_MAX = 100;

  private static final WorkingDayCalendar TARGET = WeekendWorkingDayCalendar.SATURDAY_SUNDAY;
  private static final Currency EUR = Currency.EUR;
  private static final FXMatrix FX_MATRIX = new FXMatrix(EUR);

  private static final double NOTIONAL = 1.0;

  private static final GeneratorSwapFixedON GENERATOR_OIS_EUR = GeneratorSwapFixedONMaster.getInstance().getGenerator("EUR1YEONIA", TARGET);
  /** An EONIA index */
  private static final IndexON EONIA_INDEX = GENERATOR_OIS_EUR.getIndex();
  private static final GeneratorDepositON GENERATOR_DEPOSIT_ON_EUR = new GeneratorDepositON("EUR Deposit ON", EUR, TARGET,
      EONIA_INDEX.getDayCount());
  private static final GeneratorSwapFixedIborMaster GENERATOR_SWAP_MASTER = GeneratorSwapFixedIborMaster.getInstance();
  private static final GeneratorSwapFixedIbor EUR1YEURIBOR6M = GENERATOR_SWAP_MASTER.getGenerator("EUR1YEURIBOR6M", TARGET);
  /** A 6M EURIBOR index */
  private static final IborIndex EURIBOR_6M_INDEX = EUR1YEURIBOR6M.getIborIndex();
  /** A 6M EUROLIBOR index */
  private static final IborIndex EUROLIBOR_6M_INDEX = new IborIndex(EUR, Period.ofMonths(6), 2, EURIBOR_6M_INDEX.getDayCount(),
      EURIBOR_6M_INDEX.getBusinessDayConvention(), true, "EUROLIBOR6M");
  private static final GeneratorFRA GENERATOR_FRA_6M = new GeneratorFRA("GENERATOR_FRA_6M", EURIBOR_6M_INDEX, TARGET);
  private static final GeneratorDepositIbor GENERATOR_EURIBOR6M = new GeneratorDepositIbor("GENERATOR_EURIBOR6M", EURIBOR_6M_INDEX, TARGET);

  private static final ZonedDateTime NOW = DateUtils.getUTCDate(2012, 11, 14);
  private static final ZonedDateTime[] MEETING_ECB_DATE = new ZonedDateTime[] { DateUtils.getUTCDate(2012, 12, 6),
      DateUtils.getUTCDate(2013, 1, 10), DateUtils.getUTCDate(2013, 2, 7),
      DateUtils.getUTCDate(2013, 3, 7), DateUtils.getUTCDate(2013, 4, 4), DateUtils.getUTCDate(2013, 5, 2),
      DateUtils.getUTCDate(2013, 6, 6), DateUtils.getUTCDate(2013, 7, 4),
      DateUtils.getUTCDate(2013, 8, 1), DateUtils.getUTCDate(2013, 9, 5), DateUtils.getUTCDate(2013, 10, 2),
      DateUtils.getUTCDate(2013, 11, 7) };
  private static final double[] MEETING_ECB_TIME = new double[MEETING_ECB_DATE.length];
  static {
    for (int loopdate = 0; loopdate < MEETING_ECB_DATE.length; loopdate++) {
      MEETING_ECB_TIME[loopdate] = TimeCalculator.getTimeBetween(NOW, MEETING_ECB_DATE[loopdate]);
    }
  }

  private static final ZonedDateTimeDoubleTimeSeries TS_ON_EUR_WITH_TODAY = ImmutableZonedDateTimeDoubleTimeSeries
      .ofUTC(new ZonedDateTime[] { DateUtils.getUTCDate(2011, 9, 27),
          DateUtils.getUTCDate(2011, 9, 28) }, new double[] { 0.07, 0.08 });
  private static final ZonedDateTimeDoubleTimeSeries TS_ON_EUR_WITHOUT_TODAY = ImmutableZonedDateTimeDoubleTimeSeries
      .ofUTC(new ZonedDateTime[] { DateUtils.getUTCDate(2011, 9, 27),
          DateUtils.getUTCDate(2011, 9, 28) }, new double[] { 0.07, 0.08 });

  private static final ZonedDateTimeDoubleTimeSeries TS_IBOR_EUR6M_WITH_TODAY = ImmutableZonedDateTimeDoubleTimeSeries
      .ofUTC(new ZonedDateTime[] { DateUtils.getUTCDate(2011, 9, 27),
          DateUtils.getUTCDate(2011, 9, 28) }, new double[] { 0.0035, 0.0036 });
  private static final ZonedDateTimeDoubleTimeSeries TS_IBOR_EUR6M_WITHOUT_TODAY = ImmutableZonedDateTimeDoubleTimeSeries.ofUTC(
      new ZonedDateTime[] { DateUtils.getUTCDate(2011, 9, 27) },
      new double[] { 0.0035 });

  /** Fixing time series created before the valuation date fixing is available */
  private static final Map<Index, ZonedDateTimeDoubleTimeSeries> FIXING_TS_WITHOUT_TODAY = new HashMap<>();
  /** Fixing time series created after the valuation date fixing is available */
  private static final Map<Index, ZonedDateTimeDoubleTimeSeries> FIXING_TS_WITH_TODAY = new HashMap<>();
  static {
    FIXING_TS_WITHOUT_TODAY.put(EONIA_INDEX, TS_ON_EUR_WITHOUT_TODAY);
    FIXING_TS_WITHOUT_TODAY.put(EUROLIBOR_6M_INDEX, TS_IBOR_EUR6M_WITHOUT_TODAY);
    FIXING_TS_WITHOUT_TODAY.put(EURIBOR_6M_INDEX, TS_IBOR_EUR6M_WITHOUT_TODAY);
    FIXING_TS_WITH_TODAY.put(EONIA_INDEX, TS_ON_EUR_WITH_TODAY);
    FIXING_TS_WITH_TODAY.put(EUROLIBOR_6M_INDEX, TS_IBOR_EUR6M_WITH_TODAY);
    FIXING_TS_WITH_TODAY.put(EURIBOR_6M_INDEX, TS_IBOR_EUR6M_WITH_TODAY);
  }

  private static final String CURVE_NAME_DSC_EUR = "EUR Dsc";
  private static final String CURVE_NAME_FWD6_EUR = "EUR Fwd 6M";

  /** Market values for the dsc USD curve */
  private static final double[] DSC_EUR_MARKET_QUOTES = new double[] { 0.0050, 0.0050, 0.0050, 0.0051, 0.0051, 0.0051, 0.0054, 0.0062,
      0.0069, 0.0071, 0.0072, 0.0070, 0.0074, 0.0076, 0.0100, 0.0110,
      0.0120, 0.0110, 0.0150 };
  /** Generators for the dsc USD curve */
  private static final GeneratorInstrument<? extends GeneratorAttribute>[] DSC_EUR_GENERATORS = new GeneratorInstrument<?>[] {
      GENERATOR_DEPOSIT_ON_EUR, GENERATOR_DEPOSIT_ON_EUR, GENERATOR_OIS_EUR,
      GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR,
      GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR,
      GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR, GENERATOR_OIS_EUR };
  /** Tenors for the dsc USD curve */
  private static final Period[] DSC_EUR_TENOR = new Period[] { Period.ofDays(0), Period.ofDays(1), Period.ofMonths(1), Period.ofMonths(2),
      Period.ofMonths(3), Period.ofMonths(4), Period.ofMonths(5),
      Period.ofMonths(6), Period.ofMonths(7), Period.ofMonths(8), Period.ofMonths(9), Period.ofMonths(10), Period.ofMonths(11),
      Period.ofYears(1), Period.ofYears(2), Period.ofYears(3),
      Period.ofYears(4), Period.ofYears(5), Period.ofYears(10) };
  private static final GeneratorAttributeIR[] DSC_EUR_ATTR = new GeneratorAttributeIR[DSC_EUR_TENOR.length];
  static {
    for (int i = 0; i < 2; i++) {
      DSC_EUR_ATTR[i] = new GeneratorAttributeIR(DSC_EUR_TENOR[i], Period.ZERO);
    }
    for (int i = 2; i < DSC_EUR_TENOR.length; i++) {
      DSC_EUR_ATTR[i] = new GeneratorAttributeIR(DSC_EUR_TENOR[i]);
    }
  }

  /** Market values for the Fwd 3M USD curve */
  private static final double[] FWD6_EUR_MARKET_QUOTES = new double[] { 0.0150, 0.0150, 0.0150, 0.0150, 0.0180, 0.0175, 0.0200, 0.0175 };
  /** Generators for the Fwd 3M USD curve */
  private static final GeneratorInstrument<? extends GeneratorAttribute>[] FWD6_EUR_GENERATORS = new GeneratorInstrument<?>[] {
      GENERATOR_EURIBOR6M, GENERATOR_FRA_6M, GENERATOR_FRA_6M, EUR1YEURIBOR6M,
      EUR1YEURIBOR6M, EUR1YEURIBOR6M, EUR1YEURIBOR6M, EUR1YEURIBOR6M };
  /** Tenors for the Fwd 3M USD curve */
  private static final Period[] FWD6_EUR_TENOR = new Period[] { Period.ofMonths(0), Period.ofMonths(9), Period.ofMonths(12),
      Period.ofYears(2), Period.ofYears(3), Period.ofYears(5), Period.ofYears(7),
      Period.ofYears(10) };
  private static final GeneratorAttributeIR[] FWD6_EUR_ATTR = new GeneratorAttributeIR[FWD6_EUR_TENOR.length];
  static {
    for (int i = 0; i < FWD6_EUR_TENOR.length; i++) {
      FWD6_EUR_ATTR[i] = new GeneratorAttributeIR(FWD6_EUR_TENOR[i]);
    }
  }

  /** Standard USD discounting curve instrument definitions */
  private static final InstrumentDefinition<?>[] DEFINITIONS_DSC_EUR;
  /** Standard USD Forward 3M curve instrument definitions */
  private static final InstrumentDefinition<?>[] DEFINITIONS_FWD6_EUR;

  /** Units of curves */
  private static final int[] NB_UNITS = new int[] { 2, 2 };
  private static final int NB_BLOCKS = NB_UNITS.length;
  private static final InstrumentDefinition<?>[][][][] DEFINITIONS_UNITS = new InstrumentDefinition<?>[NB_BLOCKS][][][];
  private static final GeneratorYDCurve[][][] GENERATORS_UNITS = new GeneratorYDCurve[NB_BLOCKS][][];
  private static final String[][][] NAMES_UNITS = new String[NB_BLOCKS][][];

  private static final MulticurveProviderDiscount MULTICURVE_KNOWN_DATA = new MulticurveProviderDiscount(FX_MATRIX);

  private static final LinkedHashMap<String, Currency> DSC_MAP = new LinkedHashMap<>();
  private static final LinkedHashMap<String, IndexON[]> FWD_ON_MAP = new LinkedHashMap<>();
  private static final LinkedHashMap<String, IborIndex[]> FWD_IBOR_MAP = new LinkedHashMap<>();

  static {
    DEFINITIONS_DSC_EUR = getDefinitions(DSC_EUR_MARKET_QUOTES, DSC_EUR_GENERATORS, DSC_EUR_ATTR);
    DEFINITIONS_FWD6_EUR = getDefinitions(FWD6_EUR_MARKET_QUOTES, FWD6_EUR_GENERATORS, FWD6_EUR_ATTR);
    for (int i = 0; i < NB_BLOCKS; i++) {
      DEFINITIONS_UNITS[i] = new InstrumentDefinition<?>[NB_UNITS[i]][][];
      GENERATORS_UNITS[i] = new GeneratorYDCurve[NB_UNITS[i]][];
      NAMES_UNITS[i] = new String[NB_UNITS[i]][];
    }
    DEFINITIONS_UNITS[0][0] = new InstrumentDefinition<?>[][] { DEFINITIONS_DSC_EUR };
    DEFINITIONS_UNITS[0][1] = new InstrumentDefinition<?>[][] { DEFINITIONS_FWD6_EUR };
    DEFINITIONS_UNITS[1][0] = new InstrumentDefinition<?>[][] { DEFINITIONS_DSC_EUR };
    DEFINITIONS_UNITS[1][1] = new InstrumentDefinition<?>[][] { DEFINITIONS_FWD6_EUR };
    final int nbNode1 = 2;
    final GeneratorYDCurve genIntLin = new GeneratorCurveYieldInterpolated(MATURITY_CALCULATOR, INTERPOLATOR_LINEAR);
    final GeneratorYDCurve genIntNumDFLL = new GeneratorCurveDiscountFactorInterpolatedNumber(MATURITY_CALCULATOR, nbNode1,
        INTERPOLATOR_LL);
    final GeneratorYDCurve genInt0DFLL = new GeneratorCurveDiscountFactorInterpolatedAnchorNode(MEETING_ECB_TIME,
        TimeCalculator.getTimeBetween(NOW,
            ScheduleCalculator.getAdjustedDate(NOW, GENERATOR_OIS_EUR.getSpotLag(), TARGET)),
        INTERPOLATOR_LL);
    final GeneratorYDCurve genInt0DQ = new GeneratorCurveYieldInterpolatedAnchor(MATURITY_CALCULATOR, INTERPOLATOR_DQ);
    final GeneratorYDCurve[] genCompArray = new GeneratorYDCurve[] { genIntNumDFLL, genInt0DFLL, genInt0DQ };
    final GeneratorYDCurve genComp = new GeneratorCurveAddYield(genCompArray, false);

    final LocalDate startTOY = LocalDate.of(2012, 12, 31);
    final LocalDate endTOY = LocalDate.of(2013, 1, 2);
    final double spreadTOY = 0.0030; // 30bps
    final double dfTOY = 1.0 / (1 + EONIA_INDEX.getDayCount().getDayCountFraction(startTOY, endTOY) * spreadTOY);
    final double[] times = { TimeCalculator.getTimeBetween(NOW, startTOY), TimeCalculator.getTimeBetween(NOW, endTOY) };
    final double[] df = { 1.0, dfTOY };
    final YieldAndDiscountCurve curveTOY = new DiscountCurve("TOY", new InterpolatedDoublesCurve(times, df, INTERPOLATOR_LINEAR, true));
    final GeneratorYDCurve genAddFixed = new GeneratorCurveAddYieldFixed(genComp, false, curveTOY);

    final GeneratorYDCurve genIntDQ = new GeneratorCurveYieldInterpolated(MATURITY_CALCULATOR, INTERPOLATOR_LINEAR);
    final GeneratorYDCurve genAddExistDsc = new GeneratorCurveAddYieldExisting(genIntDQ, false, CURVE_NAME_DSC_EUR);

    GENERATORS_UNITS[0][0] = new GeneratorYDCurve[] { genComp };
    GENERATORS_UNITS[0][1] = new GeneratorYDCurve[] { genIntLin };
    GENERATORS_UNITS[1][0] = new GeneratorYDCurve[] { genAddFixed };
    GENERATORS_UNITS[1][1] = new GeneratorYDCurve[] { genAddExistDsc };
    NAMES_UNITS[0][0] = new String[] { CURVE_NAME_DSC_EUR };
    NAMES_UNITS[0][1] = new String[] { CURVE_NAME_FWD6_EUR };
    NAMES_UNITS[1][0] = new String[] { CURVE_NAME_DSC_EUR };
    NAMES_UNITS[1][1] = new String[] { CURVE_NAME_FWD6_EUR };
    DSC_MAP.put(CURVE_NAME_DSC_EUR, EUR);
    FWD_ON_MAP.put(CURVE_NAME_DSC_EUR, new IndexON[] { EONIA_INDEX });
    FWD_IBOR_MAP.put(CURVE_NAME_FWD6_EUR, new IborIndex[] { EURIBOR_6M_INDEX, EUROLIBOR_6M_INDEX });
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static InstrumentDefinition<?>[] getDefinitions(final double[] marketQuotes, final GeneratorInstrument[] generators,
      final GeneratorAttribute[] attribute) {
    final InstrumentDefinition<?>[] definitions = new InstrumentDefinition<?>[marketQuotes.length];
    for (int i = 0; i < marketQuotes.length; i++) {
      definitions[i] = generators[i].generateInstrument(NOW, marketQuotes[i], NOTIONAL, attribute[i]);
    }
    return definitions;
  }

  private static final List<Pair<MulticurveProviderDiscount, CurveBuildingBlockBundle>> CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK = new ArrayList<>();

  // Calculator
  private static final PresentValueDiscountingCalculator PVDC = PresentValueDiscountingCalculator.getInstance();
  private static final ParSpreadMarketQuoteDiscountingCalculator PSMQDC = ParSpreadMarketQuoteDiscountingCalculator.getInstance();
  private static final ParSpreadMarketQuoteCurveSensitivityDiscountingCalculator PSMQCSDC = ParSpreadMarketQuoteCurveSensitivityDiscountingCalculator
      .getInstance();

  private static final MulticurveDiscountBuildingRepository CURVE_BUILDING_REPOSITORY = new MulticurveDiscountBuildingRepository(
      TOLERANCE_ROOT, TOLERANCE_ROOT, STEP_MAX);

  private static final double TOLERANCE_CAL = 1.0E-9;

  /**
   *
   */
  @BeforeSuite
  static void initClass() {
    for (int i = 0; i < NB_BLOCKS; i++) {
      CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK
          .add(makeCurvesFromDefinitions(DEFINITIONS_UNITS[i], GENERATORS_UNITS[i], NAMES_UNITS[i], MULTICURVE_KNOWN_DATA, PSMQDC,
              PSMQCSDC, false));
    }
  }

  /**
   *
   */
  @Test
  public void curveConstruction() {
    for (int i = 0; i < NB_BLOCKS; i++) {
      curveConstructionTest(DEFINITIONS_UNITS[i], CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.get(i).getFirst(), false, i);
    }
    assertEquals("Curve construction", CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.get(0).getFirst().getCurve(EURIBOR_6M_INDEX),
        CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.get(0).getFirst().getCurve(EUROLIBOR_6M_INDEX));
  }

  /**
   *
   */
  @Test(enabled = false)
  public void comparison1Unit3Units() {
    final MulticurveProviderDiscount[] units = new MulticurveProviderDiscount[2];
    final CurveBuildingBlockBundle[] bb = new CurveBuildingBlockBundle[2];
    final YieldAndDiscountCurve[] curveDsc = new YieldAndDiscountCurve[2];
    final YieldAndDiscountCurve[] curveFwd3 = new YieldAndDiscountCurve[2];
    final YieldAndDiscountCurve[] curveFwd6 = new YieldAndDiscountCurve[2];
    for (int i = 0; i < 2; i++) {
      units[i] = CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.get(i).getFirst();
      bb[i] = CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.get(i).getSecond();
      curveDsc[i] = units[i].getCurve(EUR);
      curveFwd6[i] = units[i].getCurve(EURIBOR_6M_INDEX);
    }
    assertEquals("Curve construction: 1 unit / 3 units ", curveDsc[0].getNumberOfParameters(), curveDsc[1].getNumberOfParameters());
    assertEquals("Curve construction: 1 unit / 3 units ", curveFwd3[0].getNumberOfParameters(), curveFwd3[1].getNumberOfParameters());
    assertEquals("Curve construction: 1 unit / 3 units ", curveFwd6[0].getNumberOfParameters(), curveFwd6[1].getNumberOfParameters());
    assertArrayEquals("Curve construction: 1 unit / 3 units ", ArrayUtils.toPrimitive(((YieldCurve) curveDsc[0]).getCurve().getXData()),
        ArrayUtils.toPrimitive(((YieldCurve) curveDsc[1]).getCurve().getXData()), TOLERANCE_CAL);
    assertArrayEquals("Curve construction: 1 unit / 3 units ", ArrayUtils.toPrimitive(((YieldCurve) curveDsc[0]).getCurve().getYData()),
        ArrayUtils.toPrimitive(((YieldCurve) curveDsc[1]).getCurve().getYData()), TOLERANCE_CAL);
    assertArrayEquals("Curve construction: 1 unit / 3 units ", ArrayUtils.toPrimitive(((YieldCurve) curveFwd3[0]).getCurve().getXData()),
        ArrayUtils.toPrimitive(((YieldCurve) curveFwd3[1]).getCurve().getXData()), TOLERANCE_CAL);
    assertArrayEquals("Curve construction: 1 unit / 3 units ", ArrayUtils.toPrimitive(((YieldCurve) curveFwd3[0]).getCurve().getYData()),
        ArrayUtils.toPrimitive(((YieldCurve) curveFwd3[1]).getCurve().getYData()), TOLERANCE_CAL);
    assertArrayEquals("Curve construction: 1 unit / 3 units ", ArrayUtils.toPrimitive(((YieldCurve) curveFwd6[0]).getCurve().getXData()),
        ArrayUtils.toPrimitive(((YieldCurve) curveFwd6[1]).getCurve().getXData()), TOLERANCE_CAL);
    assertArrayEquals("Curve construction: 1 unit / 3 units ", ArrayUtils.toPrimitive(((YieldCurve) curveFwd6[0]).getCurve().getYData()),
        ArrayUtils.toPrimitive(((YieldCurve) curveFwd6[1]).getCurve().getYData()), TOLERANCE_CAL);

    assertEquals("Curve construction: 1 unit / 3 units ", bb[0].getBlock(CURVE_NAME_FWD6_EUR).getFirst(),
        bb[1].getBlock(CURVE_NAME_FWD6_EUR).getFirst());
  }

  // TODO: test on the correctness of the Jacobian matrix in the CurveBuildingBlock's.

  /**
   *
   */
  @Test(enabled = false)
  public void performance() {
    long startTime, endTime;
    final int nbTest = 100;

    startTime = System.currentTimeMillis();
    for (int i = 0; i < nbTest; i++) {
      makeCurvesFromDefinitions(DEFINITIONS_UNITS[0], GENERATORS_UNITS[0], NAMES_UNITS[0], MULTICURVE_KNOWN_DATA, PSMQDC, PSMQCSDC, false);
    }
    endTime = System.currentTimeMillis();
    System.out.println(nbTest + " curve construction / 3 units: " + (endTime - startTime) + " ms");
    // Performance note: Curve construction 1 units: 07-Jan-2013: On Mac Pro 3.2 GHz Quad-Core Intel Xeon: 685 ms for 100 sets.

    startTime = System.currentTimeMillis();
    for (int i = 0; i < nbTest; i++) {
      makeCurvesFromDefinitions(DEFINITIONS_UNITS[1], GENERATORS_UNITS[1], NAMES_UNITS[1], MULTICURVE_KNOWN_DATA, PSMQDC, PSMQCSDC, false);
    }
    endTime = System.currentTimeMillis();
    System.out.println(nbTest + " curve construction / 1 unit: " + (endTime - startTime) + " ms");
    // Performance note: Curve construction 1 unit: 07-Jan-2013: On Mac Pro 3.2 GHz Quad-Core Intel Xeon: 740 ms for 100 sets.

  }

  private void curveConstructionTest(final InstrumentDefinition<?>[][][] definitions, final MulticurveProviderDiscount curves,
      final boolean withToday, final int block) {
    final int nbBlocks = definitions.length;
    for (int i = 0; i < nbBlocks; i++) {
      final InstrumentDerivative[][] instruments = CurveUtils.convert(definitions[i],
          withToday ? FIXING_TS_WITH_TODAY : FIXING_TS_WITHOUT_TODAY, NOW);
      final double[][] pv = new double[instruments.length][];
      for (int j = 0; j < instruments.length; j++) {
        pv[j] = new double[instruments[j].length];
        for (int k = 0; k < instruments[j].length; k++) {
          pv[j][k] = curves.getFxRates().convert(instruments[j][k].accept(PVDC, curves), EUR).getAmount();
          assertEquals("Curve construction: block " + block + ", unit " + i + " - instrument " + k, 0, pv[j][k], TOLERANCE_CAL);
        }
      }
    }
  }

  /**
   * Analyzes the shape of the pseudo-on forward rates for the EURIBOR6M forward curve.
   */
  @Test(enabled = false)
  public void forwardAnalysisON() {
    final MulticurveProviderInterface multicurve = CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.get(1).getFirst();
    final int jump = 1;
    final int nbDate = 500;
    ZonedDateTime startDate = NOW;
    final double[] rateDsc = new double[nbDate];
    final double[] startTime = new double[nbDate];
    try (final FileWriter writer = new FileWriter("dsc-committee.csv")) {
      for (int i = 0; i < nbDate; i++) {
        startTime[i] = TimeCalculator.getTimeBetween(NOW, startDate);
        final ZonedDateTime endDate = ScheduleCalculator.getAdjustedDate(startDate, 1, TARGET);
        final double endTime = TimeCalculator.getTimeBetween(NOW, endDate);
        final double accrualFactor = EONIA_INDEX.getDayCount().getDayCountFraction(startDate, endDate);
        rateDsc[i] = multicurve.getSimplyCompoundForwardRate(EONIA_INDEX, startTime[i], endTime, accrualFactor); // EONIA curve
        startDate = ScheduleCalculator.getAdjustedDate(startDate, jump, TARGET);
        writer.append(0.0 + "," + startTime[i] + "," + rateDsc[i] + "\n");
      }
      writer.flush();
      writer.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Analyzes the shape of the forward rate curve for EURIBOR6M forward curve.
   */
  @Test(enabled = false)
  public void forwardAnalysisTenor() {
    final MulticurveProviderInterface multicurve = CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.get(1).getFirst();
    final int jump = 1;
    final int nbDate = 750;
    ZonedDateTime startDate = NOW;
    final double[] rateDsc = new double[nbDate];
    final double[] startTime = new double[nbDate];
    try (final FileWriter writer = new FileWriter("dsc-committee.csv")) {
      for (int i = 0; i < nbDate; i++) {
        startTime[i] = TimeCalculator.getTimeBetween(NOW, startDate);
        final ZonedDateTime endDate = ScheduleCalculator.getAdjustedDate(startDate, EURIBOR_6M_INDEX, TARGET);
        final double endTime = TimeCalculator.getTimeBetween(NOW, endDate);
        final double accrualFactor = EURIBOR_6M_INDEX.getDayCount().getDayCountFraction(startDate, endDate);
        rateDsc[i] = multicurve.getSimplyCompoundForwardRate(EURIBOR_6M_INDEX, startTime[i], endTime, accrualFactor);
        startDate = ScheduleCalculator.getAdjustedDate(startDate, jump, TARGET);
        writer.append(0.0 + "," + startTime[i] + "," + rateDsc[i] + "\n");
      }
      writer.flush();
      writer.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  private static Pair<MulticurveProviderDiscount, CurveBuildingBlockBundle> makeCurvesFromDefinitions(
      final InstrumentDefinition<?>[][][] definitions, final GeneratorYDCurve[][] curveGenerators,
      final String[][] curveNames, final MulticurveProviderDiscount knownData,
      final InstrumentDerivativeVisitor<MulticurveProviderInterface, Double> calculator,
      final InstrumentDerivativeVisitor<MulticurveProviderInterface, MulticurveSensitivity> sensitivityCalculator,
      final boolean withToday) {
    final int nUnits = definitions.length;
    final MultiCurveBundle<GeneratorYDCurve>[] curveBundles = new MultiCurveBundle[nUnits];
    for (int i = 0; i < nUnits; i++) {
      final int nCurves = definitions[i].length;
      final SingleCurveBundle<GeneratorYDCurve>[] singleCurves = new SingleCurveBundle[nCurves];
      for (int j = 0; j < nCurves; j++) {
        final int nInstruments = definitions[i][j].length;
        final InstrumentDerivative[] derivatives = new InstrumentDerivative[nInstruments];
        final double[] rates = new double[nInstruments];
        for (int k = 0; k < nInstruments; k++) {
          derivatives[k] = CurveUtils.convert(definitions[i][j][k], withToday ? FIXING_TS_WITH_TODAY : FIXING_TS_WITHOUT_TODAY, NOW);
          rates[k] = derivatives[k].accept(CurveUtils.RATES_INITIALIZATION);
        }
        final GeneratorYDCurve generator = curveGenerators[i][j].finalGenerator(derivatives);
        final double[] initialGuess = generator.initialGuess(rates);
        singleCurves[j] = new SingleCurveBundle<>(curveNames[i][j], derivatives, initialGuess, generator);
      }
      curveBundles[i] = new MultiCurveBundle<>(singleCurves);
    }
    return CURVE_BUILDING_REPOSITORY.makeCurvesFromDerivatives(curveBundles, knownData, DSC_MAP, FWD_IBOR_MAP, FWD_ON_MAP, calculator,
        sensitivityCalculator);
  }

}
