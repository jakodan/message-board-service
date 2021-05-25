package com.example.messageboardservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.controller.message.MessageURICreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageURICreatorTest {

  private MessageURICreator messageURICreator;
  private String messagebaseUrl;

  @BeforeEach
  void setUp() {
    messagebaseUrl = "http://localhost/messages";
    messageURICreator = new MessageURICreator(messagebaseUrl);
  }

  @Test
  void shouldCreateURI() {
    var messageId = "1234";

    var uri = messageURICreator.create(messageId);

    assertThat(uri.toString()).isEqualTo(messagebaseUrl + "/" + messageId);
  }
}