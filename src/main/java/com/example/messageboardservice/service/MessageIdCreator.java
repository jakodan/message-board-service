package com.example.messageboardservice.service;

import lombok.experimental.UtilityClass;

@UtilityClass
class MessageIdCreator {

  static String createFrom(String text) {
    return Integer.toString(text.hashCode());
  }
}
