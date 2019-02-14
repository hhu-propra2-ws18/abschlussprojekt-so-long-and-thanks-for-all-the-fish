package de.ProPra.Lending.Dataaccess;

import lombok.Data;

import java.util.Calendar;

@Data
public class RequestListObject {
    long requestID;
    String requesterName;
    String articleName;
    long articleID;
    String requestComment;
    Calendar startDate;
    Calendar endDate;

}
