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

/**
 * {@link SessionService} takes care of issuing new sessions and checking existing ones.
 */
public interface SessionService {

  /**
   * Gets the {@link Session} for the given ID.
   *
   * @return a {@link Session} if it exists.
   * @throws NoSuchSessionException when there is no such session for the given ID
   */
  Session getSession(Long sessionId) throws NoSuchSessionException;

  /**
   * Creates a {@link Session} for the given {@link User}.
   *
   * @param user a {@link User}
   * @return a newly created session
   */
  Session login(User user);

  /**
   * Creates a {@link Session} for the given {@link User}, which is valid for {@code minutes}.
   *
   * @param user a {@link User}
   * @param minutes minutes to expire from now
   * @return a newly created session
   */
  Session login(User user, int minutes);

  /**
   * Logs the user out by invalidating its {@link Session}.
   *
   * @param sessionId Session ID
   */
  void logout(Long sessionId) throws NoSuchSessionException;

}
