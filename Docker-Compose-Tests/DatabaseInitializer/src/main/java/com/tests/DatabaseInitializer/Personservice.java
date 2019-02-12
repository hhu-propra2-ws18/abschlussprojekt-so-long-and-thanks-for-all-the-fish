package com.tests.DatabaseInitializer;

import com.tests.DatabaseInitializer.dataaccess.PersonRepository;
import com.tests.DatabaseInitializer.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Personservice {

    @Autowired
    PersonRepository personRepository;

    public long getCount() {
        return this.personRepository.findAll()
                                    .stream().count();
    }

    public List<Person> getList() {
        return this.personRepository.findAll();
    }
}
