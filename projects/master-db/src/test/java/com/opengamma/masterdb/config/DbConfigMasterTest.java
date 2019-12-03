/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.config;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.opengamma.core.config.impl.ConfigItem;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.master.config.ConfigDocument;
import com.opengamma.util.test.AbstractDbTest;
import com.opengamma.util.test.DbTest;
import com.opengamma.util.test.TestGroup;

/**
 * Test DbConfigMaster.
 */
@Test(groups = TestGroup.UNIT_DB)
public class DbConfigMasterTest extends AbstractDbTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(DbConfigMasterTest.class);

  private DbConfigMaster _cfgMaster;

  @Factory(dataProvider = "databases", dataProviderClass = DbTest.class)
  public DbConfigMasterTest(final String databaseType, final String databaseVersion) {
    super(databaseType, databaseVersion);
    LOGGER.info("running testcases for {}", databaseType);
  }

  //-------------------------------------------------------------------------
  @Override
  protected void doSetUp() {
    _cfgMaster = new DbConfigMaster(getDbConnector());
  }

  @Override
  protected void doTearDown() {
    _cfgMaster = null;
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_basics() throws Exception {
    assertNotNull(_cfgMaster);
    assertEquals(true, _cfgMaster.getUniqueIdScheme().equals("DbCfg"));
    assertNotNull(_cfgMaster.getDbConnector());
    assertNotNull(_cfgMaster.getClock());
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_single_type() throws Exception {
    final ConfigItem<ExternalId> item = ConfigItem.of(ExternalId.of("A", "B"));
    item.setName("Config test");

    final ConfigDocument added = _cfgMaster.add(new ConfigDocument(item));

    final ConfigDocument loaded = _cfgMaster.get(added.getUniqueId());
    assertEquals(added, loaded);

    final ConfigDocument loadedType = _cfgMaster.get(added.getUniqueId());
    assertEquals(added, loadedType);
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_multiple_types() throws Exception {
    final ConfigItem<ExternalId> identifierDoc = ConfigItem.of(ExternalId.of("A", "B"));
    identifierDoc.setName("ExternalId test");

    final ConfigDocument addedId = _cfgMaster.add(new ConfigDocument(identifierDoc));

    final ConfigItem<ExternalIdBundle> bundleDoc = ConfigItem.of(ExternalIdBundle.of(ExternalId.of("A", "B"), ExternalId.of("C", "D")));
    bundleDoc.setName("Bundle test");
    final ConfigDocument addedBundle = _cfgMaster.add(new ConfigDocument(bundleDoc));

    final ConfigDocument loadedId = _cfgMaster.get(addedId.getUniqueId());
    assertEquals(addedId, loadedId);

    final ConfigDocument loadedBundle = _cfgMaster.get(addedBundle.getUniqueId());
    assertEquals(addedBundle, loadedBundle);
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_toString() {
    assertEquals("DbConfigMaster[DbCfg]", _cfgMaster.toString());
  }

}
