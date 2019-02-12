package de.ProPra.Lending.Dataaccess;

import lombok.Data;

@Data
public class LendingListObject {

    //for lending ID
    long lendingID;

    // for article
    String articleName;
    String comment;
    String borrowPerson;
    double deposit;
    double rent;
}
