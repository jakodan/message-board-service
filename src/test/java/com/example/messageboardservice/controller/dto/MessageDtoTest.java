package com.example.messageboardservice.controller.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.service.model.Message;
import org.junit.jupiter.api.Test;

class MessageDtoTest {

  @Test
  void shouldCreateMessageDtoFromDomainMessage() {
    var domainMessage = new Message("this is a message");

    var messageDto = MessageDto.createFrom(domainMessage);

    assertThat(messageDto.getText()).isEqualTo(domainMessage.getText());
  }
}