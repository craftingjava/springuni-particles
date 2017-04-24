package com.springuni.user.domain.model;

/**
 * Created by lcsontos on 4/24/17.
 */
public interface UserRepository {

  /**
   * Finds a user based on its ID.
   *
   * @param id ID
   * @return a {@link User}
   */
  User find(Long id);

  /**
   * Finds a user either by email address or screen name.
   * @param emailOrScreenName Email address or screen name
   * @return a {@link User}
   */
  User find(String emailOrScreenName);

  /**
   * Stores the given user.
   * @param user a {@link User}
   * @return the stored {@link User}
   */
  User save(User user);

}
