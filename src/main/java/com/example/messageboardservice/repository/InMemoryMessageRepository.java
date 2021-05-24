package com.example.messageboardservice.repository;

import com.example.messageboardservice.service.model.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryMessageRepository implements MessageRepository {

  private final Map<String, Message> messageMap;

  public InMemoryMessageRepository(Map<String, Message> messageMap) {
    this.messageMap = messageMap;
  }

  @Override
  public void save(String messageId, Message message) {
    messageMap.putIfAbsent(messageId, message);
  }

  @Override
  public List<Message> getAll() {
    return new ArrayList<>(messageMap.values());
  }

  @Override
  public List<Message> getMessagesAfter(String messageId, int maxResults) {
    throw new AssertionError("Not implemented");
  }

  @Override
  public List<Message> getMessagesBefore(String messageId, int maxResults) {
    throw new AssertionError("Not implemented");
  }
}
