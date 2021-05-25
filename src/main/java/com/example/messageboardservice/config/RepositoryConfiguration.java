package com.example.messageboardservice.config;

import com.example.messageboardservice.repository.InMemoryMessageRepository;
import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.repository.UserRepository;
import com.example.messageboardservice.service.model.MessageUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;

@Configuration
public class RepositoryConfiguration {

  @Bean
  public MessageRepository messageRepository() {
    return new InMemoryMessageRepository();
  }

  @Bean
  public UserRepository userRepository() {
    var alice = createUser("Alice", "Alice");
    var bob = createUser("Bob", "Bob");
    var users = createUserMap(alice, bob);

    return new UserRepository(users);
  }

  private static User createUser(String username, String password) {
    return new MessageUser(username, password, List.of());
  }

  private static Map<String, User> createUserMap(User... users) {
    var userMap = new HashMap<String, User>();

    for (User u : users) {
      userMap.put(u.getUsername(), u);
    }

    return userMap;
  }
}
