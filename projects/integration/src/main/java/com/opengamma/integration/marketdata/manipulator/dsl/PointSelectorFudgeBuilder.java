/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.util.Set;
import java.util.regex.Pattern;

import org.fudgemsg.FudgeField;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeBuilder;
import org.fudgemsg.mapping.FudgeBuilderFor;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;

import com.google.common.collect.Sets;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalScheme;

@FudgeBuilderFor(PointSelector.class)
public class PointSelectorFudgeBuilder implements FudgeBuilder<PointSelector> {

  /** Field name for Fudge message. */
  private static final String CALC_CONFIGS = "calculationConfigurationNames";
  /** Field name for Fudge message. */
  private static final String IDS = "ids";
  /** Field name for Fudge message. */
  private static final String ID_MATCH_SCHEME = "idMatchScheme";
  /** Field name for Fudge message. */
  private static final String ID_MATCH_PATTERN = "idMatchPattern";
  /** Field name for Fudge message. */
  private static final String ID_LIKE_SCHEME = "idLikeScheme";
  /** Field name for Fudge message. */
  private static final String ID_LIKE_PATTERN = "idLikePattern";
  /** Field name for Fudge message. */
  private static final String SECURITY_TYPES = "securityTypes";

  @Override
  public MutableFudgeMsg buildMessage(final FudgeSerializer serializer, final PointSelector selector) {
    final MutableFudgeMsg msg = serializer.newMessage();
    final MutableFudgeMsg calcConfigsMsg = serializer.newMessage();
    if (selector.getCalculationConfigurationNames() != null) {
      for (final String calcConfigName : selector.getCalculationConfigurationNames()) {
        serializer.addToMessage(calcConfigsMsg, null, null, calcConfigName);
      }
      serializer.addToMessage(msg, CALC_CONFIGS, null, calcConfigsMsg);
    }
    if (selector.getIds() != null) {
      final MutableFudgeMsg idsMsg = serializer.newMessage();
      for (final ExternalId id : selector.getIds()) {
        serializer.addToMessage(idsMsg, null, null, id);
      }
      serializer.addToMessage(msg, IDS, null, idsMsg);
    }
    if (selector.getIdMatchScheme() != null) {
      serializer.addToMessage(msg, ID_MATCH_SCHEME, null, selector.getIdMatchScheme().toString());
    }
    if (selector.getIdMatchPattern() != null) {
      serializer.addToMessage(msg, ID_MATCH_PATTERN, null, selector.getIdMatchPattern().pattern());
    }
    if (selector.getIdLikeScheme() != null) {
      serializer.addToMessage(msg, ID_LIKE_SCHEME, null, selector.getIdLikeScheme().toString());
    }
    if (selector.getIdLikePattern() != null) {
      serializer.addToMessage(msg, ID_LIKE_PATTERN, null, selector.getIdLikePattern().pattern());
    }
    if (selector.getSecurityTypes() != null) {
      final MutableFudgeMsg securityTypesMsg = serializer.newMessage();
      for (final String securityType : selector.getSecurityTypes()) {
        serializer.addToMessage(securityTypesMsg, null, null, securityType);
      }
      serializer.addToMessage(msg, SECURITY_TYPES, null, securityTypesMsg);
    }
    return msg;
  }

  @Override
  public PointSelector buildObject(final FudgeDeserializer deserializer, final FudgeMsg msg) {
    Set<ExternalId> ids;
    if (msg.hasField(IDS)) {
      ids = Sets.newHashSet();
      final FudgeMsg idsMsg = msg.getMessage(IDS);
      for (final FudgeField field : idsMsg) {
        final ExternalId id = deserializer.fieldValueToObject(ExternalId.class, field);
        ids.add(id);
      }
    } else {
      ids = null;
    }

    ExternalScheme idMatchScheme;
    if (msg.hasField(ID_MATCH_SCHEME)) {
      final String idMatchSchemeStr = deserializer.fieldValueToObject(String.class, msg.getByName(ID_MATCH_SCHEME));
      idMatchScheme = ExternalScheme.of(idMatchSchemeStr);
    } else {
      idMatchScheme = null;
    }

    Pattern idMatchPattern;
    if (msg.hasField(ID_MATCH_PATTERN)) {
      final String idMatchPatternStr = deserializer.fieldValueToObject(String.class, msg.getByName(ID_MATCH_PATTERN));
      idMatchPattern = Pattern.compile(idMatchPatternStr);
    } else {
      idMatchPattern = null;
    }

    ExternalScheme idLikeScheme;
    if (msg.hasField(ID_LIKE_SCHEME)) {
      final String idLikeSchemeStr = deserializer.fieldValueToObject(String.class, msg.getByName(ID_LIKE_SCHEME));
      idLikeScheme = ExternalScheme.of(idLikeSchemeStr);
    } else {
      idLikeScheme = null;
    }

    Pattern idLikePattern;
    if (msg.hasField(ID_LIKE_PATTERN)) {
      final String idLikePatternStr = deserializer.fieldValueToObject(String.class, msg.getByName(ID_LIKE_PATTERN));
      idLikePattern = Pattern.compile(idLikePatternStr);
    } else {
      idLikePattern = null;
    }

    Set<String> calcConfigNames;
    if (msg.hasField(CALC_CONFIGS)) {
      calcConfigNames = Sets.newHashSet();
      final FudgeMsg calcConfigsMsg = msg.getMessage(CALC_CONFIGS);
      for (final FudgeField field : calcConfigsMsg) {
        calcConfigNames.add(deserializer.fieldValueToObject(String.class, field));
      }
    } else {
      calcConfigNames = null;
    }

    Set<String> securityTypes;
    if (msg.hasField(SECURITY_TYPES)) {
      securityTypes = Sets.newHashSet();
      final FudgeMsg securityTypesMsg = msg.getMessage(SECURITY_TYPES);
      for (final FudgeField field : securityTypesMsg) {
        final String securityType = deserializer.fieldValueToObject(String.class, field);
        securityTypes.add(securityType);
      }
    } else {
      securityTypes = null;
    }
    return new PointSelector(calcConfigNames, ids, idMatchScheme, idMatchPattern, idLikeScheme, idLikePattern, securityTypes);
  }
}
