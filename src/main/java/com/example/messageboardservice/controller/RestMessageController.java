package com.example.messageboardservice.controller;

import com.example.messageboardservice.controller.dto.Messages;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;
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

  @GetMapping
  public Messages getMessages() {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @PutMapping("/{message-id")
  public String updateMesage() {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @DeleteMapping("/message-id")
  public String deleteMessage() {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @PostMapping
  public Message postMessage() {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }
}
