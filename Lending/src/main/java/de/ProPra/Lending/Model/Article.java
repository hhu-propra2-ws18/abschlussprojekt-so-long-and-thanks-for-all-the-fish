package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Article {
    @Id
    long articleID;
    String name;
    String comment;
    long personID;
    @OneToOne
    Person ownerPerson;
    double deposit;
    double rent;
    boolean available;

    public Article(){}
    public Article(long articleID, String name, String comment, long personID, double deposit, double rent, boolean available) {
        this.articleID = articleID;
        this.name = name;
        this.comment = comment;
        this.personID = personID;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
    }
}
