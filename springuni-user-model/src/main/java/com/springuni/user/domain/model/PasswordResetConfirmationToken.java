package com.springuni.user.domain.model;

import static com.springuni.user.domain.model.ConfirmationTokenType.PASSWORD_RESET;

import lombok.NoArgsConstructor;

/**
 * Password reset confirmation token.
 */
@NoArgsConstructor
public class PasswordResetConfirmationToken extends ConfirmationToken {

  private static final int TOKEN_EXPIRATION_PERIOD_MINUTES = 10;

  /**
   * Creates a password reset confirmation token.
   *
   * @param owner a User
   */
  public PasswordResetConfirmationToken(User owner) {
    super(owner, TOKEN_EXPIRATION_PERIOD_MINUTES);
  }

  @Override
  public ConfirmationTokenType getConfirmationTokenType() {
    return PASSWORD_RESET;
  }

}
