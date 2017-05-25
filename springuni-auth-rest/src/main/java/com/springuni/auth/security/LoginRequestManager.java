package com.springuni.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcsontos on 5/25/17.
 */
public class LoginRequestManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginRequestManager.class);

  private static final String LOGIN_REQUEST_ATTRIBUTE = "login_request";

  private final ObjectMapper objectMapper;

  public LoginRequestManager(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Optional<LoginRequest> getLoginRequest(HttpServletRequest request) {
    return Optional.ofNullable((LoginRequest)request.getAttribute(LOGIN_REQUEST_ATTRIBUTE));
  }

  public void setLoginRequest(HttpServletRequest request) throws Exception {
    if (getLoginRequest(request).isPresent()) {
      return;
    }

    LoginRequest loginRequest =
        objectMapper.readValue(request.getInputStream(), LoginRequest.class);

    request.setAttribute(LOGIN_REQUEST_ATTRIBUTE, loginRequest);
  }

}
