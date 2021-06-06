package com.example.messageboardservice.controller;

import com.example.messageboardservice.controller.dto.AuthenticationRequest;
import com.example.messageboardservice.controller.dto.AuthenticationResponse;
import com.example.messageboardservice.controller.security.JwtUtil;
import com.example.messageboardservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping(
    path = "/auth",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class RESTAuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtUtil jwtUtil;

  public RESTAuthenticationController(AuthenticationManager authenticationManager,
      UserService userService, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/token")
  public ResponseEntity<AuthenticationResponse> postAuthentication(@RequestBody AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getUsername(),
              request.getPassword()));
    } catch (BadCredentialsException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    return generateAuthenticationResponse(request.getUsername());

  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthenticationResponse> refreshAuthentication(
      @CookieValue(name = "refresh_token", required = false) String refreshTokenCookie) {
    if (jwtUtil.validateRefreshToken(refreshTokenCookie)) {
      var username = jwtUtil.extractUsername(refreshTokenCookie);

      return generateAuthenticationResponse(username);
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  @DeleteMapping("/refresh")
  public ResponseEntity<Void> deleteRefreshToken() {
    return ResponseEntity.status(HttpStatus.OK)
        .header("Set-Cookie", createExpiredRefreshTokenCookie())
        .build();
  }

  private ResponseEntity<AuthenticationResponse> generateAuthenticationResponse(String username) {
    var accessToken = jwtUtil.generateAccessToken(username);
    var refreshToken = jwtUtil.generateRefreshToken(username);

    var authenticationResponse = AuthenticationResponse.builder()
        .accessToken(accessToken)
        .build();

    return ResponseEntity.status(HttpStatus.OK)
        .header("Set-Cookie", createRefreshTokenCookie(refreshToken))
        .body(authenticationResponse);
  }

  private String createRefreshTokenCookie(String refreshToken) {
    return String.format("refresh_token=%s; Path=/; Max-Age=31536000; SameSite=Strict; Secure; HttpOnly", refreshToken);
  }

  private String createExpiredRefreshTokenCookie() {
    return "refresh_token=; Path=/; Max-Age=-1; SameSite=Strict; Secure; HttpOnly";
  }
}
