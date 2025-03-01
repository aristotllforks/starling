/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.server.copier;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.opengamma.scripts.Scriptable;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.ResourceUtils;
import com.opengamma.util.db.tool.DbTool;

/**
 * Tool class that creates a database based on the given toolcontext property file and
 * initialize the database with data from a given server URL.
 */
@Scriptable
public class ServerDatabaseCreator {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerDatabaseCreator.class);

  /** Shared database URL. */
  private static final String KEY_SHARED_URL = "db.standard.url";
  /** Shared database user name. */
  private static final String KEY_SHARED_USER_NAME = "db.standard.username";
  /** Shared database password. */
  private static final String KEY_SHARED_PASSWORD = "db.standard.password";
  /** Temporary user database URL. */
  private static final String KEY_USERFINANCIAL_URL = "db.userfinancial.url";
  /** Temporary user database user name. */
  private static final String KEY_USERFINANCIAL_USER_NAME = "db.userfinancial.username";
  /** Temporary user database password. */
  private static final String KEY_USERFINANCIAL_PASSWORD = "db.userfinancial.password";
  /** Catalog. */
  private static final String CATALOG = "og-financial";

  private final String _configFile;

  private final String _serverUrl;

  public ServerDatabaseCreator(String configFile, String serverUrl) {
    configFile = StringUtils.trimToNull(configFile);
    serverUrl = StringUtils.trimToNull(serverUrl);
    ArgumentChecker.notNull(configFile, "configFile");
    ArgumentChecker.notNull(serverUrl, "serverUrl");

    _configFile = configFile;
    _serverUrl = serverUrl;
  }

  //-------------------------------------------------------------------------
  /**
   * Main method to run the tool. No arguments are needed.
   * <p>
   * If the command line is empty, the "development" configuration file is started.
   * This file is intended for use with an IDE and a checked out source code tree.
   * It relies on the <code>web</code> directory being relative to <code>integration</code> in the file
   * system as per a standard checkout of OG-Platform.
   *
   * @param args the arguments, unused
   */
  public static void main(final String[] args) { // CSIGNORE
    try {
      final CommandLineOption option = new CommandLineOption(args, ServerDatabaseCreator.class);
      final String configFile = option.getConfigFile();
      final String serverUrl = option.getServerUrl();
      if (configFile != null && serverUrl != null) {
        final ServerDatabaseCreator databaseCreator = new ServerDatabaseCreator(configFile, serverUrl);
        databaseCreator.run();
      }
      System.exit(0);
    } catch (final Exception ex) {
      LOGGER.error("Caught exception", ex);
      ex.printStackTrace();
      System.exit(1);
    }
  }

  //-------------------------------------------------------------------------
  public void run() throws Exception {
    final Resource res = ResourceUtils.createResource(_configFile);
    final Properties props = new Properties();
    try (InputStream in = res.getInputStream()) {
      if (in == null) {
        throw new FileNotFoundException(_configFile);
      }
      props.load(in);
    }

    // create main database
    LOGGER.info("Creating main database...");
    final DbTool dbTool = new DbTool();
    dbTool.setJdbcUrl(Objects.requireNonNull(props.getProperty(KEY_SHARED_URL)));
    dbTool.setUser(props.getProperty(KEY_SHARED_USER_NAME, ""));
    dbTool.setPassword(props.getProperty(KEY_SHARED_PASSWORD, ""));
    dbTool.setCatalog(CATALOG);  // ignored, as it is parsed from the url
    dbTool.setCreate(true);
    dbTool.setDrop(true);
    dbTool.setCreateTables(true);
    dbTool.execute();

    // create user database
    LOGGER.info("Creating user database...");
    final DbTool dbToolUser = new DbTool();
    dbToolUser.setJdbcUrl(Objects.requireNonNull(props.getProperty(KEY_USERFINANCIAL_URL)));
    dbToolUser.setUser(props.getProperty(KEY_USERFINANCIAL_USER_NAME, ""));
    dbToolUser.setPassword(props.getProperty(KEY_USERFINANCIAL_PASSWORD, ""));
    dbToolUser.setCatalog(CATALOG);  // ignored, as it is parsed from the url
    dbToolUser.setCreate(true);
    dbToolUser.setDrop(true);
    dbToolUser.setCreateTables(true);
    dbToolUser.execute();

    // populate the database
    LOGGER.info("Populating main database...");
    final ServerDatabasePopulator populator = new ServerDatabasePopulator(_configFile, new DatabasePopulatorTool(_serverUrl));
    populator.run();
    LOGGER.info("Successfully created server databases");
  }

}
