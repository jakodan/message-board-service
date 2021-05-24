package com.example.messageboardservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.service.model.Message;
import org.junit.jupiter.api.Test;

class MessageIdCreatorTest {

  @Test
  void shouldCreateMessageId() {
    var message = new Message("this is a message");
    var expectedMessageId = Integer.toString(message.hashCode());

    var messageId = MessageIdCreator.createFrom(message);

    assertThat(messageId).isEqualTo(expectedMessageId);
  }

}