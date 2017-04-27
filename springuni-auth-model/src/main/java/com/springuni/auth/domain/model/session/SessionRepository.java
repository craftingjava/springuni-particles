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

import com.springuni.auth.domain.model.session.exceptions.NoSuchSessionException;
import java.util.Optional;

/**
 * Repository for managing the lifecycle of {@link Session} entities.
 */
public interface SessionRepository {

  /**
   * Deletes the given user, provided that it exists.
   *
   * @param sessionId {@link Session}'s ID
   * @throws NoSuchSessionException if the user doesn't exist
   */
  void delete(Long sessionId) throws NoSuchSessionException;

  /**
   * Finds a session based on its ID.
   *
   * @param sessionId ID
   * @return a {@link Session}
   */
  Optional<Session> findById(Long sessionId);

  /**
   * Stores the given session.
   *
   * @param session a {@link Session}
   * @return the stored {@link Session}
   */
  Session save(Session session);

}
