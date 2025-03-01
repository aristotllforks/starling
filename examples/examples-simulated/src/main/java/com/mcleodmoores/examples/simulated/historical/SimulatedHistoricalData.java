/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.mcleodmoores.examples.simulated.historical;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.id.ExternalId;
import com.opengamma.util.tuple.Pair;
import com.opengamma.util.tuple.Pairs;

import au.com.bytecode.opencsv.CSVReader;

/**
 * An ultra-simple historical data simulator, we load the initial values from a CSV file (with a header row) and the format:
 *
 * <pre>
 *   <identification-scheme>, <identifier-value>, <datafield>, <value>.
 * </pre>
 */
public class SimulatedHistoricalData {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedHistoricalData.class);

  /**
   * OG Simulated data provider name.
   */
  public static final String OG_DATA_PROVIDER = "OG_DATA_PROVIDER";

  /**
   * OG Simulated data source name.
   */
  public static final String OG_DATA_SOURCE = "OG_DATA_SOURCE";

  private final Map<Pair<ExternalId, String>, Double> _finishValues = new HashMap<>();

  private static final int NUM_FIELDS = 4;
  private static final double SCALING_FACTOR = 0.005; // i.e. 0.5% * 1SD

  /**
   * Default constructor.
   */
  public SimulatedHistoricalData() {
    readFinishValues(_finishValues);
  }

  private static void readFinishValues(final Map<Pair<ExternalId, String>, Double> finishValues) {
    CSVReader reader = null;
    try (InputStream resource = SimulatedHistoricalDataGenerator.class.getResourceAsStream("historical-data.csv")) {
      if (resource == null) {
        throw new OpenGammaRuntimeException("Unable to find resource historical-data.csv");
      }
      reader = new CSVReader(new BufferedReader(new InputStreamReader(resource)));
      // Read header row
      @SuppressWarnings("unused")
      final String[] headers = reader.readNext();
      String[] line;
      int lineNum = 0;
      while ((line = reader.readNext()) != null) {
        try {
          lineNum++;
          if (line.length == 0 || line[0].startsWith("#")) {
            LOGGER.debug("Empty line on {}", lineNum);
          } else if (line.length < NUM_FIELDS) {
            LOGGER.error("Invalid number of fields ({}) in CSV on line {}", line.length, lineNum);
          } else {
            final String scheme = line[0];
            final String identifier = line[1];
            final String fieldName = line[2];
            final String valueStr = line[3];
            final Double value = Double.parseDouble(valueStr);
            final ExternalId id = ExternalId.of(scheme, identifier);
            finishValues.put(Pairs.of(id, fieldName), value);
          }
        } catch (final Exception e) {
          LOGGER.error("Problem parsing line {}: {}", lineNum, e.getMessage());
        }
      }
    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the last values.
   *
   * @return the values
   */
  public Map<Pair<ExternalId, String>, Double> getFinishValues() {
    return _finishValues;
  }

}
