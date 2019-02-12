package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Iterable<Person> findAll();
}
