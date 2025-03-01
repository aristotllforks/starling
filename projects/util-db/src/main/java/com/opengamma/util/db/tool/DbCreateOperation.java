/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.util.db.tool;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.util.db.script.DbScript;

/**
 * Creates database objects using the installation scripts.
 */
public class DbCreateOperation extends AbstractDbScriptOperation<DbToolContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DbCreateOperation.class);

  private final boolean _dropCatalog;

  /**
   * Constructs an instance.
   *
   * @param dbToolContext  the database tool context, not null
   * @param write  true to modify the database, false to output the commands that would be run
   * @param outputFile  the file to which the SQL should be written, null not to write to a file
   * @param dropCatalog  indicates whether to drop the catalog if it already exists
   */
  public DbCreateOperation(final DbToolContext dbToolContext, final boolean write, final File outputFile, final boolean dropCatalog) {
    super(dbToolContext, write, outputFile);
    _dropCatalog = dropCatalog;
  }

  //-------------------------------------------------------------------------
  @Override
  public void execute() {
    if (isDropCatalog()) {
      dropCatalog();
    }
    try (SqlScriptWriter writer = createSqlScriptWriter()) {
      final Set<String> schemaNames = getDbToolContext().getSchemaNames() != null ? getDbToolContext().getSchemaNames() : getAllSchemaNames();
      for (final String schema : schemaNames) {
        LOGGER.info("Processing schema " + schema);
        final DbScript script = getCreationScript(schema);
        LOGGER.debug("Using script: " + script);
        writer.write(schema, script);
      }
      LOGGER.info("Scripts processed successfully");
    } catch (final IOException e) {
      throw new OpenGammaRuntimeException("Error processing creation scripts", e);
    }
  }

  protected void dropCatalog() {
    contextNotNull(getDbToolContext().catalog());

    if (!isWrite()) {
      LOGGER.info("Would erase the contents of " + getDbToolContext().getCatalog() + " but skipping in read-only mode");
      return;
    }

    if (LOGGER.isInfoEnabled()) {
      // Give the user a chance to kill the script
      LOGGER.info("About to erase the contents of " + getDbToolContext().getCatalog() + "...");
      try {
        Thread.sleep(3000);
      } catch (final InterruptedException e) {
      }
    }

    LOGGER.info("Dropping contents of catalog " + getDbToolContext().getCatalog());
    getDbToolContext().getDbManagement().dropSchema(getDbToolContext().getCatalog(), null);
  }

  //-------------------------------------------------------------------------
  private boolean isDropCatalog() {
    return _dropCatalog;
  }

}
