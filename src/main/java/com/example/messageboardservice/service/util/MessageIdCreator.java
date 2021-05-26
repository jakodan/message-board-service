package com.example.messageboardservice.service.util;

import static com.example.messageboardservice.service.util.ValidationUtils.validateNotEmptyOrNull;

import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageIdCreator {

  public static String createFrom(String text, String author) {
    validateNotEmptyOrNull(text, "text");
    validateNotEmptyOrNull(author, "author");

    return Integer.toString(Math.abs(Objects.hash(text, author)));
  }
}
