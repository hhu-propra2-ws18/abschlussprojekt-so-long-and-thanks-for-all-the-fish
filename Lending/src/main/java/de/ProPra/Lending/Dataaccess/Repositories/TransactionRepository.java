package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.ServiceUser;
import de.ProPra.Lending.Model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findTransactionsByReciever(ServiceUser reciever);
    List<Transaction> findTransactionsByGiver(ServiceUser giver);
}
