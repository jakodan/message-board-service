package com.example.messageboardservice.controller;

import com.example.messageboardservice.controller.dto.MessageDto;
import com.example.messageboardservice.controller.dto.MessageDtoCollection;
import com.example.messageboardservice.controller.dto.NewMessageDto;
import com.example.messageboardservice.controller.dto.UpdatedMessage;
import com.example.messageboardservice.controller.util.MessageURICreator;
import com.example.messageboardservice.service.MessageService;
import com.example.messageboardservice.service.exception.MessageNotFoundException;
import com.example.messageboardservice.service.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping(
    path = "/messages",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class RESTMessageController {

  private final MessageURICreator messageURICreator;
  private final MessageService messageService;

  public RESTMessageController(MessageURICreator messageURICreator, MessageService messageService) {
    this.messageURICreator = messageURICreator;
    this.messageService = messageService;
  }

  @GetMapping
  public ResponseEntity<MessageDtoCollection> getMessages() {
    var messages = messageService.getAllMessages();
    var messageCollection = MessageDtoCollection.createFrom(messages);

    return ResponseEntity.ok(messageCollection);
  }

  @PostMapping
  public ResponseEntity<MessageDto> postMessage(@Validated @RequestBody NewMessageDto newMessageDto,
      @CurrentSecurityContext SecurityContext securityContext) {
    var username = getUsername(securityContext);

    var message = messageService.createMessage(newMessageDto.getText(), username, newMessageDto.getRequestKey());
    var messageUri = messageURICreator.create(message.getId());

    var responseBody = MessageDto.createFrom(message);

    return ResponseEntity.created(messageUri).body(responseBody);
  }

  @PutMapping("/{message-id}")
  public ResponseEntity<Void> updateMesage(
      @PathVariable("message-id") String messageId,
      @Validated @RequestBody UpdatedMessage updatedMessage,
      @CurrentSecurityContext SecurityContext securityContext) {
    try {
      var username = getUsername(securityContext);
      messageService.updateMessage(messageId, updatedMessage.getText(), username);
      return ResponseEntity.ok().build();
    } catch (MessageNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  @DeleteMapping("/{message-id}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable("message-id") String messageId,
      @CurrentSecurityContext SecurityContext securityContext) {
    try {
      var username = getUsername(securityContext);
      messageService.deleteMessage(messageId, username);
      return ResponseEntity.ok().build();
    } catch (MessageNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  private String getUsername(SecurityContext securityContext) {
    return ((User) securityContext.getAuthentication().getPrincipal()).getUsername();
  }
}
