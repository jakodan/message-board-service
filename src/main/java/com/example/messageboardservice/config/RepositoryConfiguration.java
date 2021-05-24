package com.example.messageboardservice.config;

import com.example.messageboardservice.repository.InMemoryMessageRepository;
import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.model.Message;
import java.util.Collections;
import java.util.LinkedHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

  @Bean
  public MessageRepository messageRepository() {
    var messageMap = Collections.synchronizedMap(new LinkedHashMap<String, Message>());

    return new InMemoryMessageRepository(messageMap);
  }
}
