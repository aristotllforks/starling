/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.security.auditlog;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.opengamma.util.test.TestGroup;

/**
 * Tests {@link DuplicateFilteringAuditLogger}.
 */
@Test(groups = TestGroup.INTEGRATION, invocationCount = 4)
public class DuplicateFilteringAuditLoggerTest {

  /**
   * Tests that duplicates are filtered.
   *
   * @throws InterruptedException  if the logger is interrupted
   */
  public void testDuplicateFiltering() throws InterruptedException {
    final InMemoryAuditLogger memoryLogger = new InMemoryAuditLogger();

    final int seconds = 3; //Big enough to make sure writes complete, short enough that we can wait for timeout

    final DuplicateFilteringAuditLogger filteringLogger = new DuplicateFilteringAuditLogger(memoryLogger, 1000, seconds);

    log5Messages(filteringLogger);
    assertEquals(5, memoryLogger.getMessages().size());

    log5Messages(filteringLogger);
    assertEquals(5, memoryLogger.getMessages().size());

    Thread.sleep(seconds * 2 * 1000); //Long enough to expire everything

    log5Messages(filteringLogger);
    assertEquals(10, memoryLogger.getMessages().size());

    log5Messages(filteringLogger);
    assertEquals(10, memoryLogger.getMessages().size());
  }

  private static void log5Messages(final DuplicateFilteringAuditLogger filteringLogger) {
    filteringLogger.log("alice", "obj1", "op1", true);
    filteringLogger.log("lisa", "obj1", "op1", true);
    filteringLogger.log("alice", "obj2", "op1", true);
    filteringLogger.log("alice", "obj1", "op2", true);
    filteringLogger.log("alice", "obj1", "op1", false);
  }

}
