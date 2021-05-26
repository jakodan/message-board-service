package com.example.messageboardservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.controller.dto.AuthenticationRequest;
import com.example.messageboardservice.controller.dto.AuthenticationResponse;
import com.example.messageboardservice.controller.security.JwtUtil;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = {"test"})
class RESTAuthenticationControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private JwtUtil jwtUtil;

  private String authenticationBaseUrl;

  @BeforeEach
  void setup() {
    authenticationBaseUrl = "http://localhost:" + port + "/auth";
  }

  @ParameterizedTest
  @MethodSource("invalidAuthenticationRequests")
  void shouldReturnUnauthorized(AuthenticationRequest authenticationRequest) {
    var httpRequest = new HttpEntity<>(authenticationRequest);

    var response = restTemplate.postForEntity(authenticationBaseUrl + "/token", httpRequest, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void shouldReturnOkForTestUser() {
    var authenticationRequest = new AuthenticationRequest("Bob", "Bob");
    var httpRequest = new HttpEntity<>(authenticationRequest);

    var response = restTemplate.postForEntity(authenticationBaseUrl + "/token", httpRequest, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var jwt = response.getBody().getJwt();
    assertThat(jwt).isNotEmpty();
    assertThat(jwtUtil.validate(jwt)).isTrue();
  }

  @Test
  void shouldReturnBadRequest_whenInvalidBody() {
    var response = restTemplate.postForEntity(authenticationBaseUrl + "/token", HttpEntity.EMPTY, AuthenticationResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private static Stream<Arguments> invalidAuthenticationRequests() {
    return Stream.of(
        Arguments.of(new AuthenticationRequest(null, null)),
        Arguments.of(new AuthenticationRequest("username", null)),
        Arguments.of(new AuthenticationRequest(null, "password")),
        Arguments.of(new AuthenticationRequest("", ""))
    );
  }

}