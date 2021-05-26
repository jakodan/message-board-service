package com.example.messageboardservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserServiceTest {

  private static final User TEST_USER = new User("TestUser", "TestPassword", List.of());

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(Map.of(TEST_USER.getUsername(), TEST_USER));
  }

  @Test
  void shouldGetUser() {
    var result = userService.loadUserByUsername(TEST_USER.getUsername());

    assertThat(result).isEqualTo(TEST_USER);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"non existing user"})
  void shouldThrowUsernameNotFoundException(String invalidUsername) {
    assertThatThrownBy(() -> userService.loadUserByUsername(invalidUsername))
        .isInstanceOf(UsernameNotFoundException.class);
  }
}