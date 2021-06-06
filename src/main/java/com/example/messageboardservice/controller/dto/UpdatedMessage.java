package com.example.messageboardservice.controller.dto;

import javax.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdatedMessage {

  @Size(min = 1, max = 250)
  String text;
}
