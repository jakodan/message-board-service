package com.example.messageboardservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestMessageControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private String messagesBaseUrl;

  @BeforeEach
  void setup() {
    messagesBaseUrl = "http://localhost:" + port + "/messages";
  }

  @Test
  void getMessagesShouldReturnNotImplemented() {
    var result = this.restTemplate.exchange(messagesBaseUrl, HttpMethod.GET, HttpEntity.EMPTY, String.class);

    assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_IMPLEMENTED);
  }

  @Test
  void postMessageShouldReturnCreatedStatus() {
    var result = this.restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, HttpEntity.EMPTY, String.class);

    assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
  }

  @Test
  void postMessageShouldReturnLocationHeader() {
    var result = this.restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, HttpEntity.EMPTY, String.class);
    var locationHeaders = result.getHeaders().get(HttpHeaders.LOCATION);

    assertThat(locationHeaders).hasSize(1);
    assertThat(locationHeaders.get(0)).matches("^http://localhost:8080/messages/[0-9]{1,10}$");
  }
}