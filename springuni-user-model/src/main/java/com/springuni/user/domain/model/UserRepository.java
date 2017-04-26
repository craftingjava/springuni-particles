package com.springuni.user.domain.model;

import java.util.Optional;

/**
 * Created by lcsontos on 4/24/17.
 */
public interface UserRepository {

  /**
   * Deletes the given user, provided that it exists.
   *
   * @param userId {@link User}'s ID
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   */
  void delete(Long userId);

  /**
   * Finds a user based on its ID.
   *
   * @param id ID
   * @return a {@link User}
   */
  Optional<User> findById(Long id);

  /**
   * Finds a user by email address.
   *
   * @param email Email address
   * @return a {@link User}
   */
  Optional<User> findByEmail(String email);

  /**
   * Finds a user by screen name.
   * @param screenName Screen name
   * @return a {@link User}
   */
  Optional<User> findByScreenName(String screenName);

  /**
   * Stores the given user.
   * @param user a {@link User}
   * @return the stored {@link User}
   */
  User save(User user);

}
