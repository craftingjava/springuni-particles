package com.springuni.auth.domain.model.userevent;

import org.springframework.context.event.EventListener;

/**
 * Created by lcsontos on 6/9/17.
 */
public class UserEventListener {

  private final UserEventReceiver userEventReceiver;

  public UserEventListener(UserEventReceiver userEventReceiver) {
    this.userEventReceiver = userEventReceiver;
  }

  @EventListener
  public void onApplicationEvent(UserEvent userEvent) {
    userEventReceiver.receive(userEvent);
  }

}
