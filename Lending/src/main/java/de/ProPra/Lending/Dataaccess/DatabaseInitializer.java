package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Dataaccess.Repositories.*;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    LendingRepository lendings;

    @Autowired
    UserRepository users;

    @Autowired
    ArticleRepository articles;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("Lendings will be generated");

        User testUser1 = new User(1, "Wolfgang");
        User testUser2 = new User(2, "Oliver");
        User testUser3 = new User(3, "Robin");
        Article testArticle1 = new Article(1, "Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 2, 500, false, testUser1);
        Article testArticle2 = new Article(2, "Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 10, 250, false, testUser1);
        Article testArticle3 = new Article(3, "Grillkohle", "schon verbrannt", 816230, 25230, false,testUser1);


           Calendar date1 = Calendar.getInstance();
           Calendar date2 = Calendar.getInstance();
           date1.set(2019, 1, 12);
           date2.set(2019, 1, 12);


        users.save(testUser1);
        users.save(testUser2);
        users.save(testUser3);
        articles.save(testArticle1);
        articles.save(testArticle2);
        articles.save(testArticle3);

            Calendar date3 = Calendar.getInstance();
            Calendar date4 = Calendar.getInstance();
            date1.set(2019, 4, 12);
            date2.set(2019, 1, 12);
            Lending testLending1 = new Lending( date4, date3, testUser1, testArticle2);
            Lending testLending2 = new Lending( date3, date4, testUser2, testArticle1);
            lendings.save(testLending1);
            lendings.save(testLending2);
        List<Lending> yourLendings = new ArrayList<>();
        yourLendings.add(testLending1);
        users.save(testUser1);
        System.out.println("Lendings finished generating");
    }
}
