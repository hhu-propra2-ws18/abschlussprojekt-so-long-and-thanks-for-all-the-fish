package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findTransactionsByReciever(Person reciever);
    List<Transaction> findTransactionsByGiver(Person giver);
}
