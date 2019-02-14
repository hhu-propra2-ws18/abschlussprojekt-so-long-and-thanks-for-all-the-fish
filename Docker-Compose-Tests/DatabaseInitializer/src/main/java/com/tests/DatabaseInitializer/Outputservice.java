package com.tests.DatabaseInitializer;

import com.tests.DatabaseInitializer.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Outputservice {

    @Autowired
    Personservice personservice;

    public void run(final String... args) {

        System.out.println(personservice.getCount());

        List<Person> listePersons = personservice.getList();

        for (Person person:listePersons) {
            System.out.println(person.getName());
        }
    }
}
