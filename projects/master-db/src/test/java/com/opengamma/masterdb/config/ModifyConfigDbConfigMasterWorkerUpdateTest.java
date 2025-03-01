/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.config;

import static org.testng.AssertJUnit.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.threeten.bp.Instant;

import com.opengamma.DataNotFoundException;
import com.opengamma.core.config.impl.ConfigItem;
import com.opengamma.elsql.ElSqlBundle;
import com.opengamma.elsql.ElSqlConfig;
import com.opengamma.id.ExternalId;
import com.opengamma.id.UniqueId;
import com.opengamma.master.config.ConfigDocument;
import com.opengamma.master.config.ConfigHistoryRequest;
import com.opengamma.master.config.ConfigHistoryResult;
import com.opengamma.util.test.DbTest;
import com.opengamma.util.test.TestGroup;

/**
 * Tests ModifyConfigDbConfigMasterWorker.
 */
@Test(groups = TestGroup.UNIT_DB)
public class ModifyConfigDbConfigMasterWorkerUpdateTest extends AbstractDbConfigMasterWorkerTest {
  // superclass sets up dummy database

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyConfigDbConfigMasterWorkerUpdateTest.class);

  @Factory(dataProvider = "databases", dataProviderClass = DbTest.class)
  public ModifyConfigDbConfigMasterWorkerUpdateTest(final String databaseType, final String databaseVersion) {
    super(databaseType, databaseVersion, false);
    LOGGER.info("running testcases for {}", databaseType);
  }

  //-------------------------------------------------------------------------
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_updateConfig_nullDocument() {
    _cfgMaster.update(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_update_noConfigId() {
    final ConfigItem<ExternalId> item = ConfigItem.of(ExternalId.of("A", "B"));
    item.setName("Name");
    _cfgMaster.update(new ConfigDocument(item));
  }

  @Test(expectedExceptions = DataNotFoundException.class)
  public void test_update_notFound() {
    final UniqueId uniqueId = UniqueId.of("DbCfg", "0", "0");
    final ConfigItem<ExternalId> item = ConfigItem.of(ExternalId.of("A", "B"));
    item.setName("Name");
    final ConfigDocument doc = new ConfigDocument(item);
    doc.setUniqueId(uniqueId);
    _cfgMaster.update(doc);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_update_notLatestVersion() {
    final UniqueId uniqueId = UniqueId.of("DbCfg", "201", "0");
    final ConfigItem<ExternalId> item = ConfigItem.of(ExternalId.of("A", "B"));
    item.setName("Name");
    final ConfigDocument doc = new ConfigDocument(item);
    doc.setUniqueId(uniqueId);
    _cfgMaster.update(doc);
  }

  @Test
  public void test_update_getUpdateGet() {
    final Instant now = Instant.now(_cfgMaster.getClock());

    final UniqueId uniqueId = UniqueId.of("DbCfg", "101", "0");
    final ConfigDocument base = _cfgMaster.get(uniqueId);
    final ConfigItem<ExternalId> input = ConfigItem.of(ExternalId.of("A", "B"));

    input.setName("NewName");
    final ConfigDocument doc = new ConfigDocument(input);
    doc.setUniqueId(uniqueId);

    final ConfigDocument updated = _cfgMaster.update(doc);
    assertEquals(false, base.getUniqueId().equals(updated.getUniqueId()));
    assertEquals(now, updated.getVersionFromInstant());
    assertEquals(null, updated.getVersionToInstant());
    assertEquals(now, updated.getCorrectionFromInstant());
    assertEquals(null, updated.getCorrectionToInstant());
    assertEquals("NewName", updated.getName());
    assertEquals(ExternalId.of("A", "B"), updated.getConfig().getValue());

    final ConfigDocument old = _cfgMaster.get(uniqueId);
    assertEquals(base.getUniqueId(), old.getUniqueId());
    assertEquals(base.getVersionFromInstant(), old.getVersionFromInstant());
    assertEquals(now, old.getVersionToInstant());  // old version ended
    assertEquals(base.getCorrectionFromInstant(), old.getCorrectionFromInstant());
    assertEquals(base.getCorrectionToInstant(), old.getCorrectionToInstant());
    assertEquals(base.getName(), old.getName());
    assertEquals(base.getConfig().getValue(), old.getConfig().getValue());

    final ConfigHistoryRequest<ExternalId> search = new ConfigHistoryRequest<>(base.getUniqueId(), null, now);
    search.setType(ExternalId.class);

    final ConfigHistoryResult<ExternalId> searchResult = _cfgMaster.history(search);
    assertEquals(2, searchResult.getDocuments().size());
  }

  @Test
  public void test_update_nameChangeNullValue() {
    final Instant now = Instant.now(_cfgMaster.getClock());

    final UniqueId uniqueId = UniqueId.of("DbCfg", "101", "0");
    final ConfigDocument base = _cfgMaster.get(uniqueId);
    //ConfigItem<ExternalId> input = ConfigItem.of(null);
    final ConfigDocument doc = new ConfigDocument(null);
    //input.setName("NewName");
    doc.setUniqueId(uniqueId);
    doc.setName("NewName");

    final ConfigDocument updated = _cfgMaster.update(doc);
    assertEquals(false, base.getUniqueId().equals(updated.getUniqueId()));
    assertEquals("NewName", updated.getName());  // name changed
    assertEquals(base.getConfig().getValue(), updated.getConfig().getValue());  // value unchanged

    final ConfigDocument old = _cfgMaster.get(uniqueId);
    assertEquals(base.getUniqueId(), old.getUniqueId());
    assertEquals(now, old.getVersionToInstant());  // old version ended
    assertEquals(base.getName(), old.getName());
    assertEquals(base.getConfig().getValue(), old.getConfig().getValue());
  }

  @Test
  public void test_update_rollback() {
    final DbConfigWorker w = new DbConfigWorker(_cfgMaster.getDbConnector(), _cfgMaster.getUniqueIdScheme());
    w.setElSqlBundle(ElSqlBundle.of(new ElSqlConfig("TestRollback"), DbConfigMaster.class));
    final ConfigDocument base = _cfgMaster.get(UniqueId.of("DbCfg", "101", "0"));
    final UniqueId uniqueId = UniqueId.of("DbCfg", "101", "0");
    final ConfigItem<ExternalId> input = ConfigItem.of(ExternalId.of("A", "B"));
    final ConfigDocument doc = new ConfigDocument(input);
    doc.setUniqueId(uniqueId);
    input.setName("Name");
    try {
      w.update(doc);
      Assert.fail();
    } catch (final BadSqlGrammarException ex) {
      // expected
    }
    final ConfigDocument test = _cfgMaster.get(UniqueId.of("DbCfg", "101", "0"));

    assertEquals(base, test);
  }

}
