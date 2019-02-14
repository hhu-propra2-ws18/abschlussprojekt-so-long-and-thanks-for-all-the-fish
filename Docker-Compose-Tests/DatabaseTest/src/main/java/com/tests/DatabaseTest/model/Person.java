package com.tests.DatabaseTest.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Person {
    @Id
    private Long id;

    private String name;

    public Person() {

    }

    public Person(String name) {
        this.name = name;
    }
}
