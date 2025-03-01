/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.position;

import static org.testng.AssertJUnit.assertEquals;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.threeten.bp.Instant;

import com.opengamma.DataNotFoundException;
import com.opengamma.id.ExternalId;
import com.opengamma.id.UniqueId;
import com.opengamma.master.position.ManageablePosition;
import com.opengamma.master.position.PositionDocument;
import com.opengamma.master.position.PositionHistoryRequest;
import com.opengamma.master.position.PositionHistoryResult;
import com.opengamma.util.test.DbTest;
import com.opengamma.util.test.TestGroup;

/**
 * Tests ModifyPositionDbPositionMasterWorker.
 */
@Test(groups = TestGroup.UNIT_DB)
public class ModifyPositionDbPositionMasterWorkerCorrectPositionTest extends AbstractDbPositionMasterWorkerTest {
  // superclass sets up dummy database

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyPositionDbPositionMasterWorkerCorrectPositionTest.class);

  @Factory(dataProvider = "databases", dataProviderClass = DbTest.class)
  public ModifyPositionDbPositionMasterWorkerCorrectPositionTest(final String databaseType, final String databaseVersion) {
    super(databaseType, databaseVersion, false);
    LOGGER.info("running testcases for {}", databaseType);
  }

  //-------------------------------------------------------------------------
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_correct_nullDocument() {
    _posMaster.correct(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_correct_noPositionId() {
    final ManageablePosition position = new ManageablePosition(BigDecimal.TEN, ExternalId.of("A", "B"));
    final PositionDocument doc = new PositionDocument();
    doc.setPosition(position);
    _posMaster.correct(doc);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_correct_noPosition() {
    final PositionDocument doc = new PositionDocument();
    doc.setUniqueId(UniqueId.of("DbPos", "121", "0"));
    _posMaster.correct(doc);
  }

  @Test(expectedExceptions = DataNotFoundException.class)
  public void test_correct_notFound() {
    final ManageablePosition pos = new ManageablePosition(BigDecimal.TEN, ExternalId.of("A", "B"));
    pos.setUniqueId(UniqueId.of("DbPos", "0", "0"));
    final PositionDocument doc = new PositionDocument(pos);
    _posMaster.correct(doc);
  }

//  @Test(expected = IllegalArgumentException.class)
//  public void test_correct_notLatestCorrection() {
//    PortfolioTreePosition pos = new PortfolioTreePosition(UniqueId("DbPos", "221", "221"), BigDecimal.TEN, ExternalId.of("A", "B"));
//    PositionDocument doc = new PositionDocument(pos);
//    _posMaster.correct(doc);
//  }

  @Test
  public void test_correct_getUpdateGet() {
    final Instant now = Instant.now(_posMaster.getClock());

    final PositionDocument base = _posMaster.get(UniqueId.of("DbPos", "121", "0"));
    final ManageablePosition pos = new ManageablePosition(BigDecimal.TEN, ExternalId.of("A", "B"));
    pos.setUniqueId(UniqueId.of("DbPos", "121", "0"));
    final PositionDocument input = new PositionDocument(pos);

    final PositionDocument corrected = _posMaster.correct(input);
    assertEquals(false, base.getUniqueId().equals(corrected.getUniqueId()));
    assertEquals(base.getVersionFromInstant(), corrected.getVersionFromInstant());
    assertEquals(base.getVersionToInstant(), corrected.getVersionToInstant());
    assertEquals(now, corrected.getCorrectionFromInstant());
    assertEquals(null, corrected.getCorrectionToInstant());
    assertEquals(input.getPosition(), corrected.getPosition());

    final PositionDocument old = _posMaster.get(UniqueId.of("DbPos", "121", "0"));
    assertEquals(base.getUniqueId(), old.getUniqueId());
    assertEquals(base.getVersionFromInstant(), old.getVersionFromInstant());
    assertEquals(base.getVersionToInstant(), old.getVersionToInstant());
    assertEquals(base.getCorrectionFromInstant(), old.getCorrectionFromInstant());
    assertEquals(now, old.getCorrectionToInstant());  // old version ended
    assertEquals(base.getPosition(), old.getPosition());

    final PositionHistoryRequest search = new PositionHistoryRequest(base.getUniqueId(), now, null);
    final PositionHistoryResult searchResult = _posMaster.history(search);
    assertEquals(2, searchResult.getDocuments().size());
  }

}
