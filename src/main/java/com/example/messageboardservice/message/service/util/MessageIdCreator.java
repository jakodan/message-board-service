package com.example.messageboardservice.message.service.util;

import static com.example.messageboardservice.util.ValidationUtils.validateNotEmptyOrNull;

import java.util.Objects;

public class MessageIdCreator {

  public String createFrom(String text, String author, String requestKey) {
    validateNotEmptyOrNull(text, "text");
    validateNotEmptyOrNull(author, "author");
    validateNotEmptyOrNull(requestKey, "requestKey");

    return Integer.toString(Math.abs(Objects.hash(text, author, requestKey)));
  }
}
