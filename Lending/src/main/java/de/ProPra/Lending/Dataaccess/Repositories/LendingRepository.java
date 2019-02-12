package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.Lending;
import org.springframework.data.repository.CrudRepository;

public interface LendingRepository extends CrudRepository<Lending, Long> {
    Iterable<Lending> findAll();
}
