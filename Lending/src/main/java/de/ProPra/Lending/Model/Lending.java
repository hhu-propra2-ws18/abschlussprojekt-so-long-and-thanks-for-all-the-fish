package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
public class Lending {
    @Id
    @GeneratedValue
    long lendingId;
    long lendingPersonID;
    long articleID;
    Date startDate;
    Date endDate;

    public Lending(){}

    public Lending(long lendingPersonID, long articleID, Date startDate, Date endDate) {
        this.lendingPersonID = lendingPersonID;
        this.articleID = articleID;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
