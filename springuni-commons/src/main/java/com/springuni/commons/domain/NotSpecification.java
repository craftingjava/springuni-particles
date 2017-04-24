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
 * NOT decorator, used to create a new specifcation that is the inverse (NOT) of the given spec.
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

  private Specification<T> spec;

  /**
   * Create a new NOT specification based on another spec.
   *
   * @param spec Specification instance to not.
   */
  public NotSpecification(final Specification<T> spec) {
    this.spec = spec;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isSatisfiedBy(final T t) {
    return !spec.isSatisfiedBy(t);
  }

}
