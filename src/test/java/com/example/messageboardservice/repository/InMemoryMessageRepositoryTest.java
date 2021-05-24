package com.example.messageboardservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.service.MessageFactory;
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
    var message = MessageFactory.create();

    messageRepository.save(message);

    var allMessages = messageRepository.getAll();

    assertThat(allMessages).containsExactly(message);
  }

  @Test
  void shouldDeleteMessage() {
    var message = MessageFactory.create();
    messageRepository.save(message);

    messageRepository.deleteMessage(message.getId());

    assertThat(messageRepository.getAll()).isEmpty();
  }

}