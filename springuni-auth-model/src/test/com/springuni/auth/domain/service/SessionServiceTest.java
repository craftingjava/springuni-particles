package com.springuni.auth.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.springuni.auth.domain.model.session.Session;
import com.springuni.auth.domain.model.session.SessionRepository;
import com.springuni.auth.domain.model.session.exceptions.NoSuchSessionException;
import com.springuni.auth.domain.model.userevent.UserEventType;
import com.springuni.commons.util.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by lcsontos on 4/27/17.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SessionServiceTest extends BaseServiceTest {

  private static final LocalDateTime ISSUED_AT = LocalDateTime.parse("2017-04-01T10:15:30");
  private static final LocalDateTime EXPIRED_AT = LocalDateTime.parse("2017-04-12T10:15:30");
  private static final LocalDateTime EXPIRES_AT = LocalDateTime.parse("9999-12-31T10:15:30");
  private static final Long NON_EXISTENT_SESSION_ID = 3L;

  @Mock private SessionRepository sessionRepository;

  private Session validSession;
  private Session expiredSession;
  private SessionService sessionService;

  @Before
  public void before() {
    validSession = new Session(1L, 1L, "123", EXPIRES_AT, ISSUED_AT);
    expiredSession = new Session(2L, 2L, "1234", EXPIRED_AT, ISSUED_AT);

    sessionService = new SessionServiceImpl(sessionRepository, userEventEmitter);

    Mockito.when(sessionRepository.findById(validSession.getId()))
        .thenReturn(Optional.of(validSession));
    Mockito.when(sessionRepository.save(validSession)).thenReturn(validSession);

    Mockito.when(sessionRepository.findById(expiredSession.getId()))
        .thenReturn(Optional.of(expiredSession));
    Mockito.when(sessionRepository.save(expiredSession)).thenReturn(expiredSession);

    Mockito.when(sessionRepository.findById(NON_EXISTENT_SESSION_ID))
        .thenReturn(Optional.empty());
  }

  @Test
  public void testCreateSession() {
    Session session = sessionService.createSession(1L, 1L, "1234");
    assertNotNull(session);
    assertEquals(1L, (long)session.getId());
    assertEquals(1L, (long)session.getUserId());
  }

  @Test
  public void testFindSession_withExpired() {
    Optional<Session> session = sessionService.findSession(expiredSession.getId());
    assertNotNull(session);
    assertFalse(session.isPresent());
  }

  @Test
  public void testFindSession_withValid() {
    Optional<Session> session = sessionService.findSession(validSession.getId());
    assertNotNull(session.isPresent());
  }

  @Test(expected = NoSuchSessionException.class)
  public void testGetSession_withExpired() throws Exception{
    sessionService.getSession(expiredSession.getId());
  }

  @Test
  public void testGetSession_withValid() throws Exception {
    Session session = sessionService.getSession(validSession.getId());
    assertNotNull(session);
  }

  @Test
  public void testLogoutUser() {
    sessionService.logoutUser(validSession.getUserId());
    assertEmittedUserEvent(UserEventType.LOGGED_OUT);
  }

  @Test(expected = NoSuchSessionException.class)
  public void testUseSession_withExpired() throws Exception {
    sessionService.useSession(expiredSession.getId(), "1234", DateTimeUtil.nowUtc());
  }

  @Test
  public void testUseSession_withValid() throws Exception {
    sessionService.useSession(validSession.getId(), "1234", DateTimeUtil.nowUtc());
  }

}
