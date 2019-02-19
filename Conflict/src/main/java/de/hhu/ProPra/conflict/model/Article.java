package de.hhu.ProPra.conflict.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class Article {

    @Id
    @GeneratedValue
    long articleID;

    String name;

    String comment;

    //@OneToOne
    long personID;

    double deposit;

    double rent;

    boolean available;


    public Article(){
    }

    public Article(String name, String comment, int personID, double deposit, double rent, boolean available){
        this.name = name;
        this.comment = comment;
        this.personID = personID;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
    }
}
