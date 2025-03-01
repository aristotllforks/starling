package com.opengamma.examples.bloomberg;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.opengamma.util.ResourceUtils;
import com.opengamma.util.db.tool.DbTool;

/**
 * Test utilities.
 */
public final class DBTestUtils {

  private static final String DB_PASSWORD_KEY = "db.standard.password";
  private static final String DB_USERNAME_KEY = "db.standard.username";
  private static final String JDBC_URL_KEY = "db.standard.url";
  private static final Logger LOGGER = LoggerFactory.getLogger(DBTestUtils.class);

  private DBTestUtils() {
  }

  public static void createHsqlDB(final String configResourceLocation) throws IOException {
    final Properties props = loadProperties(configResourceLocation);

    final DbTool dbTool = new DbTool();
    dbTool.setCatalog("og-financial");
    dbTool.setJdbcUrl(props.getProperty(JDBC_URL_KEY));
    dbTool.setUser(props.getProperty(DB_USERNAME_KEY, ""));
    dbTool.setPassword(props.getProperty(DB_PASSWORD_KEY, ""));
    dbTool.setCreate(true);
    dbTool.setDrop(true);
    dbTool.setCreateTables(true);
    dbTool.execute();
  }

  private static Properties loadProperties(final String configResourceLocation) throws IOException {
    Resource resource = ResourceUtils.createResource(configResourceLocation);
    Properties props = new Properties();
    props.load(resource.getInputStream());

    final String nextConfiguration = props.getProperty("MANAGER.NEXT.FILE");
    if (nextConfiguration != null) {
      resource = ResourceUtils.createResource(nextConfiguration);
      final Properties parentProps = new Properties();
      parentProps.load(resource.getInputStream());
      for (final String key : props.stringPropertyNames()) {
        parentProps.put(key, props.getProperty(key));
      }
      props = parentProps;
    }

    for (final String key : props.stringPropertyNames()) {
      LOGGER.debug("\t{}={}", key, props.getProperty(key));
    }

    return props;
  }

  public static void cleanUp(final String configResourceLocation) throws IOException {
    dropDatabase(configResourceLocation);
  }

  private static void dropDatabase(final String configResourceLocation) throws IOException {
    final Properties props = loadProperties(configResourceLocation);
    final DbTool dbTool = new DbTool();
    dbTool.setCatalog("og-financial");
    dbTool.setJdbcUrl(props.getProperty(JDBC_URL_KEY));
    dbTool.setUser(props.getProperty(DB_USERNAME_KEY, ""));
    dbTool.setPassword(props.getProperty(DB_PASSWORD_KEY, ""));
    dbTool.setDrop(true);
    dbTool.execute();
  }

  public static String getJettyPort(final String configResourceLocation) throws IOException {
    final Properties props = loadProperties(configResourceLocation);
    return props.getProperty("jetty.port");
  }

}
