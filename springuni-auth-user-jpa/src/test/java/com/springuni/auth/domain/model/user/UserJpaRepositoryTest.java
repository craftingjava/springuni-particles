package com.springuni.auth.domain.model.user;

import com.springuni.auth.domain.model.AuthJpaRepositoryTestConfiguration;
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
 * Created by lcsontos on 5/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AuthJpaRepositoryTestConfiguration.class})
@Transactional
@Rollback
public class UserJpaRepositoryTest {

  @Autowired
  private UserRepository userRepository;
  private User user;

  @Before
  public void before() {
    user = new User(1L, "test", "test@springuni.com");
    user.addConfirmationToken(ConfirmationTokenType.EMAIL, 10);
    userRepository.save(user);
  }

  @Test
  public void testDelete() throws Exception {
    userRepository.delete(user.getId());
    Optional<User> userOptional = userRepository.findById(user.getId());
    Assert.assertFalse(userOptional.isPresent());
  }

  @Test
  public void testFindById() {
    Optional<User> userOptional = userRepository.findById(user.getId());
    Assert.assertTrue(userOptional.isPresent());
  }

  @Test
  public void testFindByEmail() {
    Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
    Assert.assertTrue(userOptional.isPresent());
  }

  @Test
  public void testFindByScreenName() {
    Optional<User> userOptional = userRepository.findByScreenName(user.getScreenName());
    Assert.assertTrue(userOptional.isPresent());
  }

}
