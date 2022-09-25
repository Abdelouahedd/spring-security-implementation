package com.example.demo.controllers;

import com.example.demo.enteties.User;
import com.example.demo.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class UsersController {
  private UserRepository repository;

  @GetMapping(value = "/")
  public String getMethodName(Principal principal) {
    String name = principal.getName();
    return "Hello " + name;
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/api/users")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = repository.findAll();
    return ResponseEntity.ok(users);
  }

}
