package com.example.messageboardservice.user.service.model;

import java.util.Collection;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Value
public class MessageUser extends User {

  String email;

  public MessageUser(String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.email = null;
  }

  public MessageUser(String username, String email, String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.email = email;
  }

  @Override
  public void eraseCredentials() {
    // do nothing
  }
}
