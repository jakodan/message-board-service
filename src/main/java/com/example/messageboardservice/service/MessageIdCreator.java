package com.example.messageboardservice.service;

import com.example.messageboardservice.service.model.Message;
import lombok.experimental.UtilityClass;

@UtilityClass
class MessageIdCreator {

  static String createFrom(Message message) {
    return Integer.toString(message.hashCode());
  }
}
