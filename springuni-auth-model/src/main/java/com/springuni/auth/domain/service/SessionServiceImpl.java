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

package com.springuni.auth.domain.service;

import com.springuni.auth.domain.model.session.Session;
import com.springuni.auth.domain.model.session.SessionRepository;
import com.springuni.auth.domain.model.session.exceptions.NoSuchSessionException;
import com.springuni.auth.domain.model.user.User;
import com.springuni.commons.util.IdentityGenerator;
import java.util.Objects;
import java.util.Optional;

/**
 * Framework agnostic implementation of {@link UserService}.
 */
public class SessionServiceImpl implements SessionService {

  private final SessionRepository sessionRepository;

  public SessionServiceImpl(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  @Override
  public Session getSession(Long sessionId) throws NoSuchSessionException {
    Objects.requireNonNull(sessionId);
    Optional<Session> session = sessionRepository.findById(sessionId);
    return session.orElseThrow(NoSuchSessionException::new);
  }

  @Override
  public Session login(User user) {
    return login(user, 0);
  }

  @Override
  public Session login(User user, int minutes) {
    Objects.requireNonNull(user);
    Long sessionId = IdentityGenerator.generate();
    Session session = new Session(sessionId, user.getId(), minutes);
    sessionRepository.save(session);
    return session;
  }

  @Override
  public void logout(Long sessionId) throws NoSuchSessionException {
    sessionRepository.delete(sessionId);
  }

}
