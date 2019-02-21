package de.hhu.rhinoshareapp.Representations;

import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import de.hhu.rhinoshareapp.domain.model.Transaction;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import de.hhu.rhinoshareapp.domain.service.TransactionRepository;

import java.util.List;
import java.util.Optional;

public class TransactionRepresentation {
    private TransactionRepository transactions;
    private ServiceUserProvider users;

    public TransactionRepresentation(TransactionRepository transactions, ServiceUserProvider users) {
        this.transactions = transactions;
        this.users = users;
    }

    public List<Transaction> FillRecieves(long userID){
        Optional<ServiceUser> user = users.findUserByuserID(userID);
        List<Transaction> reciever = transactions.findTransactionsByReciever(user.get());
        for (Transaction transaction : reciever) {
            transaction.FillFormattedDates();
            transactions.save(transaction);
        }
        return reciever;
    }
    public List<Transaction> FillGivings(long userID){
        Optional<ServiceUser> user = users.findUserByuserID(userID);
        List<Transaction> giver = transactions.findTransactionsByGiver(user.get());
        for (Transaction transaction : giver) {
            transaction.FillFormattedDates();
            transactions.save(transaction);
        }
        return giver;
    }
}
