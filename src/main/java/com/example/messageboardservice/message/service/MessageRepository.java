package com.example.messageboardservice.message.service;

import com.example.messageboardservice.message.service.model.Message;
import java.util.Collection;

public interface MessageRepository {

  void save(Message message);

  Collection<Message> getAll();

  void deleteMessage(String messageId, String username);

  void updateMessageText(String messageId, String newText, String username);
}
