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

package com.springuni.auth.domain.service;

import com.springuni.auth.domain.model.session.Session;
import com.springuni.auth.domain.model.session.exceptions.NoSuchSessionException;
import com.springuni.auth.domain.model.user.User;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * {@link SessionService} takes care of issuing new sessions and checking existing ones.
 */
public interface SessionService {

  /**
   * Creates a {@link Session} for the given {@link User}.
   *
   * @param userId a {@link User}'s ID
   * @return a newly created session
   */
  Session createSession(long userId, String token);

  /**
   * Creates a {@link Session} for the given {@link User}, which is valid for {@code minutes}.
   *
   * @param userId a {@link User}'s ID
   * @param minutes minutes to expire from now
   * @return a newly created session
   */
  Session createSession(long userId, String token, int minutes);

  /**
   * Gets the {@link Session} for the given ID.
   *
   * @return a {@link Session} if it exists.
   * @throws NoSuchSessionException when there is no such session for the given ID
   */
  Optional<Session> findSession(Long id) throws NoSuchSessionException;

  /**
   * Gets the {@link Session} for the given ID.
   *
   * @return a {@link Session} if it exists.
   * @throws NoSuchSessionException when there is no such session for the given ID
   */
  Session getSession(Long id) throws NoSuchSessionException;

  /**
   * Logs the user out by invalidating all of its {@link Session}s.
   *
   * @param userId User ID
   */
  void logoutUser(Long userId) throws NoSuchSessionException;

  /**
   * Updates the {@link Session} for the given ID.
   *
   * @throws NoSuchSessionException when there is no such session for the given ID
   */
  void useSession(Long id, String value, LocalDateTime lastUsedAt)
      throws NoSuchSessionException;

}
