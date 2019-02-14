package de.ProPra.Lending.Dataaccess.Repositories;


import de.ProPra.Lending.Model.Request;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Long> {
    Iterable<Request> findAll();
}
