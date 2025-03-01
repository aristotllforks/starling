/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.examples.simulated.loader;

import static com.opengamma.examples.simulated.loader.PortfolioLoaderHelper.getWithException;
import static com.opengamma.examples.simulated.loader.PortfolioLoaderHelper.normaliseHeaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.LocalDate;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.component.tool.AbstractTool;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.security.equity.EquitySecurity;
import com.opengamma.financial.security.equity.GICSCode;
import com.opengamma.financial.tool.ToolContext;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.master.portfolio.ManageablePortfolio;
import com.opengamma.master.portfolio.ManageablePortfolioNode;
import com.opengamma.master.portfolio.PortfolioDocument;
import com.opengamma.master.position.ManageablePosition;
import com.opengamma.master.position.ManageableTrade;
import com.opengamma.master.position.PositionDocument;
import com.opengamma.master.security.SecurityDocument;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.scripts.Scriptable;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.DateUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Example code to load a very simple equity portfolio.
 * <p>
 * This code is kept deliberately as simple as possible. There are no checks for the securities or portfolios already existing, so if you
 * run it more than once you will get multiple copies of portfolios and securities with the same names. It is designed to run against the
 * HSQLDB example database.
 */
@Scriptable
public class ExampleEquityPortfolioLoader extends AbstractTool<ToolContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleEquityPortfolioLoader.class);

  private static final Map<String, String> SECTORS = new HashMap<>();
  static {
    SECTORS.put("10", "10 Energy");
    SECTORS.put("15", "15 Materials");
    SECTORS.put("20", "20 Industrials");
    SECTORS.put("25", "25 Consumer discretionary");
    SECTORS.put("30", "30 Consumer staples");
    SECTORS.put("35", "35 Health care");
    SECTORS.put("40", "40 Financials");
    SECTORS.put("45", "45 Information technology");
    SECTORS.put("50", "50 Telecommunication");
    SECTORS.put("55", "55 Utilities");
  }

  /**
   * The name of the portfolio.
   */
  public static final String PORTFOLIO_NAME = "Equity Portfolio";

  private final LocalDate _tradeDate1;
  private final LocalDate _tradeDate2;
  private final LocalDate _tradeDate3;

  public ExampleEquityPortfolioLoader() {
    _tradeDate1 = DateUtils.nextWeekDay(LocalDate.now().minusDays(10));
    _tradeDate2 = DateUtils.nextWeekDay(_tradeDate1);
    _tradeDate3 = DateUtils.nextWeekDay(_tradeDate2);
  }

  // -------------------------------------------------------------------------
  /**
   * Main method to run the tool.
   *
   * @param args
   *          the standard tool arguments, not null
   */
  public static void main(final String[] args) { // CSIGNORE
    new ExampleEquityPortfolioLoader().invokeAndTerminate(args);
  }

  // -------------------------------------------------------------------------
  @Override
  protected void doRun() {
    // load all equity securities
    final Collection<EquitySecurity> securities = createAndPersistEquitySecurities();

    // create shell portfolio
    final ManageablePortfolio portfolio = createEmptyPortfolio();
    final ManageablePortfolioNode rootNode = portfolio.getRootNode();

    // add each security to the portfolio
    for (final EquitySecurity security : securities) {

      final GICSCode gics = security.getGicsCode();
      if (gics == null || gics.getCode().length() != 2) {
        continue;
      }
      final String sector = SECTORS.get(gics.getSectorCode());
      if (sector == null) {
        LOGGER.info("unrecognised sector code {}", gics.getSectorCode());
        continue;
      }
      // create portfolio structure
      ManageablePortfolioNode sectorNode = rootNode.findNodeByName(sector);
      if (sectorNode == null) {
        LOGGER.debug("Creating node for sector {}", sector);
        sectorNode = new ManageablePortfolioNode(sector);
        rootNode.addChildNode(sectorNode);
      }
      // create the position and add it to the master
      final ManageablePosition position = createPositionAndTrade(security);
      final PositionDocument addedPosition = addPosition(position);

      // add the position reference (the unique identifier) to portfolio
      sectorNode.addPosition(addedPosition.getUniqueId());
    }

    // adds the complete tree structure to the master
    addPortfolio(portfolio);
  }

  protected EquitySecurity createEquitySecurity(final String companyName, final Currency currency, final String exchange,
      final String exchangeCode,
      final String gicsCode, final ExternalId... identifiers) {
    final EquitySecurity equitySecurity = new EquitySecurity(exchange, exchangeCode, companyName, currency);
    equitySecurity.setGicsCode(GICSCode.of(gicsCode));
    equitySecurity.setExternalIdBundle(ExternalIdBundle.of(identifiers));
    equitySecurity.setName(companyName);
    return equitySecurity;
  }

  /**
   * Creates securities and adds them to the master.
   *
   * @return a collection of all securities that have been persisted, not null
   */
  protected Collection<EquitySecurity> createAndPersistEquitySecurities() {
    final SecurityMaster secMaster = getToolContext().getSecurityMaster();
    final Collection<EquitySecurity> securities = loadEquitySecurities();
    for (final EquitySecurity security : securities) {
      final SecurityDocument doc = new SecurityDocument(security);
      secMaster.add(doc);
    }
    return securities;
  }

  private Collection<EquitySecurity> loadEquitySecurities() {
    final Collection<EquitySecurity> equities = new ArrayList<>();
    final InputStream inputStream = ExampleEquityPortfolioLoader.class.getResourceAsStream("example-equity.csv");
    try {
      if (inputStream != null) {
        final CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));

        final String[] headers = csvReader.readNext();
        normaliseHeaders(headers);

        String[] line;
        int rowIndex = 1;
        while ((line = csvReader.readNext()) != null) {
          final Map<String, String> equityDetails = new HashMap<>();
          for (int i = 0; i < headers.length; i++) {
            if (i >= line.length) {
              // Run out of headers for this line
              break;
            }
            equityDetails.put(headers[i], line[i]);
          }
          try {
            equities.add(parseEquity(equityDetails));
          } catch (final Exception e) {
            LOGGER.warn("Skipped row " + rowIndex + " because of an error", e);
          }
          rowIndex++;
        }
      }
    } catch (final FileNotFoundException ex) {
      throw new OpenGammaRuntimeException("File '" + inputStream + "' could not be found");
    } catch (final IOException ex) {
      throw new OpenGammaRuntimeException("An error occurred while reading file '" + inputStream + "'");
    } finally {
      IOUtils.closeQuietly(inputStream);
    }

    final StringBuilder sb = new StringBuilder();
    sb.append("Parsed ").append(equities.size()).append(" equities:\n");
    for (final EquitySecurity equity : equities) {
      sb.append("\t").append(equity.getName()).append("\n");
    }
    LOGGER.info(sb.toString());

    return equities;
  }

  private EquitySecurity parseEquity(final Map<String, String> equityDetails) {
    final String companyName = getWithException(equityDetails, "companyname");
    final String currency = getWithException(equityDetails, "currency");
    final String exchange = getWithException(equityDetails, "exchange");
    final String exchangeCode = getWithException(equityDetails, "exchangecode");
    final String gicsCode = getWithException(equityDetails, "giscode");
    final String isin = getWithException(equityDetails, "isin");
    final String cusip = getWithException(equityDetails, "cusip");
    final String ticker = getWithException(equityDetails, "ticker");

    return createEquitySecurity(companyName, Currency.of(currency), exchange, exchangeCode, gicsCode,
        ExternalId.of(ExternalSchemes.ISIN, isin),
        ExternalId.of(ExternalSchemes.CUSIP, cusip),
        ExternalId.of(ExternalSchemes.OG_SYNTHETIC_TICKER, ticker));
  }

  /**
   * Create a empty portfolio.
   * <p>
   * This creates the portfolio and the root of the tree structure that holds the positions. Subsequent methods then populate the tree.
   *
   * @return the emoty portfolio, not null
   */
  protected ManageablePortfolio createEmptyPortfolio() {
    final ManageablePortfolio portfolio = new ManageablePortfolio(PORTFOLIO_NAME);
    final ManageablePortfolioNode rootNode = portfolio.getRootNode();
    rootNode.setName("Root");
    return portfolio;
  }

  /**
   * Create a position of a random number of shares.
   * <p>
   * This creates the position using a random number of units and create one or two trades making up the position.
   *
   * @param security
   *          the security to add a position for, not null
   * @return the position, not null
   */
  protected ManageablePosition createPositionAndTrade(final EquitySecurity security) {
    LOGGER.debug("Creating position {}", security);
    final int shares = (RandomUtils.nextInt(490) + 10) * 10;

    final ExternalIdBundle bundle = security.getExternalIdBundle(); // we could add an identifier pointing back to the original source
                                                                    // database if we're doing
    // an ETL.

    final ManageablePosition position = new ManageablePosition(BigDecimal.valueOf(shares), bundle);

    // create random trades that add up in shares to the position they're under (this is not enforced by the system)
    if (shares <= 2000) {
      final ManageableTrade trade = new ManageableTrade(BigDecimal.valueOf(shares), bundle, _tradeDate3, null,
          ExternalId.of("CPARTY", "BACS"));
      position.addTrade(trade);
    } else {
      final ManageableTrade trade1 = new ManageableTrade(BigDecimal.valueOf(2000), bundle, _tradeDate1, null,
          ExternalId.of("CPARTY", "BACS"));
      position.addTrade(trade1);
      final ManageableTrade trade2 = new ManageableTrade(BigDecimal.valueOf(shares - 2000), bundle, _tradeDate2, null,
          ExternalId.of("CPARTY", "BACS"));
      position.addTrade(trade2);
    }
    return position;
  }

  /**
   * Adds the position to the master.
   *
   * @param position
   *          the position to add, not null
   * @return the added document, not null
   */
  protected PositionDocument addPosition(final ManageablePosition position) {
    return getToolContext().getPositionMaster().add(new PositionDocument(position));
  }

  /**
   * Adds the portfolio to the master.
   *
   * @param portfolio
   *          the portfolio to add, not null
   * @return the added document, not null
   */
  protected PortfolioDocument addPortfolio(final ManageablePortfolio portfolio) {
    return getToolContext().getPortfolioMaster().add(new PortfolioDocument(portfolio));
  }

}
