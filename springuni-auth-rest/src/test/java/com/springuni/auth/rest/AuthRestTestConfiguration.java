package com.springuni.auth.rest;

import com.springuni.auth.domain.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lcsontos on 5/9/17.
 */
@Configuration
public class AuthRestTestConfiguration extends AuthRestConfiguration {

  @Bean
  public UserService userService() {
    return Mockito.mock(UserService.class);
  }

}
