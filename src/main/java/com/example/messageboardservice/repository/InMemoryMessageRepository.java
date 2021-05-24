package com.example.messageboardservice.repository;

import com.example.messageboardservice.service.model.Message;
import java.util.ArrayList;
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
  public void save(String messageId, Message message) {
    messageMap.putIfAbsent(messageId, message);
  }

  @Override
  public Collection<Message> getAll() {
    return new ArrayList<>(messageMap.values());
  }

  @Override
  public Collection<Message> getMessagesAfter(String messageId, int maxResults) {
    throw new AssertionError("Not implemented");
  }

  @Override
  public Collection<Message> getMessagesBefore(String messageId, int maxResults) {
    throw new AssertionError("Not implemented");
  }
}
