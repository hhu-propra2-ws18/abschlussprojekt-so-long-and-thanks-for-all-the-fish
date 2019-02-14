package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class ReturnProcess {
    @Id
    @GeneratedValue
    long returnID;
    long lenderID;
    long lendingID;
    long articleID;
    public ReturnProcess() {
    }
}
