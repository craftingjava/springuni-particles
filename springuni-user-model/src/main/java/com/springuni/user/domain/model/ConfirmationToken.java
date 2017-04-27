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

import static com.springuni.commons.util.DateTimeUtil.expireNowUtc;
import static java.time.temporal.ChronoUnit.MINUTES;

import com.springuni.commons.domain.AuditData;
import com.springuni.commons.domain.Entity;
import com.springuni.commons.util.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Confirmation token functions as a one-time password for being able to perform operations like
 * email change and password reset.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "value")
@NoArgsConstructor
public class ConfirmationToken<P> implements Entity<Long, ConfirmationToken<P>> {

  public static final int DEFAULT_EXPIRATION_MINUTES = 10;

  private Long id;

  private String value;
  private User owner;
  private ConfirmationTokenType type;

  private boolean valid = true;
  private LocalDateTime expiresAt;
  private LocalDateTime usedAt;

  private P payload;

  private AuditData<User> auditData;

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param type confirmation token's type
   */
  public ConfirmationToken(User owner, ConfirmationTokenType type) {
    // FIXME: Use a bit more sophisticated random token value generaton later
    this(owner, type, DEFAULT_EXPIRATION_MINUTES);
  }

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param type confirmation token's type
   * @param minutes expiration in minutes
   */
  public ConfirmationToken(User owner, ConfirmationTokenType type, int minutes) {
    // FIXME: Use a bit more sophisticated random token value generaton later
    this(owner, UUID.randomUUID().toString(), type, minutes);
  }

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param value tokens's value
   * @param type confirmation token's type
   */
  public ConfirmationToken(User owner, String value, ConfirmationTokenType type) {
    this(owner, value, type, DEFAULT_EXPIRATION_MINUTES, null);
  }

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param value tokens's value
   * @param type confirmation token's type
   * @param minutes expiration in minutes
   */
  public ConfirmationToken(User owner, String value, ConfirmationTokenType type, int minutes) {
    this(owner, value, type, minutes, null);
  }

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param value tokens's value
   * @param type confirmation token's type
   * @param minutes expiration in minutes
   */
  public ConfirmationToken(
      User owner, String value, ConfirmationTokenType type, int minutes, P payload) {

    this.value = value;
    this.owner = owner;
    this.type = type;
    this.payload = payload;

    expiresAt = expireNowUtc(minutes, MINUTES);
  }

  @Override
  public Long getId() {
    return id;
  }

  /**
   * Gets the token's payload if any.
   *
   * @return token's payload.
   */
  public Optional<P> getPayload() {
    return Optional.ofNullable(payload);
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean sameIdentityAs(ConfirmationToken<P> other) {
    return equals(other);
  }

  /**
   * Use the confirmation token.
   *
   * @return this confirmation token.
   */
  public ConfirmationToken use() {
    valid = false;
    usedAt = DateTimeUtil.nowUtc();
    return this;
  }

}
