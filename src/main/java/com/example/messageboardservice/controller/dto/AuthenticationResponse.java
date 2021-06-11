package com.example.messageboardservice.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthenticationResponse {

  String username;
  String accessToken;
}
