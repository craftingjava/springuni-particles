/**
 * Copyright (c) 2017-present Laszlo Csontos All rights reserved.
 *
 * This file is part of springuni-particles.
 *
 * springuni-particles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * springuni-particles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with springuni-particles.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.springuni.user.domain.model;

import com.springuni.commons.domain.AuditData;
import com.springuni.commons.domain.Entity;
import com.springuni.crm.domain.contact.ContactData;
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

  /**
   * Creates a {@link User} instance.
   *
   * @param id ID
   * @param screenName screen name
   * @param email email
   */
  public User(Long id, String screenName, String email) {
    this.id = id;
    this.screenName = screenName;
    contactData.setEmail(email);
  }

  /**
   * Add a confirmation token to the user and invalidates all other confirmation tokens of the same
   * type.
   *
   * @param type Token type
   * @return the newly added confirmation token
   */
  public ConfirmationToken addConfirmationToken(ConfirmationTokenType type) {
    return addConfirmationToken(type, 0);
  }


  /**
   * Add a confirmation token to the user and invalidates all other confirmation tokens of the same
   * type.
   *
   * @param type token type
   * @param minutes token's expiration period in minutes
   * @return the newly added confirmation token
   */
  public ConfirmationToken addConfirmationToken(ConfirmationTokenType type, int minutes) {
    if (minutes == 0) {
      minutes = ConfirmationToken.DEFAULT_EXPIRATION_MINUTES;
    }
    // TODO: invalide all other confirmation tokens.
    ConfirmationToken confirmationToken = new ConfirmationToken(this, type, minutes);
    confirmationTokens.add(confirmationToken);
    return confirmationToken;
  }

  /**
   * Gets the confirmation token instance for the given token value, provided that it exists.
   *
   * @param token token's value.
   * @return a confirmation token
   */
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

  /**
   * Uses the given confirmation token if it exists and it's still valid.
   *
   * @param token token's value
   * @return the used token
   * @throws InvalidConfirmationTokenException if there was no such token or if it wasn't valid.
   */
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
