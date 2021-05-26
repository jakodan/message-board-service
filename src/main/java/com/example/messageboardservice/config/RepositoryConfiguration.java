package com.example.messageboardservice.config;

import com.example.messageboardservice.repository.InMemoryMessageRepository;
import com.example.messageboardservice.repository.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

  @Bean
  public MessageRepository messageRepository() {
    return new InMemoryMessageRepository();
  }

}
