package com.example.messageboardservice.message.config;

import com.example.messageboardservice.message.service.MessageService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(MessageConfigurationProperties.class)
@Slf4j
@Profile("test")
public class MessageConfiguration {

  private final MessageConfigurationProperties messageConfigurationProperties;
  private final MessageService messageService;

  public MessageConfiguration(MessageConfigurationProperties messageConfigurationProperties,
      MessageService messageService) {
    this.messageConfigurationProperties = messageConfigurationProperties;
    this.messageService = messageService;

    if (messageConfigurationProperties.getTestMessages() != null) {
      messageConfigurationProperties.getTestMessages()
          .forEach(messageConfig -> messageService.createMessage(messageConfig.text, messageConfig.author, UUID.randomUUID()));
    }
  }
}
