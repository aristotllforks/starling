/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.regression;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;
import org.fudgemsg.wire.FudgeMsgReader;
import org.fudgemsg.wire.FudgeMsgWriter;
import org.fudgemsg.wire.xml.FudgeXMLStreamReader;
import org.fudgemsg.wire.xml.FudgeXMLStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;

import com.google.common.collect.Lists;
import com.opengamma.scripts.Scriptable;
import com.opengamma.util.fudgemsg.OpenGammaFudgeContext;
import com.opengamma.util.tuple.Pair;

/**
 *
 */
@Scriptable
public class ViewRegressionTestTool {

  public static final double DELTA = 0.0001;

  private static final Options OPTIONS = createOptions();
  private static final String PROJECT_NAME = "pn";
  private static final String SERVER_CONFIG = "sc";
  private static final String DB_DUMP_DIR = "dd";
  private static final String LOGBACK_CONFIG = "l";
  private static final String VALUATION_TIME = "vt";
  private static final String BASE_DIR = "bd";
  private static final String TEST_DIR = "td";
  private static final String BASE_VERSION = "bv";
  private static final String TEST_VERSION = "tv";
  private static final String BASE_PROPS = "bp";
  private static final String TEST_PROPS = "tp";
  private static final String HELP = "h";
  private static final String REPORT_FILE = "rf";
  private static final String DEFAULT_REPORT_FILE = "regression-report.txt";
  private static final String RESULT_OUT = "resultout";
  private static final String RESULT_IN = "resultin";

  private static final Logger LOGGER = LoggerFactory.getLogger(ViewRegressionTestTool.class);

  /**
   * Main method to run the tool.
   *
   * @param args
   *          the arguments, unused
   * @throws Exception
   *           if there is a problem
   */
  public static void main(final String[] args) throws Exception { // CSIGNORE
    try {
      final TestStatus status = ViewRegressionTestTool.run(args);
      System.exit(status.ordinal());
    } catch (final Exception e) {
      e.printStackTrace();
      System.exit(TestStatus.ERROR.ordinal());
    }
  }

  private static void printUsage() {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.setWidth(120);
    formatter.printHelp("java " + ViewRegressionTestTool.class.getName(), OPTIONS, true);
  }

  private static TestStatus run(final String[] args) throws Exception {
    final CommandLineParser parser = new PosixParser();
    CommandLine cl;
    try {
      cl = parser.parse(OPTIONS, args);
    } catch (final ParseException e) {
      printUsage();
      return TestStatus.ERROR;
    }
    if (cl.hasOption(HELP)) {
      printUsage();
      return TestStatus.ERROR;
    }
    if (cl.hasOption(RESULT_OUT) && (!cl.hasOption(BASE_DIR) || !cl.hasOption(BASE_VERSION) || !cl.hasOption(BASE_PROPS))) {
      printUsage();
      return TestStatus.ERROR;
    }
    if (cl.hasOption(RESULT_IN) && (!cl.hasOption(TEST_DIR) || !cl.hasOption(TEST_VERSION) || !cl.hasOption(TEST_PROPS))) {
      printUsage();
      return TestStatus.ERROR;
    }
    if (!cl.hasOption(RESULT_IN) && !cl.hasOption(RESULT_OUT) && (!cl.hasOption(BASE_DIR) || !cl.hasOption(BASE_VERSION) || !cl.hasOption(BASE_PROPS)
        || !cl.hasOption(TEST_DIR) || !cl.hasOption(TEST_VERSION) || !cl.hasOption(TEST_PROPS))) {
      printUsage();
      return TestStatus.ERROR;
    }
    Instant valuationTime;
    if (cl.hasOption(VALUATION_TIME)) {
      valuationTime = Instant.parse(cl.getOptionValue(VALUATION_TIME));
    } else {
      // NOTE jonathan 2013-12-10 -- imply the valuation time from the market data source.
      // We should never be using Instant.now() as the valuation time in regression tests - the results would be meaningless.
      valuationTime = null;
    }
    if (cl.hasOption(RESULT_OUT) || cl.hasOption(RESULT_IN)) {
      final CreateGoldenCopyForRegressionTest test;
      if (cl.hasOption(RESULT_OUT)) {
        test = new CreateGoldenCopyForRegressionTest(cl.getOptionValue(PROJECT_NAME),
            cl.getOptionValue(SERVER_CONFIG),
            cl.getOptionValue(DB_DUMP_DIR),
            cl.getOptionValue(LOGBACK_CONFIG),
            valuationTime,
            cl.getOptionValue(BASE_DIR),
            cl.getOptionValue(BASE_VERSION),
            cl.getOptionValue(BASE_PROPS));
      } else {
        test = new CreateGoldenCopyForRegressionTest(cl.getOptionValue(PROJECT_NAME),
            cl.getOptionValue(SERVER_CONFIG),
            cl.getOptionValue(DB_DUMP_DIR),
            cl.getOptionValue(LOGBACK_CONFIG),
            valuationTime,
            cl.getOptionValue(TEST_DIR),
            cl.getOptionValue(TEST_VERSION),
            cl.getOptionValue(TEST_PROPS));
      }

      final Map<Pair<String, String>, CalculationResults> results = test.run();

      if (cl.hasOption(RESULT_OUT)) {
        final String resultFileName = cl.getOptionValue(RESULT_OUT);

        final FudgeSerializer serializer = new FudgeSerializer(OpenGammaFudgeContext.getInstance());
        try (FileWriter writer = new FileWriter(new File(resultFileName))) {
          final FudgeXMLStreamWriter streamWriter = new FudgeXMLStreamWriter(OpenGammaFudgeContext.getInstance(), writer);
          final FudgeMsgWriter fudgeMsgWriter = new FudgeMsgWriter(streamWriter);
          final MutableFudgeMsg msg = serializer.objectToFudgeMsg(results);
          fudgeMsgWriter.writeMessage(msg);
          writer.append("\n");
          fudgeMsgWriter.flush();
        }
      }
      if (cl.hasOption(RESULT_IN)) {
        final String inputFileName = cl.getOptionValue(RESULT_IN);

        final FudgeDeserializer deserializer = new FudgeDeserializer(OpenGammaFudgeContext.getInstance());

        try (FileReader reader = new FileReader(new File(inputFileName))) {
          final FudgeXMLStreamReader streamReader = new FudgeXMLStreamReader(OpenGammaFudgeContext.getInstance(), reader);
          final FudgeMsgReader fudgeMsgReader = new FudgeMsgReader(streamReader);
          final RegressionTestResults regressionTestResults;
          if (fudgeMsgReader.hasNext()) {
            final FudgeMsg fudgeMsg = fudgeMsgReader.nextMessage();
            @SuppressWarnings("unchecked")
            final Map<Pair<String, String>, CalculationResults> exemplarResults = (Map<Pair<String, String>, CalculationResults>) deserializer.fudgeMsgToObject(
                fudgeMsg);
            final List<CalculationDifference> calculationDifferences = Lists.newArrayList();
            for (final Map.Entry<Pair<String, String>, CalculationResults> entry : results.entrySet()) {
              final CalculationResults testViewResult = entry.getValue();
              final CalculationResults exemplarResult = exemplarResults.get(entry.getKey());
              if (exemplarResult == null) {
                LOGGER.warn("No exemplar result for {}", entry.getKey());
                continue;
              }
              calculationDifferences.add(CalculationDifference.between(exemplarResult, testViewResult, DELTA));
            }
            regressionTestResults = new RegressionTestResults(cl.getOptionValue(TEST_VERSION),
                cl.getOptionValue(TEST_VERSION),
                calculationDifferences);
          } else {
            regressionTestResults = new RegressionTestResults(cl.getOptionValue(TEST_VERSION),
                cl.getOptionValue(TEST_VERSION),
                Collections.<CalculationDifference> emptyList());
          }

          try (Writer writer = new BufferedWriter(new FileWriter(cl.getOptionValue(REPORT_FILE,
              DEFAULT_REPORT_FILE)))) {
            ReportGenerator.generateReport(regressionTestResults, ReportGenerator.Format.TEXT, writer);
          }

          return regressionTestResults.getStatus();

        }
      }
      return TestStatus.PASS;
    }
    final ViewRegressionTest test = new ViewRegressionTest(cl.getOptionValue(PROJECT_NAME), cl.getOptionValue(SERVER_CONFIG), cl.getOptionValue(DB_DUMP_DIR),
        cl.getOptionValue(LOGBACK_CONFIG), valuationTime, cl.getOptionValue(BASE_DIR), cl.getOptionValue(BASE_VERSION), cl.getOptionValue(BASE_PROPS),
        cl.getOptionValue(TEST_DIR), cl.getOptionValue(TEST_VERSION), cl.getOptionValue(TEST_PROPS));
    final RegressionTestResults results = test.run();
    try (Writer writer = new BufferedWriter(new FileWriter(cl.getOptionValue(REPORT_FILE, DEFAULT_REPORT_FILE)))) {
      ReportGenerator.generateReport(results, ReportGenerator.Format.TEXT, writer);
    }
    /*
     * FudgeSerializer serializer = new FudgeSerializer(OpenGammaFudgeContext.getInstance()); try (FileWriter writer = new FileWriter(new
     * File("/Users/chris/tmp/regression/results.xml"))) { FudgeXMLStreamWriter streamWriter = new FudgeXMLStreamWriter(OpenGammaFudgeContext.getInstance(),
     * writer); FudgeMsgWriter fudgeMsgWriter = new FudgeMsgWriter(streamWriter); MutableFudgeMsg msg = serializer.objectToFudgeMsg(results);
     * FudgeSerializer.addClassHeader(msg, results.getClass()); fudgeMsgWriter.writeMessage(msg); writer.append("\n"); fudgeMsgWriter.flush(); }
     */
    return results.getStatus();
  }

  private static Options createOptions() {
    final Options options = new Options();

    // TODO extra options - java executable, arbitrary additional jvm args (for memory, GC config etc)
    final Option resultout = new Option(RESULT_OUT,
        "resultout",
        true,
        "Flag indicationg whether and into what file, store the results of running singe server tests as opposed to run "
            + "regression test comparing two servers.");
    resultout.setRequired(false);
    options.addOption(resultout);

    final Option resultin = new Option(RESULT_IN,
        "resultin",
        true,
        "Flag indicationg whether and into what file, store the results of running singe server tests as opposed to run "
            + "regression test comparing two servers.");
    resultin.setRequired(false);
    options.addOption(resultin);

    final Option projectNameOption = new Option(PROJECT_NAME,
        "projectname",
        true,
        "Project name (as used in the build artifacts)");
    projectNameOption.setRequired(true);
    options.addOption(projectNameOption);

    final Option serverConfigOption = new Option(SERVER_CONFIG,
        "serverconfig",
        true,
        "Configuration file used to run the server");
    serverConfigOption.setRequired(true);
    options.addOption(serverConfigOption);

    final Option dbDumpDirOption = new Option(DB_DUMP_DIR,
        "dbdumpdir",
        true,
        "Directory containing the database dump files."
            + " If this is omitted the database won't be created or populated, the existing database will be used");
    options.addOption(dbDumpDirOption);

    final Option logbackConfigOption = new Option(LOGBACK_CONFIG, "logbackconfig", true, "Logback config for the servers");
    options.addOption(logbackConfigOption);

    final Option valuationTimeOption = new Option(VALUATION_TIME, "valuationtime", true, "Valuation time for the views");
    options.addOption(valuationTimeOption);

    final Option baseDirOption = new Option(BASE_DIR,
        "basedir",
        true,
        "Installation directory for the base version of the server");
    // baseDirOption.setRequired(true);
    options.addOption(baseDirOption);

    final Option newDirOption = new Option(TEST_DIR,
        "testdir",
        true,
        "Installation directory for the test version of the server");
    // newDirOption.setRequired(true);
    options.addOption(newDirOption);

    final Option baseVersionOption = new Option(BASE_VERSION, "baseversion", true, "Version of the base server");
    // baseVersionOption.setRequired(true);
    options.addOption(baseVersionOption);

    final Option newVersionOption = new Option(TEST_VERSION, "testversion", true, "Version of the test server");
    // newVersionOption.setRequired(true);
    options.addOption(newVersionOption);

    final Option basePropsOption = new Option(BASE_PROPS, "baseprops", true, "The DB properties file for the base server");
    // basePropsOption.setRequired(true);
    options.addOption(basePropsOption);

    final Option newPropsOption = new Option(TEST_PROPS, "testprops", true, "The DB properties file for the test server");
    // newPropsOption.setRequired(true);
    options.addOption(newPropsOption);

    final Option helpOption = new Option(HELP, "help", true, "Print usage");
    options.addOption(helpOption);

    final Option reportFileOption = new Option(REPORT_FILE, "reportfile", true, "File name of the test results report");
    options.addOption(reportFileOption);

    return options;
  }
}
