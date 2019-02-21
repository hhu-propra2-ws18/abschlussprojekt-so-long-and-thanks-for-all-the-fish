package de.ProPra.Lending.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Calendar;

@Builder
@Data
@Entity
@AllArgsConstructor
public class Article {
    @Id
    long articleID;
    String name;
    String comment;
    @OneToOne
    ServiceUser ownerServiceUser;
    double deposit;
    double rent;
    boolean available;
    Calendar finalStartDate;
    Calendar finalEndDate;
    boolean isRequested;
    String requestComment;


    public Article(){}
    public Article(long articleID, String name, String comment, double deposit, double rent, boolean available, ServiceUser ownerServiceUser) {
        this.articleID = articleID;
        this.name = name;
        this.comment = comment;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
        this.ownerServiceUser = ownerServiceUser;
    }
}
