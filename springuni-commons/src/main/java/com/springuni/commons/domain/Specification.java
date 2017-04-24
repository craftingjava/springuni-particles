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
 * Specificaiton interface.
 * <p/>
 * Use {@link com.springuni.commons.domain.AbstractSpecification} as base for creating
 * specifications, and only the method {@link #isSatisfiedBy(Object)} must be implemented.
 */
public interface Specification<T> {

  /**
   * Check if {@code t} is satisfied by the specification.
   *
   * @param t Object to test.
   * @return {@code true} if {@code t} satisfies the specification.
   */
  boolean isSatisfiedBy(T t);

  /**
   * Create a new specification that is the AND operation of {@code this} specification and another
   * specification.
   *
   * @param specification Specification to AND.
   * @return A new specification.
   */
  Specification<T> and(Specification<T> specification);

  /**
   * Create a new specification that is the OR operation of {@code this} specification and another
   * specification.
   *
   * @param specification Specification to OR.
   * @return A new specification.
   */
  Specification<T> or(Specification<T> specification);

  /**
   * Create a new specification that is the NOT operation of {@code this} specification.
   *
   * @param specification Specification to NOT.
   * @return A new specification.
   */
  Specification<T> not(Specification<T> specification);

}
