package de.hhu.rhinoshareapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long userID;

    private String name;

    private String surname;

    private String username;

    private String email;

    private String address;

    private int score;

    private String password;

    private String role;


    public ServiceUser(String name, String surname, String address ,String username, String email, String password, String role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.address = address;
        this.password = password;
        this.role = role;
        this.score = 0;
    }
}
