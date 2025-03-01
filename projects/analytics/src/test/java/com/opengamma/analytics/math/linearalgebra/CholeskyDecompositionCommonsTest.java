/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.linearalgebra;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.Test;

import com.opengamma.analytics.math.matrix.CommonsMatrixAlgebra;
import com.opengamma.analytics.math.matrix.DoubleMatrix2D;
import com.opengamma.analytics.math.matrix.MatrixAlgebra;
import com.opengamma.util.test.TestGroup;

/**
 * Tests the Cholesky decomposition wrapping.
 */
@Test(groups = TestGroup.UNIT)
public class CholeskyDecompositionCommonsTest {

  private static final MatrixAlgebra ALGEBRA = new CommonsMatrixAlgebra();
  private static final Decomposition<CholeskyDecompositionResult> CH = new CholeskyDecompositionCommons();
  private static final DoubleMatrix2D A = new DoubleMatrix2D(
      new double[][] { new double[] { 10.0, 2.0, -1.0 }, new double[] { 2.0, 5.0, -2.0 }, new double[] { -1.0, -2.0, 15.0 } });
  private static final double EPS = 1e-9;

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullObjectMatrix() {
    CH.evaluate((DoubleMatrix2D) null);
  }

  @Test
  public void testRecoverOrginal() {
    final DecompositionResult result = CH.evaluate(A);
    assertTrue(result instanceof CholeskyDecompositionResult);
    final CholeskyDecompositionResult ch = (CholeskyDecompositionResult) result;
    final DoubleMatrix2D a = (DoubleMatrix2D) ALGEBRA.multiply(ch.getL(), ch.getLT());
    checkEquals(A, a);
  }

  private static void checkEquals(final DoubleMatrix2D x, final DoubleMatrix2D y) {
    final int n = x.getNumberOfRows();
    final int m = x.getNumberOfColumns();
    assertEquals(n, y.getNumberOfRows());
    assertEquals(m, y.getNumberOfColumns());
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        assertEquals(x.getEntry(i, j), y.getEntry(i, j), EPS);
      }
    }
  }
}
