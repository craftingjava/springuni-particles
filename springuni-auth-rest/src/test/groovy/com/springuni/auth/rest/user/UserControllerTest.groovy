package com.springuni.auth.rest.user

import com.springuni.auth.domain.model.user.User
import com.springuni.auth.domain.service.UserService
import com.springuni.auth.rest.AuthRestTestConfiguration
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import static org.hamcrest.Matchers.is
import static org.mockito.Mockito.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Created by lcsontos on 5/11/17.
 */
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [AuthRestTestConfiguration])
@WebAppConfiguration
class UserControllerTest {

  @Autowired
  WebApplicationContext context

  @Autowired
  UserService userService

  MockMvc mockMvc

  @Before
  void before() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

    when(userService.getUser(1L))
        .thenReturn(new User(1L, "test", "test@springuni.com"))
  }

  @Test
  void testGetUser() {
    mockMvc.perform(get("/users/1").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(1)))
        .andExpect(jsonPath("screenName", is("test")))
        .andExpect(jsonPath("contactData.email", is("test@springuni.com")))
        .andDo(print())

    verify(userService).getUser(1L)
    verifyNoMoreInteractions(userService)
  }

}
