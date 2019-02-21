package de.ProPra.Lending.Model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Builder
@Data
@Entity
public class ServiceUser {
    @Id
    long userID;
    String name;
    int score;

    public ServiceUser(){}

    public ServiceUser(long userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public ServiceUser(long userID, String name, int score) {
        this.userID = userID;
        this.name = name;
        this.score = score;
    }
}
