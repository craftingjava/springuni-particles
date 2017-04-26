package com.springuni.user.domain.service;

import com.springuni.user.domain.model.Password;

/**
 * A functional interface for checking encrypted passwords against a cleartext password.
 */
@FunctionalInterface
public interface PasswordChecker {

  /**
   * Checks the given {@code password} against the given {@code rawPassword}
   *
   * @param password Encrypted password
   * @param rawPassword Cleartext password
   * @return true if matches, false otherwise
   */
  boolean check(Password password, String rawPassword);

}
