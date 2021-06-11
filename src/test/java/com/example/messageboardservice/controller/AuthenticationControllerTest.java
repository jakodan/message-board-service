package com.example.messageboardservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.controller.dto.AuthenticationRequest;
import com.example.messageboardservice.controller.dto.AuthenticationResponse;
import com.example.messageboardservice.controller.security.JwtUtil;
import java.util.regex.Pattern;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = {"test"})
class AuthenticationControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private JwtUtil jwtUtil;

  private String authenticationBaseUrl;

  @BeforeEach
  void setup() {
    authenticationBaseUrl = "http://localhost:" + port + "/api/v1/auth";
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
    assertThat(jwtUtil.validateAccessToken(response.getBody().getAccessToken())).isTrue();
    assertThat(response.getBody().getUsername()).isEqualTo("Bob");
  }

  @Test
  void shouldReturnBadRequest_whenInvalidBody() {
    var response = restTemplate.postForEntity(authenticationBaseUrl + "/token", HttpEntity.EMPTY, AuthenticationResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldRefreshToken() {
    var authenticationRequest = new AuthenticationRequest("Bob", "Bob");
    var httpRequest = new HttpEntity<>(authenticationRequest);

    var authResponse = restTemplate.postForEntity(authenticationBaseUrl + "/token", httpRequest, AuthenticationResponse.class);

    var refreshResponse = sendRefreshTokenRequest(authResponse);

    assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(jwtUtil.validateAccessToken(refreshResponse.getBody().getAccessToken())).isTrue();
    assertThat(refreshResponse.getBody().getUsername()).isEqualTo("Bob");
  }

  @Test
  void shouldSetRefreshTokenCookie() {
    var authenticationRequest = new AuthenticationRequest("Bob", "Bob");
    var httpRequest = new HttpEntity<>(authenticationRequest);

    var authResponse = restTemplate.postForEntity(authenticationBaseUrl + "/token", httpRequest, AuthenticationResponse.class);
    assertRefreshTokenCookieIsSet(authResponse);

    var refreshResponse = sendRefreshTokenRequest(authResponse);
    assertRefreshTokenCookieIsSet(refreshResponse);
  }

  @Test
  void shouldExpireCookie() {
    var response = restTemplate.exchange(authenticationBaseUrl + "/refresh", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

    assertThat(response.getHeaders().get("Set-Cookie").get(0))
        .isEqualTo("refresh_token=; Path=/; Max-Age=-1; SameSite=Strict; Secure; HttpOnly");
  }

  private ResponseEntity<AuthenticationResponse> sendRefreshTokenRequest(ResponseEntity<AuthenticationResponse> authenticationResponse) {
    var refreshToken = parseRefreshTokenFromResponse(authenticationResponse);
    var headers = new HttpHeaders();
    headers.set("Cookie", "refresh_token=" + refreshToken + ";");
    var refreshRequest = new HttpEntity<>(headers);
    return restTemplate.postForEntity(authenticationBaseUrl + "/refresh", refreshRequest, AuthenticationResponse.class);
  }

  private static void assertRefreshTokenCookieIsSet(ResponseEntity<AuthenticationResponse> authResponse) {
    var setCookieHeader = authResponse.getHeaders().get("Set-Cookie").get(0);
    var refreshToken = parseRefreshTokenFromResponse(authResponse);
    assertThat(setCookieHeader)
        .isEqualTo(String.format("refresh_token=%s; Path=/; Max-Age=31536000; SameSite=Strict; Secure; HttpOnly", refreshToken));
  }

  private static Stream<Arguments> invalidAuthenticationRequests() {
    return Stream.of(
        Arguments.of(new AuthenticationRequest(null, null)),
        Arguments.of(new AuthenticationRequest("username", null)),
        Arguments.of(new AuthenticationRequest(null, "password")),
        Arguments.of(new AuthenticationRequest("", ""))
    );
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