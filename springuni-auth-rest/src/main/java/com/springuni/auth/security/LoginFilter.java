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

package com.springuni.auth.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Processes login requests and delegates deciding the decision to {@link AuthenticationManager}.
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final LoginRequestManager loginRequestManager;

  /**
   * Create a login filter.
   * @param loginRequestManager a LoginRequestManager instance
   */
  public LoginFilter(LoginRequestManager loginRequestManager) {
    this.loginRequestManager = loginRequestManager;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    try {
      loginRequestManager.setLoginRequest(request);
      return super.attemptAuthentication(request, response);
    } catch (Exception e) {
      throw new InternalAuthenticationServiceException(e.getMessage(), e);
    }
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return loginRequestManager
        .getLoginRequest(request)
        .map(LoginRequest::getUsername)
        .orElse("");
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    return loginRequestManager
        .getLoginRequest(request)
        .map(LoginRequest::getPassword)
        .orElse("");
  }

}
