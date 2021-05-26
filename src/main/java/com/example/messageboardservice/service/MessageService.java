package com.example.messageboardservice.service;

import com.example.messageboardservice.repository.MessageRepository;
import com.example.messageboardservice.service.model.Message;
import com.example.messageboardservice.service.util.MessageIdCreator;
import java.util.Collection;

public class MessageService {

  private final MessageRepository messageRepository;

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public Collection<Message> getAllMessages() {
    return messageRepository.getAll();
  }

  public Message createMessage(String text, String author) {
    var messageId = MessageIdCreator.createFrom(text, author);
    var message = new Message(text, messageId, author);

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
