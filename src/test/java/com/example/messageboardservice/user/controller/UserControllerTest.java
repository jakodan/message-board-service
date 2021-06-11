package com.example.messageboardservice.user.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.controller.dto.AuthenticationResponse;
import com.example.messageboardservice.controller.security.JwtUtil;
import com.example.messageboardservice.user.controller.dto.UserDto;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = {"test"})
class UserControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private JwtUtil jwtUtil;

  private String messagesBaseUrl;
  private HttpHeaders httpHeaders;

  private static int counter = 0;
  private String username;

  @BeforeEach
  void setUp() {
    counter += 1;
    username = "jakob" + counter;
    messagesBaseUrl = "http://localhost:" + port + "/api/v1/user";
    httpHeaders = new HttpHeaders();
    httpHeaders.set("Content-Type", "application/json");
  }

  @Test
  void shouldCreateUser() {
    var postBody = "{\"username\":\"" + username + "\", \"email\":\"jakob@example.com\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode().is2xxSuccessful());
    assertThat(jwtUtil.validateAccessToken(response.getBody().getAccessToken())).isTrue();
    assertThat(response.getBody().getUsername()).isEqualTo(username);
  }

  @Test
  void shouldSetRefreshToken() {
    var postBody = "{\"username\":\"" + username + "\", \"email\":\"jakob@example.com\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertRefreshTokenCookieIsSet(response);
  }

  @Test
  void shouldReturnConflict_whenUserAlreadyExists() {
    var postBody = "{\"username\":\"jakob\", \"email\":\"jakob@example.com\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, UserDto.class);
    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @ParameterizedTest
  @ValueSource(strings = {"x", "jakob", "@example", "@", "j akob@example.com"})
  @NullAndEmptySource
  void shouldReturnBadRequest_whenInvalidEmail(String invalidEmail) {
    var postBody = "{\"username\":\"jakob\", \"email\":\"" + invalidEmail + "\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @ParameterizedTest
  @ValueSource(strings = {"x", "%%%%%%%%"})
  @EmptySource
  void shouldReturnBadRequest_whenInvalidUsername(String invalidUsername) {
    var postBody = "{\"username\":\"" + invalidUsername + "\", \"email\":\"jakob@example.com\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenNullUsername() {
    var postBody = "{\"email\":\"jakob@example.com\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenNullEmail() {
    var postBody = "{\"username\":\"jakob\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenNullPassword() {
    var postBody = "{\"username\":\"jakob\",\"email\":\"jakob@example.com\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenTooShortPassword() {
    var postBody = "{\"username\":\"jakob\",\"email\":\"jakob@example.com\",\"password\":\"xxxxx\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenTooLongPassword() {
    var tooLongPassword = StringUtils.repeat("m", 251);
    var postBody = "{\"username\":\"jakob\",\"email\":\"jakob@example.com\",\"password\":\"" + tooLongPassword + "\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenTooLongUsername() {
    var tooLongUsername = StringUtils.repeat("m", 40);
    var postBody = "{\"username\":\"" + tooLongUsername + "\",\"email\":\"jakob@example.com\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenTooLongEmail() {
    var tooLongEmail = "jakob@" + StringUtils.repeat("hej", 100) + ".com";
    var postBody = "{\"username\":\"jakob\",\"email\":\"" + tooLongEmail + "\",\"password\":\"password123\"}";

    var request = new HttpEntity<>(postBody, httpHeaders);

    var response = restTemplate.exchange(messagesBaseUrl, HttpMethod.POST, request, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private static void assertRefreshTokenCookieIsSet(ResponseEntity<AuthenticationResponse> authResponse) {
    var setCookieHeader = authResponse.getHeaders().get("Set-Cookie").get(0);
    var refreshToken = parseRefreshTokenFromResponse(authResponse);
    assertThat(setCookieHeader)
        .isEqualTo(String.format("refresh_token=%s; Path=/; Max-Age=31536000; SameSite=Strict; Secure; HttpOnly", refreshToken));
  }

  private static String parseRefreshTokenFromResponse(ResponseEntity<AuthenticationResponse> response) {
    var setCookieHeader = response.getHeaders().get("Set-Cookie").get(0);
    var pattern = Pattern.compile("^refresh_token=(?<refreshToken>.+); Path=/.+$");
    var matcher = pattern.matcher(setCookieHeader);
    if (matcher.matches()) {
      return matcher.group("refreshToken");
    } else {
      throw new AssertionError("Set-Cookie header didn't match regex");
    }
  }
}