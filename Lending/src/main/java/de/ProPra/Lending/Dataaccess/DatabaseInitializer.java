package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.APIProcessor;
import de.ProPra.Lending.Dataaccess.Repositories.*;
import de.ProPra.Lending.Model.Account;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.User;
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
        System.out.println("Lendings will be generated");

       /* User testUser1 = new User(1, "Wolfgang");
        User testUser2 = new User(2, "Oliver");
        User testUser3 = new User(3, "Robin");
        Article testArticle1 = new Article(1, "Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 2, 500, false, testUser1);
        Article testArticle2 = new Article(2, "Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 10, 250, false, testUser1);
        Article testArticle3 = new Article(3, "Grillkohle", "schon verbrannt", 816230, 25230, false, testUser1);
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
        Lending testLending1 = new Lending(date4, date3, testUser1, testArticle2);
        Lending testLending2 = new Lending(date3, date4, testUser2, testArticle1);
        lendings.save(testLending1);
        lendings.save(testLending2);
        List<Lending> yourLendings = new ArrayList<>();
        yourLendings.add(testLending1);
        users.save(testUser1);*/

       User kathrin = User.builder().userID(1).name("Kathrin").build();
       User lisa = User.builder().userID(2).name("Lisa").build();
       User memfred = User.builder().userID(3).name("Memfred").build();
       User olaf = User.builder().userID(4).name("Olaf").build();
       users.save(kathrin);
       users.save(lisa);
       users.save(memfred);
       users.save(olaf);

       Calendar endDate = Calendar.getInstance();
       Calendar startDate = Calendar.getInstance();
       startDate.set(2019, 1, 14);

       Article haecksler = Article.builder().articleID(1).isRequested(true).available(false).deposit(300).rent(10).ownerUser(lisa).name("Häcksler").finalEndDate(endDate).build();
       Article dreiTeller = Article.builder().articleID(2).isRequested(true).available(false).deposit(40).rent(1).ownerUser(kathrin).name("Drei Teller").finalEndDate(endDate).build();
       Article raclette = Article.builder().articleID(3).isRequested(true).available(false).deposit(10).rent(2).ownerUser(lisa).name("Raclettegrill").finalEndDate(endDate).build();
       Article fahrrad = Article.builder().articleID(4).isRequested(true).available(false).deposit(100).rent(4).ownerUser(memfred).name("Fahrrad").finalEndDate(endDate).build();
       Article golklumpen = Article.builder().articleID(5).isRequested(true).available(false).deposit(10000).rent(4).ownerUser(memfred).name("Goldklumpen").finalEndDate(endDate).build();
       articles.save(haecksler);
       articles.save(dreiTeller);
       articles.save(raclette);
       articles.save(fahrrad);
       articles.save(golklumpen);

       Lending haeckslerLending = Lending.builder().lendingPerson(kathrin).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(haecksler).build();
       Lending dreiTellerLending = Lending.builder().lendingPerson(memfred).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(dreiTeller).build();
       Lending racletteLending = Lending.builder().lendingPerson(olaf).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(raclette).build();
       Lending fahrradLending = Lending.builder().lendingPerson(olaf).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(fahrrad).build();
       Lending golfLending = Lending.builder().lendingPerson(kathrin).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(golklumpen).build();
       lendings.save(haeckslerLending);
       lendings.save(dreiTellerLending);
       lendings.save(racletteLending);
       lendings.save(fahrradLending);
       lendings.save(golfLending);

        APIProcessor apiProcessor = new APIProcessor();
        Account lendingAccount = Account.builder().account("Bagger").amount(10).build();

        String s = apiProcessor.postTransfer(String.class, lendingAccount, golklumpen, 1000000);
        System.out.println(s);

        System.out.println("Lendings finished generating");
    }
}
