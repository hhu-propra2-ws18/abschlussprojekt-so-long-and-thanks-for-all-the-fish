package de.hhu.rhinoshareapp.domain.database;


import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.Calendar;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    ServiceUserProvider users;

    @Autowired
    ArticleRepository articles;

    @Autowired
    LendingRepository lending;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException { //hier wird die Datenbank gefüllt
        System.out.println("Populating the database");

        User root = new User("Pasquier", "Jacques", "Musterstr. 8, 41749 Viersen", "jacques", "japas102@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");
        User joe = new User("Test", "Test", "Musterstr. 8, 41749 Viersen", "user", "test102@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");
        User jacques = new User("Test", "Test", "Musterstr. 8, 41749 Viersen", "2", "jtest111@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");

        users.saveAll(Arrays.asList(root, joe, jacques));

        User testUser1 = new User("Jeff", "Nosbusch", "strasse", "jeff", "jeff@mail.com", "1234", "user");
        User testUser2 = new User("George", "Pi", "Blumenstrasse", "george", "george@mail.com", "1234", "user");
        User testUser3 = new User("Franz", "Hoff", "Palmenstrasse", "franz", "franz@mail.com", "1234", "user");

        long id1 = testUser1.getUserID();
        long id2 = testUser2.getUserID();

        Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", id1, 500, 25, true, null);
        Article testArticle2 = new Article("Geschirr", "nur ein bisschen zerbrochen, für 20 mann", id2, 250, 25, true, null);
        Article testArticle3 = new Article("Grillkohle", "schon verbrannt", id2, 25230, 88, false, null);
        testArticle1.setOwner(testUser1);
        testArticle2.setOwner(testUser2);
        testArticle3.setOwner(testUser3);

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
        date3.set(2019, 4, 12);
        date4.set(2019, 1, 12);
        Lending testLending1 = new Lending(date4, date3, testUser1, testArticle2);
        Lending testLending2 = new Lending(date3, date4, testUser2, testArticle1);
        testLending2.setConflict(true);
        lending.save(testLending1);
        lending.save(testLending2);
    }
}
