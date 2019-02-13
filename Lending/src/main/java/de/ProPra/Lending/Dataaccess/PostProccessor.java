package de.ProPra.Lending.Dataaccess;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Dataaccess.Repositories.RequestRepository;
import de.ProPra.Lending.Model.Request;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(postBodyParas.get("startDate"));
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(postBodyParas.get("endDate"));
            long requesterID = Long.parseLong(postBodyParas.get("requesterID"));
            long articleID = Long.parseLong(postBodyParas.get("articleID"));
            Request request = new Request(false, requesterID, articleID, postBodyParas.get("requestComment"), startDate, endDate);
            requests.save(request);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Things went wrong and not right, git gud");
            //TODO: falls es ned klappt redirect auf fehlerseite
        }
    }

    public static void CheckDecision(HashMap<String, String> postBodyParas, LendingRepository lendings, ArticleRepository articles, RequestRepository requests) {
        System.out.println(postBodyParas);
    }
}
