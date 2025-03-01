/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.transport;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.fudgemsg.FudgeContext;
import org.fudgemsg.MutableFudgeMsg;
import org.testng.annotations.Test;

import com.opengamma.util.test.TestGroup;

/**
 * Test TaxonomyGatheringFudgeMessageSender.
 */
@Test(groups = TestGroup.INTEGRATION)
public class TaxonomyGatheringFudgeMessageSenderTest {

  /**
   * @throws IOException
   *           if there is a problem
   */
  public void noTaxonomyFileAvailableYet() throws IOException {
    final File tmpFile = File.createTempFile("TaxonomyGatheringFudgeMessageSenderTest_noTaxonomyFileAvailableYet", ".properties");
    FileUtils.forceDelete(tmpFile);
    FileUtils.forceDeleteOnExit(tmpFile);

    final FudgeContext context = new FudgeContext();
    final CollectingFudgeMessageReceiver collectingReceiver = new CollectingFudgeMessageReceiver();
    final ByteArrayFudgeMessageReceiver fudgeReceiver = new ByteArrayFudgeMessageReceiver(collectingReceiver);
    final DirectInvocationByteArrayMessageSender byteArraySender = new DirectInvocationByteArrayMessageSender(fudgeReceiver);
    final ByteArrayFudgeMessageSender fudgeSender = new ByteArrayFudgeMessageSender(byteArraySender, context);
    final TaxonomyGatheringFudgeMessageSender gatheringSender = new TaxonomyGatheringFudgeMessageSender(fudgeSender, tmpFile.getAbsolutePath());

    assertTrue(gatheringSender.getCurrentTaxonomy().isEmpty());
  }

  /**
   * @throws IOException
   *           if there is a problem
   * @throws InterruptedException
   *           if there is a problem
   */
  @Test(timeOut = 10000)
  public void taxonomyGathering() throws IOException, InterruptedException {
    final File tmpFile = File.createTempFile("TaxonomyGatheringFudgeMessageSenderTest_taxonomyGathering", ".properties");
    FileUtils.forceDelete(tmpFile);
    FileUtils.forceDeleteOnExit(tmpFile);

    final FudgeContext context = new FudgeContext();
    final CollectingFudgeMessageReceiver collectingReceiver = new CollectingFudgeMessageReceiver();
    final ByteArrayFudgeMessageReceiver fudgeReceiver = new ByteArrayFudgeMessageReceiver(collectingReceiver);
    final DirectInvocationByteArrayMessageSender byteArraySender = new DirectInvocationByteArrayMessageSender(fudgeReceiver);
    final ByteArrayFudgeMessageSender fudgeSender = new ByteArrayFudgeMessageSender(byteArraySender, context);
    final TaxonomyGatheringFudgeMessageSender gatheringSender = new TaxonomyGatheringFudgeMessageSender(fudgeSender, tmpFile.getAbsolutePath(), context, 1000L);

    final MutableFudgeMsg msg1 = context.newMessage();
    msg1.add("name1", 1);
    msg1.add("name2", 1);
    msg1.add("name3", 1);
    msg1.add("name1", 1);
    final MutableFudgeMsg msg2 = context.newMessage();
    msg1.add("name4", msg2);
    msg2.add(14, 1);
    msg2.add("name5", "foo");

    gatheringSender.send(msg1);

    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name1"));
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name2"));
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name3"));
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name4"));
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name5"));
    assertEquals(5, gatheringSender.getCurrentTaxonomy().size());

    final Properties props = new Properties();
    gatheringSender.waitForNextWrite();
    final InputStream is = new FileInputStream(tmpFile);
    props.load(new BufferedInputStream(is));
    is.close();

    for (final Map.Entry<Object, Object> propEntry : props.entrySet()) {
      final Integer ordinal = gatheringSender.getCurrentTaxonomy().get(propEntry.getValue());
      assertEquals(ordinal.intValue(), Integer.parseInt((String) propEntry.getKey()));
    }
    assertEquals(5, props.size());
  }

  /**
   * @throws IOException
   *           if there is a problem
   * @throws InterruptedException
   *           if there is a problem
   */
  @Test(timeOut = 20000)
  public void validFileLoadingOnStartup() throws IOException, InterruptedException {
    final File tmpFile = File.createTempFile("TaxonomyGatheringFudgeMessageSenderTest_validFileLoadingOnStartup", ".properties");
    FileUtils.forceDelete(tmpFile);
    FileUtils.forceDeleteOnExit(tmpFile);

    final FudgeContext context = new FudgeContext();
    final CollectingFudgeMessageReceiver collectingReceiver = new CollectingFudgeMessageReceiver();
    final ByteArrayFudgeMessageReceiver fudgeReceiver = new ByteArrayFudgeMessageReceiver(collectingReceiver);
    final DirectInvocationByteArrayMessageSender byteArraySender = new DirectInvocationByteArrayMessageSender(fudgeReceiver);
    final ByteArrayFudgeMessageSender fudgeSender = new ByteArrayFudgeMessageSender(byteArraySender, context);
    TaxonomyGatheringFudgeMessageSender gatheringSender = new TaxonomyGatheringFudgeMessageSender(fudgeSender, tmpFile.getAbsolutePath(), context, 1000L);

    final MutableFudgeMsg msg1 = context.newMessage();
    msg1.add("name1", 1);
    msg1.add("name2", 1);
    msg1.add("name3", 1);
    msg1.add("name1", 1);
    final MutableFudgeMsg msg2 = context.newMessage();
    msg1.add("name4", msg2);
    msg2.add(14, 1);
    msg2.add("name5", "foo");

    gatheringSender.send(msg1);
    gatheringSender.waitForNextWrite();
    gatheringSender.getTimer().cancel();

    // Now reload the file.
    gatheringSender = new TaxonomyGatheringFudgeMessageSender(fudgeSender, tmpFile.getAbsolutePath(), context, 5000L);
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name1"));
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name2"));
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name3"));
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name4"));
    assertTrue(gatheringSender.getCurrentTaxonomy().containsKey("name5"));
    assertEquals(5, gatheringSender.getCurrentTaxonomy().size());
  }

}
