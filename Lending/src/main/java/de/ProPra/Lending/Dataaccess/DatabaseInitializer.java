package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    LendingRepository lendings;

    @Autowired
    PersonRepository persons;

    @Autowired
    ArticleRepository articles;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("Lendings will be generated");
        Article testArticle1 = new Article(1, "Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 2, 500, 5, false);
        Article testArticle2 = new Article(2, "Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 3, 10, 250, false);
        Article testArticle3 = new Article(3, "Grillkohle", "schon verbrannt", 2, 816230, 25230, false);
        Person testPerson1 = new Person(1, "Wolfgang");
        Person testPerson2 = new Person(2, "Oliver");
        Person testPerson3 = new Person(3, "Robin");
        persons.save(testPerson1);
        persons.save(testPerson2);
        persons.save(testPerson3);
        articles.save(testArticle1);
        articles.save(testArticle2);
        articles.save(testArticle3);
        Lending testLending1 = new Lending(1, 1, 1);
        Lending testLending2 = new Lending(2, 2, 2);
        lendings.save(testLending1);
        lendings.save(testLending2);
        System.out.println("Lendings finished generating");
    }
}
