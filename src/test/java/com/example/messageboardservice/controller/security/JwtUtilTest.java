package com.example.messageboardservice.controller.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

  private static final String SECRET_KEY = "secret";
  private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil(SECRET_KEY);
  }

  @Test
  void shouldGenerateNonEmptyToken() {
    var token = jwtUtil.generateAccessToken("username");

    assertThat(token).isNotEmpty();
  }

  @Test
  void shouldGenerateValidToken() {
    var token = jwtUtil.generateAccessToken("username");

    assertThat(jwtUtil.validateAccessToken(token)).isTrue();
  }

  @Test
  void shouldParseUsernameFromToken() {
    var username = "username";
    var token = jwtUtil.generateAccessToken(username);

    assertThat(jwtUtil.extractUsername(token)).isEqualTo(username);
  }
}