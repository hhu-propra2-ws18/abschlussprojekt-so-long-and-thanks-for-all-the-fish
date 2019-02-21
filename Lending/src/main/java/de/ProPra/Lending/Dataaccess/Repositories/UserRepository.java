package de.ProPra.Lending.Dataaccess.Repositories;


import de.ProPra.Lending.Model.ServiceUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<ServiceUser, Long> {
    //Iterable<ServiceUser> findAll();
    Optional<ServiceUser> findUserByuserID(long id);
}
