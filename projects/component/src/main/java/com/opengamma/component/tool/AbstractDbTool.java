/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.tool;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.component.ComponentManager;
import com.opengamma.component.ComponentRepository;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.StartupUtils;
import com.opengamma.util.db.tool.DbToolContext;

/**
 * Abstract tool for operating on databases.
 * <p>
 * This type of tool requires a config-based database tool context in order to obtain direct access to a database.
 *
 * @param <T>  the type of database tool context
 */
public abstract class AbstractDbTool<T extends DbToolContext> {

  /**
   * The logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDbTool.class);

  /**
   * Help command line option.
   */
  private static final String HELP_OPTION = "h";
  /**
   * Configuration command line option.
   */
  private static final String CONFIG_RESOURCE_OPTION = "c";
  /**
   * Logging command line option.
   */
  private static final String LOGBACK_RESOURCE_OPTION = "l";
  /**
   * Write command line option.
   */
  private static final String WRITE_OPTION = "w";
  /**
   * Output file command line option.
   */
  private static final String OUTPUT_FILE_OPTION = "o";

  static {
    StartupUtils.init();
  }

  /**
   * The command line.
   */
  private CommandLine _commandLine;
  /**
   * The database tool context.
   */
  private T _dbToolContext;

  //-------------------------------------------------------------------------
  /**
   * Initializes the tool statically.
   *
   * @param logbackResource the logback resource location, not null
   * @return true if successful
   */
  public static final boolean init(final String logbackResource) {
    return ToolUtils.initLogback(logbackResource);
  }

  //-------------------------------------------------------------------------
  /**
   * Initializes and runs the tool from standard command-line arguments.
   * <p>
   * The base class defined three options:<br>
   * c/config - the config file, mandatory unless default specified<br>
   * l/logback - the logback configuration, default tool-logback.xml<br>
   * h/help - prints the help tool<br>
   *
   * @param args the command-line arguments, not null
   * @param toolContextClass the type of database tool context to create, should match the generic type argument
   * @return true if successful, false otherwise
   */
  public boolean initAndRun(final String[] args, final Class<? extends T> toolContextClass) {
    ArgumentChecker.notNull(args, "args");

    final Options options = createOptions();
    final CommandLineParser parser = new PosixParser();
    CommandLine line;
    try {
      line = parser.parse(options, args);
    } catch (final ParseException e) {
      usage(options);
      return false;
    }
    _commandLine = line;
    if (line.hasOption(HELP_OPTION)) {
      usage(options);
      return true;
    }
    String logbackResource = line.getOptionValue(LOGBACK_RESOURCE_OPTION);
    logbackResource = StringUtils.defaultIfEmpty(logbackResource, ToolUtils.getDefaultLogbackConfiguration());
    final String configResource = line.getOptionValue(CONFIG_RESOURCE_OPTION);
    return init(logbackResource) && run(configResource, toolContextClass);
  }

  /**
   * Runs the tool.
   *
   * @param configResource the config resource location, not null
   * @param dbToolContextClass the type of database tool context to create, should match the generic type argument
   * @return true if successful
   */
  public final boolean run(final String configResource, final Class<? extends T> dbToolContextClass) {
    T dbToolContext = null;
    try {
      ArgumentChecker.notEmpty(configResource, "configResource");
      LOGGER.info("Starting " + getClass().getSimpleName());
      dbToolContext = initDbToolContext(configResource, dbToolContextClass);
      LOGGER.info("Running " + getClass().getSimpleName());
      run(dbToolContext);
      LOGGER.info("Finished " + getClass().getSimpleName());
      return true;
    } catch (final Exception ex) {
      ex.printStackTrace();
      return false;
    } finally {
      if (dbToolContext != null) {
        dbToolContext.close();
      }
    }
  }

  /**
   * Runs the tool, calling {@code doRun}.
   * <p>
   * This will catch not handle exceptions, but will convert checked exceptions to unchecked.
   *
   * @param dbToolContext the database tool context, not null
   * @throws RuntimeException if an error occurs
   */
  public final void run(final T dbToolContext) {
    _dbToolContext = dbToolContext;
    try {
      final boolean write = getCommandLine().hasOption(WRITE_OPTION);
      final String outputFilePath = getCommandLine().getOptionValue(OUTPUT_FILE_OPTION);
      final File outputFile = outputFilePath != null ? new File(outputFilePath) : null;
      doRun(write, outputFile);
    } catch (final RuntimeException ex) {
      throw ex;
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  protected static <T extends DbToolContext> T initDbToolContext(final String configResource, final Class<T> dbToolContextClass) {
    final ComponentManager manager = new ComponentManager("dbtoolcontext");
    manager.start(configResource);
    final ComponentRepository repo = manager.getRepository();
    final T dbToolContext = repo.getInstance(dbToolContextClass, "tool");
    if (dbToolContext == null) {
      throw new OpenGammaRuntimeException("Unable to find tool context in config resource");
    }
    return dbToolContext;
  }

  //-------------------------------------------------------------------------
  /**
   * Override in subclasses to implement the tool.
   *
   * @param write  true to modify the database, false to output the commands that would be run
   * @param outputFile  the file to which the SQL should be written, null not to write to a file
   *
   * @throws Exception if an error occurs
   */
  protected abstract void doRun(boolean write, File outputFile) throws Exception;

  //-------------------------------------------------------------------------
  /**
   * Gets the database tool context.
   *
   * @return the context, not null during {@code doRun}
   */
  protected T getDbToolContext() {
    return _dbToolContext;
  }

  /**
   * Gets the parsed command line.
   *
   * @return the parsed command line, not null after parsing
   */
  protected CommandLine getCommandLine() {
    return _commandLine;
  }

  //-------------------------------------------------------------------------
  /**
   * Creates the command line options.
   * <p>
   * Subclasses may override this and add their own parameters. The base class defined the options h/help, c/config, l/logback.
   *
   * @return the set of command line options, not null
   */
  protected Options createOptions() {
    final Options options = new Options();
    options.addOption(createHelpOption());
    options.addOption(createConfigOption());
    options.addOption(createLogbackOption());
    options.addOption(createWriteOption());
    options.addOption(createOutputFileOption());
    return options;
  }

  private static Option createHelpOption() {
    return new Option(HELP_OPTION, "help", false, "prints this message");
  }

  private static Option createConfigOption() {
    final Option option = new Option(CONFIG_RESOURCE_OPTION, "config", true, "the database tool context configuration resource");
    option.setArgName("resource");
    option.setRequired(true);
    return option;
  }

  private static Option createLogbackOption() {
    final Option option = new Option(LOGBACK_RESOURCE_OPTION, "logback", true, "the logback configuration resource");
    option.setArgName("resource");
    option.setRequired(false);
    return option;
  }

  private static Option createWriteOption() {
    return new Option(WRITE_OPTION, "write", false, "whether to modify the database");
  }

  private static Option createOutputFileOption() {
    final Option option = new Option(OUTPUT_FILE_OPTION, "output", true, "a file to which the SQL should be output");
    option.setArgName("file");
    option.setRequired(false);
    return option;
  }

  protected void usage(final Options options) {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.setWidth(120);
    formatter.printHelp("java " + getClass().getName(), options, true);
  }

}
