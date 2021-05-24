package com.example.messageboardservice.controller.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.service.model.Message;
import java.util.List;
import org.junit.jupiter.api.Test;

class MessageDtoCollectionTest {

  @Test
  void shouldCreateFromDomainMessages() {
    var domainMessage = new Message("this is a message");

    var result = MessageDtoCollection.createFrom(List.of(domainMessage));

    assertThat(result.getMessages()).containsExactly(MessageDto.createFrom(domainMessage));
  }
}