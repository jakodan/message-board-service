package com.example.messageboardservice.message.controller.util;

import java.net.URI;

public class MessageURICreator {

  private final String messagesBaseUrl;

  public MessageURICreator(String messagesBaseUrl) {
    this.messagesBaseUrl = messagesBaseUrl;
  }

  public URI create(String messageId) {
    return URI.create(messagesBaseUrl + "/" + messageId);
  }
}
