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

import static com.springuni.auth.domain.model.userevent.UserEventType.LOGGED_OUT;
import static com.springuni.auth.domain.model.userevent.UserEventType.SIGNIN_REMEMBER_ME;
import static java.util.stream.Collectors.toList;

import com.springuni.auth.domain.model.session.Session;
import com.springuni.auth.domain.model.session.SessionRepository;
import com.springuni.auth.domain.model.session.exceptions.NoSuchSessionException;
import com.springuni.auth.domain.model.userevent.UserEvent;
import com.springuni.auth.domain.model.userevent.UserEventEmitter;
import com.springuni.commons.util.IdentityGenerator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Framework agnostic implementation of {@link UserService}.
 */
public class SessionServiceImpl implements SessionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

  private final SessionRepository sessionRepository;
  private final UserEventEmitter userEventEmitter;

  public SessionServiceImpl(
      SessionRepository sessionRepository, UserEventEmitter userEventEmitter) {

    this.sessionRepository = sessionRepository;
    this.userEventEmitter = userEventEmitter;
  }

  @Override
  public Session createSession(long userId, String token) {
    return createSession(userId, token, 0);
  }

  @Override
  public Session createSession(long userId, String token, int minutes) {
    Objects.requireNonNull(userId);
    Long id = IdentityGenerator.generate();
    Session session = new Session(id, userId, minutes);
    sessionRepository.save(session);

    LOGGER.info(
        "Created persistent session {} for user {}.", session.getId(), session.getUserId());

    return session;
  }

  @Override
  public Optional<Session> findSession(Long id) {
    Objects.requireNonNull(id);
    return sessionRepository.findById(id).map(session -> (session.isValid() ? session : null));
  }

  @Override
  public Session getSession(Long id) throws NoSuchSessionException {
    return findSession(id).orElseThrow(NoSuchSessionException::new);
  }

  @Override
  public void logoutUser(Long userId) {
    List<Long> deletedSessionIds = sessionRepository.findByUserId(userId)
        .stream()
        .peek(session -> session.setDeleted(true))
        .peek(sessionRepository::save)
        .map(Session::getId)
        .collect(toList());

    LOGGER.info("Sessions {} of user {} were deleted.", deletedSessionIds, userId);

    userEventEmitter.emit(new UserEvent(userId, LOGGED_OUT));
  }

  @Override
  public void useSession(Long id, String value, LocalDateTime lastUsedAt)
      throws NoSuchSessionException {

    Session session = getSession(id);
    session.setValue(value);
    session.setLastUsedAt(lastUsedAt);

    sessionRepository.save(session);

    LOGGER.info(
        "Auto login with persistent session {} for user {}.", session.getId(), session.getUserId());

    userEventEmitter.emit(new UserEvent(session.getUserId(), SIGNIN_REMEMBER_ME));
  }

}
