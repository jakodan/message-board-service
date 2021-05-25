package com.example.messageboardservice.service.model;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MessageUser extends User {

  public MessageUser(String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }

  @Override
  public void eraseCredentials() {
    // do nothing
  }
}
