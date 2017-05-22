package com.springuni.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.auth.domain.service.UserService;
import com.springuni.commons.security.SecurityConfigurationSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by lcsontos on 5/18/17.
 */
@Configuration
public class AuthSecurityConfiguration extends SecurityConfigurationSupport {

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public LoginFilter loginFilter(
      AuthenticationManager authenticationManager,
      AuthenticationSuccessHandler authenticationSuccessHandler,
      AuthenticationFailureHandler authenticationFailureHandler, ObjectMapper objectMapper) {

    return new LoginFilter(
        LOGIN_ENDPOINT, authenticationManager, authenticationSuccessHandler,
        authenticationFailureHandler, objectMapper);
  }

  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    UserService userService = (UserService) getApplicationContext().getBean("userService");
    return new UsernamePasswordAuthenticationManager(userService);
  }

  @Override
  protected void customizeFilters(HttpSecurity http) {
    LoginFilter loginFilter = (LoginFilter) getApplicationContext().getBean("loginFilter");
    http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void customizeRequestAuthorization(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll();
  }

}
