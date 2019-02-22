package de.hhu.rhinoshareapp.Representations;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;

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
       Optional<User> user = users.findUserByuserID(borrowID);
       List<Lending> filledLendings = new ArrayList<>();
       List<Article> requestedIsTrue = articles.findAllArticleByOwnerAndIsRequested(user.get(), true);
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
