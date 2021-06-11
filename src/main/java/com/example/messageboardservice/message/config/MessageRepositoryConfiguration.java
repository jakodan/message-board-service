package com.example.messageboardservice.message.config;

import com.example.messageboardservice.message.repository.InMemoryMessageRepository;
import com.example.messageboardservice.message.service.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageRepositoryConfiguration {

  @Bean
  public MessageRepository messageRepository() {
    return new InMemoryMessageRepository();
  }

}
