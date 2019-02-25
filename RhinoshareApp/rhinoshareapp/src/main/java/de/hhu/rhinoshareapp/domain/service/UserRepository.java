package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findUserByuserID(long id);
    long findUserIDByUsername(String username); //TODO: kann ein long nicht zurückgegeben werden ?
    Optional<User> findUserByUsername(String username);
}
