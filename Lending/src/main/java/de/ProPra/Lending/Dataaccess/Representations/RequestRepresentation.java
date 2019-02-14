package de.ProPra.Lending.Dataaccess.Representations;

import de.ProPra.Lending.Dataaccess.HtmlObjects.RequestListObject;
import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Dataaccess.Repositories.RequestRepository;
import de.ProPra.Lending.Model.Request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RequestRepresentation {
    private PersonRepository persons;
    private ArticleRepository articles;
    private RequestRepository requests;
    long borrowID;

    public List<RequestListObject> FillRequest(){
        List<RequestListObject> filledRequests = new ArrayList<>();
        Iterable<Request> potentialRequests = requests.findAll();
        for (Request potentialRequest : potentialRequests) {
            //find all articles for given borrowID
            if(articles.findById(potentialRequest.getArticleID()).get().getPersonID() == borrowID){
                RequestListObject tmpRequest = new RequestListObject();
                //get request ID
                tmpRequest.setRequestID(potentialRequest.getRequestID());
                //find name of lending person
                tmpRequest.setRequesterName(persons.findById(potentialRequest.getRequesterID()).get().getName());
                //find articlename
                tmpRequest.setArticleName(articles.findById(potentialRequest.getArticleID()).get().getName());
                //find articleID
                tmpRequest.setArticleID(potentialRequest.getArticleID());
                //find requestcomment
                tmpRequest.setRequestComment(potentialRequest.getRequestComment().replaceAll("\\+", " ").replaceAll("%2B", "+"));
                //find Dates
                String tmpDate = potentialRequest.getStartDate().get(Calendar.DATE) + "." + (potentialRequest.getStartDate().get(Calendar.MONTH)+1) + "." + potentialRequest.getStartDate().get(Calendar.YEAR);
                tmpRequest.setStartDate(tmpDate);
                tmpDate = potentialRequest.getEndDate().get(Calendar.DATE) + "." + (potentialRequest.getEndDate().get(Calendar.MONTH)+1) + "." + potentialRequest.getEndDate().get(Calendar.YEAR);
                tmpRequest.setEndDate(tmpDate);
                filledRequests.add(tmpRequest);
            }
        }
        return filledRequests;
    }
    public RequestRepresentation(PersonRepository persons, ArticleRepository articles, RequestRepository requests, long borrowID) {
        this.persons = persons;
        this.articles = articles;
        this.requests = requests;
        this.borrowID = borrowID;
    }
}
