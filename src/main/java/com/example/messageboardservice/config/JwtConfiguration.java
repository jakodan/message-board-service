package com.example.messageboardservice.config;

import com.example.messageboardservice.controller.security.JwtRequestFilter;
import com.example.messageboardservice.controller.security.JwtUtil;
import com.example.messageboardservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

  @Bean
  JwtRequestFilter jwtRequestFilter(UserRepository userRepository, JwtUtil jwtUtil) {
    return new JwtRequestFilter(userRepository, jwtUtil);
  }

  @Bean
  JwtUtil jwtUtil(@Value("${jwt.secret-key}") String secretKey) {
    return new JwtUtil(secretKey);
  }
}
