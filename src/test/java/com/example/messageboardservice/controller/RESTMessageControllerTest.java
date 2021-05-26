package com.example.messageboardservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.controller.dto.AuthenticationRequest;
import com.example.messageboardservice.controller.dto.AuthenticationResponse;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RESTMessageControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;
  private MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

  private String messagesBaseUrl;
  private String authenticationBaseUrl;

  private List<String> createdMessageIds;

  @BeforeEach
  void setup() {
    createdMessageIds = new ArrayList<>();
    messagesBaseUrl = "http://localhost:" + port + "/messages";
    authenticationBaseUrl = "http://localhost:" + port + "/auth";
    setupAuthentication("Bob", "Bob");
  }

  private void setupAuthentication(String username, String password) {
    headers.clear();
    var authenticationRequest = new AuthenticationRequest(username, password);
    var request = new HttpEntity<>(authenticationRequest);
    var response = restTemplate.postForEntity(authenticationBaseUrl + "/token", request, AuthenticationResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    headers.add("Authorization", "Bearer " + response.getBody().getJwt());
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
    headers.clear(); //no authentication required
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
    var requestBody = new HttpEntity<>(updatedMessage, headers);

    var response = restTemplate.exchange(messagesBaseUrl + "/1337", HttpMethod.PUT, requestBody, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturn404_whenDeletingNonExistingMessage() {
    var request = new HttpEntity<>(headers);
    var response = restTemplate.exchange(messagesBaseUrl + "/1337", HttpMethod.DELETE, request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturn401_whenUserNotResourceOwner_whenDeleting() {
    setupAuthentication("Bob", "Bob");
    var message = new NewMessage("new message");
    var createdMessage = postMessage(message);
    setupAuthentication("Alice", "Alice");
    var response = deleteMessage(createdMessage.getBody().getId());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    setupAuthentication("Bob", "Bob");
  }

  @Test
  void shouldReturn401_whenUserNotResourceOwner_whenUpdating() {
    setupAuthentication("Bob", "Bob");
    var message = new NewMessage("new message");
    var createdMessage = postMessage(message);
    setupAuthentication("Alice", "Alice");
    var response = updateMessage(createdMessage.getBody().getId(), new UpdatedMessage("updatedMessage"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    setupAuthentication("Bob", "Bob");
  }

  @Test
  void shouldAllowDeletion_afterReAuthentication() {
    setupAuthentication("Bob", "Bob");
    var message = new NewMessage("new message");
    var createdMessage = postMessage(message);
    setupAuthentication("Bob", "Bob"); //setup authentication again, will generate new jwt

    var response = deleteMessage(createdMessage.getBody().getId());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void shouldAllowUpdate_afterReAuthentication() {
    setupAuthentication("Bob", "Bob");
    var message = new NewMessage("new message");
    var createdMessage = postMessage(message);
    setupAuthentication("Bob", "Bob"); //setup authentication again, will generate new jwt

    var response = updateMessage(createdMessage.getBody().getId(), new UpdatedMessage("updatedMessage"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void shouldReturnUnauthorized_whenNoAuthorizationHeader_whenPosting() {
    headers.clear();

    var message = new NewMessage("new message");
    var response = postMessage(message);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void shouldReturnUnauthorized_whenNoAuthorizationHeader_whenDeleting() {
    headers.clear();

    var response = deleteMessage("messageId");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void shouldReturnUnauthorized_whenNoAuthorizationHeader_whenUpdating() {
    headers.clear();

    var response = updateMessage("messageId", new UpdatedMessage("text"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void shouldReturnUnauthorized_whenInvalidJwtToken_whenPosting() {
    headers.clear();
    headers.add("Authorization", "invalid");

    var response = postMessage(new NewMessage("text"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void shouldReturnUnauthorized_whenInvalidJwtToken_whenUpdating() {
    headers.clear();
    headers.add("Authorization", "invalid");

    var response = updateMessage("messageId", new UpdatedMessage("text"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void shouldReturnUnauthorized_whenInvalidJwtToken_whenDeleting() {
    headers.clear();
    headers.add("Authorization", "invalid");

    var response = deleteMessage("messageId");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void shouldIgnoreInvalidAuthorizationToken_whenGettingAllMessages() {
    headers.clear();
    headers.add("Authorization", "invalid");

    var response = getAllMessages();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  private ResponseEntity<MessageDtoCollection> getAllMessages() {
    return restTemplate.getForEntity(messagesBaseUrl, MessageDtoCollection.class);
  }

  private ResponseEntity<MessageDto> postMessage(NewMessage newMessage) {
    var request = new HttpEntity<>(newMessage, headers);
    var response = restTemplate.postForEntity(messagesBaseUrl, request, MessageDto.class);

    if (response.getBody() != null) {
      var createdId = response.getBody().getId();
      if (createdId != null) {
        createdMessageIds.add(createdId);
      }
    }

    return response;
  }

  private ResponseEntity<String> deleteMessage(String messageId) {
    var request = new HttpEntity<>(headers);
    return restTemplate.exchange(messagesBaseUrl + "/" + messageId, HttpMethod.DELETE, request, String.class);
  }

  private ResponseEntity<String> updateMessage(String messageId, UpdatedMessage updatedMessage) {
    var request = new HttpEntity<>(updatedMessage, headers);
    return restTemplate.exchange(messagesBaseUrl + "/" + messageId, HttpMethod.PUT, request, String.class);
  }
}