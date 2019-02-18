package com.tests.DatabaseTest;

import com.tests.DatabaseTest.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutputService {

    @Autowired
    DataService service;

    public void run(final String... args) {

        System.out.println(service.getCount());

        List<Person> personen = service.getNames();

        for (Person person : personen) {
            System.out.println(person.getName());
        }
    }
}
