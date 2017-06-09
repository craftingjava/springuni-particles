package com.springuni.auth.domain.model.session

import com.springuni.auth.domain.model.AuthJpaRepositoryTestConfiguration
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

import static org.junit.Assert.*

/**
 * Created by lcsontos on 5/25/17.
 */
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [AuthJpaRepositoryTestConfiguration])
@Transactional
@Rollback
class SessionJpaRepositoryTest {

  @Autowired
  SessionRepository sessionRepository

  Session session

  @Before
  void before() {
    session = new Session(1L, 1L, "123")
    sessionRepository.save(session)
  }

  @Test
  void testFindById() {
    Optional<Session> sessionOptional = sessionRepository.findById(session.id)
    assertTrue(sessionOptional.isPresent())
  }

  @Test
  void testFindByUserId() {
    List<Session> sessions = sessionRepository.findByUserId(session.userId)
    assertNotNull(sessions)
    assertEquals(1, sessions.size())
  }

  @Test
  void testSave() {

  }

}
