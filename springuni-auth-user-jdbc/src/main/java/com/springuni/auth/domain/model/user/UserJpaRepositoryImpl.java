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

import com.springuni.auth.domain.model.user.exceptions.NoSuchUserException;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA-based implementation of {@link UserRepository}.
 */
@Transactional
@Repository
public class UserJpaRepositoryImpl implements UserRepository {

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
    return Optional.ofNullable(entityManager.find(User.class, userId));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    TypedQuery<User> findByEmailQuery =
        entityManager.createNamedQuery("findByEmailQuery", User.class);
    findByEmailQuery.setParameter("email", email);
    return doFindUser(findByEmailQuery);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByScreenName(String screenName) {
    TypedQuery<User> findByScreenNameQuery =
        entityManager.createNamedQuery("findByScreenNameQuery", User.class);
    findByScreenNameQuery.setParameter("screenName", screenName);
    return doFindUser(findByScreenNameQuery);
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

  Optional<User> doFindUser(TypedQuery<User> userQuery) {
    try {
      return Optional.of(userQuery.getSingleResult());
    } catch (NoResultException nre) {
      return Optional.empty();
    }
  }

}
