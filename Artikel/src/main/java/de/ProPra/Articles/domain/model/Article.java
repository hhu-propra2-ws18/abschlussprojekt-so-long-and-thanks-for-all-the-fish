package de.ProPra.Articles.domain.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

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

    @Lob
    public MultipartFile file;

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

