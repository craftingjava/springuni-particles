package com.springuni.auth.domain.service

import com.springuni.auth.domain.model.session.Session
import com.springuni.auth.domain.model.session.SessionRepository
import com.springuni.auth.domain.model.session.exceptions.NoSuchSessionException
import com.springuni.commons.util.DateTimeUtil
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import java.time.LocalDateTime

import static com.springuni.commons.util.DateTimeUtil.nowUtc
import static com.springuni.auth.domain.model.userevent.UserEventType.*
import static org.junit.Assert.*
import static org.mockito.Mockito.when

/**
 * Created by lcsontos on 4/27/17.
 */
@RunWith(MockitoJUnitRunner.Silent)
class SessionServiceTest extends BaseServiceTest {

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
    validSession = new Session(1L, 1L, "123", EXPIRES_AT, ISSUED_AT)
    expiredSession = new Session(2L, 2L, "1234", EXPIRED_AT, ISSUED_AT)

    sessionService = new SessionServiceImpl(sessionRepository, userEventEmitter)

    when(sessionRepository.findById(validSession.id)).thenReturn(Optional.of(validSession))
    when(sessionRepository.save(validSession)).thenReturn(validSession)

    when(sessionRepository.findById(expiredSession.id)).thenReturn(Optional.of(expiredSession))
    when(sessionRepository.save(expiredSession)).thenReturn(expiredSession)

    when(sessionRepository.findById(NON_EXISTENT_SESSION_ID)).thenReturn(Optional.empty())
  }

  @Test
  void testCreateSession() {
    Session session = sessionService.createSession(1L, 1L, "1234")
    assertNotNull(session)
    assertEquals(1L, session.getId())
    assertEquals(1L, session.getUserId())
  }

  @Test
  void testFindSession_withExpired() {
    Optional<Session> session = sessionService.findSession(expiredSession.id)
    assertNotNull(session)
    assertFalse(session.isPresent())
  }

  @Test
  void testFindSession_withValid() {
    Optional<Session> session = sessionService.findSession(validSession.id)
    assertNotNull(session)
    assertTrue(session.isPresent())
  }

  @Test(expected = NoSuchSessionException)
  void testGetSession_withExpired() {
    sessionService.getSession(expiredSession.id)
  }

  @Test
  void testGetSession_withValid() {
    Session session = sessionService.getSession(validSession.id)
    assertNotNull(session)
  }

  @Test
  void testLogoutUser() {
    sessionService.logoutUser(validSession.userId)
    assertEmittedUserEvent(LOGGED_OUT)
  }

  @Test(expected = NoSuchSessionException)
  void testUseSession_withExpired() {
    sessionService.useSession(expiredSession.id, "1234", nowUtc())
  }

  @Test
  void testUseSession_withValid() {
    sessionService.useSession(validSession.id, "1234", nowUtc())
  }

}
