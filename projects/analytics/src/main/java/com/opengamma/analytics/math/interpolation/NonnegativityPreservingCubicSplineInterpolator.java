/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.interpolation;

import java.util.Arrays;

import com.google.common.primitives.Doubles;
import com.opengamma.analytics.math.function.PiecewisePolynomialWithSensitivityFunction1D;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.analytics.math.matrix.DoubleMatrix2D;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.ParallelArrayBinarySort;

/**
 * Filter for nonnegativity of cubic spline interpolation based on R. L. Dougherty, A. Edelman, and J. M. Hyman, "Nonnegativity-, Monotonicity-, or
 * Convexity-Preserving Cubic and Quintic Hermite Interpolation" Mathematics Of Computation, v. 52, n. 186, April 1989, pp. 471-494.
 *
 * First, interpolant is computed by another cubic interpolation method. Then the first derivatives are modified such that non-negativity conditions are
 * satisfied. Note that shape-preserving three-point formula is used at endpoints in order to ensure positivity of an interpolant in the first interval and the
 * last interval
 */
public class NonnegativityPreservingCubicSplineInterpolator extends PiecewisePolynomialInterpolator {

  private static final double SMALL = 1.e-14;

  private final HermiteCoefficientsProvider _solver = new HermiteCoefficientsProvider();
  private final PiecewisePolynomialWithSensitivityFunction1D _function = new PiecewisePolynomialWithSensitivityFunction1D();
  private final PiecewisePolynomialInterpolator _method;

  /**
   * Primary interpolation method should be passed.
   *
   * @param method
   *          PiecewisePolynomialInterpolator
   */
  public NonnegativityPreservingCubicSplineInterpolator(final PiecewisePolynomialInterpolator method) {
    _method = method;
  }

  @Override
  public PiecewisePolynomialResult interpolate(final double[] xValues, final double[] yValues) {
    ArgumentChecker.notNull(xValues, "xValues");
    ArgumentChecker.notNull(yValues, "yValues");

    ArgumentChecker.isTrue(xValues.length == yValues.length | xValues.length + 2 == yValues.length,
        "(xValues length = yValues length) or (xValues length + 2 = yValues length)");
    ArgumentChecker.isTrue(xValues.length > 2, "Data points should be more than 2");

    final int nDataPts = xValues.length;
    final int yValuesLen = yValues.length;

    for (int i = 0; i < nDataPts; ++i) {
      ArgumentChecker.isFalse(Double.isNaN(xValues[i]), "xValues containing NaN");
      ArgumentChecker.isFalse(Double.isInfinite(xValues[i]), "xValues containing Infinity");
    }
    for (int i = 0; i < yValuesLen; ++i) {
      ArgumentChecker.isFalse(Double.isNaN(yValues[i]), "yValues containing NaN");
      ArgumentChecker.isFalse(Double.isInfinite(yValues[i]), "yValues containing Infinity");
    }

    for (int i = 0; i < nDataPts - 1; ++i) {
      for (int j = i + 1; j < nDataPts; ++j) {
        ArgumentChecker.isFalse(xValues[i] == xValues[j], "xValues should be distinct");
      }
    }

    final double[] xValuesSrt = Arrays.copyOf(xValues, nDataPts);
    double[] yValuesSrt = new double[nDataPts];
    if (nDataPts == yValuesLen) {
      yValuesSrt = Arrays.copyOf(yValues, nDataPts);
    } else {
      yValuesSrt = Arrays.copyOfRange(yValues, 1, nDataPts + 1);
    }
    ParallelArrayBinarySort.parallelBinarySort(xValuesSrt, yValuesSrt);

    final double[] intervals = _solver.intervalsCalculator(xValuesSrt);
    final double[] slopes = _solver.slopesCalculator(yValuesSrt, intervals);
    final PiecewisePolynomialResult result = _method.interpolate(xValues, yValues);

    ArgumentChecker.isTrue(result.getOrder() == 4, "Primary interpolant is not cubic");

    final double[] initialFirst = _function.differentiate(result, xValuesSrt).getData()[0];
    final double[] first = firstDerivativeCalculator(yValuesSrt, intervals, slopes, initialFirst);
    final double[][] coefs = _solver.solve(yValuesSrt, intervals, slopes, first);

    for (int i = 0; i < nDataPts - 1; ++i) {
      for (int j = 0; j < 4; ++j) {
        ArgumentChecker.isFalse(Double.isNaN(coefs[i][j]), "Too large input");
        ArgumentChecker.isFalse(Double.isInfinite(coefs[i][j]), "Too large input");
      }
    }

    return new PiecewisePolynomialResult(new DoubleMatrix1D(xValuesSrt), new DoubleMatrix2D(coefs), 4, 1);
  }

  @Override
  public PiecewisePolynomialResult interpolate(final double[] xValues, final double[][] yValuesMatrix) {
    ArgumentChecker.notNull(xValues, "xValues");
    ArgumentChecker.notNull(yValuesMatrix, "yValuesMatrix");

    ArgumentChecker.isTrue(xValues.length == yValuesMatrix[0].length | xValues.length + 2 == yValuesMatrix[0].length,
        "(xValues length = yValuesMatrix's row vector length) or (xValues length + 2 = yValuesMatrix's row vector length)");
    ArgumentChecker.isTrue(xValues.length > 2, "Data points should be more than 2");

    final int nDataPts = xValues.length;
    final int yValuesLen = yValuesMatrix[0].length;
    final int dim = yValuesMatrix.length;

    for (int i = 0; i < nDataPts; ++i) {
      ArgumentChecker.isFalse(Double.isNaN(xValues[i]), "xValues containing NaN");
      ArgumentChecker.isFalse(Double.isInfinite(xValues[i]), "xValues containing Infinity");
    }
    for (int i = 0; i < yValuesLen; ++i) {
      for (int j = 0; j < dim; ++j) {
        ArgumentChecker.isFalse(Double.isNaN(yValuesMatrix[j][i]), "yValuesMatrix containing NaN");
        ArgumentChecker.isFalse(Double.isInfinite(yValuesMatrix[j][i]), "yValuesMatrix containing Infinity");
      }
    }
    for (int i = 0; i < nDataPts; ++i) {
      for (int j = i + 1; j < nDataPts; ++j) {
        ArgumentChecker.isFalse(xValues[i] == xValues[j], "xValues should be distinct");
      }
    }

    double[] xValuesSrt = new double[nDataPts];
    final DoubleMatrix2D[] coefMatrix = new DoubleMatrix2D[dim];

    for (int i = 0; i < dim; ++i) {
      xValuesSrt = Arrays.copyOf(xValues, nDataPts);
      double[] yValuesSrt = new double[nDataPts];
      if (nDataPts == yValuesLen) {
        yValuesSrt = Arrays.copyOf(yValuesMatrix[i], nDataPts);
      } else {
        yValuesSrt = Arrays.copyOfRange(yValuesMatrix[i], 1, nDataPts + 1);
      }
      ParallelArrayBinarySort.parallelBinarySort(xValuesSrt, yValuesSrt);

      final double[] intervals = _solver.intervalsCalculator(xValuesSrt);
      final double[] slopes = _solver.slopesCalculator(yValuesSrt, intervals);
      final PiecewisePolynomialResult result = _method.interpolate(xValues, yValuesMatrix[i]);

      ArgumentChecker.isTrue(result.getOrder() == 4, "Primary interpolant is not cubic");

      final double[] initialFirst = _function.differentiate(result, xValuesSrt).getData()[0];
      final double[] first = firstDerivativeCalculator(yValuesSrt, intervals, slopes, initialFirst);

      coefMatrix[i] = new DoubleMatrix2D(_solver.solve(yValuesSrt, intervals, slopes, first));
    }

    final int nIntervals = coefMatrix[0].getNumberOfRows();
    final int nCoefs = coefMatrix[0].getNumberOfColumns();
    final double[][] resMatrix = new double[dim * nIntervals][nCoefs];

    for (int i = 0; i < nIntervals; ++i) {
      for (int j = 0; j < dim; ++j) {
        resMatrix[dim * i + j] = coefMatrix[j].getRowVector(i).getData();
      }
    }

    for (int i = 0; i < nIntervals * dim; ++i) {
      for (int j = 0; j < nCoefs; ++j) {
        ArgumentChecker.isFalse(Double.isNaN(resMatrix[i][j]), "Too large input");
        ArgumentChecker.isFalse(Double.isInfinite(resMatrix[i][j]), "Too large input");
      }
    }

    return new PiecewisePolynomialResult(new DoubleMatrix1D(xValuesSrt), new DoubleMatrix2D(resMatrix), nCoefs, dim);
  }

  @Override
  public PiecewisePolynomialResultsWithSensitivity interpolateWithSensitivity(final double[] xValues, final double[] yValues) {
    ArgumentChecker.notNull(xValues, "xValues");
    ArgumentChecker.notNull(yValues, "yValues");

    ArgumentChecker.isTrue(xValues.length == yValues.length | xValues.length + 2 == yValues.length,
        "(xValues length = yValues length) or (xValues length + 2 = yValues length)");
    ArgumentChecker.isTrue(xValues.length > 2, "Data points should be more than 2");

    final int nDataPts = xValues.length;
    final int yValuesLen = yValues.length;

    for (int i = 0; i < nDataPts; ++i) {
      ArgumentChecker.isFalse(Double.isNaN(xValues[i]), "xValues containing NaN");
      ArgumentChecker.isFalse(Double.isInfinite(xValues[i]), "xValues containing Infinity");
    }
    for (int i = 0; i < yValuesLen; ++i) {
      ArgumentChecker.isFalse(Double.isNaN(yValues[i]), "yValues containing NaN");
      ArgumentChecker.isFalse(Double.isInfinite(yValues[i]), "yValues containing Infinity");
    }

    for (int i = 0; i < nDataPts - 1; ++i) {
      for (int j = i + 1; j < nDataPts; ++j) {
        ArgumentChecker.isFalse(xValues[i] == xValues[j], "xValues should be distinct");
      }
    }

    double[] yValuesSrt = new double[nDataPts];
    if (nDataPts == yValuesLen) {
      yValuesSrt = Arrays.copyOf(yValues, nDataPts);
    } else {
      yValuesSrt = Arrays.copyOfRange(yValues, 1, nDataPts + 1);
    }

    final double[] intervals = _solver.intervalsCalculator(xValues);
    final double[] slopes = _solver.slopesCalculator(yValuesSrt, intervals);
    final PiecewisePolynomialResultsWithSensitivity resultWithSensitivity = _method.interpolateWithSensitivity(xValues, yValues);

    ArgumentChecker.isTrue(resultWithSensitivity.getOrder() == 4, "Primary interpolant is not cubic");

    final double[] initialFirst = _function.differentiate(resultWithSensitivity, xValues).getData()[0];
    final double[][] slopeSensitivity = _solver.slopeSensitivityCalculator(intervals);
    final DoubleMatrix1D[] initialFirstSense = _function.differentiateNodeSensitivity(resultWithSensitivity, xValues);
    final DoubleMatrix1D[] firstWithSensitivity = firstDerivativeWithSensitivityCalculator(yValuesSrt, intervals, initialFirst, initialFirstSense);
    final DoubleMatrix2D[] resMatrix = _solver.solveWithSensitivity(yValuesSrt, intervals, slopes, slopeSensitivity, firstWithSensitivity);

    for (int k = 0; k < nDataPts; k++) {
      final DoubleMatrix2D m = resMatrix[k];
      final int rows = m.getNumberOfRows();
      final int cols = m.getNumberOfColumns();
      for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
          ArgumentChecker.isTrue(Doubles.isFinite(m.getEntry(i, j)), "Matrix contains a NaN or infinite");
        }
      }
    }

    final DoubleMatrix2D coefMatrix = resMatrix[0];
    final DoubleMatrix2D[] coefSenseMatrix = new DoubleMatrix2D[nDataPts - 1];
    System.arraycopy(resMatrix, 1, coefSenseMatrix, 0, nDataPts - 1);
    final int nCoefs = coefMatrix.getNumberOfColumns();

    return new PiecewisePolynomialResultsWithSensitivity(new DoubleMatrix1D(xValues), coefMatrix, nCoefs, 1, coefSenseMatrix);
  }

  @Override
  public PiecewisePolynomialInterpolator getPrimaryMethod() {
    return _method;
  }

  /**
   * First derivatives are modified such that cubic interpolant has the same sign as linear interpolator
   *
   * @param yValues
   * @param intervals
   * @param slopes
   * @param initialFirst
   * @return first derivative
   */
  private double[] firstDerivativeCalculator(final double[] yValues, final double[] intervals, final double[] slopes, final double[] initialFirst) {
    final int nDataPts = yValues.length;
    final double[] res = new double[nDataPts];

    for (int i = 1; i < nDataPts - 1; ++i) {
      final double tau = Math.signum(yValues[i]);
      res[i] = tau == 0. ? initialFirst[i]
          : Math.min(3. * tau * yValues[i] / intervals[i - 1], Math.max(-3. * tau * yValues[i] / intervals[i], tau * initialFirst[i])) / tau;
    }
    final double tauIni = Math.signum(yValues[0]);
    final double tauFin = Math.signum(yValues[nDataPts - 1]);
    res[0] = tauIni == 0. ? initialFirst[0]
        : Math.min(3. * tauIni * yValues[0] / intervals[0], Math.max(-3. * tauIni * yValues[0] / intervals[0], tauIni * initialFirst[0])) / tauIni;
    res[nDataPts - 1] = tauFin == 0. ? initialFirst[nDataPts - 1]
        : Math.min(3. * tauFin * yValues[nDataPts - 1] / intervals[nDataPts - 2],
            Math.max(-3. * tauFin * yValues[nDataPts - 1] / intervals[nDataPts - 2], tauFin * initialFirst[nDataPts - 1]))
            / tauFin;

    return res;
  }

  private DoubleMatrix1D[] firstDerivativeWithSensitivityCalculator(final double[] yValues, final double[] intervals, final double[] initialFirst,
      final DoubleMatrix1D[] initialFirstSense) {
    final int nDataPts = yValues.length;
    final DoubleMatrix1D[] res = new DoubleMatrix1D[nDataPts + 1];
    final double[] newFirst = new double[nDataPts];

    for (int i = 1; i < nDataPts - 1; ++i) {
      final double tau = Math.signum(yValues[i]);
      final double lower = -3. * tau * yValues[i] / intervals[i];
      final double upper = 3. * tau * yValues[i] / intervals[i - 1];
      final double ref = tau * initialFirst[i];
      final double[] tmp = new double[nDataPts];
      Arrays.fill(tmp, 0.);
      if (Math.abs(ref - lower) < SMALL && tau != 0.) {
        newFirst[i] = ref >= lower ? initialFirst[i] : lower / tau;
        for (int k = 0; k < nDataPts; ++k) {
          tmp[k] = 0.5 * initialFirstSense[i].getData()[k];
        }
        tmp[i] -= 1.5 / intervals[i];
      } else {
        if (ref < lower) {
          newFirst[i] = lower / tau;
          tmp[i] = -3. / intervals[i];
        } else {
          if (Math.abs(ref - upper) < SMALL && tau != 0.) {
            newFirst[i] = ref <= upper ? initialFirst[i] : upper / tau;
            for (int k = 0; k < nDataPts; ++k) {
              tmp[k] = 0.5 * initialFirstSense[i].getData()[k];
            }
            tmp[i] += 1.5 / intervals[i - 1];
          } else {
            if (ref > upper) {
              newFirst[i] = upper / tau;
              tmp[i] = 3. / intervals[i - 1];
            } else {
              newFirst[i] = initialFirst[i];
              System.arraycopy(initialFirstSense[i].getData(), 0, tmp, 0, nDataPts);
            }
          }
        }
      }
      res[i + 1] = new DoubleMatrix1D(tmp);
    }
    final double tauIni = Math.signum(yValues[0]);
    final double lowerIni = -3. * tauIni * yValues[0] / intervals[0];
    final double upperIni = 3. * tauIni * yValues[0] / intervals[0];
    final double refIni = tauIni * initialFirst[0];
    final double[] tmpIni = new double[nDataPts];
    Arrays.fill(tmpIni, 0.);
    if (Math.abs(refIni - lowerIni) < SMALL && tauIni != 0.) {
      newFirst[0] = refIni >= lowerIni ? initialFirst[0] : lowerIni / tauIni;
      for (int k = 0; k < nDataPts; ++k) {
        tmpIni[k] = 0.5 * initialFirstSense[0].getData()[k];
      }
      tmpIni[0] -= 1.5 / intervals[0];
    } else {
      if (refIni < lowerIni) {
        newFirst[0] = lowerIni / tauIni;
        tmpIni[0] = -3. / intervals[0];
      } else {
        if (Math.abs(refIni - upperIni) < SMALL && tauIni != 0.) {
          newFirst[0] = refIni <= upperIni ? initialFirst[0] : upperIni / tauIni;
          for (int k = 0; k < nDataPts; ++k) {
            tmpIni[k] = 0.5 * initialFirstSense[0].getData()[k];
          }
          tmpIni[0] += 1.5 / intervals[0];
        } else {
          if (refIni > upperIni) {
            newFirst[0] = upperIni / tauIni;
            tmpIni[0] = 3. / intervals[0];
          } else {
            newFirst[0] = initialFirst[0];
            System.arraycopy(initialFirstSense[0].getData(), 0, tmpIni, 0, nDataPts);
          }
        }
      }
    }
    res[1] = new DoubleMatrix1D(tmpIni);
    final double tauFin = Math.signum(yValues[nDataPts - 1]);
    final double lowerFin = -3. * tauFin * yValues[nDataPts - 1] / intervals[nDataPts - 2];
    final double upperFin = 3. * tauFin * yValues[nDataPts - 1] / intervals[nDataPts - 2];
    final double refFin = tauFin * initialFirst[nDataPts - 1];
    final double[] tmpFin = new double[nDataPts];
    Arrays.fill(tmpFin, 0.);
    if (Math.abs(refFin - lowerFin) < SMALL && tauFin != 0.) {
      newFirst[nDataPts - 1] = refFin >= lowerFin ? initialFirst[nDataPts - 1] : lowerFin / tauFin;
      for (int k = 0; k < nDataPts; ++k) {
        tmpFin[k] = 0.5 * initialFirstSense[nDataPts - 1].getData()[k];
      }
      tmpFin[nDataPts - 1] -= 1.5 / intervals[nDataPts - 2];
    } else {
      if (refFin < lowerFin) {
        newFirst[nDataPts - 1] = lowerFin / tauFin;
        tmpFin[nDataPts - 1] = -3. / intervals[nDataPts - 2];
      } else {
        if (Math.abs(refFin - upperFin) < SMALL && tauFin != 0.) {
          newFirst[nDataPts - 1] = refFin <= upperFin ? initialFirst[nDataPts - 1] : upperFin / tauFin;
          for (int k = 0; k < nDataPts; ++k) {
            tmpFin[k] = 0.5 * initialFirstSense[nDataPts - 1].getData()[k];
          }
          tmpFin[nDataPts - 1] += 1.5 / intervals[nDataPts - 2];
        } else {
          if (refFin > upperFin) {
            newFirst[nDataPts - 1] = upperFin / tauFin;
            tmpFin[nDataPts - 1] = 3. / intervals[nDataPts - 2];
          } else {
            newFirst[nDataPts - 1] = initialFirst[nDataPts - 1];
            System.arraycopy(initialFirstSense[nDataPts - 1].getData(), 0, tmpFin, 0, nDataPts);
          }
        }
      }
    }
    res[nDataPts] = new DoubleMatrix1D(tmpFin);
    res[0] = new DoubleMatrix1D(newFirst);
    return res;
  }
}
