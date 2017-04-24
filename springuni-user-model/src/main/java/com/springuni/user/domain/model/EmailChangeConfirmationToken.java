package com.springuni.user.domain.model;

import static com.springuni.user.domain.model.ConfirmationTokenType.EMAIL;

import java.util.Optional;
import lombok.NoArgsConstructor;

/**
 * Email address confirmation token, optionally holding a new email address.
 */
@NoArgsConstructor
public class EmailChangeConfirmationToken extends ConfirmationToken {

  private static final int TOKEN_EXPIRATION_PERIOD_MINUTES = 24 * 60;

  private Optional<String> newEmail;

  /**
   * Creates a new email address confirmation token.
   *
   * @param owner a User
   */
  public EmailChangeConfirmationToken(User owner) {
    this(owner, null);
  }

  /**
   * Creates a new email address confirmation token.
   *
   * @param owner a User
   * @param newEmail new email address
   */
  public EmailChangeConfirmationToken(User owner, String newEmail) {
    super(owner, TOKEN_EXPIRATION_PERIOD_MINUTES);
    this.newEmail = Optional.ofNullable(newEmail);
  }

  @Override
  public ConfirmationTokenType getConfirmationTokenType() {
    return EMAIL;
  }

}
