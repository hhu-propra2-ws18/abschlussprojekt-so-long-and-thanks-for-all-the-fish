package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import de.hhu.rhinoshareapp.domain.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findTransactionsByReciever(ServiceUser reciever);
    List<Transaction> findTransactionsByGiver(ServiceUser giver);
}
