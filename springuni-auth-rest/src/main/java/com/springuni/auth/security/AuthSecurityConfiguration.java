package com.springuni.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.auth.domain.service.SessionService;
import com.springuni.auth.domain.service.UserService;
import com.springuni.commons.security.SecurityConfigurationSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

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
  public AuthenticationProvider usernamePasswordAuthenticationProvider(UserService userService) {
    return new UsernamePasswordAuthenticationProvider(userService);
  }

  @Bean
  public AbstractAuthenticationProcessingFilter loginFilter(
      ObjectMapper objectMapper, AuthenticationManager authenticationManager,
      AuthenticationSuccessHandler authenticationSuccessHandler,
      AuthenticationFailureHandler authenticationFailureHandler,
      RememberMeServices rememberMeServices) {

    AbstractAuthenticationProcessingFilter loginFilter =
        new LoginFilter(LOGIN_ENDPOINT, objectMapper);

    loginFilter.setAuthenticationManager(authenticationManager);
    loginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    loginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
    loginFilter.setRememberMeServices(rememberMeServices);

    return loginFilter;
  }

  @Bean
  public UserDetailsService userDetailsService(UserService userService) {
    return new DelegatingUserService(userService);
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository(SessionService sessionService) {
    return new DelegatingPersistentTokenRepository(sessionService);
  }

  @Bean
  public RememberMeAuthenticationFilter rememberMeAuthenticationFilter(
      AuthenticationManager authenticationManager, RememberMeServices rememberMeServices,
      AuthenticationSuccessHandler authenticationSuccessHandler) {

    RememberMeAuthenticationFilter rememberMeAuthenticationFilter =
        new ProceedingRememberMeAuthenticationFilter(authenticationManager, rememberMeServices);

    rememberMeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);

    return rememberMeAuthenticationFilter;
  }

  @Bean
  public RememberMeServices rememberMeServices(
      UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {

    String secretKey = getRememberMeTokenSecretKey().orElseThrow(IllegalStateException::new);

    return new PersistentJwtTokenBasedRememberMeServices(
        secretKey, userDetailsService, persistentTokenRepository);
  }

  @Override
  protected void customizeAuthenticationManager(AuthenticationManagerBuilder auth) {
    AuthenticationProvider usernamePasswordAuthenticationProvider =
        lookup("usernamePasswordAuthenticationProvider");
    auth.authenticationProvider(usernamePasswordAuthenticationProvider);
  }

  @Override
  protected void customizeFilters(HttpSecurity http) {
    LoginFilter loginFilter = lookup("loginFilter");
    http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void customizeRememberMe(HttpSecurity http) throws Exception {
    UserDetailsService userDetailsService = lookup("userDetailsService");
    PersistentTokenRepository persistentTokenRepository = lookup("persistentTokenRepository");
    AbstractRememberMeServices rememberMeServices = lookup("rememberMeServices");
    RememberMeAuthenticationFilter rememberMeAuthenticationFilter =
        lookup("rememberMeAuthenticationFilter");

    http.rememberMe()
        .userDetailsService(userDetailsService)
        .tokenRepository(persistentTokenRepository)
        .rememberMeServices(rememberMeServices)
        .key(rememberMeServices.getKey())
        .and()
        .logout()
        .logoutUrl(LOGOUT_ENDPOINT)
        .and()
        .addFilterAt(rememberMeAuthenticationFilter, RememberMeAuthenticationFilter.class);
  }

  @Override
  protected void customizeRequestAuthorization(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll();
  }

}
