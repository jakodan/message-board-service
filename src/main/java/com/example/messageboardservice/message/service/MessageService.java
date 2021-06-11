package com.example.messageboardservice.message.service;

import com.example.messageboardservice.message.service.model.Message;
import com.example.messageboardservice.message.service.util.MessageIdCreator;
import com.example.messageboardservice.message.service.util.TimestampCreator;
import java.util.Collection;
import java.util.UUID;

public class MessageService {

  private final MessageRepository messageRepository;
  private final TimestampCreator timestampCreator;
  private final MessageIdCreator messageIdCreator;

  public MessageService(MessageRepository messageRepository, TimestampCreator timestampCreator,
      MessageIdCreator messageIdCreator) {
    this.messageRepository = messageRepository;
    this.timestampCreator = timestampCreator;
    this.messageIdCreator = messageIdCreator;
  }

  public Collection<Message> getAllMessages() {
    return messageRepository.getAll();
  }

  public Message createMessage(String text, String author, UUID requestKey) {
    var messageId = messageIdCreator.createFrom(text, author, requestKey.toString());
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
