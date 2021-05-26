package com.example.messageboardservice.controller.message.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.service.MessageFactory;
import org.junit.jupiter.api.Test;

class MessageDtoTest {

  @Test
  void shouldCreateMessageDtoFromDomainMessage() {
    var domainMessage = MessageFactory.create();

    var messageDto = MessageDto.createFrom(domainMessage);

    assertThat(messageDto.getText()).isEqualTo(domainMessage.getText());
    assertThat(messageDto.getId()).isEqualTo(domainMessage.getId());
    assertThat(messageDto.getAuthor()).isEqualTo(domainMessage.getAuthor());
  }
}