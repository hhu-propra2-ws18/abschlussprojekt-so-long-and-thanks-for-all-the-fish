package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.Transaction;
import de.ProPra.Lending.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findTransactionsByReciever(User reciever);
    List<Transaction> findTransactionsByGiver(User giver);
}
