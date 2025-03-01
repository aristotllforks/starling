/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.util.rest;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeField;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeDeserializer;

import com.opengamma.util.fudgemsg.OpenGammaFudgeContext;

/**
 * Utility methods to simplify RESTful work.
 * <p>
 * This is a thread-safe static utility class.
 */
public final class RestUtils {

  /**
   * text/csv.
   */
  public static final String TEXT_CSV = "text/csv";

  /**
   * text/csv type.
   */
  public static final MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

  /**
   * Restricted constructor.
   */
  private RestUtils() {
  }

  //-------------------------------------------------------------------------
  /**
   * Encodes an object into base-64 suitable for a URI.
   * <p>
   * The conversion uses Fudge, thus the object must be convertible to/from Fudge.
   *
   * @param object  the object to encode, not null
   * @return  the encoded version of the object, not null
   */
  public static String encodeBase64(final Object object) {
    final FudgeContext context = OpenGammaFudgeContext.getInstance();
    final FudgeMsg msg = context.toFudgeMsg(object).getMessage();
    final byte[] byteArray = context.toByteArray(msg);
    return Base64.encodeBase64URLSafeString(byteArray);
  }

  //-------------------------------------------------------------------------
  /**
   * Decodes an object from base-64 such as when passed in the URI.
   * <p>
   * The conversion uses Fudge, thus the object must be convertible to/from Fudge.
   *
   * @param <T> the bean type
   * @param type  the bean type to build, not null
   * @param msgBase64  the base-64 Fudge message, not null
   * @return the bean, not null
   */
  public static <T> T decodeBase64(final Class<T> type, final String msgBase64) {
    if (msgBase64 == null) {
      try {
        return type.newInstance();
      } catch (final InstantiationException ex) {
        throw new RuntimeException(ex);
      } catch (final IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
    }
    final byte[] msg = Base64.decodeBase64(msgBase64);
    final FudgeContext context = OpenGammaFudgeContext.getInstance();
    final FudgeMsg message = context.createMessageReader(new ByteArrayInputStream(msg)).nextMessage();
    if (message == null) {
      return null;
    }
    final FudgeDeserializer deser = new FudgeDeserializer(context);
    return deser.fudgeMsgToObject(type, message);
  }

  //-------------------------------------------------------------------------
  /**
   * Encode an object to query parameters.
   * <p>
   * The conversion uses Fudge, thus the object must be convertible to/from Fudge.
   *
   * @param bld  the URI builder, not null
   * @param object  the object to encode, not null
   */
  public static void encodeQueryParams(final UriBuilder bld, final Object object) {
    final FudgeContext context = OpenGammaFudgeContext.getInstance();
    final FudgeMsg msg = context.toFudgeMsg(object).getMessage();
    encode(bld, msg, "");
  }

  private static void encode(final UriBuilder bld, final FudgeMsg msg, final String keyPrefix) {
    for (final FudgeField field : msg) {
      if (field.getName() != null) {
        final String name = keyPrefix + field.getName();
        final Object value = field.getValue();
        if (value instanceof FudgeMsg) {
          final FudgeMsg subMsg = (FudgeMsg) value;
          encode(bld, subMsg, name + ".");
        } else {
          bld.queryParam(name, value.toString());
        }
      }
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Decodes query parameters to a bean.
   * <p>
   * The conversion uses Fudge, thus the object must be convertible to/from Fudge.
   *
   * @param <T> the bean type
   * @param uriInfo  the uri information, not null
   * @param type  the bean type to build, not null
   * @return the bean, not null
   */
  public static <T> T decodeQueryParams(final UriInfo uriInfo, final Class<T> type) {
    final FudgeContext context = OpenGammaFudgeContext.getInstance();
    final MutableFudgeMsg msg = context.newMessage();
    for (final Entry<String, List<String>> entry : uriInfo.getQueryParameters().entrySet()) {
      final String key = entry.getKey();
      final List<String> values = entry.getValue();
      for (final String value : values) {
        decode(msg, key, value);
      }
    }
    final FudgeDeserializer deser = new FudgeDeserializer(context);
    return deser.fudgeMsgToObject(type, msg);
  }

  private static void decode(final MutableFudgeMsg msg, final String key, final String value) {
    if (key.contains(".")) {
      final String key1 = StringUtils.substringBefore(key, ".");
      final String key2 = StringUtils.substringAfter(key, ".");
      final MutableFudgeMsg subMsg = msg.ensureSubMessage(key1, null);
      decode(subMsg, key2, value);
    } else {
      msg.add(key, value);
    }
  }

}
