package com.example.demo.security.services;

import com.example.demo.enteties.Role;
import com.example.demo.enteties.User;
import com.example.demo.repo.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public record ApplicationUserService(UserRepository repo) implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = this.repo.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s not found", email)));
    Set<Role> roles = user.getRoles();
    List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
      .map(r -> new SimpleGrantedAuthority("ROLE_"+r.getRoleName().toUpperCase()))
      .toList();
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
  }

}
