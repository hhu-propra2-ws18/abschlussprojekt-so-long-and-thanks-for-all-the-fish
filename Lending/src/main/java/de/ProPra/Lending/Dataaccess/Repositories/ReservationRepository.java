package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    Iterable<Reservation> findAll();
}
