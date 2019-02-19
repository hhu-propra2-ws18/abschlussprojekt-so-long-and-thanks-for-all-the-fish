package de.jacquespasquier.loginservice.security.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class ServiceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String username;

    private String password;

    private String role;


    public ServiceUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ServiceUser() {

    }

}
