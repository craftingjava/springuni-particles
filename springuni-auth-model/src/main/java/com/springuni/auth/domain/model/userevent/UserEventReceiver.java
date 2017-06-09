package com.springuni.auth.domain.model.userevent;

import static com.springuni.auth.domain.model.user.ConfirmationTokenType.EMAIL;

import com.springuni.auth.domain.model.user.User;
import com.springuni.auth.domain.service.UserService;
import com.springuni.commons.domain.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcsontos on 6/9/17.
 */
public class UserEventReceiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserEventReceiver.class);

  private final UserService userService;

  public UserEventReceiver(UserService userService) {
    this.userService = userService;
  }

  public void receive(UserEvent<?> userEvent) {
    UserEventType userEventType = userEvent.getUserEventType();
    Long userId = userEvent.getUserId();

    try {
      LOGGER.warn("Handling event {} for user #{}.", userEventType, userId);

      switch (userEventType) {
        case SIGNUP_REQUESTED:
          handleSignupRequested(userId);
          break;
        default:
          LOGGER.warn("Event {} hasn't been implemented yet.", userEventType);
      }
    } catch (Exception e) {
      LOGGER.warn(new StringBuilder("Couldn't handle event ")
              .append(userEventType)
              .append(" for user #")
              .append(userEvent.getUserId())
              .append("; reason: ")
              .append(e.getMessage())
              .toString(), e);
    }
  }

  protected void handleSignupRequested(Long userId) throws ApplicationException {
    User user = userService.getUser(userId);
    user.addConfirmationToken(EMAIL);
    userService.store(user);
  }

}
