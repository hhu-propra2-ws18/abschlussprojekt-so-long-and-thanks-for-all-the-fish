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

    public User(String username,String name, String email, boolean isAdmin){

        this.username=username;
        this.name=name;
        this.email=email;
        this.isAdmin=isAdmin;


    }
}
