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

package com.springuni.auth.crypto;

import com.springuni.auth.domain.model.user.Password;

/**
 * A functional interface for checking encrypted passwords against a cleartext password.
 */
@FunctionalInterface
public interface PasswordChecker {

  /**
   * Checks the given {@code password} against the given {@code rawPassword}.
   *
   * @param password Encrypted password
   * @param rawPassword Cleartext password
   * @return true if matches, false otherwise
   */
  boolean check(Password password, String rawPassword);

}
