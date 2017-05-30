package com.springuni.commons.security;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
public class SecurityConfigurationSupport
    extends WebSecurityConfigurerAdapter implements EnvironmentAware {

  protected static final String LOGIN_ENDPOINT = "/auth/login";
  protected static final String LOGOUT_ENDPOINT = "/auth/logout";

  private static final String JWT_TOKEN_SECRET_KEY = "JWT_TOKEN_SECRET_KEY";
  private static final String REMEMBER_ME_TOKEN_SECRET_KEY = "REMEMBER_ME_TOKEN_SECRET_KEY";

  private Environment environment;

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
    return new JwtAuthenticationEntryPoint(objectMapper);
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler(
      JwtTokenService jwtTokenService) {
    return new DefaultAuthenticationSuccessHandler(jwtTokenService);
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler(ObjectMapper objectMapper) {
    return new DefaultAuthenticationFailureHandler(objectMapper);
  }

  @Bean
  public JwtTokenService jwtTokenService() {
    String secretKey = getJwtTokenSecretKey().orElseThrow(IllegalStateException::new);
    return new JwtTokenServiceImpl(secretKey);
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
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

  protected Optional<String> getJwtTokenSecretKey() {
    return Optional.ofNullable(environment.getProperty(JWT_TOKEN_SECRET_KEY));
  }

  protected Optional<String> getRememberMeTokenSecretKey() {
    return Optional.ofNullable(environment.getProperty(REMEMBER_ME_TOKEN_SECRET_KEY));
  }

  protected <T> T lookup(String beanName) {
    return (T) getApplicationContext().getBean(beanName);
  }

}
