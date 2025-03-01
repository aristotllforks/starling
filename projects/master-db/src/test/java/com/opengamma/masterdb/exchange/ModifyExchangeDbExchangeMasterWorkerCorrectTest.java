/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.exchange;

import static org.testng.AssertJUnit.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.threeten.bp.Instant;

import com.opengamma.DataNotFoundException;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.UniqueId;
import com.opengamma.master.exchange.ExchangeDocument;
import com.opengamma.master.exchange.ExchangeHistoryRequest;
import com.opengamma.master.exchange.ExchangeHistoryResult;
import com.opengamma.master.exchange.ManageableExchange;
import com.opengamma.util.test.DbTest;
import com.opengamma.util.test.TestGroup;

/**
 * Tests ModifyExchangeDbExchangeMasterWorker.
 */
@Test(groups = TestGroup.UNIT_DB)
public class ModifyExchangeDbExchangeMasterWorkerCorrectTest extends AbstractDbExchangeMasterWorkerTest {
  // superclass sets up dummy database

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyExchangeDbExchangeMasterWorkerCorrectTest.class);
  private static final ExternalIdBundle BUNDLE = ExternalIdBundle.of("A", "B");
  private static final ExternalIdBundle REGION = ExternalIdBundle.of("C", "D");

  @Factory(dataProvider = "databases", dataProviderClass = DbTest.class)
  public ModifyExchangeDbExchangeMasterWorkerCorrectTest(final String databaseType, final String databaseVersion) {
    super(databaseType, databaseVersion, false);
    LOGGER.info("running testcases for {}", databaseType);
  }

  //-------------------------------------------------------------------------
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_correctExchange_nullDocument() {
    _exgMaster.correct(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_correct_noExchangeId() {
    final UniqueId uniqueId = UniqueId.of("DbExg", "101");
    final ManageableExchange exchange = new ManageableExchange(BUNDLE, "Test", REGION, null);
    exchange.setUniqueId(uniqueId);
    final ExchangeDocument doc = new ExchangeDocument(exchange);
    doc.setUniqueId(null);
    _exgMaster.correct(doc);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_correct_noExchange() {
    final ExchangeDocument doc = new ExchangeDocument();
    doc.setUniqueId(UniqueId.of("DbExg", "101", "0"));
    _exgMaster.correct(doc);
  }

  @Test(expectedExceptions = DataNotFoundException.class)
  public void test_correct_notFound() {
    final UniqueId uniqueId = UniqueId.of("DbExg", "0", "0");
    final ManageableExchange exchange = new ManageableExchange(BUNDLE, "Test", REGION, null);
    exchange.setUniqueId(uniqueId);
    final ExchangeDocument doc = new ExchangeDocument(exchange);
    _exgMaster.correct(doc);
  }

//  @Test(expected = IllegalArgumentException.class)
//  public void test_correct_notLatestCorrection() {
//    UniqueId uniqueId = UniqueId.of("DbExg", "201", "0");
//    ManageableExchange exchange = new ManageableExchange(uniqueId, "Name", "Type", ExternalIdBundle.of("A", "B"));
//    ExchangeDocument doc = new ExchangeDocument(exchange);
//    _worker.correct(doc);
//  }

  @Test
  public void test_correct_getUpdateGet() {
    final Instant now = Instant.now(_exgMaster.getClock());

    final UniqueId uniqueId = UniqueId.of("DbExg", "101", "0");
    final ExchangeDocument base = _exgMaster.get(uniqueId);
    final ManageableExchange exchange = new ManageableExchange(BUNDLE, "Test", REGION, null);
    exchange.setUniqueId(uniqueId);
    final ExchangeDocument input = new ExchangeDocument(exchange);

    final ExchangeDocument corrected = _exgMaster.correct(input);
    assertEquals(false, base.getUniqueId().equals(corrected.getUniqueId()));
    assertEquals(base.getVersionFromInstant(), corrected.getVersionFromInstant());
    assertEquals(base.getVersionToInstant(), corrected.getVersionToInstant());
    assertEquals(now, corrected.getCorrectionFromInstant());
    assertEquals(null, corrected.getCorrectionToInstant());
    assertEquals(input.getExchange(), corrected.getExchange());

    final ExchangeDocument old = _exgMaster.get(UniqueId.of("DbExg", "101", "0"));
    assertEquals(base.getUniqueId(), old.getUniqueId());
    assertEquals(base.getVersionFromInstant(), old.getVersionFromInstant());
    assertEquals(base.getVersionToInstant(), old.getVersionToInstant());
    assertEquals(base.getCorrectionFromInstant(), old.getCorrectionFromInstant());
    assertEquals(now, old.getCorrectionToInstant());  // old version ended
    assertEquals(base.getExchange(), old.getExchange());

    final ExchangeHistoryRequest search = new ExchangeHistoryRequest(base.getUniqueId(), now, null);
    final ExchangeHistoryResult searchResult = _exgMaster.history(search);
    assertEquals(2, searchResult.getDocuments().size());
  }

}
