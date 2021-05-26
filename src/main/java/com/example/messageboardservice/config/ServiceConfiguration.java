package com.example.messageboardservice.config;

import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.MessageService;
import com.example.messageboardservice.service.UserService;
import java.util.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;

@Configuration
public class ServiceConfiguration {

  @Bean
  public MessageService messageService(MessageRepository messageRepository) {
    return new MessageService(messageRepository);
  }

  @Bean
  public UserService userService() {
    var users = new HashMap<String, User>();
    return new UserService(users);
  }
}
