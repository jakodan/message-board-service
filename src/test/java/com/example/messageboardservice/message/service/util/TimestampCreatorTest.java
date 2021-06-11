package com.example.messageboardservice.message.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimestampCreatorTest {

  private TimestampCreator timestampCreator;

  @BeforeEach
  void setUp() {
    timestampCreator = new TimestampCreator("UTC");
  }

  @Test
  void shouldCreateTimestamp() {
    var result = timestampCreator.now();

    assertThat(result).isNotNull();
    assertThat(result).isBefore(OffsetDateTime.now());
    assertThat(result).isAfter(OffsetDateTime.now().minus(Duration.ofSeconds(10)));
  }
}