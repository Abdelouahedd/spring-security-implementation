package com.example.demo.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableConfigurationProperties(JwtConfig.class)
@Log4j2
public class JwtUtil {
  private final JwtConfig jwtConfig;

  public JwtUtil(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public UsernamePasswordAuthenticationToken parseToken(String token) throws JOSEException, BadJOSEException, ParseException {
    log.info("Start to parse token");
    byte[] secretKey = jwtConfig.getSecretKey().getBytes();
    SignedJWT signedJWT = SignedJWT.parse(token);
    signedJWT.verify(new MACVerifier(secretKey));
    ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

    JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.HS256, new ImmutableSecret<>(secretKey));
    jwtProcessor.setJWSKeySelector(keySelector);
    jwtProcessor.process(signedJWT, null);
    JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
    String username = claims.getSubject();
    List<String> roles = (List<String>) claims.getClaim("roles");
    List<SimpleGrantedAuthority> authorities = roles.stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());
    return new UsernamePasswordAuthenticationToken(username, null, authorities);
  }

  public String genrateToken(UserDetails details) {
    try {
      log.info("Start to generate token");
      JWTClaimsSet claims = new JWTClaimsSet.Builder()
        .subject(details.getUsername())
        //.issuer(issuer)
        .claim("roles", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .expirationTime(Date.from(Instant.now().plusSeconds(jwtConfig.getTokenExpirationAfterHours() * 3_600L)))
        .issueTime(new Date())
        .build();

      Payload payload = new Payload(claims.toJSONObject());

      JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256),
        payload);

      jwsObject.sign(new MACSigner(jwtConfig.getSecretKey()));
      log.info("sign token by signer");
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error(String.format("Error to create JWT %s", e.getMessage()));
      throw new RuntimeException(String.format("Error to create JWT %s", e.getMessage()));
    }
  }
}

