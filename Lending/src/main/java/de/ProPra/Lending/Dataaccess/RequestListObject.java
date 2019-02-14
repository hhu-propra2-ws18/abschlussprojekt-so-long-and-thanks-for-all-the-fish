package de.ProPra.Lending.Dataaccess;

import lombok.Data;

import java.util.Date;

@Data
public class RequestListObject {
    long requestID;
    String requesterName;
    String articleName;
    long articleID;
    String requestComment;
    Date startDate;       //TODO: Date ändern
    Date endDate;

}
