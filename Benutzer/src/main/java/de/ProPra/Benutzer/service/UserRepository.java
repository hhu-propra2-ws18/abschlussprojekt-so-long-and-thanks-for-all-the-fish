package de.ProPra.Benutzer.service;

import org.springframework.data.repository.CrudRepository;
import de.ProPra.Benutzer.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
