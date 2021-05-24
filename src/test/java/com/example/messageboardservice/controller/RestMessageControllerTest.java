package com.example.messageboardservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.controller.dto.MessageDto;
import com.example.messageboardservice.controller.dto.MessageDtoCollection;
import com.example.messageboardservice.controller.dto.NewMessage;
import com.example.messageboardservice.controller.dto.UpdatedMessage;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestMessageControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private String messagesBaseUrl;

  private List<String> createdMessageIds;

  @BeforeEach
  void setup() {
    createdMessageIds = new ArrayList<>();
    messagesBaseUrl = "http://localhost:" + port + "/messages";
  }

  @AfterEach
  void cleanup() {
    for (var id : createdMessageIds) {
      deleteMessage(id);
    }
  }

  @Test
  void postMessageShouldReturnCreatedStatus() {
    var message = new NewMessage("this is a message");

    var response = postMessage(message);

    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
  }

  @Test
  void postMessageShouldReturnLocationHeader() {
    var message = new NewMessage("this is a message");

    var response = postMessage(message);

    var locationHeaders = response.getHeaders().get(HttpHeaders.LOCATION);
    assertThat(locationHeaders).hasSize(1);
    assertThat(locationHeaders.get(0)).matches("^http://localhost:8080/messages/[0-9]{1,10}$");
  }

  @Test
  void shouldGetEmptyListWhenNoMessages() {
    var messageDtoCollection = getAllMessages().getBody();

    assertThat(messageDtoCollection.getMessages()).isEmpty();
  }

  @Test
  void shouldGetMessageAfterCreating() {
    var messageToCreate = new NewMessage("this is a message");
    postMessage(messageToCreate);

    var messageDtoCollection = getAllMessages().getBody();

    assertThat(messageDtoCollection.getMessages()).hasSize(1);
    var firstMessage = messageDtoCollection.getMessages().stream().findFirst().orElseThrow();
    assertThat(firstMessage.getText()).isEqualTo(messageToCreate.getText());
  }

  @Test
  void shouldDeleteMessage() {
    var message = new NewMessage("this is a message");
    var createdMessage = postMessage(message);

    deleteMessage(createdMessage.getBody().getId());

    assertThat(getAllMessages().getBody().getMessages()).isEmpty();
  }

  @Test
  void shouldUpdateMessage() {
    var newMessage = new NewMessage("this is a message");
    var createdMessage = postMessage(newMessage);

    var updatedMessage = new UpdatedMessage(createdMessage.getBody().getText() + " updated");

    updateMessage(createdMessage.getBody().getId(), updatedMessage);

    assertThat(getAllMessages().getBody().getMessages().get(0).getText()).isEqualTo(updatedMessage.getText());
  }

  @Test
  void shouldReturn404_whenUpdatingNonExistingMessage() {
    var updatedMessage = new UpdatedMessage("updated text");
    var requestBody = new HttpEntity<>(updatedMessage);

    var response = restTemplate.exchange(messagesBaseUrl + "/1337", HttpMethod.PUT, requestBody, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturn404_whenDeletingNonExistingMessage() {
    var response = restTemplate.exchange(messagesBaseUrl + "/1337", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<MessageDtoCollection> getAllMessages() {
    return restTemplate.getForEntity(messagesBaseUrl, MessageDtoCollection.class);
  }

  private ResponseEntity<MessageDto> postMessage(NewMessage newMessage) {
    var request = new HttpEntity<>(newMessage);
    var response = restTemplate.postForEntity(messagesBaseUrl, request, MessageDto.class);

    if (response.getBody() != null) {
      var createdId = response.getBody().getId();
      if (createdId != null) {
        createdMessageIds.add(createdId);
      }
    }

    return response;
  }

  private void deleteMessage(String messageId) {
    restTemplate.delete(messagesBaseUrl + "/" + messageId);
  }

  private void updateMessage(String messageId, UpdatedMessage updatedMessage) {
    var request = new HttpEntity<>(updatedMessage);
    restTemplate.put(messagesBaseUrl + "/" + messageId, request);
  }
}