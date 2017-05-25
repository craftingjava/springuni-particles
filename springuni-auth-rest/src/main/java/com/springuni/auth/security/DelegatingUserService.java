package com.springuni.auth.security;

import com.springuni.auth.domain.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by lcsontos on 5/24/17.
 */
public class DelegatingUserService implements UserDetailsService {

  private final UserService userService;

  public DelegatingUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Long userId = Long.valueOf(username);
    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException(username);
    return userService.findUser(userId)
        .map(DelegatingUser::new)
        .orElseThrow(() -> usernameNotFoundException);
  }

}
