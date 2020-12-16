package com.example.demo.enteties;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.ToString;

@Entity
@ToString
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends Utilisateur {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        Admin admin = (Admin) o;
        return admin.getEmail().equals(this.getEmail());
    }
}