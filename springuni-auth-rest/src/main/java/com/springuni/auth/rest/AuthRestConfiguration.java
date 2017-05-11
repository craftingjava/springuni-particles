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

package com.springuni.auth.rest;

import com.springuni.auth.domain.service.UserService;

import com.springuni.auth.rest.user.UserController;

import com.springuni.auth.rest.user.UserDtoMap;
import com.springuni.auth.rest.user.UserMap;
import com.springuni.commons.rest.RestConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by lcsontos on 5/9/17.
 */
@Configuration
@EnableWebMvc
public class AuthRestConfiguration extends RestConfiguration {

  /**
   * Creates the {@link UserController} instance.
   *
   * @param userService UserService
   * @return UserController
   */
  @Bean
  public UserController userController(ModelMapper modelMapper, UserService userService) {
    return new UserController(modelMapper, userService);
  }

  @Override
  protected void customizeModelMapper(ModelMapper modelMapper) {
    modelMapper.addMappings(new UserMap());
    modelMapper.addMappings(new UserDtoMap());
  }

}
