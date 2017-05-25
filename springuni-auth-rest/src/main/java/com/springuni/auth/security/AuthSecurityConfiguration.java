package com.springuni.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.auth.domain.service.SessionService;
import com.springuni.auth.domain.service.UserService;
import com.springuni.commons.security.SecurityConfigurationSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

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
  public LoginRequestManager loginRequestManager(ObjectMapper objectMapper) {
    return new LoginRequestManager(objectMapper);
  }

  @Bean
  public LoginFilter loginFilter(
      AuthenticationManager authenticationManager,
      AuthenticationSuccessHandler authenticationSuccessHandler,
      AuthenticationFailureHandler authenticationFailureHandler,
      LoginRequestManager loginRequestManager) {

    return new LoginFilter(
        LOGIN_ENDPOINT, authenticationManager, authenticationSuccessHandler,
        authenticationFailureHandler, loginRequestManager);
  }

  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    UserService userService = lookup("userService");
    return new UsernamePasswordAuthenticationManager(userService);
  }

  @Override
  protected void customizeFilters(HttpSecurity http) {
    LoginFilter loginFilter = lookup("loginFilter");
    http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void customizeRememberMe(HttpSecurity http) throws Exception {
    UserService userService = lookup("userService");
    UserDetailsService userDetailsService = new DelegatingUserService(userService);

    SessionService sessionService = lookup("sessionService");
    PersistentTokenRepository tokenRepository =
        new DelegatingPersistentTokenRepository(sessionService);

    LoginRequestManager loginRequestManager = lookup("loginRequestManager");
    RememberMeServices rememberMeServices =
        new PersistentJwtTokenBasedRememberMeServices(
            "SECRET", userDetailsService, tokenRepository, loginRequestManager);

    http.rememberMe()
        .userDetailsService(userDetailsService)
        .tokenRepository(tokenRepository)
        .rememberMeServices(rememberMeServices);
  }

  @Override
  protected void customizeRequestAuthorization(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll();
  }

}
