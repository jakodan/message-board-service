package com.example.messageboardservice.config;

import com.example.messageboardservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableConfigurationProperties(UserConfigurationProperties.class)
@Slf4j
public class UserConfiguration {

  private final UserConfigurationProperties userConfigurationProperties;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  public UserConfiguration(UserConfigurationProperties userConfigurationProperties,
      PasswordEncoder passwordEncoder, UserService userService) {
    this.userConfigurationProperties = userConfigurationProperties;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;

    if (userConfigurationProperties.getTestUsers() != null) {
      for (var user : userConfigurationProperties.getTestUsers()) {
        log.info("Creating user with username {}", user.getUsername());
        userService.createUser(user.getUsername(), passwordEncoder.encode(user.getPassword()));
      }
    }
  }
}
