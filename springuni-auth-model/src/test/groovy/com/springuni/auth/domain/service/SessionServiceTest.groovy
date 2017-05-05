package com.springuni.auth.domain.service

import com.springuni.auth.domain.model.session.Session
import com.springuni.auth.domain.model.session.SessionRepository
import com.springuni.auth.domain.model.session.exceptions.NoSuchSessionException
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import java.time.LocalDateTime

import static org.mockito.Mockito.when

/**
 * Created by lcsontos on 4/27/17.
 */
@RunWith(MockitoJUnitRunner.Silent)
class SessionServiceTest {

  static final LocalDateTime ISSUED_AT = LocalDateTime.parse("2017-04-01T10:15:30")
  static final LocalDateTime EXPIRED_AT = LocalDateTime.parse("2017-04-12T10:15:30")
  static final LocalDateTime EXPIRES_AT = LocalDateTime.parse("9999-12-31T10:15:30")

  static final NON_EXISTENT_SESSION_ID = 3L

  @Mock SessionRepository sessionRepository

  Session validSession
  Session expiredSession
  SessionService sessionService

  @Before
  void before() {
    validSession = new Session(1L, 1L, EXPIRES_AT, ISSUED_AT)
    expiredSession = new Session(2L, 2L, EXPIRED_AT, ISSUED_AT)

    sessionService = new SessionServiceImpl(sessionRepository)

    when(sessionRepository.findById(validSession.id)).thenReturn(Optional.of(validSession))
    when(sessionRepository.save(validSession)).thenReturn(validSession)

    when(sessionRepository.findById(expiredSession.id)).thenReturn(Optional.of(validSession))
    when(sessionRepository.save(expiredSession)).thenReturn(expiredSession)

    when(sessionRepository.findById(NON_EXISTENT_SESSION_ID)).thenReturn(Optional.empty())
    when(sessionRepository.delete(NON_EXISTENT_SESSION_ID)).thenThrow(NoSuchSessionException)
  }

  @Test
  void testGetSession() {
    // TODO
  }

  @Test
  void testLogin() {
    // TODO
  }

  @Test
  void testLogout() {
    // TODO
  }

}
