package de.ProPra.Lending.Dataaccess;

import lombok.Data;

import java.util.Date;

@Data
public class LendingListObject {

    //for lending ID
    long lendingID;
    Date startDate;
    Date endDate;

    // for article
    long articleID;
    String articleName;
    String comment;
    String borrowPerson;
    double deposit;
    double rent;
    boolean available;

    //for Warning
    String warning;
}
