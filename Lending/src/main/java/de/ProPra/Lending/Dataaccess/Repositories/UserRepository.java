package de.ProPra.Lending.Dataaccess.Repositories;


import de.ProPra.Lending.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {
    //Iterable<User> findAll();
    Optional<User> findUserByuserID(long id);
}
