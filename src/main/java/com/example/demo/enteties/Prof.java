package com.example.demo.enteties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


import lombok.*;

@Entity
@Data
@ToString
@PrimaryKeyJoinColumn(name = "id")
public class Prof extends Utilisateur {

  private static final long serialVersionUID = -8413377990222373383L;

  @NotNull(message = "Le chemps cin est obligatoir !!")
  @NotBlank(message = "Le chemps cin est obligatoir !!")
  @Column(length = 10, nullable = false)
  private String cin;

}