package com.example.messageboardservice.message.service.model;

import java.time.OffsetDateTime;
import lombok.Value;
import lombok.With;

@Value
@With
public class Message {

  String text;
  String id;
  String author;
  OffsetDateTime createdAt;
}
