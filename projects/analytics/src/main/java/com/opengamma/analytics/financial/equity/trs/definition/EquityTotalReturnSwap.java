/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.equity.trs.definition;

import org.apache.commons.lang.ObjectUtils;

import com.opengamma.analytics.financial.equity.Equity;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.Annuity;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.financial.interestrate.swap.derivative.TotalReturnSwap;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;

/**
 * Description of an equity total return swap.
 */
public class EquityTotalReturnSwap extends TotalReturnSwap {

  /** The equity asset */
  private final Equity _equity;
  /** The notional amount */
  private final double _notionalAmount;
  /** The notional currency */
  private final Currency _notionalCurrency;
  /** The dividend paid by the asset leg payer as a ratio of the original dividend. ratio &ge; 0 and &le; 1. */
  private final double _dividendRatio;

  /**
   * @param effectiveTime
   *          The time to the effective date.
   * @param terminatioTime
   *          The time to the termination date.
   * @param fundingLeg
   *          The funding leg, not null
   * @param equity
   *          The equity, not null
   * @param notionalAmount
   *          The notional amount
   * @param notionalCurrency
   *          The notional currency, not null
   * @param dividendRatio
   *          The dividend paid by the asset leg payer as a ratio of the original dividend. ratio &ge; 0 and &le; 1.
   */
  public EquityTotalReturnSwap(final double effectiveTime, final double terminatioTime,
      final Annuity<? extends Payment> fundingLeg, final Equity equity,
      final double notionalAmount, final Currency notionalCurrency, final double dividendRatio) {
    super(effectiveTime, terminatioTime, fundingLeg);
    ArgumentChecker.notNull(equity, "equity");
    ArgumentChecker.notNull(notionalCurrency, "notionalCurrency");
    ArgumentChecker.isTrue(ArgumentChecker.isInRangeInclusive(0, 1, dividendRatio), "Dividend ratio must be >= 0 and <= 1 "
        + "have {}", dividendRatio);
    _equity = equity;
    _dividendRatio = dividendRatio;
    _notionalAmount = notionalAmount;
    _notionalCurrency = notionalCurrency;
  }

  /**
   * Gets the equity.
   * @return The equity
   */

  public Equity getEquity() {
    return _equity;
  }

  /**
   * Gets the dividend percentage.
   * @return The dividend percentage
   */
  public double getDividendPercentage() {
    return _dividendRatio;
  }

  /**
   * Gets the notional amount.
   * @return The notional amount
   */
  public double getNotionalAmount() {
    return _notionalAmount;
  }

  /**
   * Gets the notional currency.
   * @return The notional currency
   */
  public Currency getNotionalCurrency() {
    return _notionalCurrency;
  }

  @Override
  public <U, V> V accept(final InstrumentDerivativeVisitor<U, V> visitor, final U data) {
    ArgumentChecker.notNull(visitor, "visitor");
    return visitor.visitEquityTotalReturnSwap(this, data);
  }

  @Override
  public <V> V accept(final InstrumentDerivativeVisitor<?, V> visitor) {
    ArgumentChecker.notNull(visitor, "visitor");
    return visitor.visitEquityTotalReturnSwap(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    long temp;
    temp = Double.doubleToLongBits(_dividendRatio);
    result = prime * result + (int) (temp ^ temp >>> 32);
    temp = Double.doubleToLongBits(_notionalAmount);
    result = prime * result + (int) (temp ^ temp >>> 32);
    result = prime * result + _notionalCurrency.hashCode();
    result = prime * result + _equity.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof EquityTotalReturnSwap)) {
      return false;
    }
    final EquityTotalReturnSwap other = (EquityTotalReturnSwap) obj;
    if (!ObjectUtils.equals(_equity, other._equity)) {
      return false;
    }
    if (Double.compare(_notionalAmount, other._notionalAmount) != 0) {
      return false;
    }
    if (!ObjectUtils.equals(_notionalCurrency, other._notionalCurrency)) {
      return false;
    }
    if (Double.compare(_dividendRatio, other._dividendRatio) != 0) {
      return false;
    }
    return true;
  }
}
