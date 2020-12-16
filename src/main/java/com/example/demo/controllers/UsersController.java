package com.example.demo.controllers;

import com.example.demo.repo.UtilisateurRepository;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.security.services.ApplicationUserService;
import com.example.demo.security.services.AuthResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UsersController {
    @Autowired
    private UtilisateurRepository repository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ApplicationUserService applicationUserService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(value = "/")
    public String getMethodName() {
        return "Hello World";
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginForm login) throws Exception {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect email or password ", e);
        }
        final UserDetails userDetails = applicationUserService.loadUserByUsername(login.getEmail());
        final String jwt = jwtUtil.genrateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

}

@Data
class LoginForm {
    private String email;
    private String password;
}