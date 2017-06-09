package com.springuni.auth.crypto

import com.springuni.auth.domain.model.user.Password
import groovy.util.logging.Slf4j
import org.junit.Test

import static org.junit.Assert.*

/**
 * Created by lcsontos on 6/9/17.
 */
@Slf4j
class Pbkdf2PasswordSecurityTest {

  static final RAW_PASSWORD = "ei71N7*446A%"

  final PasswordSecurity passwordSecurity = new Pbkdf2PasswordSecurityImpl()

  @Test
  void testEncryptAndCheck_withBadPassword() {
    Password password = passwordSecurity.ecrypt(RAW_PASSWORD)
    assertNotNull(password)
    assertFalse(passwordSecurity.check(password, RAW_PASSWORD + "X"))
  }


  @Test
  void testEncryptAndCheck_withGoodPassword() {
    Password password = passwordSecurity.ecrypt(RAW_PASSWORD)
    assertNotNull(password)
    assertTrue(passwordSecurity.check(password, RAW_PASSWORD))
  }

}
