package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Dataaccess.Repositories.RequestRepository;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.Person;
import de.ProPra.Lending.Model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    LendingRepository lendings;

    @Autowired
    PersonRepository persons;

    @Autowired
    ArticleRepository articles;

    @Autowired
    RequestRepository requests;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("Lendings will be generated");
        Article testArticle1 = new Article(1, "Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 2, 500, 5, false);
        Article testArticle2 = new Article(2, "Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 3, 10, 250, false);
        Article testArticle3 = new Article(3, "Grillkohle", "schon verbrannt", 2, 816230, 25230, false);
        Person testPerson1 = new Person(1, "Wolfgang");
        Person testPerson2 = new Person(2, "Oliver");
        Person testPerson3 = new Person(3, "Robin");

       try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-02");
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-02");
            Request testRequest1 = new Request(false, 1, 3, "ich will kohle", date1, date2);
            requests.save(testRequest1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        persons.save(testPerson1);
        persons.save(testPerson2);
        persons.save(testPerson3);
        articles.save(testArticle1);
        articles.save(testArticle2);
        articles.save(testArticle3);
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-05-12");
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-12");
            Lending testLending1 = new Lending(1, 1, date2, date1);
            Lending testLending2 = new Lending(2, 2, date1, date2);
            lendings.save(testLending1);
            lendings.save(testLending2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Lendings finished generating");
    }
}
