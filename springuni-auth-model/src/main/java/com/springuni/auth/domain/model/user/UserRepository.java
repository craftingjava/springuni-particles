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

import com.springuni.auth.domain.model.user.exceptions.NoSuchUserException;
import java.util.Optional;

/**
 * Repository for managing the lifecycle of {@link User} entities.
 */
public interface UserRepository {

  /**
   * Deletes the given user, provided that it exists.
   *
   * @param userId {@link User}'s ID
   * @throws NoSuchUserException if the user doesn't
   *     exist
   */
  void delete(Long userId);

  /**
   * Finds a user based on its ID.
   *
   * @param id ID
   * @return a {@link User}
   */
  Optional<User> findById(Long id);

  /**
   * Finds a user by email address.
   *
   * @param email Email address
   * @return a {@link User}
   */
  Optional<User> findByEmail(String email);

  /**
   * Finds a user by screen name.
   * @param screenName Screen name
   * @return a {@link User}
   */
  Optional<User> findByScreenName(String screenName);

  /**
   * Stores the given user.
   * @param user a {@link User}
   * @return the stored {@link User}
   */
  User save(User user);

}
