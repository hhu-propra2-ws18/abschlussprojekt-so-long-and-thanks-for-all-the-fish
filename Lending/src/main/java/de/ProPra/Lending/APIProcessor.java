package de.ProPra.Lending;

import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Model.Account;
import de.ProPra.Lending.Model.User;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class APIProcessor {
    // This Class interacts with the ProPay Application

    public Account getAccountInformationWithId(long userID, UserRepository users) {
        Optional<User> user= users.findUserByuserID(userID);
        return getAccountInformation(user.get().getName(), Account.class);
    }

    public boolean hasEnoughMoneyForDeposit(){
        return true;
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
}
