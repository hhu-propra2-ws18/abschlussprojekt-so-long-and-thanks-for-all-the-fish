package de.hhu.ProPra.conflict.service.Repositorys;

import de.hhu.ProPra.conflict.model.Conflict;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface ConflictRepository  extends CrudRepository <Conflict, Long>{

}
