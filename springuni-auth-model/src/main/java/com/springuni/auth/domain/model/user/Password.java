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

import com.springuni.commons.domain.ValueObject;
import lombok.Data;

/**
 * Represents {@link User}s' password.
 */
@Data
public class Password implements ValueObject<Password> {

  private final String passwordHash;
  private final String passwordSalt;

  /**
   * Create a new password.
   *
   * @param passwordHash password hash
   * @param passwordSalt password salt
   */
  public Password(String passwordHash, String passwordSalt) {
    this.passwordHash = passwordHash;
    this.passwordSalt = passwordSalt;
  }

  @Override
  public boolean sameValueAs(Password other) {
    return equals(other);
  }

}
