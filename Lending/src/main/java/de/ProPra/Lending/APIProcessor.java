package de.ProPra.Lending;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Model.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class APIProcessor {
    // This Class interacts with the ProPay Application

    public Account getAccountInformationWithId(long userID, UserRepository users) {
        Optional<User> user= users.findUserByuserID(userID);
        return getAccountInformation(user.get().getName(), Account.class);
    }

    public <T> T getAccountInformation(String name, final Class<T> type) {
        final Mono<T> mono = WebClient
                .create()
                .get()
                .uri(builder ->
                        builder.scheme("http")
                                .host("localhost")
                                .port("8888")
                                .pathSegment("account", name)
                                // for trailing slash
                                .path("/")
                                .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToMono(type);
        return mono.block();
    }

    public boolean hasEnoughMoneyForDeposit(Account lenderAccountInformation, long articleID, ArticleRepository articles) {
        double amount = lenderAccountInformation.getAmount();
        double deposit = articles.findArticleByarticleID(articleID).get().getDeposit();
        if(amount >= deposit) {
            return true;
        }else{
            return false;
        }
    }

    public <T> T postCreateReservation(final Class<T> type, Account lendingAccount, Article article) {
        final Mono<T> mono = WebClient
                .create()
                .post()
                .uri(builder ->
                        builder.scheme("http")
                                .host("localhost")
                                .port("8888")
                                .pathSegment("reservation", "reserve", lendingAccount.getAccount(), article.getOwnerUser().getName())
                                // for trailing slash
                                .path("/")
                                .queryParam("amount", String.valueOf(article.getDeposit()))
                                .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToMono(type);
        return mono.block();
    }

    public <T> T postTransfer(final Class<T> type, Account lendingAccount, Article article, double amount) {
        final Mono<T> mono = WebClient
                .create()
                .post()
                .uri(builder ->
                        builder.scheme("http")
                                .host("localhost")
                                .port("8888")
                                .pathSegment("account", lendingAccount.getAccount(),"transfer", article.getOwnerUser().getName())
                                // for trailing slash
                                .path("/")
                                .queryParam("amount", amount)
                                .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToMono(type);
        return mono.block();
    }

    //   transfer     HttpPost post = new HttpPost("http://localhost:8888/account/+sourceAccount+/transfer/+targetAccount+");
    //        amount double;


    //   aufheben     HttpPost post = new HttpPost("http://localhost:8888/reservation/realese/+account+");
    //   reservationId    int64;

    //   bestrafen     HttpPost post = new HttpPost("http://localhost:8888/reservation/punish/+account+");
    //   reservationId    int64; TODO: konfliktstelle!!!!!!!!! ! ! ! ! !  ! !

}
