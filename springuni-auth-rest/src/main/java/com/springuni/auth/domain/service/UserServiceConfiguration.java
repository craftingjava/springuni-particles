package com.springuni.auth.domain.service;

import com.springuni.auth.crypto.PasswordChecker;
import com.springuni.auth.crypto.PasswordEncryptor;
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
public class UserServiceConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceConfiguration.class);

  @Bean
  public UserService userService(UserRepository userRepository) {
    // TODO: replace these with the actual implementations
    PasswordChecker passwordChecker = (password, rawPassword) -> true;
    PasswordEncryptor passwordEncryptor = rawPassword -> new Password(rawPassword, "");
    UserEventEmitter userEventEmitter = userEvent -> LOGGER.info("Captured event {},", userEvent);

    return new UserServiceImpl(
        passwordChecker, passwordEncryptor, userEventEmitter, userRepository);
  }

}
