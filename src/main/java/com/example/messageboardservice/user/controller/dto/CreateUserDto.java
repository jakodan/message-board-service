package com.example.messageboardservice.user.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Value;

@Value
public class CreateUserDto {

  private static final String VALID_USERNAME_REGEX = "^\\w{2,20}$";
  private static final String VALID_PASSWORD_REGEX = "^.{6,250}$";
  @Email @NotEmpty
  String email;
  @Pattern(regexp = VALID_USERNAME_REGEX) @NotNull
  String username;
  @Pattern(regexp = VALID_PASSWORD_REGEX) @NotNull
  String password;
}
