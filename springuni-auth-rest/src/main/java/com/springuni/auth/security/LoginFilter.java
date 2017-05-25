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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Processes login requests and delegates deciding the decision to {@link AuthenticationManager}.
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private static final String LOGIN_REQUEST_ATTRIBUTE = "login_request";

  private final ObjectMapper objectMapper;

  /**
   * Create a login filter.
   * @param filterProcessesUrl url to listen on
   * @param authenticationManager authentication manager
   * @param objectMapper an ObjectMapper instance
   */
  public LoginFilter(
      String filterProcessesUrl, AuthenticationManager authenticationManager,
      AuthenticationSuccessHandler authenticationSuccessHandler,
      AuthenticationFailureHandler authenticationFailureHandler,
      ObjectMapper objectMapper) {

    setFilterProcessesUrl(filterProcessesUrl);
    setAuthenticationManager(authenticationManager);
    setAuthenticationSuccessHandler(authenticationSuccessHandler);
    setAuthenticationFailureHandler(authenticationFailureHandler);

    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    try {
      LoginRequest loginRequest =
          objectMapper.readValue(request.getInputStream(), LoginRequest.class);

      request.setAttribute(LOGIN_REQUEST_ATTRIBUTE, loginRequest);

      return super.attemptAuthentication(request, response);
    } catch (IOException ioe) {
      throw new InternalAuthenticationServiceException(ioe.getMessage(), ioe);
    }
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return toLoginRequest(request).getUsername();
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    return toLoginRequest(request).getPassword();
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    super.unsuccessfulAuthentication(request, response, failed);
  }

  private LoginRequest toLoginRequest(HttpServletRequest request) {
    return (LoginRequest)request.getAttribute(LOGIN_REQUEST_ATTRIBUTE);
  }

}
