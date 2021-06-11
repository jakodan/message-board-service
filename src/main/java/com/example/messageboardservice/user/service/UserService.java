package com.example.messageboardservice.user.service;

import com.example.messageboardservice.user.service.exception.UserAlreadyExistsException;
import com.example.messageboardservice.user.service.model.MessageUser;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class UserService implements UserDetailsService {

  private final Map<String, MessageUser> users;
  private final PasswordEncoder passwordEncoder;

  public UserService(Map<String, MessageUser> users, PasswordEncoder passwordEncoder) {
    this.users = users;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = users.get(username);

    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    return user;
  }

  public void createUser(String username, String encodedPassword) {
    users.put(username, new MessageUser(username, encodedPassword, List.of()));
  }

  public MessageUser createUser(String username, String email, String password) {
    if (users.get(username) == null) {
      var encodedPassword = passwordEncoder.encode(password);
      var user = new MessageUser(username, email, encodedPassword, List.of());
      users.put(username, user);
      return user;
    } else {
      throw new UserAlreadyExistsException(String.format("User %s already exists.", username));
    }
  }
}
