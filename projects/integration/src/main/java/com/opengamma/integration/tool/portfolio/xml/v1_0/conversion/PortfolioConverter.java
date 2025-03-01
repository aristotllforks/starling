/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.tool.portfolio.xml.v1_0.conversion;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.opengamma.core.security.Security;
import com.opengamma.id.ExternalId;
import com.opengamma.integration.tool.portfolio.xml.PortfolioPosition;
import com.opengamma.integration.tool.portfolio.xml.XmlExternalIdValidator;
import com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb.AdditionalCashflow;
import com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb.IdWrapper;
import com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb.ListedSecurityDefinition;
import com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb.MonetaryAmount;
import com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb.Portfolio;
import com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb.Position;
import com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb.Trade;
import com.opengamma.master.position.ManageablePosition;
import com.opengamma.master.position.ManageableTrade;
import com.opengamma.master.security.ManageableSecurity;

/**
 * Converts a portfolio from the JAXB extracted objects to the standard system objects.
 */
public class PortfolioConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioConverter.class);

  /**
   * The portfolio to be converted.
   */
  private final Portfolio _portfolio;

  private final XmlExternalIdValidator _xmlExternalIdValidator;

  public PortfolioConverter(final Portfolio portfolio, final XmlExternalIdValidator xmlExternalIdValidator) {
    _portfolio = portfolio;
    _xmlExternalIdValidator = xmlExternalIdValidator;

  }

  /**
   * Get the set of manageable positions for this portfolio. Note that this may add in positions which were not in the original xml file e.g. where a set of
   * trades were specified but no positions, each trade will be added to a new position.
   *
   * @return the positions, not null
   */
  public Iterable<PortfolioPosition> getPositions() {
    LOGGER.info("Starting to process root portfolio: {}", _portfolio.getName());
    return processPortfolio(_portfolio, true, null);
  }

  /**
   * Produce the collection of positions held in a portfolio. A portfolio may consist of any combination of:
   * <ul>
   * <li>recursively nested portfolios</li>
   * <li>positions (which may or may not contain trades)</li>
   * <li>trades</li>
   * </ul>
   * and this method will produce a List of PortfolioPosition objects by examining the above elements. If portfolios are nested, this method will be called
   * recursively noting the portfolio path as the portfolio hieracrhy is decended. Note that this may add in positions which were not in the original xml file
   * e.g. where a set of trades were specified but no positions, each trade will be added to a new position.
   *
   * @param portfolio
   *          the portfolio to be examined
   * @param isRoot
   *          indicates if the portfolio has no parent i.e. a root node
   * @param parentPath
   *          the path to the portfolio (an array of the names of the portfolio's ancestors).
   * @return the positions held by the portfolio
   */
  private List<PortfolioPosition> processPortfolio(final Portfolio portfolio, final boolean isRoot, final String[] parentPath) {

    LOGGER.info("Processing portfolio: {}", portfolio.getName());
    final List<PortfolioPosition> managedPositions = Lists.newArrayList();

    // This is needed as we don't want the name of the root portfolio to appear in the path. So for
    // a root portfolio we want an empty path, for a child of the root we want just the portfolio name etc.
    final String[] portfolioPath = isRoot ? new String[0] : extendPath(parentPath, portfolio.getName());

    for (final Portfolio nestedPortfolio : nullCheckIterable(portfolio.getPortfolios())) {

      managedPositions.addAll(processPortfolio(nestedPortfolio, false, portfolioPath));
    }

    for (final Position position : nullCheckIterable(portfolio.getPositions())) {

      final IdWrapper positionExternalId = position.getExternalSystemId();
      final String positionId = positionExternalId != null ? positionExternalId.toExternalId().toString() : "AUTO-CREATED";
      LOGGER.debug("Extracting position: [{}]", positionId);

      final List<Trade> trades = position.getTrades();
      BigDecimal tradeTotalQuantity = BigDecimal.ZERO;

      // If we have a security defined on the position then we need to
      // check it matches the one from the trades (if there was one)
      ManageableSecurity[] positionSecurity = extractSecurityFromPosition(position.getListedSecurityDefinition());

      for (final Trade trade : nullCheckIterable(trades)) {

        LOGGER.debug("Extracting trade: {} for position {}", trade.getExternalSystemId().getExternalId(), positionId);
        final ManageableSecurity[] tradeSecurity = extractSecurityFromTrade(trade, trades.size());
        if (positionSecurity == null) {

          positionSecurity = tradeSecurity;

        } else if (!Arrays.equals(positionSecurity, tradeSecurity)) {

          throw new PortfolioParsingException("Security must be the same for all trades grouped into a position - "
              + "position has security: [" + positionSecurity[0]
              + "] but found trade with: [" + tradeSecurity[0] + "]");
        }

        tradeTotalQuantity = tradeTotalQuantity.add(trade.getQuantity());
      }

      if (positionSecurity != null) {
        managedPositions.add(createPortfolioPosition(position, positionSecurity, portfolioPath, tradeTotalQuantity));
      } else {
        throw new PortfolioParsingException("No security specified on either trades or position");
      }
    }

    // These trades have not been supplied under positions, but directly in a portfolio
    for (final Trade trade : nullCheckIterable(portfolio.getTrades())) {

      // TODO we probably want logic to allow for the aggregation of trades into positions but for now we'll create a position per trade
      final ManageableSecurity[] security = extractSecurityFromTrade(trade, 1);
      managedPositions.add(createPortfolioPosition(trade, security, portfolioPath));
    }

    return managedPositions;
  }

  /**
   * Create a new array which is a copy of the path array with the name parameter appended at the end.
   *
   * @param path
   *          the path array to be extended
   * @param name
   *          the element to append to the array
   * @return the extended array
   */
  private String[] extendPath(final String[] path, final String name) {

    final int oldLength = path.length;
    final String[] extended = Arrays.copyOf(path, oldLength + 1);
    extended[oldLength] = name;
    return extended;
  }

  private ManageableSecurity[] extractSecurityFromPosition(final ListedSecurityDefinition listedSecurityDefinition) {

    if (listedSecurityDefinition != null) {
      LOGGER.debug("Extracting securities for position");
      return listedSecurityDefinition.getSecurityExtractor().extract();
    }
    return null;
  }

  private ManageableSecurity[] extractSecurityFromTrade(final Trade trade, final int tradesSize) {

    if (tradesSize > 1 && !trade.canBePositionAggregated()) {
      throw new PortfolioParsingException("Trade type [" + trade.getClass()
          + "] cannot be aggregated into positions");
    }

    LOGGER.debug("Extracting securities for trade: [{}]", trade.getExternalSystemId().toExternalId());
    final TradeSecurityExtractor<?> extractor = trade.getSecurityExtractor();
    return extractor.extractSecurities();
  }

  private <T> Iterable<T> nullCheckIterable(final Iterable<T> iterable) {
    return iterable == null ? ImmutableList.<T> of() : iterable;
  }

  private PortfolioPosition createPortfolioPosition(final Position position,
      final ManageableSecurity[] security,
      final String[] parentPath,
      final BigDecimal tradeQuantity) {
    return new PortfolioPosition(convertPosition(position, security[0], tradeQuantity), security, parentPath);
  }

  private PortfolioPosition createPortfolioPosition(final Trade trade, final ManageableSecurity[] security, final String[] parentPath) {
    return new PortfolioPosition(convertTradeToPosition(trade, security[0]), security, parentPath);
  }

  private ManageablePosition convertTradeToPosition(final Trade trade, final ManageableSecurity security) {
    final ManageablePosition manageablePosition = new ManageablePosition(trade.getQuantity(), security.getExternalIdBundle());
    manageablePosition.addTrade(convertTrade(trade, security));
    return manageablePosition;
  }

  private ManageablePosition convertPosition(final Position position, final Security security, final BigDecimal tradeQuantity) {

    // If the position is supplying a quantity, then we should use that
    // rather than the total quantity obtained from the trades
    final BigDecimal positionQuantity = position.getQuantity();
    final ManageablePosition manageablePosition = new ManageablePosition(
        positionQuantity != null ? positionQuantity : tradeQuantity, security.getExternalIdBundle());

    final IdWrapper externalSystemId = position.getExternalSystemId();
    if (externalSystemId != null) {
      manageablePosition.setProviderId(externalSystemId.toExternalId());
    }

    final List<Trade> trades = position.getTrades();
    for (final Trade trade : nullCheckIterable(trades)) {
      manageablePosition.addTrade(convertTrade(trade, security));
    }

    manageablePosition.setAttributes(position.getAdditionalAttributes());

    return manageablePosition;
  }

  private ManageableTrade convertTrade(final Trade trade, final Security security) {

    final ManageableTrade manageableTrade = new ManageableTrade(trade.getQuantity(),
        security.getExternalIdBundle(),
        trade.getTradeDate(),
        null,
        trade.getCounterparty().toExternalId());

    final ExternalId externalId = trade.getExternalSystemId().toExternalId();

    _xmlExternalIdValidator.validateExternalId(externalId, trade.getId());

    manageableTrade.setProviderId(externalId);

    for (final AdditionalCashflow cashflow : nullCheckIterable(trade.getAdditionalCashflows())) {

      if (cashflow.getCashflowType() == AdditionalCashflow.CashflowType.PREMIUM) {
        final MonetaryAmount monetaryAmount = cashflow.getMonetaryAmount();
        manageableTrade.setPremium(monetaryAmount.getAmount().doubleValue());
        manageableTrade.setPremiumCurrency(monetaryAmount.getCurrency());
        manageableTrade.setPremiumDate(cashflow.getCashflowDate());
      }
    }

    manageableTrade.setAttributes(trade.getAdditionalAttributes());

    return manageableTrade;
  }

}
