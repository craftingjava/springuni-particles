package com.springuni.user.domain.service;

import com.springuni.user.domain.model.UserEvent;

/**
 * Created by lcsontos on 4/25/17.
 */
@FunctionalInterface
public interface UserEventEmitter {

  void emit(UserEvent userEvent);

}
