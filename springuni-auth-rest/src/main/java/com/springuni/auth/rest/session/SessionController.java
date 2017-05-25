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

package com.springuni.auth.rest.session;

import com.springuni.auth.domain.model.session.Session;
import com.springuni.auth.domain.model.user.User;
import com.springuni.auth.domain.service.SessionService;
import com.springuni.auth.domain.service.UserService;
import com.springuni.auth.security.LoginRequest;
import com.springuni.commons.domain.exceptions.ApplicationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcsontos on 5/10/17.
 */
@RestController
public class SessionController {

  private final SessionService sessionService;
  private final UserService userService;

  public SessionController(SessionService sessionService, UserService userService) {
    this.sessionService = sessionService;
    this.userService = userService;
  }


  @GetMapping("/{sessionId}")
  public Session getSession(@PathVariable long sessionId) throws ApplicationException {
    return sessionService.getSession(sessionId);
  }

  @PostMapping
  public Session login(@RequestBody LoginRequest loginRequest) throws ApplicationException {
    User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    return sessionService.login(user);
  }

  @DeleteMapping("/{sessionId}")
  public void logout(@PathVariable long sessionId) throws ApplicationException {
    sessionService.logoutUser(sessionId);
  }

}
