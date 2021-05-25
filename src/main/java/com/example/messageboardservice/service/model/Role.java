package com.example.messageboardservice.service.model;

import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

@Value
public class Role implements GrantedAuthority {

  public static final Role USER = new Role("USER");

  String authority;

  @Override
  public String getAuthority() {
    return authority;
  }
}
