package com.tests.DatabaseTest.dataaccess;

import com.tests.DatabaseTest.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findAll();

}
