/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.server.conversion;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;

import com.opengamma.util.ArgumentChecker;

/**
 * Abstract base class for formatting double values.
 */
public abstract class DoubleValueFormatter {
  private static final int GROUP_SIZE = 3;
  private static final char PLAIN_STRING_MINUS_SIGN = '-';
  private static final char PLAIN_STRING_DECIMAL_SEPARATOR = '.';

  private final char _localeDecimalSeparator;
  private final char _localeGroupingSeparator;
  private final char _localeMinusSign;
  private final boolean _plainStringMatchesLocale;

  private final boolean _isCurrencyAmount;

  /**
   * Uses the format of the default locale.
   *
   * @param isCurrencyAmount
   *          true if the value being formatted is a currency amount
   */
  public DoubleValueFormatter(final boolean isCurrencyAmount) {
    this(isCurrencyAmount, DecimalFormatSymbols.getInstance());
  }

  /**
   * @param isCurrencyAmount
   *          true if the value being formatted is a currency amount
   * @param formatSymbols
   *          the formatting symbols
   */
  public DoubleValueFormatter(final boolean isCurrencyAmount, final DecimalFormatSymbols formatSymbols) {
    _localeGroupingSeparator = formatSymbols.getGroupingSeparator();
    _localeDecimalSeparator = formatSymbols.getDecimalSeparator();
    _localeMinusSign = formatSymbols.getMinusSign();
    _plainStringMatchesLocale = _localeMinusSign == PLAIN_STRING_MINUS_SIGN && _localeDecimalSeparator == PLAIN_STRING_DECIMAL_SEPARATOR;

    _isCurrencyAmount = isCurrencyAmount;
  }

  /**
   * Returns true if the number is a currency amount.
   *
   * @return true if the number is a currency amount.
   */
  public boolean isCurrencyAmount() {
    return _isCurrencyAmount;
  }

  /**
   * Transforms a {@link BigDecimal} value as required, for example setting the scale and
   * precision.
   *
   * @param value  the input value, not null
   * @return the processed value, not null
   */
  protected abstract BigDecimal process(BigDecimal value);

  /**
   * Gets the value after rounding.
   *
   * @param value
   *          the initial value, not null
   * @return the rounded value
   */
  public BigDecimal getRoundedValue(final BigDecimal value) {
    return process(value);
  }

  /**
   * Formats a value.
   * 
   * @param value
   *          the value, not null
   * @return the formatted value
   */
  public String format(final BigDecimal value) {
    final BigDecimal processedValue = process(value);
    return transformPlainNumberString(processedValue.toPlainString());
  }

  /**
   * Takes a plain number (rounded appropriately, and using '.' as the decimal separator and '-' if negative) and
   * applies locale-specific formatting.
   *
   * @param plainNumberString  the plain number, not null
   * @return the transformed number, not null
   */
  /* package */ String transformPlainNumberString(final String plainNumberString) {
    // A plain number string is of the form:
    //   (-)?[0-9]+(\.[0-9]+)?
    // i.e. using '-' for negative numbers and '.' as the decimal separator.
    ArgumentChecker.notNull(plainNumberString, "plainNumberString");
    final int decimalIdx = plainNumberString.indexOf(PLAIN_STRING_DECIMAL_SEPARATOR);
    final boolean isNegative = plainNumberString.charAt(0) == PLAIN_STRING_MINUS_SIGN;

    final int integerStartIdx = isNegative ? 1 : 0;
    final int integerEndIdx = decimalIdx > -1 ? decimalIdx : plainNumberString.length();
    final String integerPart = plainNumberString.substring(integerStartIdx, integerEndIdx);
    final int integerPartLength = integerPart.length();

    int firstGroupEndIdx = integerPartLength % GROUP_SIZE;
    if (firstGroupEndIdx == 0) {
      firstGroupEndIdx = GROUP_SIZE;
    }
    if (firstGroupEndIdx == integerPartLength && _plainStringMatchesLocale) {
      // Nothing to group and plain string matches locale formatting
      return plainNumberString;
    }
    final StringBuilder sb = new StringBuilder(plainNumberString.length() + integerPartLength / 3);
    if (isNegative) {
      sb.append(_localeMinusSign);
    }
    sb.append(integerPart.substring(0, firstGroupEndIdx));
    for (int i = firstGroupEndIdx; i < integerPartLength; i += GROUP_SIZE) {
      sb.append(_localeGroupingSeparator).append(integerPart.substring(i, i + 3));
    }
    if (decimalIdx > -1) {
      sb.append(_localeDecimalSeparator).append(plainNumberString.substring(decimalIdx + 1));
    }
    return sb.toString();
  }

}
