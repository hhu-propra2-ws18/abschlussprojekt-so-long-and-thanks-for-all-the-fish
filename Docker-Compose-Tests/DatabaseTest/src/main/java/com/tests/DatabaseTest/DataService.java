package com.tests.DatabaseTest;

import com.tests.DatabaseTest.dataaccess.PersonRepository;
import com.tests.DatabaseTest.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    @Autowired
    PersonRepository personRepository;

    public long getCount() {
        return this.personRepository.findAll()
                                    .stream()
                                    .count();
    }

    public List<Person> getNames() {
        List<Person> personen = new ArrayList<>();
        personen = personRepository.findAll();
        return personen;
    }
}
