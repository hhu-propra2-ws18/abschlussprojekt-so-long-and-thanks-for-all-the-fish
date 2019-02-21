package de.ProPra.Lending.Dataaccess.Representations;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.ServiceUserProvider;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReturnProcessRepresentation {
    private ServiceUserProvider users;
    private ArticleRepository articles;
    private LendingRepository lendings;
    long userID;

    public ReturnProcessRepresentation(ServiceUserProvider users, ArticleRepository articles, long userID, LendingRepository lendings) {
        this.users = users;
        this.articles = articles;
        this.userID = userID;
        this.lendings = lendings;
    }
    public List<Lending> FillReturns(){
        List<Lending> filledReturns = new ArrayList<>();
        List<Article> articles = this.articles.findAllByownerUser(users.findUserByuserID(userID).get());
        for (Article article : articles) {
            Optional<Lending> lending = lendings.findLendingBylendedArticleAndIsReturn(article, true);
            if(lending.isPresent()) {
                filledReturns.add(lending.get());
            }
        }
        return filledReturns;
    }
}
