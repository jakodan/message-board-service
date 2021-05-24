package com.example.messageboardservice.repository;

import com.example.messageboardservice.service.exception.MessageNotFoundException;
import com.example.messageboardservice.service.model.Message;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryMessageRepository implements MessageRepository {

  private final Map<String, Message> messageMap;

  public InMemoryMessageRepository() {
    this.messageMap = Collections.synchronizedMap(new LinkedHashMap<>());
  }

  @Override
  public void save(Message message) {
    messageMap.putIfAbsent(message.getId(), message);
  }

  @Override
  public Collection<Message> getAll() {
    return new LinkedHashMap<>(messageMap).values();
  }

  @Override
  public void deleteMessage(String messageId) {
    var removedMessage = messageMap.remove(messageId);

    if (removedMessage == null) {
      throw new MessageNotFoundException(messageId);
    }
  }

  @Override
  public void updateMessageText(String messageId, String newText) {
    var message = messageMap.get(messageId);

    if (message == null) {
      throw new MessageNotFoundException(messageId);
    }

    var updatedMessage = message.withText(newText);

    messageMap.put(messageId, updatedMessage);
  }
}
