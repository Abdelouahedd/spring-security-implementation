package com.example.demo;

import com.example.demo.enteties.Role;
import com.example.demo.enteties.User;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  CommandLineRunner run(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    return args -> {

      Role userRole = new Role("User");
      Role adminRole = new Role("Admin");
      Role defaultRole = new Role("Default");
      roleRepository.saveAll(Arrays.asList(userRole, adminRole, defaultRole));

      User admin = new User();
      admin.setFirstName("Admin");
      admin.setLastName("Administrator");
      admin.setUserName("Admin-app");
      admin.setEmail("Admin@hotmail.com");
      admin.setPassword(passwordEncoder.encode("admin1234"));
      admin.setRoles(Set.of(defaultRole, adminRole, userRole));

      User user = new User();
      user.setFirstName("User");
      user.setLastName("user");
      user.setUserName("user-app");
      user.setEmail("user@hotmail.com");
      user.setPassword(passwordEncoder.encode("user1234"));
      user.setRoles(Set.of(defaultRole, userRole));

      userRepository.saveAll(Arrays.asList(admin, user));
    };
  }
}
