/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 *
 * Modified by McLeod Moores Software Limited.
 *
 * Copyright (C) 2018 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.integration.copier.sheet.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.integration.copier.sheet.SheetFormat;
import com.opengamma.util.ArgumentChecker;

/**
 * An abstract table class for importing portfolio data from spreadsheets.
 */
public abstract class SheetReader {

  private String[] _columns; // The column names and order

  /**
   * Creates a sheet reader that is specific to the sheet format.
   *
   * @param sheetFormat  the sheet format, not null
   * @param inputStream  the portfolio data, not null
   * @return  the sheet reader
   */
  public static SheetReader newSheetReader(final SheetFormat sheetFormat, final InputStream inputStream) {
    ArgumentChecker.notNull(sheetFormat, "sheetFormat");
    ArgumentChecker.notNull(inputStream, "inputStream");

    switch (sheetFormat) {
      case CSV:
        return new CsvSheetReader(inputStream);
      case XLS:
        return new XlsSheetReader(inputStream, 0);
      default:
        throw new OpenGammaRuntimeException("Could not create a reader for the sheet input format " + sheetFormat.toString());
    }
  }

  /**
   * Creates a sheet reader that is specific to the sheet format.
   *
   * @param filename  the portfolio data file name, not null or empty
   * @return  the sheet reader
   */
  public static SheetReader newSheetReader(final String filename) {
    ArgumentChecker.notEmpty(filename, "filename");
    final InputStream inputStream = openFile(filename);
    return newSheetReader(SheetFormat.of(filename), inputStream);
  }

  /**
   * Loads the next row from the data source.
   *
   * @return  the rows
   */
  public abstract Map<String, String> loadNextRow();

  /**
   * Gets the columns.
   *
   * @return  the columns
   */
  public String[] getColumns() {
    return _columns;
  }

  /**
   * Sets the columns.
   *
   * @param columns  the columns, not null
   */
  public void setColumns(final String[] columns) {
    _columns = columns;
  }

  /**
   * Opens the file for reading.
   *
   * @param filename  the file name, not null or empty
   * @return  a stream
   */
  protected static InputStream openFile(final String filename) {
    ArgumentChecker.notNull(filename, "filename");
    // Open input file for reading
    FileInputStream fileInputStream;
    try {
      fileInputStream = new FileInputStream(filename);
    } catch (final FileNotFoundException ex) {
      throw new OpenGammaRuntimeException("Could not open file " + filename + " for reading, exiting immediately.");
    }

    return fileInputStream;
  }

  /**
   * Closes the input.
   */
  public abstract void close();
}
