package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Lending {
    @Id
    @GeneratedValue
    long lendingId;
    long lendingPersonID;
    long articleID;

    public Lending(){}
    public Lending(long lendingId, long lendingPersonID, long articleID) {
        this.lendingId = lendingId;
        this.lendingPersonID = lendingPersonID;
        this.articleID = articleID;
    }
}
