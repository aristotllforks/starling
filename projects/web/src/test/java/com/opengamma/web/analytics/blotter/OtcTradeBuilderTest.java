/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics.blotter;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.math.BigDecimal;

import org.apache.commons.lang.ArrayUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.google.common.collect.ImmutableMap;
import com.opengamma.financial.security.LongShort;
import com.opengamma.financial.security.fx.FXForwardSecurity;
import com.opengamma.financial.security.option.SwaptionSecurity;
import com.opengamma.financial.security.swap.SwapSecurity;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ExternalIdSearch;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.portfolio.ManageablePortfolio;
import com.opengamma.master.portfolio.ManageablePortfolioNode;
import com.opengamma.master.portfolio.PortfolioDocument;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.portfolio.impl.InMemoryPortfolioMaster;
import com.opengamma.master.position.ManageablePosition;
import com.opengamma.master.position.ManageableTrade;
import com.opengamma.master.position.PositionDocument;
import com.opengamma.master.position.PositionMaster;
import com.opengamma.master.position.impl.InMemoryPositionMaster;
import com.opengamma.master.security.ManageableSecurity;
import com.opengamma.master.security.ManageableSecurityLink;
import com.opengamma.master.security.SecurityDocument;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.master.security.SecuritySearchRequest;
import com.opengamma.master.security.SecuritySearchResult;
import com.opengamma.master.security.impl.InMemorySecurityMaster;
import com.opengamma.util.money.Currency;
import com.opengamma.util.test.TestGroup;

/**
 * Test.
 */
@Test(groups = TestGroup.UNIT)
public class OtcTradeBuilderTest {

  private static final ImmutableMap<String, String> ATTRIBUTES = ImmutableMap.of("attr1", "val1", "attr2", "val2");
  private static final OffsetTime PREMIUM_TIME = LocalTime.of(13, 0).atOffset(ZoneOffset.UTC);
  private static final OffsetTime TRADE_TIME = LocalTime.of(10, 0).atOffset(ZoneOffset.UTC);
  private static final LocalDate PREMIUM_DATE = LocalDate.of(2012, 12, 25);
  private static final LocalDate TRADE_DATE = LocalDate.of(2012, 12, 21);
  private static final double PREMIUM = 1234d;
  private static final ExternalId COUNTERPARTY_ID = ExternalId.of("Cpty", "testCpty");

  private SecurityMaster _securityMaster;
  private PositionMaster _positionMaster;
  private PortfolioMaster _portfolioMaster;
  private OtcTradeBuilder _builder;
  private ManageablePortfolio _savedPortfolio;
  private UniqueId _nodeId;

  // TODO test that the URL ID is always unversioned and the trade ID is always versioned
  // TODO what happens if an existing trade's security is changed?

  // TODO create trade with various fields missing (especially attributes)

  // TODO move to BlotterTestUtils?
  private static BeanDataSource createTradeData(final Object... valuePairs) {
    final Object[] basicData = {
        "type", "OtcTrade",
        "counterparty", "testCpty",
        "tradeDate", "2012-12-21",
        "tradeTime", "10:00",
        "premium", "1234",
        "premiumCurrency", "GBP",
        "premiumDate", "2012-12-25",
        "premiumTime", "13:00",
        "attributes", ATTRIBUTES};
    final Object[] tradeData = ArrayUtils.addAll(basicData, valuePairs);
    return BlotterTestUtils.beanData(tradeData);
  }

  @BeforeMethod
  public void setUp() throws Exception {
    _securityMaster = new InMemorySecurityMaster();
    _positionMaster = new InMemoryPositionMaster();
    _portfolioMaster = new InMemoryPortfolioMaster();
    _builder = new OtcTradeBuilder(_positionMaster,
                                   _portfolioMaster,
                                   _securityMaster,
                                   BlotterUtils.getMetaBeans(),
                                   BlotterUtils.getStringConvert());
    final ManageablePortfolio portfolio = new ManageablePortfolio();
    final ManageablePortfolioNode root = new ManageablePortfolioNode();
    final ManageablePortfolioNode node = new ManageablePortfolioNode();
    root.addChildNode(node);
    portfolio.setRootNode(root);
    _savedPortfolio = _portfolioMaster.add(new PortfolioDocument(portfolio)).getPortfolio();
    _nodeId = _savedPortfolio.getRootNode().getChildNodes().get(0).getUniqueId();
  }

  @Test
  public void newSecurityWithNoUnderlying() {
    final UniqueId tradeId = _builder.addTrade(createTradeData(), BlotterTestUtils.FX_FORWARD_DATA_SOURCE, null, _nodeId);
    final ManageableTrade trade = _positionMaster.getTrade(tradeId);
    final UniqueId positionId = trade.getParentPositionId();
    final ManageablePosition position = _positionMaster.get(positionId).getPosition();
    assertEquals(BigDecimal.ONE, trade.getQuantity());
    assertEquals(BigDecimal.ONE, position.getQuantity());
    final ManageableSecurity security = _securityMaster.get(trade.getSecurityLink().getObjectId(),
                                                      VersionCorrection.LATEST).getSecurity();
    assertNotNull(security);
    security.setUniqueId(null); // so it can be tested for equality against the unsaved version
    assertEquals(BlotterTestUtils.FX_FORWARD, security);
    assertEquals(COUNTERPARTY_ID, trade.getCounterpartyExternalId());
    assertEquals(PREMIUM, trade.getPremium());
    assertEquals(Currency.GBP, trade.getPremiumCurrency());
    assertEquals(PREMIUM_DATE, trade.getPremiumDate());
    assertEquals(TRADE_DATE, trade.getTradeDate());
    assertEquals(PREMIUM_TIME, trade.getPremiumTime());
    assertEquals(TRADE_TIME, trade.getTradeTime());
    assertEquals(ATTRIBUTES, trade.getAttributes());

    // can't check the node ID as nodes are completely replaced
    final ManageablePortfolioNode loadedRoot = _portfolioMaster.get(_savedPortfolio.getUniqueId()).getPortfolio().getRootNode();
    final ManageablePortfolioNode loadedNode = loadedRoot.getChildNodes().get(0);
    assertEquals(1, loadedNode.getPositionIds().size());
    assertEquals(positionId.getObjectId(), loadedNode.getPositionIds().get(0));
  }

  @Test
  public void existingSecurityWithNoUnderlying() {
    final UniqueId tradeId = _builder.addTrade(createTradeData(), BlotterTestUtils.FX_FORWARD_DATA_SOURCE, null, _nodeId);
    final BeanDataSource updatedTradeData = createTradeData("uniqueId", tradeId.toString(),
                                                      "counterparty", "updatedCounterparty",
                                                      "tradeDate", "2012-12-22",
                                                      "premium", "4321");
    final BeanDataSource updatedSecurityData = BlotterTestUtils.overrideBeanData(BlotterTestUtils.FX_FORWARD_DATA_SOURCE,
                                                                           "payCurrency", "AUD",
                                                                           "payAmount", "200");

    final UniqueId updatedTradeId = _builder.updateTrade(updatedTradeData, updatedSecurityData, null);
    final ManageableTrade updatedTrade = _positionMaster.getTrade(updatedTradeId);
    assertEquals("updatedCounterparty", updatedTrade.getCounterpartyExternalId().getValue());
    assertEquals(LocalDate.of(2012, 12, 22), updatedTrade.getTradeDate());
    assertEquals(4321d, updatedTrade.getPremium());
    final PositionDocument positionDocument = _positionMaster.get(updatedTrade.getParentPositionId());
    final ManageablePosition updatedPosition = positionDocument.getPosition();
    assertEquals(updatedTrade, updatedPosition.getTrade(updatedTradeId));
    final FXForwardSecurity updatedSecurity = (FXForwardSecurity) _securityMaster.get(updatedTrade.getSecurityLink().getObjectId(),
                                                                                VersionCorrection.LATEST).getSecurity();
    assertEquals(Currency.AUD, updatedSecurity.getPayCurrency());
    assertEquals(200d, updatedSecurity.getPayAmount());
  }

  @Test
  public void newSecurityWithOtcUnderlying() {
    final UniqueId tradeId = _builder.addTrade(createTradeData(),
                                         BlotterTestUtils.SWAPTION_DATA_SOURCE,
                                         BlotterTestUtils.SWAP_DATA_SOURCE,
                                         _nodeId);
    final ManageableTrade trade = _positionMaster.getTrade(tradeId);
    final UniqueId positionId = trade.getParentPositionId();
    final ManageablePosition position = _positionMaster.get(positionId).getPosition();
    assertEquals(BigDecimal.ONE, trade.getQuantity());
    assertEquals(BigDecimal.ONE, position.getQuantity());
    final SwaptionSecurity security = (SwaptionSecurity) _securityMaster.get(trade.getSecurityLink().getObjectId(),
                                                                       VersionCorrection.LATEST).getSecurity();
    assertNotNull(security);
    // check this later
    final ExternalId underlyingId = security.getUnderlyingId();
    security.setUniqueId(null); // so it can be tested for equality against the unsaved version
    security.setUnderlyingId(BlotterTestUtils.SWAPTION.getUnderlyingId()); // will need this later
    assertEquals(BlotterTestUtils.SWAPTION, security);
    assertEquals(COUNTERPARTY_ID, trade.getCounterpartyExternalId());
    assertEquals(PREMIUM, trade.getPremium());
    assertEquals(Currency.GBP, trade.getPremiumCurrency());
    assertEquals(PREMIUM_DATE, trade.getPremiumDate());
    assertEquals(TRADE_DATE, trade.getTradeDate());
    assertEquals(PREMIUM_TIME, trade.getPremiumTime());
    assertEquals(TRADE_TIME, trade.getTradeTime());
    assertEquals(ATTRIBUTES, trade.getAttributes());

    // can't check the node ID as nodes are completely replaced
    final ManageablePortfolioNode loadedRoot = _portfolioMaster.get(_savedPortfolio.getUniqueId()).getPortfolio().getRootNode();
    final ManageablePortfolioNode loadedNode = loadedRoot.getChildNodes().get(0);
    assertEquals(1, loadedNode.getPositionIds().size());
    assertEquals(positionId.getObjectId(), loadedNode.getPositionIds().get(0));

    final SecuritySearchRequest searchRequest = new SecuritySearchRequest();
    searchRequest.setExternalIdSearch(ExternalIdSearch.of(underlyingId));
    final SecuritySearchResult searchResult = _securityMaster.search(searchRequest);
    final ManageableSecurity underlying = searchResult.getSingleSecurity();
    final ExternalIdBundle underlyingBundle = underlying.getExternalIdBundle();
    // this isn't part of the equality check below because the test swaption can't know what the ID will be
    assertTrue(underlyingBundle.contains(underlyingId));
    // clear these values so we can do an equality check with the test swaption
    underlying.setUniqueId(null);
    underlying.setExternalIdBundle(ExternalIdBundle.EMPTY);
    assertEquals(BlotterTestUtils.SWAP, underlying);
  }

  @Test
  public void existingSecurityWithOtcUnderlying() {
    final UniqueId tradeId = _builder.addTrade(createTradeData(),
                                         BlotterTestUtils.SWAPTION_DATA_SOURCE,
                                         BlotterTestUtils.SWAP_DATA_SOURCE,
                                         _nodeId);
    final BeanDataSource updatedTradeData = createTradeData("uniqueId", tradeId.toString(),
                                                      "counterparty", "updatedCounterparty",
                                                      "tradeDate", "2012-12-22",
                                                      "premium", "4321");
    final BeanDataSource updatedSecurityData = BlotterTestUtils.overrideBeanData(BlotterTestUtils.SWAPTION_DATA_SOURCE,
                                                                           "payer", "false",
                                                                           "longShort", "Long",
                                                                           "currency", "CAD");
    final BeanDataSource updatedUnderlyingData = BlotterTestUtils.overrideBeanData(BlotterTestUtils.SWAP_DATA_SOURCE,
                                                                             "tradeDate", "2013-01-01",
                                                                             "eom", "false");

    final UniqueId updatedTradeId = _builder.updateTrade(updatedTradeData, updatedSecurityData, updatedUnderlyingData);
    final ManageableTrade updatedTrade = _positionMaster.getTrade(updatedTradeId);
    assertEquals("updatedCounterparty", updatedTrade.getCounterpartyExternalId().getValue());
    assertEquals(LocalDate.of(2012, 12, 22), updatedTrade.getTradeDate());
    assertEquals(4321d, updatedTrade.getPremium());
    final PositionDocument positionDocument = _positionMaster.get(updatedTrade.getParentPositionId());
    final ManageablePosition updatedPosition = positionDocument.getPosition();
    assertEquals(updatedTrade, updatedPosition.getTrade(updatedTradeId));
    final SwaptionSecurity updatedSecurity = (SwaptionSecurity) _securityMaster.get(updatedTrade.getSecurityLink().getObjectId(),
                                                                              VersionCorrection.LATEST).getSecurity();
    assertFalse(updatedSecurity.isPayer());
    assertEquals(LongShort.LONG, updatedSecurity.getLongShort());
    assertEquals(Currency.CAD, updatedSecurity.getCurrency());

    final ExternalId underlyingId = updatedSecurity.getUnderlyingId();
    final SecuritySearchRequest searchRequest = new SecuritySearchRequest();
    searchRequest.setExternalIdSearch(ExternalIdSearch.of(underlyingId));
    final SecuritySearchResult searchResult = _securityMaster.search(searchRequest);
    final SwapSecurity updatedUnderlying = (SwapSecurity) searchResult.getSingleSecurity();
    final ZonedDateTime tradeDate = ZonedDateTime.of(LocalDateTime.of(2013, 1, 1, 11, 0), ZoneOffset.UTC);
    assertEquals(tradeDate, updatedUnderlying.getTradeDate());
  }

  /**
   * directly update a position that has no trades
   */
  @Test
  @SuppressWarnings("deprecation")
  public void updatePositionWithNoTrade() {
    final ManageableSecurity security = _securityMaster.add(new SecurityDocument(BlotterTestUtils.FX_FORWARD)).getSecurity();
    final ManageablePosition position = new ManageablePosition();
    position.setQuantity(BigDecimal.ONE);
    position.setSecurityLink(new ManageableSecurityLink(security.getUniqueId()));
    final ManageablePosition savedPosition = _positionMaster.add(new PositionDocument(position)).getPosition();

    final BeanDataSource updatedTradeData = createTradeData("counterparty", "updatedCounterparty",
                                                      "tradeDate", "2012-12-22",
                                                      "premium", "4321");
    final BeanDataSource updatedSecurityData = BlotterTestUtils.overrideBeanData(BlotterTestUtils.FX_FORWARD_DATA_SOURCE,
                                                                           "payCurrency", "AUD",
                                                                           "payAmount", "200");

    _builder.updatePosition(savedPosition.getUniqueId(), updatedTradeData, updatedSecurityData, null);
    final ManageablePosition loadedPosition = _positionMaster.get(savedPosition.getUniqueId()).getPosition();
    assertEquals(1, loadedPosition.getTrades().size());
    final ManageableTrade trade = loadedPosition.getTrades().get(0);
    assertEquals("updatedCounterparty", trade.getCounterpartyExternalId().getValue());
    assertEquals(LocalDate.of(2012, 12, 22), trade.getTradeDate());
    assertEquals(4321d, trade.getPremium());
    final FXForwardSecurity updatedSecurity = (FXForwardSecurity) _securityMaster.get(trade.getSecurityLink().getObjectId(),
                                                                                VersionCorrection.LATEST).getSecurity();
    assertEquals(Currency.AUD, updatedSecurity.getPayCurrency());
    assertEquals(200d, updatedSecurity.getPayAmount());
  }
}
