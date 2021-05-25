package com.example.messageboardservice.controller.message.dto;

import com.example.messageboardservice.service.model.Message;
import lombok.Value;

@Value
public class MessageDto {

  String text;
  String id;

  public static MessageDto createFrom(Message domainMessage) {
    return new MessageDto(domainMessage.getText(), domainMessage.getId());
  }
}
