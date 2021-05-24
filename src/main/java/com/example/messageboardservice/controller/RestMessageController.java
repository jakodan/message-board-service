package com.example.messageboardservice.controller;

import com.example.messageboardservice.controller.dto.MessageDto;
import com.example.messageboardservice.controller.dto.MessageDtoCollection;
import com.example.messageboardservice.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/messages")
public class RestMessageController {

  private final MessageURICreator messageURICreator;
  private final MessageService messageService;

  public RestMessageController(MessageURICreator messageURICreator, MessageService messageService) {
    this.messageURICreator = messageURICreator;
    this.messageService = messageService;
  }

  @GetMapping
  public ResponseEntity<MessageDtoCollection> getMessages() {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @PostMapping
  public ResponseEntity<MessageDto> postMessage(MessageDto messageDto) {
    var messageId = messageService.createMessage(messageDto.getText());
    var messageUri = messageURICreator.create(messageId);
    return ResponseEntity.created(messageUri).build();
  }

  @PutMapping("/{message-id")
  public ResponseEntity<String> updateMesage() {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @DeleteMapping("/message-id")
  public ResponseEntity<String> deleteMessage() {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

}
