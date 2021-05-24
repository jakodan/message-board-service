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
  void shouldReturnMessageId_whenCreatingMessage() {
    var message = new Message("this is a message");
    var messageId = messageService.createMessage(message.getText());

    var expectedMessageId = MessageIdCreator.createFrom(message);

    assertThat(messageId).isEqualTo(expectedMessageId);
  }

  @Test
  void shouldSaveMessageToRepository() {
    var message = new Message("this is a message");
    var messageId = MessageIdCreator.createFrom(message);

    messageService.createMessage(message.getText());

    verify(messageRepository).save(messageId, message);
  }

  @Test
  void shouldGetAllMessages() {
    var message = new Message("this is a message");
    when(messageRepository.getAll()).thenReturn(List.of(message));

    var messages = messageService.getAllMessages();

    assertThat(messages).containsExactly(message);
  }
}