package com.example.demo.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtil {

  @Autowired
  private JwtConfig config;

  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimResolve) {
    final Claims claims = extractAllClaims(token);
    return claimResolve.apply(claims);
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(config.getSecretKey()).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String genrateToken(UserDetails details) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles",details.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    return createToken(claims, details);
  }

  private String createToken(Map<String, Object> claims, UserDetails details) {
    return Jwts.builder().setClaims(claims).setSubject(details.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
    //  .claim("roles", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
      .setExpiration(
        new Date(System.currentTimeMillis() + config.getTokenExpirationAfterDays() * 60 * 60 * 10))
      .signWith(SignatureAlgorithm.HS256, config.getSecretKey()).compact();

  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String email = extractEmail(token);
    return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
