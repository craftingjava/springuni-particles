package com.springuni.auth.rest

import com.springuni.auth.domain.service.UserService
import com.springuni.auth.rest.user.UserMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import static org.mockito.Mockito.mock

/**
 * Created by lcsontos on 5/9/17.
 */
@Configuration
class AuthRestTestConfiguration extends AuthRestConfiguration {

  @Bean
  UserMapper userMapper() {
    return mock(UserMapper)
  }

  @Bean
  UserService userService() {
    return mock(UserService)
  }

}
