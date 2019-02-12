package de.ProPra.Articles.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

}

