package com.example.messageboardservice.config;

import com.example.messageboardservice.controller.security.JwtRequestFilter;
import com.example.messageboardservice.service.UserService;
import java.security.SecureRandom;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final int BCRYPT_STRENGTH = 10;
  private final UserService userService;
  private final JwtRequestFilter jwtRequestFilter;

  public SecurityConfig(UserService userService,
      JwtRequestFilter jwtRequestFilter) {
    this.userService = userService;
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/auth/token").permitAll()
        .antMatchers(HttpMethod.GET, "/messages").permitAll()
        .anyRequest().authenticated()
        .and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http = http
        .exceptionHandling()
        .authenticationEntryPoint(
            (request, response, ex) -> {
              response.sendError(
                  HttpServletResponse.SC_UNAUTHORIZED, //return 401 instead of 403 in cases where Authorization header is invalid or missing
                  ex.getMessage()
              );
            }
        )
        .and();

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Primary
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(BCRYPT_STRENGTH, new SecureRandom());
  }

  @Bean(name = "myAuthenticationManager")
  @Override
  @SneakyThrows
  public AuthenticationManager authenticationManagerBean() {
    return super.authenticationManagerBean();
  }

}
