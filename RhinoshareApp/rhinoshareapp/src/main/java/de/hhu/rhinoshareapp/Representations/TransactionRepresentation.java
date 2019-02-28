package de.hhu.rhinoshareapp.Representations;

import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.model.Transaction;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import de.hhu.rhinoshareapp.domain.service.TransactionRepository;

import java.util.List;
import java.util.Optional;

public class TransactionRepresentation {
    private TransactionRepository transactions;
    private UserRepository users;

    public TransactionRepresentation(TransactionRepository transactions, UserRepository users) {
        this.transactions = transactions;
        this.users = users;
    }

    public List<Transaction> fillRecieves(long userID){
        Optional<User> user = users.findUserByuserID(userID);
        List<Transaction> reciever = transactions.findTransactionsByReciever(user.get());
        for (Transaction transaction : reciever) {
            transaction.fillFormattedDates();
            transactions.save(transaction);
        }
        return reciever;
    }
    public List<Transaction> fillGivings(long userID){
        Optional<User> user = users.findUserByuserID(userID);
        List<Transaction> giver = transactions.findTransactionsByGiver(user.get());
        for (Transaction transaction : giver) {
            transaction.fillFormattedDates();
            transactions.save(transaction);
        }
        return giver;
    }
}
