package de.hhu.ProPra.conflict.service;


import de.hhu.ProPra.conflict.model.Article;
import de.hhu.ProPra.conflict.model.Lending;
import de.hhu.ProPra.conflict.model.User;
import de.hhu.ProPra.conflict.service.Repositorys.ArticleRepository;
import de.hhu.ProPra.conflict.service.Repositorys.ConflictRepository;
import de.hhu.ProPra.conflict.service.Repositorys.LendingRepository;
import de.hhu.ProPra.conflict.service.Repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Calendar;

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

        User testUser1 = new User("jeff", "Jeff", "Jeff@email.com", true, "1, zuhause str, Dueseldorf");
        User testUser2 = new User("george", "George", "George@email.com", false, "2, lupo str, Duesseldorf");
        User testUser3 = new User("franz", "Franz", "Franz@email.com", true, "3, heinrich heine allee, Duesseldorf");

        Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 2, 500, false, testUser1);
        Article testArticle2 = new Article("Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 10, 250, false, testUser1);
        Article testArticle3 = new Article("Grillkohle", "schon verbrannt", 816230, 25230, false,testUser1);

        users.save(testUser1);
        users.save(testUser2);
        users.save(testUser3);
        articles.save(testArticle1);
        articles.save(testArticle2);
        articles.save(testArticle3);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.set(2019, 1, 12);
        date2.set(2019, 1, 12);
        Calendar date3 = Calendar.getInstance();
        Calendar date4 = Calendar.getInstance();
        date1.set(2019, 4, 12);
        date2.set(2019, 1, 12);
        Lending testLending1 = new Lending( date4, date3, testUser1, testArticle2);
        Lending testLending2 = new Lending( date3, date4, testUser2, testArticle1);
        lendings.save(testLending1);
        lendings.save(testLending2);



    }


}
