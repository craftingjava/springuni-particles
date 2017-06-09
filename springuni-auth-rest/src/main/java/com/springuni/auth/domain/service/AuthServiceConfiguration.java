package com.springuni.auth.domain.service;

import com.springuni.auth.crypto.PasswordSecurity;
import com.springuni.auth.crypto.Pbkdf2PasswordSecurityImpl;
import com.springuni.auth.domain.model.session.SessionRepository;
import com.springuni.auth.domain.model.user.UserRepository;
import com.springuni.auth.domain.model.userevent.UserEventEmitter;
import com.springuni.auth.domain.model.userevent.UserEventListener;
import com.springuni.auth.domain.model.userevent.UserEventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lcsontos on 5/18/17.
 */
@Configuration
public class AuthServiceConfiguration implements ApplicationEventPublisherAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceConfiguration.class);

  private ApplicationEventPublisher applicationEventPublisher;

  @Bean
  public UserEventListener userEventListener(UserService userService) {
    UserEventReceiver userEventReceiver = new UserEventReceiver(userService);
    return new UserEventListener(userEventReceiver);
  }

  @Bean
  public UserEventEmitter userEventEmitter() {
    return userEvent -> {
      applicationEventPublisher.publishEvent(userEvent);
      LOGGER.info("Published event {}.", userEvent);
    };
  }

  @Bean
  public UserService userService(UserRepository userRepository, UserEventEmitter userEventEmitter) {
    PasswordSecurity passwordSecurity = new Pbkdf2PasswordSecurityImpl();
    return new UserServiceImpl(passwordSecurity, userEventEmitter, userRepository);
  }

  @Bean
  public SessionService sessionService(
      SessionRepository sessionRepository, UserEventEmitter userEventEmitter) {

    return new SessionServiceImpl(sessionRepository, userEventEmitter);
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

}
