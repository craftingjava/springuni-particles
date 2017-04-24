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
 * Abstract base implementation of composite {@link Specification} with default
 * implementations for {@code and}, {@code or} and {@code not}.
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

  /**
   * {@inheritDoc}
   */
  public abstract boolean isSatisfiedBy(T t);

  /**
   * {@inheritDoc}
   */
  public Specification<T> and(final Specification<T> specification) {
    return new AndSpecification<>(this, specification);
  }

  /**
   * {@inheritDoc}
   */
  public Specification<T> or(final Specification<T> specification) {
    return new OrSpecification<>(this, specification);
  }

  /**
   * {@inheritDoc}
   */
  public Specification<T> not(final Specification<T> specification) {
    return new NotSpecification<>(specification);
  }

}
