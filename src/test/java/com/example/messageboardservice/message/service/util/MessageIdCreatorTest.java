package com.example.messageboardservice.message.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MessageIdCreatorTest {

  private MessageIdCreator messageIdCreator;

  @BeforeEach
  void setUp() {
    messageIdCreator = new MessageIdCreator();
  }

  @Test
  void shouldCreateMessageId() {
    var text = "this is a message";
    var author = "test-user";
    var requestKey = "request-key";

    var expectedMessageId = Integer.toString(Math.abs(Objects.hash(text, author, requestKey)));

    var messageId = messageIdCreator.createFrom(text, author, requestKey);

    assertThat(messageId).isEqualTo(expectedMessageId);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowIllegalArgumentException_whenTextIsNullOrEmpty(String text) {
    assertThatThrownBy(() -> messageIdCreator.createFrom(text, "author", "request-key"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowIllegalArgumentException_whenAuthorIsNullOrEmpty(String author) {
    assertThatThrownBy(() -> messageIdCreator.createFrom("text", author, "request-key"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowIllegalArgumentException_whenRequestKeyIsNullOrEmpty(String requestKey) {
    assertThatThrownBy(() -> messageIdCreator.createFrom("text", "author", requestKey))
        .isInstanceOf(IllegalArgumentException.class);
  }

}