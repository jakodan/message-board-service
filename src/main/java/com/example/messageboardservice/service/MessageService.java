package com.example.messageboardservice.service;

import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.model.Message;
import java.util.Collection;

public class MessageService {

  private final MessageRepository messageRepository;

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public Collection<Message> getAllMessages() {
    return messageRepository.getAll();
  }

  public Message createMessage(String text) {
    var messageId = MessageIdCreator.createFrom(text);
    var message = new Message(text, messageId);

    messageRepository.save(message);

    return message;
  }

  public void deleteMessage(String messageId) {
    messageRepository.deleteMessage(messageId);
  }
}
