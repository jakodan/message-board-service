package com.example.messageboardservice.message.service.exception;

public class MessageNotFoundException extends RuntimeException {

  public MessageNotFoundException(String messageId) {
    super(String.format("Message with id %s not found", messageId));
  }
}
