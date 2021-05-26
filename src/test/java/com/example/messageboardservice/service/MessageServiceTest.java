package com.example.messageboardservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.model.Message;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageServiceTest {

  private MessageService messageService;
  private MessageRepository messageRepository;

  @BeforeEach
  void setUp() {
    messageRepository = mock(MessageRepository.class);
    messageService = new MessageService(messageRepository);
  }

  @Test
  void shouldReturnMessage_whenCreatingMessage() {
    var messageText = "this is a message";
    var author = "test-user";

    var createdMessage = messageService.createMessage(messageText, author);

    var expectedMessageId = MessageIdCreator.createFrom(messageText, author);

    assertThat(createdMessage).isEqualTo(new Message(messageText, expectedMessageId, author));
  }

  @Test
  void shouldSaveMessageToRepository() {
    var messageText = "this is a message";
    var author = "test-user";
    var messageId = MessageIdCreator.createFrom(messageText, author);

    var message = new Message(messageText, messageId, author);

    messageService.createMessage(message.getText(), author);

    verify(messageRepository).save(message);
  }

  @Test
  void shouldGetAllMessages() {
    var message = new Message("this is a message", "message-id", "author");
    when(messageRepository.getAll()).thenReturn(List.of(message));

    var messages = messageService.getAllMessages();

    assertThat(messages).containsExactly(message);
  }

  @Test
  void shouldDeleteMessage() {
    var messageId = "123";
    var username = "test-user";

    messageService.deleteMessage(messageId, username);

    verify(messageRepository).deleteMessage(messageId, username);
  }

  @Test
  void shouldUpdateMessage() {
    var messageId = "123";
    var newText = "new text";
    var username = "test-user";

    messageService.updateMessage(messageId, newText, username);

    verify(messageRepository).updateMessageText(messageId, newText, username);
  }
}