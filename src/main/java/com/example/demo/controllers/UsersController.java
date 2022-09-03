package com.example.demo.controllers;

import com.example.demo.enteties.Utilisateur;
import com.example.demo.repo.UtilisateurRepository;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.security.services.ApplicationUserService;
import com.example.demo.security.services.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UsersController {
  private UtilisateurRepository repository;

  @GetMapping(value = "/")
  public String getMethodName() {
    return "Hello World";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/api/users")
  public ResponseEntity<List<Utilisateur>> getAllUsers() {
    List<Utilisateur> utilisateurs = repository.findAll();
    return ResponseEntity.ok(utilisateurs);
  }

}
