/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.regression;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.xml.stream.XMLStreamWriter;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;
import org.fudgemsg.wire.FudgeMsgReader;
import org.fudgemsg.wire.FudgeMsgWriter;
import org.fudgemsg.wire.xml.FudgeXMLStreamReader;
import org.fudgemsg.wire.xml.FudgeXMLStreamWriter;

import com.google.common.base.Throwables;
import com.opengamma.util.fudgemsg.OpenGammaFudgeContext;
import com.opengamma.util.xml.FormattingXmlStreamWriter;

import net.sf.saxon.s9api.Processor;

/**
 *
 */
public class GoldenCopyPersistenceHelper {

  /**
   * Relative path to golden copy dir
   */
  private static final FudgeContext FUDGE_CONTEXT = OpenGammaFudgeContext.getInstance();

  private static final String GOLDEN_COPY_SUBDIR = "golden_copy";

  private final File _goldenCopyDir;

  /**
   * Create a persistence helper, specifying the root of the regression dir.
   * @param regressionDir the root of the regression directory. (i.e. the one
   * holding the dbdump and golden_copy directories).
   */
  public GoldenCopyPersistenceHelper(final File regressionDir) {
    _goldenCopyDir = new File(regressionDir, GOLDEN_COPY_SUBDIR);
  }

  public GoldenCopy load(final String viewName, final String snapshotName) {
    final String name = buildFilename(viewName, snapshotName);

    try (FileReader reader = new FileReader(new File(_goldenCopyDir, name));
        FudgeMsgReader fudgeMessageReader = new FudgeMsgReader(new FudgeXMLStreamReader(FUDGE_CONTEXT, reader));) {
      final FudgeMsg nextMessage = fudgeMessageReader.nextMessage();
      final FudgeDeserializer deser = new FudgeDeserializer(FUDGE_CONTEXT);
      return deser.fudgeMsgToObject(GoldenCopy.class, nextMessage);
    } catch (final Exception ex) {
      throw Throwables.propagate(ex);
    }

  }


  public void save(final GoldenCopy goldenCopy) {
    final FudgeSerializer serializer = new FudgeSerializer(FUDGE_CONTEXT);
    final String viewName = goldenCopy.getViewName();
    final String snapshotName = goldenCopy.getSnapshotName();
    final String name = buildFilename(viewName, snapshotName);
    final Processor p = new Processor(false);

    if (!_goldenCopyDir.exists()) {
      final boolean createdDirOk = _goldenCopyDir.mkdirs();
      if (!createdDirOk) {
        throw new IllegalStateException("Unable to create dir: " + _goldenCopyDir);
      }
    }

    try (FileWriter writer = new FileWriter(new File(_goldenCopyDir, name));
        FudgeMsgWriter fudgeMsgWriter = createMsgWriter(p, writer);
        ) {
      final MutableFudgeMsg msg = serializer.objectToFudgeMsg(goldenCopy);
      fudgeMsgWriter.writeMessage(msg);
      writer.append("\n");
      fudgeMsgWriter.flush();
    } catch (final Exception ex) {
      throw Throwables.propagate(ex);
    }

  }

  private FudgeMsgWriter createMsgWriter(final Processor p, final FileWriter writer) {

    final XMLStreamWriter xmlStreamWriter = FormattingXmlStreamWriter.builder(writer)
                                                               .indent(true)
                                                               .build();

    final FudgeXMLStreamWriter streamWriter = new FudgeXMLStreamWriter(FUDGE_CONTEXT, xmlStreamWriter);
    final FudgeMsgWriter fudgeMsgWriter = new FudgeMsgWriter(streamWriter);

    return fudgeMsgWriter;
  }

  private String buildFilename(final String viewName, final String snapshotName) {
    final String name = viewName + "." + snapshotName + ".xml";
    return name;
  }


}
