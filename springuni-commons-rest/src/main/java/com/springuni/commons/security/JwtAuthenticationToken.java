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

package com.springuni.commons.security;

import io.jsonwebtoken.Claims;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

/**
 * Created by lcsontos on 5/17/17.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private static final String AUTHORITIES = "authorities";

  private final long userId;

  private JwtAuthenticationToken(long userId, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.userId = userId;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Long getPrincipal() {
    return userId;
  }

  /**
   * Factory method for creating a new {@code {@link JwtAuthenticationToken}}.
   * @param claims JWT claims
   * @return a JwtAuthenticationToken
   */
  public static JwtAuthenticationToken of(Claims claims) {
    long userId = Long.valueOf(claims.getSubject());

    Collection<GrantedAuthority> authorities =
        Arrays.stream(String.valueOf(claims.get(AUTHORITIES)).split(","))
            .map(String::trim)
            .filter(StringUtils::hasText)
            .map(String::toUpperCase)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

    JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userId, authorities);

    Date now = new Date();
    Date expiration = claims.getExpiration();
    Date notBefore = Optional.ofNullable(claims.getNotBefore()).orElse(now);
    jwtAuthenticationToken.setAuthenticated(now.after(notBefore) && now.before(expiration));

    return jwtAuthenticationToken;
  }

}
