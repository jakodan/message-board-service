package com.example.messageboardservice.controller.dto;

import lombok.Value;

@Value
public class AuthenticationRequest {

  String username;
  String password;
}
