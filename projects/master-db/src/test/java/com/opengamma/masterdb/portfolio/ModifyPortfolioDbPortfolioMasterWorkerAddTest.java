/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.portfolio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.threeten.bp.Instant;

import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.master.DocumentVisibility;
import com.opengamma.master.portfolio.ManageablePortfolio;
import com.opengamma.master.portfolio.ManageablePortfolioNode;
import com.opengamma.master.portfolio.PortfolioDocument;
import com.opengamma.util.test.DbTest;
import com.opengamma.util.test.TestGroup;

/**
 * Tests ModifyPortfolioDbPortfolioMasterWorker.
 */
@Test(groups = TestGroup.UNIT_DB)
public class ModifyPortfolioDbPortfolioMasterWorkerAddTest extends AbstractDbPortfolioMasterWorkerTest {
  // superclass sets up dummy database

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyPortfolioDbPortfolioMasterWorkerAddTest.class);

  @Factory(dataProvider = "databases", dataProviderClass = DbTest.class)
  public ModifyPortfolioDbPortfolioMasterWorkerAddTest(final String databaseType, final String databaseVersion) {
    super(databaseType, databaseVersion, false);
    LOGGER.info("running testcases for {}", databaseType);
  }

  //-------------------------------------------------------------------------
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_add_nullDocument() {
    _prtMaster.add(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_add_noPortfolio() {
    final PortfolioDocument doc = new PortfolioDocument();
    _prtMaster.add(doc);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_add_noRootNode() {
    final ManageablePortfolio mockPortfolio = mock(ManageablePortfolio.class);
    when(mockPortfolio.getName()).thenReturn("Test");
    final PortfolioDocument doc = new PortfolioDocument();
    doc.setPortfolio(mockPortfolio);
    _prtMaster.add(doc);
  }

  @Test
  public void test_add_add() {
    final Instant now = Instant.now(_prtMaster.getClock());

    final ManageablePortfolioNode rootNode = new ManageablePortfolioNode("Root");
    final ManageablePortfolioNode childNode = new ManageablePortfolioNode("Child");
    childNode.addPosition(UniqueId.of("TestPos", "1234"));
    rootNode.addChildNode(childNode);
    final ManageablePortfolio portfolio = new ManageablePortfolio("Test");
    portfolio.setRootNode(rootNode);
    final PortfolioDocument doc = new PortfolioDocument();
    doc.setPortfolio(portfolio);
    final PortfolioDocument test = _prtMaster.add(doc);

    final UniqueId uniqueId = test.getUniqueId();
    assertNotNull(uniqueId);
    assertEquals("DbPrt", uniqueId.getScheme());
    assertTrue(uniqueId.isVersioned());
    assertTrue(Long.parseLong(uniqueId.getValue()) >= 1000);
    assertEquals("0", uniqueId.getVersion());
    assertEquals(now, test.getVersionFromInstant());
    assertEquals(null, test.getVersionToInstant());
    assertEquals(now, test.getCorrectionFromInstant());
    assertEquals(null, test.getCorrectionToInstant());
    assertEquals(DocumentVisibility.VISIBLE, test.getVisibility());

    final ManageablePortfolio testPortfolio = test.getPortfolio();
    assertEquals(uniqueId, testPortfolio.getUniqueId());
    assertEquals("Test", testPortfolio.getName());
    assertNotNull(testPortfolio.getAttributes());

    final ManageablePortfolioNode testRootNode = testPortfolio.getRootNode();
    assertEquals("Root", testRootNode.getName());
    assertEquals(null, testRootNode.getParentNodeId());
    assertEquals(uniqueId, testRootNode.getPortfolioId());
    assertEquals(1, testRootNode.getChildNodes().size());

    final ManageablePortfolioNode testChildNode = testRootNode.getChildNodes().get(0);
    assertEquals("Child", testChildNode.getName());
    assertEquals(testRootNode.getUniqueId(), testChildNode.getParentNodeId());
    assertEquals(uniqueId, testChildNode.getPortfolioId());
    assertEquals(0, testChildNode.getChildNodes().size());
    assertEquals(1, testChildNode.getPositionIds().size());
    assertEquals(ObjectId.of("TestPos", "1234"), testChildNode.getPositionIds().get(0));
  }

  @Test
  public void test_addWithAttributes_add() {
    final Instant now = Instant.now(_prtMaster.getClock());

    final ManageablePortfolioNode rootNode = new ManageablePortfolioNode("Root");
    final ManageablePortfolioNode childNode = new ManageablePortfolioNode("Child");
    childNode.addPosition(UniqueId.of("TestPos", "1234"));
    rootNode.addChildNode(childNode);
    final ManageablePortfolio portfolio = new ManageablePortfolio("Test");
    portfolio.setRootNode(rootNode);
    portfolio.addAttribute("A1", "V1");
    portfolio.addAttribute("A2", "V2");
    final PortfolioDocument doc = new PortfolioDocument();
    doc.setPortfolio(portfolio);
    final PortfolioDocument test = _prtMaster.add(doc);

    final UniqueId uniqueId = test.getUniqueId();
    assertNotNull(uniqueId);
    assertEquals("DbPrt", uniqueId.getScheme());
    assertTrue(uniqueId.isVersioned());
    assertTrue(Long.parseLong(uniqueId.getValue()) >= 1000);
    assertEquals("0", uniqueId.getVersion());
    assertEquals(now, test.getVersionFromInstant());
    assertEquals(null, test.getVersionToInstant());
    assertEquals(now, test.getCorrectionFromInstant());
    assertEquals(null, test.getCorrectionToInstant());

    final ManageablePortfolio testPortfolio = test.getPortfolio();
    assertEquals(uniqueId, testPortfolio.getUniqueId());
    assertEquals("Test", testPortfolio.getName());
    assertNotNull(testPortfolio.getAttributes());
    assertEquals(2, testPortfolio.getAttributes().size());
    assertEquals("V1", testPortfolio.getAttributes().get("A1"));
    assertEquals("V2", testPortfolio.getAttributes().get("A2"));

    final ManageablePortfolioNode testRootNode = testPortfolio.getRootNode();
    assertEquals("Root", testRootNode.getName());
    assertEquals(null, testRootNode.getParentNodeId());
    assertEquals(uniqueId, testRootNode.getPortfolioId());
    assertEquals(1, testRootNode.getChildNodes().size());

    final ManageablePortfolioNode testChildNode = testRootNode.getChildNodes().get(0);
    assertEquals("Child", testChildNode.getName());
    assertEquals(testRootNode.getUniqueId(), testChildNode.getParentNodeId());
    assertEquals(uniqueId, testChildNode.getPortfolioId());
    assertEquals(0, testChildNode.getChildNodes().size());
    assertEquals(1, testChildNode.getPositionIds().size());
    assertEquals(ObjectId.of("TestPos", "1234"), testChildNode.getPositionIds().get(0));
  }

  @Test
  public void test_add_addThenGet() {
    final ManageablePortfolioNode rootNode = new ManageablePortfolioNode("Root");
    final ManageablePortfolioNode childNode = new ManageablePortfolioNode("Child");
    rootNode.addChildNode(childNode);
    final ManageablePortfolio portfolio = new ManageablePortfolio("Test");
    portfolio.setRootNode(rootNode);
    final PortfolioDocument doc = new PortfolioDocument();
    doc.setPortfolio(portfolio);
    final PortfolioDocument added = _prtMaster.add(doc);

    final PortfolioDocument test = _prtMaster.get(added.getUniqueId());
    assertEquals(added, test);
  }

  @Test
  public void test_addWithAttributes_addThenGet() {
    final ManageablePortfolioNode rootNode = new ManageablePortfolioNode("Root");
    final ManageablePortfolioNode childNode = new ManageablePortfolioNode("Child");
    rootNode.addChildNode(childNode);
    ManageablePortfolio portfolio = new ManageablePortfolio("Test");
    portfolio.setRootNode(rootNode);
    portfolio.addAttribute("A1", "V1");
    portfolio.addAttribute("A2", "V2");
    PortfolioDocument doc = new PortfolioDocument();
    doc.setPortfolio(portfolio);
    PortfolioDocument added = _prtMaster.add(doc);

    PortfolioDocument test = _prtMaster.get(added.getUniqueId());
    assertEquals(added, test);

    //add another
    portfolio = new ManageablePortfolio("Test2");
    portfolio.setRootNode(rootNode);
    portfolio.addAttribute("A21", "V21");
    portfolio.addAttribute("A22", "V22");
    doc = new PortfolioDocument();
    doc.setPortfolio(portfolio);
    added = _prtMaster.add(doc);

    test = _prtMaster.get(added.getUniqueId());
    assertEquals(added, test);
  }

  @Test
  public void test_addCustomVisibility_addThenGet() {
    final ManageablePortfolioNode rootNode = new ManageablePortfolioNode("Root");
    final ManageablePortfolioNode childNode = new ManageablePortfolioNode("Child");
    rootNode.addChildNode(childNode);
    final ManageablePortfolio portfolio = new ManageablePortfolio("Test");
    portfolio.setRootNode(rootNode);
    final PortfolioDocument doc = new PortfolioDocument();
    doc.setPortfolio(portfolio);
    doc.setVisibility(DocumentVisibility.HIDDEN);
    final PortfolioDocument added = _prtMaster.add(doc);

    final PortfolioDocument test = _prtMaster.get(added.getUniqueId());
    assertEquals(DocumentVisibility.HIDDEN, test.getVisibility());
    assertEquals(added, test);
  }

}
