package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.APIProcessor;
import de.ProPra.Lending.Dataaccess.Repositories.*;
import de.ProPra.Lending.Model.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PostProccessor {
    public HashMap<String, String> SplitString(String postBody){
        HashMap<String, String> postBodyParas = new HashMap<>();
        String[] splittedPostBody = postBody.split("&");
        for (String para : splittedPostBody) {
            String[] splittedPara = para.split("=");
            postBodyParas.put(splittedPara[0], splittedPara[1].replace("+"," "));
        }
        return postBodyParas;
    }
    public void CreateNewLending(HashMap<String, String> postBodyParas, ArticleRepository articles, LendingRepository lendings, UserRepository users) {
        //set timeperiod information
        Calendar startDate = Calendar.getInstance();
        String[] datePieces = postBodyParas.get("startDate").split("-");
        startDate.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1])-1, Integer.parseInt(datePieces[2]));
        Calendar endDate = Calendar.getInstance();
        datePieces = postBodyParas.get("endDate").split("-");
        endDate.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1])-1, Integer.parseInt(datePieces[2]));

        //collect necessary information
        User lendingPerson = users.findUserByuserID(Long.parseLong(postBodyParas.get("requesterID"))).get();
        Article lendedArticle = articles.findArticleByarticleID(Long.parseLong(postBodyParas.get("articleID"))).get();
        lendedArticle.setLendingUser(lendingPerson);
        lendedArticle.setRequestComment(postBodyParas.get("requestComment"));
        lendedArticle.setRequested(true);
        articles.save(lendedArticle);

        // create new Lending
        Lending newLending = new Lending();
        newLending.setLendingPerson(lendingPerson);
        newLending.setLendedArticle(lendedArticle);
        newLending.setEndDate(endDate);
        newLending.setStartDate(startDate);
        lendings.save(newLending);
    }
    public void CheckDecision(HashMap<String, String> postBodyParas, LendingRepository lendings, ArticleRepository articles, UserRepository users, ReservationRepository reservations) {
        if(postBodyParas.containsKey("choice")) {
            if (postBodyParas.get("choice").equals("accept")) {
                //Deposit check and lock depositamount

                APIProcessor apiProcessor = new APIProcessor();
                Lending lending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
                Article article = lending.getLendedArticle();

                try {
                    Account lendingAccount = apiProcessor.getAccountInformationWithId(lending.getLendingPerson().getUserID(), users);
                    if(apiProcessor.hasEnoughMoneyForDeposit(lendingAccount, article.getArticleID(), articles)){
                        //make reservation
                        Reservation reservation = apiProcessor.postCreateReservation(Reservation.class, lendingAccount, article);
                        //remove request out off article leave lending perosn behind
                        reservations.save(reservation);
                        article.setRequestComment("");
                        article.setRequested(false);
                        articles.save(article);
                        //set isAccepted true
                        lending.setProPayReservation(reservation);
                        lending.setAccepted(true);
                        lendings.save(lending);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO: reservierung konnte nicht angenommen werden
                }

            } else {
                CleanUpLending(postBodyParas, lendings, articles);
            }
        }else if(postBodyParas.containsKey("choicereturn")){
            APIProcessor apiProcessor = new APIProcessor();
            Lending lending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
            Article article = lending.getLendedArticle();
            Account lendingAccount = apiProcessor.getAccountInformationWithId(lending.getLendingPerson().getUserID(), users);
            double amount = CalculateLendingPrice(lending, article);
            System.out.println(apiProcessor.postTransfer(String.class,lendingAccount, article, amount));
            if (postBodyParas.get("choicereturn").equals("accept")) {

                //TODO: bezahlen und kaution zurück.
                CleanUpLending(postBodyParas, lendings, articles);
            } else {
                //TODO: bezahlen und warnstelle
            }
        }
    }

    public void CleanUpLending(HashMap<String, String> postBodyParas, LendingRepository lendings, ArticleRepository articles) {
        Lending lending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
        Article article = lending.getLendedArticle();
        //remove request out off article
        article.setRequestComment("");
        article.setRequested(false);
        article.setLendingUser(null);
        article.setAvailable(true);
        articles.save(article);
        //remove lending
        lendings.delete(lending);
    }
    public void initializeNewReturn(HashMap<String, String> postBodyParas, LendingRepository lendings) {
        Lending lending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
        lending.setReturn(true);
        lendings.save(lending);
    }
    public double CalculateLendingPrice(Lending lending, Article article){
        Calendar currentDate = Calendar.getInstance();
        Calendar startDate = lending.getStartDate();
        long time = currentDate.getTime().getTime() - startDate.getTime().getTime();
        long days = Math.round( (double)time / (24. * 60.*60.*1000.) );
        return days * article.getRent() + 1.0;
    }
}
