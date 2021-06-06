package com.example.messageboardservice.config;

import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.MessageService;
import com.example.messageboardservice.service.UserService;
import com.example.messageboardservice.service.util.MessageIdCreator;
import com.example.messageboardservice.service.util.TimestampCreator;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;

@Configuration
public class ServiceConfiguration {

  @Bean
  public MessageService messageService(MessageRepository messageRepository, TimestampCreator timestampCreator,
      MessageIdCreator messageIdCreator) {
    return new MessageService(messageRepository, timestampCreator, messageIdCreator);
  }

  @Bean
  public UserService userService() {
    var users = new HashMap<String, User>();
    return new UserService(users);
  }

  @Bean
  public TimestampCreator timestampCreator(@Value("${timezone}") String timezone) {
    return new TimestampCreator(timezone);
  }

  @Bean
  public MessageIdCreator messageIdCreator() {
    return new MessageIdCreator();
  }
}
