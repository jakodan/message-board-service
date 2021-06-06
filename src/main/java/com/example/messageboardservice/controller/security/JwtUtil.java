package com.example.messageboardservice.controller.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

  private final String secretKey;

  public JwtUtil(String secretKey) {
    this.secretKey = secretKey;
  }

  public String generateAccessToken(String username) {
    var claims = Jwts.claims().setSubject(username);
    claims.put("scopes", List.of(Scopes.ACCESS));
    return createToken(claims, Duration.ofMinutes(30));
  }

  public String generateRefreshToken(String username) {
    var claims = Jwts.claims().setSubject(username);
    claims.put("scopes", List.of(Scopes.REFRESH_TOKEN));
    return createToken(claims, Duration.ofDays(365));
  }

  public boolean validateAccessToken(String token) {
    try {
      var claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      if (((List<Scopes>) claims.getBody().get("scopes")).contains(Scopes.ACCESS)) {
        throw new RuntimeException("Scopes did not contain ACCESS.");
      }
      return true;
    } catch (RuntimeException e) {
      log.error("Invalid JWT", e);
      return false;
    }
  }

  public boolean validateRefreshToken(String token) {
    try {
      var claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      if (((List<Scopes>) claims.getBody().get("scopes")).contains(Scopes.REFRESH_TOKEN)) {
        throw new RuntimeException("Scopes did not contain REFRESH_TOKEN.");
      }
      return true;
    } catch (RuntimeException e) {
      log.error("Invalid JWT", e);
      return false;
    }
  }

  private String createToken(Map<String, Object> claims, TemporalAmount expirationTime) {
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plus(expirationTime)))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public String extractUsername(String jwt) {
    return extractClaim(jwt, Claims::getSubject);
  }

  private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
    var claims = extractAllClaims(jwt);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String jwt) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
  }
}
