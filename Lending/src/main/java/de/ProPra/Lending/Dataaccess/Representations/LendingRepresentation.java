package de.ProPra.Lending.Dataaccess.Representations;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class LendingRepresentation {

    private LendingRepository lendings;
    private UserRepository users;
    private ArticleRepository articles;

    // Returns a list of of lendings for a User given by his id, that are Accepted and not returned
    public List<Lending> FillLendings(long userID) {
        User user = users.findUserByuserID(userID).get();
        List<Lending> lendingList = lendings.findAllBylendingPersonAndIsAcceptedAndIsReturn(user, true, false);
        for (Lending lending : lendingList) {
            Calendar endDate = lending.getEndDate();
            Calendar currentDate = Calendar.getInstance();
            if (currentDate.after(endDate)) {
                lending.setWarning("ATTENTION: YOU HAVE RETURN THIS ARTICLE");
            } else {
                lending.setWarning("You can use this article without worries");
            }
        }

        return lendingList;
    }

    // Returns a list of all borrowed articles for a User given by his id
    public List<Article> FillBorrows(long borrowPersonID){
        User user = users.findUserByuserID(borrowPersonID).get();
        return articles.findAllByownerUser(user);
    }
    @Autowired
    public LendingRepresentation(LendingRepository lendings, UserRepository users, ArticleRepository articles) {
        this.lendings = lendings;
        this.users = users;
        this.articles = articles;
    }
}
