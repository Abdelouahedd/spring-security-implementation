package com.example.demo.security.services;

import java.util.Collection;
import java.util.HashSet;

import com.example.demo.enteties.Utilisateur;
import com.example.demo.repo.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur user = this.repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s not found", email)));
        Collection<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

}
