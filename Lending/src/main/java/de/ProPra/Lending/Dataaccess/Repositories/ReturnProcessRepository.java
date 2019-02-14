package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.ReturnProcess;
import org.springframework.data.repository.CrudRepository;

public interface ReturnProcessRepository extends CrudRepository<ReturnProcess, Long> {
    Iterable<ReturnProcess> findAll();
}
