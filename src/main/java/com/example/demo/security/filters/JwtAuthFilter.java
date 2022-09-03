package com.example.demo.security.filters;

import com.example.demo.security.jwt.JwtConfig;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.security.services.ApplicationUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Log4j2
public class JwtAuthFilter extends OncePerRequestFilter {

  @Autowired
  private ApplicationUserService userDetails;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private JwtConfig jwtConfig;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    String jwt = null;
    final String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
    if (authorizationHeader != null && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
      try {
        jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractEmail(jwt);
        Claims claims = jwtUtil.extractAllClaims(jwt);
        List<String> roles = claims.get("roles", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = this.userDetails.loadUserByUsername(email);
          if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
              userDetails, authorities, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
          }
        }
        filterChain.doFilter(request, response);
      } catch (Exception e) {
        log.error(String.format("Error auth token: %s", jwt), e);
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", e.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }

    } else {
      filterChain.doFilter(request, response);
    }

  }

}
