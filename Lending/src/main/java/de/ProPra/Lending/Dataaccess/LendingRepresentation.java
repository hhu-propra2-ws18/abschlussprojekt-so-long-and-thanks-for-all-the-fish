package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LendingRepresentation {

    private LendingRepository lendings;
    private PersonRepository persons;
    private ArticleRepository articles;

    public List<LendingListObject> fillLendings(long lendingPersonID){
        List<LendingListObject> filledLendingRepresentation = new ArrayList<>();

        Iterable<Lending> allLendings = lendings.findAll();
        for (Lending lending : allLendings) {

            if(lending.getLendingPersonID() == lendingPersonID){
                LendingListObject lendingListObject = new LendingListObject();
                //allocate lendingID
                lendingListObject.setLendingID(lending.getLendingId());
                //allocate article infos
                Article specificArticle = articles.findById(lending.getArticleID()).get();
                lendingListObject.setArticleName(specificArticle.getName());
                lendingListObject.setComment(specificArticle.getComment());
                lendingListObject.setBorrowPerson(persons.findById(specificArticle.getPersonID()).get().getName());
                lendingListObject.setDeposit(specificArticle.getDeposit());
                lendingListObject.setRent(specificArticle.getRent());
                //add to List
                filledLendingRepresentation.add(lendingListObject);
            }
        }


        return filledLendingRepresentation;
    }

    public List<LendingListObject> fillBorrows(long borrowPersonID){
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
            }
        }


        return filledLendingRepresentation;
    }

    @Autowired
    public LendingRepresentation(LendingRepository lendings, PersonRepository persons, ArticleRepository articles) {
        this.lendings = lendings;
        this.persons = persons;
        this.articles = articles;
    }
}
