/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.holiday;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;

import com.opengamma.DataNotFoundException;
import com.opengamma.elsql.ElSqlBundle;
import com.opengamma.elsql.ElSqlConfig;
import com.opengamma.id.UniqueId;
import com.opengamma.master.holiday.HolidayDocument;
import com.opengamma.master.holiday.HolidayHistoryRequest;
import com.opengamma.master.holiday.HolidayHistoryResult;
import com.opengamma.master.holiday.ManageableHoliday;
import com.opengamma.util.money.Currency;
import com.opengamma.util.test.DbTest;
import com.opengamma.util.test.TestGroup;

/**
 * Tests ModifyHolidayDbHolidayMasterWorker.
 */
@Test(groups = TestGroup.UNIT_DB)
public class ModifyHolidayDbHolidayMasterWorkerUpdateTest extends AbstractDbHolidayMasterWorkerTest {
  // superclass sets up dummy database

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHolidayDbHolidayMasterWorkerUpdateTest.class);

  @Factory(dataProvider = "databases", dataProviderClass = DbTest.class)
  public ModifyHolidayDbHolidayMasterWorkerUpdateTest(final String databaseType, final String databaseVersion) {
    super(databaseType, databaseVersion, false);
    LOGGER.info("running testcases for {}", databaseType);
  }

  //-------------------------------------------------------------------------
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_updateHoliday_nullDocument() {
    _holMaster.update(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_update_noHolidayId() {
    final UniqueId uniqueId = UniqueId.of("DbHol", "101");
    final ManageableHoliday holiday = new ManageableHoliday(Currency.USD, Arrays.asList(LocalDate.of(2010, 6, 9)));
    holiday.setUniqueId(uniqueId);
    final HolidayDocument doc = new HolidayDocument();
    doc.setHoliday(holiday);
    _holMaster.update(doc);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_update_noHoliday() {
    final HolidayDocument doc = new HolidayDocument();
    doc.setUniqueId(UniqueId.of("DbHol", "101", "0"));
    _holMaster.update(doc);
  }

  @Test(expectedExceptions = DataNotFoundException.class)
  public void test_update_notFound() {
    final UniqueId uniqueId = UniqueId.of("DbHol", "0", "0");
    final ManageableHoliday holiday = new ManageableHoliday(Currency.USD, Arrays.asList(LocalDate.of(2010, 6, 9)));
    holiday.setUniqueId(uniqueId);
    final HolidayDocument doc = new HolidayDocument(holiday);
    _holMaster.update(doc);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_update_notLatestVersion() {
    final UniqueId uniqueId = UniqueId.of("DbHol", "201", "0");
    final ManageableHoliday holiday = new ManageableHoliday(Currency.USD, Arrays.asList(LocalDate.of(2010, 6, 9)));
    holiday.setUniqueId(uniqueId);
    final HolidayDocument doc = new HolidayDocument(holiday);
    _holMaster.update(doc);
  }

  @Test
  public void test_update_getUpdateGet() {
    final Instant now = Instant.now(_holMaster.getClock());

    final UniqueId uniqueId = UniqueId.of("DbHol", "101", "0");
    final HolidayDocument base = _holMaster.get(uniqueId);
    final ManageableHoliday holiday = new ManageableHoliday(Currency.USD, Arrays.asList(LocalDate.of(2010, 6, 9)));
    holiday.setUniqueId(uniqueId);
    final HolidayDocument input = new HolidayDocument(holiday);

    final HolidayDocument updated = _holMaster.update(input);
    assertEquals(false, base.getUniqueId().equals(updated.getUniqueId()));
    assertEquals(now, updated.getVersionFromInstant());
    assertEquals(null, updated.getVersionToInstant());
    assertEquals(now, updated.getCorrectionFromInstant());
    assertEquals(null, updated.getCorrectionToInstant());
    assertEquals(input.getHoliday(), updated.getHoliday());

    final HolidayDocument old = _holMaster.get(uniqueId);
    assertEquals(base.getUniqueId(), old.getUniqueId());
    assertEquals(base.getVersionFromInstant(), old.getVersionFromInstant());
    assertEquals(now, old.getVersionToInstant());  // old version ended
    assertEquals(base.getCorrectionFromInstant(), old.getCorrectionFromInstant());
    assertEquals(base.getCorrectionToInstant(), old.getCorrectionToInstant());
    assertEquals(base.getHoliday(), old.getHoliday());

    final HolidayHistoryRequest search = new HolidayHistoryRequest(base.getUniqueId(), null, now);
    final HolidayHistoryResult searchResult = _holMaster.history(search);
    assertEquals(2, searchResult.getDocuments().size());
  }

  @Test
  public void test_update_rollback() {
    final DbHolidayMaster w = new DbHolidayMaster(_holMaster.getDbConnector());
    w.setElSqlBundle(ElSqlBundle.of(new ElSqlConfig("TestRollback"), DbHolidayMaster.class));
    final HolidayDocument base = _holMaster.get(UniqueId.of("DbHol", "101", "0"));
    final UniqueId uniqueId = UniqueId.of("DbHol", "101", "0");
    final ManageableHoliday holiday = new ManageableHoliday(Currency.USD, Arrays.asList(LocalDate.of(2010, 6, 9)));
    holiday.setUniqueId(uniqueId);
    final HolidayDocument input = new HolidayDocument(holiday);
    try {
      w.update(input);
      Assert.fail();
    } catch (final BadSqlGrammarException ex) {
      // expected
    }
    final HolidayDocument test = _holMaster.get(UniqueId.of("DbHol", "101", "0"));

    assertEquals(base, test);
  }

}
