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

package com.springuni.commons.security;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.commons.rest.RestErrorResponse;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * General authentication failure handler.
 */
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(DefaultAuthenticationFailureHandler.class);

  private final ObjectMapper objectMapper;

  public DefaultAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {

    LOGGER.warn(exception.getMessage());

    HttpStatus httpStatus = translateAuthenticationException(exception);

    response.setStatus(httpStatus.value());
    response.setContentType(APPLICATION_JSON_VALUE);

    writeResponse(response.getWriter(), httpStatus, exception);
  }

  protected HttpStatus translateAuthenticationException(AuthenticationException exception) {
    return UNAUTHORIZED;
  }

  protected void writeResponse(
      Writer writer, HttpStatus httpStatus, AuthenticationException exception) throws IOException {

    RestErrorResponse restErrorResponse = RestErrorResponse.of(httpStatus, exception);
    objectMapper.writeValue(writer, restErrorResponse);
  }

}
