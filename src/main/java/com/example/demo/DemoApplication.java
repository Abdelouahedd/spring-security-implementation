package com.example.demo;

import com.example.demo.enteties.Admin;
import com.example.demo.enteties.Prof;
import com.example.demo.enteties.Utilisateur;
import com.example.demo.repo.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
  @Bean
  CommandLineRunner run(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      Utilisateur admin = new Admin();
      admin.setEmail("Admin@hotmail.com");
      admin.setRole(Roles.ADMIN);
      admin.setNom("Admin");
      admin.setPrenom("Administrator");
      admin.setPassword(passwordEncoder.encode("admin1234"));
      Prof prof = new Prof();
      prof.setEmail("prof@hotmail.com");
      prof.setRole(Roles.PROF);
      prof.setNom("prof");
      prof.setPrenom("professor");
      prof.setPassword(passwordEncoder.encode("prof1234"));
      prof.setCin("ae1234");
      utilisateurRepository.saveAll(Arrays.asList(admin, prof));
    };
  }
}
