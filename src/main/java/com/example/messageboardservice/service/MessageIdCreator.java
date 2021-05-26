package com.example.messageboardservice.service;

import static com.example.messageboardservice.service.ValidationUtils.validateNotEmptyOrNull;

import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
class MessageIdCreator {

  static String createFrom(String text, String author) {
    validateNotEmptyOrNull(text, "text");
    validateNotEmptyOrNull(author, "author");

    return Integer.toString(Math.abs(Objects.hash(text, author)));
  }
}
