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
 * A domain event is something that is unique, but does not have a lifecycle.
 */
public interface DomainEvent<T> {

  /**
   * The identity may be explicit, for example the sequence number of a payment,
   * or it could be derived from various aspects of the event such as where, when and what
   * has happened.
   *
   * @param other The other domain event
   * @return <code>true</code> if the given domain event and this event are regarded as being the
   *     same event
   */
  boolean sameEventAs(T other);

}
