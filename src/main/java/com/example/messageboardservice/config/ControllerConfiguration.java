package com.example.messageboardservice.config;

import com.example.messageboardservice.controller.MessageURICreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

  @Bean
  public MessageURICreator messageURICreator(@Value("${messages-base-url}") String messagesBaseUrl) {
    return new MessageURICreator(messagesBaseUrl);
  }
}
