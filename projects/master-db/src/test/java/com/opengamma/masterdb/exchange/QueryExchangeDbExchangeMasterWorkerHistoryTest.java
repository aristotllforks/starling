/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.exchange;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.opengamma.id.ObjectId;
import com.opengamma.master.exchange.ExchangeHistoryRequest;
import com.opengamma.master.exchange.ExchangeHistoryResult;
import com.opengamma.util.paging.PagingRequest;
import com.opengamma.util.test.DbTest;
import com.opengamma.util.test.TestGroup;

/**
 * Tests QueryExchangeDbExchangeMasterWorker.
 */
@Test(groups = TestGroup.UNIT_DB)
public class QueryExchangeDbExchangeMasterWorkerHistoryTest extends AbstractDbExchangeMasterWorkerTest {
  // superclass sets up dummy database

  private static final Logger LOGGER = LoggerFactory.getLogger(QueryExchangeDbExchangeMasterWorkerHistoryTest.class);

  @Factory(dataProvider = "databases", dataProviderClass = DbTest.class)
  public QueryExchangeDbExchangeMasterWorkerHistoryTest(final String databaseType, final String databaseVersion) {
    super(databaseType, databaseVersion, true);
    LOGGER.info("running testcases for {}", databaseType);
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_history_documents() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(2, test.getDocuments().size());
    assert202(test.getDocuments().get(0));
    assert201(test.getDocuments().get(1));
  }

  @Test
  public void test_history_documentCountWhenMultipleExchanges() {
    final ObjectId oid = ObjectId.of("DbExg", "102");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(1, test.getPaging().getTotalItems());

    assertEquals(1, test.getDocuments().size());
    assert102(test.getDocuments().get(0));
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_history_noInstants() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(PagingRequest.ALL, test.getPaging().getRequest());
    assertEquals(2, test.getPaging().getTotalItems());

    assertEquals(2, test.getDocuments().size());
    assert202(test.getDocuments().get(0));
    assert201(test.getDocuments().get(1));
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_history_noInstants_pageOne() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final PagingRequest pr = PagingRequest.ofPage(1, 1);
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    request.setPagingRequest(pr);
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(pr, test.getPaging().getRequest());
    assertEquals(2, test.getPaging().getTotalItems());

    assertEquals(1, test.getDocuments().size());
    assert202(test.getDocuments().get(0));
  }

  @Test
  public void test_history_noInstants_pageTwo() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final PagingRequest pr = PagingRequest.ofPage(2, 1);
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    request.setPagingRequest(pr);
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertNotNull(test);
    assertNotNull(test.getPaging());
    assertEquals(pr, test.getPaging().getRequest());
    assertEquals(2, test.getPaging().getTotalItems());

    assertNotNull(test.getDocuments());
    assertEquals(1, test.getDocuments().size());
    assert201(test.getDocuments().get(0));
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_history_versionsFrom_preFirst() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    request.setVersionsFromInstant(_version1Instant.minusSeconds(5));
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(2, test.getPaging().getTotalItems());

    assertEquals(2, test.getDocuments().size());
    assert202(test.getDocuments().get(0));
    assert201(test.getDocuments().get(1));
  }

  @Test
  public void test_history_versionsFrom_firstToSecond() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    request.setVersionsFromInstant(_version1Instant.plusSeconds(5));
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(2, test.getPaging().getTotalItems());

    assertEquals(2, test.getDocuments().size());
    assert202(test.getDocuments().get(0));
    assert201(test.getDocuments().get(1));
  }

  @Test
  public void test_history_versionsFrom_postSecond() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    request.setVersionsFromInstant(_version2Instant.plusSeconds(5));
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(1, test.getPaging().getTotalItems());

    assertEquals(1, test.getDocuments().size());
    assert202(test.getDocuments().get(0));
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_history_versionsTo_preFirst() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    request.setVersionsToInstant(_version1Instant.minusSeconds(5));
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(0, test.getPaging().getTotalItems());

    assertEquals(0, test.getDocuments().size());
  }

  @Test
  public void test_history_versionsTo_firstToSecond() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    request.setVersionsToInstant(_version1Instant.plusSeconds(5));
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(1, test.getPaging().getTotalItems());

    assertEquals(1, test.getDocuments().size());
    assert201(test.getDocuments().get(0));
  }

  @Test
  public void test_history_versionsTo_postSecond() {
    final ObjectId oid = ObjectId.of("DbExg", "201");
    final ExchangeHistoryRequest request = new ExchangeHistoryRequest(oid);
    request.setVersionsToInstant(_version2Instant.plusSeconds(5));
    final ExchangeHistoryResult test = _exgMaster.history(request);

    assertEquals(2, test.getPaging().getTotalItems());

    assertEquals(2, test.getDocuments().size());
    assert202(test.getDocuments().get(0));
    assert201(test.getDocuments().get(1));
  }

}
