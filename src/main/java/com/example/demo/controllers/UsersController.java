package com.example.demo.controllers;

import com.example.demo.enteties.Utilisateur;
import com.example.demo.repo.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/api/users")
  public ResponseEntity<List<Utilisateur>> getAllUsers() {
    List<Utilisateur> utilisateurs = repository.findAll();
    return ResponseEntity.ok(utilisateurs);
  }

}
