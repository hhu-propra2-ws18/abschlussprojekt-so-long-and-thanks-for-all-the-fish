package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Request {
    @Id
    @GeneratedValue
    long requestID;
    boolean isPermitted;
    long requesterID;
    long articleID;
    String requestComment;
    Date startDate;
    Date endDate;

    public Request( boolean isPermitted, long requesterID, long articleID, String requestComment, Date startDate, Date endDate) {
        this.isPermitted = isPermitted;
        this.requesterID = requesterID;
        this.articleID = articleID;
        this.requestComment = requestComment;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Request() {
    }
}
