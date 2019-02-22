package de.hhu.rhinoshareapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long userID;

    private String name;

    private String surname;

    private String username;

    private String email;

    @OneToOne
    private Address address;

    private int score;

    private String password;

    private String role;


    public User(String name, String surname, Address address , String username, String email, String password, String role) {
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
