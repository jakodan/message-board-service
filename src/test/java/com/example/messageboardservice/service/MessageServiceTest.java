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

    var createdMessage = messageService.createMessage(messageText);

    var expectedMessageId = MessageIdCreator.createFrom(messageText);

    assertThat(createdMessage).isEqualTo(new Message(messageText, expectedMessageId));
  }

  @Test
  void shouldSaveMessageToRepository() {
    var messageText = "this is a message";
    var messageId = MessageIdCreator.createFrom(messageText);

    var message = new Message(messageText, messageId);

    messageService.createMessage(message.getText());

    verify(messageRepository).save(message);
  }

  @Test
  void shouldGetAllMessages() {
    var message = new Message("this is a message", "message-id");
    when(messageRepository.getAll()).thenReturn(List.of(message));

    var messages = messageService.getAllMessages();

    assertThat(messages).containsExactly(message);
  }

  @Test
  void shouldDeleteMessage() {
    var messageId = "123";

    messageService.deleteMessage(messageId);

    verify(messageRepository).deleteMessage(messageId);
  }

  @Test
  void shouldUpdateMessage() {
    var messageId = "123";
    var newText = "new text";

    messageService.updateMessage(messageId, newText);

    verify(messageRepository).updateMessageText(messageId, newText);
  }
}