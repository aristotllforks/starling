/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.bbg.loader;

import static com.opengamma.bbg.BloombergConstants.BLOOMBERG_FINANCIAL_COMMODITY_OPTION_TYPE;
import static com.opengamma.bbg.BloombergConstants.BLOOMBERG_INTEREST_RATE_TYPE;
import static com.opengamma.bbg.BloombergConstants.FIELD_EXCH_CODE;
import static com.opengamma.bbg.BloombergConstants.FIELD_FUT_VAL_PT;
import static com.opengamma.bbg.BloombergConstants.FIELD_ID_BBG_UNIQUE;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_EXERCISE_TYP;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_EXPIRE_DT;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_PUT_CALL;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_STRIKE_PX;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_TICK_VAL;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_UNDERLYING_SECURITY_DES;
import static com.opengamma.bbg.BloombergConstants.FIELD_OPT_UNDL_CRNCY;
import static com.opengamma.bbg.BloombergConstants.FIELD_PARSEKYABLE_DES;
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
import com.opengamma.financial.security.option.IRFutureOptionSecurity;
import com.opengamma.financial.security.option.OptionType;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.master.security.ManageableSecurity;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.DateUtils;
import com.opengamma.util.time.Expiry;

/**
 * Loads the data for an InterestRate Future Option from Bloomberg.
 */
public class IRFutureOptionLoader extends SecurityLoader {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(IRFutureOptionLoader.class);
  /**
   * The fields to load from Bloomberg.
   */
  private static final Set<String> BLOOMBERG_IR_FUTURE_OPTION_FIELDS = ImmutableSet.of(
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
      FIELD_OPT_TICK_VAL,
      FIELD_FUT_VAL_PT,
      FIELD_UNDL_ID_BB_UNIQUE);

  /**
   * The valid Bloomberg security types for Interest Rate Future Option.
   */
  public static final Set<String> VALID_SECURITY_TYPES = ImmutableSet.of(
      BLOOMBERG_INTEREST_RATE_TYPE, BLOOMBERG_FINANCIAL_COMMODITY_OPTION_TYPE);

  /**
   * Creates an instance.
   * 
   * @param referenceDataProvider
   *          the provider, not null
   */
  public IRFutureOptionLoader(final ReferenceDataProvider referenceDataProvider) {
    super(LOGGER, referenceDataProvider, SecurityType.IR_FUTURE_OPTION);
  }

  // -------------------------------------------------------------------------
  @Override
  protected ManageableSecurity createSecurity(final FudgeMsg fieldData) {
    final String rootTicker = fieldData.getString(FIELD_TICKER);
    final String exchangeCode = fieldData.getString(FIELD_EXCH_CODE);
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

    final IRFutureOptionSecurity security = new IRFutureOptionSecurity(
        exchangeCode,
        expiry,
        getExerciseType(optionExerciseType),
        buildUnderlyingTicker(underlingTicker),
        pointValue,
        getIsMargined(exchangeCode),
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

  private boolean getIsMargined(final String exchangeCode) {
    // Yomi put this in on direction from mark.
    if (exchangeCode.equalsIgnoreCase("CME")) {
      return false;
    } else if (exchangeCode.equalsIgnoreCase("LIF") || exchangeCode.equalsIgnoreCase("EUX")) {
      return true;
    } else {
      throw new OpenGammaRuntimeException("cannot work out isMargined from exchangeCode " + exchangeCode);
    }
  }

  @Override
  protected Set<String> getBloombergFields() {
    return BLOOMBERG_IR_FUTURE_OPTION_FIELDS;
  }

}
