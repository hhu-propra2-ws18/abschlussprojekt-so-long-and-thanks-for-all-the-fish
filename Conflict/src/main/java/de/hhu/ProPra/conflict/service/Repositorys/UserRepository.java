package de.hhu.ProPra.conflict.service.Repositorys;

import de.hhu.ProPra.conflict.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(long id);
}
