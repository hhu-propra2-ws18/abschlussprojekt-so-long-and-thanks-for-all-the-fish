package de.ProPra.Lending.Dataaccess;

import lombok.Data;

import java.util.Calendar;

@Data
public class LendingListObject {

    //for lending ID
    long lendingID;
    Calendar startDate;
    Calendar endDate;

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
