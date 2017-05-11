package com.springuni.auth.rest.user;

import com.springuni.auth.domain.model.user.User;
import org.modelmapper.PropertyMap;

/**
 * Created by lcsontos on 5/11/17.
 */
public class UserMap extends PropertyMap<User, UserDto> {

  @Override
  protected void configure() {
    skip().setPassword(null);
  }

}
