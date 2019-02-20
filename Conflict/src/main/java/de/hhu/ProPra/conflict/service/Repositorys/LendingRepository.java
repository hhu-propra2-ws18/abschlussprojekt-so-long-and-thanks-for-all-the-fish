package de.hhu.ProPra.conflict.service.Repositorys;

import de.hhu.ProPra.conflict.model.Lending;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LendingRepository extends CrudRepository<Lending, Long> {

    Lending findById(long lendingID);

    List<Lending> findAllByConflict(boolean conflict);

}
