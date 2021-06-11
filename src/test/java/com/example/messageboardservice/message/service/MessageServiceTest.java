package com.example.messageboardservice.message.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.messageboardservice.message.service.model.Message;
import com.example.messageboardservice.message.service.util.MessageIdCreator;
import com.example.messageboardservice.message.service.util.TimestampCreator;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageServiceTest {

  private MessageService messageService;
  private MessageRepository messageRepository;
  private TimestampCreator timestampCreator;
  private MessageIdCreator messageIdCreator;

  @BeforeEach
  void setUp() {
    messageRepository = mock(MessageRepository.class);
    timestampCreator = mock(TimestampCreator.class);
    messageIdCreator = mock(MessageIdCreator.class);

    messageService = new MessageService(messageRepository, timestampCreator, messageIdCreator);
  }

  @Test
  void shouldReturnMessage_whenCreatingMessage() {
    var messageText = "this is a message";
    var author = "test-user";
    var requestKey = UUID.randomUUID();
    var createdAt = OffsetDateTime.now();
    var messageId = "123";

    when(timestampCreator.now()).thenReturn(createdAt);
    when(messageIdCreator.createFrom(messageText, author, requestKey.toString())).thenReturn(messageId);

    var createdMessage = messageService.createMessage(messageText, author, requestKey);

    assertThat(createdMessage).isEqualTo(new Message(messageText, messageId, author, createdAt));
  }

  @Test
  void shouldSaveMessageToRepository() {
    var messageText = "this is a message";
    var author = "test-user";
    var requestKey = UUID.randomUUID();
    var messageId = "123";
    when(messageIdCreator.createFrom(messageText, author, requestKey.toString())).thenReturn(messageId);

    var timestamp = OffsetDateTime.now();
    when(timestampCreator.now()).thenReturn(timestamp);
    var message = new Message(messageText, messageId, author, timestamp);

    messageService.createMessage(message.getText(), author, requestKey);

    verify(messageRepository).save(message);
  }

  @Test
  void shouldGetAllMessages() {
    var timestamp = OffsetDateTime.now();
    when(timestampCreator.now()).thenReturn(timestamp);
    var message = new Message("this is a message", "message-id", "author", timestamp);
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