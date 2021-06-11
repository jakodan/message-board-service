package com.example.messageboardservice.message.config;

import com.example.messageboardservice.message.controller.util.MessageURICreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageControllerConfiguration {

  @Bean
  public MessageURICreator messageURICreator(@Value("${messages-base-url}") String messagesBaseUrl) {
    return new MessageURICreator(messagesBaseUrl);
  }
}
