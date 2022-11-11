/**
 * Copyright (C) 2015 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.starling.client.utils;

import java.time.DayOfWeek;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * Temporal adjuster to find previous business day (in the loose sense, not using calendars.
 */
public class PreviousBusinessDayTemporalAdjuster implements TemporalAdjuster {
  @Override
  public Temporal adjustInto(Temporal temporal) {
    Temporal yesterday = temporal.minus(Period.ofDays(1));
    DayOfWeek lastWeekday = DayOfWeek.from(yesterday);
    if (lastWeekday.equals(DayOfWeek.SATURDAY) || lastWeekday.equals(DayOfWeek.SUNDAY)) {
      return yesterday.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
    } else {
      return yesterday;
    }
  }
}
