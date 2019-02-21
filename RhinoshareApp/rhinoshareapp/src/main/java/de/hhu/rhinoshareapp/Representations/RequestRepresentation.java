package de.hhu.rhinoshareapp.Representations;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestRepresentation {
    private ServiceUserProvider users;
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
    public RequestRepresentation(ServiceUserProvider users, ArticleRepository articles, LendingRepository lendings, long borrowID) {
        this.users = users;
        this.articles = articles;
        this.lendings = lendings;
        this.borrowID = borrowID;
    }
}