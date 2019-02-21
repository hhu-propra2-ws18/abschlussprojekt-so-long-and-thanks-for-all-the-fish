package de.hhu.rhinoshareapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Lending {
    @Id
    @GeneratedValue
    long lendingID;
    @OneToOne
    ServiceUser lendingPerson;
    @OneToOne
    Article lendedArticle;

    Calendar startDate;
    Calendar endDate;
    String formattedStartDate;
    String formattedEndDate;
    boolean isAccepted;
    boolean isReturn;
    boolean isConflict;
    String warning;

    @OneToOne
    Reservation proPayReservation;

    public Lending(Calendar startDate, Calendar endDate, ServiceUser lendingPerson, Article lendedArticle) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.lendingPerson = lendingPerson;
        this.lendedArticle = lendedArticle;
    }
    public void FillFormattedDates(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        formattedEndDate = dateFormat.format(endDate.getTime());
        formattedStartDate = dateFormat.format(startDate.getTime());
    }

    public Lending(ServiceUser lendingPerson, Calendar endDate) {
        this.lendingPerson = lendingPerson;
        this.endDate = endDate;
    }

}