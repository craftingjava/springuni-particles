package com.springuni.user.domain.model;

import static com.springuni.commons.util.DateTimeUtil.expireNowUtc;
import static java.time.temporal.ChronoUnit.MINUTES;

import com.springuni.commons.domain.AuditData;
import com.springuni.commons.domain.Entity;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is a general template for adding various kinds of {@link ConfirmationToken}s to the systems.
 *
 * @see {@link EmailChangeConfirmationToken}
 * @see {@link PasswordResetConfirmationToken}
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public abstract class ConfirmationToken<C extends ConfirmationToken<C>>
    implements Entity<Long, C> {

  private Long id;
  private User owner;
  private String value;

  private boolean valid = true;
  private LocalDateTime expiresAt;

  private AuditData<User> auditData;

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param minutes expiration in minutes
   */
  public ConfirmationToken(User owner, int minutes) {
    this.owner = owner;
    expiresAt = expireNowUtc(minutes, MINUTES);
  }

  /**
   * Returns the type of this token; should be implement by sub-classes.
   *
   * @return type of this token
   */
  public abstract ConfirmationTokenType getConfirmationTokenType();

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean sameIdentityAs(C other) {
    return equals(other);
  }

}
