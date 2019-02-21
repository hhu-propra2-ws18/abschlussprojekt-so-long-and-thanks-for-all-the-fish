package de.ProPra.Lending.Dataaccess.Representations;

import de.ProPra.Lending.Dataaccess.Repositories.TransactionRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Model.Transaction;
import de.ProPra.Lending.Model.User;

import java.util.List;
import java.util.Optional;

public class TransactionRepresentation {
    private TransactionRepository transactions;
    private UserRepository users;

    public TransactionRepresentation(TransactionRepository transactions, UserRepository users) {
        this.transactions = transactions;
        this.users = users;
    }

    public List<Transaction> FillRecieves(long userID){
        Optional<User> user = users.findUserByuserID(userID);
        List<Transaction> reciever = transactions.findTransactionsByReciever(user.get());
        for (Transaction transaction : reciever) {
            transaction.FillFormattedDates();
            transactions.save(transaction);
        }
        return reciever;
    }
    public List<Transaction> FillGivings(long userID){
        Optional<User> user = users.findUserByuserID(userID);
        List<Transaction> giver = transactions.findTransactionsByGiver(user.get());
        for (Transaction transaction : giver) {
            transaction.FillFormattedDates();
            transactions.save(transaction);
        }
        return giver;
    }
}
