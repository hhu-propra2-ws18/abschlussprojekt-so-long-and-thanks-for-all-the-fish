package com.tests.DatabaseInitializer.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }
}
