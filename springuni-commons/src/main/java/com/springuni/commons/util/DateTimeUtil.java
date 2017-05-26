/**
 * Copyright (c) 2017-present Laszlo Csontos All rights reserved.
 *
 * This file is part of springuni-particles.
 *
 * springuni-particles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * springuni-particles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with springuni-particles.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.springuni.commons.util;

import static java.time.Clock.systemUTC;
import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Optional;

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

  /**
   * Returns a {@link Date} instance representing the given {@link LocalDateTime} according to UTC.
   *
   * @return a date time in UTC
   */
  public static Date toDate(LocalDateTime localDateTime) {
    return Optional.ofNullable(localDateTime)
        .map(ldt -> ldt.toInstant(UTC))
        .map(Date::from)
        .orElse(null);
  }

  /**
   * Returns a {@link LocalDateTime} instance representing the given {@link Date} according to UTC.
   *
   * @return a date time in UTC
   */
  public static LocalDateTime toLocalDateTime(Date date) {
    return Optional.ofNullable(date)
        .map(Date::toInstant)
        .map(i -> i.atOffset(UTC))
        .map(OffsetDateTime::toLocalDateTime)
        .orElse(null);
  }

}
