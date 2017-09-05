package com.springuni.auth.rest.user;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.springuni.auth.domain.model.user.User;
import com.springuni.auth.domain.model.user.exceptions.NoSuchUserException;
import com.springuni.auth.domain.service.UserService;
import com.springuni.auth.rest.AuthRestTestConfiguration;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by lcsontos on 5/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AuthRestTestConfiguration.class})
@WebAppConfiguration
public class UserControllerTest {

  @Autowired private WebApplicationContext context;
  @Autowired private UserService userService;
  private MockMvc mockMvc;

  @Before
  public void before() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    reset(userService);

    when(userService.getUser(0L)).thenThrow(NoSuchUserException.class);
    when(userService.getUser(1L)).thenReturn(new User(1L, "test", "test@springuni.com"));
  }

  @Test
  public void testGetUser() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/users/1").contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("screenName", Matchers.is("test"))).andExpect(
            MockMvcResultMatchers.jsonPath("contactData.email", Matchers.is("test@springuni.com")))
            .andDo(MockMvcResultHandlers.print());

    verify(userService).getUser(1L);
    verifyNoMoreInteractions(userService);
  }

  @Test
  public void testGetUser_notFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/users/0").contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode", Matchers.is(404)))
            .andExpect(MockMvcResultMatchers.jsonPath("reasonPhrase", Matchers.is("Not Found")))
            .andDo(MockMvcResultHandlers.print());

    verify(userService).getUser(0L);
    verifyNoMoreInteractions(userService);
  }

}
