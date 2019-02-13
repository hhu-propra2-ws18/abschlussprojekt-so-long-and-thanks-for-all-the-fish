package de.ProPra.Benutzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import de.ProPra.Benutzer.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    public User findByUsername(String username);
}
