package com.example.messageboardservice.controller.dto;

import lombok.Value;

@Value
public class RefreshAuthenticationRequest {

  String refreshToken;
}
