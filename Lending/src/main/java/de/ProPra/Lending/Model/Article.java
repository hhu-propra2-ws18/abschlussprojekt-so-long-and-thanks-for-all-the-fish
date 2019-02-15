package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Calendar;

@Data
@Entity
public class Article {
    @Id
    long articleID;
    String name;
    String comment;
    @OneToOne
    User ownerUser;
    double deposit;
    double rent;
    boolean available;
    Calendar finalStartDate;
    Calendar finalEndDate;
    @OneToOne
    User lendingUser;
    boolean isRequested;
    String requestComment;


    public Article(){}
    public Article(long articleID, String name, String comment, double deposit, double rent, boolean available, User ownerUser) {
        this.articleID = articleID;
        this.name = name;
        this.comment = comment;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
        this.ownerUser = ownerUser;
    }
}
