package com.springuni.commons.util;

import static java.time.Clock.systemUTC;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

/**
 * Utility methods for date & time manipulations.
 */
public final class DateTimeUtil {

  private DateTimeUtil() {
  }

  /**
   * Calculate and return an expiration date as of {@code nowUtc} using the given period time.
   *
   * @param time time period
   * @param unit time unit of {@code time}
   * @return an expiration date
   */
  public static LocalDateTime expireNowUtc(int time, TemporalUnit unit) {
    return nowUtc().plus(time, unit);
  }

  /**
   * Returns a {@link LocalDateTime} instance representing the current moment according to UTC.
   *
   * @return Current date time in UTC
   */
  public static LocalDateTime nowUtc() {
    return LocalDateTime.now(systemUTC());
  }

}
