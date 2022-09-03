package com.example.demo.security.filters;

import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.security.services.ApplicationUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final String BAD_CREDENTIAL_MESSAGE = "Authentication failed for username: %s and password: %s";

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final ApplicationUserService applicationUserService;



  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {

    String email = null;
    String password = null;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, String> map = objectMapper.readValue(request.getInputStream(), Map.class);
      email = map.get("email");
      password = map.get("password");
      log.debug("Login with username: {}", email);
      return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    } catch (AuthenticationException e) {
      log.error(String.format(BAD_CREDENTIAL_MESSAGE, email, password), e);
      throw e;
    } catch (Exception e) {
      response.setStatus(INTERNAL_SERVER_ERROR.value());
      Map<String, String> error = new HashMap<>();
      error.put("errorMessage", e.getMessage());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      new ObjectMapper().writeValue(response.getOutputStream(), error);
      throw new RuntimeException(String.format("Error in attemptAuthentication with username %s and password %s", email, password), e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authentication) throws IOException, ServletException {
    User user = (User) authentication.getPrincipal();
    final UserDetails userDetails = applicationUserService.loadUserByUsername(user.getUsername());
    String accessToken = jwtUtil.genrateToken(userDetails);

    //String refreshToken = JwtUtil.createRefreshToken(user.getUsername());
    response.addHeader("access_token", accessToken);
    //response.addHeader("refresh_token", refreshToken);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> error = new HashMap<>();
    error.put("errorMessage", "Bad credentials");
    response.setContentType(APPLICATION_JSON_VALUE);
    mapper.writeValue(response.getOutputStream(), error);
  }
}
