package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Dataaccess.Repositories.RequestRepository;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class LendingRepresentation {

    private LendingRepository lendings;
    private PersonRepository persons;
    private ArticleRepository articles;
    private RequestRepository requests;

    public List<LendingListObject> FillLendings(long lendingPersonID){
        List<LendingListObject> filledLendingRepresentation = new ArrayList<>();

        Iterable<Lending> allLendings = lendings.findAll();
        for (Lending lending : allLendings) {

            //look for lendings for the given personID
            if(lending.getLendingPersonID() == lendingPersonID){
                LendingListObject lendingListObject = new LendingListObject();
                //allocate lendingID
                lendingListObject.setLendingID(lending.getLendingId());
                //allocate article infos
                Article specificArticle = articles.findById(lending.getArticleID()).get();
                lendingListObject.setArticleID(specificArticle.getArticleID());
                lendingListObject.setArticleName(specificArticle.getName());
                lendingListObject.setComment(specificArticle.getComment());
                lendingListObject.setBorrowPerson(persons.findById(specificArticle.getPersonID()).get().getName());
                lendingListObject.setDeposit(specificArticle.getDeposit());
                lendingListObject.setRent(specificArticle.getRent());
                lendingListObject.setEndDate(lending.getEndDate());
                Date endDate = lendingListObject.getEndDate();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms");
                Date currentDate = new Date();
                try {
                    String tmpDate = formatter.format(currentDate);
                    currentDate = formatter.parse(tmpDate);
                    if(currentDate.after(endDate)){
                        lendingListObject.setWarning("ATTENTION: YOU HAVE RETURN THIS ARTICLE");
                    }else{
                        lendingListObject.setWarning("You can use this article without worries");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //add to List
                filledLendingRepresentation.add(lendingListObject);
            }
        }


        return filledLendingRepresentation;
    }

    public List<LendingListObject> FillBorrows(long borrowPersonID){
        List<LendingListObject> filledLendingRepresentation = new ArrayList<>();

        Iterable<Article> allArticles = articles.findAll();
        for (Article article : allArticles) {

            if(article.getPersonID() == borrowPersonID){
                LendingListObject lendingListObject = new LendingListObject();
                //allocate article infos
                lendingListObject.setArticleName(article.getName());
                lendingListObject.setComment(article.getComment());
                lendingListObject.setBorrowPerson(persons.findById(article.getPersonID()).get().getName());
                lendingListObject.setDeposit(article.getDeposit());
                lendingListObject.setRent(article.getRent());
                //add to List
                filledLendingRepresentation.add(lendingListObject);
                //add isRequestet
            }
        }


        return filledLendingRepresentation;
    }

    @Autowired
    public LendingRepresentation(LendingRepository lendings, PersonRepository persons, ArticleRepository articles, RequestRepository requests) {
        this.lendings = lendings;
        this.persons = persons;
        this.articles = articles;
        this.requests = requests;
    }
}
