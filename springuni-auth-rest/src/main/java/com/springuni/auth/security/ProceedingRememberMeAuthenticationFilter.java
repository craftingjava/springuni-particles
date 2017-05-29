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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

/**
 * Created by lcsontos on 5/29/17.
 */
public class ProceedingRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ProceedingRememberMeAuthenticationFilter.class);

  private AuthenticationSuccessHandler successHandler;

  public ProceedingRememberMeAuthenticationFilter(
      AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {

    super(authenticationManager, rememberMeServices);
  }

  @Override
  public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
    this.successHandler = successHandler;
  }

  @Override
  protected void onSuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, Authentication authResult) {

    if (successHandler == null) {
      return;
    }

    try {
      successHandler.onAuthenticationSuccess(request, response, authResult);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

}
