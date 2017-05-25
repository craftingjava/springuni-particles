package com.springuni.auth.domain.service;

import com.springuni.auth.crypto.PasswordChecker;
import com.springuni.auth.crypto.PasswordEncryptor;
import com.springuni.auth.domain.model.session.SessionRepository;
import com.springuni.auth.domain.model.user.Password;
import com.springuni.auth.domain.model.user.UserRepository;
import com.springuni.auth.domain.model.userevent.UserEventEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lcsontos on 5/18/17.
 */
@Configuration
public class AuthServiceConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceConfiguration.class);

  @Bean
  public UserEventEmitter userEventEmitter() {
    // TODO: replace these with the actual implementation
    return userEvent -> LOGGER.info("Captured event {},", userEvent);
  }

  @Bean
  public UserService userService(UserRepository userRepository, UserEventEmitter userEventEmitter) {
    // TODO: replace these with the actual implementations
    PasswordChecker passwordChecker = (password, rawPassword) -> true;
    PasswordEncryptor passwordEncryptor = rawPassword -> new Password(rawPassword, "");

    return new UserServiceImpl(
        passwordChecker, passwordEncryptor, userEventEmitter, userRepository);
  }

  @Bean
  public SessionService sessionService(
      SessionRepository sessionRepository, UserEventEmitter userEventEmitter) {

    return new SessionServiceImpl(sessionRepository, userEventEmitter);
  }

}
