package com.example.messageboardservice.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.messageboardservice.user.service.exception.UserAlreadyExistsException;
import com.example.messageboardservice.user.service.model.MessageUser;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  private static final MessageUser TEST_USER = new MessageUser("TestUser", "TestPassword", List.of());

  private UserService userService;
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    passwordEncoder = mock(PasswordEncoder.class);
    var users = new HashMap<String, MessageUser>();
    users.put(TEST_USER.getUsername(), TEST_USER);
    userService = new UserService(users, passwordEncoder);
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

  @Test
  void shouldReturnUsername_whenCreated() {
    var username = "jakob";
    var email = "jakob@example.com";
    var password = "password123";
    var encodedPassword = "encodedPassword123";
    when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

    var result = userService.createUser(username, email, password);

    assertThat(result.getUsername()).isEqualTo(username);
  }

  @Test
  void shouldReturnEmail_whenCreated() {
    var username = "jakob";
    var email = "jakob@example.com";
    var password = "password123";
    var encodedPassword = "encodedPassword123";
    when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

    var result = userService.createUser(username, email, password);

    assertThat(result.getEmail()).isEqualTo(email);
  }

  @Test
  void shouldReturnEncodedPassword_whenCreated() {
    var username = "jakob";
    var email = "jakob@example.com";
    var password = "password123";
    var encodedPassword = "encodedPassword123";
    when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

    var result = userService.createUser(username, email, password);

    assertThat(result.getPassword()).isEqualTo(encodedPassword);
  }

  @Test
  void shouldThrowUserAlreadyExistsException() {
    var username = "jakob";
    var email = "jakob@example.com";
    var password = "password123";
    var encodedPassword = "encodedPassword123";
    when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

    userService.createUser(username, email, password);

    assertThatThrownBy(() -> userService.createUser(username, email, password))
        .isInstanceOf(UserAlreadyExistsException.class);
  }
}