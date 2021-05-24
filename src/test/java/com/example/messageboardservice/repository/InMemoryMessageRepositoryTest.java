package com.example.messageboardservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.messageboardservice.service.MessageFactory;
import com.example.messageboardservice.service.exception.MessageNotFoundException;
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

  @Test
  void shouldUpdateMessageText() {
    var message = MessageFactory.create();
    messageRepository.save(message);

    var updatedMessageText = message.getText() + " updated";

    messageRepository.updateMessageText(message.getId(), updatedMessageText);

    var expectedMessage = new Message(updatedMessageText, message.getId());
    var updatedMessage = messageRepository.getAll().stream().findFirst().orElseThrow();
    assertThat(updatedMessage).isEqualTo(expectedMessage);
  }

  @Test
  void shouldThrowMessageNotFoundException_whenUpdatingAndNotFound() {
    assertThatThrownBy(() -> messageRepository.updateMessageText("non-existing-message-id", "text update"))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void shouldThrowMessageNotFoundException_whenDeletingAndNotFound() {
    assertThatThrownBy(() -> messageRepository.deleteMessage("non-existing-message-id"))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void shouldKeepInsertionOrderOfMessages() {
    var message1 = new Message("text", "1");
    var message2 = new Message("text", "2");

    messageRepository.save(message2);
    messageRepository.save(message1);

    assertThat(messageRepository.getAll()).containsExactly(message2, message1);
  }

}