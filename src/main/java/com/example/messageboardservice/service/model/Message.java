package com.example.messageboardservice.service.model;

import lombok.Value;
import lombok.With;

@Value
@With
public class Message {

  String text;
  String id;
}
