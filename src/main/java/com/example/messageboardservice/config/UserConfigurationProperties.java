package com.example.messageboardservice.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@Data
public class UserConfigurationProperties {

  private List<UserConfig> testUsers;

  @Data
  public static class UserConfig {

    String username;
    String password;
  }
}
