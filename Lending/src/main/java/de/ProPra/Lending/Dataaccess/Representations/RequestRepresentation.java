package de.ProPra.Lending.Dataaccess.Representations;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.ServiceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestRepresentation {
    private UserRepository users;
    private ArticleRepository articles;
    private LendingRepository lendings;
    long borrowID;

   public List<Lending> FillRequest(){
       //suche alle artikel wo die request flag gesetzt ist
       Optional<ServiceUser> user = users.findUserByuserID(borrowID);
       List<Lending> filledLendings = new ArrayList<>();
       List<Article> requestedIsTrue = articles.findAllArticleByownerUserAndIsRequested(user.get(), true);
       for (Article article : requestedIsTrue) {
           Lending lending = lendings.findLendingBylendedArticle(article).get();
           lending.FillFormattedDates();
           filledLendings.add(lending);
       }
       return filledLendings;
   }
    public RequestRepresentation(UserRepository users, ArticleRepository articles, LendingRepository lendings, long borrowID) {
        this.users = users;
        this.articles = articles;
        this.lendings = lendings;
        this.borrowID = borrowID;
    }
}
