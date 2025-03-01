/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.regression;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import com.google.common.collect.Lists;

/**
 * Implementation of RegressionIO which reads and writes a dump to a zip file.
 */
public abstract class ZipFileRegressionIO extends RegressionIO implements AutoCloseable {

  public ZipFileRegressionIO(final File zipFile, final Format format) throws IOException {
    super(zipFile, format);
  }

  protected String createObjectPath(final String type, final String identifier) {

    String objectPath = createFilename(identifier);
    if (type != null) {
      objectPath = type + "/" + objectPath;
    }
    return objectPath;
  }

  public static ZipFileRegressionIO createWriter(final File zipFile, final Format format) throws IOException {
    return new WritingZipFileRegressionIO(zipFile, format);
  }

  public static ZipFileRegressionIO createReader(final File zipFile, final Format format) throws IOException {
    return new ReadingZipFileRegressionIO(zipFile, format);
  }

  /**
   * An implementation which only writes to zip files. Read operations throw {@link UnsupportedOperationException}s.
   */
  private static final class WritingZipFileRegressionIO extends ZipFileRegressionIO {

    /**
     * The zip output stream - not null
     */
    private final ZipArchiveOutputStream _zipArchiveOS;

    private WritingZipFileRegressionIO(final File zipFile, final Format format) throws IOException {
      super(zipFile, format);
      _zipArchiveOS = new ZipArchiveOutputStream(zipFile);
    }

    @Override
    public void write(final String type, final Object o, final String identifier) throws IOException {
      final ZipArchiveEntry entry = new ZipArchiveEntry(createObjectPath(type, identifier));

      _zipArchiveOS.putArchiveEntry(entry);
      getFormat().write(getFormatContext(), o, _zipArchiveOS);
      _zipArchiveOS.flush();
      _zipArchiveOS.closeArchiveEntry();

    }

    @Override
    public void endWrite() throws IOException {
      super.endWrite();
      if (_zipArchiveOS != null) {
        _zipArchiveOS.close();
      }
    }

    @Override
    public void close() throws Exception {
      if (_zipArchiveOS != null) {
        _zipArchiveOS.close();
      }
    }

    private UnsupportedOperationException unimplemented() {
      throw new UnsupportedOperationException("Cannot perform reads when in write mode.");
    }

    @Override
    public Object read(final String type, final String identifier) throws IOException {
      throw unimplemented();
    }

    @Override
    public List<String> enumObjects(final String type) throws IOException {
      throw unimplemented();
    }

  }

  /**
   * An implementation which only reads from zip files. Write operations throw {@link UnsupportedOperationException}s.
   */
  private static final class ReadingZipFileRegressionIO extends ZipFileRegressionIO {

    /**
     * The zip file. Not null.
     */
    private ZipFile _zipFile;

    ReadingZipFileRegressionIO(final File zipFile, final Format format) throws IOException {
      super(zipFile, format);
      initZipFile(zipFile);

    }

    private void initZipFile(final File zipFile) throws IOException {
      if (!zipFile.exists()) {
        throw new IllegalStateException("Unable to locate specified zip file on the file system: " + zipFile.getPath());
      }
      _zipFile = new ZipFile(zipFile);
    }

    @Override
    public void close() throws Exception {
      _zipFile.close();
    }

    @Override
    public void write(final String type, final Object o, final String identifier) throws IOException {
      unimplemented();
    }

    private void unimplemented() {
      throw new UnsupportedOperationException("Cannot perform writes when in read mode.");
    }

    @Override
    public Object read(final String type, final String identifier) throws IOException {
      final String objectPath = createObjectPath(type, identifier);
      final ZipArchiveEntry entry = _zipFile.getEntry(objectPath);

      if (entry == null) {
        throw new IllegalArgumentException(objectPath + " does not exist in this archive.");
      }

      final InputStream inputStream = _zipFile.getInputStream(entry);
      return getFormat().read(getFormatContext(), inputStream);

    }

    @Override
    public List<String> enumObjects(final String type) throws IOException {

      final List<String> objectIdentifiers = Lists.newLinkedList();
      final Pattern typeNameFormat = Pattern.compile("(.*)/(.*)");

      final Enumeration<ZipArchiveEntry> entries = _zipFile.getEntries();

      while (entries.hasMoreElements()) {
        final ZipArchiveEntry nextEntry = entries.nextElement();
        final String entryName = nextEntry.getName();
        final Matcher matcher = typeNameFormat.matcher(entryName);
        if (matcher.matches()) {
          final String objectType = matcher.group(1);
          if (objectType.equals(type)) {
            final String objectIdentifierFileName = matcher.group(2);
            if (isIdentifierIncluded(objectIdentifierFileName)) {
              final String objectIdentifier = stripIdentifierExtension(objectIdentifierFileName);
              objectIdentifiers.add(objectIdentifier);
            }
          }
        }
      }

      return objectIdentifiers;
    }

  }

}
