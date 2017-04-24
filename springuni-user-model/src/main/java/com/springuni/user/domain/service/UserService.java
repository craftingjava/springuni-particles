package com.springuni.user.domain.service;

import com.springuni.user.domain.model.Session;
import com.springuni.user.domain.model.User;

/**
 * Created by lcsontos on 4/24/17.
 */
public interface UserService {

  /**
   * Changes the {@link User}'s email address, provided that {@code newEmail} is available.
   *
   * @param userId {@link User}'s ID
   * @param newEmail new email address
   * @return the modified {@link User}
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   * @throws com.springuni.user.domain.model.exceptions.EmailIsAlreadyTakenException if the given
   *     email is already taken
   */
  User changeEmail(Long userId, String newEmail);

  /**
   * Changes the {@link User}'s password.
   *
   * @param userId {@link User}'s ID
   * @param rawPassword new (cleartext) password
   * @return the modified {@link User}
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   */
  User changePassword(Long userId, String rawPassword);

  /**
   * Changes the {@link User}'s email address, provided that {@code newEmail} is available.
   *
   * @param userId {@link User}'s ID
   * @param screenName new screen name
   * @return the modified {@link User}
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   * @throws com.springuni.user.domain.model.exceptions.ScreenNameIsAlreadyTakenException if the
   *     given email is already taken
   */
  User changeScreenName(Long userId, String screenName);

  /**
   * Confirms the {@link User}'s email address with the given token, provided that it's valid.
   *
   * @param userId {@link User}'s ID
   * @param token confirmation token
   * @return the modified {@link User}
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   * @throws com.springuni.user.domain.model.exceptions.InvalidConfirmationTokenException if the
   *     given confirmation token is invalid
   */
  User confirmEmail(Long userId, String token);

  /**
   * Confirms the {@link User}'s previously requested password reset.
   *
   * @param userId {@link User}'s ID
   * @param token confirmation token
   * @return the modified {@link User}
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   * @throws com.springuni.user.domain.model.exceptions.InvalidConfirmationTokenException if the
   *     given confirmation token is invalid
   */
  User confirmPasswordReset(Long userId, String token);

  /**
   * Deletes the given {@link User}.
   *
   * @param userId {@link User}'s ID
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   */
  void delete(Long userId);

  /**
   * Checks if such a {@link User} exists in the system with the given email or screen name.
   *
   * @param emailOrScreenName Email or screen name
   * @return the {@link User}'s ID if exists
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   */
  Long exists(String emailOrScreenName);

  /**
   * Finds a {@link User} in the system by its ID.
   *
   * @param userId Email or screen name
   * @return the {@link User}'s ID if exists, null otherwise
   */
  User findUser(Long userId);

  /**
   * Finds a {@link User} in the system by its ID.
   *
   * @param emailOrScreenName Email or screen name
   * @return the {@link User}'s ID if exists, null otherwise
   */
  User findUser(String emailOrScreenName);

  /**
   * Returns the currently authenticated {@link User}.
   *
   * @return Authenticted {@link User}
   */
  User getAuthenticatedUser();

  /**
   * Checks if the given {@code email} is taken.
   *
   * @param email email to check
   * @return true if it's taken, false otherwise
   */
  boolean isEmailTaken(String email);

  /**
   * Checks if the given {@code screenName} is taken.
   *
   * @param screenName screenName to check
   * @return true if it's taken, false otherwise
   */
  boolean isScreenNameTaken(String screenName);

  /**
   * Returns the next available screen name based on the given email address.
   *
   * @param email Email
   * @return an available screen name.
   */
  String nextScreenName(String email);

  /**
   * Request email change for the given user.
   *
   * @param userId {@link User}'s ID
   * @param newEmail new email
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   */
  void requestEmailChange(Long userId, String newEmail);

  /**
   * Request password reset for the given {@link User}
   *
   * @param userId {@link User}'s ID
   * @throws com.springuni.user.domain.model.exceptions.NoSuchUserException if the user doesn't
   *     exist
   */
  void requestPasswordReset(Long userId);

  /**
   * Signs a user in with the given {@code emailOrScreenName} and {@code password}.
   *
   * @param emailOrScreenName Email or screen name
   * @param password password
   * @return the user's {@link Session}
   */
  Session signin(String emailOrScreenName, String password);

  /**
   * Signs the user's session out.
   *
   * @param sessionId Session ID
   * @throws com.springuni.user.domain.model.exceptions.NoSuchSessionException if the user's session
   *     doesn't exist or if it has already expired.
   */
  void signout(String sessionId);

  /**
   * Signs a user up.
   *
   * @param user a {@link User}
   */
  void signup(User user);

  /**
   * Stores the given {@link User}.
   *
   * @param user a {@link User} to store
   * @return the stored user
   */
  User store(User user);

}
