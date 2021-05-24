package com.example.messageboardservice.service;

import com.example.messageboardservice.service.model.Message;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageFactory {

  public static Message create() {
    return new Message("this is a test message", "test-message-id");
  }
}
