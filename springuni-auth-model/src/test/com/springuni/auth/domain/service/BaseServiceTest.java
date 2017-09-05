package com.springuni.auth.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import com.springuni.auth.domain.model.userevent.UserEvent;
import com.springuni.auth.domain.model.userevent.UserEventEmitter;
import com.springuni.auth.domain.model.userevent.UserEventType;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

/**
 * Created by lcsontos on 5/25/17.
 */
public class BaseServiceTest {

  @Mock protected UserEventEmitter userEventEmitter;

  protected UserEvent captureEmittedUserEvent() {
    ArgumentCaptor<UserEvent> userEventCaptor =
        (ArgumentCaptor<UserEvent>) ArgumentCaptor.forClass(UserEvent.class);
    verify(userEventEmitter).emit(userEventCaptor.capture());
    return userEventCaptor.getValue();
  }

  protected void assertEmittedUserEvent(UserEventType expectedUserEventType) {
    UserEvent userEvent = captureEmittedUserEvent();
    assertEquals(expectedUserEventType, userEvent.getUserEventType());
  }

}
