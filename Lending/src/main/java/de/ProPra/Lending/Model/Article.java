package de.ProPra.Lending.Model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Calendar;

@Builder
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

    public Article(long articleID, String name, String comment, User ownerUser, double deposit, double rent, boolean available, Calendar finalStartDate, Calendar finalEndDate, User lendingUser, boolean isRequested, String requestComment) {
        this.articleID = articleID;
        this.name = name;
        this.comment = comment;
        this.ownerUser = ownerUser;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
        this.finalStartDate = finalStartDate;
        this.finalEndDate = finalEndDate;
        this.lendingUser = lendingUser;
        this.isRequested = isRequested;
        this.requestComment = requestComment;
    }
}
