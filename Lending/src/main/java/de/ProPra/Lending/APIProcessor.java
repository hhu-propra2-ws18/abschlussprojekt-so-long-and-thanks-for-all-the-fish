package de.ProPra.Lending;

import de.ProPra.Lending.Dataaccess.PostProccessor;
import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.ReservationRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Model.Account;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Optional;

@Data
@NoArgsConstructor
public class APIProcessor {
    // This Class interacts with the ProPay Application
    private boolean errorOccurred = false;
    private HashMap<String, String> errorMessage = new HashMap<>();

    public Account getAccountInformationWithId(long userID, UserRepository users) {
        errorOccurred = false;
        Optional<User> user = users.findUserByuserID(userID);
        if (!user.isPresent()) {
            errorOccurred = true;
            errorMessage.put("reason", "User not found");
            return null;
        }
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
                .exchange()
                .flatMap(clientResponse -> ErrorHandling(type, clientResponse));
        return mono.block();
    }

    public boolean hasEnoughMoneyForDeposit(Account lenderAccountInformation, long articleID, ArticleRepository articles) {
        double amount = lenderAccountInformation.getAmount();
        double deposit = articles.findArticleByarticleID(articleID).get().getDeposit();
        if (amount >= deposit) {
            return true;
        } else {
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
                .exchange()
                .flatMap(clientResponse -> ErrorHandling(type, clientResponse));
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
                                .pathSegment("account", lendingAccount.getAccount(), "transfer", article.getOwnerUser().getName())
                                // for trailing slash
                                .path("/")
                                .queryParam("amount", amount)
                                .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .flatMap(clientResponse -> ErrorHandling(type, clientResponse));
        return mono.block();
    }

    public <T> T postMoney(final Class<T> type, Account lendingAccount, double amount) {
        final Mono<T> mono = WebClient
                .create()
                .post()
                .uri(builder ->
                        builder.scheme("http")
                                .host("localhost")
                                .port("8888")
                                .pathSegment("account", lendingAccount.getAccount())
                                // for trailing slash
                                .path("/")
                                .queryParam("amount", amount)
                                .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .flatMap(clientResponse -> ErrorHandling(type, clientResponse));
        return mono.block();
    }

    public <T> T punishOrRealeseReservation(final Class<T> type, Account lendingAccount, Article article, long id, String punishOrRelease) {
        final Mono<T> mono = WebClient
                .create()
                .post()
                .uri(builder ->
                        builder.scheme("http")
                                .host("localhost")
                                .port("8888")
                                .pathSegment("reservation", punishOrRelease, lendingAccount.getAccount())
                                // for trailing slash
                                .path("/")
                                .queryParam("reservationId", id)
                                .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .flatMap(clientResponse -> ErrorHandling(type, clientResponse));
        return mono.block();
    }

    public void PunishOrReleaseConflictingLending(HashMap<String, String> postBodyParas, LendingRepository lendings, UserRepository users, ArticleRepository articles, ReservationRepository reservations) {
        String lendingID = postBodyParas.get("lendingID");
        String decision = postBodyParas.get("decision");
        Lending conflictingLending = lendings.findLendingBylendingID(Long.parseLong(lendingID)).get();
        Account lendingAccount = getAccountInformationWithId(conflictingLending.getLendingPerson().getUserID(), users);
        Article conflictingArticle = conflictingLending.getLendedArticle();
        if (decision.equals("true")) {
            punishOrRealeseReservation(Account.class, lendingAccount, conflictingArticle, conflictingLending.getProPayReservation().getId(), "punish");
        } else {
            punishOrRealeseReservation(Account.class, lendingAccount, conflictingArticle, conflictingLending.getProPayReservation().getId(), "release");
        }

        // Delete Lending and Reservation
        PostProccessor postProccessor = new PostProccessor();
        postProccessor.CleanUpLending(postBodyParas, lendings, articles);
        reservations.delete(conflictingLending.getProPayReservation());
    }

    private <T> Mono<? extends T> ErrorHandling(Class<T> type, ClientResponse clientResponse) {
        errorOccurred = false;
        if (clientResponse.statusCode().is5xxServerError() || clientResponse.statusCode().is4xxClientError() || clientResponse.statusCode().is3xxRedirection()) {
            clientResponse.body((clientHttpResponse, context) -> {
                System.out.println("ERROR ---------------------------------------------->");
                //Fill error Informations for error Page
                errorOccurred = true;
                errorMessage.put("reason", clientHttpResponse.getStatusCode().getReasonPhrase());
                System.out.println("error message: " + errorMessage.get("reason"));

                return clientHttpResponse.getBody();
            });
        } else {
            System.out.println(clientResponse.statusCode());
            return clientResponse.bodyToMono(type);
        }
        return clientResponse.bodyToMono(type);
    }


}
