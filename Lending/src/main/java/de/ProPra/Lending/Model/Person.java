package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Person {
    @Id
    long personID;
    String name;

    public Person(){}
    public Person(long personID, String name) {
        this.personID = personID;
        this.name = name;
    }
}
