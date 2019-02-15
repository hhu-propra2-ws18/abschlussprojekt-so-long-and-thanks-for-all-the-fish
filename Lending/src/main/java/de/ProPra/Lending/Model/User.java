package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User {
    @Id
    long userID;
    String name;
    double backBalance;
    int score;

    public User(){}
    public User(long userID, String name) {
        this.userID = userID;
        this.name = name;
    }
}
