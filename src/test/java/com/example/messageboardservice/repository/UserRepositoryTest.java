package com.example.messageboardservice.repository;

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

class UserRepositoryTest {

  private static final User TEST_USER = new User("TestUser", "TestPassword", List.of());

  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository = new UserRepository(Map.of(TEST_USER.getUsername(), TEST_USER));
  }

  @Test
  void shouldGetUser() {
    var result = userRepository.loadUserByUsername(TEST_USER.getUsername());

    assertThat(result).isEqualTo(TEST_USER);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"non existing user"})
  void shouldThrowUsernameNotFoundException(String invalidUsername) {
    assertThatThrownBy(() -> userRepository.loadUserByUsername(invalidUsername))
        .isInstanceOf(UsernameNotFoundException.class);
  }
}