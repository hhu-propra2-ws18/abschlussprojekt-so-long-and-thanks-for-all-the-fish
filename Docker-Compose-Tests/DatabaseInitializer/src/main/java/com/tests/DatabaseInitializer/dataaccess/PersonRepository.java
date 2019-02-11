package com.tests.DatabaseInitializer.dataaccess;

import com.tests.DatabaseInitializer.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

}
