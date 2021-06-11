package com.example.messageboardservice.message.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@Data
public class MessageConfigurationProperties {

  private List<MessageConfig> testMessages;

  @Data
  public static class MessageConfig {

    String author;
    String text;
  }
}
