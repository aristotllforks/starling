/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.Pair;

/**
 * Viewport containing an arbitrary collection of cells.
 */
public class ArbitraryViewportDefinition extends ViewportDefinition {

  /** Cells in the viewport, not empty, sorted by column then row (i.e. along rows) */
  private final List<GridCell> _cells;

  /**
   * @param version
   *          the version
   * @param cells
   *          Cells in the viewport, not empty
   * @param enableLogging
   *          Whether full logging info should be collected for the viewport's cells
   */
  /* package */ ArbitraryViewportDefinition(final int version, final List<GridCell> cells, final boolean enableLogging) {
    super(version, enableLogging);
    ArgumentChecker.notEmpty(cells, "cells");
    _cells = Lists.newArrayList(cells);
    Collections.sort(_cells);
  }

  @Override
  public Iterator<GridCell> iterator() {
    return _cells.iterator();
  }

  @Override
  public boolean isValidFor(final GridStructure gridStructure) {
    for (final GridCell cell : _cells) {
      if (cell.getRow() >= gridStructure.getRowCount()
          || cell.getColumn() >= gridStructure.getColumnCount()) {
        return false;
      }
    }
    return true;
  }

  @Override
  Pair<Integer, Boolean> getChangedNode(final ViewportDefinition viewportDefinition) {
    return null;
  }

  @Override
  public String toString() {
    return "ArbitraryViewportDefinition [_enableLogging=" + enableLogging() + ", _cells=" + _cells + "]";
  }
}
