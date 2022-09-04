package com.example.demo.security;

import com.example.demo.security.filters.CustomAuthenticationFilter;
import com.example.demo.security.filters.JwtAuthFilter;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.security.services.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  private final JwtUtil jwtUtil;
  private ApplicationUserService userDetailsService;
  private JwtAuthFilter jwtAuthFilter;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain webFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http
      .csrf()
      .disable()
      .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .headers()
      .frameOptions()
      .disable()
      .and()
      .authorizeHttpRequests(auth ->
        auth.antMatchers("/login", "/h2-console/**").permitAll()
          .anyRequest().authenticated()
      )
      .addFilter(new CustomAuthenticationFilter(authenticationManager, jwtUtil, userDetailsService))
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
      .headers().cacheControl();

    return http.build();
  }
}
