/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.statistics.distribution;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;

import com.opengamma.util.CompareUtils;

import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;

/**
 *
 * The generalized Pareto distribution is a family of power law probability distributions with location parameter $\mu$, shape parameter $\xi$ and scale
 * parameter $\sigma$.
 */
public class GeneralizedParetoDistribution implements ProbabilityDistribution<Double> {
  // TODO check cdf, pdf for support
  private final double _mu;
  private final double _sigma;
  private final double _ksi;
  // TODO better seed
  private final RandomEngine _engine;

  /**
   *
   * @param mu The location parameter
   * @param sigma The scale parameter, not negative or zero
   * @param ksi The shape parameter, not zero
   */
  public GeneralizedParetoDistribution(final double mu, final double sigma, final double ksi) {
    this(mu, sigma, ksi, new MersenneTwister64(new Date()));
  }

  /**
   *
   * @param mu The location parameter
   * @param sigma The scale parameter
   * @param ksi The shape parameter
   * @param engine A uniform random number generator, not null
   */
  public GeneralizedParetoDistribution(final double mu, final double sigma, final double ksi, final RandomEngine engine) {
    Validate.isTrue(sigma > 0, "sigma must be > 0");
    Validate.isTrue(!CompareUtils.closeEquals(ksi, 0, 1e-15), "ksi cannot be zero");
    Validate.notNull(engine);
    _mu = mu;
    _sigma = sigma;
    _ksi = ksi;
    _engine = engine;
  }

  /**
   * @return The location parameter
   */
  public double getMu() {
    return _mu;
  }

  /**
   * @return The scale parameter
   */
  public double getSigma() {
    return _sigma;
  }

  /**
   * @return The shape parameter
   */
  public double getKsi() {
    return _ksi;
  }

  /**
   * {@inheritDoc}
   * @throws IllegalArgumentException If $x \not\in$ support
   */
  @Override
  public double getCDF(final Double x) {
    Validate.notNull(x);
    return 1 - Math.pow(1 + _ksi * getZ(x), -1. / _ksi);
  }

  /**
   * {@inheritDoc}
   *
   * @return Not supported
   * @throws NotImplementedException
   *           not implemented
   */
  @Override
  public double getInverseCDF(final Double p) {
    throw new NotImplementedException();
  }

  /**
   * {@inheritDoc}
   * @throws IllegalArgumentException If $x \not\in$ support
   */
  @Override
  public double getPDF(final Double x) {
    Validate.notNull(x);
    return Math.pow(1 + _ksi * getZ(x), -(1. / _ksi + 1)) / _sigma;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double nextRandom() {
    return _mu + _sigma * (Math.pow(_engine.nextDouble(), -_ksi) - 1) / _ksi;
  }

  private double getZ(final double x) {
    if (_ksi > 0 && x < _mu) {
      throw new IllegalArgumentException("Support for GPD is in the range x >= mu if ksi > 0");
    }
    if (_ksi < 0 && (x <= _mu || x >= _mu - _sigma / _ksi)) {
      throw new IllegalArgumentException("Support for GPD is in the range mu <= x <= mu - sigma / ksi if ksi < 0");
    }
    return (x - _mu) / _sigma;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(_ksi);
    result = prime * result + (int) (temp ^ temp >>> 32);
    temp = Double.doubleToLongBits(_mu);
    result = prime * result + (int) (temp ^ temp >>> 32);
    temp = Double.doubleToLongBits(_sigma);
    result = prime * result + (int) (temp ^ temp >>> 32);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final GeneralizedParetoDistribution other = (GeneralizedParetoDistribution) obj;
    if (Double.doubleToLongBits(_ksi) != Double.doubleToLongBits(other._ksi)) {
      return false;
    }
    if (Double.doubleToLongBits(_mu) != Double.doubleToLongBits(other._mu)) {
      return false;
    }
    return Double.doubleToLongBits(_sigma) == Double.doubleToLongBits(other._sigma);
  }

}
