package com.example.demo.enteties;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@PrimaryKeyJoinColumn(name = "id")
public class Etudiant extends Utilisateur {
  private static final long serialVersionUID = 8422167425794132721L;

  @NotBlank(message = "Le chemp CNE est obligatoir !!")
  @NotNull(message = "Le chemp CNE est obligatoir !!")
  @Column(length = 50, nullable = false, unique = true)
  private String cne;

}