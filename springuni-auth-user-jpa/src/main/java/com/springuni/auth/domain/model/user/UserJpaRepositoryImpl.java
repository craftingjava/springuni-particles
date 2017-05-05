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

package com.springuni.auth.domain.model.user;

import static com.springuni.commons.util.Maps.entriesToMap;
import static com.springuni.commons.util.Maps.entry;

import com.springuni.auth.domain.model.user.exceptions.NoSuchUserException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA-based implementation of {@link UserRepository}.
 */
@Transactional
@Repository
public class UserJpaRepositoryImpl implements UserRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserJpaRepositoryImpl.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void delete(Long userId) throws NoSuchUserException {
    Optional<User> userOptional = findById(userId);
    User user = userOptional.orElseThrow(NoSuchUserException::new);
    user.setDeleted(true);
    entityManager.merge(user);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findById(Long userId) {
    Map parameters = Stream.of(entry("userId", userId)).collect(entriesToMap());
    return doFindUser("findByIdQuery", parameters);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    Map parameters = Stream.of(entry("email", email)).collect(entriesToMap());
    return doFindUser("findByEmailQuery", parameters);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByScreenName(String screenName) {
    Map parameters = Stream.of(entry("screenName", screenName)).collect(entriesToMap());
    return doFindUser("findByScreenNameQuery", parameters);
  }

  @Override
  public User save(User user) {
    if (user.isNew()) {
      entityManager.persist(user);
    } else {
      user = entityManager.merge(user);
    }
    return user;
  }

  Optional<User> doFindUser(String queryName, Map<String, ?> parameters) {
    TypedQuery<User> userQuery = entityManager.createNamedQuery(queryName, User.class);
    parameters.forEach(userQuery::setParameter);
    try {
      return Optional.of(userQuery.getSingleResult());
    } catch (NoResultException nre) {
      LOGGER.debug(nre.getMessage(), nre);
      return Optional.empty();
    }
  }

}
