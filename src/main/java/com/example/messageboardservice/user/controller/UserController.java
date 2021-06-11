package com.example.messageboardservice.user.controller;

import com.example.messageboardservice.controller.dto.AuthenticationResponse;
import com.example.messageboardservice.controller.security.JwtUtil;
import com.example.messageboardservice.user.controller.dto.CreateUserDto;
import com.example.messageboardservice.user.controller.dto.UserDto;
import com.example.messageboardservice.user.service.UserService;
import com.example.messageboardservice.user.service.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  public UserController(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping
  public ResponseEntity<AuthenticationResponse> createUser(@Validated @RequestBody CreateUserDto createUserDto) {
    try {
      var user = userService.createUser(createUserDto.getUsername(), createUserDto.getEmail(), createUserDto.getPassword());
      var userDto = UserDto.from(user);

      log.info("Created user {}", userDto);

      return generateAuthenticationResponse(userDto.getUsername());
    } catch (UserAlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  private ResponseEntity<AuthenticationResponse> generateAuthenticationResponse(String username) {
    var accessToken = jwtUtil.generateAccessToken(username);
    var refreshToken = jwtUtil.generateRefreshToken(username);

    var authenticationResponse = AuthenticationResponse.builder()
        .accessToken(accessToken)
        .username(username)
        .build();

    return ResponseEntity.status(HttpStatus.OK)
        .header("Set-Cookie", createRefreshTokenCookie(refreshToken))
        .body(authenticationResponse);
  }

  private String createRefreshTokenCookie(String refreshToken) {
    return String.format("refresh_token=%s; Path=/; Max-Age=31536000; SameSite=Strict; Secure; HttpOnly", refreshToken);
  }
}
