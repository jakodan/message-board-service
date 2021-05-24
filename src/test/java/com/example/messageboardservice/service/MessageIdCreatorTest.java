package com.example.messageboardservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MessageIdCreatorTest {

  @Test
  void shouldCreateMessageId() {
    var text = "this is a message";
    var expectedMessageId = Integer.toString(text.hashCode());

    var messageId = MessageIdCreator.createFrom(text);

    assertThat(messageId).isEqualTo(expectedMessageId);
  }

}