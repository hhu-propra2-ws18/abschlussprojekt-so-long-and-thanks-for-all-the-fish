package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Dataaccess.Repositories.RequestRepository;
import de.ProPra.Lending.Model.Request;

import java.util.ArrayList;
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
                tmpRequest.setRequestComment(potentialRequest.getRequestComment().replaceAll("\\+", " "));
                //find Dates
                tmpRequest.setStartDate(potentialRequest.getStartDate()); //TODO: Date ändern
                tmpRequest.setEndDate(potentialRequest.getEndDate());
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
