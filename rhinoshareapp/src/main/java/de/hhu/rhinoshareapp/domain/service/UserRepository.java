package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUsername(String username);
    Optional<Person> findUserByuserID(long id);
    long findUserIDByUsername(String username); //TODO: kann ein long nicht zurückgegeben werden ?
    Optional<Person> findUserByUsername(String username);
    List<Person> findByRole(String role);
}
