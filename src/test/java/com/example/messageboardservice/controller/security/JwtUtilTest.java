package com.example.messageboardservice.controller.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

class JwtUtilTest {

  private static final String SECRET_KEY = "secret";
  private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil(SECRET_KEY);
  }

  @Test
  void shouldGenerateNonEmptyToken() {
    var user = new User("username", "password", List.of());
    var token = jwtUtil.generateToken(user);

    assertThat(token).isNotEmpty();
  }

  @Test
  void shouldGenerateValidtoken() {
    var user = new User("username", "password", List.of());
    var token = jwtUtil.generateToken(user);

    assertThat(jwtUtil.validate(token)).isTrue();
  }

  @Test
  void shouldParseUsernameFromToken() {
    var username = "username";
    var user = new User(username, "password", List.of());
    var token = jwtUtil.generateToken(user);

    assertThat(jwtUtil.extractUsername(token)).isEqualTo(username);
  }
}