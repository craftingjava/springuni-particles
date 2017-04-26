package com.springuni.user.domain.service;

import com.springuni.user.domain.model.Password;

/**
 * A functional interface for encrypting cleartext passwords.
 */
@FunctionalInterface
public interface PasswordEncryptor {

  /**
   * Encrypts the given {@code rawPassword}.
   *
   * @param rawPassword Cleartext password
   * @return encrypted password
   */
  Password ecrypt(String rawPassword);

}
