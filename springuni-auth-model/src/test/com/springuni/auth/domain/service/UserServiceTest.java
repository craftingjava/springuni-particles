package com.springuni.auth.domain.service;

import static com.springuni.auth.domain.model.user.ConfirmationTokenType.EMAIL;
import static com.springuni.auth.domain.model.user.ConfirmationTokenType.PASSWORD_RESET;
import static com.springuni.auth.domain.model.userevent.UserEventType.DELETED;
import static com.springuni.auth.domain.model.userevent.UserEventType.EMAIL_CHANGED;
import static com.springuni.auth.domain.model.userevent.UserEventType.EMAIL_CHANGE_REQUESTED;
import static com.springuni.auth.domain.model.userevent.UserEventType.PASSWORD_CHANGED;
import static com.springuni.auth.domain.model.userevent.UserEventType.PASSWORD_RESET_REQUESTED;
import static com.springuni.auth.domain.model.userevent.UserEventType.SCREEN_NAME_CHANGED;
import static com.springuni.auth.domain.model.userevent.UserEventType.SIGNIN_FAILED;
import static com.springuni.auth.domain.model.userevent.UserEventType.SIGNIN_SUCCEEDED;
import static com.springuni.auth.domain.model.userevent.UserEventType.SIGNUP_REQUESTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springuni.auth.crypto.PasswordSecurity;
import com.springuni.auth.domain.model.user.ConfirmationToken;
import com.springuni.auth.domain.model.user.Password;
import com.springuni.auth.domain.model.user.User;
import com.springuni.auth.domain.model.user.UserRepository;
import com.springuni.auth.domain.model.user.exceptions.EmailIsAlreadyTakenException;
import com.springuni.auth.domain.model.user.exceptions.InvalidConfirmationTokenException;
import com.springuni.auth.domain.model.user.exceptions.InvalidEmailException;
import com.springuni.auth.domain.model.user.exceptions.NoSuchUserException;
import com.springuni.auth.domain.model.user.exceptions.ScreenNameIsAlreadyTakenException;
import com.springuni.auth.domain.model.user.exceptions.UnconfirmedUserException;
import com.springuni.auth.domain.model.userevent.UserEvent;
import java.util.Optional;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by lcsontos on 4/24/17.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest extends BaseServiceTest {

  private static final Long NON_EXISTENT_USER_ID = 3L;
  private static final String NON_EXISTENT_USER_EMAIL = "non-existent@springuni.com";
  private static final String NON_EXISTENT_USER_SCREEN_NAME = "non-existent";

  @Mock private PasswordSecurity passwordSecurity;
  @Mock private UserRepository userRepository;

  private ConfirmationToken validEmailConfirmationToken;
  private ConfirmationToken validPasswordResetConfirmationToken;
  private ConfirmationToken invalidEmailConfirmationToken;
  private ConfirmationToken invalidPasswordResetConfirmationToken;
  private User user1;
  private User user2;
  private UserService userService;

  @Before
  public void before() throws Exception {
    user1 = new User(1L, "test1", "test1@springuni.com");
    validEmailConfirmationToken = user1.addConfirmationToken(EMAIL);
    validPasswordResetConfirmationToken = user1.addConfirmationToken(PASSWORD_RESET);

    user2 = new User(2L, "test2", "test2@springuni.com");
    invalidEmailConfirmationToken = user2.addConfirmationToken(EMAIL).use();
    invalidPasswordResetConfirmationToken = user2.addConfirmationToken(PASSWORD_RESET).use();

    userService = new UserServiceImpl(passwordSecurity, userEventEmitter, userRepository);

    when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
    when(userRepository.findByScreenName(user1.getScreenName())).thenReturn(Optional.of(user1));
    when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
    when(userRepository.save(user1)).thenReturn(user1);

    when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
    when(userRepository.findByScreenName(user2.getScreenName())).thenReturn(Optional.of(user2));
    when(userRepository.findByEmail(user2.getEmail())).thenReturn(Optional.of(user2));
    when(userRepository.save(user2)).thenReturn(user2);

    doThrow(NoSuchUserException.class).when(userRepository).delete(NON_EXISTENT_USER_ID);
    when(userRepository.findById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());
    when(userRepository.findByScreenName(NON_EXISTENT_USER_SCREEN_NAME)).thenReturn(Optional.empty());
    when(userRepository.findByEmail(NON_EXISTENT_USER_EMAIL)).thenReturn(Optional.empty());
  }

  @Test(expected = EmailIsAlreadyTakenException.class)
  public void testChangeEmail_withExisting() throws Exception {
    userService.changeEmail(user1.getId(), user2.getEmail());
  }

  @Test
  public void testChangeEmail_withNew() throws Exception {
    User changedUser = userService.changeEmail(user1.getId(), NON_EXISTENT_USER_EMAIL);
    Assert.assertNotNull(changedUser);
    assertEquals(NON_EXISTENT_USER_EMAIL, changedUser.getEmail());

    assertEmittedUserEvent(EMAIL_CHANGED);
  }

  @Test
  public void testChangePassword() throws Exception {
    ArgumentCaptor<String> rawPasswordCaptor =
        (ArgumentCaptor<String>) ArgumentCaptor.forClass(String.class);

    when(passwordSecurity.ecrypt(rawPasswordCaptor.capture()))
        .thenReturn(new Password("test", "test"));

    User changedUser = userService.changePassword(user1.getId(), "somepassword");
    Assert.assertNotNull(changedUser);
    Assert.assertNotNull(changedUser.getPassword());

    DefaultGroovyMethods
        .invokeMethod(Mockito.verify(passwordSecurity), "ecrypt", new Object[]{"somepassword"});

    assertEmittedUserEvent(PASSWORD_CHANGED);
  }

  @Test(expected = ScreenNameIsAlreadyTakenException.class)
  public void testChangeScreenName_withExisting() throws Exception {
    userService.changeScreenName(user1.getId(), user2.getScreenName());
  }

  @Test
  public void testChangeScreenName_withNew() throws Exception {
    User changedUser = userService.changeScreenName(user1.getId(), NON_EXISTENT_USER_SCREEN_NAME);
    Assert.assertNotNull(changedUser);
    assertEquals(NON_EXISTENT_USER_SCREEN_NAME, changedUser.getScreenName());

    assertEmittedUserEvent(SCREEN_NAME_CHANGED);
  }

  @Test(expected = InvalidConfirmationTokenException.class)
  public void testConfirmEmail_withInvalidToken() throws Exception {
    userService.confirmEmail(user1.getId(), "invalid");
  }

  @Test
  public void testConfirmEmail_withValidToken() throws Exception {
    User changedUser = userService
        .confirmEmail(user1.getId(), validEmailConfirmationToken.getValue());
    Assert.assertNotNull(changedUser);

    Optional<ConfirmationToken> confirmationToken = changedUser
        .getConfirmationToken(validEmailConfirmationToken.getValue());

    Assert.assertTrue(confirmationToken.isPresent());
    Assert.assertFalse(confirmationToken.get().isValid());
  }

  @Test(expected = InvalidConfirmationTokenException.class)
  public void testConfirmPasswordReset_withInvalidToken() throws Exception {
    userService.confirmPasswordReset(user1.getId(), "invalid");
  }

  @Test
  public void testConfirmPasswordReset_withValidToken() throws Exception {
    User changedUser = userService
        .confirmPasswordReset(user1.getId(), validPasswordResetConfirmationToken.getValue());
    Assert.assertNotNull(changedUser);

    Optional<ConfirmationToken> confirmationToken = changedUser
        .getConfirmationToken(validPasswordResetConfirmationToken.getValue());
    Assert.assertNotNull(confirmationToken.isPresent());
    Assert.assertFalse(confirmationToken.get().isValid());
  }

  @Test(expected = NoSuchUserException.class)
  public void testDelete_withNonExistent() throws Exception {
    userService.delete(NON_EXISTENT_USER_ID);
  }

  @Test
  public void testDelete_withExistent() throws Exception {
    userService.delete(user1.getId());
    verify(userRepository).delete(user1.getId());

    assertEmittedUserEvent(DELETED);
  }

  @Test
  public void testFindUser_withNonExistentUserId() {
    Optional<User> user = userService.findUser(NON_EXISTENT_USER_ID);
    Assert.assertFalse(user.isPresent());
  }

  @Test
  public void testFindUser_withExistentUserId() {
    Optional<User> user = userService.findUser(user1.getId());
    assertEquals(user1, user.get());

    user = userService.findUser(user2.getId());
    assertEquals(user2, user.get());
  }

  @Test
  public void testFindUser_withNonExistentEmailOrScreenName() {
    Optional<User> user = userService.findUser(NON_EXISTENT_USER_SCREEN_NAME);
    Assert.assertFalse(user.isPresent());
  }

  @Test
  public void testFindUser_withExistentEmailOrScreenName() {
    Optional<User> user = userService.findUser(user1.getEmail());
    assertEquals(user1, user.get());

    user = userService.findUser(user2.getEmail());
    assertEquals(user2, user.get());
  }

  @Test
  public void testIsEmailTaken_withNonExistentEmail() {
    Assert.assertFalse(userService.isEmailTaken(NON_EXISTENT_USER_EMAIL));
  }

  @Test
  public void testIsEmailTaken_withExistentEmail() {
    Assert.assertTrue(userService.isEmailTaken(user1.getEmail()));
    Assert.assertTrue(userService.isEmailTaken(user2.getEmail()));
  }

  @Test
  public void testIsEmailTaken_withNonExistentScreenName() {
    Assert.assertFalse(userService.isScreenNameTaken(NON_EXISTENT_USER_SCREEN_NAME));
  }

  @Test
  public void testIsEmailTaken_withExistentScreenName() {
    Assert.assertTrue(userService.isScreenNameTaken(user1.getScreenName()));
    Assert.assertTrue(userService.isScreenNameTaken(user2.getScreenName()));
  }

  @Test
  public void testLogin() throws Exception {
    when(passwordSecurity.check(isNull(), anyString())).thenReturn(true);
    user1.setConfirmed(true);
    userService.login(user1.getEmail(), "valid");

    assertEmittedUserEvent(SIGNIN_SUCCEEDED);
  }

  public void testLogin_withBadPassword() throws Exception {
    user1.setConfirmed(true);
    when(passwordSecurity.check(any(Password.class), anyString())).thenReturn(false);

    try {
      userService.login(user1.getEmail(), "invalid");
      Assert.fail("NoSuchUserException expected");
    } catch (NoSuchUserException nsue) {
      // Passed
    }

    assertEmittedUserEvent(SIGNIN_FAILED);
  }

  @Test(expected = NoSuchUserException.class)
  public void testLogin_withNonExistentUser() throws Exception {
    userService.login(NON_EXISTENT_USER_EMAIL, "invalid");
  }

  @Test(expected = UnconfirmedUserException.class)
  public void testLogin_withUnconfirmedUser() throws Exception {
    when(passwordSecurity.check(any(Password.class), anyString())).thenReturn(true);
    userService.login(user1.getEmail(), "valid");
  }

  @Test
  public void testNextScreenName() throws Exception {
    when(userRepository.findByScreenName("test"))
        .thenReturn(Optional.of(new User(3L, "test", "test@spriguni.com")));
    String nextScreenName = userService.nextScreenName("test@springuni.com");
    assertEquals("test3", nextScreenName);
    verify(userRepository, times(4)).findByScreenName(anyString());
  }

  @Test
  public void testRequestEmailChange() throws Exception {
    userService.requestEmailChange(user1.getId(), NON_EXISTENT_USER_EMAIL);

    User user = captureSavedUser();
    Assert.assertNotNull(user);
    UserEvent userEvent = captureEmittedUserEvent();
    Assert.assertNotNull(userEvent);

    assertThat(user.getConfirmationTokens(),
        CoreMatchers.hasItem((ConfirmationToken)userEvent.getPayload().get()));
    assertEquals(EMAIL_CHANGE_REQUESTED, userEvent.getUserEventType());
  }

  @Test
  public void testRequestPasswordReset() throws Exception {
    userService.requestPasswordReset(user1.getId());

    User user = captureSavedUser();
    Assert.assertNotNull(user);
    UserEvent userEvent = captureEmittedUserEvent();
    Assert.assertNotNull(userEvent);

    assertThat(user.getConfirmationTokens(),
        CoreMatchers.hasItem((ConfirmationToken)userEvent.getPayload().get()));
    assertEquals(PASSWORD_RESET_REQUESTED, userEvent.getUserEventType());
  }

  @Test(expected = EmailIsAlreadyTakenException.class)
  public void testSignup_withExistentEmail() throws Exception {
    User user3 = new User(3L, "test3", "test2@springuni.com");
    userService.signup(user3, "test");
  }

  @Test(expected = ScreenNameIsAlreadyTakenException.class)
  public void testSignup_withExistentScreenName() throws Exception {
    User user3 = new User(3L, "test2", "test3@springuni.com");
    userService.signup(user3, "test");
  }

  @Test(expected = InvalidEmailException.class)
  public void testSignup_withInvalidEmail() throws Exception {
    User user3 = new User(3L, "test2", "invalid");
    userService.signup(user3, "test");
  }

  @Test
  public void testSignup() throws Exception {
    User user3 = new User(3L, NON_EXISTENT_USER_SCREEN_NAME, NON_EXISTENT_USER_EMAIL);
    when(userRepository.save(user3)).thenReturn(user3);
    userService.signup(user3, "test");
    verify(passwordSecurity).ecrypt("test");
    assertEmittedUserEvent(SIGNUP_REQUESTED);
  }

  @Test
  public void testStore() {

  }

  private User captureSavedUser() {
    ArgumentCaptor<User> userCaptor = (ArgumentCaptor<User>) ArgumentCaptor.forClass(User.class);
    DefaultGroovyMethods
        .invokeMethod(Mockito.verify(userRepository), "save", new Object[]{userCaptor.capture()});
    return userCaptor.getValue();
  }

}
