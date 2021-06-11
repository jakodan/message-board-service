package com.example.messageboardservice.message;

import com.example.messageboardservice.message.service.model.Message;
import java.time.OffsetDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageFactory {

  public static Message create() {
    return new Message("this is a test message", "test-message-id", "test-author", OffsetDateTime.now());
  }
}
