package com.springuni.auth.crypto;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.springuni.auth.domain.model.user.Password;
import groovy.util.logging.Slf4j;
import org.junit.Test;

/**
 * Created by lcsontos on 6/9/17.
 */
@Slf4j
public class Pbkdf2PasswordSecurityTest {

  private static final String RAW_PASSWORD = "ei71N7*446A%";
  private final PasswordSecurity passwordSecurity = new Pbkdf2PasswordSecurityImpl();

  @Test
  public void testEncryptAndCheck_withBadPassword() {
    Password password = passwordSecurity.ecrypt(RAW_PASSWORD);
    assertNotNull(password);
    assertFalse(passwordSecurity.check(password, RAW_PASSWORD + "X"));
  }

  @Test
  public void testEncryptAndCheck_withGoodPassword() {
    Password password = passwordSecurity.ecrypt(RAW_PASSWORD);
    assertNotNull(password);
    assertTrue(passwordSecurity.check(password, RAW_PASSWORD));
  }

}
