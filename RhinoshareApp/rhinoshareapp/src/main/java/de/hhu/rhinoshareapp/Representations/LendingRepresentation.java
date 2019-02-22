package de.hhu.rhinoshareapp.Representations;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class LendingRepresentation {

    private LendingRepository lendings;
    private ServiceUserProvider users;
    private ArticleRepository articles;

    // Returns a list of of lendings for a User given by his id, that are Accepted and not returned
    public List<Lending> FillLendings(long userID) {
        User user = users.findUserByuserID(userID).get();
        List<Lending> lendingList = lendings.findAllBylendingPersonAndIsAcceptedAndIsReturnAndIsConflict(user, true, false, false);
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
        User user = users.findUserByuserID(userID).get();
        List<Lending> lendingList = lendings.findAllBylendingPersonAndIsAcceptedAndIsReturnAndIsConflict(user, true, false, true);
        for (Lending lending : lendingList) {
            lending.setWarning("The article you lended is currently investigated");
        }
        return lendingList;
    }
    public List<Lending> FillConflictsOwner(long userID) {
        User user = users.findUserByuserID(userID).get();
        List<Article> articles = this.articles.findAllByownerUser(user);
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

    // Returns a list of all borrowed articles for a User given by his id
    public List<Article> FillBorrows(long borrowPersonID){
        User user = users.findUserByuserID(borrowPersonID).get();
        return articles.findAllByownerUser(user);
    }
    @Autowired
    public LendingRepresentation(LendingRepository lendings, ServiceUserProvider users, ArticleRepository articles) {
        this.lendings = lendings;
        this.users = users;
        this.articles = articles;
    }
}
