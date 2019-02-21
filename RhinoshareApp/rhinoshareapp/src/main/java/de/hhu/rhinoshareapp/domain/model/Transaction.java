package de.hhu.rhinoshareapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    long transactionID;
    @OneToOne
    ServiceUser reciever;
    @OneToOne
    ServiceUser giver;
    @OneToOne
    Article article;
    double amount;

    public Transaction(ServiceUser reciever, ServiceUser giver, Article article, double amount, Calendar transactionDate) {
        this.reciever = reciever;
        this.giver = giver;
        this.article = article;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    Calendar transactionDate;
    String formattedTransactionDate;

    public void FillFormattedDates(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        formattedTransactionDate = dateFormat.format(transactionDate.getTime());
    }
}
