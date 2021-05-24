package com.example.messageboardservice.service;

import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.model.Message;

public class MessageService {

  private final MessageRepository messageRepository;

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public String createMessage(String text) {
    var message = new Message(text);
    var messageId = MessageIdCreator.createFrom(message);

    messageRepository.save(messageId, message);

    return messageId;
  }
}
