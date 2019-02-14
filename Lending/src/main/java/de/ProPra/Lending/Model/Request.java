package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;


@Data
@Entity
public class Request {
    @Id
    @GeneratedValue
    long requestID;
    long requesterID;
    long articleID;
    String requestComment;
    Calendar startDate;
    Calendar endDate;

    public Request(long requesterID, long articleID, String requestComment,Calendar startDate, Calendar endDate) {
        this.requesterID = requesterID;
        this.articleID = articleID;
        this.requestComment = requestComment;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Request() {
    }
}
