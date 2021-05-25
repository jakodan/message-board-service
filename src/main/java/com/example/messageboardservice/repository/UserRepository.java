package com.example.messageboardservice.repository;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class UserRepository implements UserDetailsService {

  private final Map<String, User> users;

  public UserRepository(Map<String, User> users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = users.get(username);

    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    return user;
  }
}
