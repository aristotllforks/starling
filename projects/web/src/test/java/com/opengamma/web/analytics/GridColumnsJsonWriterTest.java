/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics;

import static org.testng.AssertJUnit.assertTrue;

import org.json.JSONArray;
import org.json.JSONException;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.opengamma.util.test.TestGroup;
import com.opengamma.web.analytics.formatting.ResultsFormatter;
import com.opengamma.web.analytics.formatting.TypeFormatter;

/**
 * Test.
 */
@Test(groups = TestGroup.UNIT)
public class GridColumnsJsonWriterTest {

  @Test
  public void getJson() throws JSONException {
    final TestCellRenderer renderer = new TestCellRenderer();
    final GridColumn col1 = new GridColumn("col1", "col1 desc", Double.class, renderer);
    final GridColumn col2 = new GridColumn("col2", "col2 desc", String.class, renderer);
    final GridColumn col3 = new GridColumn("col3", "col3 desc", Double.class, renderer);
    final GridColumn col4 = new GridColumn("col4", "col4 desc", String.class, renderer);
    final GridColumnGroup group1 = new GridColumnGroup("group1", ImmutableList.of(col1, col2), true);
    final GridColumnGroup group2 = new GridColumnGroup("group2", ImmutableList.of(col3, col4), false);
    final ImmutableList<GridColumnGroup> groups = ImmutableList.of(group1, group2);
    final GridColumnsJsonWriter writer = new GridColumnsJsonWriter(new ResultsFormatter());
    final String json = writer.getJson(groups);
    final String expectedJson =
        "[{\"name\":\"group1\",\"dependencyGraphsAvailable\":true,\"columns\":[" +
            "{\"header\":\"col1\",\"description\":\"col1 desc\",\"type\":\"DOUBLE\"}," +
            "{\"header\":\"col2\",\"description\":\"col2 desc\",\"type\":\"STRING\"}]}," +
        "{\"name\":\"group2\",\"dependencyGraphsAvailable\":false,\"columns\":[" +
            "{\"header\":\"col3\",\"description\":\"col3 desc\",\"type\":\"DOUBLE\"}," +
            "{\"header\":\"col4\",\"description\":\"col4 desc\",\"type\":\"STRING\"}]}]";
    assertTrue(JsonTestUtils.equal(new JSONArray(expectedJson), new JSONArray(json)));
  }

  private static class TestCellRenderer implements GridColumn.CellRenderer {

    @Override
    public ResultsCell getResults(final int rowIndex,
                                  final TypeFormatter.Format format,
                                  final ResultsCache cache,
                                  final Class<?> columnType,
                                  final Object inlineKey) {
      return null;
    }
  }
}
