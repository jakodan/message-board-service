package com.example.messageboardservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messageboardservice.controller.message.RestMessageController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

  @Autowired
  private RestMessageController controller;

  @Test
  void contextLoads() {
    assertThat(controller).isNotNull();
  }
}
