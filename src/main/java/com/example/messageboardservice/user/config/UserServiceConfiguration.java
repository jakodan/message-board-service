package com.example.messageboardservice.user.config;

import com.example.messageboardservice.user.service.UserService;
import com.example.messageboardservice.user.service.model.MessageUser;
import java.util.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserServiceConfiguration {

  @Bean
  public UserService userService(PasswordEncoder passwordEncoder) {
    var users = new HashMap<String, MessageUser>();
    return new UserService(users, passwordEncoder);
  }
}
