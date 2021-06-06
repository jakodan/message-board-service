package com.example.messageboardservice.service;

import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.model.Message;
import com.example.messageboardservice.service.util.MessageIdCreator;
import com.example.messageboardservice.service.util.TimestampCreator;
import java.util.Collection;

public class MessageService {

  private final MessageRepository messageRepository;
  private final TimestampCreator timestampCreator;

  public MessageService(MessageRepository messageRepository, TimestampCreator timestampCreator) {
    this.messageRepository = messageRepository;
    this.timestampCreator = timestampCreator;
  }

  public Collection<Message> getAllMessages() {
    return messageRepository.getAll();
  }

  public Message createMessage(String text, String author) {
    var messageId = MessageIdCreator.createFrom(text, author);
    var message = new Message(text, messageId, author, timestampCreator.now());

    messageRepository.save(message);

    return message;
  }

  public void deleteMessage(String messageId, String username) {
    messageRepository.deleteMessage(messageId, username);
  }

  public void updateMessage(String messageId, String newText, String username) {
    messageRepository.updateMessageText(messageId, newText, username);
  }
}
