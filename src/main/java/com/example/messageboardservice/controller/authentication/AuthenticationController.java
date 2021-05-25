package com.example.messageboardservice.controller.authentication;

import com.example.messageboardservice.config.JwtUtil;
import com.example.messageboardservice.controller.authentication.dto.AuthenticationRequest;
import com.example.messageboardservice.controller.authentication.dto.AuthenticationResponse;
import com.example.messageboardservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public AuthenticationController(AuthenticationManager authenticationManager,
      UserRepository userRepository, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
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

    var userDetails = userRepository.loadUserByUsername(request.getUsername());
    var jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}
