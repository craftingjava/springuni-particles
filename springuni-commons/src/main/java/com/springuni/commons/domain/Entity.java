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

package com.springuni.commons.domain;

/**
 * An entity, as explained in the DDD book.
 */
public interface Entity<I, E> {

  /**
   * Entities compare by identity, not by attributes.
   *
   * @param other The other entity.
   * @return true if the identities are the same, regardles of other attributes.
   */
  boolean sameIdentityAs(E other);

  /**
   * Gets the entity's unique ID.
   *
   * @return ID
   */
  I getId();

  /**
   * Sets the entity's unique ID.
   *
   * @param id ID
   */
  void setId(I id);

  /**
   * Checks if {@code this} is a new entity or it isn't. An entity is new if it hasn't been an
   * identity assigned to.
   *
   * @return {@code true} if this is new entity, {@code false} otherwise
   */
  default boolean isNew() {
    if (getId() == null) {
      return true;
    }
    return false;
  }

}
