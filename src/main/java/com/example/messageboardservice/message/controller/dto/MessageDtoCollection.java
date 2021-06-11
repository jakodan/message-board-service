package com.example.messageboardservice.message.controller.dto;

import com.example.messageboardservice.message.service.model.Message;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;

@Value
public class MessageDtoCollection {

  List<MessageDto> messages;

  public static MessageDtoCollection createFrom(Collection<Message> domainMessages) {
    var messages = domainMessages.stream()
        .map(MessageDto::createFrom)
        .collect(Collectors.toList());

    return new MessageDtoCollection(messages);
  }
}
