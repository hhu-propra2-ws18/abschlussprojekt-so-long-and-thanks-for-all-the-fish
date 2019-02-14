package de.ProPra.Lending.Dataaccess.Representations;

import de.ProPra.Lending.Dataaccess.HtmlObjects.RequestListObject;
import de.ProPra.Lending.Dataaccess.HtmlObjects.ReturnProcessListObject;
import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Dataaccess.Repositories.ReturnProcessRepository;
import de.ProPra.Lending.Model.Request;
import de.ProPra.Lending.Model.ReturnProcess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReturnProcessRepresentation {
    private PersonRepository persons;
    private ArticleRepository articles;
    private ReturnProcessRepository returns;
    private LendingRepository lendings;
    long returnID;

    public ReturnProcessRepresentation(PersonRepository persons, ArticleRepository articles, ReturnProcessRepository returns, long returnID, LendingRepository lendings) {
        this.persons = persons;
        this.articles = articles;
        this.returns = returns;
        this.returnID = returnID;
        this.lendings = lendings;
    }

    public List<ReturnProcessListObject> FillReturns() {
        List<ReturnProcessListObject> filledReturns = new ArrayList<>();
        Iterable<ReturnProcess> potentialReturns = returns.findAll();
        for (ReturnProcess potentialReturn : potentialReturns) {
            //find all articles for given borrowID
            if(articles.findById(potentialReturn.getArticleID()).get().getPersonID() == returnID   ){
                ReturnProcessListObject tmpReturnProcess = new ReturnProcessListObject();
                tmpReturnProcess.setReturnID(potentialReturn.getReturnID());
                tmpReturnProcess.setLendingID(potentialReturn.getLendingID());
                tmpReturnProcess.setArticleName(articles.findById(potentialReturn.getArticleID()).get().getName());
                tmpReturnProcess.setReturnerName(persons.findById(potentialReturn.getLenderID()).get().getName());
                filledReturns.add(tmpReturnProcess);
            }
        }

        return filledReturns;
    }
}
