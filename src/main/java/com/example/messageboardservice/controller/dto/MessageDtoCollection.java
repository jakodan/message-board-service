package com.example.messageboardservice.controller.dto;

import java.util.Collection;
import lombok.Value;

@Value
public class MessageDtoCollection {

  Collection<MessageDto> messages;
}
