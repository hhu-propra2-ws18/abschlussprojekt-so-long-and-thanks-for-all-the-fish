package de.hhu.ProPra.conflict.model;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Article {

    @Id
    @GeneratedValue
    long articleID;

    String name;

    String comment;

    double deposit;

    double rent;

    boolean available;

    @OneToOne
    User ownerUser;

    @OneToOne
    User lendingUser;

    public Article(){
    }


    public Article(String name, String comment, double deposit, double rent, boolean available, User ownerUser) {
        this.name = name;
        this.comment = comment;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
        this.ownerUser = ownerUser;
    }
}
