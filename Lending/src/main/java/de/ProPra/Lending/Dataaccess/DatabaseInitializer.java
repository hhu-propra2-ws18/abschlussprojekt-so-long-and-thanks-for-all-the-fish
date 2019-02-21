package de.ProPra.Lending.Dataaccess;


import de.ProPra.Lending.APIProcessor;
import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.ReservationRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Model.*;
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

    @Autowired
    ReservationRepository reservations;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("Lendings will be generated");

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
       Article raclette = Article.builder().articleID(3).isRequested(true).available(false).deposit(10).rent(25).ownerUser(lisa).name("Raclettegrill").finalEndDate(endDate).build();
       Article fahrrad = Article.builder().articleID(4).isRequested(true).available(false).deposit(100).rent(500).ownerUser(memfred).name("Fahrrad").finalEndDate(endDate).build();
       Article goldklumpen = Article.builder().articleID(5).isRequested(true).available(false).deposit(10000).rent(4).ownerUser(memfred).name("Goldklumpen").finalEndDate(endDate).build();
       articles.save(haecksler);
       articles.save(dreiTeller);
       articles.save(raclette);
       articles.save(fahrrad);
       articles.save(goldklumpen);

        Reservation reservation = Reservation.builder().amount(100).id(123).build();
        reservations.save(reservation);

       Lending haeckslerLending = Lending.builder().lendingPerson(kathrin).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(haecksler).build();
       Lending dreiTellerLending = Lending.builder().lendingPerson(memfred).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(dreiTeller).build();
       Lending racletteLending = Lending.builder().lendingPerson(olaf).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(raclette).build();
       Lending fahrradLending = Lending.builder().lendingPerson(olaf).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(fahrrad).build();
       Lending goldLending = Lending.builder().lendingPerson(kathrin).proPayReservation(reservation).isAccepted(false).isReturn(false).endDate(endDate).startDate(startDate).lendedArticle(goldklumpen).build();
       lendings.save(haeckslerLending);
       lendings.save(dreiTellerLending);
       lendings.save(racletteLending);
       lendings.save(fahrradLending);
       lendings.save(goldLending);

        APIProcessor apiProcessor = new APIProcessor();
        Account lendingAccount = Account.builder().account("Bagger").amount(10).build();

        String s = apiProcessor.postTransfer(String.class, lendingAccount, goldklumpen, 1000000);
        //System.out.println(s);



        System.out.println("Lendings finished generating");
    }
}
