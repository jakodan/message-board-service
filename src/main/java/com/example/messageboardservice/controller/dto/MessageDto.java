package com.example.messageboardservice.controller.dto;

import com.example.messageboardservice.service.model.Message;
import lombok.Value;

@Value
public class MessageDto {

  String text;

  static MessageDto createFrom(Message domainMessage) {
    return new MessageDto(domainMessage.getText());
  }
}
