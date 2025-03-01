/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.util;

import static org.testng.Assert.assertTrue;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.opengamma.util.test.TestGroup;

/**
 * Tests for {@link ParallelArrayBinarySort}.
 */
@Test(groups = TestGroup.UNIT)
public class ParallelArrayBinarySortTest {
  private static final Float[] F1 = new Float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f };
  private static final Double[] Y3 = new Double[] {2.1, 4.1, 6.1, 8.1, 10.1, 12.1, 14.1, 16.1, 18.1, 20.1 };
  private static final Float[] F2 = new Float[] {1f, 7f, 3f, 4f, 9f, 10f, 8f, 2f, 5f, 6f };
  private static final Double[] Y4 = new Double[] {2.1, 14.1, 6.1, 8.1, 18.1, 20.1, 16.1, 4.1, 10.1, 12.1 };

  private static final double[] X1D = new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
  private static final double[] Y1D = new double[] {2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };
  private static final double[] X2D = new double[] {1, 7, 3, 4, 9, 10, 8, 2, 5, 6 };
  private static final double[] Y2D = new double[] {2, 14, 6, 8, 18, 20, 16, 4, 10, 12 };

  private static final int[] X1I = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
  private static final int[] Y1I = new int[] {2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };
  private static final int[] X2I = new int[] {1, 7, 3, 4, 9, 10, 8, 2, 5, 6 };
  private static final int[] Y2I = new int[] {2, 14, 6, 8, 18, 20, 16, 4, 10, 12 };

  private static final long[] X1L = new long[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
  private static final long[] X2L = new long[] {1, 7, 3, 4, 9, 10, 8, 2, 5, 6 };

  private static final float[] X1F = new float[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
  private static final float[] X2F = new float[] {1, 7, 3, 4, 9, 10, 8, 2, 5, 6 };

  private static final double EPS = 1e-15;
  /**
   * Tests that the key array cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDoubleNullKeys() {
    final double[] t = null;
    ParallelArrayBinarySort.parallelBinarySort(t, Y1D);
  }

  /**
   * Tests that the value array cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDoubleNullValues() {
    ParallelArrayBinarySort.parallelBinarySort(X1D, (double[]) null);
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDoubleDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1D, new double[] {2, 4, 6 });
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testFloatDoubleDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1F, new double[] {2, 4, 6 });
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testFloatIntDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1F, new int[] {2, 4, 6 });
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testIntDoubleDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1I, new double[] {2, 4, 6 });
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testIntIntDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1I, new int[] {2, 4, 6 });
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testLongDoubleDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1L, new double[] {2, 4, 6 });
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testLongIntDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1L, new int[] {2, 4, 6 });
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDoubleIntDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1D, new int[] {2, 4, 6 });
  }

  /**
   * Tests that the key array cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDoubleObjectNullKeys() {
    ParallelArrayBinarySort.parallelBinarySort((double[]) null, Y1D);
  }

  /**
   * Tests that the value array cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDoubleObjectNullValues() {
    ParallelArrayBinarySort.parallelBinarySort(X1D, (Object[]) null);
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDoubleObjectDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(X1D, new Object[] {2, 4, 6 });
  }

  /**
   * Tests that the key array cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testObjectNullKeys() {
    ParallelArrayBinarySort.parallelBinarySort((String[]) null, Y3);
  }

  /**
   * Tests that the value array cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testObjectNullValues() {
    ParallelArrayBinarySort.parallelBinarySort(F1, null);
  }

  /**
   * Tests that the arrays must be the same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testObjectDifferentLengths() {
    ParallelArrayBinarySort.parallelBinarySort(F1, new Double[] {2., 4., 6. });
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testIntsSortByDouble() {
    final int n = X1I.length;
    final int[] x2 = Arrays.copyOf(X2I, n);
    final double[] y2 = Arrays.copyOf(Y2D, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y2);
    assertArrayEquals(X1I, x2);
    assertArrayEquals(Y1D, y2, EPS);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testLongsSortByDouble() {
    final int n = X1L.length;
    final long[] x2 = Arrays.copyOf(X2L, n);
    final double[] y2 = Arrays.copyOf(Y2D, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y2);
    assertArrayEquals(X1L, x2);
    assertArrayEquals(Y1D, y2, EPS);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testFloatsSortByDouble() {
    final int n = X1F.length;
    final float[] x2 = Arrays.copyOf(X2F, n);
    final double[] y2 = Arrays.copyOf(Y2D, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y2);
    assertTrue(Arrays.equals(X1F, x2));
    assertArrayEquals(Y1D, y2, EPS);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testDoublesSortByDouble() {
    final int n = X1D.length;
    final double[] x2 = Arrays.copyOf(X2D, n);
    final double[] y2 = Arrays.copyOf(Y2D, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y2);
    assertArrayEquals(X1D, x2, EPS);
    assertArrayEquals(Y1D, y2, EPS);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testIntsSortByInts() {
    final int n = X1I.length;
    final int[] x2 = Arrays.copyOf(X2I, n);
    final int[] y2 = Arrays.copyOf(Y2I, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y2);
    assertArrayEquals(X1I, x2);
    assertArrayEquals(Y1I, y2);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testLongsSortByInts() {
    final int n = X1L.length;
    final long[] x2 = Arrays.copyOf(X2L, n);
    final int[] y2 = Arrays.copyOf(Y2I, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y2);
    assertArrayEquals(X1L, x2);
    assertArrayEquals(Y1I, y2);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testFloatsSortByInts() {
    final int n = X1F.length;
    final float[] x2 = Arrays.copyOf(X2F, n);
    final int[] y2 = Arrays.copyOf(Y2I, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y2);
    assertTrue(Arrays.equals(X1F, x2));
    assertArrayEquals(Y1I, y2);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testDoublesSortByInts() {
    final int n = X1D.length;
    final double[] x2 = Arrays.copyOf(X2D, n);
    final int[] y2 = Arrays.copyOf(Y2I, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y2);
    assertArrayEquals(X1D, x2, EPS);
    assertArrayEquals(Y1I, y2);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testObjects() {
    final int n = F2.length;
    final Float[] f2 = Arrays.copyOf(F2, n);
    final Double[] y4 = Arrays.copyOf(Y4, n);
    ParallelArrayBinarySort.parallelBinarySort(f2, y4);
    assertArrayEquals(F1, f2);
    assertArrayEquals(Y3, y4);
  }

  /**
   * Tests the sort.
   */
  @Test
  public void testDoubleObject() {
    final int n = X2D.length;
    final double[] x2 = Arrays.copyOf(X2D, n);
    final Double[] y4 = Arrays.copyOf(Y4, n);
    ParallelArrayBinarySort.parallelBinarySort(x2, y4);
    assertArrayEquals(X1D, x2, 0);
    assertArrayEquals(Y3, y4);
  }
}
