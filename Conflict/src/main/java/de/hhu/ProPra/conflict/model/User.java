package de.hhu.ProPra.conflict.model;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private long id;

    private String username;
    private String name;
    private String password;
    private int score = 0;
    private String email;
    private boolean isAdmin;
    private double bankBalance = 0;
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
}
