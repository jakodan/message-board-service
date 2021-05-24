package com.example.messageboardservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.service.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryMessageRepositoryTest {

  private MessageRepository messageRepository;

  @BeforeEach
  void setUp() {
    messageRepository = new InMemoryMessageRepository();
  }

  @Test
  void shouldGetAllMessages() {
    var message = new Message("this is a message");
    var messageId = "123";
    messageRepository.save(messageId, message);

    var allMessages = messageRepository.getAll();

    assertThat(allMessages).containsExactly(message);
  }

}