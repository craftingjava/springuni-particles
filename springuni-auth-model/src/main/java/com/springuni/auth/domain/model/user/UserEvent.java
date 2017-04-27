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

package com.springuni.auth.domain.model.user;

import com.springuni.commons.domain.DomainEvent;
import java.util.Optional;
import lombok.Data;

/**
 * An event related to one of the {@link UserEventType}s.
 */
@Data
public class UserEvent<P> implements DomainEvent<UserEvent> {

  private final Long userId;
  private final UserEventType userEventType;

  private final P payload;

  /**
   * Create a new user event.
   *
   * @param userId {@link User}'s ID
   * @param userEventType {@link UserEventType}
   */
  public UserEvent(Long userId, UserEventType userEventType) {
    this(userId, userEventType, null);
  }

  /**
   * Create a new user event.
   *
   * @param userId {@link User}'s ID
   * @param userEventType {@link UserEventType}
   * @param payload event's payload
   */
  public UserEvent(Long userId, UserEventType userEventType, P payload) {
    this.userId = userId;
    this.userEventType = userEventType;
    this.payload = payload;
  }

  /**
   * Gets this event's payload if any.
   *
   * @return a Payload
   */
  public Optional<P> getPayload() {
    return Optional.ofNullable(payload);
  }

  @Override
  public boolean sameEventAs(UserEvent other) {
    return equals(other);
  }

}
