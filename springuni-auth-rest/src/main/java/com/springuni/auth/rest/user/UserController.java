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

package com.springuni.auth.rest.user;

import com.springuni.auth.domain.model.user.User;
import com.springuni.auth.domain.service.UserService;
import com.springuni.commons.domain.exceptions.ApplicationException;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcsontos on 5/9/17.
 */
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserMapper userMapper;
  private final UserService userService;

  public UserController(UserMapper userMapper, UserService userService) {
    this.userMapper = userMapper;
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public UserDto getUser(@PathVariable long userId) throws ApplicationException {
    User user = userService.getUser(userId);
    return userMapper.toUserDto(user);
  }

  @GetMapping("/")
  public List<User> getUsers() {
    // TODO: add a service method for listing users
    return null;
  }

  @PostMapping
  public void createUser(@RequestBody @Validated UserDto userDto) throws ApplicationException {
    User user = userMapper.toUser(userDto);
    userService.signup(user, userDto.getPassword());
  }

  @DeleteMapping("/{userId}")
  public void deleteUser(@PathVariable long userId) throws ApplicationException {
    userService.delete(userId);
  }

  @PutMapping
  public UserDto updateUser(@RequestBody UserDto userDto) throws ApplicationException {
    User user = userMapper.toUserWithoutEmailAndScreenName(userDto);
    user = userService.store(user);
    return userMapper.toUserDto(user);
  }

  @PutMapping("{userId}/confirm_email/{token}")
  public void confirmEmail(@PathVariable Long userId, @PathVariable String token)
      throws ApplicationException {

    userService.confirmEmail(userId, token);
  }

  @PutMapping("{userId}/confirm_password_reset/{token}")
  public void confirmPasswordReset(@PathVariable Long userId, @PathVariable String token)
      throws ApplicationException {

    userService.confirmPasswordReset(userId, token);
  }

}
