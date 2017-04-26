package com.springuni.user.domain.model;

import com.springuni.commons.domain.AuditData;
import com.springuni.commons.domain.Entity;
import com.springuni.contact.domain.model.ContactData;
import com.springuni.user.domain.model.exceptions.InvalidConfirmationTokenException;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class for users.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = "screenName")
public class User implements Entity<Long, User> {

  private Long id;
  private String screenName;

  private ContactData contactData = new ContactData();

  private Password password;

  private Set<String> authorities = new LinkedHashSet<>();

  private Timezone timezone = Timezone.AMERICA_LOS_ANGELES;
  private Locale locale = Locale.US;

  private boolean confirmed;
  private boolean locked;
  private boolean deleted;

  private AuditData<User> auditData;

  private Set<ConfirmationToken> confirmationTokens = new LinkedHashSet<>();

  public User(Long id, String screenName, String email) {
    this.id = id;
    this.screenName = screenName;
    contactData.setEmail(email);
  }

  public ConfirmationToken addConfirmationToken(ConfirmationTokenType type) {
    return addConfirmationToken(type, 0);
  }

  public ConfirmationToken addConfirmationToken(ConfirmationTokenType type, int minutes) {
    if (minutes == 0) {
      minutes = ConfirmationToken.DEFAULT_EXPIRATION_MINUTES;
    }
    ConfirmationToken confirmationToken = new ConfirmationToken(this, type, minutes);
    confirmationTokens.add(confirmationToken);
    return confirmationToken;
  }

  public Optional<ConfirmationToken> getConfirmationToken(String token) {
    return confirmationTokens.stream()
        .filter(ct -> token.equals(ct.getValue()))
        .findFirst();
  }

  public String getEmail() {
    return contactData.getEmail();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public boolean sameIdentityAs(User other) {
    return equals(other);
  }

  public void setEmail(String email) {
    contactData.setEmail(email);
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public ConfirmationToken useConfirmationToken(String token)
      throws InvalidConfirmationTokenException {

    Optional<ConfirmationToken> confirmationTokenHolder = getConfirmationToken(token);
    if (!confirmationTokenHolder.isPresent()) {
      throw new InvalidConfirmationTokenException();
    }

    ConfirmationToken confirmationToken = confirmationTokenHolder.get();
    if (!confirmationToken.isValid()) {
      throw new InvalidConfirmationTokenException();
    }

    return confirmationToken.use();
  }

}
