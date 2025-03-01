/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.interpolation;

import java.util.Arrays;

import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.analytics.math.matrix.DoubleMatrix2D;

/**
 * Solves cubic spline problem with Not-A-Knot endpoint conditions, where the third derivative at the endpoints is the same as that of their adjacent points.
 */
public class CubicSplineNakSolver extends CubicSplineSolver {

  @Override
  public DoubleMatrix2D solve(final double[] xValues, final double[] yValues) {

    final double[] intervals = getDiffs(xValues);

    return getSplineCoeffs(xValues, yValues, intervals, matrixEqnSolver(getMatrix(intervals), getVector(yValues, intervals)));
  }

  @Override
  public DoubleMatrix2D[] solveWithSensitivity(final double[] xValues, final double[] yValues) {
    final double[] intervals = getDiffs(xValues);
    final double[][] toBeInv = getMatrix(intervals);
    final double[] vector = getVector(yValues, intervals);
    final double[][] vecSensitivity = getVectorSensitivity(intervals);

    return getSplineCoeffsWithSensitivity(xValues, yValues, intervals, toBeInv, vector, vecSensitivity);
  }

  @Override
  public DoubleMatrix2D[] solveMultiDim(final double[] xValues, final DoubleMatrix2D yValuesMatrix) {
    final int dim = yValuesMatrix.getNumberOfRows();
    final DoubleMatrix2D[] coefMatrix = new DoubleMatrix2D[dim];

    for (int i = 0; i < dim; ++i) {
      coefMatrix[i] = solve(xValues, yValuesMatrix.getRowVector(i).getData());
    }

    return coefMatrix;
  }

  @Override
  public DoubleMatrix1D getKnotsMat1D(final double[] xValues) {
    final int nData = xValues.length;
    if (nData == 2) {
      return new DoubleMatrix1D(new double[] { xValues[0], xValues[nData - 1] });
    }
    if (nData == 3) {
      return new DoubleMatrix1D(new double[] { xValues[0], xValues[nData - 1] });
    }
    return new DoubleMatrix1D(xValues);
  }

  /**
   * @param xValues
   *          X values of Data
   * @param yValues
   *          Y values of Data
   * @param intervals
   *          {xValues[1]-xValues[0], xValues[2]-xValues[1],...}
   * @param solnVector
   *          Values of second derivative at knots
   * @return Coefficient matrix whose i-th row vector is (a_0,a_1,...) for i-th intervals, where a_0,a_1,... are coefficients of f(x) = a_0 + a_1 x^1 + ....
   */
  private DoubleMatrix2D getSplineCoeffs(final double[] xValues, final double[] yValues, final double[] intervals, final double[] solnVector) {
    final int nData = xValues.length;

    if (nData == 2) {
      final double[][] res = new double[][] { {
                    yValues[1] / intervals[0] - yValues[0] / intervals[0] - intervals[0] * solnVector[0] / 2. - intervals[0] * solnVector[1] / 6.
                        + intervals[0] * solnVector[0] / 6.,
                    yValues[0] } };
      return new DoubleMatrix2D(res);
    }
    if (nData == 3) {
      final double[][] res = new double[][] {
                    { solnVector[0] / 2., yValues[1] / intervals[0] - yValues[0] / intervals[0] - intervals[0] * solnVector[0] / 2., yValues[0] } };
      return new DoubleMatrix2D(res);
    }
    return getCommonSplineCoeffs(xValues, yValues, intervals, solnVector);
  }

  private DoubleMatrix2D[] getSplineCoeffsWithSensitivity(final double[] xValues, final double[] yValues, final double[] intervals, final double[][] toBeInv,
      final double[] vector,
      final double[][] vecSensitivity) {
    final int nData = xValues.length;

    if (nData == 2) {
      final DoubleMatrix2D[] res = new DoubleMatrix2D[nData];
      final double[][] coef = new double[][] { { yValues[1] / intervals[0] - yValues[0] / intervals[0], yValues[0] } };
      res[0] = new DoubleMatrix2D(coef);
      final double[][] coefSense = new double[2][];
      coefSense[0] = new double[] { -1. / intervals[0], 1. / intervals[0] };
      coefSense[1] = new double[] { 1., 0. };
      res[1] = new DoubleMatrix2D(coefSense);
      return res;
    }
    if (nData == 3) {
      final DoubleMatrix2D[] res = new DoubleMatrix2D[2];
      final DoubleMatrix1D[] soln = combinedMatrixEqnSolver(toBeInv, vector, vecSensitivity);
      final double[][] coef = new double[][] { { soln[0].getData()[0] / 2.,
                    yValues[1] / intervals[0] - yValues[0] / intervals[0] - intervals[0] * soln[0].getData()[0] / 2., yValues[0] } };
      res[0] = new DoubleMatrix2D(coef);
      final double[][] coefSense = new double[3][0];
      coefSense[0] = new double[] { soln[1].getData()[0] / 2., soln[2].getData()[0] / 2., soln[3].getData()[0] / 2. };
      coefSense[1] = new double[] { -1. / intervals[0] - intervals[0] * soln[1].getData()[0] / 2., 1. / intervals[0] - intervals[0] * soln[2].getData()[0] / 2.,
                    -intervals[0] * soln[3].getData()[0] / 2. };
      coefSense[2] = new double[] { 1., 0., 0. };
      res[1] = new DoubleMatrix2D(coefSense);
      return res;
    }
    final DoubleMatrix2D[] res = new DoubleMatrix2D[nData];
    final DoubleMatrix1D[] soln = combinedMatrixEqnSolver(toBeInv, vector, vecSensitivity);
    res[0] = getCommonSplineCoeffs(xValues, yValues, intervals, soln[0].getData());
    final double[][] solnMatrix = new double[nData][nData];
    for (int i = 0; i < nData; ++i) {
      for (int j = 0; j < nData; ++j) {
        solnMatrix[i][j] = soln[j + 1].getData()[i];
      }
    }
    final DoubleMatrix2D[] tmp = getCommonSensitivityCoeffs(intervals, solnMatrix);
    System.arraycopy(tmp, 0, res, 1, nData - 1);
    return res;
  }

  /**
   * Cubic spline is obtained by solving a linear problem Ax=b where A is a square matrix and x,b are vector
   *
   * @param yValues
   *          Y Values of data
   * @param intervals
   *          {xValues[1]-xValues[0], xValues[2]-xValues[1],...}
   * @return Vector b
   */
  private double[] getVector(final double[] yValues, final double[] intervals) {

    final int nData = yValues.length;
    double[] res = new double[nData];

    if (nData == 3) {
      for (int i = 0; i < nData; ++i) {
        res[i] = 2. * yValues[2] / (intervals[0] + intervals[1]) - 2. * yValues[0] / (intervals[0] + intervals[1]) - 2. * yValues[1] / intervals[0]
            + 2. * yValues[0] / intervals[0];
      }
    } else {
      res = getCommonVectorElements(yValues, intervals);
    }
    return res;
  }

  private double[][] getVectorSensitivity(final double[] intervals) {

    final int nData = intervals.length + 1;
    double[][] res = new double[nData][nData];

    if (nData == 3) {
      for (int i = 0; i < nData; ++i) {
        res[i][0] = -2. / (intervals[0] + intervals[1]) + 2. / intervals[0];
        res[i][1] = -2. / intervals[0];
        res[i][2] = 2. / (intervals[0] + intervals[1]);
      }
    } else {
      res = getCommonVectorSensitivity(intervals);
    }
    return res;
  }

  /**
   * Cubic spline is obtained by solving a linear problem Ax=b where A is a square matrix and x,b are vector
   *
   * @param intervals
   *          {xValues[1]-xValues[0], xValues[2]-xValues[1],...}
   * @return Matrix A
   */
  private double[][] getMatrix(final double[] intervals) {

    final int nData = intervals.length + 1;
    double[][] res = new double[nData][nData];

    for (int i = 0; i < nData; ++i) {
      Arrays.fill(res[i], 0.);
    }

    if (nData == 2) {
      res[0][1] = intervals[0];
      res[1][0] = intervals[0];
      return res;
    }
    if (nData == 3) {
      res[0][0] = intervals[1];
      res[1][1] = intervals[1];
      res[2][2] = intervals[1];
      return res;
    }
    res = getCommonMatrixElements(intervals);
    res[0][0] = -intervals[1];
    res[0][1] = intervals[0] + intervals[1];
    res[0][2] = -intervals[0];
    res[nData - 1][nData - 3] = -intervals[nData - 2];
    res[nData - 1][nData - 2] = intervals[nData - 3] + intervals[nData - 2];
    res[nData - 1][nData - 1] = -intervals[nData - 3];
    return res;
  }

}
