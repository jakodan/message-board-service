package com.example.messageboardservice.message.controller.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.message.MessageFactory;
import java.util.List;
import org.junit.jupiter.api.Test;

class MessageDtoCollectionTest {

  @Test
  void shouldCreateFromDomainMessages() {
    var domainMessage = MessageFactory.create();

    var result = MessageDtoCollection.createFrom(List.of(domainMessage));

    assertThat(result.getMessages()).containsExactly(MessageDto.createFrom(domainMessage));
  }
}