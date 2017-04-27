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

package com.springuni.auth.domain.model.session;

import static java.time.temporal.ChronoUnit.MINUTES;

import com.springuni.auth.domain.model.user.User;
import com.springuni.commons.domain.Entity;
import com.springuni.commons.util.DateTimeUtil;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Data stored in {@link User}'s session.
 */
@Getter
@EqualsAndHashCode
public class Session implements Entity<Long, Session> {

  public static final int DEFAULT_EXPIRATION_MINUTES = 7 * 24 * 60;

  private final Long sessionId;
  private final Long userId;

  private final LocalDateTime expiresAt;
  private final LocalDateTime issuedAt;

  /**
   * Creates a new {@link Session} with the given ID for the given User ID.
   *
   * @param sessionId Session ID
   * @param userId User ID
   */
  public Session(Long sessionId, Long userId) {
    this(sessionId, userId, DEFAULT_EXPIRATION_MINUTES);
  }

  /**
   * Creates a new {@link Session} with the given ID for the given User ID.
   *
   * @param sessionId Session ID
   * @param userId User ID
   * @param minutes minutes to expire from time of issue
   */
  public Session(Long sessionId, Long userId, int minutes) {
    this.sessionId = sessionId;
    this.userId = userId;
    if (minutes == 0) {
      minutes = DEFAULT_EXPIRATION_MINUTES;
    }
    expiresAt = DateTimeUtil.expireNowUtc(minutes, MINUTES);
    issuedAt = DateTimeUtil.nowUtc();
  }

  /**
   * Use for testing only.
   *
   * @param sessionId Session ID
   * @param userId User ID
   * @param expiresAt expire at
   * @param issuedAt issued at
   */
  public Session(Long sessionId, Long userId, LocalDateTime expiresAt, LocalDateTime issuedAt) {
    this.sessionId = sessionId;
    this.userId = userId;
    this.expiresAt = expiresAt;
    this.issuedAt = issuedAt;
  }

  @Override
  public Long getId() {
    return sessionId;
  }

  /**
   * Returns is this session if still valid.
   *
   * @return true if valid, false otherwise
   */
  public boolean isValid() {
    LocalDateTime now = DateTimeUtil.nowUtc();
    return isValid(now);
  }

  boolean isValid(LocalDateTime now) {
    return expiresAt.isAfter(now);
  }

  @Override
  public boolean sameIdentityAs(Session other) {
    return false;
  }

}
