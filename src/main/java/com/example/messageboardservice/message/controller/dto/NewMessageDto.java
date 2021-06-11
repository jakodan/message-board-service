package com.example.messageboardservice.message.controller.dto;

import java.util.UUID;
import javax.validation.constraints.Size;
import lombok.Value;
import org.springframework.lang.NonNull;

@Value
public class NewMessageDto {

  @Size(min = 1, max = 250)
  String text;

  @NonNull
  UUID requestKey;

}
