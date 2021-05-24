package com.example.messageboardservice.config;

import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.MessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

  @Bean
  public MessageService messageService(MessageRepository messageRepository) {
    return new MessageService(messageRepository);
  }
}
