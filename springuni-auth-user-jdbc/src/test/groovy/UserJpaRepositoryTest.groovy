import com.springuni.auth.domain.model.user.UserRepository
import com.springuni.jpa.JpaConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

import static org.junit.Assert.*

/**
 * Created by lcsontos on 5/5/17.
 */
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [JpaConfig])
@Transactional
@TransactionConfiguration(defaultRollback = true)
class UserJpaRepositoryTest {

  @Autowired
  UserRepository userRepository

  @Test
  void testUserRepository() {
    assertNotNull(userRepository)
  }

}
