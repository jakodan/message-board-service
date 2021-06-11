package com.example.messageboardservice.user.controller.dto;

import com.example.messageboardservice.user.service.model.MessageUser;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {

  String username;

  public static UserDto from(MessageUser domainUser) {
    return UserDto.builder()
        .username(domainUser.getUsername())
        .build();
  }
}
