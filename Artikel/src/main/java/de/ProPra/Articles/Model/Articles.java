package de.ProPra.Articles.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Articles {
    @Id
    @GeneratedValue
    long articleID;

    String name;

    String comment;

    double deposit;

    double rent;

}

