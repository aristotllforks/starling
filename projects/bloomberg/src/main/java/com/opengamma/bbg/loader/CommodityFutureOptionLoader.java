/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg.loader;

import static com.opengamma.bbg.BloombergConstants.BBG_BASE_METAL_TYPE;
import static com.opengamma.bbg.BloombergConstants.BBG_COAL;
import static com.opengamma.bbg.BloombergConstants.BBG_CRUDE_OIL;
import static com.opengamma.bbg.BloombergConstants.BBG_ELECTRICITY;
import static com.opengamma.bbg.BloombergConstants.BBG_FOODSTUFF;
import static com.opengamma.bbg.BloombergConstants.BBG_LIVESTOCK;
import static com.opengamma.bbg.BloombergConstants.BBG_PRECIOUS_METAL_TYPE;
import static com.opengamma.bbg.BloombergConstants.BBG_REFINED_PRODUCTS;
import static com.opengamma.bbg.BloombergConstants.BBG_SOY;
import static com.opengamma.bbg.BloombergConstants.BBG_WHEAT;
import static com.opengamma.bbg.BloombergConstants.FIELD_EXCH_CODE;
import static com.opengamma.bbg.BloombergConstants.FIELD_FUT_VAL_PT;
import static com.opengamma.bbg.BloombergConstants.FIELD_ID_BBG_UNIQUE;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_EXERCISE_TYP;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_EXPIRE_DT;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_PUT_CALL;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_STRIKE_PX;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_UNDERLYING_SECURITY_DES;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_UNDL_CRNCY;
import static com.opengamma.bbg.BloombergConstants.FIELD_PARSEKYABLE_DES;
import static com.opengamma.bbg.BloombergConstants.FIELD_PRIMARY_EXCHANGE_NAME;
import static com.opengamma.bbg.BloombergConstants.FIELD_TICKER;
import static com.opengamma.bbg.BloombergConstants.FIELD_UNDL_ID_BB_UNIQUE;

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
import com.opengamma.financial.security.option.CommodityFutureOptionSecurity;
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
 * Loads the data for an Commodity Future Option from Bloomberg.
 */
public class CommodityFutureOptionLoader extends SecurityLoader {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(CommodityFutureOptionLoader.class);
  /**
   * The fields to load from Bloomberg.
   */
  private static final Set<String> BLOOMBERG_FUTURE_OPTION_FIELDS = ImmutableSet.of(
      FIELD_TICKER,
      FIELD_EXCH_CODE,
      FIELD_PARSEKYABLE_DES,
      FIELD_OPT_EXERCISE_TYP,
      FIELD_OPT_STRIKE_PX,
      FIELD_OPT_PUT_CALL,
      FIELD_OPT_UNDERLYING_SECURITY_DES,
      FIELD_OPT_UNDL_CRNCY,
      FIELD_OPT_EXPIRE_DT,
      FIELD_ID_BBG_UNIQUE,
      FIELD_FUT_VAL_PT,
      FIELD_UNDL_ID_BB_UNIQUE);

  /**
   * The valid Bloomberg security types for Commodity Future Option. These strings will often come up as 'FUTURES_CATEGORY'
   */
  public static final Set<String> VALID_SECURITY_TYPES = ImmutableSet.of(
      BBG_PRECIOUS_METAL_TYPE,
      BBG_BASE_METAL_TYPE,
      BBG_REFINED_PRODUCTS,
      BBG_ELECTRICITY,
      BBG_COAL,
      BBG_CRUDE_OIL,
      BBG_WHEAT,
      BBG_SOY,
      BBG_FOODSTUFF,
      BBG_LIVESTOCK);

  private static final ExchangeDataProvider EXCHANGE_DATA = DefaultExchangeDataProvider.getInstance();

  /**
   * Creates an instance.
   * 
   * @param referenceDataProvider
   *          the provider, not null
   */
  public CommodityFutureOptionLoader(final ReferenceDataProvider referenceDataProvider) {
    super(LOGGER, referenceDataProvider, SecurityType.COMMODITY_FUTURE_OPTION);
  }

  // -------------------------------------------------------------------------
  @Override
  protected ManageableSecurity createSecurity(final FudgeMsg fieldData) {
    final String rootTicker = fieldData.getString(FIELD_TICKER);
    String exchangeCode = fieldData.getString(FIELD_EXCH_CODE);
    final String exchangeDescription = fieldData.getString(FIELD_PRIMARY_EXCHANGE_NAME);
    final String optionExerciseType = fieldData.getString(FIELD_OPT_EXERCISE_TYP);
    final double optionStrikePrice = fieldData.getDouble(FIELD_OPT_STRIKE_PX); // Bloomberg data in percent.
    final double pointValue = fieldData.getDouble(FIELD_FUT_VAL_PT);
    final String putOrCall = fieldData.getString(FIELD_OPT_PUT_CALL);
    final String underlingTicker = fieldData.getString(FIELD_OPT_UNDERLYING_SECURITY_DES);
    final String currency = fieldData.getString(FIELD_OPT_UNDL_CRNCY);
    final String expiryDate = fieldData.getString(FIELD_OPT_EXPIRE_DT);
    final String bbgUniqueID = fieldData.getString(FIELD_ID_BBG_UNIQUE);
    final String underlyingUniqueID = fieldData.getString(FIELD_UNDL_ID_BB_UNIQUE);
    final String secDes = fieldData.getString(FIELD_PARSEKYABLE_DES);

    if (!BloombergDataUtils.isValidField(bbgUniqueID)) {
      LOGGER.warn("bloomberg UniqueID is missing, cannot construct irFutureOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(rootTicker)) {
      LOGGER.warn("option root ticker is missing, cannot construct irFutureOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(underlyingUniqueID)) {
      LOGGER.warn("bloomberg UniqueID for Underlying Security is missing, cannot construct irFutureOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(putOrCall)) {
      LOGGER.warn("option type is missing, cannot construct irFutureOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(exchangeCode)) {
      LOGGER.warn("exchange is missing, cannot construct irFutureOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(expiryDate)) {
      LOGGER.warn("option expiry date is missing, cannot construct irFutureOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(underlingTicker)) {
      LOGGER.warn("option underlying ticker is missing, cannot construct irFutureOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(currency)) {
      LOGGER.warn("option currency is missing, cannot construct irFutureOption security");
      return null;
    }
    if (!BloombergDataUtils.isValidField(optionExerciseType)) {
      LOGGER.warn("option exercise type is missing, cannot construct irFutureOption security");
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
    if (BloombergDataUtils.isValidField(secDes)) {
      identifiers.add(ExternalSchemes.bloombergTickerSecurityId(secDes));
    }

    // currently we will pick up the unified bbg exchange code - we try to map to MIC via the description
    if (exchangeDescription != null) {
      final String exchangeMIC = EXCHANGE_DATA.getExchangeFromDescription(exchangeCode).getMic();
      if (exchangeMIC != null) {
        exchangeCode = exchangeMIC;
      }
    }

    final CommodityFutureOptionSecurity security = new CommodityFutureOptionSecurity(
        exchangeCode,
        exchangeCode,
        expiry,
        getExerciseType(optionExerciseType),
        buildUnderlyingTicker(underlingTicker),
        pointValue,
        ogCurrency,
        optionStrikePrice / 100, // Strike in percent //TODO: use normalization (like in BloombergRateClassifier)?
        optionType);
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
    return BLOOMBERG_FUTURE_OPTION_FIELDS;
  }

}
