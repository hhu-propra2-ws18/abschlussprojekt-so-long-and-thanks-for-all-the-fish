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
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    long userID;

    private String name;

    private String surname;

    public String username;

    private String email;

    @OneToOne (fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    private Address address;

    private int score;

    private String password;

    private String role;


    public Person(String name, String surname, Address address , String username, String email, String password, String role) {
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
