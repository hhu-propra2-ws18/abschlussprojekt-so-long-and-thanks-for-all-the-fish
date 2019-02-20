package de.hhu.ProPra.conflict.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Data
@Entity
public class Lending {

    @Id
    @GeneratedValue
    long lendingID;
    @OneToOne
    User lendingPerson;
    @OneToOne
    Article lendedArticle;

    Calendar startDate;
    Calendar endDate;
    String formattedStartDate;
    String formattedEndDate;
    boolean isAccepted;
    boolean isReturn;
    String warning;
    boolean conflict = false;

    public Lending(){}
    public Lending(Calendar startDate, Calendar endDate, User lendingPerson, Article lendedArticle) {
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

    public Lending(User lendingPerson, Calendar endDate) {
        this.lendingPerson = lendingPerson;
        this.endDate = endDate;
    }

    public Lending(long lendingID, User lendingPerson, Article lendedArticle, Calendar startDate, Calendar endDate, String formattedStartDate, String formattedEndDate, boolean isAccepted, boolean isReturn, String warning) {
        this.lendingID = lendingID;
        this.lendingPerson = lendingPerson;
        this.lendedArticle = lendedArticle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.formattedStartDate = formattedStartDate;
        this.formattedEndDate = formattedEndDate;
        this.isAccepted = isAccepted;
        this.isReturn = isReturn;
        this.warning = warning;
    }

}
