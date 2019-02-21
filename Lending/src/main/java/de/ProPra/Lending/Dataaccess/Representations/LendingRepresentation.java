package de.ProPra.Lending.Dataaccess.Representations;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class LendingRepresentation {

    private LendingRepository lendings;
    private UserRepository users;
    private ArticleRepository articles;

    // Returns a list of of lendings for a ServiceUser given by his id, that are Accepted and not returned
    public List<Lending> FillLendings(long userID) {
        ServiceUser serviceUser = users.findUserByuserID(userID).get();
        List<Lending> lendingList = lendings.findAllBylendingPersonAndIsAcceptedAndIsReturnAndIsConflict(serviceUser, true, false, false);
        for (Lending lending : lendingList) {
            Calendar endDate = lending.getEndDate();
            Calendar currentDate = Calendar.getInstance();
            if (currentDate.after(endDate)) {
                lending.setWarning("ATTENTION: YOU HAVE RETURN THIS ARTICLE");
            } else {
                long time = endDate.getTime().getTime() - currentDate.getTime().getTime();
                long days = Math.round( (double)time / (24. * 60.*60.*1000.) );
                lending.setWarning("You can use this article without worries for the next "+days+" days");
            }
        }

        return lendingList;
    }
    public List<Lending> FillConflicts(long userID) {
        ServiceUser serviceUser = users.findUserByuserID(userID).get();
        List<Lending> lendingList = lendings.findAllBylendingPersonAndIsAcceptedAndIsReturnAndIsConflict(serviceUser, true, false, true);
        for (Lending lending : lendingList) {
            lending.setWarning("The article you lended is currently investigated");
        }
        return lendingList;
    }
    public List<Lending> FillConflictsOwner(long userID) {
        ServiceUser serviceUser = users.findUserByuserID(userID).get();
        List<Article> articles = this.articles.findAllByownerUser(serviceUser);
        List<Lending> lendingList = new ArrayList<>();
        for (Article article : articles) {
            Optional<Lending> conflictLending = lendings.findLendingBylendedArticle(article);
            if(conflictLending.get().isConflict()){
                conflictLending.get().setWarning("Your Article in this Lending is currently investigated");
                lendingList.add(conflictLending.get());
            }
        }
        return lendingList;
    }

    // Returns a list of all borrowed articles for a ServiceUser given by his id
    public List<Article> FillBorrows(long borrowPersonID){
        ServiceUser serviceUser = users.findUserByuserID(borrowPersonID).get();
        return articles.findAllByownerUser(serviceUser);
    }
    @Autowired
    public LendingRepresentation(LendingRepository lendings, UserRepository users, ArticleRepository articles) {
        this.lendings = lendings;
        this.users = users;
        this.articles = articles;
    }
}
