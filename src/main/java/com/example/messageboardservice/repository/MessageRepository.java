package com.example.messageboardservice.repository;

import com.example.messageboardservice.service.model.Message;
import java.util.Collection;

public interface MessageRepository {

  void save(Message message);

  Collection<Message> getAll();

  Collection<Message> getMessagesAfter(String messageId, int maxResults);

  Collection<Message> getMessagesBefore(String messageId, int maxResults);

  void deleteMessage(String messageId);
}
