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

import lombok.Getter;

/**
 * Created by lcsontos on 4/24/17.
 */
@Getter
public enum State {

  AK("Alaska"),
  AL("Alabama"),
  AR("Arkansas"),
  AZ("Arizona"),
  CA("California"),
  CO("Colorado"),
  CT("Connecticut"),
  DE("Delaware"),
  FL("Florida"),
  GA("Georgia"),
  HI("Hawaii"),
  IA("Iowa"),
  ID("Idaho"),
  IL("Illinois"),
  IN("Indiana"),
  KS("Kansas"),
  KY("Kentucky"),
  LA("Louisiana"),
  MA("Massachusetts"),
  MD("Maryland"),
  ME("Maine"),
  MI("Michigan"),
  MN("Minnesota"),
  MO("Missouri"),
  MS("Mississippi"),
  MT("Montana"),
  NC("North Carolina"),
  ND("North Dakota"),
  NE("Nebraska"),
  NH("New Hampshire"),
  NJ("New Jersey"),
  NM("New Mexico"),
  NV("Nevada"),
  NY("New York"),
  OH("Ohio"),
  OK("Oklahoma"),
  OR("Oregon"),
  PA("Pennsylvania"),
  RI("Rhode Island"),
  SC("South Carolina"),
  SD("South Dakota"),
  TN("Tennessee"),
  TX("Texas"),
  UT("Utah"),
  VA("Virginia"),
  VT("Vermont"),
  WA("Washington"),
  WI("Wisconsin"),
  WV("West Virginia"),
  WY("Wyoming"),
  NA("N/A");

  private String state;

  State(String state) {
    this.state = state;
  }

}
