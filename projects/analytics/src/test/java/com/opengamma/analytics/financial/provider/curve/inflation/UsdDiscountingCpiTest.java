/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.curve.inflation;

import static org.testng.AssertJUnit.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.threeten.bp.Period;
import org.threeten.bp.ZonedDateTime;

import com.mcleodmoores.analytics.financial.curve.CurveUtils;
import com.opengamma.analytics.financial.curve.inflation.generator.GeneratorPriceIndexCurve;
import com.opengamma.analytics.financial.curve.inflation.generator.GeneratorPriceIndexCurveInterpolated;
import com.opengamma.analytics.financial.instrument.InstrumentDefinition;
import com.opengamma.analytics.financial.instrument.index.GeneratorAttribute;
import com.opengamma.analytics.financial.instrument.index.GeneratorAttributeIR;
import com.opengamma.analytics.financial.instrument.index.GeneratorInstrument;
import com.opengamma.analytics.financial.instrument.index.GeneratorSwapFixedInflationMaster;
import com.opengamma.analytics.financial.instrument.index.GeneratorSwapFixedInflationZeroCoupon;
import com.opengamma.analytics.financial.instrument.index.IndexPrice;
import com.opengamma.analytics.financial.instrument.swap.SwapFixedInflationZeroCouponDefinition;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.Annuity;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.financial.interestrate.swap.derivative.Swap;
import com.opengamma.analytics.financial.provider.calculator.generic.LastTimeCalculator;
import com.opengamma.analytics.financial.provider.calculator.inflation.PresentValueDiscountingInflationIssuerCalculator;
import com.opengamma.analytics.financial.provider.calculator.inflationissuer.ParSpreadInflationMarketQuoteCurveSensitivityIssuerDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.inflationissuer.ParSpreadInflationMarketQuoteIssuerDiscountingCalculator;
import com.opengamma.analytics.financial.provider.curve.CurveBuildingBlockBundle;
import com.opengamma.analytics.financial.provider.curve.MultiCurveBundle;
import com.opengamma.analytics.financial.provider.curve.SingleCurveBundle;
import com.opengamma.analytics.financial.provider.curve.inflationissuer.InflationIssuerDiscountBuildingRepository;
import com.opengamma.analytics.financial.provider.description.MulticurveProviderDiscountDataSets;
import com.opengamma.analytics.financial.provider.description.inflation.InflationIssuerProviderDiscount;
import com.opengamma.analytics.financial.provider.description.inflation.InflationIssuerProviderInterface;
import com.opengamma.analytics.financial.provider.description.interestrate.IssuerProviderDiscount;
import com.opengamma.analytics.financial.provider.sensitivity.inflation.InflationSensitivity;
import com.opengamma.analytics.math.interpolation.Interpolator1D;
import com.opengamma.analytics.math.interpolation.factory.FlatExtrapolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.LogLinearInterpolator1dAdapter;
import com.opengamma.analytics.math.interpolation.factory.NamedInterpolator1dFactory;
import com.opengamma.timeseries.precise.zdt.ImmutableZonedDateTimeDoubleTimeSeries;
import com.opengamma.timeseries.precise.zdt.ZonedDateTimeDoubleTimeSeries;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.DateUtils;
import com.opengamma.util.tuple.Pair;

/**
 *
 */
public class UsdDiscountingCpiTest {

  private static final Interpolator1D INTERPOLATOR_LOG_LINEAR = NamedInterpolator1dFactory.of(LogLinearInterpolator1dAdapter.NAME,
      FlatExtrapolator1dAdapter.NAME, FlatExtrapolator1dAdapter.NAME);

  private static final LastTimeCalculator MATURITY_CALCULATOR = LastTimeCalculator.getInstance();
  private static final double TOLERANCE_ROOT = 1.0E-10;
  private static final int STEP_MAX = 100;

  private static final Currency USD = Currency.USD;

  private static final double NOTIONAL = 1.0;

  private static final GeneratorSwapFixedInflationZeroCoupon GENERATOR_INFLATION_SWAP = GeneratorSwapFixedInflationMaster.getInstance().getGenerator("USCPI");
  private static final IndexPrice US_CPI = GENERATOR_INFLATION_SWAP.getIndexPrice();

  private static final ZonedDateTime NOW = DateUtils.getUTCDate(2012, 9, 28);

  private static final ZonedDateTimeDoubleTimeSeries TS_PRICE_INDEX_USD_WITH_TODAY = ImmutableZonedDateTimeDoubleTimeSeries.ofUTC(new ZonedDateTime[] {DateUtils.getUTCDate(2011, 9, 27),
      DateUtils.getUTCDate(2011, 9, 28), DateUtils.getUTCDate(2012, 6, 30), DateUtils.getUTCDate(2012, 7, 31), DateUtils.getUTCDate(2012, 8, 30) }, new double[] {200, 200, 200, 200, 200 });
  private static final String CURVE_NAME_CPI_USD = "USD CPI";

  /** Market values for the CPI USD curve */
  private static final double[] CPI_USD_MARKET_QUOTES = new double[] {0.0200, 0.0200, 0.0250, 0.0260, 0.0200, 0.0270, 0.0280, 0.0290, 0.0300, 0.0310, 0.0320, 0.0330, 0.0330, 0.0330, 0.0330 };
  /** Generators for the CPI USD curve */
  private static final GeneratorInstrument<? extends GeneratorAttribute>[] CPI_USD_GENERATORS = new GeneratorInstrument<?>[] {GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP,
    GENERATOR_INFLATION_SWAP,
    GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP,
    GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP, GENERATOR_INFLATION_SWAP };
    /** Tenors for the CPI USD curve */
    private static final Period[] CPI_USD_TENOR = new Period[] {Period.ofYears(1),
        Period.ofYears(2), Period.ofYears(3), Period.ofYears(4), Period.ofYears(5), Period.ofYears(6), Period.ofYears(7),
        Period.ofYears(8), Period.ofYears(9), Period.ofYears(10), Period.ofYears(12), Period.ofYears(15), Period.ofYears(20),
        Period.ofYears(25), Period.ofYears(30) };
    private static final GeneratorAttributeIR[] CPI_USD_ATTR = new GeneratorAttributeIR[CPI_USD_TENOR.length];
    static {
      for (int loopins = 0; loopins < CPI_USD_TENOR.length; loopins++) {
        CPI_USD_ATTR[loopins] = new GeneratorAttributeIR(CPI_USD_TENOR[loopins]);
      }
    }

    /** Standard USD CPI curve instrument definitions */
    private static final InstrumentDefinition<?>[] DEFINITIONS_CPI_USD;

    /** Units of curves */
    private static final int[] NB_UNITS = new int[] {1 };
    private static final int NB_BLOCKS = NB_UNITS.length;
    private static final InstrumentDefinition<?>[][][][] DEFINITIONS_UNITS = new InstrumentDefinition<?>[NB_BLOCKS][][][];
    private static final GeneratorPriceIndexCurve[][][] GENERATORS_UNITS = new GeneratorPriceIndexCurve[NB_BLOCKS][][];
    private static final String[][][] NAMES_UNITS = new String[NB_BLOCKS][][];

    private static final IssuerProviderDiscount US_MULTICURVE_PROVIDER = MulticurveProviderDiscountDataSets.createIssuerProvider().copy();
    private static final InflationIssuerProviderDiscount KNOWN_DATA = new InflationIssuerProviderDiscount(US_MULTICURVE_PROVIDER);

    private static final LinkedHashMap<String, IndexPrice[]> US_CPI_MAP = new LinkedHashMap<>();

    static {
      DEFINITIONS_CPI_USD = getDefinitions(CPI_USD_MARKET_QUOTES, CPI_USD_GENERATORS, CPI_USD_ATTR);

      for (int loopblock = 0; loopblock < NB_BLOCKS; loopblock++) {
        DEFINITIONS_UNITS[loopblock] = new InstrumentDefinition<?>[NB_UNITS[loopblock]][][];
        GENERATORS_UNITS[loopblock] = new GeneratorPriceIndexCurve[NB_UNITS[loopblock]][];
        NAMES_UNITS[loopblock] = new String[NB_UNITS[loopblock]][];
      }
      DEFINITIONS_UNITS[0][0] = new InstrumentDefinition<?>[][] {DEFINITIONS_CPI_USD };

      final GeneratorPriceIndexCurve genIntLin = new GeneratorPriceIndexCurveInterpolated(MATURITY_CALCULATOR, INTERPOLATOR_LOG_LINEAR);
      GENERATORS_UNITS[0][0] = new GeneratorPriceIndexCurve[] {genIntLin };

      NAMES_UNITS[0][0] = new String[] {CURVE_NAME_CPI_USD };

      US_CPI_MAP.put(CURVE_NAME_CPI_USD, new IndexPrice[] {US_CPI });
    }

    @SuppressWarnings({"rawtypes", "unchecked" })
    public static InstrumentDefinition<?>[] getDefinitions(final double[] marketQuotes, final GeneratorInstrument[] generators, final GeneratorAttribute[] attribute) {
      final InstrumentDefinition<?>[] definitions = new InstrumentDefinition<?>[marketQuotes.length];
      for (int loopmv = 0; loopmv < marketQuotes.length; loopmv++) {
        definitions[loopmv] = generators[loopmv].generateInstrument(NOW, marketQuotes[loopmv], NOTIONAL, attribute[loopmv]);
      }
      return definitions;
    }

    private static final List<Pair<InflationIssuerProviderDiscount, CurveBuildingBlockBundle>> CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK = new ArrayList<>();

    // Calculator
    private static final PresentValueDiscountingInflationIssuerCalculator PVIC = PresentValueDiscountingInflationIssuerCalculator.getInstance();
    private static final ParSpreadInflationMarketQuoteIssuerDiscountingCalculator PSIMQC = ParSpreadInflationMarketQuoteIssuerDiscountingCalculator.getInstance();
    private static final ParSpreadInflationMarketQuoteCurveSensitivityIssuerDiscountingCalculator PSIMQCSC = ParSpreadInflationMarketQuoteCurveSensitivityIssuerDiscountingCalculator.getInstance();

    private static final InflationIssuerDiscountBuildingRepository CURVE_BUILDING_REPOSITORY = new InflationIssuerDiscountBuildingRepository(TOLERANCE_ROOT, TOLERANCE_ROOT, STEP_MAX);

    private static final double TOLERANCE_CAL = 1.0E-9;

    @BeforeSuite
    static void initClass() {
      for (int loopblock = 0; loopblock < NB_BLOCKS; loopblock++) {
        CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.add(makeCurvesFromDefinitions(DEFINITIONS_UNITS[loopblock], GENERATORS_UNITS[loopblock], NAMES_UNITS[loopblock], KNOWN_DATA, PSIMQC, PSIMQCSC));
      }
    }

    @Test(enabled = true)
    public void performance() {
      long startTime, endTime;
      final int nbTest = 1000;

      startTime = System.currentTimeMillis();
      for (int looptest = 0; looptest < nbTest; looptest++) {
        makeCurvesFromDefinitions(DEFINITIONS_UNITS[0], GENERATORS_UNITS[0], NAMES_UNITS[0], KNOWN_DATA, PSIMQC, PSIMQCSC);
      }
      endTime = System.currentTimeMillis();
      System.out.println("InflationBuildingCurveSimpleTestEUR - " + nbTest + " curve construction Price index EUR 1 units: " + (endTime - startTime) + " ms");
      // Performance note: curve construction Price index EUR 1 units: 27-Mar-13: On Dell Precision T1850 3.5 GHz Quad-Core Intel Xeon: 3564 ms for 1000 sets.
    }

    @Test
    public void curveConstructionGeneratorOtherBlocks() {
      for (int loopblock = 0; loopblock < NB_BLOCKS; loopblock++) {
        curveConstructionTest(DEFINITIONS_UNITS[loopblock], CURVES_PAR_SPREAD_MQ_WITHOUT_TODAY_BLOCK.get(loopblock).getFirst(), loopblock);
      }
    }

    public void curveConstructionTest(final InstrumentDefinition<?>[][][] definitions, final InflationIssuerProviderDiscount curves, final int block) {
      final int nbBlocks = definitions.length;
      for (int loopblock = 0; loopblock < nbBlocks; loopblock++) {
        final InstrumentDerivative[][] instruments = convert(definitions[loopblock]);
        final double[][] pv = new double[instruments.length][];
        for (int loopcurve = 0; loopcurve < instruments.length; loopcurve++) {
          pv[loopcurve] = new double[instruments[loopcurve].length];
          for (int loopins = 0; loopins < instruments[loopcurve].length; loopins++) {
            pv[loopcurve][loopins] = curves.getFxRates().convert(instruments[loopcurve][loopins].accept(PVIC, curves), USD).getAmount();
            assertEquals("Curve construction: block " + block + ", unit " + loopblock + " - instrument " + loopins, 0, pv[loopcurve][loopins], TOLERANCE_CAL);
          }
        }
      }
    }

    @SuppressWarnings("unchecked")
    private static Pair<InflationIssuerProviderDiscount, CurveBuildingBlockBundle> makeCurvesFromDefinitions(final InstrumentDefinition<?>[][][] definitions,
        final GeneratorPriceIndexCurve[][] curveGenerators,
        final String[][] curveNames, final InflationIssuerProviderDiscount knownData, final InstrumentDerivativeVisitor<InflationIssuerProviderInterface, Double> calculator,
        final InstrumentDerivativeVisitor<InflationIssuerProviderInterface, InflationSensitivity> sensitivityCalculator) {
      final int nUnits = definitions.length;
      final MultiCurveBundle<GeneratorPriceIndexCurve>[] curveBundles = new MultiCurveBundle[nUnits];
      for (int i = 0; i < nUnits; i++) {
        final int nCurves = definitions[i].length;
        final SingleCurveBundle<GeneratorPriceIndexCurve>[] singleCurves = new SingleCurveBundle[nCurves];
        for (int j = 0; j < nCurves; j++) {
          final int nInstruments = definitions[i][j].length;
          final InstrumentDerivative[] derivatives = new InstrumentDerivative[nInstruments];
          final double[] initialGuess = new double[nInstruments];
          for (int k = 0; k < nInstruments; k++) {
            derivatives[k] = convert(definitions[i][j][k]);
            initialGuess[k] = definitions[i][j][k].accept(CurveUtils.INFLATION_INITIALIZATION);
          }
          final GeneratorPriceIndexCurve generator = curveGenerators[i][j].finalGenerator(derivatives);
          singleCurves[j] = new SingleCurveBundle<>(curveNames[i][j], derivatives, initialGuess, generator);
        }
        curveBundles[i] = new MultiCurveBundle<>(singleCurves);
      }
      return CURVE_BUILDING_REPOSITORY.makeCurvesFromDerivatives(curveBundles, knownData, US_CPI_MAP, calculator,
          sensitivityCalculator);
    }

    private static InstrumentDerivative[][] convert(final InstrumentDefinition<?>[][] definitions) {
      final InstrumentDerivative[][] instruments = new InstrumentDerivative[definitions.length][];
      for (int loopcurve = 0; loopcurve < definitions.length; loopcurve++) {
        instruments[loopcurve] = new InstrumentDerivative[definitions[loopcurve].length];
        int loopins = 0;
        for (final InstrumentDefinition<?> instrument : definitions[loopcurve]) {
          InstrumentDerivative ird;
          if (instrument instanceof SwapFixedInflationZeroCouponDefinition) {
            final Annuity<? extends Payment> ird1 = ((SwapFixedInflationZeroCouponDefinition) instrument).getFirstLeg().toDerivative(NOW);
            final Annuity<? extends Payment> ird2 = ((SwapFixedInflationZeroCouponDefinition) instrument).getSecondLeg().toDerivative(NOW, TS_PRICE_INDEX_USD_WITH_TODAY);
            ird = new Swap<>(ird1, ird2);
          }
          else {
            ird = instrument.toDerivative(NOW);
          }
          instruments[loopcurve][loopins++] = ird;
        }
      }
      return instruments;
    }

    private static InstrumentDerivative convert(final InstrumentDefinition<?> instrument) {
      InstrumentDerivative ird;
      if (instrument instanceof SwapFixedInflationZeroCouponDefinition) {
        final Annuity<? extends Payment> ird1 = ((SwapFixedInflationZeroCouponDefinition) instrument).getFirstLeg().toDerivative(NOW);
        final Annuity<? extends Payment> ird2 = ((SwapFixedInflationZeroCouponDefinition) instrument).getSecondLeg().toDerivative(NOW, TS_PRICE_INDEX_USD_WITH_TODAY);
        ird = new Swap<>(ird1, ird2);
      }
      else {
        ird = instrument.toDerivative(NOW);
      }
      return ird;
    }

}
