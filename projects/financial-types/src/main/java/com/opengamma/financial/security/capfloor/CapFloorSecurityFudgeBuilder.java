/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.capfloor;

import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeBuilder;
import org.fudgemsg.mapping.FudgeBuilderFor;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;

import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.security.FinancialSecurityFudgeBuilder;
import com.opengamma.id.ExternalIdFudgeBuilder;
import com.opengamma.util.fudgemsg.AbstractFudgeBuilder;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.ZonedDateTimeFudgeBuilder;

/**
 * A Fudge builder for {@code CapFloorSecurity}.
 */
@FudgeBuilderFor(CapFloorSecurity.class)
public class CapFloorSecurityFudgeBuilder extends AbstractFudgeBuilder implements FudgeBuilder<CapFloorSecurity> {

  /** Field name. */
  public static final String START_DATE_FIELD_NAME = "startDate";
  /** Field name. */
  public static final String MATURITY_DATE_FIELD_NAME = "maturityDate";
  /** Field name. */
  public static final String NOTIONAL_FIELD_NAME = "notional";
  /** Field name. */
  public static final String UNDERLYING_IDENTIFIER_FIELD_NAME = "underlyingIdentifier";
  /** Field name. */
  public static final String STRIKE_FIELD_NAME = "strike";
  /** Field name. */
  public static final String FREQUENCY_FIELD_NAME = "frequency";
  /** Field name. */
  public static final String CURRENCY_FIELD_NAME = "currency";
  /** Field name. */
  public static final String DAY_COUNT_FIELD_NAME = "dayCount";
  /** Field name. */
  public static final String IS_PAYER_FIELD_NAME = "isPayer";
  /** Field name. */
  public static final String IS_CAP_FIELD_NAME = "isCap";
  /** Field name. */
  public static final String IS_IBOR_FIELD_NAME = "isIbor";

  @Override
  public MutableFudgeMsg buildMessage(final FudgeSerializer serializer, final CapFloorSecurity object) {
    final MutableFudgeMsg msg = serializer.newMessage();
    CapFloorSecurityFudgeBuilder.toFudgeMsg(serializer, object, msg);
    return msg;
  }

  public static void toFudgeMsg(final FudgeSerializer serializer, final CapFloorSecurity object, final MutableFudgeMsg msg) {
    FinancialSecurityFudgeBuilder.toFudgeMsg(serializer, object, msg);
    addToMessage(msg, START_DATE_FIELD_NAME, ZonedDateTimeFudgeBuilder.toFudgeMsg(serializer, object.getStartDate()));
    addToMessage(msg, MATURITY_DATE_FIELD_NAME, ZonedDateTimeFudgeBuilder.toFudgeMsg(serializer, object.getMaturityDate()));
    addToMessage(msg, NOTIONAL_FIELD_NAME, object.getNotional());
    addToMessage(msg, UNDERLYING_IDENTIFIER_FIELD_NAME, ExternalIdFudgeBuilder.toFudgeMsg(serializer, object.getUnderlyingId()));
    addToMessage(msg, STRIKE_FIELD_NAME, object.getStrike());
    addToMessage(msg, FREQUENCY_FIELD_NAME, object.getFrequency());
    addToMessage(msg, CURRENCY_FIELD_NAME, object.getCurrency());
    addToMessage(msg, DAY_COUNT_FIELD_NAME, object.getDayCount());
    addToMessage(msg, IS_PAYER_FIELD_NAME, object.isPayer());
    addToMessage(msg, IS_CAP_FIELD_NAME, object.isCap());
    addToMessage(msg, IS_IBOR_FIELD_NAME, object.isIbor());
  }

  @Override
  public CapFloorSecurity buildObject(final FudgeDeserializer deserializer, final FudgeMsg msg) {
    final CapFloorSecurity object = new CapFloorSecurity();
    CapFloorSecurityFudgeBuilder.fromFudgeMsg(deserializer, msg, object);
    return object;
  }

  public static void fromFudgeMsg(final FudgeDeserializer deserializer, final FudgeMsg msg, final CapFloorSecurity object) {
    FinancialSecurityFudgeBuilder.fromFudgeMsg(deserializer, msg, object);
    object.setStartDate(ZonedDateTimeFudgeBuilder.fromFudgeMsg(deserializer, msg.getMessage(START_DATE_FIELD_NAME)));
    object.setMaturityDate(ZonedDateTimeFudgeBuilder.fromFudgeMsg(deserializer, msg.getMessage(MATURITY_DATE_FIELD_NAME)));
    object.setNotional(msg.getDouble(NOTIONAL_FIELD_NAME));
    object.setUnderlyingId(ExternalIdFudgeBuilder.fromFudgeMsg(deserializer, msg.getMessage(UNDERLYING_IDENTIFIER_FIELD_NAME)));
    object.setStrike(msg.getDouble(STRIKE_FIELD_NAME));
    object.setFrequency(msg.getValue(Frequency.class, FREQUENCY_FIELD_NAME));
    object.setCurrency(msg.getValue(Currency.class, CURRENCY_FIELD_NAME));
    object.setDayCount(msg.getValue(DayCount.class, DAY_COUNT_FIELD_NAME));
    object.setPayer(msg.getBoolean(IS_PAYER_FIELD_NAME));
    object.setCap(msg.getBoolean(IS_CAP_FIELD_NAME));
    object.setIbor(msg.getBoolean(IS_IBOR_FIELD_NAME));
  }

}
