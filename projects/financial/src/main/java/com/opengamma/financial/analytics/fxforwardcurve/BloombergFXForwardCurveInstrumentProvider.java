/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.fxforwardcurve;

import org.apache.commons.lang.ObjectUtils;
import java.time.LocalDate;
import java.time.Period;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.core.value.MarketDataRequirementNames;
import com.opengamma.financial.analytics.ircurve.IndexType;
import com.opengamma.financial.analytics.ircurve.strips.DataFieldType;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalScheme;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.time.Tenor;

/**
 * Generates FX forward and (optionally) FX spot tickers for constructing FX forward rate curves.
 */
public class BloombergFXForwardCurveInstrumentProvider implements FXForwardCurveInstrumentProvider {
  /** The default data field */
  private static final String DATA_FIELD = MarketDataRequirementNames.MARKET_VALUE;
  /** The default data field type */
  private static final DataFieldType FIELD_TYPE = DataFieldType.OUTRIGHT;
  /** The default scheme */
  private static final ExternalScheme SCHEME = ExternalSchemes.BLOOMBERG_TICKER;
  /** The FX forward ticker prefix */
  private final String _prefix;
  /** The FX forward ticker postfix */
  private final String _postfix;
  /** The FX spot rate prefix */
  private final String _spotPrefix;
  /** The data field name */
  private final String _dataFieldName;
  /** The FX spot rate ticker */
  private final String _spotName;
  /** The FX spot rate external id */
  private final ExternalId _spotId;
  /** True if the BBG spot rate is to be used */
  private final boolean _useSpotRateFromGraph;

  /**
   * Constructor where only FX forward ticker information is supplied. This sets the _useSpotRateFromGraph field to true, which means that the FX spot rate will
   * be supplied by {@link com.opengamma.engine.value.ValueRequirementNames#SPOT_RATE}.
   *
   * @param prefix
   *          The FX forward prefix, not null
   * @param postfix
   *          The FX forward postfix, not null
   * @param dataFieldName
   *          The Bloomberg data field name, not null
   */
  public BloombergFXForwardCurveInstrumentProvider(final String prefix, final String postfix, final String dataFieldName) {
    ArgumentChecker.notNull(prefix, "prefix");
    ArgumentChecker.notNull(postfix, "postfix");
    ArgumentChecker.notNull(dataFieldName, "dataFieldName");
    _prefix = prefix;
    _postfix = postfix;
    _dataFieldName = dataFieldName;
    _spotPrefix = null;
    _spotName = null;
    _spotId = null;
    _useSpotRateFromGraph = true;
  }

  /**
   * Constructor where the FX forward ticker and FX spot rate ticker information is supplied. This sets the _useSpotRateFromGraph field to false, which means
   * that the FX spot rate will be requested from the Bloomberg ticker.
   * 
   * @param prefix
   *          The FX forward prefix, not null
   * @param postfix
   *          The FX forward postfix, not null
   * @param spotPrefix
   *          The FX spot prefix, not null
   * @param dataFieldName
   *          The FX spot data field name, not null
   */
  public BloombergFXForwardCurveInstrumentProvider(final String prefix, final String postfix, final String spotPrefix, final String dataFieldName) {
    ArgumentChecker.notNull(prefix, "prefix");
    ArgumentChecker.notNull(postfix, "postfix");
    ArgumentChecker.notNull(spotPrefix, "spotPrefix");
    ArgumentChecker.notNull(dataFieldName, "dataFieldName");
    _prefix = prefix;
    _postfix = postfix;
    _spotPrefix = spotPrefix;
    _dataFieldName = dataFieldName;
    _spotName = spotPrefix + " " + _postfix;
    _spotId = ExternalId.of(SCHEME, _spotName);
    _useSpotRateFromGraph = false;
  }

  /**
   * Gets the FX forward ticker prefix.
   * 
   * @return The FX forward ticker prefix
   */
  public String getPrefix() {
    return _prefix;
  }

  /**
   * Gets the FX forward ticker postfix.
   * 
   * @return The FX forward ticker postfix
   */
  public String getPostfix() {
    return _postfix;
  }

  /**
   * Gets the FX spot ticker prefix.
   * 
   * @return The FX spot ticker prefix
   */
  public String getSpotPrefix() {
    if (_useSpotRateFromGraph) {
      throw new IllegalStateException("This configuration does not support FX spot rate tickers");
    }
    return _spotPrefix;
  }

  @Override
  public String getDataFieldName() {
    return _dataFieldName;
  }

  /**
   * Gets the spot ticker value e.g. EUR Curncy.
   * 
   * @return The spot ticker value.
   */
  public String getSpotName() {
    if (_useSpotRateFromGraph) {
      throw new IllegalStateException("This configuration does not support FX spot rate tickers");
    }
    return _spotName;
  }

  @Override
  public ExternalId getSpotInstrument() {
    if (_useSpotRateFromGraph) {
      throw new IllegalStateException("This configuration does not support FX spot rate tickers");
    }
    return _spotId;
  }

  @Override
  public String getMarketDataField() {
    return DATA_FIELD;
  }

  @Override
  public DataFieldType getDataFieldType() {
    return FIELD_TYPE;
  }

  @Override
  public boolean useSpotRateFromGraph() {
    return _useSpotRateFromGraph;
  }

  @Override
  public ExternalId getInstrument(final LocalDate curveDate, final Tenor tenor) {
    final StringBuffer ticker = new StringBuffer();
    ticker.append(_prefix);
    final Period period = tenor.getPeriod();
    if (period.getYears() != 0) {
      ticker.append(period.getYears() + "Y");
    } else if (period.getMonths() != 0) {
      ticker.append(period.getMonths() + "M");
    } else {
      final int days = period.getDays();
      if (days != 0) {
        if (days % 7 == 0) {
          ticker.append(days / 7 + "W");
        } else if (days == 1) {
          ticker.append("ON");
        } else if (days == 2) {
          ticker.append("TN");
        } else if (days == 3) {
          ticker.append("SN");
        } else {
          throw new OpenGammaRuntimeException("Cannot handle period of " + days + " days");
        }
      } else {
        throw new OpenGammaRuntimeException("Can only handle periods of year, month, week and day");
      }
    }
    ticker.append(" ");
    ticker.append(_postfix);
    return ExternalId.of(SCHEME, ticker.toString());
  }

  @Override
  public ExternalId getInstrument(final LocalDate curveDate, final Tenor tenor, final int numQuarterlyFuturesFromTenor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ExternalId getInstrument(final LocalDate curveDate, final Tenor tenor, final int periodsPerYear, final boolean isPeriodicZeroDeposit) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ExternalId getInstrument(final LocalDate curveDate, final Tenor tenor, final Tenor payTenor, final Tenor receiveTenor, final IndexType payIndexType,
      final IndexType receiveIndexType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ExternalId getInstrument(final LocalDate curveDate, final Tenor tenor, final Tenor resetTenor, final IndexType indexType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ExternalId getInstrument(final LocalDate curveDate, final Tenor startTenor, final Tenor futureTenor, final int numFutureFromTenor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ExternalId getInstrument(final LocalDate curveDate, final Tenor startTenor, final int startIMMPeriods, final int endIMMPeriods) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int hashCode() {
    return getPrefix().hashCode() + getPostfix().hashCode() + getDataFieldName().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BloombergFXForwardCurveInstrumentProvider)) {
      return false;
    }
    final BloombergFXForwardCurveInstrumentProvider other = (BloombergFXForwardCurveInstrumentProvider) obj;
    if (!_useSpotRateFromGraph && !ObjectUtils.equals(getSpotPrefix(), other.getSpotPrefix())) {
      return false;
    }
    return getPrefix().equals(other.getPrefix())
        && getPostfix().equals(other.getPostfix())
        && getDataFieldName().equals(other.getDataFieldName());
  }

}
