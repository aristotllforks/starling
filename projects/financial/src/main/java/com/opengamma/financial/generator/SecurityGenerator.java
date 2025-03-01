/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.generator;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.core.convention.ConventionSource;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeriesSource;
import com.opengamma.core.holiday.HolidaySource;
import com.opengamma.core.legalentity.LegalEntitySource;
import com.opengamma.core.position.Counterparty;
import com.opengamma.core.region.RegionSource;
import com.opengamma.core.value.MarketDataRequirementNames;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.DefaultComputationTargetResolver;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.CompiledFunctionDefinition;
import com.opengamma.engine.function.DummyFunctionReinitializer;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputsImpl;
import com.opengamma.engine.marketdata.ExternalIdBundleResolver;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.OpenGammaExecutionContext;
import com.opengamma.financial.analytics.model.forex.FXUtils;
import com.opengamma.financial.currency.ConfigDBCurrencyMatrixSource;
import com.opengamma.financial.currency.ConfigDBCurrencyPairsSource;
import com.opengamma.financial.currency.CurrencyMatrixResolver;
import com.opengamma.financial.currency.CurrencyPairs;
import com.opengamma.financial.currency.CurrencyPairsResolver;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalScheme;
import com.opengamma.id.VersionCorrection;
import com.opengamma.lambdava.functions.Function2;
import com.opengamma.master.config.ConfigMaster;
import com.opengamma.master.exchange.ExchangeMaster;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesMaster;
import com.opengamma.master.historicaltimeseries.impl.DefaultHistoricalTimeSeriesResolver;
import com.opengamma.master.historicaltimeseries.impl.DefaultHistoricalTimeSeriesSelector;
import com.opengamma.master.legalentity.LegalEntityMaster;
import com.opengamma.master.position.ManageableTrade;
import com.opengamma.master.security.ManageableSecurity;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.master.security.impl.MasterSecuritySource;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.async.AsynchronousExecution;
import com.opengamma.util.async.AsynchronousOperation;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.DateUtils;
import com.opengamma.util.tuple.Pair;

/**
 * Utility class for constructing parameters to random (but reasonable) securities.
 *
 * @param <T>
 *          the security type, or a common super type if multiple types are being produced
 */
public abstract class SecurityGenerator<T extends ManageableSecurity> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityGenerator.class);

  /**
   * Format dates.
   */
  public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();

  /**
   * Format rates.
   */
  public static final DecimalFormat RATE_FORMATTER = new DecimalFormat("0.###%");

  /**
   * Format notionals.
   */
  public static final DecimalFormat NOTIONAL_FORMATTER = new DecimalFormat("0,000");

  /**
   * Constant for the length of a year in days.
   */
  protected static final double YEAR_LENGTH = 365.25;

  private Random _random = new Random();
  private ConfigSource _configSource;
  private ConfigMaster _configMaster;
  private HolidaySource _holidaySource;
  private HistoricalTimeSeriesSource _historicalSource;
  private HistoricalTimeSeriesMaster _htsMaster;
  private RegionSource _regionSource;
  private LegalEntitySource _legalEntitySource;
  private LegalEntityMaster _legalEntityMaster;
  private ExchangeMaster _exchangeMaster;
  private SecurityMaster _securityMaster;
  private String _currencyCurveName;
  private final Map<Currency, String> _curveCalculationConfig = new HashMap<>();
  private ExternalScheme _preferredScheme;
  private Function2<Currency, Currency, ExternalId> _spotRateIdentifier;
  private ConventionSource _conventionSource;

  private Currency[] _currencies;

  public Random getRandom() {
    return _random;
  }

  public void setRandom(final Random random) {
    _random = random;
  }

  protected int getRandom(final int n) {
    return getRandom().nextInt(n);
  }

  protected double getRandom(final double low, final double high) {
    return low + (high - low) * getRandom().nextDouble();
  }

  protected <X> X getRandom(final X[] xs) {
    return xs[getRandom(xs.length)];
  }

  protected <X> X getRandom(final List<X> xs) {
    return xs.get(getRandom(xs.size()));
  }

  protected int getRandom(final int[] xs) {
    return xs[getRandom(xs.length)];
  }

  protected double getRandom(final double[] xs) {
    return xs[getRandom(xs.length)];
  }

  public ConventionSource getConventionSource() {
    return _conventionSource;
  }

  public void setConventionSource(final ConventionSource conventionSource) {
    _conventionSource = conventionSource;
  }

  public ConfigSource getConfigSource() {
    return _configSource;
  }

  public void setConfigSource(final ConfigSource configSource) {
    _configSource = configSource;
  }

  public ConfigMaster getConfigMaster() {
    return _configMaster;
  }

  public void setConfigMaster(final ConfigMaster configMaster) {
    _configMaster = configMaster;
  }

  public HolidaySource getHolidaySource() {
    return _holidaySource;
  }

  public void setHolidaySource(final HolidaySource holidaySource) {
    _holidaySource = holidaySource;
  }

  public HistoricalTimeSeriesSource getHistoricalSource() {
    return _historicalSource;
  }

  public void setHistoricalSource(final HistoricalTimeSeriesSource historicalSource) {
    _historicalSource = historicalSource;
  }

  public HistoricalTimeSeriesMaster getHistoricalTimeSeriesMaster() {
    return _htsMaster;
  }

  public void setHistoricalTimeSeriesMaster(final HistoricalTimeSeriesMaster htsMaster) {
    _htsMaster = htsMaster;
  }

  public ExchangeMaster getExchangeMaster() {
    return _exchangeMaster;
  }

  public void setExchangeMaster(final ExchangeMaster exchangeMaster) {
    _exchangeMaster = exchangeMaster;
  }

  public RegionSource getRegionSource() {
    return _regionSource;
  }

  public void setRegionSource(final RegionSource regionSource) {
    _regionSource = regionSource;
  }

  public LegalEntitySource getLegalEntitySource() {
    return _legalEntitySource;
  }

  public void setLegalEntitySource(final LegalEntitySource legalEntitySource) {
    _legalEntitySource = legalEntitySource;
  }

  public LegalEntityMaster getLegalEntityMaster() {
    return _legalEntityMaster;
  }

  public void setLegalEntityMaster(final LegalEntityMaster legalEntityMaster) {
    _legalEntityMaster = legalEntityMaster;
  }

  public SecurityMaster getSecurityMaster() {
    return _securityMaster;
  }

  public void setSecurityMaster(final SecurityMaster securityMaster) {
    _securityMaster = securityMaster;
  }

  public String getCurrencyCurveName() {
    return _currencyCurveName;
  }

  public void setCurrencyCurveName(final String currencyCurveName) {
    _currencyCurveName = currencyCurveName;
  }

  public String getCurveCalculationConfig(final Currency currency) {
    return _curveCalculationConfig.get(currency);
  }

  public void setCurveCalculationConfigs(final Map<Currency, String> curveCalculationConfigs) {
    _curveCalculationConfig.clear();
    _curveCalculationConfig.putAll(curveCalculationConfigs);
  }

  public void setCurveCalculationConfig(final Currency currency, final String curveCalculationConfig) {
    _curveCalculationConfig.put(currency, curveCalculationConfig);
  }

  public Function2<Currency, Currency, ExternalId> getSpotRateIdentifier() {
    return _spotRateIdentifier;
  }

  public void setSpotRateIdentifier(final Function2<Currency, Currency, ExternalId> spotRateIdentifier) {
    _spotRateIdentifier = spotRateIdentifier;
  }

  private FunctionExecutionContext createFunctionExecutionContext(final LocalDate valuationTime) {
    final FunctionExecutionContext context = new FunctionExecutionContext();
    context.setValuationTime(valuationTime.atTime(LocalTime.NOON).toInstant(ZoneOffset.UTC));
    context.setValuationClock(DateUtils.fixedClockUTC(context.getValuationTime()));
    context.setComputationTargetResolver(new DefaultComputationTargetResolver(context.getSecuritySource()).atVersionCorrection(VersionCorrection.LATEST));
    OpenGammaExecutionContext.setHolidaySource(context, getHolidaySource());
    OpenGammaExecutionContext.setRegionSource(context, getRegionSource());
    OpenGammaExecutionContext.setConventionSource(context, getConventionSource());
    OpenGammaExecutionContext.setSecuritySource(context, new MasterSecuritySource(getSecurityMaster()));
    OpenGammaExecutionContext.setHistoricalTimeSeriesSource(context, getHistoricalSource());
    OpenGammaExecutionContext.setConfigSource(context, getConfigSource());
    OpenGammaExecutionContext.setLegalEntitySource(context, getLegalEntitySource());
    return context;
  }

  private FunctionCompilationContext createFunctionCompilationContext() {
    final FunctionCompilationContext context = new FunctionCompilationContext();
    context.setFunctionReinitializer(new DummyFunctionReinitializer());
    OpenGammaCompilationContext.setHolidaySource(context, getHolidaySource());
    OpenGammaCompilationContext.setRegionSource(context, getRegionSource());
    OpenGammaCompilationContext.setLegalEntitySource(context, getLegalEntitySource());
    OpenGammaCompilationContext.setConventionSource(context, getConventionSource());
    OpenGammaCompilationContext.setSecuritySource(context, new MasterSecuritySource(getSecurityMaster()));
    OpenGammaCompilationContext.setHistoricalTimeSeriesResolver(context,
        new DefaultHistoricalTimeSeriesResolver(new DefaultHistoricalTimeSeriesSelector(getConfigSource()), getHistoricalTimeSeriesMaster()));
    final DefaultComputationTargetResolver targetResolver = new DefaultComputationTargetResolver(context.getSecuritySource());
    targetResolver.addResolver(CurrencyPairs.TYPE, new CurrencyPairsResolver(new ConfigDBCurrencyPairsSource(getConfigSource())));
    targetResolver.addResolver(CurrencyMatrixResolver.TYPE, new CurrencyMatrixResolver(new ConfigDBCurrencyMatrixSource(getConfigSource())));
    context.setRawComputationTargetResolver(targetResolver);
    context.setComputationTargetResolver(context.getRawComputationTargetResolver().atVersionCorrection(VersionCorrection.LATEST));
    OpenGammaCompilationContext.setConfigSource(context, getConfigSource());
    return context;
  }

  private CompiledFunctionDefinition createFunction(final FunctionCompilationContext compContext, final FunctionExecutionContext execContext,
      final AbstractFunction function) {
    function.setUniqueId(function.getClass().getSimpleName());
    function.init(compContext);
    return function.compile(compContext, execContext.getValuationTime());
  }

  private ComputedValue execute(final FunctionExecutionContext context, final CompiledFunctionDefinition function, final ComputationTarget target,
      final ValueRequirement output, final ComputedValue... inputs) {
    final FunctionInputsImpl functionInputs = new FunctionInputsImpl(context.getComputationTargetResolver().getSpecificationResolver(), Arrays.asList(inputs));
    Set<ComputedValue> results;
    try {
      results = function.getFunctionInvoker().execute(context, functionInputs, target, Collections.singleton(output));
    } catch (final AsynchronousExecution ex) {
      results = AsynchronousOperation.getResult(ex);
    }
    for (final ComputedValue result : results) {
      if (output.getValueName().equals(result.getSpecification().getValueName())) {
        return result;
      }
    }
    throw new OpenGammaRuntimeException("Function " + function + " didn't produce " + output + " from " + functionInputs);
  }

  private ComputedValue findMarketData(final ExternalIdBundleResolver resolver, final ValueRequirement requirement) {
    final ComputationTargetSpecification targetSpec = resolver.getTargetSpecification(requirement.getTargetReference());
    // TODO: What to do if the targetSpec can't be resolved. We can still get an identifier bundle, but the spec for the CV will be wrong
    final Pair<LocalDate, Double> value = getHistoricalSource().getLatestDataPoint(MarketDataRequirementNames.MARKET_VALUE,
        resolver.getExternalIdBundle(targetSpec), null);
    if (value == null) {
      return null;
    }
    return new ComputedValue(
        new ValueSpecification(requirement.getValueName(), targetSpec, ValueProperties.with(ValuePropertyNames.FUNCTION, "MARKET_DATA").get()),
        value.getSecond());
  }

  private ComputedValue[] findMarketData(final FunctionCompilationContext compilationContext, final Collection<ValueRequirement> requirements) {
    LOGGER.debug("Resolving {}", requirements);
    final ExternalIdBundleResolver lookup = new ExternalIdBundleResolver(compilationContext.getComputationTargetResolver());
    final ComputedValue[] values = new ComputedValue[requirements.size()];
    int i = 0;
    for (final ValueRequirement requirement : requirements) {
      final ComputedValue value = findMarketData(lookup, requirement);
      if (value == null) {
        LOGGER.debug("Couldn't resolve {}", requirement);
        return null;
      }
      values[i++] = value;
    }
    return values;
  }

  protected Double getApproxFXRate(final LocalDate date, final Pair<Currency, Currency> currencies) {
    final Currency payCurrency;
    final Currency receiveCurrency;
    if (FXUtils.isInBaseQuoteOrder(currencies.getFirst(), currencies.getSecond())) {
      payCurrency = currencies.getFirst();
      receiveCurrency = currencies.getSecond();
    } else {
      payCurrency = currencies.getSecond();
      receiveCurrency = currencies.getFirst();
    }
    final ExternalId spotRateIdentifier = getSpotRateIdentifier().execute(payCurrency, receiveCurrency);
    final Pair<LocalDate, Double> spotRate = getHistoricalSource().getLatestDataPoint(MarketDataRequirementNames.MARKET_VALUE, spotRateIdentifier.toBundle(),
        null);
    if (spotRate == null) {
      LOGGER.debug("No spot rate for {}", spotRateIdentifier);
      return null;
    }
    final double rate = 1;
    LOGGER.debug("Calculated rate {} for {} on {}", new Object[] { rate, currencies, date });
    return rate;
  }

  public ExternalScheme getPreferredScheme() {
    return _preferredScheme;
  }

  public void setPreferredScheme(final ExternalScheme preferredScheme) {
    _preferredScheme = preferredScheme;
  }

  public static Currency[] getDefaultCurrencies() {
    return new Currency[] { Currency.USD, Currency.GBP, Currency.EUR, Currency.JPY, Currency.CHF };
  }

  public void setCurrencies(final Currency[] currencies) {
    _currencies = currencies;
  }

  public Currency[] getCurrencies() {
    if (_currencies == null) {
      return getDefaultCurrencies();
    }
    return _currencies;
  }

  protected Currency getRandomCurrency() {
    return getRandom(getCurrencies());
  }

  private boolean isWorkday(final DayOfWeek dow, final Currency currency) {
    // TODO: use a proper convention/holiday source
    return dow.getValue() < 6;
  }

  private boolean isHoliday(final LocalDate ldp, final Currency currency) {
    return getHolidaySource().isHoliday(ldp, currency);
  }

  /**
   * Returns the date unchanged if this is a working day, otherwise advances the date.
   *
   * @param zdt
   *          the date to consider
   * @param currency
   *          the currency identifying the holiday zone
   * @return the original or adjusted date
   */
  // TODO: replace this with a date adjuster
  protected ZonedDateTime nextWorkingDay(ZonedDateTime zdt, final Currency currency) {
    while (!isWorkday(zdt.getDayOfWeek(), currency) || isHoliday(zdt.toLocalDate(), currency)) {
      zdt = zdt.plusDays(1);
    }
    return zdt;
  }

  protected ZonedDateTime nextWorkingDay(ZonedDateTime zdt, final Currency... currencies) {
    ArgumentChecker.isTrue(currencies.length > 0, "currencies");
    do {
      for (final Currency currency : currencies) {
        if (!isWorkday(zdt.getDayOfWeek(), currency) || isHoliday(zdt.toLocalDate(), currency)) {
          zdt = zdt.plusDays(1);
          continue;
        }
      }
      return zdt;
    } while (true);
  }

  /**
   * Returns the date unchanged if this is a working day, otherwise retreats the date.
   *
   * @param zdt
   *          the date to consider
   * @param currency
   *          the currency identifying the holiday zone
   * @return the original or adjusted date
   */
  // TODO: replace this with a date adjuster
  protected ZonedDateTime previousWorkingDay(ZonedDateTime zdt, final Currency currency) {
    while (!isWorkday(zdt.getDayOfWeek(), currency) || isHoliday(zdt.toLocalDate(), currency)) {
      zdt = zdt.minusDays(1);
    }
    return zdt;
  }

  protected ZonedDateTime previousWorkingDay(ZonedDateTime zdt, final Currency... currencies) {
    ArgumentChecker.isTrue(currencies.length > 0, "currencies");
    do {
      for (final Currency currency : currencies) {
        if (!isWorkday(zdt.getDayOfWeek(), currency) || isHoliday(zdt.toLocalDate(), currency)) {
          zdt = zdt.minusDays(1);
          continue;
        }
      }
      return zdt;
    } while (true);
  }

  /**
   * Creates a new random, but reasonable, security.
   *
   * @return the new security, or null if no security can be generated
   */
  public abstract T createSecurity();

  /**
   * Creates a new random, but reasonable, trade.
   *
   * @param quantityGenerator
   *          the supplied quantity generator
   * @param securityPersister
   *          the supplied security persister
   * @param counterPartyGenerator
   *          the supplied counter party generator
   * @return the new trade, or null if no trade can be generated
   */
  public ManageableTrade createSecurityTrade(final QuantityGenerator quantityGenerator, final SecurityPersister securityPersister,
      final NameGenerator counterPartyGenerator) {
    ManageableTrade trade = null;
    final T security = createSecurity();
    if (security != null) {
      final ZonedDateTime tradeDate = previousWorkingDay(ZonedDateTime.now().minusDays(getRandom(30)), getRandomCurrency());
      trade = new ManageableTrade(quantityGenerator.createQuantity(), securityPersister.storeSecurity(security), tradeDate.toLocalDate(),
          tradeDate.toOffsetDateTime().toOffsetTime(), ExternalId.of(Counterparty.DEFAULT_SCHEME, counterPartyGenerator.createName()));
    }
    return trade;
  }

}
