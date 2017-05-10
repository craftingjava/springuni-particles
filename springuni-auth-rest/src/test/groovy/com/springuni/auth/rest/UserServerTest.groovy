package com.springuni.auth.rest

import com.springuni.auth.domain.model.user.User
import com.springuni.auth.domain.service.UserService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

import static org.mockito.Mockito.*

/**
 * Created by lcsontos on 5/9/17.
 */
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [AuthRestTestConfiguration])
class UserServerTest {

  @Autowired
  RouterFunction<ServerResponse> routerFunction

  @Autowired
  UserServer userServer

  @Autowired
  UserService userService

  WebTestClient testClient

  @Before
  void before() {
    testClient = WebTestClient
        .bindToRouterFunction(routerFunction)
        .build()

    when(userService.findUser(1L)).thenReturn(Optional.of(new User(1L, "test", "test@springuni.com")))
  }

  @Test
  void testGetUser() {
    testClient.get()
        .uri("/users/1")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
  }

}
