package com.springuni.user.domain.service;

import static com.springuni.user.domain.model.ConfirmationTokenType.PASSWORD_RESET;
import static com.springuni.user.domain.model.UserEventType.DELETED;
import static com.springuni.user.domain.model.UserEventType.EMAIL_CHANGED;
import static com.springuni.user.domain.model.UserEventType.EMAIL_CHANGE_REQUESTED;
import static com.springuni.user.domain.model.UserEventType.EMAIL_CONFIRMED;
import static com.springuni.user.domain.model.UserEventType.PASSWORD_CHANGED;
import static com.springuni.user.domain.model.UserEventType.PASSWORD_RESET_CONFIRMED;
import static com.springuni.user.domain.model.UserEventType.PASSWORD_RESET_REQUESTED;
import static com.springuni.user.domain.model.UserEventType.SCREEN_NAME_CHANGED;
import static com.springuni.user.domain.model.UserEventType.SIGNIN_FAILED;
import static com.springuni.user.domain.model.UserEventType.SIGNIN_SUCCEEDED;
import static com.springuni.user.domain.model.UserEventType.SIGNUP_REQUESTED;

import com.springuni.commons.util.IdentityGenerator;
import com.springuni.commons.util.Validator;
import com.springuni.user.domain.model.ConfirmationToken;
import com.springuni.user.domain.model.Password;
import com.springuni.user.domain.model.User;
import com.springuni.user.domain.model.UserEvent;
import com.springuni.user.domain.model.UserRepository;
import com.springuni.user.domain.model.exceptions.EmailIsAlreadyTakenException;
import com.springuni.user.domain.model.exceptions.InvalidConfirmationTokenException;
import com.springuni.user.domain.model.exceptions.InvalidEmailException;
import com.springuni.user.domain.model.exceptions.NoSuchUserException;
import com.springuni.user.domain.model.exceptions.ScreenNameIsAlreadyTakenException;
import com.springuni.user.domain.model.exceptions.UnconfirmedUserException;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcsontos on 4/24/17.
 */
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private static final int NEXT_SCREEN_NAME_MAX_TRIES = 20;

  private final PasswordChecker passwordChecker;
  private final PasswordEncryptor passwordEncryptor;

  private final UserEventEmitter userEventEmitter;
  private final UserRepository userRepository;

  public UserServiceImpl(
      PasswordChecker passwordChecker, PasswordEncryptor passwordEncryptor,
      UserEventEmitter userEventEmitter, UserRepository userRepository) {

    this.passwordChecker = passwordChecker;
    this.passwordEncryptor = passwordEncryptor;
    this.userEventEmitter = userEventEmitter;
    this.userRepository = userRepository;
  }

  @Override
  public User changeEmail(Long userId, String newEmail)
      throws EmailIsAlreadyTakenException, InvalidEmailException, NoSuchUserException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(newEmail, "newEmail");

    User user = getUser(userId);
    checkEmail(user, newEmail);
    if (newEmail.equals(user.getEmail())) {
      return user;
    }

    user.setEmail(newEmail);
    user = store(user);

    userEventEmitter.emit(new UserEvent(userId, EMAIL_CHANGED));

    return user;
  }

  @Override
  public User changePassword(Long userId, String rawPassword) throws NoSuchUserException {
    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(rawPassword, "rawPassword");

    User user = getUser(userId);
    Password newPassword = passwordEncryptor.ecrypt(rawPassword);
    user.setPassword(newPassword);
    user = store(user);

    userEventEmitter.emit(new UserEvent(userId, PASSWORD_CHANGED));

    return user;
  }

  @Override
  public User changeScreenName(Long userId, String newScreenName)
      throws NoSuchUserException, ScreenNameIsAlreadyTakenException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(newScreenName, "newScreenName");

    User user = getUser(userId);
    checkScreenName(user, newScreenName);
    user.setScreenName(newScreenName);
    user = store(user);

    userEventEmitter.emit(new UserEvent(userId, SCREEN_NAME_CHANGED));

    return user;
  }

  @Override
  public User confirmEmail(Long userId, String token)
      throws InvalidConfirmationTokenException, NoSuchUserException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(token, "token");

    User user = getUser(userId);

    ConfirmationToken<String> confirmationToken = user.useConfirmationToken(token);

    Optional<String> newEmail = confirmationToken.getPayload();
    if (!newEmail.isPresent()) {
      boolean confirmed = user.isConfirmed();
      user.setConfirmed(true);
      user = store(user);
      if (!confirmed) {
        userEventEmitter.emit(new UserEvent(userId, EMAIL_CONFIRMED));
      }
    } else {
      try {
        user = changeEmail(userId, newEmail.get());
      } catch (EmailIsAlreadyTakenException | InvalidEmailException e) {
        LOGGER.warn(e.getMessage(), e);
      }
    }

    return user;
  }

  @Override
  public User confirmPasswordReset(Long userId, String token)
      throws InvalidConfirmationTokenException, NoSuchUserException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(token, "token");

    User user = getUser(userId);
    user.useConfirmationToken(token);
    user = store(user);

    userEventEmitter.emit(new UserEvent(userId, PASSWORD_RESET_CONFIRMED));

    return user;
  }

  @Override
  public void delete(Long userId) {
    userRepository.delete(userId);
    userEventEmitter.emit(new UserEvent(userId, DELETED));
  }

  @Override
  public Optional<User> findUser(Long userId) {
    Objects.requireNonNull(userId);
    return userRepository.findById(userId);
  }

  @Override
  public Optional<User> findUser(String emailOrScreenName) {
    Objects.requireNonNull(emailOrScreenName);
    Optional<User> user = null;
    if (Validator.isEmail(emailOrScreenName)) {
      user = userRepository.findByEmail(emailOrScreenName);
    } else {
      user = userRepository.findByScreenName(emailOrScreenName);
    }
    return user;
  }

  @Override
  public User getUser(Long userId) throws NoSuchUserException {
    Optional<User> user = findUser(userId);
    return user.orElseThrow(NoSuchUserException::new);
  }

  @Override
  public User getUser(String emailOrScreenName) throws NoSuchUserException {
    Optional<User> user = findUser(emailOrScreenName);
    return user.orElseThrow(NoSuchUserException::new);
  }

  @Override
  public boolean isEmailTaken(String email) {
    Objects.requireNonNull(email);
    Optional<User> user = userRepository.findByEmail(email);
    return user.isPresent();
  }

  @Override
  public boolean isScreenNameTaken(String screenName) {
    Objects.requireNonNull(screenName);
    Optional<User> user = userRepository.findByScreenName(screenName);
    return user.isPresent();
  }

  @Override
  public User login(String emailOrScreenName, String rawPassword)
      throws NoSuchUserException, UnconfirmedUserException {

    Objects.requireNonNull(emailOrScreenName, "emailOrScreenName");
    Objects.requireNonNull(rawPassword, "rawPassword");

    User user = getUser(emailOrScreenName);

    if (!user.isConfirmed()) {
      throw new UnconfirmedUserException();
    }

    if (passwordChecker.check(user.getPassword(), rawPassword)) {
      userEventEmitter.emit(new UserEvent(user.getId(), SIGNIN_SUCCEEDED));
      return user;
    }

    userEventEmitter.emit(new UserEvent(user.getId(), SIGNIN_FAILED));
    throw new NoSuchUserException();
  }

  @Override
  public String nextScreenName(String email) throws InvalidEmailException {
    Objects.requireNonNull(email);
    if (!Validator.isEmail(email)) {
      throw new InvalidEmailException();
    }

    String screenName = email.split("@")[0];

    int index = 1;
    String possibleScreenName = screenName;
    while (isScreenNameTaken(possibleScreenName) && index < NEXT_SCREEN_NAME_MAX_TRIES) {
      possibleScreenName = screenName + (index++);
    }

    if (index < NEXT_SCREEN_NAME_MAX_TRIES) {
      return possibleScreenName;
    }

    if (!isScreenNameTaken(possibleScreenName)) {
      return possibleScreenName;
    } else {
      return screenName + IdentityGenerator.generate();
    }
  }

  @Override
  public void requestEmailChange(Long userId, String newEmail)
      throws NoSuchUserException, EmailIsAlreadyTakenException, InvalidEmailException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(newEmail, "newEmail");

    User user = getUser(userId);
    checkEmail(user, newEmail);
    if (newEmail.equals(user.getEmail())) {
      return;
    }

    ConfirmationToken confirmationToken = user.addConfirmationToken(PASSWORD_RESET);
    store(user);

    userEventEmitter.emit(new UserEvent<>(userId, EMAIL_CHANGE_REQUESTED, confirmationToken));
  }

  @Override
  public void requestPasswordReset(Long userId) throws NoSuchUserException {
    Objects.requireNonNull(userId, "userId");

    User user = getUser(userId);
    ConfirmationToken confirmationToken = user.addConfirmationToken(PASSWORD_RESET);
    store(user);

    userEventEmitter.emit(new UserEvent<>(userId, PASSWORD_RESET_REQUESTED, confirmationToken));
  }

  @Override
  public void signup(User user, String rawPassword) throws InvalidEmailException {
    Objects.requireNonNull(user, "user");
    Objects.requireNonNull(rawPassword, "rawPassword");

    if (!Validator.isEmail(user.getEmail())) {
      throw new InvalidEmailException();
    }

    Password password = passwordEncryptor.ecrypt(rawPassword);
    user.setPassword(password);
    user = store(user);

    userEventEmitter.emit(new UserEvent(user.getId(), SIGNUP_REQUESTED));
  }

  @Override
  public User store(User user) {
    return userRepository.save(user);
  }

  private void checkEmail(User user, String newEmail)
      throws EmailIsAlreadyTakenException, InvalidEmailException {

    if (!Validator.isEmail(newEmail)) {
      throw new InvalidEmailException();
    }

    Optional<User> otherUser = findUser(newEmail);
    if (otherUser.isPresent() && !user.equals(otherUser.get())) {
      throw new EmailIsAlreadyTakenException();
    }
  }

  private void checkScreenName(User user, String newScreenName)
      throws ScreenNameIsAlreadyTakenException {

    Optional<User> otherUser = findUser(newScreenName);
    if (otherUser.isPresent() && !user.equals(otherUser.get())) {
      throw new ScreenNameIsAlreadyTakenException();
    }
  }

}
