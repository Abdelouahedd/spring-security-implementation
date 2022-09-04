package com.example.demo.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.jwt")
@Getter
@Setter
public class JwtConfig {
  private String secretKey;
  private String tokenPrefix;
  private Integer tokenExpirationAfterHours;
  private Integer refreshTokenExpirationAfterHour;
}
