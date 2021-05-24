package com.example.messageboardservice.repository;

import com.example.messageboardservice.service.model.Message;
import java.util.Collection;

public interface MessageRepository {

  void save(Message message);

  Collection<Message> getAll();

  void deleteMessage(String messageId);

  void updateMessageText(String messageId, String newText);
}
