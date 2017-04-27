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

package com.springuni.user.domain.model;

import static java.time.temporal.ChronoUnit.HOURS;

import com.springuni.commons.domain.ValueObject;
import com.springuni.commons.util.DateTimeUtil;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Data stored in {@link User}'s session.
 */
@Data
public class Session implements ValueObject<Session> {

  private final String sessionId;
  private final Long userId;
  private final LocalDateTime expiresAt;

  /**
   * Creates a new {@link Session} with the given ID for the given User ID.
   *
   * @param sessionId Session ID
   * @param userId User ID
   */
  public Session(String sessionId, Long userId) {
    this.sessionId = sessionId;
    this.userId = userId;
    expiresAt = DateTimeUtil.expireNowUtc(24, HOURS);
  }

  @Override
  public boolean sameValueAs(Session other) {
    return equals(other);
  }

}
