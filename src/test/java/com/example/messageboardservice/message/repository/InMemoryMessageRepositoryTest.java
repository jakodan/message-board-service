package com.example.messageboardservice.message.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.messageboardservice.message.MessageFactory;
import com.example.messageboardservice.message.service.MessageRepository;
import com.example.messageboardservice.message.service.exception.MessageNotFoundException;
import com.example.messageboardservice.message.service.exception.UnauthorizedException;
import com.example.messageboardservice.message.service.model.Message;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

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
  void shouldDeleteMessage_whenUsernameIsAuthor() {
    var message = MessageFactory.create();
    messageRepository.save(message);

    messageRepository.deleteMessage(message.getId(), message.getAuthor());

    assertThat(messageRepository.getAll()).isEmpty();
  }

  @Test
  void shouldUpdateMessageText() {
    var message = MessageFactory.create();
    messageRepository.save(message);

    var updatedMessageText = message.getText() + " updated";

    messageRepository.updateMessageText(message.getId(), updatedMessageText, message.getAuthor());

    var expectedMessage = new Message(updatedMessageText, message.getId(), message.getAuthor(), null);
    var updatedMessage = messageRepository.getAll().stream().findFirst().orElseThrow();
    assertThat(updatedMessage).usingRecursiveComparison().ignoringFields("createdAt").isEqualTo(expectedMessage);
  }

  @Test
  void shouldThrowMessageNotFoundException_whenUpdatingAndNotFound() {
    assertThatThrownBy(() -> messageRepository.updateMessageText("non-existing-message-id", "text update", "test-user"))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void shouldThrowMessageNotFoundException_whenDeletingAndNotFound() {
    assertThatThrownBy(() -> messageRepository.deleteMessage("non-existing-message-id", "test-user"))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void shouldKeepInsertionOrderOfMessages() {
    var numberOfMessages = 1000;
    var random = new SecureRandom();
    var messages = new Message[numberOfMessages];

    for (int i = 0; i < numberOfMessages; i++) {
      var m = new Message("text", Integer.toString(random.nextInt()), "test-author", OffsetDateTime.now());
      messages[i] = m;
      messageRepository.save(m);
    }

    assertThat(messageRepository.getAll()).containsExactly(messages);
  }

  @Test
  void shouldThrowUnauthorizedException_whenDeleting_andAuthorAndUsernameMismatch() {
    var message = MessageFactory.create();
    messageRepository.save(message);

    assertThatThrownBy(() -> messageRepository.deleteMessage(message.getId(), message.getAuthor() + " wrong"))
        .isInstanceOf(UnauthorizedException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrow_whenDeleting_andNullOrEmptyInput(String input) {
    assertThatThrownBy(() -> messageRepository.deleteMessage(input, "username"))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> messageRepository.deleteMessage("messageId", input))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrow_whenUpdating_andNullOrEmptyInput(String input) {
    assertThatThrownBy(() -> messageRepository.updateMessageText(input, "newText", "test-user"))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> messageRepository.updateMessageText("messageId", input, "test-user"))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> messageRepository.updateMessageText("messageId", "newText", input))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldThrowUnauthorizedException_whenUpdating_andAuthorAndUsernameMismatch() {
    var message = MessageFactory.create();
    messageRepository.save(message);

    assertThatThrownBy(() -> messageRepository.updateMessageText(message.getId(), "newText", message.getAuthor() + " wrong"))
        .isInstanceOf(UnauthorizedException.class);
  }
}