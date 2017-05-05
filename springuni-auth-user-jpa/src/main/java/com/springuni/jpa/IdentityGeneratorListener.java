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

package com.springuni.jpa;

import com.springuni.commons.domain.Entity;
import com.springuni.commons.util.IdentityGenerator;
import javax.persistence.PrePersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcsontos on 5/5/17.
 */
public class IdentityGeneratorListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(IdentityGeneratorListener.class);


  /**
   * Generates a new identity for the given {@link Entity}.
   * @param entity Entity
   */
  @PrePersist
  public void generate(Entity<Long, ?> entity) {
    if (!entity.isNew()) {
      return;
    }
    long id = IdentityGenerator.generate();
    entity.setId(id);
    LOGGER.debug("Generated ID {} for {}.", id, entity.getClass());
  }

}
