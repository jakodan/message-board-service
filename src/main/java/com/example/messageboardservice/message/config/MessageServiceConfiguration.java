package com.example.messageboardservice.message.config;

import com.example.messageboardservice.message.service.MessageRepository;
import com.example.messageboardservice.message.service.MessageService;
import com.example.messageboardservice.message.service.util.MessageIdCreator;
import com.example.messageboardservice.message.service.util.TimestampCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageServiceConfiguration {

  @Bean
  public MessageService messageService(MessageRepository messageRepository, TimestampCreator timestampCreator,
      MessageIdCreator messageIdCreator) {
    return new MessageService(messageRepository, timestampCreator, messageIdCreator);
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
