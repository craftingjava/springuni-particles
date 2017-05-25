package com.springuni.auth.domain.service

import com.springuni.auth.domain.model.userevent.UserEvent
import com.springuni.auth.domain.model.userevent.UserEventEmitter
import com.springuni.auth.domain.model.userevent.UserEventType
import org.mockito.ArgumentCaptor
import org.mockito.Mock

import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.verify

/**
 * Created by lcsontos on 5/25/17.
 */
class BaseServiceTest {

  @Mock UserEventEmitter userEventEmitter

  protected void assertEmittedUserEvent(UserEventType expectedUserEventType) {
    UserEvent userEvent = captureEmittedUserEvent()
    assertEquals(expectedUserEventType, userEvent.userEventType)
  }

  protected UserEvent captureEmittedUserEvent() {
    ArgumentCaptor<UserEvent> userEventCaptor = ArgumentCaptor.forClass(UserEvent)
    verify(userEventEmitter).emit(userEventCaptor.capture())
    return userEventCaptor.value
  }

}
