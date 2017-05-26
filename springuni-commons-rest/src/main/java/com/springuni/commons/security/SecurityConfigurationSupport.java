package com.springuni.commons.security;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by lcsontos on 5/18/17.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfigurationSupport extends WebSecurityConfigurerAdapter {

  protected static final String LOGIN_ENDPOINT = "/sessions";

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
    return new JwtAuthenticationEntryPoint(objectMapper);
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler(
      JwtTokenService jwtTokenService, ObjectMapper objectMapper) {

    return new DefaultAuthenticationSuccessHandler(jwtTokenService, objectMapper);
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler(ObjectMapper objectMapper) {
    return new DefaultAuthenticationFailureHandler(objectMapper);
  }

  @Bean
  public JwtTokenService jwtTokenService() {
    return new JwtTokenServiceImpl();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(new JwtAuthenticationProvider());
    customizeAuthenticationManager(auth);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    AuthenticationEntryPoint authenticationEntryPoint = lookup("authenticationEntryPoint");

    http.csrf().disable()
        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .sessionManagement().sessionCreationPolicy(STATELESS);

    customizeRequestAuthorization(http.authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers(POST, LOGIN_ENDPOINT).permitAll()
        .and());

    http.authorizeRequests().anyRequest().authenticated();

    JwtTokenService jwtTokenService = lookup("jwtTokenService");

    customizeFilters(
        http.addFilterBefore(
            new JwtAuthenticationFilter(jwtTokenService),
            UsernamePasswordAuthenticationFilter.class));

    customizeRememberMe(http);
  }

  protected void customizeAuthenticationManager(AuthenticationManagerBuilder auth) {
  }

  protected void customizeFilters(HttpSecurity http) {
  }

  protected void customizeRememberMe(HttpSecurity http) throws Exception {
  }

  protected void customizeRequestAuthorization(HttpSecurity http) throws Exception {
  }

  protected <T> T lookup(String beanName) {
    return (T) getApplicationContext().getBean(beanName);
  }

}
