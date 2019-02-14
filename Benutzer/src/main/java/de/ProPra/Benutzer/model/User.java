package de.ProPra.Benutzer.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private long id;

    private String username;
    private String name;
    private int score;
    private String email;
    private boolean isAdmin;
    private double bankBalance;
    private String address;

    public User(String username, String name, String email, boolean isAdmin, String address) {
        this.address = address;
        this.username = username;
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public User() {
    }

    ;
}
