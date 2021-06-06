package com.example.messageboardservice.controller.dto;

import com.example.messageboardservice.service.model.Message;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageDto {

  String text;
  String id;
  String author;
  String createdAt;

  public static MessageDto createFrom(Message domainMessage) {
    return MessageDto.builder()
        .text(domainMessage.getText())
        .id(domainMessage.getId())
        .author(domainMessage.getAuthor())
        .createdAt(domainMessage.getCreatedAt().toString())
        .build();
  }
}
