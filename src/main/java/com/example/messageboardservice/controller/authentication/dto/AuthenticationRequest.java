package com.example.messageboardservice.controller.authentication.dto;

import lombok.Value;

@Value
public class AuthenticationRequest {

  String username;
  String password;
}
