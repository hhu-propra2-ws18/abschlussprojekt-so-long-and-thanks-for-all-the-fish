package de.hhu.rhinoshareapp.Representations;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReturnProcessRepresentation {
    private UserRepository users;
    private ArticleRepository articles;
    private LendingRepository lendings;
    long userID;

    public ReturnProcessRepresentation(UserRepository users, ArticleRepository articles, long userID, LendingRepository lendings) {
        this.users = users;
        this.articles = articles;
        this.userID = userID;
        this.lendings = lendings;
    }
    public List<Lending> fillReturns(){
        List<Lending> filledReturns = new ArrayList<>();
        List<Article> articles = this.articles.findAllByOwner(users.findUserByuserID(userID).get());
        for (Article article : articles) {
            Optional<Lending> lending = lendings.findLendingBylendedArticleAndIsReturn(article, true);
            if(lending.isPresent()) {
                filledReturns.add(lending.get());
            }
        }
        return filledReturns;
    }
}
