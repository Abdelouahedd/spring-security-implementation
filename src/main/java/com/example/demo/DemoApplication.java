package com.example.demo;

import com.example.demo.enteties.Etudiant;
import com.example.demo.enteties.Prof;
import com.example.demo.enteties.Utilisateur;
import com.example.demo.repo.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private UtilisateurRepository repository;
	@Autowired
	private PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Prof pr = new Prof();
		pr.setEmail("email@gmail.com");
		pr.setCin("EB&É55");
		pr.setNom("nomnom");
		pr.setPrenom("prenom");
		pr.setRole(Roles.PROF);
		pr.setPassword(encoder.encode("password"));

		Prof p = repository.save(pr);
		System.out.println("Prof added --> " + p);

		Etudiant et = new Etudiant();
		et.setCne("G138325292");
		et.setNom("ennouri");
		et.setPrenom("Abdo");
		et.setEmail("en@gmail.com");
		et.setPassword(encoder.encode("password"));
		et.setRole(Roles.ETUDIANT);
		Etudiant e = repository.save(et);
		System.out.println("Etudiant added --> " + e);
		Utilisateur u = repository.findById((long) 2).get();

		System.out.println("get user " + u);
		System.out.println("email user " + u.getEmail());
		System.out.println("pass user " + u.getPassword());
	}

}
