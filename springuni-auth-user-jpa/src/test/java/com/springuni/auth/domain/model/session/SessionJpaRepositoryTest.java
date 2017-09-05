package com.springuni.auth.domain.model.session;

import com.springuni.auth.domain.model.AuthJpaRepositoryTestConfiguration;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lcsontos on 5/25/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AuthJpaRepositoryTestConfiguration.class})
@Transactional
@Rollback
public class SessionJpaRepositoryTest {

  @Autowired
  private SessionRepository sessionRepository;
  private Session session;

  @Before
  public void before() {
    session = new Session(1L, 1L, "123");
    sessionRepository.save(session);
  }

  @Test
  public void testFindById() {
    Optional<Session> sessionOptional = sessionRepository.findById(session.getId());
    Assert.assertTrue(sessionOptional.isPresent());
  }

  @Test
  public void testFindByUserId() {
    List<Session> sessions = sessionRepository.findByUserId(session.getUserId());
    Assert.assertNotNull(sessions);
    Assert.assertEquals(1, sessions.size());
  }

  @Test
  public void testSave() {

  }

}
