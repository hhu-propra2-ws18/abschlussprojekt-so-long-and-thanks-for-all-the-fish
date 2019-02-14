package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Dataaccess.Repositories.*;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.Request;
import de.ProPra.Lending.Model.ReturnProcess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

public class PostProccessor {
    public static HashMap<String, String> SplitString(String postBody){
        HashMap<String, String> postBodyParas = new HashMap<>();
        String[] splittedPostBody = postBody.split("&");
        for (String para : splittedPostBody) {
            String[] splittedPara = para.split("=");
            postBodyParas.put(splittedPara[0], splittedPara[1]);
        }
        return postBodyParas;
    }
    public static void CreateNewEntryRequest(HashMap<String,String> postBodyParas, RequestRepository requests){
        try {

            Calendar startDate = Calendar.getInstance();
            String[] datePieces = postBodyParas.get("startDate").split("-");
            startDate.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1])-1, Integer.parseInt(datePieces[2]));
            Calendar endDate = Calendar.getInstance();
            datePieces = postBodyParas.get("endDate").split("-");
            startDate.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1])-1, Integer.parseInt(datePieces[2]));

            long requesterID = Long.parseLong(postBodyParas.get("requesterID"));
            long articleID = Long.parseLong(postBodyParas.get("articleID"));
            Request request = new Request(requesterID, articleID, postBodyParas.get("requestComment"), startDate, endDate);
            requests.save(request);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Things went wrong and not right, git gud");
            //TODO: falls es ned klappt redirect auf fehlerseite
        }
    }

    public static void CheckDecision(HashMap<String, String> postBodyParas, LendingRepository lendings, ArticleRepository articles, RequestRepository requests, ReturnProcessRepository returns) {
        if(postBodyParas.containsKey("choice")) {
            Request request = requests.findById(Long.valueOf(postBodyParas.get("requestID"))).get();
            if (postBodyParas.get("choice").equals("accept")) {
                Lending acceptedLending = new Lending();
                //fill new lending
                acceptedLending.setArticleID(request.getArticleID());

                acceptedLending.setEndDate(request.getEndDate());
                acceptedLending.setStartDate(request.getStartDate());

                acceptedLending.setLendingPersonID(request.getRequesterID());
                lendings.save(acceptedLending);
                requests.delete(request);
            } else {
                //set article to available add no lending
                Article article = articles.findById(request.getArticleID()).get();
                article.setAvailable(true);
                articles.save(article);
                //TODO: optional we inform the lending person
                requests.delete(request);

            }
        }else if(postBodyParas.containsKey("choicereturn")){
            if (postBodyParas.get("choicereturn").equals("accept")) {
                PostProccessor.RetunLendingFinal(postBodyParas, lendings, articles, returns);
            } else {
                //redirect to problemsolving queue
            }
        }
    }

    public static void RetunLendingFinal(HashMap<String, String> postBody, LendingRepository lendings, ArticleRepository articles, ReturnProcessRepository returns) { //TODO: ask for status
        Article article = articles.findById(returns.findById(Long.valueOf(postBody.get("returnID"))).get().getArticleID()).get();    //TODO: Kaution zurückgegeben Kosten bezahlen
        article.setAvailable(true);
        articles.save(article);
        lendings.delete(lendings.findById(Long.valueOf(postBody.get("lendingID"))).get());
        returns.delete(returns.findById(Long.valueOf(postBody.get("returnID"))).get());
    }

    public static void CreateNewReturnProccess(HashMap<String, String> postBodyParas, LendingRepository lendings, ArticleRepository articles, ReturnProcessRepository returns) {
        ReturnProcess returnProcess = new ReturnProcess();
        Lending lending = lendings.findById(Long.valueOf(postBodyParas.get("lendingID"))).get();
        lending.setWaitingForAnswer(true);
        returnProcess.setLendingID(lending.getLendingId());
        returnProcess.setLenderID(lending.getLendingPersonID());
        returnProcess.setArticleID(Long.parseLong(postBodyParas.get("articleID")));
        lendings.save(lending);
        returns.save(returnProcess);
    }
}
