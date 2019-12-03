/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg.loader;

import static com.opengamma.bbg.BloombergConstants.BLOOMBERG_EQUITY_OPTION_SECURITY_TYPE;
import static com.opengamma.bbg.BloombergConstants.FIELD_EXCH_CODE;
import static com.opengamma.bbg.BloombergConstants.FIELD_ID_BBG_UNIQUE;
import static com.opengamma.bbg.BloombergConstants.FIELD_MARKET_SECTOR_DES;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPTION_ROOT_TICKER;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_EXERCISE_TYP;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_EXPIRE_DT;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_PUT_CALL;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_STRIKE_PX;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_TICK_VAL;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_UNDERLYING_SECURITY_DES;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_UNDL_CRNCY;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_VAL_PT;
import static com.opengamma.bbg.BloombergConstants.FIELD_PRIMARY_EXCHANGE_NAME;
import static com.opengamma.bbg.BloombergConstants.FIELD_SECURITY_DES;

import java.util.HashSet;
import java.util.Set;

import org.fudgemsg.FudgeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.LocalDate;

import com.google.common.collect.ImmutableSet;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.bbg.referencedata.ReferenceDataProvider;
import com.opengamma.bbg.security.BloombergSecurityProvider;
import com.opengamma.bbg.util.BloombergDataUtils;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.security.option.EquityOptionSecurity;
import com.opengamma.financial.security.option.ExerciseType;
import com.opengamma.financial.security.option.OptionType;
import com.opengamma.financial.timeseries.exchange.DefaultExchangeDataProvider;
import com.opengamma.financial.timeseries.exchange.ExchangeDataProvider;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.master.security.ManageableSecurity;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.DateUtils;
import com.opengamma.util.time.Expiry;

/**
 * Loads the data for an Equity Option from Bloomberg.
 */
public class EquityOptionLoader extends SecurityLoader {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(EquityOptionLoader.class);
  /**
   * The fields to load from Bloomberg.
   */
  private static final Set<String> BLOOMBERG_EQUITY_OPTION_FIELDS = ImmutableSet.of(
      FIELD_OPTION_ROOT_TICKER,
      FIELD_EXCH_CODE,
      FIELD_SECURITY_DES,
      FIELD_MARKET_SECTOR_DES,
      FIELD_OPT_EXERCISE_TYP,
      FIELD_OPT_STRIKE_PX,
      FIELD_OPT_PUT_CALL,
      FIELD_OPT_UNDERLYING_SECURITY_DES,
      FIELD_OPT_UNDL_CRNCY,
      FIELD_OPT_EXPIRE_DT,
      FIELD_ID_BBG_UNIQUE,
      FIELD_OPT_TICK_VAL,
      FIELD_OPT_VAL_PT);

  /**
   * The valid Bloomberg security types for EquityOption.
   */
  public static final Set<String> VALID_SECURITY_TYPES = ImmutableSet.of(BLOOMBERG_EQUITY_OPTION_SECURITY_TYPE);

  private static final ExchangeDataProvider EXCHANGE_DATA = DefaultExchangeDataProvider.getInstance();

  /**
   * Creates an instance.
   * 
   * @param referenceDataProvider
   *          the provider, not null
   */
  public EquityOptionLoader(final ReferenceDataProvider referenceDataProvider) {
    super(LOGGER, referenceDataProvider, SecurityType.EQUITY_OPTION);
  }

  // -------------------------------------------------------------------------
  @Override
  protected ManageableSecurity createSecurity(final FudgeMsg fieldData) {
    final String rootTicker = fieldData.getString(FIELD_OPTION_ROOT_TICKER);
    String exchange = fieldData.getString(FIELD_EXCH_CODE);
    final String exchangeDescription = fieldData.getString(FIELD_PRIMARY_EXCHANGE_NAME);
    final String optionExerciseType = fieldData.getString(FIELD_OPT_EXERCISE_TYP);
    final double optionStrikePrice = fieldData.getDouble(FIELD_OPT_STRIKE_PX);
    final double pointValue = fieldData.getDouble(FIELD_OPT_VAL_PT);
    final String putOrCall = fieldData.getString(FIELD_OPT_PUT_CALL);
    final String underlingTicker = fieldData.getString(FIELD_OPT_UNDERLYING_SECURITY_DES);
    final String currency = fieldData.getString(FIELD_OPT_UNDL_CRNCY);
    final String expiryDate = fieldData.getString(FIELD_OPT_EXPIRE_DT);
    final String bbgUniqueID = fieldData.getString(FIELD_ID_BBG_UNIQUE);
    final String secDes = fieldData.getString(FIELD_SECURITY_DES);
    final String marketSector = fieldData.getString(FIELD_MARKET_SECTOR_DES);

    if (!BloombergDataUtils.isValidField(bbgUniqueID)) {
      LOGGER.warn("bloomberg UniqueID is missing, cannot construct equityOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(rootTicker)) {
      LOGGER.warn("option root ticker is missing, cannot construct equityOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(putOrCall)) {
      LOGGER.warn("option type is missing, cannot construct equityOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(exchange)) {
      LOGGER.warn("exchange is missing, cannot construct equityOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(expiryDate)) {
      LOGGER.warn("option expiry date is missing, cannot construct equityOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(underlingTicker)) {
      LOGGER.warn("option underlying ticker is missing, cannot construct equityOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(currency)) {
      LOGGER.warn("option currency is missing, cannot construct equityOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(optionExerciseType)) {
      LOGGER.warn("option exercise type is missing, cannot construct equityOption security");
      return null;
    }
    final OptionType optionType = getOptionType(putOrCall);
    // get year month day from expiryDate in the yyyy-mm-dd format
    LocalDate expiryLocalDate = null;
    try {
      expiryLocalDate = LocalDate.parse(expiryDate);
    } catch (final Exception e) {
      throw new OpenGammaRuntimeException(expiryDate + " returned from bloomberg not in format yyyy-mm-dd", e);
    }
    final int year = expiryLocalDate.getYear();
    final int month = expiryLocalDate.getMonthValue();
    final int day = expiryLocalDate.getDayOfMonth();
    final Expiry expiry = new Expiry(DateUtils.getUTCDate(year, month, day));

    final Currency ogCurrency = Currency.parse(currency);

    final Set<ExternalId> identifiers = new HashSet<>();
    identifiers.add(ExternalSchemes.bloombergBuidSecurityId(bbgUniqueID));
    if (BloombergDataUtils.isValidField(secDes) && BloombergDataUtils.isValidField(marketSector)) {
      identifiers.add(ExternalSchemes.bloombergTickerSecurityId(secDes + " " + marketSector));
    }

    final ExerciseType exerciseType = getExerciseType(optionExerciseType);

    // currently we will pick up the unified bbg exchange code - we try to map to MIC via the description
    if (exchangeDescription != null) {
      final String exchangeMIC = EXCHANGE_DATA.getExchangeFromDescription(exchangeDescription).getMic();
      if (exchangeMIC != null) {
        exchange = exchangeMIC;
      }
    }

    final EquityOptionSecurity security = new EquityOptionSecurity(
        optionType,
        optionStrikePrice,
        ogCurrency,
        ExternalSchemes.bloombergTickerSecurityId(underlingTicker),
        exerciseType,
        expiry,
        pointValue,
        exchange);
    security.setExternalIdBundle(ExternalIdBundle.of(identifiers));
    security.setUniqueId(BloombergSecurityProvider.createUniqueId(bbgUniqueID));
    // build option display name
    final StringBuilder buf = new StringBuilder(rootTicker);
    buf.append(" ");
    buf.append(expiryDate);
    if (optionType == OptionType.CALL) {
      buf.append(" C ");
    } else {
      buf.append(" P ");
    }
    buf.append(optionStrikePrice);
    security.setName(buf.toString());
    return security;
  }

  @Override
  protected Set<String> getBloombergFields() {
    return BLOOMBERG_EQUITY_OPTION_FIELDS;
  }

}
