/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.opengamma.util.test.TestGroup;

/**
 * Just a quick thing to test the performance difference between contiguous and scattered memory using DGEMV()
 */
@Test(groups = TestGroup.UNIT_SLOW)
public class BLAS2Test {
  boolean _debug = false;

  void dgemv(final double[][] mat, final double[] vect, final double[] ans, final int m, final int n) {
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        ans[i] += mat[i][j] * vect[j];
      }
    }
  }

  double[] dgemv(final double[][] mat, final double[] vect, final int m, final int n) {
    final double[] ans = new double[m];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        ans[i] += mat[i][j] * vect[j];
      }
    }
    return ans;
  }

  void setMatrix(final double[][] mat, final int m, final int n) {
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        mat[i][j] = i * n + j;
      }
    }
  }

  void printMatrix(final double[][] mat, final int m, final int n) {
    final Logger log = LoggerFactory.getLogger(SimpleTimerTest.class);
    log.info("Printing matrix");
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        log.info("matrix[" + i + "][" + j + "]=" + mat[i][j]);
      }
    }
  }

  void setVector(final double[] vect, final int n) {
    for (int i = 0; i < n; i++) {
      vect[i] = i;
    }
  }

  void printVector(final double[] vect, final int n) {
    final Logger log = LoggerFactory.getLogger(BLAS2Test.class);
    log.info("Printing vector");
    for (int i = 0; i < n; i++) {
      log.info("vector[" + i + "]=" + vect[i]);
    }
  }

  double[] conMatDGEMV(final MatrixCreator mat, final double[] vect) {
    //    final Logger log = LoggerFactory.getLogger(BLAS2Test.class);
    final double[] ans = new double[mat.getheight()];
    //    int w=mat.getwidth();
    for (int i = 0; i < mat.getheight(); i++) {
      for (int j = 0; j < mat.getwidth(); j++) {
        ans[i] += mat.getval(i, j) * vect[j];
        //        log.info("accessing"+mat.getval(i, j)+" by "+vect[j]);
      }
    }
    return ans;
  }

  void conMatSetMatrix(final MatrixCreator mat) {
    //    final Logger log = LoggerFactory.getLogger(MatrixCreator.class);
    for (int i = 0; i < mat.getheight(); i++) {
      for (int j = 0; j < mat.getwidth(); j++) {
        mat.setval(i, j);
        //        log.info("writing" + (i * mat.getwidth() + j));
      }
    }
  }

  @Test
  public void test() {
    final Logger log = LoggerFactory.getLogger(BLAS2Test.class);
    final SimpleTimer timer = new SimpleTimer();
    final int m = 2 << 10;
    final int n = 3 << 10;

    //    int m = 2; // rows
    //    int n = 3; // columns

    final double[][] mat = new double[m][n];
    setMatrix(mat, m, n);

    final double[] vect = new double[n];
    setVector(vect, n);

    final double[] answer = new double[m];

    final MatrixCreator contmat = new MatrixCreator(m, n);
    conMatSetMatrix(contmat);

    final int trials = 1000;
    /*
     * Trials for pass by reference DGEMV
     */
    long dttime = 0;
    for (int i = 0; i < trials; i++) {
      timer.startTimer();
      dgemv(mat, vect, answer, m, n);
      timer.stopTimer();
      dttime += timer.totalTime();
      answer[0] += 1;
    }
    log.info("Total time using direct reference: " + dttime / 1e9 + "s");

    /*
     * Trials for java return vomit DGEMV
     */
    long ttime = 0;
    double[] newans;
    for (int i = 0; i < trials; i++) {
      timer.startTimer();
      newans = dgemv(mat, vect, m, n);
      timer.stopTimer();
      ttime += timer.totalTime();
      newans[0] += 1;
    }
    log.info("Total time using returned vector: " + ttime / 1e9 + "s");

    /*
     * Trials for contiguous memory access
     */
    long cttime = 0;
    double[] conmatans;

    //    conmatans=conMatDGEMV(contmat,vect);
    //    log.info("Contig answer");
    //    printVector(conmatans,m);

    for (int i = 0; i < trials; i++) {
      timer.startTimer();
      conmatans = conMatDGEMV(contmat, vect);
      timer.stopTimer();
      cttime += timer.totalTime();
      conmatans[m - 1] += 1;
    }

    log.info("Total time using contiguous mem: " + cttime / 1e9 + "s");

    log.info("Scalings [contiguous/java]: " + (double) cttime / ttime);
    log.info("Scalings [contiguous/direct]: " + (double) cttime / dttime);
    log.info("Scalings [direct/java]: " + (double) dttime / ttime);

  }

}
