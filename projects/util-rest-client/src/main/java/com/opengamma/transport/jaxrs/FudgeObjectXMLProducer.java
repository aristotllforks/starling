/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.transport.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.fudgemsg.FudgeMsg;
import org.fudgemsg.FudgeMsgEnvelope;
import org.fudgemsg.wire.FudgeMsgWriter;
import org.fudgemsg.wire.xml.FudgeXMLStreamWriter;
import org.joda.beans.Bean;

import com.google.common.base.Charsets;

/**
 * A JAX-RS provider to convert RESTful responses to Fudge XML encoded messages.
 * <p>
 * This converts directly to Fudge from the RESTful resource without the need to manually
 * create the message in application code.
 */
@Provider
@Produces(MediaType.APPLICATION_XML)
public class FudgeObjectXMLProducer extends FudgeBase implements MessageBodyWriter<Object> {

  /**
   * Creates the producer.
   */
  public FudgeObjectXMLProducer() {
    super();
  }

  //-------------------------------------------------------------------------
  @Override
  public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
    return FudgeRest.MEDIA_TYPE.equals(mediaType)
        || type == FudgeResponse.class || Bean.class.isAssignableFrom(type)
        || FudgeMsgEnvelope.class.isAssignableFrom(type) || FudgeMsg.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(final Object obj, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(
      final Object obj,
      final Class<?> type,
      final Type genericType,
      final Annotation[] annotations,
      final MediaType mediaType,
      final MultivaluedMap<String, Object> httpHeaders,
      final OutputStream entityStream) throws IOException, WebApplicationException {

    FudgeMsgEnvelope msg;
    if (obj instanceof FudgeResponse) {
      final FudgeResponse wrapper = (FudgeResponse) obj;
      msg = getFudgeContext().toFudgeMsg(wrapper.getValue());
    } else if (obj instanceof FudgeMsgEnvelope) {
      msg = (FudgeMsgEnvelope) obj;
    } else if (obj instanceof FudgeMsg) {
      msg = new FudgeMsgEnvelope((FudgeMsg) obj);
    } else {
      msg = getFudgeContext().toFudgeMsg(obj);
    }

    final OutputStreamWriter entityWriter = new OutputStreamWriter(entityStream, Charsets.UTF_8);
    @SuppressWarnings("resource")
    final FudgeMsgWriter writer = new FudgeMsgWriter(new FudgeXMLStreamWriter(getFudgeContext(), entityWriter));
    writer.writeMessageEnvelope(msg, getFudgeTaxonomyId());
    writer.flush();
  }

}
