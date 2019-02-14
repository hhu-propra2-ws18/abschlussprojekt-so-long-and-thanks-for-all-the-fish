package de.ProPra.Lending.Dataaccess.HtmlObjects;

import lombok.Data;



@Data
public class RequestListObject {
    long requestID;
    String requesterName;
    String articleName;
    long articleID;
    String requestComment;
    String startDate;
    String endDate;

}
