package com.example.messageboardservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MessageIdCreatorTest {

  @Test
  void shouldCreateMessageId() {
    var text = "this is a message";
    var author = "test-user";
    var expectedMessageId = Integer.toString(Math.abs(Objects.hash(text, author)));

    var messageId = MessageIdCreator.createFrom(text, author);

    assertThat(messageId).isEqualTo(expectedMessageId);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowIllegalArgumentException_whenTextIsNullOrEmpty(String text) {
    assertThatThrownBy(() -> MessageIdCreator.createFrom(text, "author"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowIllegalArgumentException_whenAuthorIsNullOrEmpty(String author) {
    assertThatThrownBy(() -> MessageIdCreator.createFrom("text", author))
        .isInstanceOf(IllegalArgumentException.class);
  }

}