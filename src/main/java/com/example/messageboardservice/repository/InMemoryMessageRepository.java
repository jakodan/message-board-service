package com.example.messageboardservice.repository;

import com.example.messageboardservice.service.exception.MessageNotFoundException;
import com.example.messageboardservice.service.exception.UnauthorizedException;
import com.example.messageboardservice.service.model.Message;
import com.example.messageboardservice.service.util.ValidationUtils;
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
  public void deleteMessage(String messageId, String username) {
    ValidationUtils.validateNotEmptyOrNull(messageId, "messageId");
    ValidationUtils.validateNotEmptyOrNull(username, "username");

    var message = messageMap.get(messageId);

    if (message == null) {
      throw new MessageNotFoundException(messageId);
    }

    if (message.getAuthor() != null && username.equals(message.getAuthor())) {
      messageMap.remove(messageId);
      return;
    }

    throw new UnauthorizedException(String.format("User %s not authorized to delete messageId %s", username, messageId));
  }

  @Override
  public void updateMessageText(String messageId, String newText, String username) {
    ValidationUtils.validateNotEmptyOrNull(messageId, "messageId");
    ValidationUtils.validateNotEmptyOrNull(newText, "newText");
    ValidationUtils.validateNotEmptyOrNull(username, "username");

    var message = messageMap.get(messageId);

    if (message == null) {
      throw new MessageNotFoundException(messageId);
    }

    if (message.getAuthor() != null && message.getAuthor().equals(username)) {
      var updatedMessage = message.withText(newText);
      messageMap.put(messageId, updatedMessage);
      return;
    }

    throw new UnauthorizedException(String.format("User %s not authorized to update messageId %s", username, messageId));
  }
}
