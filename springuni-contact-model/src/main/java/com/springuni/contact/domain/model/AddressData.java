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

package com.springuni.contact.domain.model;

import com.springuni.commons.domain.ValueObject;
import lombok.Data;

/**
 * Represents an address.
 */
@Data
public class AddressData implements ValueObject<AddressData> {

  private Country country;
  private State state;
  private String city;

  private String addressLine1;
  private String addressLine2;

  private String zipCode;

  private AddressType addressType;

  @Override
  public boolean sameValueAs(AddressData other) {
    return equals(other);
  }

}
