package com.example.messageboardservice.message.service.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class TimestampCreator {

  private final String timezone;

  public TimestampCreator(String timezone) {
    this.timezone = timezone;
  }

  public OffsetDateTime now() {
    return OffsetDateTime.now(ZoneId.of(timezone));
  }
}
