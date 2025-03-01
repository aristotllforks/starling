/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.depgraph.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.fudgemsg.FudgeField;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeBuilder;
import org.fudgemsg.mapping.FudgeBuilderFor;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.engine.depgraph.DependencyGraph;
import com.opengamma.engine.depgraph.ResolutionFailure;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;

/**
 * Fudge builder for {@link DependencyGraphBuildTrace} objects.
 */
@FudgeBuilderFor(DependencyGraphBuildTrace.class)
public class DependencyGraphBuildTraceFudgeBuilder implements FudgeBuilder<DependencyGraphBuildTrace> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DependencyGraphBuildTraceFudgeBuilder.class);

  @Override
  public MutableFudgeMsg buildMessage(final FudgeSerializer serializer, final DependencyGraphBuildTrace object) {
    final MutableFudgeMsg result = serializer.newMessage();
    final DependencyGraph graph = object.getDependencyGraph();
    serializer.addToMessage(result, "dependencyGraph", null, graph);
    final Map<Throwable, Integer> exceptions = object.getExceptionsWithCounts();
    for (final Map.Entry<Throwable, Integer> exception : exceptions.entrySet()) {
      final MutableFudgeMsg submessage = serializer.newMessage();
      submessage.add("class", exception.getKey().getClass().getName());
      submessage.add("message", exception.getKey().getMessage());
      if (exception.getValue() > 1) {
        submessage.add("repeat", exception.getValue());
      }
      result.add("exception", submessage);
    }
    for (final ResolutionFailure failure : object.getFailures()) {
      serializer.addToMessage(result, "failure", null, failure);
    }
    serializer.addToMessage(result, "mapping", null, object.getMappings());
    return result;
  }

  @Override
  public DependencyGraphBuildTrace buildObject(final FudgeDeserializer deserializer, final FudgeMsg message) {
    final DependencyGraph dependencyGraph = deserializer.fudgeMsgToObject(DependencyGraph.class, message.getMessage("dependencyGraph"));
    final Map<Throwable, Integer> exceptionsWithCounts = new LinkedHashMap<>();
    final List<FudgeField> exceptionSubMessages = message.getAllByName("exception");
    for (final FudgeField field : exceptionSubMessages) {
      final FudgeMsg subMessage = (FudgeMsg) field.getValue();
      final String clazzName = subMessage.getString("class");
      final String exceptionMessage = subMessage.getString("message");
      Throwable throwable;
      try {
        throwable = new ThrowableWithClass(exceptionMessage, Class.forName(clazzName));
      } catch (final ClassNotFoundException ex) {
        throwable = new ThrowableWithClass(exceptionMessage, null);
        LOGGER.error("Exception class not found, setting exception class to null");
      }
      if (subMessage.hasField("repeat")) {
        final Integer repeat = subMessage.getInt("repeat");
        if (repeat == null) {
          LOGGER.error("repeat field was present, but not integer");
        }
        exceptionsWithCounts.put(throwable, repeat);
      } else {
        exceptionsWithCounts.put(throwable, 1);
      }
    }
    final List<ResolutionFailure> failures = new ArrayList<>();
    final List<FudgeField> failureMessages = message.getAllByName("failure");
    for (final FudgeField field : failureMessages) {
      final FudgeMsg subMessage = (FudgeMsg) field.getValue();
      final ResolutionFailure failure = deserializer.fudgeMsgToObject(ResolutionFailure.class, subMessage);
      if (failure == null) {
        LOGGER.error("Couldn't deserialize failure " + subMessage.toString());
      }
      failures.add(failure);
    }
    final Map<ValueRequirement, ValueSpecification> mappings = new HashMap<>();
    final FudgeField mapping = message.getByName("mapping");
    final FudgeMsg mappingMsg = (FudgeMsg) mapping.getValue();
    final List<FudgeField> keys = mappingMsg.getAllByOrdinal(1); // keys
    final List<FudgeField> values = mappingMsg.getAllByOrdinal(2); // values
    if (keys.size() != values.size()) {
      LOGGER.error("keys and values list in map don't have the same number of elements: message = {}", message);
      throw new OpenGammaRuntimeException("keys and values list in map don't have the same number of elements");
    }
    for (int i = 0; i < keys.size(); i++) { // better to use iterators?
      final FudgeField reqField = keys.get(i);
      final ValueRequirement valueReq = deserializer.fieldValueToObject(ValueRequirement.class, reqField);
      final FudgeField specField = values.get(i);
      final ValueSpecification valueSpec = deserializer.fieldValueToObject(ValueSpecification.class, specField);
      if (valueReq == null || valueSpec == null) {
        LOGGER.error("valueReq or valueSpec was null during deserialize: message = {}", message);
        throw new OpenGammaRuntimeException("valueReq or valueSpec was null during deserialize");
      }
      mappings.put(valueReq, valueSpec);
    }
    return DependencyGraphBuildTrace.of(dependencyGraph, exceptionsWithCounts, failures, mappings);
  }

  /**
   * Minimal wrapper for throwables sent over network.
   */
  public static class ThrowableWithClass extends Throwable {
    private static final long serialVersionUID = 1L;

    private final Class<?> _sourceClass;

    public ThrowableWithClass(final String message, final Class<?> sourceClass) {
      super(message);

      _sourceClass = sourceClass;
    }

    public Class<?> getSourceClass() {
      return _sourceClass;
    }

    // note - hc and eq defined for purpose of unit testing fudge builder.
    // Throwables don't normally define these.
    @Override
    public int hashCode() {
      return new HashCodeBuilder()
          .append(_sourceClass)
          .append(getMessage())
          .hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == null || obj.getClass() != getClass()) {
        return false;
      }
      final ThrowableWithClass other = (ThrowableWithClass) obj;
      return new EqualsBuilder()
          .append(_sourceClass, other._sourceClass)
          .append(getMessage(), other.getMessage())
          .isEquals();
    }

  }

}
