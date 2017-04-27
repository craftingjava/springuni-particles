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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common input validation methods.
 */
public class Validator {

  private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
      "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)"
          + "+[\\w](?:[\\w-]*[\\w])?");

  private Validator() {
  }

  /**
   * Returns <code>true</code> if the string is a valid email address.
   *
   * @param  email the string to check
   * @return <code>true</code> if the string is a valid email address;
   *         <code>false</code> otherwise
   */
  public static boolean isEmail(String email) {
    if (Objects.isNull(email)) {
      return false;
    }
    Matcher matcher = EMAIL_ADDRESS_PATTERN.matcher(email);
    return matcher.matches();
  }

}
