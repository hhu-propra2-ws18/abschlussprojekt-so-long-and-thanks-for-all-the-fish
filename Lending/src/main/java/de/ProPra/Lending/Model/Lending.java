package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Calendar;

@Data
@Entity
public class Lending {
    @Id
    @GeneratedValue
    long lendingId;
    long lendingPersonID;
    long articleID;
    Calendar startDate;
    Calendar endDate;
    boolean waitingForAnswer;

    public Lending(){}

    public Lending(long lendingPersonID, long articleID, Calendar startDate, Calendar endDate, boolean waitingForAnswer) {
        this.lendingPersonID = lendingPersonID;
        this.articleID = articleID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.waitingForAnswer = waitingForAnswer;
    }
}
