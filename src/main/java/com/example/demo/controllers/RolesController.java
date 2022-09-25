package com.example.demo.controllers;

import com.example.demo.enteties.Role;
import com.example.demo.repo.RoleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolesController {
  private final RoleRepository roleRepository;

  public RolesController(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @GetMapping
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping
  public Role addRole(@RequestBody String roleName) {
    Role role = new Role(roleName);
    roleRepository.saveAndFlush(role);
    return role;
  }
}
